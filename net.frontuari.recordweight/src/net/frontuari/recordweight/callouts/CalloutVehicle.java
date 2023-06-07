/**
 * 
 */
package net.frontuari.recordweight.callouts;

import org.adempiere.base.annotation.Callout;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.I_FTU_Vehicle;
import net.frontuari.recordweight.model.I_FTU_VehicleType;
import net.frontuari.recordweight.model.MFTUVehicleModel;
import net.frontuari.recordweight.model.MFTUVehicleType;

@Callout(tableName = I_FTU_Vehicle.Table_Name,columnName = {I_FTU_Vehicle.COLUMNNAME_FTU_VehicleModel_ID,
		I_FTU_Vehicle.COLUMNNAME_FTU_VehicleType_ID})
public class CalloutVehicle extends FTUCallout {

	@Override
	protected String start() {
		if(getColumnName().equals(I_FTU_Vehicle.COLUMNNAME_FTU_VehicleModel_ID)) {
			Integer m_FTU_VehicleModel_ID = (Integer) getValue();
			if (m_FTU_VehicleModel_ID == null || m_FTU_VehicleModel_ID.intValue() == 0)
				return "";
			
			//Set Product From Entry Ticket
			MFTUVehicleModel vm = new MFTUVehicleModel(getCtx(), m_FTU_VehicleModel_ID, null);
			//	Set Trailer Plate, Vehicle and driver of Entry Ticket
			if (vm.getFTU_VehicleType_ID() > 0) {
				setValue(I_FTU_VehicleType.COLUMNNAME_FTU_VehicleType_ID, vm.getFTU_VehicleType_ID());
				//Set Product From Entry Ticket
				MFTUVehicleType vt = new MFTUVehicleType(getCtx(), vm.getFTU_VehicleType_ID(), null);
				setValue(I_FTU_VehicleType.COLUMNNAME_LoadCapacity, vt.getLoadCapacity());
				setValue(I_FTU_Vehicle.COLUMNNAME_VolumeCapacity, vt.getVolumeCapacity());
				setValue(I_FTU_Vehicle.COLUMNNAME_MinLoadCapacity, vt.getMinLoadCapacity());
				setValue(I_FTU_Vehicle.COLUMNNAME_MinVolumeCapacity, vt.getMinVolumeCapacity());
				
			}
		} 
		
		if(getColumnName().equals(I_FTU_Vehicle.COLUMNNAME_FTU_VehicleType_ID)) {
			Integer m_FTU_VehicleType_ID = (Integer) getValue();
			if (m_FTU_VehicleType_ID == null || m_FTU_VehicleType_ID.intValue() == 0)
				return "";
			
			//Set Product From Entry Ticket
			MFTUVehicleType vt = new MFTUVehicleType(getCtx(), m_FTU_VehicleType_ID, null);
			setValue(I_FTU_VehicleType.COLUMNNAME_LoadCapacity, vt.getLoadCapacity());
			setValue(I_FTU_Vehicle.COLUMNNAME_VolumeCapacity, vt.getVolumeCapacity());
			setValue(I_FTU_Vehicle.COLUMNNAME_MinLoadCapacity, vt.getMinLoadCapacity());
			setValue(I_FTU_Vehicle.COLUMNNAME_MinVolumeCapacity, vt.getMinVolumeCapacity());
		}
		return "";
	}

}
