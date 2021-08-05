package net.frontuari.recordweight.process;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.MClientInfo;
import org.compiere.model.MDocType;
import org.compiere.model.MTree;
import org.compiere.model.MTree_Base;
import org.compiere.model.MTree_Node;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUBillOfLading;
import net.frontuari.recordweight.model.MFTUBillOfLadingLine;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.X_FTU_BillOfLading;

public class FTUGenerateFreightCost extends FTUProcess{
	String p_FTU_ZeroCost;
	String p_FTU_AdjustPrice;
	BigDecimal p_qtyCalc;
	
	BigDecimal FTU_Capacity;
	BigDecimal FTU_Diference;
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("FTU_ZeroCost"))
				p_FTU_ZeroCost = para.getParameterAsString();
			else if(name.equals("FTU_AdjustPrice"))
				p_FTU_AdjustPrice = para.getParameterAsString();
			else if(name.equals("qtyCalc"))
				p_qtyCalc = para.getParameterAsBigDecimal();
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		BigDecimal totalW = BigDecimal.ZERO;
		BigDecimal grandTotal = BigDecimal.ZERO;
		BigDecimal maxPrice = BigDecimal.ZERO;
		
		int SalesRegionParent_ID = 0;
		
		MFTULoadOrder lo = new MFTULoadOrder(getCtx(),getRecord_ID(), null);
		int CurrencyID = Env.getContextAsInt(getCtx(), "$C_Currency_ID");
		
		if(!lo.get_ValueAsBoolean("IsDelivered"))
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "FTU_MsgRequiredIsDelivered"));
		
		/*if(!lo.get_ValueAsBoolean("IsInvoiced"))
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "FTU_MsgRequiredIsInvoiced"));*/
		
		if(getQtyFreightCostByLoadOrder(lo.get_ID()) > 0)
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "FTU_MsgExistsFreightCostCompleted"));
		
		MFTUBillOfLading bol = new MFTUBillOfLading(getCtx(), 0, null);
		MDocType dt = new MDocType(getCtx(),lo.getC_DocType_ID() , get_TrxName());
		
		bol.setAD_Org_ID(lo.getAD_Org_ID());
		bol.setFTU_LoadOrder_ID(lo.get_ID());
		bol.setC_DocType_ID(dt.get_ValueAsInt("C_DocTypeBillOfLading_ID"));
		bol.setFTU_EntryTicket_ID(lo.getFTU_EntryTicket_ID());
		if(lo.getM_Shipper_ID() == 0) {
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), lo.getFTU_EntryTicket_ID(), null);
			bol.setM_Shipper_ID(et.getM_Shipper_ID());
		}else {
			bol.setM_Shipper_ID(lo.getM_Shipper_ID());
		}
		bol.setFTU_Driver_ID(lo.getFTU_Driver_ID());
		bol.setFTU_Vehicle_ID(lo.getFTU_Vehicle_ID());
		bol.setDateDoc(new Timestamp(System.currentTimeMillis()));
		bol.saveEx();
		
		String sql = "SELECT "
				+ "	il.C_Invoice_ID,"
				+ "	miol.M_InOut_ID,"
				+ "	lol.FTU_DeliveryRute_ID,"
				+ "	SUM(lol.Weight) as Weight,"
				+ "	SUM(COUNT(distinct mio.m_inout_id)) over (partition by lol.FTU_DeliveryRute_ID) AS QtyTravel "
				+ "	FROM FTU_LoadOrderLine as lol"
				+ "	JOIN M_InOutLine as miol on miol.M_InOutLine_ID = lol.M_InOutLine_ID "
				+ "	JOIN M_InOut as mio on (mio.M_InOut_ID = miol.M_InOut_ID) "
				+ "	LEFT JOIN C_InvoiceLine as il on (il.C_OrderLine_ID = lol.C_OrderLine_ID) "
				+ "	WHERE lol.FTU_LoadOrder_ID = ? "
				+ "	GROUP BY miol.M_InOut_ID,il.C_Invoice_ID,lol.FTU_DeliveryRute_ID";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql.toString(), get_TrxName());
			ps.setInt(1, getRecord_ID());
			BigDecimal price = BigDecimal.ZERO;
			BigDecimal costs = BigDecimal.ZERO;
			rs = ps.executeQuery();
			//	
			while(rs.next()){
				MFTUBillOfLadingLine boll = new MFTUBillOfLadingLine(getCtx(), 0, get_TrxName());
				boll.setAD_Org_ID(bol.getAD_Org_ID());
				boll.setFTU_BillOfLading_ID(bol.get_ID());
				//	Search Sales Region
				int SalesRegionID = DB.getSQLValue(get_TrxName(), "SELECT C_SalesRegion_ID FROM C_BPartner_Location bpl JOIN M_InOut io ON bpl.C_BPartner_Location_ID = io.C_BPartner_Location_ID WHERE io.M_InOut_ID = ?", rs.getInt("M_InOut_ID"));
				boll.setC_SalesRegion_ID(SalesRegionID);
				boll.set_ValueOfColumn("FTU_DeliveryRute_ID", rs.getInt("FTU_DeliveryRute_ID"));
				boll.setC_Invoice_ID(rs.getInt("C_Invoice_ID"));
				boll.setM_InOut_ID(rs.getInt("M_InOut_ID"));
				boll.setWeight(rs.getBigDecimal("Weight"));
				// Calculate Cost Freight
				// get Price for Trip
				price = DB.getSQLValueBD(get_TrxName(), "SELECT "
						+ "	(CurrencyConvert((CASE WHEN ? >= ValueMax::numeric THEN pft.PriceActual ELSE pft.Price END),pft.C_Currency_ID,?,?,pft.C_ConversionType_ID,pft.AD_Client_ID,pft.AD_Org_ID)/?) AS Price "
						+ " FROM FTU_PriceForTrip pft " 
						+ " WHERE pft.FTU_DeliveryRute_ID = ? ", new Object[] { rs.getBigDecimal("QtyTravel"), CurrencyID, bol.getDateDoc(),p_qtyCalc, rs.getInt("FTU_DeliveryRute_ID")});
				
				if(price == null)
				{
					price = BigDecimal.ZERO;
				}
				// set costs
				if(p_FTU_ZeroCost.equals("N")) {
					costs = rs.getBigDecimal("Weight").multiply(price);
				}
				boll.setCosts(costs.setScale(2, RoundingMode.HALF_UP));
				
				boll.saveEx(get_TrxName());
				
				totalW = totalW.add(rs.getBigDecimal("Weight"));
				grandTotal = grandTotal.add(costs);
				
				SalesRegionParent_ID = getSalesRegionParent(SalesRegionID);
				
			}
		} catch(Exception ex) {
			log.severe("getLinesForInOut() Error: " + ex.getMessage());
		} finally {
			  DB.close(rs, ps);
		      rs = null; ps = null;
		}
		
		if(p_FTU_AdjustPrice.equals("Y")) {
			
			MClientInfo mci = new MClientInfo(getCtx(), 0, get_TrxName());
			
			mci = MClientInfo.get(getCtx());
			
			FTU_Capacity = getVehicleLoadCapacity(lo.getFTU_EntryTicket_ID());
			FTU_Diference = FTU_Capacity.subtract(totalW);
			
			MFTUBillOfLadingLine boll = new MFTUBillOfLadingLine(getCtx(), 0, get_TrxName());
			boll.setAD_Org_ID(bol.getAD_Org_ID());
			boll.setFTU_BillOfLading_ID(bol.get_ID());
			boll.set_ValueOfColumn("C_Charge_ID", mci.getC_ChargeFreight_ID());
			boll.setWeight(FTU_Diference);
			boll.setCosts(maxPrice.multiply(FTU_Diference));
			boll.saveEx();
			
		}
		
		// Update Parent Sales Region
		bol.setC_SalesRegion_ID(SalesRegionParent_ID);
		// Update Totals
		bol.setWeight(totalW);
		bol.setGrandTotal(grandTotal);
		bol.setDocStatus(X_FTU_BillOfLading.DOCSTATUS_Completed);
		bol.saveEx(get_TrxName());
		
		addBufferLog(bol.get_ID(), new Timestamp(System.currentTimeMillis()), null, Msg.parseTranslation(getCtx(), "@FTU_BillOfLading_ID@")+": "+bol.getDocumentNo(), bol.get_Table_ID(), bol.get_ID());
		
		return "@OK@";
	}

	private int getSalesRegionParent(int TreeNode)
	{
		int TreeID = MTree.getDefaultAD_Tree_ID(getAD_Client_ID(), "C_SalesRegion_ID");
		
		MTree_Base tbase = new MTree_Base(getCtx(), TreeID, get_TrxName());
		
		MTree_Node tn = MTree_Node.get(tbase, TreeNode);
		
		int parentID = 0;
		if(tn.getParent_ID() == 0)
			parentID = tn.getNode_ID();
		else
			parentID = getSalesRegionParent(tn.getParent_ID());
		
		return parentID;
		
		
	}
	
	private BigDecimal getVehicleLoadCapacity(int FTU_EntryTicket_ID) {
		return DB.getSQLValueBD(null, "SELECT v.LoadCapacity FROM FTU_Vehicle AS v "
				+ "INNER JOIN FTU_EntryTicket AS et ON (et.FTU_Vehicle_ID = v.FTU_Vehicle_ID) "
				+ "WHERE et.FTU_EntryTicket_ID = ?", FTU_EntryTicket_ID);
	}
	
	private int getQtyFreightCostByLoadOrder(int FTU_Load_Order_ID) {
		return DB.getSQLValue(get_TrxName(), "SELECT count(*) FROM FTU_BillOfLading WHERE FTU_LoadOrder_ID = ? and DocStatus = 'CO'", FTU_Load_Order_ID);
	}

}
