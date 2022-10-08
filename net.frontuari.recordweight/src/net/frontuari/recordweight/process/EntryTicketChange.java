package net.frontuari.recordweight.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;

import net.frontuari.recordweight.base.CustomProcess;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTUVehicle;
import net.frontuari.recordweight.model.X_FTU_EntryTicket;

/**
 *
 */
public class EntryTicketChange extends CustomProcess {

	/**	Entry Ticket				*/
	private int p_FTU_EntryTicket_ID 	= 0;
	/**	Shipper						*/
	private int p_M_Shipper_ID 			= 0;
	/**	Driver						*/
	private int p_FTU_Driver_ID 		= 0;
	/**	Vehicle						*/
	private int p_FTU_Vehicle_ID 		= 0;
	/**	Quantity					*/
	private int m_Updated				= 0;
	
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			
			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if(name.equals("FTU_EntryTicket_ID"))
				p_FTU_EntryTicket_ID = para.getParameterAsInt();
			else if(name.equals("M_Shipper_ID"))
				p_M_Shipper_ID = para.getParameterAsInt();
			else if(name.equals("FTU_Driver_ID"))
				p_FTU_Driver_ID = para.getParameterAsInt();
			else if(name.equals("FTU_Vehicle_ID"))
				p_FTU_Vehicle_ID = para.getParameterAsInt();
		}
	}

	@Override
	protected String doIt() throws Exception {
		//	Valid Entry Ticket
		if(p_FTU_EntryTicket_ID == 0)
			throw new AdempiereUserError("@FTU_EntryTicket_ID@ @NotFound@");
		if(p_M_Shipper_ID == 0
				&& p_FTU_Driver_ID == 0
				&& p_FTU_Vehicle_ID == 0)
			throw new AdempiereUserError("[@M_Shipper_ID@ @FTU_Driver_ID@ @FTU_Vehicle_ID@] @NotFound@");
		//	Change Ticket
		MFTUEntryTicket m_FTU_EntryTicket = new MFTUEntryTicket(getCtx(), p_FTU_EntryTicket_ID, get_TrxName());
		//	Valid Document
		if(!m_FTU_EntryTicket.getDocStatus()
				.equals(X_FTU_EntryTicket.DOCSTATUS_Completed))
			throw new AdempiereUserError("@FTU_EntryTicket_ID@ @no@ @completed@");
		//	Change Shipper
		if(p_M_Shipper_ID != 0) {
			m_FTU_EntryTicket.setM_Shipper_ID(p_M_Shipper_ID);
		}
		//	Change Driver
		if(p_FTU_Driver_ID != 0) {
			m_FTU_EntryTicket.setFTU_Driver_ID(p_FTU_Driver_ID);
		}
		//	Change Vehicle
		if(p_FTU_Vehicle_ID != 0) {
			m_FTU_EntryTicket.setFTU_Vehicle_ID(p_FTU_Vehicle_ID);
		}
		m_FTU_EntryTicket.saveEx();
		//	Updated
		m_Updated ++;
		//	Add Log
		addLog("@FTU_EntryTicket_ID@ " + m_FTU_EntryTicket.getDocumentNo() + " @Updated@");
		//	Change Depend
		MFTULoadOrder[] m_LO = m_FTU_EntryTicket.getLoadOrder(null);
		
		//	For all Load Orders
		for(MFTULoadOrder m_FTU_LoadOrder : m_LO) {
			//	Change Shipper
			if(p_M_Shipper_ID != 0) {
				m_FTU_LoadOrder.setM_Shipper_ID(p_M_Shipper_ID);
			}
			//	Change Driver
			if(p_FTU_Driver_ID != 0) {
				m_FTU_LoadOrder.setFTU_Driver_ID(p_FTU_Driver_ID);
			}
			//	Change Vehicle
			if(p_FTU_Vehicle_ID != 0) {
				MFTUVehicle m_Vehicle = MFTUVehicle.get(getCtx(), p_FTU_Vehicle_ID);
				if(m_Vehicle.getLoadCapacity().doubleValue()
						< m_FTU_LoadOrder.getWeight().doubleValue()) {
					throw new AdempiereUserError("@LoadCapacity@ @less@ @Weight@ [@FTU_LoadOrder_ID@ " 
						+ m_FTU_LoadOrder.getDocumentNo() + "]");
				} else if(m_Vehicle.getVolumeCapacity().doubleValue() 
						< m_FTU_LoadOrder.getVolume().doubleValue()) {
					throw new AdempiereUserError("@VolumeCapacity@ @less@ @Volume@ [@FTU_LoadOrder_ID@ " 
						+ m_FTU_LoadOrder.getDocumentNo() + "]");	
				}
				//	
				m_FTU_LoadOrder.setFTU_Vehicle_ID(p_FTU_Vehicle_ID);
				m_FTU_LoadOrder.setLoadCapacity(m_Vehicle.getLoadCapacity());
				m_FTU_LoadOrder.setVolumeCapacity(m_Vehicle.getVolumeCapacity());
			}
			m_FTU_LoadOrder.saveEx();
			//	Add Log
			addLog("@FTU_LoadOrder_ID@ " + m_FTU_LoadOrder.getDocumentNo() + " @Updated@");
			//	Updated
			m_Updated ++;
		}
		//	Return
		return "@Updated@ " + m_Updated;
	}

}
