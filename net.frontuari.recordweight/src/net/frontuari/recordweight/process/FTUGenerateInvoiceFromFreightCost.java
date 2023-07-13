package net.frontuari.recordweight.process;

import java.sql.Timestamp;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUFreightCost;
import net.frontuari.recordweight.model.MFTUFreightCostLine;

@Process
public class FTUGenerateInvoiceFromFreightCost extends FTUProcess{

	
	private int p_C_DocType_ID = 0;
	@Override
	protected void prepare() {
		
		for (ProcessInfoParameter para:getParameter()){
			
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("C_DocType_ID"))
				p_C_DocType_ID = para.getParameterAsInt();
			//else if (name.equals("C_DocType_ID"))
			
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		MFTUFreightCost fc = new MFTUFreightCost(getCtx(), getRecord_ID(), get_TrxName());
		
		//validate shipper has valid bpartner
		if (!(fc.getM_Shipper().getC_BPartner_ID()>0))
			throw new AdempiereException(" la transportista no tiene un tercero asociado");
		
		MInvoice inv = new MInvoice(getCtx(), 0, get_TrxName());
		inv.setAD_Org_ID(fc.getAD_Org_ID());
		inv.setC_DocTypeTarget_ID(p_C_DocType_ID);
		inv.setBPartner((MBPartner) fc.getM_Shipper().getC_BPartner());
		inv.setSalesRep_ID(1000007);
		inv.setDateInvoiced(now);
		inv.setDateAcct(now);
		inv.saveEx();
		
		for (MFTUFreightCostLine fcline : fc.getLines(null)) {
			MInvoiceLine line = new MInvoiceLine(inv);
			line.setC_Charge_ID(fcline.getC_Charge_ID());
			line.setPrice(fcline.getCosts());
			line.setQty(Env.ONE);
			line.saveEx();
		}
		
		addBufferLog(inv.getC_Invoice_ID(), now, null, inv.getDocumentNo(), MInvoice.Table_ID, inv.getC_Invoice_ID());
		return null;
	}

}
