package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MConversionRate;
import org.compiere.model.MProcessPara;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUShipperLiquidation;
import net.frontuari.recordweight.model.MFTUShipperLiquidationLine;

@Process
public class CreateFromDairyShipperLiquidation extends FTUProcess {

	private int			p_FTU_ShipperLiquidation_ID = 0;
	private int			m_created = 0;
	
	CLogger log = CLogger.getCLogger(CreateFromShipperLiquidation.class);
	
		@Override
		protected void prepare() {
			ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				if (para[i].getParameter() == null)
					;
				else if (name.equals("FTU_ShipperLiquidation_ID"))
					p_FTU_ShipperLiquidation_ID = para[i].getParameterAsInt();
				else
					MProcessPara.validateUnknownParameter(getProcessInfo().getAD_Process_ID(), para[i]);
			}
		}
	
		@Override
		protected String doIt() throws Exception {
			if (p_FTU_ShipperLiquidation_ID == 0)
				throw new AdempiereUserError("@NotFound@ @FTU_ShipperLiquidation_ID@");
			
			if (getProcessInfo().getAD_InfoWindow_ID() > 0)
				return createLines();
			else
				throw new AdempiereException("@NotSupported@");
		}
		
		private String createLines() {
			//	Get Liquidation
			MFTUShipperLiquidation liq = new MFTUShipperLiquidation(getCtx(), p_FTU_ShipperLiquidation_ID, get_TrxName());
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.T_Selection_ID, v.GrandTotal, ");
			sql.append("v.Description, v.C_Order_ID, v.C_Currency_ID, v.DatePromised ");
			sql.append("FROM T_Selection t, FTU_RV_PDToLiquidate v ");
			sql.append("WHERE (t.T_Selection_ID)=(v.FTU_RV_PDToLiquidate_ID) ");
			sql.append("AND t.AD_PInstance_ID=? AND v.M_Shipper_ID = ? ");
			sql.append("ORDER BY t.T_Selection_ID ");
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
				pstmt.setInt(1, getAD_PInstance_ID());
				pstmt.setInt(2, liq.getM_Shipper_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MFTUShipperLiquidationLine line = new MFTUShipperLiquidationLine(liq);
					line.setC_Order_ID(rs.getInt("C_Order_ID"));
					line.setDescription(rs.getString("Description"));
					//line.setC_Invoice_ID(rs.getInt("C_Invoice_ID"));
					BigDecimal amt = MConversionRate.convert(getCtx(), rs.getBigDecimal("GrandTotal"), rs.getInt("C_Currency_ID")
							, liq.getC_Currency_ID(), rs.getTimestamp("DatePromised"),	liq.getC_ConversionType_ID()
							, getAD_Client_ID(), liq.getAD_Org_ID());
					if(amt==null) {
						log.warning("No hay tasa de cambio para la moneda: "+rs.getInt("C_Currency_ID")+" hacia la moneda: "+liq.getC_Currency_ID()+" a fecha del: "+rs.getTimestamp("DatePromised")+" del tipo de conversion: "+liq.getC_ConversionType().getName());
						amt = BigDecimal.ZERO;
					}
					line.setAmount(amt);
					line.saveEx();
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