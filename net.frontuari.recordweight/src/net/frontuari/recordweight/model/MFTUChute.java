/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 *
 */
public class MFTUChute extends X_FTU_Chute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6826464192288284721L;

	/**
	 * @param ctx
	 * @param FTU_Chute_ID
	 * @param trxName
	 */
	public MFTUChute(Properties ctx, int FTU_Chute_ID, String trxName) {
		super(ctx, FTU_Chute_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUChute(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
