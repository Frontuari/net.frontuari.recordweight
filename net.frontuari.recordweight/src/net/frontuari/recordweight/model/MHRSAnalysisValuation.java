package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MHRSAnalysisValuation extends X_HRS_AnalysisValuation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3950870709182877267L;

	public MHRSAnalysisValuation(Properties ctx, int HRS_AnalysisValuation_ID, String trxName) {
		super(ctx, HRS_AnalysisValuation_ID, trxName);
	}

	public MHRSAnalysisValuation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
