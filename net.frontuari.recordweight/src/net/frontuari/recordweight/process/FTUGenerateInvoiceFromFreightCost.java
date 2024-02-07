package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MConversionRate;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPriceList;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUFreightCost;
import net.frontuari.recordweight.model.MFTUFreightCostLine;

/***
 * Generate Invoice and/or credit note from Freight Cost
 * @author Jorge Colmenarez, 2023-07-14 06:31
 * @version FTUGenerateInvoiceFromFreightCost.java v1.0
 */
@Process
public class FTUGenerateInvoiceFromFreightCost extends FTUProcess{

	
	private int p_C_DocType_ID = 0;
	private int p_C_DocTypeCreditNote_ID = 0;
	private int p_M_PriceList_ID = 0;
	private boolean p_createCreditNote = false;
	private int p_C_Charge_ID = 0;
	@Override
	protected void prepare() {
		
		for (ProcessInfoParameter para:getParameter()){
			
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("C_DocType_ID"))
				p_C_DocType_ID = para.getParameterAsInt();
			else if(name.equals("C_DocTypeTarget_ID"))
				p_C_DocTypeCreditNote_ID = para.getParameterAsInt();
			else if(name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = para.getParameterAsInt();
			else if(name.equals("CreateCreditMemo"))
				p_createCreditNote = para.getParameterAsBoolean();
			else if(name.equals("C_DocTypeTarget_ID"))
				p_C_DocTypeCreditNote_ID = para.getParameterAsInt();
			else if(name.equals("C_Charge_ID"))
				p_C_Charge_ID = para.getParameterAsInt();
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		MFTUFreightCost fc = new MFTUFreightCost(getCtx(), getRecord_ID(), get_TrxName());
		StringBuilder msg = null;
		//	Validate Reference
		if(p_createCreditNote)
		{
			int cnDoc = DB.getSQLValue(get_TrxName(), "SELECT COUNT(i.*) FROM C_Invoice i JOIN C_DocType dt ON i.C_DocType_ID = dt.C_DocType_ID WHERE i.FTU_FreightCost_ID = ? AND i.DocStatus IN ('CO','CL') AND dt.DocBaseType = 'APC'", getRecord_ID());
			if(cnDoc > 0)
				msg = new StringBuilder("Error el costo de flete ya tiene una nota de credito generada "+Env.NL);
		}
		int invDoc = DB.getSQLValue(get_TrxName(), "SELECT COUNT(i.*) FROM C_Invoice i JOIN C_DocType dt ON i.C_DocType_ID = dt.C_DocType_ID WHERE i.FTU_FreightCost_ID = ? AND i.DocStatus IN ('CO','CL') AND dt.DocBaseType = 'API'", getRecord_ID());
		if(invDoc > 0) {
			if(msg == null)
				msg = new StringBuilder("Error el costo de flete ya tiene una factura generada");
			else
				msg = msg.append("Error el costo de flete ya tiene una factura generada");
		}
		//	Error if Freight Cost has documents
		if(msg!=null)
			throw new AdempiereException(msg.toString());
		
		MInvoice inv = null;
		MInvoice cn = null;
		//validate shipper has valid bpartner
		if (!(fc.getM_Shipper().getC_BPartner_ID()>0))
			throw new AdempiereException(" la transportista no tiene un tercero asociado");
		
		MFTUFreightCostLine[] fcLines = fc.getLines(" AND DiscountWeight <= 0");
		MFTUFreightCostLine[] cnLines = fc.getLines(" AND DiscountWeight > 0 ");
		
		MPriceList pl = new MPriceList(getCtx(), p_M_PriceList_ID, get_TrxName());
		int c_currency_id = pl.getC_Currency_ID();
		
		int counter = 0;
		//	Create AP Invoice 
		if (fcLines.length>0) {
			inv = new MInvoice(getCtx(), 0, get_TrxName());
			inv.setAD_Org_ID(fc.getAD_Org_ID());
			inv.setC_DocTypeTarget_ID(p_C_DocType_ID);
			inv.setIsSOTrx(false);
			inv.setBPartner((MBPartner) fc.getM_Shipper().getC_BPartner());
			inv.setSalesRep_ID(getAD_User_ID());
			inv.setM_PriceList_ID(p_M_PriceList_ID);
			inv.setDateInvoiced(now);
			inv.setDateAcct(now);
			inv.set_ValueOfColumn("FTU_FreightCost_ID", fc.getFTU_FreightCost_ID());
			inv.saveEx();
			inv.set_ValueOfColumn("LVE_POInvoiceNo", inv.getDocumentNo());
			inv.saveEx();
			
			for (MFTUFreightCostLine fcline : fcLines) {
				inv.setC_Order_ID(fcline.getC_Order_ID());
				inv.saveEx();
				MInvoiceLine line = new MInvoiceLine(inv);
				int olineID = DB.executeUpdate("SELECT MAX(C_OrderLine_ID) FROM C_OrderLine WHERE C_Order_ID = ?", fcline.getC_Order_ID(), get_TrxName());
				line.setC_OrderLine_ID(olineID);
				line.setC_Charge_ID(fcline.getC_Charge_ID());
				if (c_currency_id != fc.getC_Currency_ID()) {
					BigDecimal amt = MConversionRate.convert(getCtx(), fcline.getCosts(), fc.getC_Currency_ID(), c_currency_id,now,fc.getC_ConversionType_ID() ,fc.getAD_Client_ID(), fc.getAD_Org_ID());
					line.setPrice(amt);
				}else {
					line.setPrice(fcline.getCosts());
				}
				line.setQty(Env.ONE);
				line.saveEx();
			}
			counter++;
		}
		
		//	Create AP Credit
		if (cnLines.length>0 && p_createCreditNote) {
			cn = new MInvoice(getCtx(),0,get_TrxName());
			cn.setAD_Org_ID(fc.getAD_Org_ID());
			cn.setC_DocTypeTarget_ID(p_C_DocTypeCreditNote_ID);
			cn.setIsSOTrx(false);
			cn.setBPartner((MBPartner) fc.getM_Shipper().getC_BPartner());
			cn.setSalesRep_ID(getAD_User_ID());
			cn.setM_PriceList_ID(p_M_PriceList_ID);
			cn.setDateInvoiced(now);
			cn.setDateAcct(now);
			if (inv != null)
			cn.set_ValueOfColumn("LVE_invoiceAffected_ID", inv.getC_Invoice_ID());
			cn.set_ValueOfColumn("FTU_FreightCost_ID", fc.getFTU_FreightCost_ID());
			cn.saveEx();
			cn.set_ValueOfColumn("LVE_POInvoiceNo", cn.getDocumentNo());
			cn.saveEx();
			
			for (MFTUFreightCostLine fcline : cnLines) {
				MInvoiceLine line = new MInvoiceLine(cn);
				line.setC_Charge_ID((p_C_Charge_ID>0 ? p_C_Charge_ID : fcline.getC_Charge_ID()));
				if (c_currency_id != fc.getC_Currency_ID()) {
					BigDecimal amt = MConversionRate.convert(getCtx(), fcline.getPriceActual().multiply(fcline.getDiscountWeight()).setScale(4,RoundingMode.HALF_UP), fc.getC_Currency_ID(), c_currency_id,now,fc.getC_ConversionType_ID() ,fc.getAD_Client_ID(), fc.getAD_Org_ID());
					line.setPrice(amt);
				}else {
					line.setPrice(fcline.getPriceActual().multiply(fcline.getDiscountWeight()).setScale(4,RoundingMode.HALF_UP));
				}
				line.setQty(Env.ONE);
				line.saveEx();
			}
			counter++;
		}
		if (inv != null)
			addBufferLog(inv.getC_Invoice_ID(), now, null, inv.getDocumentNo(), MInvoice.Table_ID, inv.getC_Invoice_ID());
		
		if (cn != null)
			addBufferLog(cn.getC_Invoice_ID(), now, null, cn.getDocumentNo(), MInvoice.Table_ID, cn.getC_Invoice_ID());
		
		return "Documento(s) @Created@(s): "+counter;
	}

}
