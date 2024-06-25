/**
 * 
 */
package net.frontuari.recordweight.callouts;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.MDocType;
import org.compiere.model.MProduction;
import org.compiere.util.DB;
import org.eevolution.model.X_PP_Order;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.I_HRS_Analysis;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.X_HRS_Analysis;

/**
 * @author Jorge Colmenarez, 2023-06-07 15:13
 */
@Callout(tableName = I_HRS_Analysis.Table_Name, columnName = {I_HRS_Analysis.COLUMNNAME_PP_Order_ID,
		I_HRS_Analysis.COLUMNNAME_FTU_EntryTicket_ID, I_HRS_Analysis.COLUMNNAME_C_DocType_ID
		,I_HRS_Analysis.COLUMNNAME_M_Production_ID})
public class CalloutAnalysis extends FTUCallout {

	@Override
	protected String start() {
		if(getColumnName().equals(I_HRS_Analysis.COLUMNNAME_PP_Order_ID)) {
			Integer m_PP_Order_ID = (Integer) getValue();
			if (m_PP_Order_ID == null || m_PP_Order_ID.intValue() == 0)
				return "";
			
			X_PP_Order ppOrder = new X_PP_Order(getCtx(), m_PP_Order_ID, null);
			if(ppOrder.getM_Product_ID() > 0) 
				setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, ppOrder.getM_Product_ID());
			//	Added by Jorge Colmenarez, 2022-12-03 09:11
			if(ppOrder.getM_AttributeSetInstance_ID()>0)
				setValue(I_HRS_Analysis.COLUMNNAME_Analysis_ID, ppOrder.getM_AttributeSetInstance_ID());
			//	End Jorge Colmenarez
		} 
		if(getColumnName().equals(I_HRS_Analysis.COLUMNNAME_M_Production_ID)) {
			Integer m_M_Production_ID = (Integer) getValue();
			if (m_M_Production_ID == null || m_M_Production_ID.intValue() == 0)
				return "";
			
			MProduction production = new MProduction(getCtx(), m_M_Production_ID, null);
			if(production.getM_Product_ID() > 0) 
				setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, production.getM_Product_ID());
			//	Added by Jorge Colmenarez, 2022-12-03 09:11
			int asiID = DB.getSQLValue(null, "SELECT MAX(M_AttributeSetInstance_ID) FROM M_ProductionLine WHERE IsEndProduct = 'Y' AND M_Production_ID = ? AND M_Product_ID = ?", production.get_ID(),production.getM_Product_ID());
			if(asiID>0)
				setValue(I_HRS_Analysis.COLUMNNAME_Analysis_ID, asiID);
			//	End Jorge Colmenarez
		} 
		if(getColumnName().equals(I_HRS_Analysis.COLUMNNAME_FTU_EntryTicket_ID)) {
			Integer m_FTU_EntryTicket_ID = (Integer) getValue();
			if (m_FTU_EntryTicket_ID == null || m_FTU_EntryTicket_ID.intValue() == 0)
				return "";
			
			MFTUEntryTicket mEntryTicket = new MFTUEntryTicket(getCtx(), m_FTU_EntryTicket_ID, null);
			int m_Product_ID = -1;
			if(mEntryTicket.getOperationType().contentEquals(X_HRS_Analysis.OPERATIONTYPE_DeliveryBulkMaterial)) {
				String sql = "SELECT lo.M_Product_ID "
						+ "FROM FTU_LoadOrder lo "
						+ "INNER JOIN FTU_LoadOrderLine lol ON (lo.FTU_LoadOrder_ID = lol.FTU_LoadOrder_ID) "
						+ "WHERE lo.FTU_EntryTicket_ID = ?";
				m_Product_ID = DB.getSQLValue(mEntryTicket.get_TrxName(), sql, mEntryTicket.getFTU_EntryTicket_ID());
			} 
			else if(mEntryTicket.getOperationType().contentEquals(X_HRS_Analysis.OPERATIONTYPE_RawMaterialReceipt)
					|| mEntryTicket.getOperationType().contentEquals(X_HRS_Analysis.OPERATIONTYPE_ProductBulkReceipt)) {
				m_Product_ID = mEntryTicket.getM_Product_ID();
			}
			if(m_Product_ID > 0)
				setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, m_Product_ID );
			//	Added by Jorge Colmenarez, 2022-12-03 09:06
			if(mEntryTicket.getC_BPartner_ID()>0)
				setValue("C_BPartner_ID",mEntryTicket.getC_BPartner_ID());
			if(mEntryTicket.getC_BPartner_ID()<=0) {
				if(mEntryTicket.getC_Order_ID()>0)
					setValue("C_BPartner_ID",mEntryTicket.getC_Order().getC_BPartner_ID());
				if(mEntryTicket.getDD_Order_ID()>0)
					setValue("C_BPartner_ID",mEntryTicket.getDD_Order().getC_BPartner_ID());
			}
			if(mEntryTicket.getC_OrderLine_ID()>0) {
				setValue("Qty", mEntryTicket.getC_OrderLine().getQtyOrdered());
				if(mEntryTicket.getC_OrderLine().getM_AttributeSetInstance_ID()>0)
					setValue(I_HRS_Analysis.COLUMNNAME_Analysis_ID, mEntryTicket.getC_OrderLine().getM_AttributeSetInstance_ID());
			}
			//	Only for MIM Operation Type
			if(mEntryTicket.getFTU_LoadOrder_ID()>0) {
				MFTULoadOrder lo = new MFTULoadOrder(getCtx(), mEntryTicket.getFTU_LoadOrder_ID(), null);
				setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, lo.getM_Product_ID());
				setValue("C_BPartner_ID",lo.getFTU_EntryTicket().getDD_Order().getC_BPartner_ID());
				setValue("Qty", lo.getConfirmedWeight());
			}
			//	End Jorge Colmenarez
		}
		if(getColumnName().equals(I_HRS_Analysis.COLUMNNAME_C_DocType_ID)) {
			if(getValue()!=null) {
				MDocType dt = new MDocType(getCtx(), (Integer)getValue(), null);
				setValue("IsCompletePercent", dt.get_ValueAsBoolean("IsCompletePercent"));
			}
		}
		
		return "";
	}

}
