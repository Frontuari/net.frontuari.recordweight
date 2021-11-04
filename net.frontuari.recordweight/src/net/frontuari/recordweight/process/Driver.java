package net.frontuari.recordweight.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTUVehicle;
import net.frontuari.recordweight.model.X_FTU_EntryTicket;

/**
 *
 */
public class Driver extends FTUProcess {

	String Cedula;
	int FTU_Driver_ID;
	
	@Override
	protected void prepare() {
		System.out.println("AAAAAAAAa");
		for (ProcessInfoParameter para:getParameter()){
			
			String name = para.getParameterName();
			
		}
	}

	@Override
	protected String doIt() throws Exception {
		System.out.println("b");
		return null;
	}

}
