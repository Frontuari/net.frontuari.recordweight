package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.DB;

public class MFTUWeightScale extends X_FTU_WeightScale {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4022244931324950042L;

	public MFTUWeightScale(Properties ctx, int FTU_WeightScale_ID, String trxName) {
		super(ctx, FTU_WeightScale_ID, trxName);
	}

	public MFTUWeightScale(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public static List<MFTUWeightScale> getWeightScaleOfOrg(Properties ctx, int p_AD_Org_ID, String trxName) {
		List<MFTUWeightScale> list = new Query(ctx, I_FTU_WeightScale.Table_Name, "AD_Org_ID = ? AND IsActive = ?", trxName)
			.setOnlyActiveRecords(true)
			.setParameters(p_AD_Org_ID, true)
			.<MFTUWeightScale>list();
		//	
		return list;
	}
	
	/**
	 * Get Serial Port Configuration
	 * @return
	 * @return MFTUSerialPortConfig
	 */
	public MFTUSerialPortConfig getSerialPortConfig(){
		return (MFTUSerialPortConfig) getFTU_SerialPortConfig();
	}
	
	/**
	 * Get Screen Configuration
	 * @return
	 * @return MFTUScreenConfig
	 */
	public MFTUScreenConfig getScreenConfig(){
		return (MFTUScreenConfig) getFTU_ScreenConfig();
	}
	
	
	public static boolean isWeightScaleOrg(int p_AD_Org_ID, String trxName) {
		int m_FTU_WeightScale_ID = DB.getSQLValue(trxName, 
				"SELECT FTU_WeightScale_ID "
				+ "FROM FTU_WeightScale "
				+ "WHERE AD_Org_ID = ? "
				+ "AND IsActive = 'Y' "
				+ "AND IsRecordWeightUsed = 'Y' ", 
				p_AD_Org_ID);
		//	Verify
		return (m_FTU_WeightScale_ID > 0);
	}

}
