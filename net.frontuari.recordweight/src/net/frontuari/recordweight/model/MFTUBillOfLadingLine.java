package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTUBillOfLadingLine extends X_FTU_BillOfLadingLine{

	public MFTUBillOfLadingLine(Properties ctx, int FTU_BillOfLadingLine_ID, String trxName) {
		super(ctx, FTU_BillOfLadingLine_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MFTUBillOfLadingLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
