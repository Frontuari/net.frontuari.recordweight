package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MClientInfo;
import org.compiere.model.MDocType;
import org.compiere.model.MProduct;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.I_FTU_LoadOrder;
import net.frontuari.recordweight.model.I_FTU_RecordWeight;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTUMobilizationGuide;
import net.frontuari.recordweight.model.MFTURecordWeight;
import net.frontuari.recordweight.model.X_FTU_RecordWeight;

/** 
 *
 */
@org.adempiere.base.annotation.Process
public class LoadOrderGuideGenerate extends FTUProcess {

	/**	Organization 				*/
	private int 		p_AD_Org_ID				= 0;

	/**	Organization Transaction	*/
	private int 		p_AD_OrgTrx_ID			= 0;
	
	/**	Warehouse					*/
	private int 		p_M_Warehouse_ID		= 0;
	
	/**	Load Order					*/
	private int 		p_FTU_LoadOrder_ID		= 0;
	
	/**	Record Weight				*/
	private int 		p_FTU_RecordWeight_ID	= 0;
	
	/**	Document Type Target		*/
	private int 		p_C_DocTypeTarget_ID	= 0;
	
	/**	Document Date				*/
	private Timestamp 	p_DateDoc 				= null;
	
	private BigDecimal 			p_Qty 			= Env.ZERO;

	private BigDecimal 			m_MaxReceipt 	= Env.ZERO;
	
	private MFTURecordWeight 	m_RecordWeight 	= null;
	
	private MFTULoadOrder 		m_LoadOrder 	= null;
	
	private String 				msg				= "";
	
	private int 				created 		= 0;
	
	private boolean isSOTrx = true;
	
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = para.getParameterAsInt();
			else if (name.equals("AD_OrgTrx_ID"))
				p_AD_OrgTrx_ID = para.getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para.getParameterAsInt();
			else if (name.equals("FTU_LoadOrder_ID"))
				p_FTU_LoadOrder_ID = para.getParameterAsInt();
			else if (name.equals("FTU_RecordWeight_ID"))
				p_FTU_RecordWeight_ID = para.getParameterAsInt();
			else if (name.equals("C_DocTypeTarget_ID"))
				p_C_DocTypeTarget_ID = para.getParameterAsInt();
			else if (name.equals("DateDoc"))
				p_DateDoc = (Timestamp) para.getParameter();
		}
		//	Get Record Identifier
		if(getRecord_ID() != 0){
			if(getTable_ID() == I_FTU_RecordWeight.Table_ID){
				p_FTU_RecordWeight_ID = getRecord_ID();
			}
			else if(getTable_ID() == I_FTU_LoadOrder.Table_ID)
				p_FTU_LoadOrder_ID = getRecord_ID();
		}
		
		m_MaxReceipt = new BigDecimal(MSysConfig.getValue("QUANTITY_TO_GENERATE_SHIPMENT_GUIDE", "0", Env.getAD_Client_ID(Env.getCtx())));
		if(m_MaxReceipt != null
				&& m_MaxReceipt.compareTo(Env.ZERO) <= 0)
			m_MaxReceipt = Env.ZERO;
				
	}
	
	@Override
	protected String doIt() throws Exception {
		
		if(p_FTU_RecordWeight_ID != 0){
			m_RecordWeight = new MFTURecordWeight(getCtx(), p_FTU_RecordWeight_ID, get_TrxName());
			if(p_AD_Org_ID == 0){
				p_AD_Org_ID = m_RecordWeight.getAD_Org_ID();
				p_AD_OrgTrx_ID = p_AD_Org_ID;
			}
		}
		//	Get Load Order
		if(m_RecordWeight != null)
			p_FTU_LoadOrder_ID = m_RecordWeight.getFTU_LoadOrder_ID();
		//	Valid Load Order
		if(p_FTU_LoadOrder_ID == 0)
			throw new AdempiereUserError("@FTU_LoadOrder_ID@ @NotFound@");
		//	Instance Load Order
		m_LoadOrder = new MFTULoadOrder(getCtx(), p_FTU_LoadOrder_ID, get_TrxName());
		//	Valid Order
		MDocType m_DocType = MDocType.get(getCtx(), m_LoadOrder.getC_DocType_ID());
		//	Valid just Check
		if(!m_DocType.get_ValueAsBoolean("IsGenerateShipmentGuide"))
			return "OK";
		
	
		//	Valid if this yet generated
		int mobilizationGuide_ID = DB.getSQLValue(get_TrxName(), "SELECT MAX(mg.FTU_MobilizationGuide_ID) " +
				"FROM FTU_MobilizationGuide mg " +
				"WHERE mg.DocStatus NOT IN('VO', 'RE') " +
				"AND mg.FTU_LoadOrder_ID = ?", p_FTU_LoadOrder_ID);
		//	
		if(mobilizationGuide_ID > 0)
			return "@IsGenerated@";
		//	
		if(p_AD_Org_ID == 0){
			p_AD_Org_ID = m_LoadOrder.getAD_Org_ID();
			p_AD_OrgTrx_ID = p_AD_Org_ID;
		}
		
		//	If is Record Weight
		if(m_LoadOrder.isHandleRecordWeight()
				&& m_LoadOrder.getOperationType()
						.equals(X_FTU_RecordWeight.OPERATIONTYPE_DeliveryBulkMaterial)) {
			//	Valid Record Weight
			if(m_RecordWeight == null) {
				p_FTU_RecordWeight_ID = DB.getSQLValue(get_TrxName(), "SELECT MAX(rw.FTU_RecordWeight_ID) " +
						"FROM FTU_RecordWeight rw " +
						"WHERE rw.DocStatus NOT IN('VO', 'RE','CL') " +
						"AND rw.FTU_LoadOrder_ID = ?", p_FTU_LoadOrder_ID);
				if(p_FTU_RecordWeight_ID > 0)
					m_RecordWeight = new MFTURecordWeight(getCtx(), p_FTU_RecordWeight_ID, get_TrxName());
			}
			//	Valid Record Weight
			if(m_RecordWeight == null)
				throw new AdempiereUserError("@FTU_RecordWeight_ID@ @NotFound@ @FTU_LoadOrder_ID@ @IsHandleRecordWeight@");
			//	
			MClientInfo m_ClientInfo = MClientInfo.get(getCtx());
			if(m_ClientInfo.getC_UOM_Weight_ID() == 0)
				return "@C_UOM_Weight_ID@ @NotFound@";
			//	Get Category
			MProduct product = null;
			if(m_RecordWeight.getM_Product_ID() != 0)
				product = MProduct.get(getCtx(), m_RecordWeight.getM_Product_ID());
			else
				product = MProduct.get(getCtx(), m_LoadOrder.getM_Product_ID());
			//	Rate Convert
			BigDecimal rate = MUOMConversion.getProductRateFrom(Env.getCtx(), 
					product.getM_Product_ID(), m_ClientInfo.getC_UOM_Weight_ID());
			MUOM uom = MUOM.get(getCtx(), product.getC_UOM_ID());
			//	Set Precision
			int precision = uom.getStdPrecision();
			//	Valid Conversion
			if(rate == null)
				throw new AdempiereUserError("@NoUOMConversion@");
				
			
			p_Qty = m_RecordWeight.getNetWeight()
					.multiply(rate)
					.setScale(precision);
			//	Get Weight
		} else {
			p_Qty = m_LoadOrder.getWeight();
		}

		BigDecimal acum = Env.ZERO;
		BigDecimal acum2 = Env.ZERO;
		BigDecimal acum3 = Env.ZERO;
		boolean  b = false;
		while(m_MaxReceipt.compareTo(Env.ZERO) >= 0
				&& !b) {
			if(m_MaxReceipt.compareTo(Env.ZERO) == 0){
				generateGuide(p_Qty);
				break;
			}else 
				acum = acum.add(m_MaxReceipt);
			if(p_Qty.compareTo(acum) > 0){
				acum2 = m_MaxReceipt;
				acum3 = p_Qty.subtract(acum);
				generateGuide(acum2);
				if(p_Qty.compareTo(m_MaxReceipt) == 0)
					b = true;
			}
			else if((acum3.compareTo(acum) <= 0) && (acum3.compareTo(Env.ZERO) > 0)) {
				generateGuide(acum3);
				b = true;
			}
			else if(p_Qty.compareTo(acum) <= 0){
				generateGuide(p_Qty);
				b = true;
			}else {
				b = true;
			}
		}
	
		return msg;
		
	}

	private String generateGuide(BigDecimal p_Qty) {
		//	Create Guide
		//instance load order to gather values
		MFTULoadOrder order = new MFTULoadOrder(getCtx(), p_FTU_LoadOrder_ID, get_TrxName());
		MFTUEntryTicket ticket = (MFTUEntryTicket) order.getFTU_EntryTicket();
		MDocType dt = new MDocType(getCtx(), p_C_DocTypeTarget_ID, get_TrxName());
		//check issotrx by docTypeBase 
		
		isSOTrx = dt.getDocBaseType().equalsIgnoreCase("DGD") ? true : false;
		//
		MFTUMobilizationGuide m_MobilizationGuide = new MFTUMobilizationGuide(getCtx(), 0, get_TrxName());
		m_MobilizationGuide.setAD_Org_ID(p_AD_Org_ID);
		m_MobilizationGuide.setAD_OrgTrx_ID(p_AD_OrgTrx_ID);
		m_MobilizationGuide.setC_DocType_ID(p_C_DocTypeTarget_ID);
		m_MobilizationGuide.setDateDoc(p_DateDoc);
		m_MobilizationGuide.setFTU_VehicleType_ID(m_LoadOrder.getFTU_VehicleType_ID());
		m_MobilizationGuide.setIsSOTrx(isSOTrx);
		m_MobilizationGuide.setC_BPartner_ID(ticket.getC_BPartner_ID());
		//	Set References
		m_MobilizationGuide.setFTU_LoadOrder_ID(p_FTU_LoadOrder_ID);
		//	Get Record Weight
		if(m_RecordWeight != null)
			m_MobilizationGuide.setFTU_RecordWeight_ID(m_RecordWeight.getFTU_RecordWeight_ID());
		else {
			p_FTU_RecordWeight_ID = DB.getSQLValue(get_TrxName(), "SELECT MAX(rw.FTU_RecordWeight_ID) " +
					"FROM FTU_RecordWeight rw " +
					"WHERE rw.DocStatus NOT IN('VO', 'RE','CL') " +
					"AND rw.FTU_LoadOrder_ID = ?", p_FTU_LoadOrder_ID);
			if(p_FTU_RecordWeight_ID > 0){
				m_RecordWeight = new MFTURecordWeight(getCtx(), p_FTU_RecordWeight_ID, get_TrxName());
				m_MobilizationGuide.setFTU_RecordWeight_ID(p_FTU_RecordWeight_ID);
			}
		}
		//	Set Warehouse
		if(p_M_Warehouse_ID != 0)
			m_MobilizationGuide.setM_Warehouse_ID(p_M_Warehouse_ID);
		

		m_MobilizationGuide.setQtyToDeliver(p_Qty);
		
		//	
		m_MobilizationGuide.saveEx();
		//	Complete Document
		m_MobilizationGuide.processIt(DocAction.ACTION_Prepare);
		m_MobilizationGuide.saveEx();
		//	Message
		msg = m_MobilizationGuide.getProcessMsg();
		if(msg != null)
			return "@Error@: " + msg;
		
		created++;
		
		msg = "@Created@ " + created;
		
		return msg;
	}
}
