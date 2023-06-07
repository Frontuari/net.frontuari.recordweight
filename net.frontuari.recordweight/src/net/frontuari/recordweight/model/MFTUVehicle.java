/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CCache;
import org.compiere.util.Env;

public class MFTUVehicle extends X_FTU_Vehicle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4717776034015269214L;

	/**
	 * @param ctx
	 * @param FTU_Vehicle_ID
	 * @param trxName
	 */
	public MFTUVehicle(Properties ctx, int FTU_Vehicle_ID, String trxName) {
		super(ctx, FTU_Vehicle_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUVehicle(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * Get Vehicle from Cache
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 19/12/2013, 14:10:45
	 * @param ctx
	 * @param FTU_Vehicle_ID
	 * @return
	 * @return MFTUVehicle
	 */
	public static MFTUVehicle get (Properties ctx, int FTU_Vehicle_ID)
	{
		Integer key = FTU_Vehicle_ID;
		MFTUVehicle retValue = (MFTUVehicle) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MFTUVehicle (ctx, FTU_Vehicle_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get
	
	/**	Cache						*/
	private static CCache<Integer,MFTUVehicle>	s_cache	= new CCache<Integer,MFTUVehicle>("FTU_Vehicle", 20, 10);	//	10 minutes
	
	/**
     * Valid Capacity
     */
    @Override
    protected boolean beforeSave(boolean newRecord) {
    	super.beforeSave(newRecord);
    	if(getLoadCapacity() == null
    			|| getLoadCapacity().equals(Env.ZERO)) {
    		throw new AdempiereException("@LoadCapacity@ = @0@");
    	}else if(getMinLoadCapacity().compareTo(getLoadCapacity()) > 0 )
    		throw new AdempiereException("@MinLoadCapacity@ > @LoadCapcity@"  );
    	else if(getMinVolumeCapacity().compareTo(getVolumeCapacity()) > 0 )
    		throw new AdempiereException("@MinLoadCapacity@ > @LoadCapcity@"  );
    	
    	return true;
    }


}
