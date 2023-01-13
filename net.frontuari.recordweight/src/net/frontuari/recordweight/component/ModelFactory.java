/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Copyright (C) 2022 Frontuari, C.A. <https://frontuari.net> and contributors (see README.md file).
 */

package net.frontuari.recordweight.component;

import net.frontuari.recordweight.base.CustomModelFactory;
import net.frontuari.recordweight.model.I_FTU_BillOfLading;
import net.frontuari.recordweight.model.I_FTU_CheckPointSeqLine;
import net.frontuari.recordweight.model.I_FTU_Chute;
import net.frontuari.recordweight.model.I_FTU_Driver;
import net.frontuari.recordweight.model.I_FTU_EntryTicket;
import net.frontuari.recordweight.model.I_FTU_EntryTicket_CheckPoint;
import net.frontuari.recordweight.model.I_FTU_LoadOrder;
import net.frontuari.recordweight.model.I_FTU_LoadOrderLine;
import net.frontuari.recordweight.model.I_FTU_MobilizationGuide;
import net.frontuari.recordweight.model.I_FTU_RW_ApprovalMotive;
import net.frontuari.recordweight.model.I_FTU_RecordWeight;
import net.frontuari.recordweight.model.I_FTU_ScreenConfig;
import net.frontuari.recordweight.model.I_FTU_SerialPortConfig;
import net.frontuari.recordweight.model.I_FTU_Vehicle;
import net.frontuari.recordweight.model.I_FTU_VehicleBrand;
import net.frontuari.recordweight.model.I_FTU_VehicleModel;
import net.frontuari.recordweight.model.I_FTU_VehicleType;
import net.frontuari.recordweight.model.I_FTU_WS_Warehouse;
import net.frontuari.recordweight.model.I_FTU_WeightScale;
import net.frontuari.recordweight.model.I_FTU_WeightScale_Role;
import net.frontuari.recordweight.model.I_HRS_Analysis;
import net.frontuari.recordweight.model.I_HRS_QualityParameter;
import net.frontuari.recordweight.model.MFTUBillOfLading;
import net.frontuari.recordweight.model.MFTUChute;
import net.frontuari.recordweight.model.MFTUDriver;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;
import net.frontuari.recordweight.model.MFTUMobilizationGuide;
import net.frontuari.recordweight.model.MFTUMovement;
import net.frontuari.recordweight.model.MFTURecordWeight;
import net.frontuari.recordweight.model.MFTUScreenConfig;
import net.frontuari.recordweight.model.MFTUSerialPortConfig;
import net.frontuari.recordweight.model.MFTUVehicle;
import net.frontuari.recordweight.model.MFTUVehicleBrand;
import net.frontuari.recordweight.model.MFTUVehicleModel;
import net.frontuari.recordweight.model.MFTUVehicleType;
import net.frontuari.recordweight.model.MFTUWeightScale;
import net.frontuari.recordweight.model.MFTU_CheckPointSeqLine;
import net.frontuari.recordweight.model.MFTU_EntryTicket_CheckPoint;
import net.frontuari.recordweight.model.MHRSAnalysis;
import net.frontuari.recordweight.model.MHRSQualityParameter;
import net.frontuari.recordweight.model.X_FTU_CheckPointSeqLine;
import net.frontuari.recordweight.model.X_FTU_RW_ApprovalMotive;
import net.frontuari.recordweight.model.X_FTU_ScreenConfig;
import net.frontuari.recordweight.model.X_FTU_SerialPortConfig;
import net.frontuari.recordweight.model.X_FTU_WS_Warehouse;
import net.frontuari.recordweight.model.X_FTU_WeightScale_Role;
import net.frontuari.recordweight.model.X_HRS_QualityParameter;
import net.frontuari.recordweight.model.X_I_EntryTicket;

/**
 * Model Factory
 */
public class ModelFactory extends CustomModelFactory {

	/**
	 * For initialize class. Register the models to build
	 * 
	 * <pre>
	 * protected void initialize() {
	 * 	registerModel(MTableExample.Table_Name, MTableExample.class);
	 * }
	 * </pre>
	 */
	@Override
	protected void initialize() {
		registerModel(I_FTU_Driver.Table_Name, MFTUDriver.class);
		registerModel(I_FTU_EntryTicket.Table_Name, MFTUEntryTicket.class);
		registerModel(I_FTU_LoadOrder.Table_Name, MFTULoadOrder.class);
		registerModel(I_FTU_LoadOrderLine.Table_Name, MFTULoadOrderLine.class);
		registerModel(I_FTU_MobilizationGuide.Table_Name, MFTUMobilizationGuide.class);
		registerModel(I_FTU_RecordWeight.Table_Name, MFTURecordWeight.class);
		registerModel(I_FTU_RW_ApprovalMotive.Table_Name, X_FTU_RW_ApprovalMotive.class);
		registerModel(I_FTU_ScreenConfig.Table_Name, MFTUScreenConfig.class);
		registerModel(I_FTU_SerialPortConfig.Table_Name, MFTUSerialPortConfig.class);
		registerModel(I_FTU_Vehicle.Table_Name, MFTUVehicle.class);
		registerModel(I_FTU_VehicleBrand.Table_Name, MFTUVehicleBrand.class);
		registerModel(I_FTU_VehicleType.Table_Name, MFTUVehicleType.class);
		registerModel(I_FTU_VehicleModel.Table_Name, MFTUVehicleModel.class);
		registerModel(I_FTU_WeightScale.Table_Name, MFTUWeightScale.class);
		registerModel(I_FTU_WeightScale_Role.Table_Name, X_FTU_WeightScale_Role.class);
		registerModel(I_FTU_WS_Warehouse.Table_Name, X_FTU_WS_Warehouse.class);
		registerModel(I_FTU_Chute.Table_Name, MFTUChute.class);		
		registerModel(I_HRS_Analysis.Table_Name, MHRSAnalysis.class);
		registerModel(I_HRS_QualityParameter.Table_Name, MHRSQualityParameter.class);
		registerModel(I_FTU_BillOfLading.Table_Name, MFTUBillOfLading.class);
		registerModel(X_I_EntryTicket.Table_Name, X_I_EntryTicket.class);
		registerModel(MFTUMovement.Table_Name, MFTUMovement.class);
		registerModel(I_FTU_CheckPointSeqLine.Table_Name, MFTU_CheckPointSeqLine.class);
		registerModel(I_FTU_EntryTicket_CheckPoint.Table_Name, MFTU_EntryTicket_CheckPoint.class);
	}

}
