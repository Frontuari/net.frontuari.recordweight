package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MConversionRate;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MProcessPara;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUProcess;

@org.adempiere.base.annotation.Process
public class CreateShipperDiscountInvoice extends FTUProcess {

	private int p_C_DocType_ID	=	0;
	private int p_C_Charge_ID	=	0;
	private int p_M_PriceList_ID	=	0;
	private int p_C_ConversionType_ID	=	0;
	private String p_DocAction	=	MInvoice.ACTION_Prepare;
	private boolean p_ConsolidateDocument	=	false;
	
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
			else if (name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = para[i].getParameterAsInt();
			else if (name.equals("C_ConversionType_ID"))
				p_C_ConversionType_ID = para[i].getParameterAsInt();
			else if (name.equals("DocAction"))
				p_DocAction = para[i].getParameterAsString();
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = para[i].getParameterAsBoolean();
			else
				MProcessPara.validateUnknownParameter(getProcessInfo().getAD_Process_ID(), para[i]);
		}
	}

	@Override
	protected String doIt() throws Exception {
		int m_created = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.T_Selection_ID, t.ViewID,v.AD_Org_ID, v.C_BPartner_ID, TO_CHAR(v.DateReceived,'DD-MM-YYY') as DateReceived, v.TicketNo, ");
		sql.append("v.NetWeight, v.PriceActual, v.DiscountWeight, v.DiscountAmt, v.C_Currency_ID, v.DateOrdered, v.ISO_Code, v.AnalysisType, v.HRS_AnalysisValuation_ID ");
		sql.append("FROM T_Selection t, FTU_RV_ShipperDiscount v ");
		sql.append("WHERE (t.T_Selection_ID)=(v.FTU_RV_ShipperDiscount_ID) ");
		sql.append("AND t.AD_PInstance_ID=? ");
		sql.append("ORDER BY t.T_Selection_ID ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int oldBPartner_ID = 0;
		MInvoice i = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(1, getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int bpartnerID = rs.getInt("C_BPartner_ID");
				MBPartner bp = new MBPartner(getCtx(), bpartnerID, get_TrxName());
				if(oldBPartner_ID==0) {
					//	Create first document
					i = createHeader(bp, rs.getInt("AD_Org_ID"));
					oldBPartner_ID = bpartnerID;
					//	Create first line
					String Description = rs.getString("AnalysisType")
							+" desde Recoleccion: "+rs.getString("TicketNo")
							+" de fecha: "+rs.getString("DateReceived")
							+" Volumen registrado: "+rs.getBigDecimal("NetWeight")
							+" Volumen descontado: "+rs.getBigDecimal("DiscountWeight")
							+" Precio de Compra: "+rs.getBigDecimal("PriceActual")+" "+rs.getString("ISO_Code")
							+" Total Descuento: "+rs.getBigDecimal("DiscountAmt");
					BigDecimal discountamt = MConversionRate.convert(getCtx(), rs.getBigDecimal("DiscountAmt"), rs.getInt("C_Currency_ID"), i.getC_Currency_ID(), i.getDateInvoiced(), p_C_ConversionType_ID, i.getAD_Client_ID(), i.getAD_Org_ID());
					if(discountamt==null) {
						DB.close(rs,pstmt);
						throw new AdempiereException("No existe tasa de cambio para convertir de: "+rs.getString("ISO_Code")+" a "+i.getC_Currency().getISO_Code()+" en fecha "+i.getDateInvoiced()+" con el tipo de conversion "+i.getC_ConversionType().getValue());
					}
					createLine(i,p_C_Charge_ID,discountamt,Description,rs.getInt("HRS_AnalysisValuation_ID"));
				}else if(oldBPartner_ID!=bpartnerID) {
					//	Process Last Invoice
					if(!i.processIt(p_DocAction)) {
						DB.close(rs,pstmt);
						throw new AdempiereException(i.getProcessMsg());
					}else {
						i.save(get_TrxName());
						addBufferLog(i.get_ID(), new Timestamp(System.currentTimeMillis()), null, i.getDocumentInfo(), MInvoice.Table_ID, i.get_ID());
						m_created++;
					}
					//	Create first document
					i = createHeader(bp, rs.getInt("AD_Org_ID"));
					oldBPartner_ID = bpartnerID;
					//	Create first line
					String Description = rs.getString("AnalysisType")
							+" desde Recoleccion: "+rs.getString("TicketNo")
							+" de fecha: "+rs.getString("DateReceived")
							+" Volumen registrado: "+rs.getBigDecimal("NetWeight")
							+" Volumen descontado: "+rs.getBigDecimal("DiscountWeight")
							+" Precio de Compra: "+rs.getBigDecimal("PriceActual")+" "+rs.getString("ISO_Code")
							+" Total Descuento: "+rs.getBigDecimal("DiscountAmt");
					BigDecimal discountamt = MConversionRate.convert(getCtx(), rs.getBigDecimal("DiscountAmt"), rs.getInt("C_Currency_ID"), i.getC_Currency_ID(), i.getDateInvoiced(), p_C_ConversionType_ID, i.getAD_Client_ID(), i.getAD_Org_ID());
					if(discountamt==null) {
						DB.close(rs,pstmt);
						throw new AdempiereException("No existe tasa de cambio para convertir de: "+rs.getString("ISO_Code")+" a "+i.getC_Currency().getISO_Code()+" en fecha "+i.getDateInvoiced()+" con el tipo de conversion "+i.getC_ConversionType().getValue());
					}
					createLine(i,p_C_Charge_ID,discountamt,Description,rs.getInt("HRS_AnalysisValuation_ID"));
				}else {
					String Description = rs.getString("AnalysisType")
							+" desde Recoleccion: "+rs.getString("TicketNo")
							+" de fecha: "+rs.getString("DateReceived")
							+" Volumen registrado: "+rs.getBigDecimal("NetWeight")
							+" Volumen descontado: "+rs.getBigDecimal("DiscountWeight")
							+" Precio de Compra: "+rs.getBigDecimal("PriceActual")+" "+rs.getString("ISO_Code")
							+" Total Descuento: "+rs.getBigDecimal("DiscountAmt");
					BigDecimal discountamt = MConversionRate.convert(getCtx(), rs.getBigDecimal("DiscountAmt"), rs.getInt("C_Currency_ID"), i.getC_Currency_ID(), i.getDateInvoiced(), p_C_ConversionType_ID, i.getAD_Client_ID(), i.getAD_Org_ID());
					if(discountamt==null) {
						DB.close(rs,pstmt);
						throw new AdempiereException("No existe tasa de cambio para convertir de: "+rs.getString("ISO_Code")+" a "+i.getC_Currency().getISO_Code()+" en fecha "+i.getDateInvoiced()+" con el tipo de conversion "+i.getC_ConversionType().getValue());
					}
					updateLine(i, discountamt, Description,rs.getInt("HRS_AnalysisValuation_ID"));
				}
				if(!p_ConsolidateDocument) {
					if(!i.processIt(p_DocAction)) {
						DB.close(rs,pstmt);
						throw new AdempiereException(i.getProcessMsg());
					}else {
						i.save(get_TrxName());
						addBufferLog(i.get_ID(), new Timestamp(System.currentTimeMillis()), null, i.getDocumentInfo(), MInvoice.Table_ID, i.get_ID());
						m_created++;
						oldBPartner_ID = 0;
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new AdempiereException(e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//	Process Last Invoice for Consolidate Docs
		if(p_ConsolidateDocument) {
			if(!i.processIt(p_DocAction)) {
				DB.close(rs,pstmt);
				throw new AdempiereException(i.getProcessMsg());
			}else {
				i.save(get_TrxName());
				addBufferLog(i.get_ID(), new Timestamp(System.currentTimeMillis()), null, i.getDocumentInfo(), MInvoice.Table_ID, i.get_ID());
				m_created++;
			}
		}
		
		StringBuilder msgreturn = new StringBuilder("@Created@ = ").append(m_created);
		return msgreturn.toString();
	}
	
	/***
	 * Create Invoice Header
	 * @param bp
	 * @param AD_Org_ID
	 * @return
	 */
	private MInvoice createHeader(MBPartner bp,int AD_Org_ID) {
		MInvoice i = new MInvoice(getCtx(), 0, get_TrxName());
		i.setAD_Org_ID(AD_Org_ID);
		i.setC_DocTypeTarget_ID(p_C_DocType_ID);
		i.setIsSOTrx(false);
		i.setBPartner(bp);
		i.setM_PriceList_ID(p_M_PriceList_ID);
		i.setC_ConversionType_ID(p_C_ConversionType_ID);
		i.save(get_TrxName());
		i.set_ValueOfColumn("LVE_POInvoiceNo", i.getDocumentNo());
		i.save(get_TrxName());
		return i;
	}
	
	/***
	 * Create Invoice Line
	 * @param i
	 * @param C_Charge_ID
	 * @param price
	 * @param Description
	 */
	private void createLine(MInvoice i,int C_Charge_ID, BigDecimal price, String Description, int valID) {
		MInvoiceLine il = new MInvoiceLine(i);
		il.setC_Charge_ID(C_Charge_ID);
		il.setDescription(Description);
		il.setQty(BigDecimal.ONE);
		il.setPrice(price);
		il.save(get_TrxName());
		DB.executeUpdate("UPDATE HRS_AnalysisValuation SET IsInvoiced='Y' WHERE HRS_AnalysisValuation_ID = ?", valID, get_TrxName());
	}
	
	/***
	 * Update Invoice Line
	 * @param i
	 * @param price
	 * @param Description
	 */
	private void updateLine(MInvoice i, BigDecimal price, String Description, int valID) {
		for(MInvoiceLine line : i.getLines()) {
			line.addDescription(Description);
			line.setPrice(line.getPriceActual().add(price));
			line.saveEx(get_TrxName());
			DB.executeUpdate("UPDATE HRS_AnalysisValuation SET IsInvoiced='Y' WHERE HRS_AnalysisValuation_ID = ?", valID, get_TrxName());
		}
	}

}
