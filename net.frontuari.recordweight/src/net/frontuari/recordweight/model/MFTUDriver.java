/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author jruiz
 *
 */
public class MFTUDriver extends X_FTU_Driver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7268223547762044853L;

	/**
	 * @param ctx
	 * @param FTU_Driver_ID
	 * @param trxName
	 */
	public MFTUDriver(Properties ctx, int FTU_Driver_ID, String trxName) {
		super(ctx, FTU_Driver_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUDriver(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

}
