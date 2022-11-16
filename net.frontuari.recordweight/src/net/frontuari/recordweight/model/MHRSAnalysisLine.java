package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MHRSAnalysisLine extends X_HRS_AnalysisLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2436487355378589287L;

	public MHRSAnalysisLine(Properties ctx, int HRS_AnalysisLine_ID, String trxName) {
		super(ctx, HRS_AnalysisLine_ID, trxName);
	}

	public MHRSAnalysisLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
