package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTUFreightCostLine extends X_FTU_FreightCostLine{

	public MFTUFreightCostLine(Properties ctx, int FTU_BillOfLadingLine_ID, String trxName) {
		super(ctx, FTU_BillOfLadingLine_ID, trxName);
	}
	
	public MFTUFreightCostLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
