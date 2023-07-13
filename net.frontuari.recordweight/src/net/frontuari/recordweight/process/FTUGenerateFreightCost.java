package net.frontuari.recordweight.process;

import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUFreightCost;
import net.frontuari.recordweight.model.MFTUFreightCostLine;
import net.frontuari.recordweight.model.MFTURecordWeight;
import net.frontuari.recordweight.model.X_FTU_PriceForTrip;

@Process
public class FTUGenerateFreightCost extends FTUProcess{

	int p_C_DocType_ID = 0;
	int p_FTU_PriceForTrip_ID = 0;
	boolean p_ConsolidateDocument  = false;
	
	@Override
	protected void prepare() {

		for (ProcessInfoParameter para:getParameter()){
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("C_DocType_ID"))
				p_C_DocType_ID = para.getParameterAsInt();
			else if(name.equals("FTU_PriceForTrip_ID"))
				p_FTU_PriceForTrip_ID = para.getParameterAsInt();
			else if(name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = para.getParameterAsBoolean();
		}
	}

	@Override
	protected String doIt() throws Exception {
		

		Timestamp now = new Timestamp(System.currentTimeMillis());
		

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT T.* from FTU_RecordWeight"
				+ " T JOIN (SELECT T_Selection_ID FROM T_Selection WHERE T_Selection.AD_PInstance_ID = "+ getAD_PInstance_ID()+" ) Ts "
						+ "on Ts.T_Selection_ID =  T.FTU_RecordWeight_ID"
						+ " JOIN FTU_EntryTicket et on T.FTU_EntryTicket_ID = et.FTU_EntryTicket_ID "
						+ " ORDER BY et.M_Shipper_ID ";
		
		try {
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
			MFTUFreightCost cost = null;
			while (rs.next())
				{
				
				X_FTU_PriceForTrip pft = new X_FTU_PriceForTrip(getCtx(), p_FTU_PriceForTrip_ID, get_TrxName());
				
				if (p_ConsolidateDocument) {
				
					MFTURecordWeight rw = new MFTURecordWeight(getCtx(), rs.getInt("FTU_RecordWeight_ID"), get_TrxName());
						if (cost == null || cost.getM_Shipper_ID() != rw.getFTU_EntryTicket().getM_Shipper_ID()) {
							if (cost != null)
								cost.saveEx();
							
						cost = new MFTUFreightCost(getCtx(), 0, get_TrxName());
						cost.setAD_Org_ID(rw.getAD_Org_ID());
						cost.setC_DocType_ID(p_C_DocType_ID);
						cost.setM_Shipper_ID(rw.getFTU_EntryTicket().getM_Shipper_ID());
						cost.setDateDoc(now);
						cost.setDocStatus("DR");
						cost.saveEx();
						
						addBufferLog(cost.getFTU_FreightCost_ID(), now, null, cost.getDocumentNo(), MFTUFreightCost.Table_ID, cost.getFTU_FreightCost_ID());

					}
						MFTUFreightCostLine line = new MFTUFreightCostLine(getCtx(), 0, get_TrxName());
						line.setFTU_FreightCost_ID(cost.getFTU_FreightCost_ID());
						line.setWeight(rw.getNetWeight());
						line.set_ValueOfColumn("FTU_RecordWeight_ID", rw.getFTU_RecordWeight_ID());
						line.setCosts(rw.getNetWeight().multiply(pft.getPriceActual()).setScale(2, RoundingMode.HALF_UP));
						line.saveEx();
					
				}
				
				else {
					
					MFTURecordWeight rw = new MFTURecordWeight(getCtx(), rs.getInt("FTU_RecordWeight_ID"), get_TrxName());
					cost = new MFTUFreightCost(getCtx(), 0, get_TrxName());
					cost.setAD_Org_ID(rw.getAD_Org_ID());
					cost.setC_DocType_ID(p_C_DocType_ID);
					cost.setM_Shipper_ID(rw.getFTU_EntryTicket().getM_Shipper_ID());
					cost.setDateDoc(now);
					cost.setDocStatus("DR");
					cost.saveEx();
					
					addBufferLog(cost.getFTU_FreightCost_ID(), now, null, cost.getDocumentNo(), MFTUFreightCost.Table_ID, cost.getFTU_FreightCost_ID());

					
					MFTUFreightCostLine line = new MFTUFreightCostLine(getCtx(), 0, get_TrxName());
					line.setFTU_FreightCost_ID(cost.getFTU_FreightCost_ID());
					line.setWeight(rw.getNetWeight());
					line.set_ValueOfColumn("FTU_RecordWeight_ID", rw.getFTU_RecordWeight_ID());
					line.setCosts(rw.getNetWeight().multiply(pft.getPriceActual()).setScale(2, RoundingMode.HALF_UP));
					line.saveEx();
				}
				
				
				}
		}catch (Exception e) {
			throw new AdempiereException(e);
		}finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;	
		}
		return "COMPLETADO";
	}

}
