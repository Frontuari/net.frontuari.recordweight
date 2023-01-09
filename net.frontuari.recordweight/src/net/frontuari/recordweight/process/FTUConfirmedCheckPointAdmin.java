package net.frontuari.recordweight.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.CustomProcess;
import net.frontuari.recordweight.model.MFTU_EntryTicket_CheckPoint;

public class FTUConfirmedCheckPointAdmin extends CustomProcess{
	
	protected int FTU_EntryTicket_CheckPoint_ID = 0;
	protected String IsApproved = "N";
	protected String IsVoided = "N";
	protected int FTU_Motive_ID = 0;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null);
			else if (name.equals("IsVoided"))
				IsVoided = para[i].getParameterAsString();
			else if (name.equals("IsApproved"))
				IsApproved = para[i].getParameterAsString();
			else if (name.equals("FTU_Motive_ID"))
				FTU_Motive_ID = para[i].getParameterAsInt();
		}
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM FTU_EntryTicket_CheckPoint as etcp " +
					 "WHERE EXISTS (SELECT T_Selection_ID FROM T_Selection as ts WHERE ts.AD_PInstance_ID=? AND " +
					 "ts.T_Selection_ID = etcp.FTU_EntryTicket_CheckPoint_ID)";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				FTU_EntryTicket_CheckPoint_ID = rs.getInt("FTU_EntryTicket_CheckPoint_ID");
				MFTU_EntryTicket_CheckPoint etcp = new MFTU_EntryTicket_CheckPoint(getCtx(), FTU_EntryTicket_CheckPoint_ID, get_TrxName());
				
				if(IsApproved.equals("Y")) {
					etcp.setIsApproved(true);
					etcp.set_ValueOfColumn("IsVoided", false);
					etcp.setFTU_Motive_ID(FTU_Motive_ID);
				}
				
				if(IsVoided.equals("Y")) {
					etcp.set_ValueOfColumn("IsVoided", true);
					etcp.setIsApproved(false);
				}
				
				etcp.saveEx();
			}
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		
		return null;
		
	}

}
