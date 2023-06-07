/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTUScreenConfig extends X_FTU_ScreenConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2758336463744175463L;

	/**
	 * @param ctx
	 * @param FTU_ScreenConfig_ID
	 * @param trxName
	 */
	public MFTUScreenConfig(Properties ctx, int FTU_ScreenConfig_ID, String trxName) {
		super(ctx, FTU_ScreenConfig_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUScreenConfig(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	

	@Override
	public boolean beforeSave(boolean isNew){
		return true;
	}
	
	public String toString(){
		return    "ID=" + get_ID()
				+ "\nName=" + getName()
				+ "\nStart Character=" + getStartCharacter()
				+ "\nEnd Charater=" + getEndCharacter();
	}

}
