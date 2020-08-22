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
public class MFTUVehicleBrand extends X_FTU_VehicleBrand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166713204134075549L;

	/**
	 * @param ctx
	 * @param FTU_VehicleBrand_ID
	 * @param trxName
	 */
	public MFTUVehicleBrand(Properties ctx, int FTU_VehicleBrand_ID, String trxName) {
		super(ctx, FTU_VehicleBrand_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUVehicleBrand(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

}
