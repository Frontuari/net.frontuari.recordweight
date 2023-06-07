/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTUVehicleModel extends X_FTU_VehicleModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3211841043630709906L;

	/**
	 * @param ctx
	 * @param FTU_VehicleModel_ID
	 * @param trxName
	 */
	public MFTUVehicleModel(Properties ctx, int FTU_VehicleModel_ID, String trxName) {
		super(ctx, FTU_VehicleModel_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUVehicleModel(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
