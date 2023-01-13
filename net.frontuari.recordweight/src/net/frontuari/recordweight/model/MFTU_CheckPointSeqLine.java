package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class MFTU_CheckPointSeqLine extends X_FTU_CheckPointSeqLine{

	private static final long serialVersionUID = 1L;
	
	public MFTU_CheckPointSeqLine(Properties ctx, int FTU_CheckPointSeqLine_ID, String trxName) {
		super(ctx, FTU_CheckPointSeqLine_ID, trxName);
	}

	public MFTU_CheckPointSeqLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	
	 @Override
    protected boolean beforeSave(boolean newRecord) {
    	
    	int FTU_CheckPointSeq_ID = getFTU_CheckPointSeq_ID();
    	int newLineNo = (getMaxLineNo(FTU_CheckPointSeq_ID) + 10);
    	
    	if(newRecord)
    		setLineNo(newLineNo);

    	return true;
    }
	 
	public int getMaxLineNo(int FTU_CheckPointSeq_ID) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT MAX(LineNo) as LineNo FROM FTU_CheckPointSeqLine WHERE FTU_CheckPointSeq_ID = ").append(FTU_CheckPointSeq_ID);
		
		return DB.getSQLValueEx(get_TrxName(), sql.toString());
	}

}
