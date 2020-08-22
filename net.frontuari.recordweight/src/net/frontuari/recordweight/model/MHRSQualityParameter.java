/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_M_AttributeInstance;
import org.compiere.model.MAttributeInstance;
import org.compiere.model.Query;

/**
 * @author dmartinez
 *
 */
public class MHRSQualityParameter extends X_HRS_QualityParameter {

	/**
	 * @param ctx
	 * @param HRS_QualityParameter_ID
	 * @param trxName
	 */
	public MHRSQualityParameter(Properties ctx, int HRS_QualityParameter_ID, String trxName) {
		super(ctx, HRS_QualityParameter_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MHRSQualityParameter(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**************************************************************************
	 * 	Get Attribute Instance
	 *	@param p_M_Product_ID attribute set instance
	 *	@return Attribute Instance or null
	 */
	public static MHRSQualityParameter getMHRSQualityParameter(Properties ctx, int p_MAttribute_ID, int p_M_Product_ID, String trxName) {
		
		String whereClause = I_HRS_QualityParameter.COLUMNNAME_M_Attribute_ID + "=? AND "
				+ I_HRS_QualityParameter.COLUMNNAME_M_Product_ID + "=?";
		MHRSQualityParameter retValue = new Query(ctx, I_HRS_QualityParameter.Table_Name, whereClause,trxName)
				.setParameters(p_MAttribute_ID, p_M_Product_ID)
				.setOnlyActiveRecords(true)
				.first();
		if(retValue != null)
			return retValue;
		return null;
	} 

}
