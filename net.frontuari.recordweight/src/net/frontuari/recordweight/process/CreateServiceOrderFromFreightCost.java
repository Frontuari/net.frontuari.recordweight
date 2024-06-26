package net.frontuari.recordweight.process;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUFreightCost;

@Process
public class CreateServiceOrderFromFreightCost extends FTUProcess {

	private String returnMsg = "";
	private int p_C_DocType_ID = 0;
	private int p_C_Charge_ID = 0;
	private int p_M_Warehouse_ID = 0;
	private int p_M_PriceList_ID = 0;
	
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
			else if (name.equals("C_Charge_ID"))
				p_C_Charge_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = para[i].getParameterAsInt();
			else
				log.warning("Parametro desconocido:" + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		MFTUFreightCost fc = new MFTUFreightCost(getCtx(), getRecord_ID(), get_TrxName());
		if(fc.get_ID()<=0)
			throw new AdempiereException("@Mandatory@ @FTU_FreightCost_ID@");
		
		if (fc.getM_Shipper().getC_BPartner_ID()==0)
			throw new AdempiereException("Transportista no posee tercero");
		MOrder o = new MOrder(getCtx(), 0, get_TrxName());
		
		o.setAD_Org_ID(fc.getAD_Org_ID());
		o.setC_DocTypeTarget_ID(p_C_DocType_ID);
		o.setC_BPartner_ID(fc.getM_Shipper().getC_BPartner_ID());
		o.setDateOrdered(fc.getDateDoc());
		o.setDateAcct(fc.getDateDoc());
		o.setM_Warehouse_ID(p_M_Warehouse_ID);
		o.setM_PriceList_ID(p_M_PriceList_ID);
		o.setIsSOTrx(false);
		o.setDocAction(DocAction.ACTION_Complete);	
		o.saveEx();
		
		MOrderLine ol = new MOrderLine(o);
		if(p_C_Charge_ID>0)
			ol.setC_Charge_ID(p_C_Charge_ID);
		else
			ol.setDescription("SERVICIO DE FLETE");
		ol.setQty(Env.ONE);
		ol.setPrice(fc.getGrandTotal());
		ol.saveEx();
		
		if (!o.processIt(DocAction.ACTION_Complete)) {
			return "no se pudo completar la Orden de Compra.";
		}else {
			o.saveEx();
			returnMsg = returnMsg + " OC: " + o.getDocumentInfo();
			addBufferLog(o.get_ID(), o.getDateOrdered(), null, o.getDocumentInfo(), MOrder.Table_ID, o.get_ID());
		}
		
		MInOut io = new MInOut(o, o.getC_DocType().getC_DocTypeShipment_ID(), fc.getDateDoc());
		io.saveEx();
		
		MInOutLine iol = new MInOutLine(io);
		if(p_C_Charge_ID>0)
			iol.setC_Charge_ID(p_C_Charge_ID);
		else
			iol.setDescription("SERVICIO DE FLETE");
		iol.setC_OrderLine_ID(ol.getC_OrderLine_ID());
		iol.setQty(Env.ONE);
		iol.saveEx();
		
		if (!io.processIt(DocAction.ACTION_Complete)) {
			return "no se pudo completar la factura.";
		}else {
			io.saveEx();
			returnMsg = returnMsg + ", Entrega: " + io.getDocumentInfo();
			addBufferLog(io.get_ID(), io.getMovementDate(), null, io.getDocumentInfo(), MInOut.Table_ID, io.get_ID());
		}
		
		return returnMsg;
	}

}
