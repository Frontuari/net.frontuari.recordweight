/**
 * 
 */
package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CCache;
import org.compiere.util.Env;

public class MFTUVehicleType extends X_FTU_VehicleType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6606695072605125184L;

	/**
	 * @param ctx
	 * @param FTU_VehicleType_ID
	 * @param trxName
	 */
	public MFTUVehicleType(Properties ctx, int FTU_VehicleType_ID, String trxName) {
		super(ctx, FTU_VehicleType_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUVehicleType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

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
    		throw new AdempiereException("@MinLoadCapacity@ > @LoadCapacity@"  );
    	else if(getMinVolumeCapacity().compareTo(getVolumeCapacity()) > 0 )
    		throw new AdempiereException("@MinVolumeCapacity@ > @VolumeCapacity@"  );
		
		return true;
	}
	
	/**
	 * Get Vehicle Type from Cache
	 * @param ctx
	 * @param FTA_VehicleType_ID
	 * @return
	 * @return MFTAVehicleType
	 */
	public static MFTUVehicleType get (Properties ctx, int FTA_VehicleType_ID)
	{
		Integer key = FTA_VehicleType_ID;
		MFTUVehicleType retValue = (MFTUVehicleType) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MFTUVehicleType (ctx, FTA_VehicleType_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get
	
	/**	Cache						*/
	private static CCache<Integer,MFTUVehicleType>	s_cache	= new CCache<Integer,MFTUVehicleType>("FTA_VehicleType", 20, 10);	//	10 minutes

}
