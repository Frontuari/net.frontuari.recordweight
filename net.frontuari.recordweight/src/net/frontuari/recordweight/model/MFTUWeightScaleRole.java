/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;

/**
 * @author dixon
 *
 */
public class MFTUWeightScaleRole extends X_FTU_WeightScale_Role {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3417106283936908542L;

	/**
	 * @param ctx
	 * @param FTU_WeightScale_Role_ID
	 * @param trxName
	 */
	public MFTUWeightScaleRole(Properties ctx, int FTU_WeightScale_Role_ID, String trxName) {
		super(ctx, FTU_WeightScale_Role_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUWeightScaleRole(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * Get Serial Weight Scale from Role
	 * @author Yamel Senih 26/03/2013, 01:34:37
	 * @param ctx
	 * @param p_AD_Role_ID
	 * @param trxName
	 * @return
	 * @return List<MFTUWeightScale>
	 */
	public static List<MFTUWeightScale> getWeightScaleOfRole(Properties ctx, int p_AD_Role_ID, String trxName){
		List<MFTUWeightScale> list = new Query(ctx, I_FTU_WeightScale.Table_Name, 
				"EXISTS(SELECT 1 " +
				"			FROM " + Table_Name + " wsr " +
				"		WHERE wsr.FTU_WeightScale_ID = FTU_WeightScale.FTU_WeightScale_ID " +
				"		AND wsr.IsActive = 'Y' " +
				"		AND wsr.AD_Role_ID=?)"
				, trxName)
			.setOnlyActiveRecords(true)
			.setParameters(p_AD_Role_ID)
			.<MFTUWeightScale>list();
		//	
		return list;
	}

}
