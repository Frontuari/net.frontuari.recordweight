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
 * Copyright (C) 2020 FRONTUARI <https://www.frontuari.net> and contributors (see README.md file).
 */

package net.frontuari.recordweight.component;

import org.eevolution.model.I_DD_OrderLine;

import net.frontuari.recordweight.base.FTUCalloutFactory;
import net.frontuari.recordweight.callouts.CallOutEntryTicket;
import net.frontuari.recordweight.callouts.CalloutAnalysis;
import net.frontuari.recordweight.callouts.CalloutLoadOrder;
import net.frontuari.recordweight.callouts.CalloutRecordWeight;
import net.frontuari.recordweight.callouts.CalloutVehicle;
import net.frontuari.recordweight.callouts.FTU_CalloutDDOrderLine;
import net.frontuari.recordweight.model.I_FTU_EntryTicket;
import net.frontuari.recordweight.model.I_FTU_RecordWeight;
import net.frontuari.recordweight.model.I_FTU_Vehicle;
import net.frontuari.recordweight.model.I_HRS_Analysis;
import net.frontuari.recordweight.model.X_FTU_LoadOrderLine;

/**
 * Callout Factory
 */
public class CalloutFactory extends FTUCalloutFactory {

	/**
	 * For initialize class. Register the custom callout to build
	 * 
	 * <pre>
	 * protected void initialize() {
	 * 	registerCallout(MTableExample.Table_Name, MTableExample.COLUMNNAME_Text, CPrintPluginInfo.class);
	 * }
	 * </pre>
	 */
	@Override
	protected void initialize() {
		//	Entry Ticket
		registerCallout(I_FTU_EntryTicket.Table_Name, I_FTU_EntryTicket.COLUMNNAME_M_Shipper_ID, CallOutEntryTicket.class);
		registerCallout(I_FTU_EntryTicket.Table_Name, I_FTU_EntryTicket.COLUMNNAME_C_Order_ID, CallOutEntryTicket.class);
		registerCallout(I_FTU_EntryTicket.Table_Name, I_FTU_EntryTicket.COLUMNNAME_C_OrderLine_ID, CallOutEntryTicket.class);
		registerCallout(I_FTU_EntryTicket.Table_Name, I_FTU_EntryTicket.COLUMNNAME_OperationType, CallOutEntryTicket.class);
		//	Record Weight
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_C_DocType_ID, CalloutRecordWeight.class);
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_FTU_EntryTicket_ID, CalloutRecordWeight.class);
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_GrossWeight, CalloutRecordWeight.class);
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_TareWeight, CalloutRecordWeight.class);
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_FTU_LoadOrder_ID, CalloutRecordWeight.class);
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_FTU_Chute_ID, CalloutRecordWeight.class);
		registerCallout(I_FTU_RecordWeight.Table_Name, I_FTU_RecordWeight.COLUMNNAME_FTU_WeightScale_ID, CalloutRecordWeight.class);
		//	Added by Jorge Colmenarez, 2021-06-07 15:24
		registerCallout(I_FTU_RecordWeight.Table_Name, "FTU_RecordWeightSource_ID", CalloutRecordWeight.class);
		//	End Jorge Colmenarez
		//	Vehicle
		registerCallout(I_FTU_Vehicle.Table_Name, I_FTU_Vehicle.COLUMNNAME_FTU_VehicleModel_ID, CalloutVehicle.class);
		registerCallout(I_FTU_Vehicle.Table_Name, I_FTU_Vehicle.COLUMNNAME_FTU_VehicleType_ID, CalloutVehicle.class);
		//	Analysis
		registerCallout(I_HRS_Analysis.Table_Name, I_HRS_Analysis.COLUMNNAME_PP_Order_ID, CalloutAnalysis.class);
		registerCallout(I_HRS_Analysis.Table_Name, I_HRS_Analysis.COLUMNNAME_FTU_EntryTicket_ID, CalloutAnalysis.class);
		registerCallout(I_HRS_Analysis.Table_Name, "M_InOutLine_ID", CalloutAnalysis.class);
		registerCallout(I_FTU_EntryTicket.Table_Name, "DD_Order_ID", CallOutEntryTicket.class);
		registerCallout(I_FTU_EntryTicket.Table_Name, "DD_OrderLine_ID", CallOutEntryTicket.class);
		registerCallout(I_DD_OrderLine.Table_Name, I_DD_OrderLine.COLUMNNAME_QtyEntered, FTU_CalloutDDOrderLine.class);
		registerCallout(I_DD_OrderLine.Table_Name, I_DD_OrderLine.COLUMNNAME_C_UOM_ID, FTU_CalloutDDOrderLine.class);
		registerCallout(I_DD_OrderLine.Table_Name, I_DD_OrderLine.COLUMNNAME_QtyOrdered, FTU_CalloutDDOrderLine.class);
		registerCallout(I_DD_OrderLine.Table_Name, I_DD_OrderLine.COLUMNNAME_M_Product_ID, FTU_CalloutDDOrderLine.class);
		//	Load Order
		registerCallout(X_FTU_LoadOrderLine.Table_Name, X_FTU_LoadOrderLine.COLUMNNAME_C_OrderLine_ID, CalloutLoadOrder.class);
	}

}
