package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MConversionRate;
import org.compiere.model.MProcessPara;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

import net.frontuari.payselection.model.MFTUPaymentRequest;
import net.frontuari.payselection.model.MFTUPaymentRequestLine;
import net.frontuari.recordweight.base.FTUProcess;

@org.adempiere.base.annotation.Process
public class CreateFromPRShipperLiquidation extends FTUProcess {

	private int			p_FTU_PaymentRequest_ID = 0;
	private int			m_created = 0;
	
	CLogger log = CLogger.getCLogger(CreateFromPRShipperLiquidation.class);
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("FTU_PaymentRequest_ID"))
				p_FTU_PaymentRequest_ID = para[i].getParameterAsInt();
			else
				MProcessPara.validateUnknownParameter(getProcessInfo().getAD_Process_ID(), para[i]);
		}
		
		if(p_FTU_PaymentRequest_ID == 0)
			p_FTU_PaymentRequest_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		if (p_FTU_PaymentRequest_ID == 0)
			throw new AdempiereUserError("@NotFound@ @FTU_PaymentRequest_ID@");
		
		if (getProcessInfo().getAD_InfoWindow_ID() > 0)
			return createLines();
		else
			throw new AdempiereException("@NotSupported@");
	}
	
	private String createLines() {
		//	Get Liquidation
		MFTUPaymentRequest pr = new MFTUPaymentRequest(getCtx(), p_FTU_PaymentRequest_ID, get_TrxName());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.T_Selection_ID, t.ViewID, v.FTU_ShipperLiquidation_ID, v.DateTrx, v.C_BPartner_ID, C_BP_BankAccount_ID, v.OpenAmt, v.Description, v.C_Currency_ID, v.C_ConversionType_ID,v.DocumentNo ");
		sql.append("FROM T_Selection t, FTU_RV_ShipperLiqToPay v ");
		sql.append("WHERE (t.T_Selection_ID)=(v.FTU_RV_ShipperLiqToPay_ID) ");
		sql.append("AND t.AD_PInstance_ID=? ");
		sql.append("ORDER BY t.T_Selection_ID ");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(1, getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			int LineNo = 10;
			while (rs.next())
			{
				MFTUPaymentRequestLine line = new MFTUPaymentRequestLine(getCtx(),0,get_TrxName());
				line.setAD_Org_ID(pr.getAD_Org_ID());
				line.setFTU_PaymentRequest_ID(pr.get_ID());
				line.set_ValueOfColumn("Description", rs.getString("Description"));
				line.setLine(LineNo);
				line.setDueDate(rs.getTimestamp("DateTrx"));
				line.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
				line.setC_BP_BankAccount_ID(rs.getInt("C_BP_BankAccount_ID"));
				BigDecimal openamt = MConversionRate.convert(getCtx(), rs.getBigDecimal("OpenAmt"), rs.getInt("C_Currency_ID"), pr.getC_Currency_ID(), pr.getDateDoc(), rs.getInt("C_ConversionType_ID"), pr.getAD_Client_ID(), pr.getAD_Org_ID());
				if(openamt==null) {
					String msg = "No hay tasa de cambio para convertir el monto del documento: "+rs.getString("DocumentNo");
					addLog(msg);
					continue;
				}
				line.setPayAmt(openamt);
				line.set_ValueOfColumn("FTU_ShipperLiquidation_ID", rs.getInt("FTU_ShipperLiquidation_ID"));
				line.saveEx(get_TrxName());
				LineNo += 10;
				m_created++;
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
		
		StringBuilder msgreturn = new StringBuilder("@Created@ = ").append(m_created);
		return msgreturn.toString();
	}

}
