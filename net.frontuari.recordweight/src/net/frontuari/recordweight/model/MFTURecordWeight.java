/**
 * 
 */
package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.apache.velocity.runtime.parser.node.GetExecutor;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
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
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;
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
		// ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE,
		// getC_Invoice_ID());
		// if (re == null)
		return null;
		// return re.getPDF(file);
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
		// setProcessing(false);
		return true;
	} // unlockIt

	/**
	 * Invalidate Document
	 * 
	 * @return true if success
	 */
	public boolean invalidateIt() {
		log.info("invalidateIt - " + toString());
		// setDocAction(DOCACTION_Prepare);
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

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		// Valid Weight
		String status = validWeight();
		if (m_processMsg != null)
			return status;

		// Valid Weight
		boolean isValidWeight = true;
		if (getNetWeight() == null 
				|| getNetWeight().doubleValue() == 0) {
			isValidWeight = false;
			m_processMsg = "@NetWeight@ <= 0";
			return DocAction.STATUS_Invalid;
		}
		


		// Add Support complete record weight with Shipment Guide
		if ((getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial) ||
				getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)) &&
					isValidWeight) {
			// m_processMsg = validateShipmentGuide(); 
			if (m_processMsg != null) 
				return DocAction.STATUS_InProgress;
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
		/*
		if(getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)) {
			if(getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)
					&& getM_Product().getM_AttributeSet_ID() <= 0) {
				
			} else {
				status = getHRS_Analysis().getStatus();
				if(! status.equals(X_HRS_Analysis.STATUS_Completed)) {
					m_processMsg = "@HRS_Analysis_ID@ - @Status@ " + MRefList.getListName(getCtx(), X_HRS_Analysis.STATUS_AD_Reference_ID, status);
				}
			}
		}*/
		
		boolean withDDOrder = false;		
		
		if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)) {
			
			System.out.println(getFTU_LoadOrder().getM_Product());
			
			//if(getHRS_Analysis_ID() <= 0) {
				if(getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)
						&& getFTU_LoadOrder().getM_Product().getM_AttributeSet_ID() <= 0) {
					
				} else {
					int p_HRS_Analysis_ID = MHRSAnalysis.getByEntryTicket(getFTU_EntryTicket_ID());
					System.out.println(p_HRS_Analysis_ID);
					if(p_HRS_Analysis_ID > 0) {
						setHRS_Analysis_ID(p_HRS_Analysis_ID);
					}else {
						throw new AdempiereException("No Existe un Analisis Valido Asociado al Ticket de Entrada!!");
					}
				}
			//}
			
		}

		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		/* Add by Carlos Vargas */
		if (getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)) {
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			if(et.get_ValueAsInt("DD_Order_ID") > 0) {
				withDDOrder = true;
				if(createMMovement(et) != null) {
					throw new AdempiereException("Error al Generar el Movimiento!!");
				}
			}
		}
		
		boolean isGenerateInOut = MSysConfig.getBooleanValue("FTU_GENERATE_INOUT", false, getAD_Client_ID());
		boolean isGenerateMovement = MSysConfig.getBooleanValue("FTU_GENERATE_MOVEMENT", false, getAD_Client_ID());

		log.info(toString());
		if ((getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
				// || getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(OPERATIONTYPE_ProductBulkReceipt)) && isValidWeight && isGenerateInOut && !withDDOrder) {
			// Generate Material Receipt
			String msg = createInOut();
			//
			if (m_processMsg != null)
				return DocAction.STATUS_Invalid;
			else
				m_processMsg = msg;
		}

		// Add support for generating inventory movements
		else if (getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement) && isGenerateMovement) {
			String msg = createMovement();
			if (m_processMsg != null)
				return DocAction.STATUS_Invalid;
			else
				m_processMsg = msg;
		}
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null) {
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		setDefiniteDocumentNo();
		
//		setAnalysysIDNO();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		
		createPDF();
		return DocAction.STATUS_Completed;
	} // completeIt

	/**
	 * Update Values for Load Order
	 * 
	 * @return
	 * @return String
	 */
	private String updateLoadOrder() {
		// Valid Operation Type
		if (!getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
				&& !getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)
				&& !getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement))
			return null;
		// Get Orders From Load Order
		MFTULoadOrder lo = (MFTULoadOrder) getFTU_LoadOrder();
		if (lo == null || lo.get_ID() <= 0) {
			return "@FTU_LoadOrder_ID@ @NotFound@";
		}
		//
		lo.setIsWeightRegister(true);
		lo.setConfirmedWeight(getNetWeight());
		// Save
		lo.saveEx(get_TrxName());
		//
		return null;
	}

	/**
	 * Add Support complete record weight with Shipment Guide
	 * 
	 * @return
	 * @return String
	 */
	/*
	 * private String validateShipmentGuide() { // Valid Order int
	 * m_FTU_LoadOrder_ID = getFTU_LoadOrder_ID(); if (m_FTU_LoadOrder_ID == 0)
	 * return null; // MFTULoadOrder m_FTU_LoadOrder = new MFTULoadOrder(getCtx(),
	 * m_FTU_LoadOrder_ID, get_TrxName()); MDocType m_DocType =
	 * MDocType.get(getCtx(), m_FTU_LoadOrder.getC_DocType_ID()); // Valid just
	 * Check if (m_DocType.get_ValueAsBoolean("IsGenerateShipmentGuide") &&
	 * !MSysConfig.getBooleanValue("FTU_MANDATORY_SHIPMENT_GUIDE", false,
	 * getAD_Client_ID())) return null; // String sql =
	 * "SELECT mg.FTU_MobilizationGuide_ID" + "	FROM FTU_RecordWeight rw" +
	 * "	INNER JOIN FTU_MobilizationGuide mg ON (rw.FTU_LoadOrder_ID = mg.FTU_LoadOrder_ID)"
	 * + "	WHERE" + "		mg.DocStatus IN ('CO','CL')" +
	 * "		AND mg.IsSotrx = 'Y'" + "		AND rw.FTU_RecordWeight_ID=?";
	 * 
	 * int shipmentGuide_ID = DB.getSQLValue(get_TrxName(), sql, get_ID()); if
	 * (shipmentGuide_ID > 0) return null; else return
	 * "@FTU_MobilizationGuide_ID@ @NotFound@"; }
	 */

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
		if ((getGrossWeight() == null || getGrossWeight().compareTo(Env.ZERO) == 0) && !isI_IsImported()) {
			m_processMsg = "@GrossWeight@ = @0@";
			return (!isSOTrx() ? DocAction.STATUS_Invalid : DocAction.STATUS_InProgress);
		} else if ((getTareWeight() == null || getTareWeight().compareTo(Env.ZERO) == 0) && !isI_IsImported()) {
			m_processMsg = "@TareWeight@ = @0@";
			return (!isSOTrx() ? DocAction.STATUS_InProgress : DocAction.STATUS_Invalid);
		} else if (getNetWeight().compareTo(Env.ZERO) < 0) {
			m_processMsg = "@NetWeight@ < @0@";
			return DocAction.STATUS_Invalid;
		}
		return null;
	}

	/**
	 * Set the Analysis Id number after completed
	 * 
	 * @return
	 */
	private String setAnalysysIDNO() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT "
				+ "	c.hrs_analysis_id , c.IsValidAnalysis, c.documentno,c.docstatus   "
				+ "FROM HRS_Analysis c "
				+ "WHERE FTU_EntryTicket_ID =  " + getFTU_EntryTicket_ID();

		pstmt = DB.prepareStatement(sql, get_TrxName());
		int m_HRS_Analysis_ID = 0;
		String m_IsValidAnalysis = "";
		String m_documentno = "";
		String m_docstatus = "";

		try {
			rs = pstmt.executeQuery();

			if (rs.next()) {
				m_HRS_Analysis_ID = rs.getInt("hrs_analysis_id");
				m_IsValidAnalysis = rs.getString("IsValidAnalysis");
				m_documentno = rs.getString("documentno");
				m_docstatus = rs.getString("docstatus");
				}
		} catch (Exception e) {

		}

		if (m_HRS_Analysis_ID > 0) {
			setHRS_Analysis_ID(m_HRS_Analysis_ID);
		}

		if (m_IsValidAnalysis.isBlank() || m_IsValidAnalysis.equals("N")) {

			if (m_docstatus.isBlank() || m_docstatus.equals("DR")) {
				throw new AdempiereException(" Analisis, " + m_documentno + " no Completado ");
			}
//			System.out.println("no Valido");
			throw new AdempiereException(" Analisis, " + m_documentno + " no Valido");
		}

		return "";

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
				|| getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)) {
			// Valid QualityAnalysis Reference
			m_processMsg = validQAReference();
			if (m_processMsg != null)
				return false;
			// Valid Mobilization Guide Reference
			m_processMsg = validMGReference();
			if (m_processMsg != null)
				return false;
		} else if (getOperationType().equals(OPERATIONTYPE_RawMaterialReceipt)) {
			// Valid QualityAnalysis Reference
			
			//Valid form exists QA Carlos Vargas
			
			/*m_processMsg = validQAReference();
			if (m_processMsg != null)
				return false;*/
		}
		// Reverse only M_InOut record
		if (!getOperationType().equals(OPERATIONTYPE_MaterialInputMovement)
				&& !getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)
				&& !getOperationType().equals(OPERATIONTYPE_OtherRecordWeight)) {
			// Reverse In/Out
			m_processMsg = reverseInOut();
			if (m_processMsg != null)
				return false;
		}
		// Add support for reactivate Movement
		else if (getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)) {
			// Reverse Movement
			m_processMsg = reverseMovement();
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
		
		if(et.get_ValueAsInt("DD_Order_ID") > 0) {
			setVoidItToMMovmenete(et);
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
				&& !getOperationType().equals(OPERATIONTYPE_OtherRecordWeight)) {
			m_processMsg = reverseInOut();
			if (m_processMsg != null)
				return false;
		}

		// Add support for reactivate Movement
		else if (getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)) {
			// Reverse Movement
			m_processMsg = reverseMovement();
			if (m_processMsg != null)
				return false;
		}
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
		
		if(et.get_ValueAsInt("DD_Order_ID") > 0) {
			setVoidItToMMovmenete(et);
		}

		setFTU_RW_ApprovalMotive_ID(-1);
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
			mInOut.processIt(X_M_InOut.DOCACTION_Reverse_Correct);
			mInOut.saveEx();
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
		if (getFTU_EntryTicket() != null) {
			msg = validETReferenceDuplicated();
		}
		// Operation Type
		else if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialOutputMovement)) {

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
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialOutputMovement))
			setIsSOTrx(true);
		
		/*if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)) {
			if(getHRS_Analysis_ID() <= 0) {
				if(getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)
						&& getM_Product().getM_AttributeSet_ID() <= 0) {
					
				} else {
					int p_HRS_Analysis_ID = MHRSAnalysis.getByEntryTicket(getFTU_EntryTicket_ID());
					if(p_HRS_Analysis_ID > 0)
						setHRS_Analysis_ID(p_HRS_Analysis_ID);
					else 
						msg = "@HRS_Analysis_ID@ @NotFound@";
				}
			}
		}*/
		
		
		// If msg is distinct of null display Adempiere Exception with msg
		if (msg != null)
			throw new AdempiereException(msg);
		
		//FR [7]
		if (getFTU_RW_ApprovalMotive_ID() != 0){
			X_FTU_RW_ApprovalMotive approval = (X_FTU_RW_ApprovalMotive)getFTU_RW_ApprovalMotive();  
			setIsApproved(approval.isApproved());
		}
		
		return true;
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
				m_Current_Movement = new MMovement(getCtx(), 0, get_TrxName());
				m_Current_Movement.setC_DocType_ID(m_C_DocTypeMovement_ID);
				m_Current_Movement.setDateReceived(getDateForDocument());
				// Set Organization
				m_Current_Movement.setAD_Org_ID(getAD_Org_ID());
				m_Current_Movement.setDD_Order_ID(m_DD_Order.get_ID());
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
					msg.append(" - " + m_Current_Movement.getDocumentNo());
				else
					msg.append(m_Current_Movement.getDocumentNo());
			}
			// Valid exist Movement
			if (m_Current_Movement != null) {
				// Create Line
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
				m_MovementLine.setMovementQty(line.getQty());
				m_MovementLine.saveEx();
				// Reference
				line.setM_MovementLine_ID(m_MovementLine.getM_MovementLine_ID());
				line.setConfirmedQty(line.getQty());
				line.saveEx();
			}

		} // Create
			// Complete Movement
		completeMovement(m_Current_Movement);
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
			}
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
			MInOut m_Receipt = new MInOut(order, m_DocType.getC_DocTypeShipment_ID(), getDateForDocument());
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
			if (!validateOrderedQty) {
				BigDecimal qtyOrdered = oLine.getQtyDelivered().add(oLine.getQtyReserved());
				if (m_MovementQty.compareTo(qtyOrdered) > 0) {
					throw new IllegalStateException("Total matched delivered qty > ordered qty. MatchedDeliveredQty="
							+ m_MovementQty + ", OrderedQty=" + oLine.getQtyOrdered() + ", Line=" + oLine);
				}
			}

			BigDecimal qtyOnHand = MStorageOnHand.getQtyOnHandForLocator(product.getM_Product_ID(), m_M_Locator_ID, 0, get_TrxName());
			if(qtyOnHand.add(m_MovementQty).compareTo(chute.getQty()) > 0 ) 
				throw new AdempiereException("@FTU_Chute_ID@ @QtyQ="+ chute.getQty() +", < "+ qtyOnHand.add(m_MovementQty) );
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
			m_Receipt.processIt(DocAction.ACTION_Complete);
			m_Receipt.saveEx(get_TrxName());

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
			MInOut m_Receipt = new MInOut(order, m_DocType.getC_DocTypeShipment_ID(), getDateForDocument());
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
			BigDecimal qtyOnHand = 
					MStorageOnHand.getQtyOnHandForLocator(product.getM_Product_ID(),
							m_M_Locator_ID, 0, get_TrxName());
			if(qtyOnHand.add(m_MovementQty).compareTo(chute.getQty()) > 0 ) 
				throw new AdempiereException("@FTU_Chute_ID@ @QtyQ="+ chute.getQty() +", < "+ m_MovementQty );

			// Set Product
			ioLine.setProduct(product);
			/*
			 * if (asi != null)
			 * ioLine.setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
			 */
			ioLine.setM_Locator_ID(m_M_Locator_ID);
			MOrderLine[] orderLines = order.getLines("AND M_Product_ID=" + product.getM_Product_ID(), "");
			for (int i = 0; i < orderLines.length; i++)
				ioLine.setC_OrderLine_ID(orderLines[i].getC_OrderLine_ID());
			// Set Quantity
			ioLine.setQty(m_MovementQty);
			ioLine.saveEx(get_TrxName());
			// Manually Process Shipment
			m_Receipt.processIt(DocAction.ACTION_Complete);
			m_Receipt.saveEx(get_TrxName());

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
				MInOut m_Receipt = new MInOut(order, m_DocType.getC_DocTypeShipment_ID(), getDateForDocument());
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
				BigDecimal m_Qty = m_MovementQty.multiply(orderRate);

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
				/*
				 * if (asi != null)
				 * ioLine.setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
				 */

				ioLine.setC_OrderLine_ID(lol[i].getC_OrderLine_ID());

				// Set Quantity
				ioLine.setC_UOM_ID(oLine.getC_UOM_ID());
				ioLine.setQty(m_MovementQty);
				ioLine.setQtyEntered(m_Qty);
				ioLine.setM_Locator_ID(m_MovementQty);
				//
				ioLine.saveEx(get_TrxName());
				// Manually Process Shipment
				m_Receipt.processIt(DocAction.ACTION_Complete);
				m_Receipt.saveEx(get_TrxName());

				lol[i].setConfirmedQty(m_MovementQty);
				lol[i].setM_InOutLine_ID(ioLine.getM_InOutLine_ID());
				lol[i].saveEx(get_TrxName());

				l_DocumentNo = " - " + l_DocumentNo + "@M_InOut_ID@: " + m_Receipt.getDocumentNo();

			} // Create Shipments

			lo.setIsDelivered(true);
			lo.save(get_TrxName());
		}
		// Delivery Finished Product
		else if (getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)) {
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
		for (MFTULoadOrderLine line : lines) {
			// Valid Document Order and Business Partner
			MOrderLine oLine = (MOrderLine) line.getC_OrderLine();
			//
			if (m_Current_BPartner_ID != oLine.getC_BPartner_ID()
					|| m_Current_Warehouse_ID != oLine.getM_Warehouse_ID()) {
				// Complete Previous Shipment
				completeShipment(m_Current_Shipment);
				// Initialize Order and
				m_Current_Warehouse_ID = oLine.getM_Warehouse_ID();
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
				BigDecimal m_MovementQty = m_Qty.multiply(rate);
				shipmentLine.setM_Warehouse_ID(m_Current_Shipment.getM_Warehouse_ID());
				shipmentLine.setC_UOM_ID(product.getC_UOM_ID());
				shipmentLine.setQty(line.getQty());
				// References
				shipmentLine.setM_Locator_ID(m_MovementQty);
				shipmentLine.setC_OrderLine_ID(line.getC_OrderLine_ID());
				// Save Line
				shipmentLine.saveEx(get_TrxName());

				// Manually Process Shipment
				line.setConfirmedQty(m_MovementQty);
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
			m_Current_Shipment.setDocAction(X_M_InOut.DOCACTION_Complete);
			m_Current_Shipment.processIt(X_M_InOut.DOCACTION_Complete);
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

		if (MSysConfig.getBooleanValue("FTU_IS_PAY_WEIGHT_RECEIPT_QTY", false, getAD_Client_ID()))
			return getPayWeight();
		else
			return getNetWeight();
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
		
		
			MDDOrder ddo = new MDDOrder(getCtx(), et.get_ValueAsInt("DD_Order_ID"), get_TrxName());
			MDocType dt = new MDocType(getCtx(), ddo.getC_DocType_ID(), get_TrxName());
			
			System.out.println(dt.get_Value("IsMovementAutomatic"));
			
			if(dt.get_Value("IsMovementAutomatic").equals(true)) {
				
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
					
					System.out.println(getFTU_Chute_ID());
					
					if(getFTU_Chute_ID() > 0) {
						mml.setM_LocatorTo_ID(getFTU_Chute().getM_Locator_ID());
					}else {
						mml.setM_LocatorTo_ID(ddol.getM_LocatorTo_ID());
					}
					
					mml.setMovementQty(getNetWeight());
					mml.saveEx(get_TrxName());
					
					double currentQty = ddol.getQtyDelivered().doubleValue();
					currentQty = (currentQty + getNetWeight().doubleValue());
					
					ddol.setQtyDelivered(new BigDecimal(currentQty));
					
					ddol.saveEx(get_TrxName());
					
					
				}
			
				if  (!mv.processIt(DOCACTION_Complete))
				{
					return mv.getProcessMsg();
				}else {
					m_processMsg = "Movimiento Creado con el Nro: "+mv.getDocumentNo();
				}
				
			}
		
		
		return null;
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
			if(et.get_ValueAsInt("DD_OrderLine_ID") > 0) {
				MDDOrderLine ddol = new MDDOrderLine(getCtx(), et.get_ValueAsInt("DD_OrderLine_ID"), get_TrxName());
				double newQtyDelivered = (ddol.getQtyDelivered().doubleValue() - getNetWeight().doubleValue());
				ddol.setQtyDelivered(new BigDecimal(newQtyDelivered));
			}
			m_processMsg = "Registro de Peso "+getDocumentNo()+" y el movimiento de inventario "+mm.getDocumentNo()+" Anulados!!";
		}
		
		return null;
		
	}

}
