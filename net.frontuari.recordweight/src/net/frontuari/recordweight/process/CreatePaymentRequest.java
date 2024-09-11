package net.frontuari.recordweight.process;

import java.lang.System;
import java.sql.Timestamp;

import org.compiere.model.MDocType;
import org.compiere.model.MProcessPara;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.Msg;

import net.frontuari.payselection.model.MFTUPaymentRequest;
import net.frontuari.payselection.model.MFTUPaymentRequestLine;
import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUShipperLiquidation;

@org.adempiere.base.annotation.Process
public class CreatePaymentRequest extends FTUProcess {
	
	private int			p_FTU_ShipperLiquidation_ID = 0;
	private int			p_C_DocType_ID = 0;
	private int			p_C_BP_BankAccount_ID = 0;
	private String		p_DocAction = DocAction.ACTION_Prepare;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BP_BankAccount_ID"))
				p_C_BP_BankAccount_ID = para[i].getParameterAsInt();
			else if (name.equals("DocAction"))
				p_DocAction = para[i].getParameterAsString();
			else
				MProcessPara.validateUnknownParameter(getProcessInfo().getAD_Process_ID(), para[i]);
		}
		p_FTU_ShipperLiquidation_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		if (p_FTU_ShipperLiquidation_ID == 0)
			throw new AdempiereUserError("@NotFound@ @FTU_ShipperLiquidation_ID@");
		
		MFTUShipperLiquidation liq = new MFTUShipperLiquidation(getCtx(), p_FTU_ShipperLiquidation_ID, get_TrxName());
		if(!liq.getDocStatus().equals(DocAction.ACTION_Complete))
			throw new AdempiereUserError("@DocStatus@ @mustbe@ @document.status.completed@");
		
		MDocType dt = new MDocType(getCtx(), p_C_DocType_ID, get_TrxName());
		//	Create header
		MFTUPaymentRequest pr = new MFTUPaymentRequest(getCtx(),0,get_TrxName());
		pr.setAD_Org_ID(liq.getAD_Org_ID());
		pr.setC_DocType_ID(p_C_DocType_ID);
		pr.setRequestType(dt.get_ValueAsString("RequestType"));
		pr.setC_Currency_ID(liq.getC_Currency_ID());
		pr.set_ValueOfColumn("C_ConversionType_ID",liq.getC_ConversionType_ID());
		pr.setDateDoc(new Timestamp(System.currentTimeMillis()));
		pr.setDescription(Msg.translate(getCtx(), "liquidation.shipper")+" "+liq.getDocumentInfo());
		pr.saveEx(get_TrxName());
		//	Create line
		MFTUPaymentRequestLine prl = new MFTUPaymentRequestLine(getCtx(), 0, get_TrxName());
		prl.setAD_Org_ID(pr.getAD_Org_ID());
		prl.setFTU_PaymentRequest_ID(pr.get_ID());
		prl.setLine(10);
		prl.setC_BPartner_ID(liq.getM_Shipper().getC_BPartner_ID());
		prl.setC_BP_BankAccount_ID(p_C_BP_BankAccount_ID);
		prl.setPayAmt(liq.getPayAmt());
		prl.saveEx(get_TrxName());
		
		if(!pr.processIt(p_DocAction)) {
			throw new AdempiereUserError(pr.getProcessMsg());
		}else {
			pr.save(get_TrxName());
			liq.setFTU_PaymentRequest_ID(pr.get_ID());
			liq.saveEx(get_TrxName());
			addBufferLog(0, new Timestamp(System.currentTimeMillis()), null, Msg.translate(getCtx(), "@FTU_PaymentRequest_ID@")+": "+pr.getDocumentInfo(), pr.get_Table_ID(), pr.get_ID());
		}
		
		return "@Created@: "+pr.getDocumentInfo();
	}

}
