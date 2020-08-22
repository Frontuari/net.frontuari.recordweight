/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author dixon
 *
 */
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
		/*if(getPosStartCut() > getPosEndCut())
			throw new AdempiereException("@PosStartCutOverPosEndCut@");
		if(getPosStart_SCut() > getPosEnd_SCut())
			throw new AdempiereException("@PosStart_SCutOverPosEnd_SCut@");
		if(getStrLength() <= 0)
			throw new AdempiereException("@StrLengthUnderZero@");*/
		return true;
	}
	
	public String toString(){
		return    "ID=" + get_ID()
				+ "\nName=" + getName()
				+ "\nStart Character=" + getStartCharacter()
				+ "\nEnd Charater=" + getEndCharacter()/*
				+ "\nPosition Start Cut=" + getPosStartCut()
				+ "\nPosition End Cut=" + getPosEndCut()
				+ "\nPosition Start Cut Screen=" + getPosEnd_SCut()
				+ "\nPosition End Cut=" + getPosEnd_SCut()*/;
	}

}
