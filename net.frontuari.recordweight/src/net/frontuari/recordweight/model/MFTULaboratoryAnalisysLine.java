package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTULaboratoryAnalisysLine extends X_FTU_Laboratory_A_Line {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4392472879355630371L;

	public MFTULaboratoryAnalisysLine(Properties ctx, int FTU_Laboratory_A_Line_ID, String trxName) {
		super(ctx, FTU_Laboratory_A_Line_ID, trxName);
	}

	public MFTULaboratoryAnalisysLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
