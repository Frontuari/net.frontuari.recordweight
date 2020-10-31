package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Msg;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MHRSAnalysis;

public class GenerateMovementFromAnalysis extends FTUProcess {

	/**	Parameters for generate Movement Document */
	private int p_C_DocTypeMovement_ID = 0;
	private int p_M_Locator_To_ID = 0;
	private BigDecimal p_MovementQty = BigDecimal.ZERO;
	private String p_DocAction = "CO";
	
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("C_DocTypeMovement_ID"))
				p_C_DocTypeMovement_ID = para.getParameterAsInt();
			else if(name.equals("M_LocatorTo_ID"))
				p_M_Locator_To_ID = para.getParameterAsInt();
			else if(name.equals("MovementQty"))
				p_MovementQty = para.getParameterAsBigDecimal();
			else if(name.equals("DocAction"))
				p_DocAction = para.getParameterAsString();
		}
	}

	@Override
	protected String doIt() throws Exception {		
		MHRSAnalysis a = new MHRSAnalysis(getCtx(), getRecord_ID(), get_TrxName());
		if(!a.getDocStatus().equals(MHRSAnalysis.DOCSTATUS_Completed))
			throw new AdempiereException("Error: "+Msg.parseTranslation(getCtx(), "@InvoiceCreateDocNotCompleted@"));
		
		if(!a.isValidAnalysis())
			throw new AdempiereException("Error: "+Msg.parseTranslation(getCtx(), "@HRS_Analysis_ID@")+" "+Msg.parseTranslation(getCtx(), "@NotValid@"));
		
		MInOutLine iol = new MInOutLine(getCtx(), a.get_ValueAsInt("M_InOutLine_ID"), get_TrxName());
		
		if(p_MovementQty.compareTo(iol.getMovementQty())>0)
			throw new AdempiereException("Error: "+Msg.parseTranslation(getCtx(), "@Parameter@")+" "+Msg.parseTranslation(getCtx(), "@MovementQty@")+" ["+p_MovementQty+"] > "+Msg.parseTranslation(getCtx(), "@MovementQty@")+" "+Msg.parseTranslation(getCtx(), "@of@")+" "+Msg.parseTranslation(getCtx(), "@M_InOutLine_ID@")+" ["+iol.getMovementQty()+"]");
		
		//	Create Movement Header
		MMovement m = new MMovement(getCtx(), 0, get_TrxName());
		m.setAD_Org_ID(iol.getAD_Org_ID());
		m.setC_DocType_ID(p_C_DocTypeMovement_ID);
		m.saveEx(get_TrxName());
		//	Create Movement Line
		MMovementLine ml = new MMovementLine(m);
		ml.setM_Product_ID(a.getM_Product_ID());
		ml.setM_Locator_ID(a.get_ValueAsInt("M_Locator_ID"));
		ml.setM_LocatorTo_ID(p_M_Locator_To_ID);
		ml.setMovementQty(p_MovementQty);
		ml.saveEx(get_TrxName());
		
		if(!m.processIt(p_DocAction))
		{
			throw new AdempiereException(m.getProcessMsg());
		}
		m.saveEx(get_TrxName());
		
		addBufferLog(m.get_ID(), new Timestamp(System.currentTimeMillis()), null, Msg.parseTranslation(getCtx(), "@M_Movement_ID@")+": "+m.getDocumentNo(), m.get_Table_ID(), m.get_ID());
		return "@OK@";
	}

}
