/**
 * 
 */
package net.frontuari.recordweight.callouts;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.I_C_DocType;
import org.compiere.model.MDocType;
import org.compiere.util.DB;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.I_FTU_RecordWeight;
import net.frontuari.recordweight.model.MFTUChute;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;
import net.frontuari.recordweight.model.MFTURecordWeight;
import net.frontuari.recordweight.model.MFTUWeightScale;
import net.frontuari.recordweight.model.MHRSAnalysis;
import net.frontuari.recordweight.model.X_FTU_EntryTicket;
import net.frontuari.recordweight.model.X_FTU_RecordWeight;

@Callout(tableName = I_FTU_RecordWeight.Table_Name, columnName = {I_FTU_RecordWeight.COLUMNNAME_C_DocType_ID,
		I_FTU_RecordWeight.COLUMNNAME_FTU_WeightScale_ID,I_FTU_RecordWeight.COLUMNNAME_FTU_EntryTicket_ID,
		I_FTU_RecordWeight.COLUMNNAME_GrossWeight,I_FTU_RecordWeight.COLUMNNAME_TareWeight,
		I_FTU_RecordWeight.COLUMNNAME_FTU_LoadOrder_ID,I_FTU_RecordWeight.COLUMNNAME_FTU_Chute_ID,
		I_FTU_RecordWeight.COLUMNNAME_FTU_RecordWeightSource_ID})
public class CalloutRecordWeight extends FTUCallout {

	@Override
	protected String start() {
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_C_DocType_ID)) {
			Integer m_C_DocType_ID = (Integer) getValue();
			if (m_C_DocType_ID == null || m_C_DocType_ID.intValue() == 0)
				return "";
			
			MDocType m_DocType = MDocType.get(getCtx(), m_C_DocType_ID.intValue());
			//	Set Context
			Env.setContext(getCtx(), getWindowNo(), I_C_DocType.COLUMNNAME_DocBaseType, m_DocType.getDocBaseType());
			//	Is SO Trx
			setValue(I_FTU_RecordWeight.COLUMNNAME_IsSOTrx, m_DocType.isSOTrx());
		} 
		//	BR [ 6 ]
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_FTU_WeightScale_ID)) {
			Integer m_FTU_WeightScale_ID = (Integer) getValue();
			if (m_FTU_WeightScale_ID == null || m_FTU_WeightScale_ID.intValue() == 0)
				return "";
			MFTUWeightScale scale = new MFTUWeightScale(getCtx(), m_FTU_WeightScale_ID, null);
			if(scale.getC_UOM_ID() > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_C_UOM_ID, scale.getC_UOM_ID());
			return "";			
		}
		
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_FTU_EntryTicket_ID)) {
			Integer m_FTU_EntryTicket_ID = (Integer) getValue();
			if (m_FTU_EntryTicket_ID == null || m_FTU_EntryTicket_ID.intValue() == 0)
				return "";
			
			//	Set load order
			//	Return Load Order 
			String sql = " SELECT FTU_LoadOrder_ID " +
					" FROM FTU_LoadOrder" +
					" WHERE FTU_EntryTicket_ID = ?" +
					" AND FTU_LoadOrder.DocStatus IN ('CO')";
			
			int m_FTU_LoadOrder_ID = DB.getSQLValue(null, sql, m_FTU_EntryTicket_ID);
			//	Set Value Load Order into Record Weight
			if(m_FTU_LoadOrder_ID > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_FTU_LoadOrder_ID, m_FTU_LoadOrder_ID);
			
			//Set Product From Entry Ticket
			MFTUEntryTicket et = new MFTUEntryTicket(getCtx(), m_FTU_EntryTicket_ID, null);
			//	Set Trailer Plate, Vehicle and driver of Entry Ticket
			if (et.getM_Product_ID()!= 0 )
				setValue(I_FTU_RecordWeight.COLUMNNAME_M_Product_ID, et.getM_Product_ID());
			if(et.getTrailerPlate() != null
					&& et.getTrailerPlate().length() > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_TrailerPlate, et.getTrailerPlate());
			if(et.getFTU_Driver_ID() > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_FTU_Driver_ID, et.getFTU_Driver_ID());
			if(et.getFTU_Vehicle_ID() > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_FTU_Vehicle_ID, et.getFTU_Vehicle_ID());
			
			//	Set Shipper from entry ticket
			if(et.getM_Shipper_ID() > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_M_Shipper_ID, et.getM_Shipper_ID());
			
			if(et.getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)
					|| et.getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_DeliveryBulkMaterial)) {
				int warehouseID = et.getC_Order().getM_Warehouse_ID();
				setValue(I_FTU_RecordWeight.COLUMNNAME_M_Warehouse_ID, warehouseID);
				
				int p_HRS_Analysis_ID = MHRSAnalysis.getByEntryTicket(et.getFTU_EntryTicket_ID());
				if(p_HRS_Analysis_ID > 0)
					setValue(I_FTU_RecordWeight.COLUMNNAME_HRS_Analysis_ID, p_HRS_Analysis_ID);
			}
			
		} 
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_GrossWeight)
					|| getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_TareWeight) ) {
			BigDecimal value = (BigDecimal) getValue();
			//	BR [ 7 ]
			if(value == null)
				return "";
			if(getField().getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_GrossWeight)
					&& value.compareTo(Env.ZERO) <= 0)
				return "";
			if(getField().getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_TareWeight)
					&& value.compareTo(Env.ZERO) <= 0)
				return "";
			
			Integer m_FTU_EntryTicket_ID = (Integer) getValue(I_FTU_RecordWeight.COLUMNNAME_FTU_EntryTicket_ID);
			if (m_FTU_EntryTicket_ID == null || m_FTU_EntryTicket_ID.intValue() == 0)
				return "";
			
			BigDecimal tareWeight = (BigDecimal) (getValue(I_FTU_RecordWeight.COLUMNNAME_TareWeight) != null
					? getValue(I_FTU_RecordWeight.COLUMNNAME_TareWeight)
					: Env.ZERO);
		    BigDecimal grossWeight = (BigDecimal) (getValue(I_FTU_RecordWeight.COLUMNNAME_GrossWeight) != null
		    		? getValue(I_FTU_RecordWeight.COLUMNNAME_GrossWeight)
		    		: Env.ZERO);
		    BigDecimal netWeight = grossWeight.subtract(tareWeight);
		    //	Set Net Weight
		    setValue(I_FTU_RecordWeight.COLUMNNAME_NetWeight,netWeight);

		    boolean isSOTrx = getValue(I_FTU_RecordWeight.COLUMNNAME_IsSOTrx).equals("Y");
		    
		    Timestamp today = new Timestamp(System.currentTimeMillis());  
		    //	Set Day
		    if(!isSOTrx){
		    	if(getField().getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_TareWeight)
		    			&& tareWeight.compareTo(Env.ZERO) != 0){
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_OutDate, today);
		    	}else if(getField().getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_GrossWeight)
		    			&& grossWeight.compareTo(Env.ZERO) != 0){
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_InDate, today);
		    	}
		    	//	Valid Weight Status
		    	if(grossWeight.compareTo(Env.ZERO) == 0)
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_WeightStatus, X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForGrossWeight);
		    	else if(tareWeight.compareTo(Env.ZERO) == 0)
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_WeightStatus, X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForTareWeight);
		    	else
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_WeightStatus, X_FTU_RecordWeight.WEIGHTSTATUS_Completed);	    	
		    } else{
		    	if(getField().getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_TareWeight)
		    			&& tareWeight.compareTo(Env.ZERO) != 0){
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_InDate, today);
		    	}else if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_GrossWeight)
		    			&& grossWeight.compareTo(Env.ZERO) != 0){
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_OutDate, today);
		    	}
		    	//	Valid Weight Status
		    	if(tareWeight.compareTo(Env.ZERO) == 0)
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_WeightStatus, X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForTareWeight);
		    	else if(grossWeight.compareTo(Env.ZERO) == 0)
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_WeightStatus, X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForGrossWeight);
		    	else
		    		setValue(I_FTU_RecordWeight.COLUMNNAME_WeightStatus, X_FTU_RecordWeight.WEIGHTSTATUS_Completed);
		    } 
		} 
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_FTU_LoadOrder_ID)) {

			if (getValue() == null)
				return "";
			
			if (getField().getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_FTU_LoadOrder_ID)){
				int l_FTU_LoadOrder_ID = (Integer) getValue();
				
				if (l_FTU_LoadOrder_ID != 0)
				{
					MFTULoadOrder lo = new MFTULoadOrder(getCtx(), l_FTU_LoadOrder_ID, null);
					MFTULoadOrderLine[] lolines = lo.getLines(true);
					//get First Product From Load Order
					if (lolines.length > 0 )
						setValue(I_FTU_RecordWeight.COLUMNNAME_M_Product_ID, lolines[0].getM_Product_ID());
				}
			}
			
		} 
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_FTU_Chute_ID)) {

			Integer m_FTU_Chute_ID = (Integer) getValue();
			if (m_FTU_Chute_ID == null || m_FTU_Chute_ID.intValue() == 0)
				return "";
			
			MFTUChute chute = new MFTUChute(getCtx(), m_FTU_Chute_ID, null);
			if(chute.getM_Warehouse_ID() > 0)
				setValue(I_FTU_RecordWeight.COLUMNNAME_M_Warehouse_ID, chute.getM_Warehouse_ID());
			
		}
		//	Added by Jorge Colmenarez 2021-06-07 15:15
		//	Set values from RW Selected
		if(getColumnName().equals(I_FTU_RecordWeight.COLUMNNAME_FTU_RecordWeightSource_ID))
		{
			if(getValue() == null)
				return "";
			
			Integer rwSourceID = (Integer) getValue();
			MFTURecordWeight rw = new MFTURecordWeight(getCtx(), rwSourceID, null);
			setValue("FTU_EntryTicket_ID", rw.getFTU_EntryTicket_ID());
			setValue("FTU_LoadOrder_ID", rw.getFTU_LoadOrder_ID());
			setValue("M_Shipper_ID", rw.getM_Shipper_ID());
			setValue("FTU_Driver_ID", rw.getFTU_Driver_ID());
			setValue("FTU_Vehicle_ID", rw.getFTU_Vehicle_ID());
			setValue("FTU_WeightScale_ID", rw.getFTU_WeightScale_ID());
			setValue("M_Warehouse_ID", rw.getM_Warehouse_ID());
			setValue("C_UOM_ID", rw.getC_UOM_ID());
			setValue("TareWeight", rw.getGrossWeight());
			setValue("NetWeight", rw.getGrossWeight().negate());
			setValue("InDate", rw.getOutDate());
			setValue("WeightStatus", "G");
			
		}
		//	End Jorge Colmenarez
		return "";
	}

}
