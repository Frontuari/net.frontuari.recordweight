/**
 * 
 */
package net.frontuari.recordweight.callouts;

import org.compiere.model.MInOutLine;
import org.compiere.util.DB;
import org.eevolution.model.X_PP_Order;

import net.frontuari.recordweight.base.CustomCallout;

import net.frontuari.recordweight.model.I_HRS_Analysis;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.X_HRS_Analysis;

/**
 * @author jruiz
 *
 */
public class CalloutAnalysis extends CustomCallout {

	@Override
	protected String start() {
		if(getColumnName().equals(I_HRS_Analysis.COLUMNNAME_PP_Order_ID)) {
			Integer m_PP_Order_ID = (Integer) getValue();
			if (m_PP_Order_ID == null || m_PP_Order_ID.intValue() == 0)
				return "";
			
			X_PP_Order ppOrder = new X_PP_Order(getCtx(), m_PP_Order_ID, null);
			if(ppOrder.getM_Product_ID() > 0) 
				setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, ppOrder.getM_Product_ID());
			
		} 
		if(getColumnName().equals(I_HRS_Analysis.COLUMNNAME_FTU_EntryTicket_ID)) {
			Integer m_FTU_EntryTicket_ID = (Integer) getValue();
			if (m_FTU_EntryTicket_ID == null || m_FTU_EntryTicket_ID.intValue() == 0)
				return "";
			
			MFTUEntryTicket mEntryTicket = new MFTUEntryTicket(getCtx(), m_FTU_EntryTicket_ID, null);
			int m_Product_ID = -1;
			if(mEntryTicket.getOperationType().contentEquals(X_HRS_Analysis.OPERATIONTYPE_DeliveryBulkMaterial)) {
				String sql = "SELECT FTU_LoadOrder.M_Product_ID "
						+ "FROM FTU_LoadOrder "
						+ "INNER JOIN FTU_LoadOrderLine ON (FTU_LoadOrder.FTU_LoadOrder_ID = FTU_LoadOrderLine.FTU_LoadOrder_ID) "
						+ "WHERE FTU_LoadOrder.FTU_EntryTicket_ID = ?"
						;
				m_Product_ID = DB.getSQLValue(mEntryTicket.get_TrxName(), sql, mEntryTicket.getFTU_EntryTicket_ID());
			} else if(mEntryTicket.getOperationType().contentEquals(X_HRS_Analysis.OPERATIONTYPE_RawMaterialReceipt)
					|| mEntryTicket.getOperationType().contentEquals(X_HRS_Analysis.OPERATIONTYPE_ProductBulkReceipt)) {
				m_Product_ID = mEntryTicket.getM_Product_ID();
			}
			if(m_Product_ID > 0 )
				setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, m_Product_ID );
		}
		if(getColumnName().equals("M_InOutLine_ID"))
		{
			Integer m_InOutLine_ID = (Integer) getValue();
			if(m_InOutLine_ID == null || m_InOutLine_ID.intValue() == 0)
				return "";
			
			MInOutLine iol = new MInOutLine(getCtx(), m_InOutLine_ID.intValue(), null);
			setValue(I_HRS_Analysis.COLUMNNAME_M_Product_ID, iol.getM_Product_ID());
			setValue("M_Warehouse_ID", iol.getM_Locator().getM_Warehouse_ID());
			setValue("M_Locator_ID", iol.getM_Locator_ID());
			
		}
		return "";
	}

}
