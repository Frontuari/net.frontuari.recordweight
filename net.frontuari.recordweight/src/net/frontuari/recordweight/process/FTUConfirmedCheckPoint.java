package net.frontuari.recordweight.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import net.frontuari.recordweight.model.MFTU_EntryTicket_CheckPoint;
import net.frontuari.recordweight.base.CustomProcess;
import net.frontuari.recordweight.model.MFTUEntryTicket;

public class FTUConfirmedCheckPoint extends CustomProcess{
	
	private int FTU_CheckPointSeq_ID = 0;
	private int FTU_EntryTicket_ID = 0;
	private String IsApproved = "";
	private int FTU_Motive_ID = 0;
	private int[] lines;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null);
			else if (name.equals("FTU_CheckPointSeq_ID"))
				FTU_CheckPointSeq_ID = para[i].getParameterAsInt();
			else if (name.equals("FTU_EntryTicket_ID"))
				FTU_EntryTicket_ID = para[i].getParameterAsInt();
			else if (name.equals("IsApproved"))
				IsApproved = para[i].getParameterAsString();
			else if (name.equals("FTU_Motive_ID"))
				FTU_Motive_ID = para[i].getParameterAsInt();
		}
	}

	@Override
	protected String doIt() throws Exception {
		int qtyCpBS = getQtyCheckPointsBySeq(FTU_CheckPointSeq_ID);
		int qtyCpEt = getQtyFromCheckPointInEntryTicket(FTU_EntryTicket_ID);
		int qtyCpDpv = getQtyFromCheckPointDesapproved(FTU_EntryTicket_ID);
		
		if(qtyCpBS == qtyCpEt)
			throw new AdempiereException("Este ticket fue validado en su Totalidad!!");
		
		if(qtyCpDpv > 0)
			throw new AdempiereException("Este ticket fue desaprovado en un puesto de control!!");
		
		int position = ((getSumLineFromEntryTicketCheckPoint(FTU_EntryTicket_ID) + 1) * 10);
			
		ArrayList<Integer> mypositions = getMyRolePositionsInSeq(FTU_CheckPointSeq_ID);
		
		int myposition = 0;
		
		for(int i = 0; i < mypositions.size(); i++) {
			if(mypositions.get(i) == position) {
				myposition = mypositions.get(i);
			}
		}
		
		String Message = "";
		
		if(myposition == position) {
			
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), FTU_EntryTicket_ID, get_TrxName());
			MFTU_EntryTicket_CheckPoint etcp = new MFTU_EntryTicket_CheckPoint(getCtx(), 0, get_TrxName());
				etcp.setAD_Org_ID(et.getAD_Org_ID());
				etcp.set_ValueOfColumn("AD_Client_ID", et.getAD_Client_ID());
				etcp.setLineNo(myposition);
				etcp.setFTU_EntryTicket_ID(et.getFTU_EntryTicket_ID());
				etcp.setFTU_CheckPoint_ID(getCheckPointID());
				etcp.setIsApproved(IsApproved.equals("Y") ? true : false);
				etcp.setFTU_Motive_ID(FTU_Motive_ID);
			etcp.saveEx();
			Message = "Operacion realizada con Exito!!";
			
		}else{
			throw new AdempiereException("Puesto de control no Autorizado!!");
		}
		
		return Message;
	}
	
	public int getCheckPointID() {
		int AD_Role_ID = Env.getContextAsInt(getCtx(), "#AD_Role_ID");
		int AD_User_ID = Env.getContextAsInt(getCtx(), "#AD_User_ID");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cp.FTU_CheckPoint_ID FROM FTU_CheckPoint as cp inner join FTU_CheckPointLine as cpl on (cpl.FTU_CheckPoint_ID = cp.FTU_CheckPoint_ID) WHERE cpl.AD_Role_ID=")
		.append(AD_Role_ID)
		.append(" OR cpl.AD_User_ID=")
		.append(AD_User_ID);
		return DB.getSQLValueEx(get_TrxName(), sql.toString());
	}
	
	public int getSumLineFromEntryTicketCheckPoint(int EntryTicket_ID) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COALESCE(COUNT(LineNo), 1) as lines from FTU_EntryTicket_CheckPoint where FTU_EntryTicket_ID = ?");
		return DB.getSQLValueEx(get_TrxName(), sql.toString(), EntryTicket_ID);
	}
	
	public int getQtyCheckPointsBySeq(int FTU_CheckPointSeq_ID) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cpsl.FTU_CheckPointSeqLine_ID) as qty from FTU_CheckPointSeqLine as cpsl where cpsl.FTU_CheckPointSeq_ID = ")
		.append(FTU_CheckPointSeq_ID);
		return DB.getSQLValueEx(get_TrxName(), sql.toString());
	}
	
	public int getQtyFromCheckPointInEntryTicket(int FTU_EntryTicket_ID) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(FTU_EntryTicket_CheckPoint_ID) as qty from FTU_EntryTicket_CheckPoint where FTU_EntryTicket_ID = ")
		.append(FTU_EntryTicket_ID);
		return DB.getSQLValueEx(get_TrxName(), sql.toString());
	}
	
public int getQtyFromCheckPointDesapproved(int FTU_EntryTicket_ID) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(FTU_EntryTicket_CheckPoint_ID) as qty from FTU_EntryTicket_CheckPoint where FTU_EntryTicket_ID = ")
		.append(FTU_EntryTicket_ID).append(" AND IsApproved = 'N'");
		return DB.getSQLValueEx(get_TrxName(), sql.toString());
	}
	
	public ArrayList<Integer> getMyRolePositionsInSeq(int FTU_CheckPointSeq_ID) {
		ArrayList<Integer> lines = new ArrayList<Integer>();
		int AD_Role_ID = Env.getContextAsInt(getCtx(), "#AD_Role_ID");
		int AD_User_ID = Env.getContextAsInt(getCtx(), "#AD_User_ID");
		
		final String sql = "SELECT cpsl.LineNo FROM FTU_CheckPointSeq AS cps " 
							+ "INNER JOIN FTU_CheckPointSeqLine as cpsl on (cpsl.FTU_CheckPointSeq_ID = cps.FTU_CheckPointSeq_ID)"
							+ "INNER JOIN FTU_CheckPoint as cp on (cp.FTU_CheckPoint_ID = cpsl.FTU_CheckPoint_ID) "
							+ "INNER JOIN FTU_CheckPointLine as cpl on (cpl.FTU_CheckPoint_ID = cp.FTU_CheckPoint_ID)"
							+ "WHERE cps.FTU_CheckPointSeq_ID=? AND (cpl.AD_Role_ID=? OR cpl.AD_User_ID=?) ORDER BY cpsl.LineNo ASC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, FTU_CheckPointSeq_ID);
			pstmt.setInt(2, AD_Role_ID);
			pstmt.setInt(3, AD_User_ID);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				lines.add(rs.getInt("lineno"));
			}
				
		}catch(Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return lines;
	}

}
