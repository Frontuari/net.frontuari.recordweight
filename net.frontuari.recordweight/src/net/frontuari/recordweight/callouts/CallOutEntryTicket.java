/**
 * 
 */
package net.frontuari.recordweight.callouts;

import java.util.Optional;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.MOrder;
import org.compiere.model.MProduction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;
import org.eevolution.model.MPPProductBOM;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.I_FTU_EntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.X_FTU_EntryTicket;

/**
 *	Jorge Colmenarez, 2023-06-07 15:20
 */
@Callout(tableName = I_FTU_EntryTicket.Table_Name, columnName = {I_FTU_EntryTicket.COLUMNNAME_M_Shipper_ID,
		I_FTU_EntryTicket.COLUMNNAME_C_Order_ID,I_FTU_EntryTicket.COLUMNNAME_C_OrderLine_ID,
		I_FTU_EntryTicket.COLUMNNAME_OperationType,I_FTU_EntryTicket.COLUMNNAME_DD_Order_ID,
		I_FTU_EntryTicket.COLUMNNAME_DD_OrderLine_ID,I_FTU_EntryTicket.COLUMNNAME_FTU_LoadOrder_ID,
		"PP_Order_ID"})
public class CallOutEntryTicket extends FTUCallout {

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
						|| p_OperationType.equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialInputMovement)
						|| p_OperationType.equals(X_FTU_EntryTicket.OPERATIONTYPE_ImportRawMaterial)) {
				Env.setContext(getCtx(), getWindowNo(), "IsSOTrx", "N");
				getTab().setValue("IsSOTrx", "N");
			}else {
				Env.setContext(getCtx(), getWindowNo(), "IsSOTrx", "Y");
				getTab().setValue("IsSOTrx", "Y");
			}
		} else if (I_FTU_EntryTicket.COLUMNNAME_DD_Order_ID.equals(getColumnName())) {
			
			int DD_Order_ID = Optional.ofNullable((Integer) getValue())
					.orElse(0);
			
			if (DD_Order_ID == 0)
				return "";
			
			MDDOrder ddOrder = new MDDOrder(getCtx(), DD_Order_ID, null);
			setValue(I_FTU_EntryTicket.COLUMNNAME_C_BPartner_ID, ddOrder.getC_BPartner_ID());
		} else if (I_FTU_EntryTicket.COLUMNNAME_DD_OrderLine_ID.equals(getColumnName())) {
			
			int DD_OrderLine_ID = Optional.ofNullable((Integer) getValue())
					.orElse(0);
			
			if (DD_OrderLine_ID == 0)
				return "";
			
			MDDOrderLine ddOLine = new MDDOrderLine(getCtx(), DD_OrderLine_ID, null);
			setValue(I_FTU_EntryTicket.COLUMNNAME_M_Product_ID, ddOLine.getM_Product_ID());
		} else if (I_FTU_EntryTicket.COLUMNNAME_FTU_LoadOrder_ID.equals(getColumnName())) {
			
			int FTU_Order_ID = Optional.ofNullable((Integer) getValue())
					.orElse(0);
			
			if (FTU_Order_ID == 0)
				return "";
			
			MFTULoadOrder LoadOrder = new MFTULoadOrder(getCtx(), FTU_Order_ID, null);
			setValue(I_FTU_EntryTicket.COLUMNNAME_FTU_Driver_ID, LoadOrder.getFTU_Driver_ID());
			setValue(I_FTU_EntryTicket.COLUMNNAME_FTU_Vehicle_ID, LoadOrder.getFTU_Vehicle_ID());
			setValue(I_FTU_EntryTicket.COLUMNNAME_M_Shipper_ID, LoadOrder.getM_Shipper_ID());
		}
		//	Added by Jorge Colmenarez, 2024-05-21 17:34
		//	Set Production and EndProduct from PPOrder
		else if(getColumnName().equals("PP_Order_ID")) {
			int PP_Order_ID = Optional.ofNullable((Integer) getValue())
					.orElse(0);
			
			if (PP_Order_ID == 0)
				return "";
			
			int ProductionID = DB.getSQLValue(null, "SELECT MAX(M_Production_ID) FROM M_Production WHERE PP_Order_ID = ? AND DocStatus IN ('DR','IP','IN')", PP_Order_ID);
            if (ProductionID > 0) {
               setValue("M_Production_ID", ProductionID);
               MProduction p = new MProduction(this.getCtx(), ProductionID, null);
               String operationType = getTab().get_ValueAsString("OperationType");
               if (operationType.equals("PIM")) {
                  MPPProductBOM pbom = new MPPProductBOM(getCtx(), p.getPP_Product_BOM_ID(), null);
                  if (pbom.get_ValueAsBoolean("IsRequireWeighing")) {
                     setValue("M_Product_ID", p.getM_Product_ID());
                  } else {
                     int productID = DB.getSQLValue(null, "SELECT pl.M_Product_ID FROM M_ProductionLine pl JOIN M_Production p ON pl.M_Production_ID = p.M_Production_ID JOIN PP_Product_BOMLine pbom ON p.PP_Product_BOM_ID=pbom.PP_Product_BOM_ID AND pl.M_Product_ID = pbom.M_Product_ID AND pbom.ComponentType = 'VA' AND pl.M_Production_ID = ?", ProductionID);
                     if (productID > 0) {
                        setValue("M_Product_ID", productID);
                     }
                  }
               }
            }
		}
		//	End Jorge Colmenarez
		
		return "";
	}

}
