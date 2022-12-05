package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Util;

public class MFTUQualityParam extends X_FTU_Quality_Param {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2804944745446130614L;

	public MFTUQualityParam(Properties ctx, int FTU_Quality_Param_ID, String trxName) {
		super(ctx, FTU_Quality_Param_ID, trxName);
	}

	public MFTUQualityParam(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/***
	 * get Quality Params from Product
	 * @param Product_ID
	 * @param whereClause
	 * @return
	 */
	public static MFTUQualityParam[] getLines(int Product_ID,String whereClause) {
		MFTUQualityParam[] lines = null;
		StringBuilder whereClauseFinal = new StringBuilder(MFTUQualityParam.COLUMNNAME_M_Product_ID+"=? ");
		if (!Util.isEmpty(whereClause, true))
			whereClauseFinal.append(whereClause);
		
		List<MFTUQualityParam> list = new Query(Env.getCtx(), I_FTU_Quality_Param.Table_Name, whereClauseFinal.toString(), null)
				.setParameters(Product_ID)
				.list();
		lines = list.toArray(new MFTUQualityParam[list.size()]);
		
		return lines;
	}

}
