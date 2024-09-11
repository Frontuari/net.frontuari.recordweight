package net.frontuari.recordweight.process;

import java.lang.System;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import org.adempiere.base.annotation.Process;
import org.compiere.model.MConversionRate;
import org.compiere.model.MMovement;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUFreightCost;
import net.frontuari.recordweight.model.MFTUFreightCostLine;
import net.frontuari.recordweight.model.X_FTU_PriceForTrip;

@Process
public class CreateFreightCostConsingment extends FTUProcess {

	private int p_C_DocType_ID = 0;
	private int p_FTU_PriceForTrip_ID = 0;
	private int p_C_Charge_ID = 0;
	private MMovement mov = null;
	
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("C_DocTypeFreightCost_ID"))
				p_C_DocType_ID = para.getParameterAsInt();
			else if(name.equals("FTU_PriceForTrip_ID"))
				p_FTU_PriceForTrip_ID = para.getParameterAsInt();
			else if(name.equals("C_Charge_ID"))
				p_C_Charge_ID = para.getParameterAsInt();
		}
		mov = new MMovement(getCtx(), getRecord_ID(), get_TrxName());
	}

	@Override
	protected String doIt() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		X_FTU_PriceForTrip pft = new X_FTU_PriceForTrip(getCtx(), p_FTU_PriceForTrip_ID, get_TrxName());
		
		MFTUFreightCost cost = new MFTUFreightCost(getCtx(), 0, get_TrxName());
		cost.setAD_Org_ID(mov.getAD_Org_ID());
		cost.setDateDoc(now);
		cost.setAD_User_ID(getAD_User_ID());
		cost.setC_DocType_ID(p_C_DocType_ID);
		cost.setFTU_PriceForTrip_ID(p_FTU_PriceForTrip_ID);
		cost.setC_Currency_ID(pft.getC_Currency_ID());
		cost.setC_ConversionType_ID(pft.getC_ConversionType_ID());
		cost.setM_Shipper_ID(mov.getM_Shipper_ID());
		cost.setFTU_Driver_ID(mov.get_ValueAsInt("FTU_Driver_ID"));
		cost.setFTU_Vehicle_ID(mov.get_ValueAsInt("FTU_Vehicle_ID"));
		cost.setDocStatus(MFTUFreightCost.DOCSTATUS_Drafted);
		cost.setDocAction(MFTUFreightCost.ACTION_Prepare);
		cost.saveEx();
		
		addBufferLog(cost.getFTU_FreightCost_ID(), now, null, cost.getDocumentNo(), MFTUFreightCost.Table_ID, cost.getFTU_FreightCost_ID());

		MFTUFreightCostLine line = new MFTUFreightCostLine(getCtx(), 0, get_TrxName());
		line.setFTU_FreightCost_ID(cost.getFTU_FreightCost_ID());
		line.setFTU_DeliveryRute_ID(pft.getFTU_DeliveryRute_ID());
		line.setC_Charge_ID(p_C_Charge_ID);
		BigDecimal qty = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(MovementQty) FROM M_MovementLine WHERE M_Movement_ID = ?", mov.get_ID());
		line.setWeight(qty);
		line.setDiscountWeight(BigDecimal.ZERO);
		if(!pft.get_ValueAsBoolean("IsFlatFee"))
			line.setCosts(qty.multiply(pft.getPriceActual()).setScale(4, RoundingMode.HALF_UP));
		else
			line.setCosts((BigDecimal)pft.get_Value("Amount"));
		line.setValueMin(pft.getValueMin());
		line.setValueMax(pft.getValueMax());
		line.setPrice(pft.getPrice());
		line.setPriceActual(pft.getPriceActual());
		line.setC_ConversionType_ID(pft.getC_ConversionType_ID());
		line.setC_Currency_ID(pft.getC_Currency_ID());
		BigDecimal rate = MConversionRate.getRate(pft.getC_Currency_ID(), cost.getC_Currency_ID(), cost.getDateDoc(), pft.getC_ConversionType_ID(), cost.getAD_Client_ID(), cost.getAD_Org_ID());
		line.setFinalPrice(pft.getPriceActual());
		line.setRate(rate);
		line.saveEx();
		
		return "OK";
	}

}
