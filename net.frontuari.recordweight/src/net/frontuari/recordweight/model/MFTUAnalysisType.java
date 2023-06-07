package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTUAnalysisType extends X_FTU_AnalysisType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6147370894696409458L;

	public MFTUAnalysisType(Properties ctx, int FTU_Analysis_Type_ID, String trxName) {
		super(ctx, FTU_Analysis_Type_ID, trxName);
	}

	public MFTUAnalysisType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
