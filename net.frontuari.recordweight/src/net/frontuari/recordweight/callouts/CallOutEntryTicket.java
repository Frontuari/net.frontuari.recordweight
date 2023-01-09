/**
 * 
 */
package net.frontuari.recordweight.callouts;

import java.util.Optional;

import org.compiere.model.MOrder;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

import net.frontuari.recordweight.base.CustomCallout;
import net.frontuari.recordweight.model.I_FTU_EntryTicket;
import net.frontuari.recordweight.model.X_FTU_EntryTicket;

/**
 *
 */
public class CallOutEntryTicket extends CustomCallout {

	@Override
	protected String start() {
		if (getColumnName().equals(I_FTU_EntryTicket.COLUMNNAME_M_Shipper_ID)) {
			Integer m_M_Shipper_ID = (Integer) getValue();
			if (m_M_Shipper_ID == null || m_M_Shipper_ID.intValue() == 0) {
				Env.setContext(getCtx(), getWindowNo(), I_FTU_EntryTicket.COLUMNNAME_M_Shipper_ID, 0);
			}
		} else if(getColumnName().equals(I_FTU_EntryTicket.COLUMNNAME_C_Order_ID)) {
			Integer m_Order_ID = (Integer) getValue();
			if (m_Order_ID == null || m_Order_ID.intValue() == 0)
				return "";
			//	Set Business Partner
			MOrder m_Order = new MOrder(Env.getCtx(), m_Order_ID, null);
			setValue(I_FTU_EntryTicket.COLUMNNAME_C_BPartner_ID, m_Order.getC_BPartner_ID());
		} else if(getColumnName().equals(I_FTU_EntryTicket.COLUMNNAME_C_OrderLine_ID)) {
			Integer m_OrderLine_ID = (Integer)getValue();
			
			if (m_OrderLine_ID == null 
					|| m_OrderLine_ID.intValue() == 0)
				return "";
		
			//	Search Product
			String sql = "SELECT ol.M_Product_ID FROM C_OrderLine ol WHERE ol.C_OrderLine_ID =?";
			
			int m_Product_ID = DB.getSQLValue(null, sql, m_OrderLine_ID);
			
			//	Validate Product ID
			if(m_Product_ID == 0)
				return "";
			
			//	Set Product
			setValue(I_FTU_EntryTicket.COLUMNNAME_M_Product_ID, m_Product_ID);
			
		} else if(getColumnName().equals(I_FTU_EntryTicket.COLUMNNAME_OperationType)) {
			String p_OperationType = (String) getValue();
			if (p_OperationType  == null || p_OperationType.equals(""))
				return "";
			//	if is Receipt
			if(p_OperationType.equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)
					|| p_OperationType.equals(X_FTU_EntryTicket.OPERATIONTYPE_ProductBulkReceipt)
						|| p_OperationType.equals(X_FTU_EntryTicket.OPERATIONTYPE_ReceiptMoreThanOneProduct)
						|| p_OperationType.equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialInputMovement))
				Env.setContext(getCtx(), getWindowNo(), "IsSOTrx", "N");
			else
				Env.setContext(getCtx(), getWindowNo(), "IsSOTrx", "Y");
		} else if ("DD_Order_ID".equals(getColumnName())) {
			
			int DD_Order_ID = Optional.ofNullable((Integer) getValue())
					.orElse(0);
			
			if (DD_Order_ID == 0)
				return "";
			
			MDDOrder ddOrder = new MDDOrder(getCtx(), DD_Order_ID, null);
			setValue(I_FTU_EntryTicket.COLUMNNAME_C_BPartner_ID, ddOrder.getC_BPartner_ID());
		} else if ("DD_OrderLine_ID".equals(getColumnName())) {
			
			int DD_OrderLine_ID = Optional.ofNullable((Integer) getValue())
					.orElse(0);
			
			if (DD_OrderLine_ID == 0)
				return "";
			
			MDDOrderLine ddOLine = new MDDOrderLine(getCtx(), DD_OrderLine_ID, null);
			setValue(I_FTU_EntryTicket.COLUMNNAME_M_Product_ID, ddOLine.getM_Product_ID());
		}
		
		return "";
	}

}
