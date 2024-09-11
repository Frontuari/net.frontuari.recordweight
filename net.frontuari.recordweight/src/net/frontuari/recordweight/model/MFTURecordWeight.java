/**
 * 
 */
package net.frontuari.recordweight.model;

import java.io.File;
import java.lang.System;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCost;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLandedCost;
import org.compiere.model.MOrderLandedCostAllocation;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPeriod;
import org.compiere.model.MPriceList;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.model.MProductionLine;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.model.X_M_AttributeSet;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

import net.frontuari.custom.model.FTUMInOut;
/**
 *
 */
public class MFTURecordWeight extends X_FTU_RecordWeight implements DocAction, DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = -35234234232322223L;

	/**
	 * *** Constructor ***
	 * 
	 * @param ctx
	 * @param FTU_RecordWeight_ID
	 * @param trxName
	 */
	public MFTURecordWeight(Properties ctx, int FTU_RecordWeight_ID, String trxName) {
		super(ctx, FTU_RecordWeight_ID, trxName);
	}

	/**
	 * *** Constructor ***
	 * 
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTURecordWeight(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * Get Document Info
	 * 
	 * @return document info (untranslated)
	 */
	public String getDocumentInfo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	} // getDocumentInfo

	/**
	 * Create PDF
	 * 
	 * @return File or null
	 */
	public File createPDF() {
		try {
			File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
			return createPDF(temp);
		} catch (Exception e) {
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	} // getPDF

	/**
	 * Create PDF file
	 * 
	 * @param file output file
	 * @return file if success
	 */
	public File createPDF(File file) {
		return null;
	} // createPDF

	/**************************************************************************
	 * Process document
	 * 
	 * @param processAction document action
	 * @return true if performed
	 */
	public boolean processIt(String processAction) {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(processAction, getDocAction());
	} // processIt

	/** Process Message */
	private String m_processMsg = null;
	/** Just Prepared Flag */
	private boolean m_justPrepared = false;

	private BigDecimal m_Valideight = null;

	/**
	 * Unlock Document.
	 * 
	 * @return true if success
	 */
	public boolean unlockIt() {
		log.info("unlockIt - " + toString());
		return true;
	} // unlockIt

	/**
	 * Invalidate Document
	 * 
	 * @return true if success
	 */
	public boolean invalidateIt() {
		log.info("invalidateIt - " + toString());
		return true;
	} // invalidateIt

	/**
	 * Prepare Document
	 * 
	 * @return new status (In Progress or Invalid)
	 */
	public String prepareIt() {
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		MPeriod.testPeriodOpen(getCtx(), getDateDoc(), getC_DocType_ID(), getAD_Org_ID());
		
		if(!getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts)
				&& !getOperationType().equals(OPERATIONTYPE_MultipleProductMovement))
			m_processMsg = validETReferenceDuplicated();
		
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
			
		MFTUEntryTicket entryTicket = (MFTUEntryTicket) getFTU_EntryTicket();
		int p_HRS_Analysis_ID = MHRSAnalysis.getByEntryTicket(entryTicket.getFTU_EntryTicket_ID());
		if(p_HRS_Analysis_ID > 0) {
			if(getHRS_Analysis_ID() <= 0)
				setHRS_Analysis_ID(p_HRS_Analysis_ID);
			MProduct product = (MProduct) entryTicket.getM_Product();
			if(product == null
					|| product.get_ID() <= 0)
				product = (MProduct) getHRS_Analysis().getM_Product();
			MAttributeSet attSet = null;
			if(product.getM_AttributeSet_ID() > 0) {
				attSet = (MAttributeSet) product.getM_AttributeSet();
				if(attSet != null) {
					if((getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)
							|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial))) {
						boolean isMandatory = attSet.getMandatoryType().equals(X_M_AttributeSet.MANDATORYTYPE_AlwaysMandatory);
						if((isMandatory
								&& getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial))
									|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)) {
							setProductAttribute(true);
							setIsValidAnalysis(getHRS_Analysis().isValidAnalysis());
							saveEx();
						}
					}
				}
			}			
		}
		
		// Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	} // prepareIt

	/**
	 * Approve Document
	 * 
	 * @return true if success
	 */
	public boolean approveIt() {
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	} // approveIt

	/**
	 * Reject Approval
	 * 
	 * @return true if success
	 */
	public boolean rejectIt() {
		log.info("rejectIt - " + toString());
		
		MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
		
		if(et.get_ValueAsInt("DD_Order_ID") > 0) {
			setVoidItToMMovmenete(et);
		}
		
		setIsApproved(false);
		return true;
	} // rejectIt

	/**
	 * Complete Document
	 * 
	 * @return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt() {
		// Re-Check
		if (!m_justPrepared) {
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		
		MDocType doc = (MDocType) getC_DocType();
		
		if (doc.get_ValueAsBoolean("RequiresQualityAnalysis")) {
			if (getHRS_Analysis_ID()<=0) {
				m_processMsg = "Requiere un Análisis de Calidad válido o aprobación para poder completar el documento";
				return DocAction.STATUS_WaitingConfirmation;
			}
		}
		
		//	Added by Jorge Colmenarez, 2021-06-30 10:36
		//	Support for Validate NetWeigth Tolerance
		//David castillo 2021-08-05 added percentage tolerance
		int tolerance = MSysConfig.getIntValue("FTU_RW_TOLERANCE", 0,getAD_Client_ID());
		int tolerancePercentage = MSysConfig.getIntValue("FTU_RW_TOLERANCE_PRC", 0,getAD_Client_ID());
		if((getOperationType().equalsIgnoreCase(OPERATIONTYPE_RawMaterialReceipt) || getOperationType().equalsIgnoreCase("IRM")) && !isApproved())
		{
			BigDecimal oNetWeight = (BigDecimal) get_Value("OriginNetWeight");
			BigDecimal difference = getNetWeight().subtract(oNetWeight);
			if(difference.compareTo(BigDecimal.valueOf(tolerance).negate()) == -1 
					|| difference.compareTo(BigDecimal.valueOf(tolerance)) == 1)
			{
				//	Added by Jorge Colmenarez, 2021-11-04 14:53
				//	Support for write QtyDifference
				DB.executeUpdate("UPDATE FTU_RecordWeight SET DifferenceQty="+difference+" WHERE FTU_RecordWeight_ID = ?", get_ID(), get_TrxName());
				//	End Jorge Colmenarez
				m_processMsg = "El peso neto ["+getNetWeight()+"] no puede exceder la carga origen ["+oNetWeight+"], diferencia= "+difference+", tolerancia = "+tolerance+" se requiere una autorizacion.";
				return DocAction.STATUS_WaitingConfirmation;
			}
		}
		if(getOperationType().equalsIgnoreCase(OPERATIONTYPE_DeliveryFinishedProduct) && !isApproved())
		{
			BigDecimal oNetWeight = getFTU_LoadOrder().getWeight();
			
			BigDecimal toleranceAmt = (new BigDecimal(tolerancePercentage).divide(Env.ONEHUNDRED, 2,RoundingMode.HALF_UP));
			toleranceAmt = oNetWeight.multiply(toleranceAmt);
			BigDecimal difference = getNetWeight().subtract(oNetWeight);
			if(difference.compareTo(toleranceAmt.negate()) == -1 
					|| difference.compareTo(toleranceAmt) == 1)
			{
				//	Added by Jorge Colmenarez, 2021-11-04 14:53
				//	Support for write QtyDifference
				DB.executeUpdate("UPDATE FTU_RecordWeight SET DifferenceQty="+difference+" WHERE FTU_RecordWeight_ID = ?", get_ID(), get_TrxName());
				//	End Jorge Colmenarez
				m_processMsg = "El peso neto ["+getNetWeight()+"] no puede exceder la capacidad de carga ["+oNetWeight+"], diferencia= "+difference+", tolerancia = ["+tolerancePercentage+"% = "+toleranceAmt+"] se requiere una autorizacion.";
				return DocAction.STATUS_WaitingConfirmation;
			}
		}
		//	End Jorge Colmenarez
		//	Added By Jorge Colmenarez, 2023-07-15 13:57 Support to validate on MaterialInputMovement
		if(getOperationType().equalsIgnoreCase(OPERATIONTYPE_MaterialInputMovement))
		{
			BigDecimal maxtolerance = MSysConfig.getBigDecimalValue("RECORDWEIGHT_TOLERANCE_DOWNMAX", BigDecimal.ZERO, getAD_Client_ID(), getAD_Org_ID());
			BigDecimal oNetWeight = getOriginNetWeight();
			BigDecimal difference = getNetWeight().subtract(oNetWeight);
			//	Modified by Jorge Colmenarez, 2023-09-26 16:21
			//	Support for set DifferenceQty
			setDifferenceQty(difference);
			saveEx();
			//DB.executeUpdate("UPDATE FTU_RecordWeight SET DifferenceQty="+difference+" WHERE FTU_RecordWeight_ID = ?", get_ID(), get_TrxName());
			//	End Jorge Colmenarez
			if(difference.compareTo(maxtolerance.negate()) == -1 && !isApproved())
			{
				//	Added by Jorge Colmenarez, 2021-11-04 14:53
				//	Support for write QtyDifference
				//	End Jorge Colmenarez
				m_processMsg = "El peso neto ["+getNetWeight()+"] no puede exceder la capacidad de carga ["+oNetWeight+"], diferencia= "+difference+", tolerancia = ["+maxtolerance+"] se requiere una autorizacion.";
				return DocAction.STATUS_WaitingConfirmation;
			}
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		// Valid Weight
		String status = validWeight();
		if (m_processMsg != null)
			return status;

		// Valid Weight
		boolean isValidWeight = true;
		if (getOperationType().contentEquals(OPERATIONTYPE_OtherRecordWeight)) {
			if (getNetWeight() == null) {
				isValidWeight = false;
				m_processMsg = "@NetWeight@ null";
				return DocAction.STATUS_Invalid;
			}
		}else {
			if (getNetWeight() == null 
				|| getNetWeight().doubleValue() == 0) {
			isValidWeight = false;
			m_processMsg = "@NetWeight@ <= 0";
			return DocAction.STATUS_Invalid;
			}
		}
		 
		// User Validation
		m_processMsg = updateLoadOrder();
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		// Implicit Approval
		if (!isApproved())
			approveIt();

		if ((getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)) 
					&& isValidWeight)
			m_processMsg = calculatePayWeight();
		
		boolean withDDOrder = false;		

		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		if (getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)) {
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.get_ValueAsInt("DD_Order_ID") > 0) {
				withDDOrder = true;
				String result = createMMovement(et);
				if( result != null) {
					throw new AdempiereException(result);
				}
			}
		}
		//	Added by Jorge Colmenarez, 2024-05-21 17:28
		//	Support for update ProductionQty by Product
		if(getOperationType().equals(OPERATIONTYPE_ProductionInMovement)) {
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			int productID = getM_Product_ID() > 0 ? getM_Product_ID() : et.getM_Product_ID();
			if (et.get_ValueAsInt("M_Production_ID") > 0) {
				MProduction p = new MProduction(getCtx(), et.get_ValueAsInt("M_Production_ID"), get_TrxName());
				for(MProductionLine line : p.getLines()) {
					if (line.getM_Product_ID() == productID) {
                        MWarehouse w = (MWarehouse)getM_Warehouse();
                        int locatorID = w.getDefaultLocator() != null ? w.getDefaultLocator().get_ID() : line.getM_Locator_ID();
                        line.setIsEndProduct(true);
                        line.setM_Locator_ID(locatorID);
                        line.setQtyUsed(getNetWeight());
                        line.set_ValueOfColumn("FTU_RecordWeight_ID", get_ID());
                        line.saveEx();
                     }
				}
			}
		}
		//	End Jorge Colmenarez
		
		boolean isGenerateInOut = MSysConfig.getBooleanValue("FTU_GENERATE_INOUT", false, getAD_Client_ID());
		boolean isGenerateMovement = MSysConfig.getBooleanValue("FTU_GENERATE_MOVEMENT", false, getAD_Client_ID());

		if ((getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
				//	Modified by Jorge Colmenarez, 2024-03-16 11:07
				//	Support for generate InOut when Operation Type it's Delivery Multiple Products
				|| getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts)
				//	End Jorge Colmenarez
				|| getOperationType().equals(OPERATIONTYPE_ProductBulkReceipt)) && isValidWeight && isGenerateInOut && !withDDOrder) {
			//	Added by Jorge Colmenarez, 2024-02-06 11:14
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			String msg = null;
			if(et.isGenLandedCost()) {
				msg = createFreightCost(et);
			}
			//	End Jorge Colmenarez
			// Generate Material Receipt
			msg += createInOut();
			//
			if (m_processMsg != null)
				return DocAction.STATUS_Invalid;
			else
				m_processMsg = msg;
		}
		
		if (OPERATIONTYPE_DeliveryFinishedProduct.equals(getOperationType()))
		{
			 MFTULoadOrder loadOrder = (MFTULoadOrder) getFTU_LoadOrder();
			 
			 if (loadOrder.isImmediateDelivery())
			 {
				 MInOut [] inouts = loadOrder.getInOutFromLoadOrder(loadOrder.get_ID());
				 
				 for (MInOut inout: inouts)
				 {
					 inout.set_ValueNoCheck(COLUMNNAME_FTU_RecordWeight_ID, get_ID());
					 inout.saveEx();
				 } 
			 }
		}
		// Add support for generating inventory movements
		else if ((getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				|| getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)
				|| getOperationType().equals(OPERATIONTYPE_ProductionOutMovement)) 
				&& isGenerateMovement) {
			String msg = createMovement();
			if (m_processMsg != null)
				return DocAction.STATUS_Invalid;
			else
				m_processMsg = msg;
		}
		// Add support for generating inventory movements inputs
		else if (getOperationType().equals(OPERATIONTYPE_MaterialInputMovement) && isGenerateMovement) {
			String msg = createMovementInput((MFTUEntryTicket) getFTU_EntryTicket());
			//	Added by Jorge Colmenarez, 2024-02-06 11:14
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.isGenLandedCost()) {
				msg += createFreightCost(et);
				int ofcID = DB.getSQLValue(get_TrxName(), 
						"SELECT MAX(fcl.C_Order_ID) FROM FTU_FreightCostLine fcl JOIN FTU_FreightCost fc ON fcl.FTU_FreightCost_ID = fc.FTU_FreightCost_ID WHERE fc.DocStatus = 'CO' AND fc.FTU_EntryTicket_ID = ? AND fcl.FTU_RecordWeight_ID = ? ", 
						new Object[] {getFTU_EntryTicket_ID(), getFTU_RecordWeight_ID()});
				if(ofcID>0) {
					MOrder o = new MOrder(getCtx(), ofcID, get_TrxName());
					MDocType dt = new MDocType(getCtx(), o.getC_DocType_ID(), get_TrxName());
					if(dt.get_ValueAsInt("C_DocTypeCostAdjustment_ID")<=0) {
						m_processMsg = "Ocurrio un error al crear el ajuste al costo: Tipo de documento no configurado en la orden de compra de flete: "+o.getDocumentInfo();
						return DocAction.STATUS_Invalid;
					}
					MDocType dtac = new MDocType(getCtx(), dt.get_ValueAsInt("C_DocTypeCostAdjustment_ID"), get_TrxName());
					//	Create Cost Adjustment
					MInventory ac = new MInventory(getCtx(), 0, get_TrxName());
					ac.setAD_Org_ID(getAD_Org_ID());
					ac.setC_DocType_ID(dtac.get_ID());
					if(dtac.get_ValueAsBoolean("IsCostAdjustmentToDate"))
						ac.set_ValueOfColumn("IsCostAdjustmentToDate", true);
					ac.setDescription("Creado desde: "+o.getDocumentInfo());
					ac.setC_Currency_ID(o.getC_Currency_ID());
					ac.setC_ConversionType_ID(o.getC_ConversionType_ID());
					MProduct p = new MProduct(getCtx(), getM_Product_ID(), get_TrxName());
					int asID = DB.getSQLValue(get_TrxName(), "SELECT C_AcctSchema_ID FROM C_AcctSchema WHERE C_Currency_ID = ? AND AD_Client_ID = ?", o.getC_Currency_ID(), getAD_Client_ID());
					if(asID<=0) {
						m_processMsg = "No existe esquema contable para la moneda: "+o.getC_Currency().getISO_Code();
						return DocAction.STATUS_Invalid;
					}
					MAcctSchema as = new MAcctSchema(getCtx(), asID,get_TrxName());
					String costingMethod = p.getCostingMethod(as);
					ac.setCostingMethod(costingMethod);
					ac.setMovementDate(o.getDateOrdered());
					ac.set_ValueOfColumn("C_Order_ID", o.get_ID());
					ac.setDocStatus(MInventory.STATUS_Drafted);
					ac.setDocAction(MInventory.ACTION_Complete);
					ac.saveEx();
					//	Create Cost Adjustment Line
					MInventoryLine acl = new MInventoryLine(getCtx(),0,get_TrxName());
					acl.setM_Inventory_ID(ac.get_ID());
					acl.setAD_Org_ID(ac.getAD_Org_ID());
					acl.setLine(10);
					acl.setM_Product_ID(p.get_ID());
					acl.setM_Locator_ID(0);
					acl.setC_Charge_ID(et.getC_Charge_ID());
					MCost cost = p.getCostingRecord(as, getAD_Org_ID(), 0, costingMethod);
					acl.setCurrentCostPrice(cost.getCurrentCostPrice());
					String costingLevel = p.getCostingLevel(as);
					String whereClause = "";
					if (MAcctSchema.COSTINGLEVEL_Client.equals(costingLevel))
						whereClause="";
					else if (MAcctSchema.COSTINGLEVEL_Organization.equals(costingLevel))
						whereClause=" AND AD_Org_ID = "+getAD_Org_ID();
					else if (MAcctSchema.COSTINGLEVEL_BatchLot.equals(costingLevel))
						whereClause=" AND M_AttributeSetInstance_ID = "+acl.getM_AttributeSetInstance_ID();
					//	Get Storage
					BigDecimal storage = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(QtyOnHand) FROM M_StorageOnHand WHERE M_Product_ID = ?"+whereClause, p.get_ID());
					if(dtac.get_ValueAsBoolean("IsCostAdjustmentToDate"))
						acl.set_ValueOfColumn("QtyAvailable",storage);
					log.warning("Storage= "+storage+ " Current Cost Price="+cost.getCurrentCostPrice());
					log.warning("Total Freight Cost = "+o.getGrandTotal());
					BigDecimal addCost = o.getGrandTotal().divide(storage,10,RoundingMode.HALF_UP);
					log.warning("Cost Adjustment= "+addCost);
					BigDecimal newCostPrice = cost.getCurrentCostPrice().add(addCost);
					log.warning("New Cost Price="+newCostPrice);
					acl.setNewCostPrice(newCostPrice);
					acl.saveEx();
					//	Complete Adjustment
					if(!ac.processIt(MInventory.ACTION_Complete)) {
						m_processMsg = ac.getProcessMsg();
					}
					ac.saveEx();
					msg += " [Ajuste de Costo por Transferencia: "+ac.getDocumentInfo()+"]";
				}
			}
			//	End Jorge Colmenarez
			if (m_processMsg != null)
				return DocAction.STATUS_Invalid;
			else
				m_processMsg = msg;
		}
		
		/*
		 * Check if M_InOut Related is completed
		 */
		
		int InOutsPending = 0;
		
		String sql = "SELECT COUNT(M_InOut_ID) FROM M_InOut where IsSOTrx = 'Y' AND DocStatus NOT IN ('CO','CL','VO','RE') AND FTU_RecordWeight_ID = " + getFTU_RecordWeight_ID();
		
		InOutsPending = DB.getSQLValue(get_TrxName(), sql);
		if (InOutsPending > 0) {
			m_processMsg = m_processMsg + " No puede ser completado debido a que no se dispone de inventario suficiente";
			return DocAction.STATUS_Invalid;
		}
		
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null) {
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		setDefiniteDocumentNo();
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		
		createPDF();
		return DocAction.STATUS_Completed;
	} // completeIt

	/**
	 * Update Values for Load Order
	 * @author Jorge Colmenarez, 2021-06-07 14:46, jcolmenarez@frontuari.net
	 * Support for update Load Order when Operation Type it's DMP
	 * @return String
	 */
	private String updateLoadOrder() {
		// Valid Operation Type
		if (!getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
				&& !getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)
				&& !getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				//	Added by Jorge Colmenarez
				&& !getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)
				&& !getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts))
			return null;
		// Get Orders From Load Order
		MFTULoadOrder lo = (MFTULoadOrder) getFTU_LoadOrder();
		if (lo == null || lo.get_ID() <= 0) {
			return "@FTU_LoadOrder_ID@ @NotFound@";
		}
		//	Added by Jorge Colmenarez
		//	Apply validations by products
		
		BigDecimal confirmedWeight = getNetWeight();
		Boolean firstUpdate = false;
		if(getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts))
		{
			for(MFTULoadOrderLine line : lo.getLines(true, " IsConfirmed = 'N' AND M_Product_ID = "+getM_Product_ID()))
			{
					line.setConfirmedWeight(confirmedWeight);
					line.setIsConfirmed(true);
					line.saveEx();
					firstUpdate = true;
			}
			if (!firstUpdate) {
				for(MFTULoadOrderLine line : lo.getLines(true, " IsConfirmed = 'Y' AND ConfirmedWeight > 0 AND M_Product_ID = "+getM_Product_ID())) {
						line.setConfirmedWeight(line.getConfirmedWeight().add(confirmedWeight));
						line.saveEx();
						firstUpdate = false;
				}
			}
		}
		
		//
		if(getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts))
		{
			MFTULoadOrderLine[] lines = lo.getLines(true, " IsConfirmed = 'N'");
			if(lines.length == 0)
			lo.setIsWeightRegister(true);
			lo.setConfirmedWeight(lo.getConfirmedWeight().add(getNetWeight()));
		}
		else
		{
			lo.setIsWeightRegister(true);
			lo.setConfirmedWeight(getNetWeight());
		}
		// Save
		lo.saveEx(get_TrxName());
		//
		return null;
	}

	/**
	 * Caluculate Payment Weight
	 * 
	 * @return
	 * @return String
	 */
	private String calculatePayWeight() {
		setPayWeight(getNetWeight());
		return null;
	}

	/**
	 * Valid Weight
	 * 
	 * @return
	 * @return String
	 */
	private String validWeight() {
		// Valid Weight
		if ((getGrossWeight() == null || getGrossWeight().compareTo(Env.ZERO) == 0)) {
			m_processMsg = "@GrossWeight@ = @0@";
			return (!isSOTrx() ? DocAction.STATUS_Invalid : DocAction.STATUS_InProgress);
		} else if ((getTareWeight() == null || getTareWeight().compareTo(Env.ZERO) == 0)) {
			m_processMsg = "@TareWeight@ = @0@";
			return (!isSOTrx() ? DocAction.STATUS_InProgress : DocAction.STATUS_Invalid);
		} else if (getNetWeight().compareTo(Env.ZERO) < 0 && !getOperationType().equalsIgnoreCase(OPERATIONTYPE_OtherRecordWeight)) {
			m_processMsg = "@NetWeight@ < @0@";
			return DocAction.STATUS_Invalid;
		}
		return null;
	}

	/**
	 * Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1) // get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	/**
	 * Void Document. Same as Close.
	 * 
	 * @return true if success
	 */
	public boolean voidIt() {
		log.info("voidIt - " + toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		//
		if (getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				|| getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)) {
			// Valid QualityAnalysis Reference
			m_processMsg = validQAReference();
			if (m_processMsg != null)
				return false;
			// Valid Mobilization Guide Reference
			m_processMsg = validMGReference();
			if (m_processMsg != null)
				return false;
		}
		// Reverse only M_InOut record
		if (!getOperationType().equals(OPERATIONTYPE_MaterialInputMovement)
				&& !getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				&& !getOperationType().equals(OPERATIONTYPE_OtherRecordWeight)
				&& !getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)
				&& !getOperationType().equals("IRM")) {
			//	Added by Jorge Colmenarez, 2024-02-06 15:29
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.isGenLandedCost()) {
				try {
					reverseFreightCost();
				} catch (Exception e) {
					m_processMsg = e.getLocalizedMessage();
				}
			}
			//	End Jorge Colmenarez
			// Reverse In/Out
			m_processMsg = reverseInOut();
			if (m_processMsg != null)
				return false;
		}
		// Add support for reactivate Movement
		else if (getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				|| getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)) {
			// Reverse Movement
			m_processMsg = reverseMovement();
			if (m_processMsg != null)
				return false;
		}
		// Add support for reactivate Input Movement
		else if (getOperationType().equals(OPERATIONTYPE_MaterialInputMovement)) {
			//	Added by Jorge Colmenarez, 2024-02-06 15:29
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.isGenLandedCost()) {
				try {
					reverseFreightCost();
				} catch (Exception e) {
					m_processMsg = e.getLocalizedMessage();
				}
			}
			//	End Jorge Colmenarez
			// Reverse Movement
			m_processMsg = reverseInputMovement();
			if (m_processMsg != null)
				return false;
		}
		//
		addDescription(Msg.getMsg(getCtx(), "Voided"));
		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;
		
		MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
		
		if(et.get_ValueAsInt("DD_Order_ID") > 0 && !getOperationType().equals("IRM")) {
			setVoidItToMMovmenete(et);
		}
		
		//Added  by david castillo 21/11/2022 delete weighted qty
		if(getFTU_LoadOrder_ID() > 0) {
			MFTULoadOrder lo = (MFTULoadOrder) getFTU_LoadOrder();
			if (lo == null || lo.get_ID() <= 0) {
				m_processMsg = m_processMsg + " @FTU_LoadOrder_ID@ @NotFound@";
				return false;
			}
			//	Added by Jorge Colmenarez
			//	Apply validations by products
			
			BigDecimal confirmedWeight = getNetWeight();
			for(MFTULoadOrderLine line : lo.getLines(true, " IsConfirmed = 'N' AND M_Product_ID = "+getM_Product_ID()))
			{
					line.setConfirmedWeight(line.getConfirmedWeight().subtract(confirmedWeight));
					if (line.getConfirmedWeight().compareTo(Env.ZERO)==0)
					line.setIsConfirmed(false);
					
					line.saveEx();
	
			}
		}
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	} // voidIt

	/**
	 * Close Document. Cancel not delivered Qunatities
	 * 
	 * @return true if success
	 */
	public boolean closeIt() {
		log.info("closeIt - " + toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;
		// Add Support for closed Load Order

		setProcessed(true);
		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;

		return true;
	} // closeIt

	/**
	 * Reverse Correction
	 * 
	 * @return true if success
	 */
	public boolean reverseCorrectIt() {
		log.info("reverseCorrectIt - " + toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		// Void It
		voidIt();
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		return false;
	} // reverseCorrectionIt

	/**
	 * Reverse Accrual - none
	 * 
	 * @return true if success
	 */
	public boolean reverseAccrualIt() {
		log.info("reverseAccrualIt - " + toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		// Void It
		voidIt();
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		return false;
	} // reverseAccrualIt

	/**
	 * Re-activate
	 * 
	 * @return true if success
	 */
	public boolean reActivateIt() {
		log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		//
		// Reverse only M_InOut record
		if (!getOperationType().equals(OPERATIONTYPE_MaterialInputMovement)
				&& !getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				&& !getOperationType().equals(OPERATIONTYPE_OtherRecordWeight)
				&& !getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)
				&& !getOperationType().equals(OPERATIONTYPE_ProductionInMovement)
				&& !getOperationType().equals(OPERATIONTYPE_ProductionOutMovement)) {
			//	Added by Jorge Colmenarez, 2024-02-06 15:29
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.isGenLandedCost()) {
				try {
					reverseFreightCost();
				} catch (Exception e) {
					m_processMsg = e.getLocalizedMessage();
				}
			}
			//	End Jorge Colmenarez
			m_processMsg = reverseInOut();
			if (m_processMsg != null)
				return false;
		}

		// Add support for reactivate Movement
		else if (getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				|| getOperationType().equals(OPERATIONTYPE_ProductionOutMovement)) {
			// Reverse Movement
			m_processMsg = reverseMovement();
			if (m_processMsg != null)
				return false;
		}
		// Add support for reactivate Movement
		else if (getOperationType().equals(OPERATIONTYPE_MaterialInputMovement)) {
			// Reverse Movement
			m_processMsg = reverseInputMovement();
			//	Added by Jorge Colmenarez, 2024-02-06 15:29
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.isGenLandedCost()) {
				try {
					reverseFreightCost();
				} catch (Exception e) {
					m_processMsg = e.getLocalizedMessage();
				}
			}
			//	End Jorge Colmenarez
			if (m_processMsg != null)
				return false;
		}
		
		//	Added by Jorge Colmenarez, 2021-06-07 15:37
		if(getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts) || getOperationType().equals(OPERATIONTYPE_MultipleProductMovement) )
		{
			MFTULoadOrder lo = new MFTULoadOrder(getCtx(), getFTU_LoadOrder_ID(), get_TrxName());
			for(MFTULoadOrderLine line : lo.getLines(true, " IsConfirmed = 'Y' AND M_Product_ID = "+getM_Product_ID()))
			{
				//	Added by Jorge Colmenarez, 2021-07-29 12:02
				//	Support for Reactivate when has confirmend weight more that 0
				BigDecimal newConfirmedWeight =line.getConfirmedWeight().subtract(getNetWeight());
				if(newConfirmedWeight.compareTo(BigDecimal.ZERO)  <= 0)
					newConfirmedWeight = BigDecimal.ZERO;
				line.setConfirmedWeight(newConfirmedWeight);
				//	End Jorge Colmenarez
				line.setIsConfirmed(false);
				line.saveEx(get_TrxName());
			}
			BigDecimal confirmedWeight = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(confirmedWeight) FROM FTU_LoadOrderLine WHERE FTU_LoadOrder_ID = ? AND IsConfirmed = 'Y'", getFTU_LoadOrder_ID());
			if(confirmedWeight == null)
				confirmedWeight = BigDecimal.ZERO;
			
			lo.setConfirmedWeight(confirmedWeight);
			lo.saveEx(get_TrxName());
		}
		//	End Jorge Colmenarez
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
		
		if(et.get_ValueAsInt("DD_Order_ID") > 0) {
			setVoidItToMMovmenete(et);
		}

		setFTU_WeightApprovalMotive_ID(-1);
		// setHRS_Analysis_ID(0);
		setIsApproved(false);
		setIsPrinted(false);
		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	} // reActivateIt

	/**
	 * Valid Reference in another record
	 * 
	 * @return
	 * @return String
	 */
	private String validInOutReference() {
		String m_ReferenceNo = DB
				.getSQLValueString(
						get_TrxName(), "SELECT MAX(re.DocumentNo) FROM M_InOut re "
								+ "WHERE re.DocStatus NOT IN('VO', 'RE') " + "AND re.FTU_RecordWeight_ID = ?",
						getFTU_RecordWeight_ID());
		if (m_ReferenceNo != null)
			throw new AdempiereException("@SQLErrorReferenced@ @M_InOut_ID@: " + m_ReferenceNo);
		return null;
	}

	/**
	 * Valid Mobilization Guide reference
	 * 
	 * @return
	 * @return String
	 */
	private String validMGReference() {
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(),
				"SELECT MAX(mg.DocumentNo)  FROM FTU_MobilizationGuide mg "
						+ "WHERE mg.DocStatus NOT IN('VO', 'RE') AND mg.FTU_RecordWeight_ID = ?",
				getFTU_RecordWeight_ID());
		if (m_ReferenceNo != null)
			return "@SQLErrorReferenced@ @FTU_MobilizationGuide_ID@: " + m_ReferenceNo;
		return null;
	}

	/**
	 * Valid Reference in Quality Analysis
	 * 
	 * @return
	 * @return String
	 */
	private String validQAReference() {
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(),
				"SELECT MAX(qa.DocumentNo) FROM HRS_Analysis qa "
						+ "WHERE qa.DocStatus NOT IN('VO', 'RE') AND qa.FTU_EntryTicket_ID = ?",
				getFTU_EntryTicket_ID());
		if (m_ReferenceNo != null)
			return "@SQLErrorReferenced@ @FTU_QualityAnalysis_ID@: " + m_ReferenceNo;
		return null;
	}

	/**
	 * Reverse Receipt / Delivery
	 * 
	 * @return
	 * @return String
	 */
	private String reverseInOut() {
		MFTULoadOrder m_FTULoadOrder = (MFTULoadOrder) getFTU_LoadOrder();

		// List
		List<MInOut> list = new Query(getCtx(), MInOut.Table_Name, "FTU_RecordWeight_ID=? AND DocStatus IN('CO', 'CL')",
				get_TrxName()).setParameters(getFTU_RecordWeight_ID()).setOrderBy("DocStatus").list();
		//
		for (MInOut mInOut : list) {
			if (mInOut.getDocStatus().equals(X_M_InOut.DOCSTATUS_Closed))
				return "@M_InOut_ID@ @Closed@";
			if (m_FTULoadOrder.isImmediateDelivery()) {
				mInOut.set_ValueOfColumn("FTU_RecordWeight_ID", 0);
				mInOut.saveEx();
				continue;
			}
			mInOut.setDocAction(X_M_InOut.DOCACTION_Reverse_Correct);
			if(!mInOut.processIt(X_M_InOut.DOCACTION_Reverse_Correct)) {
				return "ERROR REVERSANDO DOCUMENTO RELACIONADO : " +mInOut.getDocumentNo();
			}
			mInOut.saveEx();
		}
		//
		return null;
	}
	
	/**
	 * Reverse input movement
	 * 
	 * @return
	 * @return String
	 */
	private String reverseInputMovement() {
		// List
		log.log(Level.SEVERE, "reverseInputMovement");
		List<MMovement> lists = new Query(getCtx(), MMovement.Table_Name,
				"FTU_RecordWeight_ID = ? AND DocStatus IN ('CO','CL')", get_TrxName())
						.setParameters(getFTU_RecordWeight_ID()).setOrderBy("DocStatus").list();

		for (MMovement mMovement : lists) {

			if (mMovement.getDocStatus().equals(X_M_Movement.DOCSTATUS_Closed))
				throw new AdempiereException("@FTU_RecordWeight_ID@ @Referenced@ --> @M_Movement_ID@ "
						+ mMovement.getDocumentNo() + "@DocStatus@ " + X_M_Movement.DOCSTATUS_Closed);

			mMovement.set_ValueOfColumn("FTU_RecordWeight_ID", null);
			mMovement.setDocAction(X_M_Movement.DOCACTION_Reverse_Correct);
			mMovement.processIt(X_M_Movement.DOCACTION_Reverse_Correct);
		    log.log(Level.SEVERE, "mensaje proceso:" + mMovement.getProcessMsg());
			
			
			mMovement.saveEx();
			return mMovement.getProcessMsg();
			
		}
		//
		return null;
	}

	/**
	 * Reverse movement
	 * 
	 * @return
	 * @return String
	 */
	private String reverseMovement() {
		// List
		log.log(Level.SEVERE, m_processMsg);
		MFTULoadOrder m_FTULoadOrder = new MFTULoadOrder(getCtx(), getFTU_LoadOrder_ID(), get_TrxName());
		List<MMovement> lists = new Query(getCtx(), MMovement.Table_Name,
				"FTU_RecordWeight_ID = ? AND DocStatus IN ('CO','CL')", get_TrxName())
						.setParameters(getFTU_RecordWeight_ID()).setOrderBy("DocStatus").list();

		for (MMovement mMovement : lists) {

			if (mMovement.getDocStatus().equals(X_M_Movement.DOCSTATUS_Closed))
				throw new AdempiereException("@FTU_RecordWeight_ID@ @Referenced@ --> @M_Movement_ID@ "
						+ mMovement.getDocumentNo() + "@DocStatus@ " + X_M_Movement.DOCSTATUS_Closed);

			if (m_FTULoadOrder.isImmediateDelivery()) {
				mMovement.set_ValueOfColumn("FTU_RecordWeight_ID", 0);
				mMovement.saveEx();
				continue;
			}
			mMovement.set_ValueOfColumn("FTU_RecordWeight_ID", null);
			mMovement.setDocAction(X_M_Movement.DOCACTION_Reverse_Correct);
			mMovement.processIt(X_M_Movement.DOCACTION_Reverse_Correct);
			mMovement.saveEx();
			
			return mMovement.getProcessMsg();
			}
		//
		return null;
	}

	/*************************************************************************
	 * Get Summary
	 * 
	 * @return Summary of Document
	 */
	public String getSummary() {
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		// - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	} // getSummary

	/**
	 * Get Process Message
	 * 
	 * @return clear text error message
	 */
	public String getProcessMsg() {
		return m_processMsg;
	} // getProcessMsg

	/**
	 * Get Document Owner (Responsible)
	 * 
	 * @return AD_User_ID
	 */
	public int getDoc_User_ID() {
		// return getSalesRep_ID();
		return 0;
	} // getDoc_User_ID

	/**
	 * Get Document Approval Amount
	 * 
	 * @return amount
	 */
	public BigDecimal getApprovalAmt() {
		return null; // getTotalLines();
	} // getApprovalAmt

	/**
	 * Get Document Currency
	 * 
	 * @return C_Currency_ID
	 */
	public int getC_Currency_ID() {
		// MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
		// return pl.getC_Currency_ID();
		return 0;
	} // getC_Currency_ID

	/**
	 * Add to Description
	 * 
	 * @param description text
	 */
	public void addDescription(String description) {
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	} // addDescription

	/**
	 * Document Status is Complete or Closed
	 * 
	 * @return true if CO, CL or RE
	 */
	public boolean isComplete() {
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) || DOCSTATUS_Closed.equals(ds) || DOCSTATUS_Reversed.equals(ds);
	} // isComplete

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
		// Valid Document Action
		if (AD_Table_ID == Table_ID) {
			if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid)) {
				options[index++] = DocumentEngine.ACTION_Prepare;
			}
			else if(docStatus.equals(DocumentEngine.STATUS_WaitingConfirmation))
			{
				options[index++] = DocumentEngine.ACTION_Complete;
				options[index++] = DocumentEngine.ACTION_Reject;
			}
			// Complete .. CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
				options[index++] = DocumentEngine.ACTION_Void;
				options[index++] = DocumentEngine.ACTION_ReActivate;
				options[index++] = DocumentEngine.ACTION_Close;

			} else if (docStatus.equals(DocumentEngine.STATUS_Closed))
				options[index++] = DocumentEngine.ACTION_None;
		}

		return index;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		super.beforeSave(newRecord);
		if (newRecord)
			setIsPrinted(false);
		// Add validation for trying to save the registry do not allow weight if the
		// load order or the analysis of quality are null.
		// Msg display
		String msg = null;

		// Valid Operation Type
		if (getOperationType() == null)
			msg = "@FTU_EntryTicket_ID@ @NotFound@";
		// Valid Entry ticket
		if (getFTU_EntryTicket() != null && !getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts)) {
			msg = validETReferenceDuplicated();
		}
		// Operation Type
		else if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialOutputMovement)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts)
				|| getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)) {

			/*
			 * If Operation Type In Delivery Bulk Material, Delivery Finished Product Or
			 * Material Output Movement and Load Order equals 0 view msg Load Order not
			 * found.
			 */
			if (getFTU_LoadOrder_ID() == 0)
				msg = "@FTU_LoadOrder_ID@ @NotFound@";
		}
		// Set Sales Transaction
		if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_ProductBulkReceipt)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_ReceiptMoreThanOneProduct)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialInputMovement)) {
			setIsSOTrx(false);
		}else if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialOutputMovement)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryMultipleProducts))
			setIsSOTrx(true);
		// If msg is distinct of null display Adempiere Exception with msg
		if (msg != null)
			throw new AdempiereException(msg);
		
		//FR [7]
		if (getFTU_WeightApprovalMotive_ID() != 0){
			X_FTU_WeightApprovalMotive approval = (X_FTU_WeightApprovalMotive)getFTU_WeightApprovalMotive();  
			setIsApproved(approval.isApproved());
		}
		
		return true;
	}
	
	/**
	 * Add support for generating inventory movements input
	 * @author Jorge Colmenarez, 2023-06-29 16:02
	 * @return String
	 */
	private String createMovementInput(MFTUEntryTicket et) {
		// Get Orders From Load Order
		MFTULoadOrder lo = new MFTULoadOrder(getCtx(), et.getFTU_LoadOrder_ID(), get_TrxName());
		// Get Lines from Load Order
		MFTULoadOrderLine[] lines = lo.getLinesForMovement();
		// Current Values
		int m_Current_BPartner_ID = 0;
		MMovement m_Current_Movement = null;
		StringBuffer msg = new StringBuffer();
		int m_Created = 0;
		// Create Movements
		for (MFTULoadOrderLine line : lines) {
			// Valid Line
			MDDOrderLine m_DD_OrderLine = (MDDOrderLine) line.getDD_OrderLine();
			//
			if (m_DD_OrderLine == null) {
				m_processMsg = "@DD_OrderLine_ID@ @NotFound@";
				return null;
			}
			//
			MDDOrder m_DD_Order = (MDDOrder) m_DD_OrderLine.getDD_Order();
			// Get Document Type
			MDocType m_DocType = MDocType.get(getCtx(), m_DD_Order.getC_DocType_ID());
			// Get Document for Movement
			int m_C_DocTypeMovement_ID = m_DocType.get_ValueAsInt("C_DocTypeMovement_ID");
			// Valid Document
			if (m_C_DocTypeMovement_ID == 0) {
				m_processMsg = "@C_DocTypeMovement_ID@ @NotFound@";
				return null;
			}
			// Generate
			if (m_Current_BPartner_ID != m_DD_Order.getC_BPartner_ID()) {
				// Complete Previous Movements
				completeMovement(m_Current_Movement);
				m_Current_BPartner_ID = m_DD_Order.getC_BPartner_ID();
				// Create Movement
				m_Current_Movement = new MMovement(getCtx(), 0, get_TrxName());
				m_Current_Movement.setC_DocType_ID(m_C_DocTypeMovement_ID);
				m_Current_Movement.setDateReceived(getDateForDocument());
				// Set Organization
				m_Current_Movement.setAD_Org_ID(getAD_Org_ID());
				m_Current_Movement.set_ValueOfColumn("AD_OrgTarget_ID", getM_Warehouse().getAD_Org_ID());
				m_Current_Movement.setM_Warehouse_ID(m_DD_Order.getM_Warehouse_ID());
				m_Current_Movement.setM_WarehouseTo_ID(getM_Warehouse_ID());
				//m_Current_Movement.setDD_Order_ID(m_DD_Order.get_ID());
				if (m_DD_Order.getC_BPartner_ID() > 0) {
					m_Current_Movement.setC_BPartner_ID(m_DD_Order.getC_BPartner_ID());
					m_Current_Movement.setC_BPartner_Location_ID(m_DD_Order.getC_BPartner_Location_ID());
					m_Current_Movement.saveEx();
				}
				m_Current_Movement.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
				m_Current_Movement.saveEx();
				//
				m_Created++;
				// Initialize Message
				if (msg.length() > 0)
					msg.append(" - " + m_Current_Movement.getDocumentInfo());
				else
					msg.append(m_Current_Movement.getDocumentInfo());
			}
			// Valid exist Movement
			if (m_Current_Movement != null) {
				// Create Line
				MMovementLine m_MovementLine = new MMovementLine(m_Current_Movement);
				// Reference
				m_MovementLine.set_ValueOfColumn("AD_OrgTarget_ID", m_Current_Movement.get_ValueAsInt("AD_OrgTarget_ID"));
				m_MovementLine.setM_Movement_ID(m_Current_Movement.getM_Movement_ID());
				//m_MovementLine.setDD_OrderLine_ID(m_DD_OrderLine.getDD_OrderLine_ID());
				// Set Product
				m_MovementLine.setM_Product_ID(line.getM_Product_ID());
				MProduct prod = MProduct.get(getCtx(), line.getM_Product_ID());
				m_MovementLine.setM_Locator_ID(m_DD_OrderLine.getM_LocatorTo_ID());
				int M_LocatorTo_ID = getFTU_Chute().getM_Locator_ID();
				if(M_LocatorTo_ID <= 0)
				{
					MWarehouse w = new MWarehouse(getCtx(), getM_Warehouse_ID(), get_TrxName());
					M_LocatorTo_ID = w.getDefaultLocator().get_ID();
				}
				m_MovementLine.setM_LocatorTo_ID(M_LocatorTo_ID);
				if (m_DD_OrderLine.getM_AttributeSetInstance_ID() > 0)
					m_MovementLine.setM_AttributeSetInstance_ID(m_DD_OrderLine.getM_AttributeSetInstance_ID());
				if (m_DD_OrderLine.getM_AttributeSetInstanceTo_ID() > 0)
					m_MovementLine.setM_AttributeSetInstanceTo_ID(m_DD_OrderLine.getM_AttributeSetInstanceTo_ID());
				
				if (prod.get_ValueAsBoolean("IsBulk")){
					//	Added by Jorge Colmenarez, 2023-07-25 17:06
					//	Support for set DifferenceQty on Movement
					if(getOperationType().equalsIgnoreCase(OPERATIONTYPE_MaterialInputMovement))
					{
						if(getDifferenceQty().compareTo(BigDecimal.ZERO)<0) {
							m_MovementLine.setMovementQty(getOriginNetWeight());
							m_MovementLine.setScrappedQty(getDifferenceQty().abs());
						}
						else {
							m_MovementLine.setMovementQty(getOriginNetWeight());
							BigDecimal diff = getOriginNetWeight().subtract(getNetWeight());
							log.warning("Differencia Translado: "+diff);
							if(diff.compareTo(BigDecimal.ZERO)>0){
								m_MovementLine.setScrappedQty(diff.abs());
							}
						}
					}
					else {
						m_MovementLine.setMovementQty(getNetWeight());
					}
					//	End Jorge Colmenarez
				}
				else {
					m_MovementLine.setMovementQty(line.getQty());
				}
				m_MovementLine.saveEx();
			}

		} // Create
			// Complete Movement
		completeMovement(m_Current_Movement);
		//
		return "@M_Movement_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "] ";
	}

	/**
	 * Add support for generating inventory movements
	 * 
	 * @return
	 * @return String
	 */
	private String createMovement() {
		// Get Orders From Load Order
		MFTULoadOrder lo = (MFTULoadOrder) getFTU_LoadOrder();
		// Get Lines from Load Order
		MFTULoadOrderLine[] lines = lo.getLinesForMovement();
		// Current Values
		int m_Current_BPartner_ID = 0;
		MMovement m_Current_Movement = null;
		StringBuffer msg = new StringBuffer();
		int m_Created = 0;
		boolean productSelect = false; 
		// Create Movements
		for (MFTULoadOrderLine line : lines) {

			if (lo.isImmediateDelivery()) {
				if (line.getM_MovementLine_ID() != 0) {
					MMovementLine movl = new MMovementLine(getCtx(), line.getM_MovementLine_ID(), get_TrxName());
					MMovement mov = new MMovement(getCtx(), movl.getM_Movement_ID(), get_TrxName());
					mov.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
					mov.saveEx();
				}
				break;
			}
			// Valid Line
			MDDOrderLine m_DD_OrderLine = (MDDOrderLine) line.getDD_OrderLine();
			//
			if (m_DD_OrderLine == null) {
				m_processMsg = "@DD_OrderLine_ID@ @NotFound@";
				return null;
			}
			//
			MDDOrder m_DD_Order = (MDDOrder) m_DD_OrderLine.getDD_Order();
			// Get Document Type
			MDocType m_DocType = MDocType.get(getCtx(), m_DD_Order.getC_DocType_ID());
			// Get Document for Movement
			int m_C_DocTypeMovement_ID = m_DocType.get_ValueAsInt("C_DocTypeMovement_ID");
			// Valid Document
			if (m_C_DocTypeMovement_ID == 0) {
				m_processMsg = "@C_DocTypeMovement_ID@ @NotFound@";
				return null;
			}
			// Generate
			if (m_Current_BPartner_ID != m_DD_Order.getC_BPartner_ID()) {
				// Complete Previous Movements
				completeMovement(m_Current_Movement);
				m_Current_BPartner_ID = m_DD_Order.getC_BPartner_ID();
				// Create Movement
				if (getOperationType().equals(OPERATIONTYPE_MultipleProductMovement) && getFTU_RecordWeightSource_ID() > 0 ) {
					int sourceRecordWeigthId = getMMovementByRecordWeigthSourceID();
					m_Current_Movement = new MMovement(getCtx(), sourceRecordWeigthId , get_TrxName());	
				}else {
					m_Current_Movement = new MMovement(getCtx(), 0, get_TrxName());
					m_Current_Movement.setC_DocType_ID(m_C_DocTypeMovement_ID);
					m_Current_Movement.setDateReceived(getDateForDocument());
					// Set Organization
					m_Current_Movement.setAD_Org_ID(getAD_Org_ID());
					m_Current_Movement.set_ValueOfColumn("AD_OrgTarget_ID", getM_Warehouse().getAD_Org_ID());
					m_Current_Movement.setDD_Order_ID(m_DD_Order.get_ID());
					m_Current_Movement.setM_Warehouse_ID(getM_Warehouse_ID());
					m_Current_Movement.setM_WarehouseTo_ID(m_DD_Order.getM_Warehouse_ID());
					if (m_DD_Order.getC_BPartner_ID() > 0) {
						m_Current_Movement.setC_BPartner_ID(m_DD_Order.getC_BPartner_ID());
						m_Current_Movement.setC_BPartner_Location_ID(m_DD_Order.getC_BPartner_Location_ID());
						m_Current_Movement.saveEx();
					}
					m_Current_Movement.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
					m_Current_Movement.saveEx();
				}
				//
				m_Created++;
				// Initialize Message
				if (msg.length() > 0)
					msg.append(" - " + m_Current_Movement.getDocumentNo());
				else
					msg.append(m_Current_Movement.getDocumentNo());
			}
			// Valid exist Movement
			if (m_Current_Movement != null) {
				// Create Line
				if (!getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)) {
					MMovementLine m_MovementLine = new MMovementLine(m_Current_Movement);
					// Reference
					m_MovementLine.setM_Movement_ID(m_Current_Movement.getM_Movement_ID());
					m_MovementLine.setDD_OrderLine_ID(m_DD_OrderLine.getDD_OrderLine_ID());
					m_MovementLine.set_ValueOfColumn("AD_OrgTarget_ID", m_Current_Movement.get_ValueAsInt("AD_OrgTarget_ID"));
					// Set Product
					m_MovementLine.setM_Product_ID(line.getM_Product_ID());
					MProduct prod = MProduct.get(getCtx(), line.getM_Product_ID());
					m_MovementLine.setM_Locator_ID(m_DD_OrderLine.getM_Locator_ID());
					m_MovementLine.setM_LocatorTo_ID(m_DD_OrderLine.getM_LocatorTo_ID());
					if (m_DD_OrderLine.getM_AttributeSetInstance_ID() > 0)
						m_MovementLine.setM_AttributeSetInstance_ID(m_DD_OrderLine.getM_AttributeSetInstance_ID());
					if (m_DD_OrderLine.getM_AttributeSetInstanceTo_ID() > 0)
						m_MovementLine.setM_AttributeSetInstanceTo_ID(m_DD_OrderLine.getM_AttributeSetInstanceTo_ID());
					
					if (prod.get_ValueAsBoolean("IsBulk")){
						m_MovementLine.setMovementQty(getNetWeight());
					}
					else {
							m_MovementLine.setMovementQty(line.getQty());
					}
					
					m_MovementLine.saveEx();
					// Reference
					line.setM_MovementLine_ID(m_MovementLine.getM_MovementLine_ID());
					line.setConfirmedQty(line.getQty());
					line.saveEx();
				}else {
					if ( getM_Product_ID() == 0) {
						m_processMsg = "@M_Product_ID@ @NotFound@";
						return null;
					}
					if(  getM_Product_ID() == line.getM_Product_ID()) {
						MMovementLine m_MovementLine = new MMovementLine(m_Current_Movement);
						// Reference
						m_MovementLine.setM_Movement_ID(m_Current_Movement.getM_Movement_ID());
						m_MovementLine.setDD_OrderLine_ID(m_DD_OrderLine.getDD_OrderLine_ID());
						// Set Product
						m_MovementLine.setM_Product_ID(line.getM_Product_ID());
						m_MovementLine.setM_Locator_ID(m_DD_OrderLine.getM_Locator_ID());
						m_MovementLine.setM_LocatorTo_ID(m_DD_OrderLine.getM_LocatorTo_ID());
						if (m_DD_OrderLine.getM_AttributeSetInstance_ID() > 0)
							m_MovementLine.setM_AttributeSetInstance_ID(m_DD_OrderLine.getM_AttributeSetInstance_ID());
						if (m_DD_OrderLine.getM_AttributeSetInstanceTo_ID() > 0)
							m_MovementLine.setM_AttributeSetInstanceTo_ID(m_DD_OrderLine.getM_AttributeSetInstanceTo_ID());
						
						MProduct product = MProduct.get(getCtx(), line.getM_Product_ID());
						if((boolean) product.get_Value("isbulk")) m_MovementLine.setMovementQty( getNetWeight() );
						else m_MovementLine.setMovementQty(line.getQty());
						m_MovementLine.saveEx();
						// Reference
						line.setM_MovementLine_ID(m_MovementLine.getM_MovementLine_ID());
						
						if((boolean) product.get_Value("isbulk")) line.setConfirmedQty( getNetWeight() );
						else line.setConfirmedQty(line.getQty());
						
						line.saveEx();
						productSelect = true;
					}
					
					if(productSelect) break;
				}
			}
		} // Create
		
		if(getOperationType().equals(OPERATIONTYPE_ProductionOutMovement)) {
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
	         if (m_Current_Movement.get_ValueAsInt("FTU_RecordWeight_ID") <= 0) {
	            m_Current_Movement.set_ValueOfColumn("FTU_RecordWeight_ID", get_ID());
	         }

	         m_Current_Movement.set_ValueOfColumn("PP_Order_ID", et.get_ValueAsInt("PP_Order_ID"));
	         m_Current_Movement.saveEx();
		}
		
		if (!getOperationType().equals(OPERATIONTYPE_MultipleProductMovement)) {
			// Complete Movement
			if(MSysConfig.getBooleanValue("COMPLETE_MOVEMENT_RECORDWEIGTH", true, getAD_Client_ID(),getAD_Org_ID()))
				completeMovement(m_Current_Movement);
		}else if(!productSelect) {
			m_processMsg = "@M_Product_ID@ @NotFound@";
			return null;
		}
		//
		lo.setIsMoved(true);
		lo.save(get_TrxName());
		//
		return "@M_Movement_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "]";
	}

	/**
	 * Complete Document
	 * 
	 * @return void
	 */
	private void completeMovement(MMovement m_Current_Movement) {
		if (m_Current_Movement != null) {
			if (m_Current_Movement.getDocStatus().equals(X_M_Movement.DOCSTATUS_Drafted)) {
				m_Current_Movement.setDocAction(X_M_Movement.DOCACTION_Complete);
				if (!m_Current_Movement.processIt(X_M_Movement.DOCACTION_Complete))
					throw new AdempiereException(m_Current_Movement.getProcessMsg());
				m_Current_Movement.saveEx();
				if(getFTU_WeightApprovalMotive_ID()>0) {
					if(getFTU_WeightApprovalMotive().isGenerateLoss())
						createLossInventory(m_Current_Movement, (X_FTU_WeightApprovalMotive)getFTU_WeightApprovalMotive());
				}else {
					String sql = "SELECT FTU_WeightApprovalMotive_ID FROM FTU_WeightApprovalMotive WHERE IsDefault = 'Y' and IsActive = 'Y' AND "
							+ "AD_Client_ID = " + getAD_Client_ID();
					int ID = DB.getSQLValue(get_TrxName(), sql);
					
					if (ID>0){
						X_FTU_WeightApprovalMotive motive = new X_FTU_WeightApprovalMotive(p_ctx, ID, get_TrxName());
						if (motive.isGenerateLoss())
							createLossInventory(m_Current_Movement, motive);
					}
					
				}
			}
		}
	}
	
	/***
	 * Create and complete Loss Inventory
	 * @param m_Current_Movement
	 * @param wam
	 */
	private void createLossInventory(MMovement m_Current_Movement, X_FTU_WeightApprovalMotive wam) {
		MInventory inv = null;
		for(MMovementLine line : m_Current_Movement.getLines(true)) {
			if(line.getScrappedQty().compareTo(BigDecimal.ZERO)==0)
				continue;
			//	Create Inventory Header
			if(inv == null) {
				inv = new MInventory((MWarehouse)line.getM_LocatorTo().getM_Warehouse(), m_Current_Movement.get_TrxName());
				inv.setC_DocType_ID(wam.getC_DocTypeInventory_ID());
				inv.setMovementDate(m_Current_Movement.getMovementDate());
				inv.setDescription("Creado por ajuste de merma en traslado desde: "+line.getM_LocatorTo().getValue()+" Documento Nro:"+m_Current_Movement.getDocumentNo());
				inv.set_ValueOfColumn("M_Movement_ID", m_Current_Movement.get_ID());
				inv.setDocStatus(DOCSTATUS_Drafted);
				inv.setDocAction(DOCACTION_Complete);
				inv.saveEx(get_TrxName());
			}
			//	Create Lines
			MInventoryLine il = new MInventoryLine(inv, line.getM_LocatorTo_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), null, null, line.getScrappedQty());
			il.setC_Charge_ID(wam.getC_Charge_ID());
			il.saveEx(get_TrxName());
		}
		//	Complete
		if(inv!=null) {
			if(!inv.processIt(ACTION_Complete))
				throw new AdempiereException(inv.getProcessMsg());
			inv.saveEx(get_TrxName());
		}
	}

	/**
	 * Create a Material Receipt from the Record Weight
	 * 
	 * @param order
	 * @return
	 * @return String
	 */
	private String createInOut() {
		// this yet generated
		if (validInOutReference() != null)
			return "";
		//
		//	Added by Jorge Colmenarez, 2024-03-16 12:49
		//	Add Support for Delivery Multiple Products
		MDocType LoadOrderDocType  = (MDocType) getFTU_LoadOrder().getC_DocType();
		Boolean isCreateShipment = LoadOrderDocType.get_ValueAsBoolean("isCreateShipment");
		//	End Jorge Colmenarez
		// DocumentNo
		String l_DocumentNo = "";
		// Create Material Receipt or Shipment by Operation Type
		if (getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)) {
			// Get Order and Line
			MOrderLine oLine = (MOrderLine) getFTU_EntryTicket().getC_OrderLine();
			MOrder order = oLine.getParent();

			if (order == null) {
				m_processMsg = "@C_Order_ID@ @NotFound@";
				return null;
			}

			MDocType m_DocType = MDocType.get(getCtx(), order.getC_DocType_ID());

			if (m_DocType.getC_DocTypeShipment_ID() == 0) {
				m_processMsg = "@C_DocTypeShipment_ID@ @NotFound@";
				return null;
			}
			// Create Receipt
			FTUMInOut m_Receipt = new FTUMInOut(order, m_DocType.getC_DocTypeShipment_ID(), getDateForDocument());
			m_Receipt.setDateAcct(getDateForDocument());
			// Set New Organization and warehouse
			m_Receipt.setAD_Org_ID(getAD_Org_ID());
			m_Receipt.setAD_OrgTrx_ID(getAD_Org_ID());
			// Get Chute
			MFTUChute chute = null;
			int m_M_Warehouse_ID = 0;
			int m_M_Locator_ID = 0;
			// Get Chute from Table or Default
			if (getFTU_Chute_ID() != 0)
				chute = new MFTUChute(getCtx(), getFTU_Chute_ID(), get_TableName());
			else {
				int m_FTA_Chute_ID = DB.getSQLValue(get_TrxName(), "SELECT c.FTU_Chute_ID " + "FROM FTU_Chute c "
						+ "WHERE AD_Org_ID = ? " + "ORDER BY IsDefault DESC", getAD_Org_ID());
				// Search Chute
				if (m_FTA_Chute_ID > 0)
					chute = new MFTUChute(getCtx(), m_FTA_Chute_ID, get_TableName());
			}
			// Set Warehouse and Locator
			if (chute != null) {
				m_M_Warehouse_ID = chute.getM_Warehouse_ID();
				m_M_Locator_ID = chute.getM_Locator_ID();
			} else {
				m_M_Warehouse_ID = getM_Warehouse_ID();
				if (m_M_Warehouse_ID <= 0)
					m_M_Warehouse_ID = Env.getContextAsInt(getCtx(), "#M_Warehouse_ID");
			}

			if (m_M_Warehouse_ID <= 0)
				throw new AdempiereException("@M_Warehouse_ID@ @NotFound@");

			// Set Warehouse
			m_Receipt.setM_Warehouse_ID(m_M_Warehouse_ID);

			m_Receipt.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
			// Save
			m_Receipt.saveEx(get_TrxName());
			//
			MInOutLine ioLine = new MInOutLine(m_Receipt);

			MProduct product = MProduct.get(getCtx(), oLine.getM_Product_ID());
			// Rate Convert
			BigDecimal rate = MUOMConversion.getProductRateFrom(Env.getCtx(), product.getM_Product_ID(), getC_UOM_ID());

			if (rate == null) {
				m_processMsg = "@NoUOMConversion@";
				return null;
			}

			// Change for Get Valid Weight
			// BigDecimal m_MovementQty = getNetWeight().multiply(rate);
			BigDecimal m_MovementQty = getValidWeight(false).multiply(rate);

			boolean validateOrderedQty = MSysConfig.getBooleanValue(MSysConfig.VALIDATE_MATCHING_TO_ORDERED_QTY, true,
					Env.getAD_Client_ID(Env.getCtx()));
			if (validateOrderedQty) {
				BigDecimal qtyOrdered = oLine.getQtyDelivered().add(oLine.getQtyReserved());
				if (m_MovementQty.compareTo(qtyOrdered) > 0) {
					throw new IllegalStateException("Total matched delivered qty > ordered qty. MatchedDeliveredQty="
							+ m_MovementQty + ", OrderedQty=" + oLine.getQtyOrdered() + ", Line=" + oLine);
				}
			}

			// Set Product
			ioLine.setProduct(product);
			ioLine.setC_OrderLine_ID(oLine.getC_OrderLine_ID());
			// Set Locator
			if (m_M_Locator_ID != 0)
				ioLine.setM_Locator_ID(m_M_Locator_ID);
			else
				ioLine.setM_Locator_ID(m_MovementQty);
			// Set Quantity
			ioLine.setQty(m_MovementQty);
			ioLine.saveEx(get_TrxName());
			// Manually Process Shipment
			//	Added By Jorge Colmenarez, 2021-07-22 11:59
			//	Support for get DocAction from SysConfig Variable
			String docAction = MSysConfig.getValue("DOCACTION_INOUT_FROM_RECORDWEIHT", X_M_InOut.DOCACTION_Complete, getAD_Client_ID());
			m_Receipt.processIt(docAction);
			//	End Jorge Colmenarez
			m_Receipt.saveEx(get_TrxName());
			//	Added by Jorge Colmenarez, 2024-02-06 15:54
			if(getFTU_EntryTicket().isGenLandedCost()) {
				updateFreightCostLine(m_Receipt);
			}
			//	End Jorge Colmenarez
			l_DocumentNo = "@M_InOut_ID@: " + m_Receipt.getDocumentNo();
		}
		// Product Bulk Receipt
		else if (getOperationType().equals(OPERATIONTYPE_ProductBulkReceipt)) {
			// Get Order and Line
			MOrder order = null;
			// MAttributeSetInstance asi = null;
			MProduct product = (MProduct) getM_Product();
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TableName());
			if (et.getC_Order_ID() != 0)
				order = (MOrder) et.getC_Order();

			if (order == null) {
				m_processMsg = "@C_Order_ID@ @NotFound@";
				return null;
			}

			if (product == null) {
				m_processMsg = "@M_Product_ID@ @NotFound@";
				return null;
			}

			MDocType m_DocType = MDocType.get(getCtx(), order.getC_DocType_ID());

			if (m_DocType.getC_DocTypeShipment_ID() == 0) {
				m_processMsg = "@C_DocTypeShipment_ID@ @NotFound@";
				return null;
			}
			// Create Receipt
			FTUMInOut m_Receipt = new FTUMInOut(order, m_DocType.getC_DocTypeShipment_ID(), getDateForDocument());
			m_Receipt.setDateAcct(getDateForDocument());
			// Set New Organization and warehouse
			m_Receipt.setAD_Org_ID(getAD_Org_ID());
			m_Receipt.setAD_OrgTrx_ID(getAD_Org_ID());
			// Set Warehouse
			m_Receipt.setM_Warehouse_ID(getM_Warehouse_ID());
			// Set Farmer Credit and Record Weight
			m_Receipt.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
			// Save
			m_Receipt.saveEx(get_TrxName());
			//
			MInOutLine ioLine = new MInOutLine(m_Receipt);

			// Rate Convert
			BigDecimal rate = MUOMConversion.getProductRateFrom(Env.getCtx(), product.getM_Product_ID(), getC_UOM_ID());

			if (rate == null) {
				m_processMsg = "@NoUOMConversion@";
				return null;
			}

			// Get Chute
			MFTUChute chute = null;
			int m_M_Warehouse_ID = 0;
			int m_M_Locator_ID = 0;
			// Get Chute from Table or Default
			if (getFTU_Chute_ID() != 0)
				chute = new MFTUChute(getCtx(), getFTU_Chute_ID(), get_TableName());
			else {
				int m_FTA_Chute_ID = DB.getSQLValue(get_TrxName(), "SELECT c.FTU_Chute_ID " + "FROM FTU_Chute c "
						+ "WHERE AD_Org_ID = ? " + "ORDER BY IsDefault DESC", getAD_Org_ID());
				// Search Chute
				if (m_FTA_Chute_ID > 0)
					chute = new MFTUChute(getCtx(), m_FTA_Chute_ID, get_TableName());
			}
			// Set Warehouse and Locator
			if (chute != null) {
				m_M_Warehouse_ID = chute.getM_Warehouse_ID();
				m_M_Locator_ID = chute.getM_Locator_ID();
			} else {
				m_M_Warehouse_ID = getM_Warehouse_ID();
				if (m_M_Warehouse_ID <= 0)
					m_M_Warehouse_ID = Env.getContextAsInt(getCtx(), "#M_Warehouse_ID");
			}

			if (m_M_Warehouse_ID <= 0)
				throw new AdempiereException("@M_Warehouse_ID@ @NotFound@");

			BigDecimal m_MovementQty = getValidWeight(false).multiply(rate);
			// Set Product
			ioLine.setProduct(product);
			
			if (m_M_Locator_ID != 0)
				ioLine.setM_Locator_ID(m_M_Locator_ID);
			else
				ioLine.setM_Locator_ID(m_MovementQty);
			
			/*MOrderLine[] orderLines = order.getLines("AND M_Product_ID=" + product.getM_Product_ID(), "");
			for (int i = 0; i < orderLines.length; i++) {
				ioLine.setC_OrderLine_ID(orderLines[i].getC_OrderLine_ID());
				if(orderLines[i].getM_AttributeSetInstance_ID()>0)
					ioLine.setM_AttributeSetInstance_ID(orderLines[i].getM_AttributeSetInstance_ID());
			}*/
			
			MOrderLine line = (MOrderLine) et.getC_OrderLine();
			if(MSysConfig.getBooleanValue("SET_ORDERLINE_FROM_ENTRYTICKET", true, getAD_Client_ID(), getAD_Org_ID()))
				ioLine.setC_OrderLine_ID(line.getC_OrderLine_ID());
			else {
				MOrderLine[] orderLines = order.getLines("AND M_Product_ID=" + product.getM_Product_ID(), "");
				for (int i = 0; i < orderLines.length; i++)
					ioLine.setC_OrderLine_ID(orderLines[i].getC_OrderLine_ID());
			}
			if(line.getM_AttributeSetInstance_ID()>0)
				ioLine.setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
			//added by david castillo 18/07/2023 
			if (product.getM_AttributeSet_ID()>0 && !(ioLine.getM_AttributeSetInstance_ID()>0)) {
				MAttributeSetInstance inst = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
				inst.saveEx(get_TrxName());
				ioLine.setM_AttributeSetInstance_ID(inst.getM_AttributeSetInstance_ID());
			}
			// Set Quantity
			ioLine.setQty(m_MovementQty);
			ioLine.saveEx(get_TrxName());
			// Manually Process Shipment
			//	Added By Jorge Colmenarez, 2021-07-22 11:59
			//	Support for get DocAction from SysConfig Variable
			String docAction = MSysConfig.getValue("DOCACTION_INOUT_FROM_RECORDWEIHT", X_M_InOut.DOCACTION_Complete, getAD_Client_ID());
			m_Receipt.processIt(docAction);
			//	End Jorge Colmenarez
			m_Receipt.saveEx(get_TrxName());
			//	Added by Jorge Colmenarez, 2024-02-06 15:54
			if(getFTU_EntryTicket().isGenLandedCost()) {
				updateFreightCostLine(m_Receipt);
			}
			//	End Jorge Colmenarez
			l_DocumentNo = "@M_InOut_ID@: " + m_Receipt.getDocumentNo();
		}
		// Delivery Bulk Material
		else if (getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)) {
			// Get Orders From Load Order
			MFTULoadOrder lo = (MFTULoadOrder) getFTU_LoadOrder();
			// Verify if Is Immediate Delivery
			if (lo.isImmediateDelivery()) {
				// Not Created Shipments
				MInOut[] inOut = lo.getInOutFromLoadOrder(getFTU_LoadOrder_ID());
				if (inOut == null) { // If not exists In Out return NULL
					return null;
				}
				// Set Value of FTU_RecordWeight
				for (MInOut mInOut : inOut) {
					mInOut.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
					mInOut.saveEx();
				}
				return null;
			}

			// Get Lines from Load Order
			MFTULoadOrderLine[] lol = lo.getLines(true);

			BigDecimal m_AcumWeight = Env.ZERO;
			BigDecimal m_TotalWeight = Env.ZERO;
			// Create Shipments
			for (int i = 0; i < lol.length; i++) {

				if (m_AcumWeight.compareTo(m_TotalWeight) == 1)
					break;
				// Get Order and Line
				MOrder order = null;
				MOrderLine oLine = null;
				// MAttributeSetInstance asi = null;
				MProduct product = null;
				if (lol[i].getC_OrderLine_ID() != 0) {
					oLine = (MOrderLine) lol[i].getC_OrderLine();
					order = (MOrder) oLine.getC_Order();
					product = MProduct.get(getCtx(), oLine.getM_Product_ID());
				}
				// Valid Order
				if (order == null) {
					m_processMsg = "@C_Order_ID@ @NotFound@";
					return null;
				}
				// Valid Order Line
				if (oLine == null) {
					m_processMsg = "@C_OrderLine_ID@ @NotFound@";
					return null;
				}
				// Valid Product
				if (product == null) {
					m_processMsg = "@M_Product_ID@ @NotFound@";
					return null;
				}
				MDocType m_DocType = MDocType.get(getCtx(), order.getC_DocType_ID());

				if (m_DocType.getC_DocTypeShipment_ID() == 0) {
					m_processMsg = "@C_DocTypeShipment_ID@ @NotFound@";
					return null;
				}

				// Create Receipt
				FTUMInOut m_Receipt = new FTUMInOut(order, m_DocType.getC_DocTypeShipment_ID(), getDateForDocument());
				m_Receipt.setDateAcct(getDateForDocument());
				// Set New Organization and warehouse
				m_Receipt.setAD_Org_ID(getAD_Org_ID());
				m_Receipt.setAD_OrgTrx_ID(getAD_Org_ID());
				// Set Warehouse
				m_Receipt.setM_Warehouse_ID(getM_Warehouse_ID());
				// Set Farmer Credit and Record Weight
				m_Receipt.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
				// Save
				m_Receipt.saveEx(get_TrxName());
				//
				MInOutLine ioLine = new MInOutLine(m_Receipt);

				// Rate Convert
				BigDecimal rate = MUOMConversion.getProductRateFrom(Env.getCtx(), product.getM_Product_ID(),
						getC_UOM_ID());

				// Validate Rate equals null
				if (rate == null) {
					MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
					MUOM oLineUOM = MUOM.get(getCtx(), getC_UOM_ID());
					m_processMsg = "@NoUOMConversion@ @from@ " + oLineUOM.getName() + " @to@ " + productUOM.getName();
					return null;
				}
				//
				BigDecimal orderRate = MUOMConversion.getProductRateTo(Env.getCtx(), product.getM_Product_ID(),
						oLine.getC_UOM_ID());
				// Validate Rate equals null
				if (orderRate == null) {
					MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
					MUOM oLineUOM = MUOM.get(getCtx(), oLine.getC_UOM_ID());
					throw new AdempiereException(
							"@NoUOMConversion@ @from@ " + oLineUOM.getName() + " @to@ " + productUOM.getName());
				}
				//
				BigDecimal m_QtyWeight = getNetWeight();
				BigDecimal m_MovementQty = m_QtyWeight.multiply(rate);

				if (m_TotalWeight == Env.ZERO)
					m_TotalWeight = getValidWeight(false).multiply(rate);

				if (lol.length == 1) {
					m_MovementQty = getValidWeight(false).multiply(rate);
				} else {
					m_AcumWeight = m_AcumWeight.add(m_MovementQty);
					if (m_AcumWeight.compareTo(getValidWeight(false).multiply(rate)) == 1)
						m_MovementQty = m_MovementQty
								.subtract(m_AcumWeight.subtract(getValidWeight(false).multiply(rate)));
					else if (m_AcumWeight.compareTo(getValidWeight(false).multiply(rate)) == -1)
						m_MovementQty = m_MovementQty.add(getValidWeight(false).multiply(rate).subtract(m_AcumWeight));
				}
				
				// Set Product
				ioLine.setProduct(product);
				
				String MMPolicy = product.getMMPolicy();
				MStorageOnHand[] storages = getWarehouse(getCtx(), getM_Warehouse_ID(), getM_Product_ID(), 0,
						null, MClient.MMPOLICY_FiFo.equals(MMPolicy), true, 0, get_TrxName(), false, 0);
				BigDecimal qtyToDeliver = m_MovementQty;
				BigDecimal available = BigDecimal.ZERO;
				for (MStorageOnHand storage: storages)
				{					
					available = storage.getQtyOnHand();	
					log.log(Level.SEVERE, "Cantidad disponible" + storage.toString()+ " - Cantidad a entregar: " +qtyToDeliver);
							if (available.compareTo(qtyToDeliver) >=0) {

								MInOutLine ioLine2 = new MInOutLine(m_Receipt);
								ioLine2.setProduct(product);
								ioLine2.setC_OrderLine_ID(lol[i].getC_OrderLine_ID());
								//ioLine2.setM_AttributeSetInstance_ID(ma.getM_AttributeSetInstance_ID());
								// Set Quantity
								ioLine2.setC_UOM_ID(oLine.getC_UOM_ID());
								ioLine2.setQty(qtyToDeliver);
								ioLine2.setQtyEntered(qtyToDeliver);
								ioLine2.setM_Locator_ID(storage.getM_Locator_ID());
								//
								ioLine2.saveEx(get_TrxName());
								qtyToDeliver = Env.ZERO;
								ioLine = ioLine2;
							}else {
								MInOutLine ioLine2 = new MInOutLine(m_Receipt);
								ioLine2.setProduct(product);
								ioLine2.setC_OrderLine_ID(lol[i].getC_OrderLine_ID());
								//ioLine2.setM_AttributeSetInstance_ID(ma.getM_AttributeSetInstance_ID());
								// Set Quantity
								ioLine2.setC_UOM_ID(oLine.getC_UOM_ID());
								ioLine2.setQty(available);
								ioLine2.setQtyEntered(available);
								ioLine2.setM_Locator_ID(storage.getM_Locator_ID());
								//
								ioLine2.saveEx(get_TrxName());
								qtyToDeliver = qtyToDeliver.subtract(available);
								ioLine = ioLine2;
							}							
							
							if (qtyToDeliver.signum() == 0)
								break;
				}
				
				if (qtyToDeliver.compareTo(Env.ZERO) == 1)
					throw new AdempiereException("No hay suficiente inventario disponible para despachar este producto");					
				
				// Manually Process Shipment
				//	Added By Jorge Colmenarez, 2021-07-22 11:59
				//	Support for get DocAction from SysConfig Variable
				m_Receipt.processIt(X_M_InOut.DOCACTION_Complete);
				//	End Jorge Colmenarez
				m_Receipt.saveEx(get_TrxName());
				lol[i].setConfirmedQty(m_MovementQty);
				lol[i].setM_InOutLine_ID(ioLine.getM_InOutLine_ID());
				lol[i].saveEx(get_TrxName());
				if (m_Receipt.getDocStatus().equalsIgnoreCase(X_M_InOut.DOCSTATUS_Completed)) {
					l_DocumentNo = " - " + l_DocumentNo + "@M_InOut_ID@: " + m_Receipt.getDocumentNo();
				}else {
					l_DocumentNo = " - " + l_DocumentNo +  m_Receipt.getProcessMsg();
				}
			} // Create Shipments

			lo.setIsDelivered(true);
			lo.save(get_TrxName());
		}
		//	Added by Jorge Colmenarez, 2024-03-16 12:49
		//	Add Support for Delivery Multiple Products
		// Delivery Finished Product
		else if (getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct) ||
				(getOperationType().contentEquals(OPERATIONTYPE_DeliveryMultipleProducts) && isCreateShipment)) {
		//	End Jorge Colmenarez
			// Crate Shipments
			l_DocumentNo = createShipments();
		}
		return l_DocumentNo;
	} // createMaterialReceipt

	/**
	 * Create Shipment from Record Weight
	 * 
	 * @return
	 * @return String
	 */
	private String createShipments() {
		// Get Orders From Load Order
		MFTULoadOrder lo = (MFTULoadOrder) getFTU_LoadOrder();
		// Save
		lo.saveEx(get_TrxName());
		// Verify if Is Immediate Delivery
		if (lo.isImmediateDelivery()) {
			// Not Created Shipments
			MInOut[] inOut = lo.getInOutFromLoadOrder(getFTU_LoadOrder_ID());
			if (inOut == null) {
				return null;
			}
			// Set Value of FTU_RecordWeight
			for (MInOut mInOut : inOut) {
				mInOut.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
				mInOut.saveEx();
			}
			return null;
		}
		// Get Lines from Load Order
		MFTULoadOrderLine[] lines = lo.getLinesForInOut();
		// Current Values
		int m_Current_BPartner_ID = 0;
		int m_Current_Warehouse_ID = 0;
		MInOut m_Current_Shipment = null;
		StringBuffer msg = new StringBuffer();
		int m_Created = 0;
		//
		if  (getOperationType().equalsIgnoreCase(OPERATIONTYPE_DeliveryMultipleProducts)) {
			lines = getProductLine(lines);
		}
		for (MFTULoadOrderLine line : lines) {
			// Valid Document Order and Business Partner
			MOrderLine oLine = (MOrderLine) line.getC_OrderLine();
			//
			if (m_Current_BPartner_ID != oLine.getC_BPartner_ID()
					|| m_Current_Warehouse_ID != oLine.getM_Warehouse_ID()) {
				// Complete Previous Shipment
				completeShipment(m_Current_Shipment);
				// Initialize Order and
				//	Modified by Jorge Colmenarez, 2022-11-19 18:02
				//	Get Warehouse from RecordWeight
				//m_Current_Warehouse_ID = oLine.getM_Warehouse_ID();
				boolean setWarehouseByHeader = MSysConfig.getBooleanValue("CheckWarehouseByLoadOrderHeader", false, getAD_Client_ID(), getAD_Org_ID());
				m_Current_Warehouse_ID = (getM_Warehouse_ID()>0 && setWarehouseByHeader ? getM_Warehouse_ID() : oLine.getM_Warehouse_ID());
				//	End Jorge Colmenarez
				m_Current_BPartner_ID = oLine.getC_BPartner_ID();
				// Get Warehouse
				MWarehouse warehouse = MWarehouse.get(getCtx(), m_Current_Warehouse_ID, get_TrxName());
				// Valid Purchase Order and Business Partner
				if (oLine.getC_Order_ID() == 0)
					throw new AdempiereException("@C_Order_ID@ @NotFound@");
				if (m_Current_BPartner_ID == 0)
					throw new AdempiereException("@C_BPartner_ID@ @NotFound@");
				// Create Order
				MOrder order = new MOrder(getCtx(), oLine.getC_Order_ID(), get_TrxName());
				// Create Shipment From Order
				m_Current_Shipment = new MInOut(order, 0, getDateForDocument());
				m_Current_Shipment.setDateAcct(getDateForDocument());
				m_Current_Shipment.setAD_Org_ID(warehouse.getAD_Org_ID());
				m_Current_Shipment.setAD_OrgTrx_ID(warehouse.getAD_Org_ID());
				m_Current_Shipment.setC_BPartner_ID(m_Current_BPartner_ID);
				// Set Warehouse
				m_Current_Shipment.setM_Warehouse_ID(m_Current_Warehouse_ID);
				m_Current_Shipment.setDocStatus(X_M_InOut.DOCSTATUS_Drafted);
				// Reference
				m_Current_Shipment.set_ValueOfColumn("FTU_RecordWeight_ID", getFTU_RecordWeight_ID());
				m_Current_Shipment.saveEx(get_TrxName());
				// Add to Counter
				m_Created++;
				// Initialize Message
				if (msg.length() > 0)
					msg.append(" - " + m_Current_Shipment.getDocumentNo());
				else
					msg.append(m_Current_Shipment.getDocumentNo());
			}
			// Shipment Created?
			if (m_Current_Shipment != null) {
				// Get Values from Result Set
				BigDecimal m_Qty = line.getQty();
				// Valid Null
				if (m_Qty == null)
					m_Qty = Env.ZERO;
				// Create Shipment Line
				MInOutLine shipmentLine = new MInOutLine(getCtx(), 0, get_TrxName());
				// Instance MProduct
				MProduct product = MProduct.get(getCtx(), line.getM_Product_ID());
				// Rate Convert
				BigDecimal rate = MUOMConversion.getProductRateFrom(Env.getCtx(), product.getM_Product_ID(),
						product.getC_UOM_ID());
				// Validate Rate equals null
				if (rate == null) {
					throw new AdempiereException("@NoUOMConversion@");
				}
				// Set Values for Lines
				shipmentLine.setAD_Org_ID(m_Current_Shipment.getAD_Org_ID());
				shipmentLine.setM_InOut_ID(m_Current_Shipment.getM_InOut_ID());
				// Quantity and Product
				shipmentLine.setM_Product_ID(product.getM_Product_ID());
				if (getOperationType().equalsIgnoreCase(OPERATIONTYPE_DeliveryMultipleProducts)) {
					shipmentLine.setM_Warehouse_ID(getFTU_Chute().getM_Warehouse_ID());
					shipmentLine.setM_Locator_ID(getFTU_Chute().getM_Locator_ID());
					//shipmentLine.setDescription();
					BigDecimal qtyCount = (BigDecimal) get_Value("qtyCount");
					BigDecimal ConfirmedQty = (BigDecimal) line.get_Value("ConfirmedQty");
					if (ConfirmedQty == null)
						ConfirmedQty = Env.ZERO;
					
					if (qtyCount == null)
						throw new AdempiereException("No Introdujo Cantidad Contada");
					if (qtyCount.compareTo(Env.ZERO) <= 0)
						throw new AdempiereException("Cantidad Contada no puede ser menor o igual a 0");
					shipmentLine.setMovementQty(qtyCount);
					shipmentLine.setQty(qtyCount);
					shipmentLine.setQtyEntered(getNetWeight());
					
					shipmentLine.setC_UOM_ID(product.getC_UOM_ID());
					
					line.setConfirmedQty(qtyCount.add(ConfirmedQty));
				}else {
					BigDecimal m_MovementQty = m_Qty.multiply(rate);
					shipmentLine.setM_Warehouse_ID(m_Current_Shipment.getM_Warehouse_ID());
					shipmentLine.setC_UOM_ID(product.getC_UOM_ID());
					shipmentLine.setQty(line.getQty());
					// References
					shipmentLine.setM_Locator_ID(m_MovementQty);
					line.setConfirmedQty(m_MovementQty);
				}
				shipmentLine.setC_OrderLine_ID(line.getC_OrderLine_ID());
				
				// Save Line
				shipmentLine.saveEx(get_TrxName());
				// Manually Process Shipment
				line.setM_InOutLine_ID(shipmentLine.get_ID());
				line.saveEx();
				// Set true Is Delivered and Is Weight Register
				lo.setIsDelivered(true);
				// Save
				lo.saveEx(get_TrxName());
			} // End Invoice Line Created
		} // End Invoice Generated
			// Complete Shipment
		completeShipment(m_Current_Shipment);
		// Commit Transaction
		// Info
		return "@M_InOut_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "]";
	}

	/**
	 * Complete Shipment
	 * 
	 * @param m_Current_Shipment
	 * @return void
	 */
	private void completeShipment(MInOut m_Current_Shipment) {
		if (m_Current_Shipment != null && m_Current_Shipment.getDocStatus().equals(X_M_InOut.DOCSTATUS_Drafted)) {
			String docAction = MSysConfig.getValue("DOCACTION_INOUT_FROM_RECORDWEIHT", X_M_InOut.DOCACTION_Complete, getAD_Client_ID());
			m_Current_Shipment.setDocAction(docAction);
			m_Current_Shipment.processIt(docAction);
			m_Current_Shipment.saveEx();
		}
	}

	/**
	 * Reference entry ticket in another sing of weight
	 * 
	 * @return
	 * @return String
	 */
	private String validETReferenceDuplicated() {
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(),
				"SELECT rw.DocumentNo FROM FTU_RecordWeight rw WHERE rw.FTU_EntryTicket_ID = ? "
						+ "AND rw.FTU_RecordWeight_ID <> " + getFTU_RecordWeight_ID() + " "
						+ "AND rw.DocStatus IN ('CO','CL','DR','IP')",
				getFTU_EntryTicket_ID());
		if (m_ReferenceNo != null) {
			MFTUEntryTicket m_EntryTicket = (MFTUEntryTicket) getFTU_EntryTicket();
			return "@SQLErrorReferenced@ @FTU_EntryTicket_ID@: " + m_EntryTicket.getDocumentNo()
					+ " @Generate@ @from@ @FTU_RecordWeight_ID@: " + m_ReferenceNo;
		}
		return null;
	}
	
	/***
	 * Get Movement by RecordWeightSource
	 * @return
	 */
	private int getMMovementByRecordWeigthSourceID(  ) {
		return DB.getSQLValue(get_TrxName(), "select max( mm.m_movement_id ) from " + 
				"ftu_recordweight fr " + 
				"inner join ftu_loadorder fl on fl.ftu_loadorder_id = fr.ftu_loadorder_id " + 
				"inner join ftu_loadorderline fl2 on fl.ftu_loadorder_id = fl2.ftu_loadorder_id " + 
				"inner join dd_orderline do2 on fl2.dd_orderline_id = do2.dd_orderline_id " + 
				"inner join dd_order do22 on do22.dd_order_id = do2.dd_order_id " + 
				"inner join m_movement mm on mm.dd_order_id = do22.dd_order_id " + 
				"where fr.ftu_recordweight_id = ? and mm.docstatus != 'VO' " , getFTU_RecordWeightSource_ID());
	}

	/**
	 * Get Valid Weight For Shipment or Receipt
	 * 
	 * @param reload
	 * @return
	 * @return BigDecimal
	 */
	private BigDecimal getValidWeight(boolean reload) {
		if (!reload && m_Valideight != null)
			return m_Valideight;

		if (MSysConfig.getBooleanValue("FTU_IS_PAY_WEIGHT_RECEIPT_QTY", false, getAD_Client_ID())) {
			return getPayWeight();}
		else {
			if (getOriginNetWeight().compareTo(getNetWeight())>0) {
				
				return getOriginNetWeight();

			}else {
				
				return getNetWeight();
	
			}
		}
	}

	/**
	 * Get Date for transaction like delivery or receipt from record weight
	 * 
	 * @return
	 * @return Timestamp
	 */
	public Timestamp getDateForDocument() {
		// Get Date for Receipt
		// I = In Date (InDate)
		// O = Out Date (OutDate)
		String FTU_date_for_inout = MSysConfig.getValue("FTU_DATE_FOR_IN_OUT", getAD_Client_ID());
		//
		Timestamp m_InOutDate = getDateDoc();
		//
		if (FTU_date_for_inout != null) {
			if (FTU_date_for_inout.equals("I")) {
				m_InOutDate = getInDate();
			} else if (FTU_date_for_inout.equals("O")) {
				m_InOutDate = getOutDate();
			}
			// Valid null
			if (m_InOutDate == null)
				m_InOutDate = getDateDoc();
		}
		// Return
		return m_InOutDate;
	}

	@Override
	public BigDecimal getPayWeight() {
		String selectionWeight = getSelectionWeight();
		// If null
		if (selectionWeight == null)
			selectionWeight = SELECTIONWEIGHT_PaymentWeight;
		// choice weight
		if (selectionWeight.equals(SELECTIONWEIGHT_ImportWeight))
			return super.getImportWeight();
		// Payment Weight
		return super.getPayWeight();
	}
	
	public String createMMovement(MFTUEntryTicket et) {
		
			String message = null;
			
			MDDOrder ddo = new MDDOrder(getCtx(), et.get_ValueAsInt("DD_Order_ID"), get_TrxName());
			MDocType dt = new MDocType(getCtx(), ddo.getC_DocType_ID(), get_TrxName());
			
			if(dt.get_ValueAsBoolean("IsMovementAutomatic")) {
				
				MMovement mv = new MMovement(getCtx() , 0, get_TrxName());
				mv.setAD_Org_ID(ddo.getAD_Org_ID());
				mv.set_ValueOfColumn("DD_Order_ID", ddo.get_ID());
				mv.setMovementDate(new Timestamp(System.currentTimeMillis()));
				mv.set_ValueOfColumn("FTU_RecordWeight_ID", get_ID());
				mv.setC_DocType_ID(dt.get_ValueAsInt("C_DocTypeMovement_ID"));
				mv.saveEx(get_TrxName());
				
				if(et.get_ValueAsInt("DD_OrderLine_ID") > 0) {
					
					MDDOrderLine ddol = new MDDOrderLine(getCtx(), et.get_ValueAsInt("DD_OrderLine_ID"), get_TrxName());
					
					MMovementLine mml = new MMovementLine(getCtx(), 0, get_TrxName());
					mml.setM_Movement_ID(mv.get_ID());
					mml.setAD_Org_ID(ddol.getAD_Org_ID());
					mml.setLine(ddol.getLine());
					mml.setM_Product_ID(ddol.getM_Product_ID());
					mml.setM_Locator_ID(ddol.getM_Locator_ID());
					mml.setDD_OrderLine_ID(ddol.get_ID());					
					if(getFTU_Chute_ID() > 0) {
						mml.setM_LocatorTo_ID(getFTU_Chute().getM_Locator_ID());
					}else {
						mml.setM_LocatorTo_ID(ddol.getM_LocatorTo_ID());
					}
					
					//Add Support for UOM Conversion by Argenis Rodríguez
					BigDecimal rate = MUOMConversion.getProductRateFrom(getCtx(), ddol.getM_Product_ID(), getC_UOM_ID());
					
					if (rate == null)
						return "@NoUOMConversion@";
					
					BigDecimal movementQty = rate.multiply(getNetWeight());
					
					mml.setMovementQty(movementQty);
					mml.saveEx(get_TrxName());
					//End by Argenis Rodríguez
				}
			
				if (!mv.processIt(DOCACTION_Complete))
				{
					message =  mv.getProcessMsg();
				}else {
					m_processMsg = "Movimiento Creado con el Nro: "+mv.getDocumentNo();
				}
			}
		return message;
	}
	
	public String setVoidItToMMovmenete(MFTUEntryTicket et) {
		
		StringBuilder sql = new StringBuilder();
		int M_Movement_ID = 0;
		sql.append("SELECT M_Movement_ID FROM M_Movement WHERE FTU_RecordWeight_ID = ? AND DocStatus = ?");
		M_Movement_ID = DB.getSQLValue(get_TrxName(), sql.toString(), getFTU_RecordWeight_ID(), DOCSTATUS_Completed);
		
		MMovement mm = new MMovement(getCtx(), M_Movement_ID, get_TrxName());
		if(!mm.processIt(DOCACTION_Void)) {
			return mm.getProcessMsg();
		}else {
			m_processMsg = "Registro de Peso "+getDocumentNo()+" y el movimiento de inventario "+mm.getDocumentNo()+" Anulados!!";
		}
		return null;
	}

	/***
	 * get Product Lines from Load Order Lines
	 * @param allLines
	 * @return
	 */
	public MFTULoadOrderLine[] getProductLine (MFTULoadOrderLine[] allLines) {
		ArrayList<MFTULoadOrderLine> prodLine = new ArrayList<MFTULoadOrderLine>();
		for (int i = 0; i < allLines.length; i++) {
			if(allLines[i].getM_Product_ID() == getM_Product_ID())
			prodLine.add(allLines[i]);
				
			}
		MFTULoadOrderLine[] newLines = new MFTULoadOrderLine[prodLine.size()];
		prodLine.toArray(newLines);
		return  newLines;
	}
	
	/***
	 * Update Voided Line
	 */
	public void updateVoidedLine() {
		String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE FTU_LoadOrder_ID = ? AND M_Product_ID = ?";
		
		int LoadOrderLine_ID = DB.getSQLValue(get_TrxName(), sql,getFTU_LoadOrder_ID(),getM_Product_ID());
		if (LoadOrderLine_ID > 0) {
			MFTULoadOrderLine line = new MFTULoadOrderLine(getCtx(), LoadOrderLine_ID, this.get_TrxName());
			line.setConfirmedWeight(line.getConfirmedWeight().subtract(getNetWeight()));
			line.saveEx();
			}
	}

	/**
	 * 	Get Storage Info for Warehouse or locator
	 *	@param ctx context
	 *	@param M_Warehouse_ID ignore if M_Locator_ID > 0
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance id, 0 to retrieve all instance
	 *	@param minGuaranteeDate optional minimum guarantee date if all attribute instances
	 *	@param FiFo first in-first-out
	 *  @param positiveOnly if true, only return storage records with qtyOnHand > 0
	 *  @param M_Locator_ID optional locator id
	 *	@param trxName transaction
	 *  @param forUpdate
	 *	@return existing - ordered by location priority (desc) and/or guarantee date
	 */
	private MStorageOnHand[] getWarehouse (Properties ctx, int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, Timestamp minGuaranteeDate,
		boolean FiFo, boolean positiveOnly, int M_Locator_ID, String trxName, boolean forUpdate, int timeout)
	{
		if ((M_Warehouse_ID == 0 && M_Locator_ID == 0) || M_Product_ID == 0)
			return new MStorageOnHand[0];
		
		boolean allAttributeInstances = false;
		if (M_AttributeSetInstance_ID == 0)
			allAttributeInstances = true;		
		
		ArrayList<MStorageOnHand> list = new ArrayList<MStorageOnHand>();
		//	Specific Attribute Set Instance
		String sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
			+ "s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
			+ "s.QtyOnHand,s.DateLastInventory,s.M_StorageOnHand_UU,s.DateMaterialPolicy "
			+ "FROM M_StorageOnHand s"
			+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
			+ " INNER JOIN M_Warehouse w ON (l.M_Warehouse_ID = w.M_Warehouse_ID AND w.IsInTransit = 'N') "
			+ " LEFT JOIN M_LocatorType lt ON (l.M_LocatorType_ID = lt.M_LocatorType_ID) ";
		if (M_Locator_ID > 0)
			sql += "WHERE l.M_Locator_ID = ? AND (CASE WHEN l.M_LocatorType_ID IS NOT NULL THEN lt.IsAvailableForShipping = 'Y' ELSE TRUE END) ";
		else
			sql += "WHERE l.M_Warehouse_ID=? AND (CASE WHEN l.M_LocatorType_ID IS NOT NULL THEN lt.IsAvailableForShipping = 'Y' ELSE TRUE END) ";
		sql += " AND s.M_Product_ID=?";
		if (M_AttributeSetInstance_ID > 0)
			sql= sql + " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
		if (positiveOnly)
		{
			sql += " AND s.QtyOnHand > 0 ";
		}
		else
		{
			sql += " AND s.QtyOnHand <> 0 ";
		}
		sql += "ORDER BY l.PriorityNo DESC, DateMaterialPolicy ";
		if (!FiFo)
			sql += " DESC, s.M_AttributeSetInstance_ID DESC ";
		else
			sql += ", s.M_AttributeSetInstance_ID ";
		//	All Attribute Set Instances
		if (allAttributeInstances)
		{
			sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
				+ " s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
				+ " s.QtyOnHand,s.DateLastInventory,s.M_StorageOnHand_UU,s.DateMaterialPolicy "
				+ " FROM M_StorageOnHand s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID)"
				+ " LEFT JOIN M_LocatorType lt ON (l.M_LocatorType_ID = lt.M_LocatorType_ID) "
				+ " LEFT OUTER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID) ";
			if (M_Locator_ID > 0)
				sql += "WHERE l.M_Locator_ID = ? AND (CASE WHEN l.M_LocatorType_ID IS NOT NULL THEN lt.IsAvailableForShipping = 'Y' ELSE TRUE END) ";
			else
				sql += "WHERE l.M_Warehouse_ID=? AND (CASE WHEN l.M_LocatorType_ID IS NOT NULL THEN lt.IsAvailableForShipping = 'Y' ELSE TRUE END) ";
			sql += " AND s.M_Product_ID=? ";
			if (positiveOnly)
			{
				sql += " AND s.QtyOnHand > 0 ";
			}
			else
			{
				sql += " AND s.QtyOnHand <> 0 ";
			}
			
			if (minGuaranteeDate != null)
			{
				sql += "AND (asi.GuaranteeDate IS NULL OR asi.GuaranteeDate>?) ";
			}
			
			MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
			
			if(product.isUseGuaranteeDateForMPolicy()){
				sql += "ORDER BY l.PriorityNo DESC, COALESCE(asi.GuaranteeDate,s.DateMaterialPolicy)";
				if (!FiFo)
					sql += " DESC, s.M_AttributeSetInstance_ID DESC ";
				else
					sql += ", s.M_AttributeSetInstance_ID ";
			}
			else
			{
				sql += "ORDER BY l.PriorityNo DESC, l.M_Locator_ID, s.DateMaterialPolicy";
				if (!FiFo)
					sql += " DESC, s.M_AttributeSetInstance_ID DESC ";
				else
					sql += ", s.M_AttributeSetInstance_ID ";
			}
			
			sql += ", s.QtyOnHand DESC";
		} 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, M_Locator_ID > 0 ? M_Locator_ID : M_Warehouse_ID);
			pstmt.setInt(2, M_Product_ID);
			if (!allAttributeInstances)
			{
				if (M_AttributeSetInstance_ID > 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			}
			else if (minGuaranteeDate != null)
			{
				pstmt.setTimestamp(3, minGuaranteeDate);
			}
			rs = pstmt.executeQuery();
			while (rs.next())
			{	
				if(rs.getBigDecimal(11).signum() != 0)
				{
					MStorageOnHand storage = new MStorageOnHand (ctx, rs, trxName);
					if (!Util.isEmpty(trxName) && forUpdate)
					{
						DB.getDatabase().forUpdate(storage, timeout);
					}
					list.add (storage);
				}
			}	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		MStorageOnHand[] retValue = new MStorageOnHand[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getWarehouse

	/***
	 * Create Freight Cost Document
	 * @author Jorge Colmenarez, 2024-02-06 11:33
	 * @param et
	 * @return msg or null
	 */
	private String createFreightCost(MFTUEntryTicket et) {
		StringBuffer msg = new StringBuffer();
		int m_Created = 0;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(et.getFTU_PriceForTrip_ID()<=0) {
			m_processMsg = "@FTU_PriceForTrip_ID@ @Mandatory@";
			return null; 
		}
		X_FTU_PriceForTrip pft = new X_FTU_PriceForTrip(getCtx(), et.getFTU_PriceForTrip_ID(), get_TrxName());
		try {
			//	Create Cost Freight
			MFTUFreightCost cost = new MFTUFreightCost(getCtx(), 0, get_TrxName());
			cost.setAD_Org_ID(getAD_Org_ID());
			cost.setC_DocType_ID(et.getC_DocTypeTarget_ID());
			cost.setDateDoc(now);
			cost.setAD_User_ID(Env.getContextAsInt(getCtx(), "#AD_User_ID"));
			cost.setFTU_PriceForTrip_ID(pft.get_ID());
			cost.setC_Currency_ID(pft.getC_Currency_ID());
			cost.setC_ConversionType_ID(pft.getC_ConversionType_ID());
			cost.setM_Shipper_ID(et.getM_Shipper_ID());
			cost.setFTU_EntryTicket_ID(et.getFTU_EntryTicket_ID());
			cost.setFTU_LoadOrder_ID(getFTU_LoadOrder_ID());
			cost.setFTU_Driver_ID(getFTU_Driver_ID());
			cost.setFTU_Vehicle_ID(getFTU_Vehicle_ID());
			cost.setDocStatus(MFTUFreightCost.DOCSTATUS_Drafted);
			cost.setDocAction(MFTUFreightCost.ACTION_Prepare);
			cost.saveEx();
			
			//	Create Line
			MFTUFreightCostLine line = new MFTUFreightCostLine(getCtx(), 0, get_TrxName());
			line.setFTU_FreightCost_ID(cost.getFTU_FreightCost_ID());
			line.setFTU_DeliveryRute_ID(pft.getFTU_DeliveryRute_ID());
			line.setC_Charge_ID(et.getC_Charge_ID());
			line.setFTU_RecordWeight_ID(getFTU_RecordWeight_ID());
			int invID = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Invoice_ID) FROM C_Invoice WHERE DocStatus IN ('CO','CL') AND C_Order_ID = ? ", et.getC_Order_ID());
			if(invID > 0)
				line.setC_Invoice_ID(invID);
			line.setWeight(getNetWeight());
			line.setDiscountWeight(getDifferenceQty());
			line.setValueMin(pft.getValueMin());
			line.setValueMax(pft.getValueMax());
			line.setPrice(pft.getPrice());
			line.setPriceActual(pft.getPriceActual());
			line.setC_ConversionType_ID(pft.getC_ConversionType_ID());
			line.setC_Currency_ID(pft.getC_Currency_ID());
			BigDecimal rate = MConversionRate.getRate(pft.getC_Currency_ID(), cost.getC_Currency_ID(), cost.getDateDoc(), pft.getC_ConversionType_ID(), cost.getAD_Client_ID(), cost.getAD_Org_ID());
			line.setFinalPrice(pft.getPriceActual());
			line.setRate(rate);
			line.setCosts(getNetWeight().multiply(pft.getPriceActual()).setScale(4, RoundingMode.HALF_UP));
			line.saveEx();
			
			if(!cost.processIt(MFTUFreightCost.ACTION_Complete)) {
				m_processMsg = cost.getProcessMsg();
			}
			cost.saveEx();
			msg.append(cost.getDocumentInfo());
			//	Create Order Purchase
			if (!(cost.getM_Shipper().getC_BPartner_ID()>0))
				throw new AdempiereException(" la transportista no tiene un tercero asociado");
			
			MPriceList pl = new MPriceList(getCtx(), et.get_ValueAsInt("M_PriceList_ID"), get_TrxName());
			int c_currency_id = pl.getC_Currency_ID();
			
			MFTUFreightCostLine[] fcLines = cost.getLines("");
			MDocType dt = new MDocType(getCtx(), cost.getC_DocType_ID(), get_TrxName());
			if (fcLines.length>0) {
				//	Create Order from Freight Cost
				MOrder ord = new MOrder(getCtx(), 0, get_TrxName());
				ord.setAD_Org_ID(cost.getAD_Org_ID());
				ord.setC_DocTypeTarget_ID(dt.get_ValueAsInt("C_DocTypeOrder_ID"));
				ord.setIsSOTrx(false);
				ord.setBPartner((MBPartner) cost.getM_Shipper().getC_BPartner());
				ord.setM_PriceList_ID(pl.getM_PriceList_ID());
				ord.setC_ConversionType_ID(cost.getC_ConversionType_ID());
				ord.setSalesRep_ID(cost.getAD_User_ID());
				ord.setDateOrdered(now);
				ord.setDateAcct(now);
				ord.setDescription("Costo de Flete generado desde pesada: "+getDocumentInfo());
				ord.set_ValueOfColumn("FTU_FreightCost_ID", cost.getFTU_FreightCost_ID());
				ord.setDocStatus(MOrder.STATUS_Drafted);
				ord.setDocAction(MOrder.ACTION_Complete);
				ord.saveEx();
				//	Create Order Line from Freight Cost
				for (MFTUFreightCostLine fcline : fcLines) {
					MOrderLine oline = new MOrderLine(ord);
					oline.setC_Charge_ID(fcline.getC_Charge_ID());
					if (c_currency_id != cost.getC_Currency_ID()) {
						BigDecimal amt = MConversionRate.convert(getCtx(), fcline.getCosts(), cost.getC_Currency_ID(), c_currency_id,now,cost.getC_ConversionType_ID() ,cost.getAD_Client_ID(), cost.getAD_Org_ID());
						oline.setPrice(amt);
					}else {
						oline.setPrice(fcline.getCosts());
					}
					oline.setQty(Env.ONE);
					oline.saveEx();
					fcline.setC_Order_ID(ord.get_ID());
					fcline.saveEx();
				}
				
				if(!ord.processIt(MOrder.ACTION_Complete)) {
					m_processMsg = cost.getProcessMsg();
				}
				ord.saveEx();
				msg.append(" - Orden de Compra: "+ord.getDocumentInfo());
				if(et.getC_Order_ID()>0) {
					//	Create Landed Cost from Order Freight Cost
					MOrderLandedCost lcost = new MOrderLandedCost(getCtx(), 0, get_TrxName());
					lcost.setC_Order_ID(et.getC_Order_ID());
					lcost.setAD_Org_ID(getAD_Org_ID());
					lcost.set_ValueOfColumn("C_OrderSource_ID", ord.get_ID());
					lcost.setM_CostElement_ID(et.getM_CostElement_ID());
					lcost.setDescription(ord.getDescription());
					lcost.setLandedCostDistribution(MOrderLandedCost.LANDEDCOSTDISTRIBUTION_Quantity);
					lcost.setAmt(ord.getGrandTotal());
					lcost.saveEx();
					//	Create Landed Cost Allocation
					MOrderLandedCostAllocation olca = new MOrderLandedCostAllocation(getCtx(), 0, get_TrxName());
					olca.setAD_Org_ID(getAD_Org_ID());
					olca.setC_OrderLandedCost_ID(lcost.get_ID());
					olca.setC_OrderLine_ID(et.getC_OrderLine_ID());
					olca.setBase(getNetWeight());
					olca.setQty(getNetWeight());
					olca.setAmt(lcost.getAmt());
					olca.setProcessed(false);
					olca.saveEx();
				}
			}
			m_Created++;
		}catch(Exception ex) {
			m_processMsg = ex.getLocalizedMessage();
			return null;
		}
		
		return "@FTU_FreightCost_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "] ";
	}
	
	/***
	 * Update reference Receipt on Freight Cost Lines
	 * @param io
	 */
	private void updateFreightCostLine(MInOut io) {
		List<MFTUFreightCostLine> list = new Query(getCtx(), I_FTU_FreightCostLine.Table_Name, " FTU_RecordWeight_ID = ? AND EXISTS(SELECT 1 FROM FTU_FreightCost fc WHERE fc.DocStatus = 'CO' AND fc.FTU_FreightCost_ID = FTU_FreightCostLine.FTU_FreightCost_ID)", get_TrxName())
				.setParameters(io.get_ValueAsInt("FTU_RecordWeigth_ID"))
				.list();
		MFTUFreightCostLine[] fclines = list.toArray(new MFTUFreightCostLine[list.size()]);
		for(MFTUFreightCostLine line : fclines) {
			line.setM_InOut_ID(io.get_ID());
			line.saveEx();
		}
	}
	
	/****
	 * Reverse Freight Cost Allocated
	 * @throws Exception 
	 */
	private void reverseFreightCost() throws Exception {
		List<MFTUFreightCostLine> list = new Query(getCtx(), I_FTU_FreightCostLine.Table_Name, " FTU_RecordWeight_ID = ?  AND EXISTS(SELECT 1 FROM FTU_FreightCost fc WHERE fc.DocStatus = 'CO' AND fc.FTU_FreightCost_ID = FTU_FreightCostLine.FTU_FreightCost_ID)", get_TrxName())
				.setParameters(getFTU_RecordWeight_ID())
				.list();
		MFTUFreightCostLine[] fclines = list.toArray(new MFTUFreightCostLine[list.size()]);
		MFTUFreightCost fc = null;
		for(MFTUFreightCostLine line : fclines) {
			if(line.getC_Order_ID()>0) {
				MOrder o = new MOrder(getCtx(), line.getC_Order_ID(), get_TrxName());
				//	Drop Landed Cost if Exists
				int olcID = DB.getSQLValue(get_TrxName(), "SELECT C_OrderLandedCost_ID FROM C_OrderLandedCost WHERE C_OrderSource_ID = ?", o.get_ID());
				if(olcID>0) {
					MOrderLandedCost olc = new MOrderLandedCost(getCtx(), olcID, get_TrxName());
					for(MOrderLandedCostAllocation olca : olc.getLines(""))
						olca.deleteEx(true);
					olc.deleteEx(true);
				}
				//	Reverse Cost Adjustment if Exists
				int caID = DB.getSQLValue(get_TrxName(), "SELECT M_Inventory_ID FROM M_Inventory WHERE DocStatus = 'CO' AND C_Order_ID = ?", o.get_ID());
				if(caID > 0) {
					MInventory i = new MInventory(getCtx(), caID, get_TrxName());
					if(!i.processIt(MInventory.ACTION_Reverse_Correct)) {
						m_processMsg = i.getProcessMsg();
					}
					i.saveEx();
				}
				if(!o.processIt(MOrder.ACTION_Void)) {
					m_processMsg = o.getProcessMsg();
				}
				o.saveEx();
				if(fc==null)
					fc = new MFTUFreightCost(getCtx(), line.getFTU_FreightCost_ID(), get_TrxName());
				if(fc.get_ID() != line.getFTU_FreightCost_ID()) {
					if(!fc.processIt(MFTUFreightCost.ACTION_Void))
						m_processMsg = fc.getProcessMsg();
					fc.saveEx();
					fc = new MFTUFreightCost(getCtx(), line.getFTU_FreightCost_ID(), get_TrxName());
				}
			}
		}
		if(fc!=null)
			if(!fc.processIt(MFTUFreightCost.ACTION_Void))
				m_processMsg = fc.getProcessMsg();
		fc.saveEx();
	}
}
