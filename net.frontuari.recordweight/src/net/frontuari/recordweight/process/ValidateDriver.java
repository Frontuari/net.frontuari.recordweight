package net.frontuari.recordweight.process;

import java.io.IOException;

import org.compiere.process.ProcessInfoParameter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import net.frontuari.recordweight.model.MFTUDriver;

import net.frontuari.recordweight.base.FTUProcess;

@org.adempiere.base.annotation.Process
public class ValidateDriver extends FTUProcess{
	
	String Cedula;
	int FTU_Driver_ID;
	private String ConsultarDatos() throws IOException {
		Document doc = Jsoup.connect("http://www.cne.gob.ve/web/registro_electoral/ce.php?nacionalidad=V&cedula="+Cedula).get();
		String Nombre = doc.select("body > table > tbody > tr > td > table > tbody > tr:nth-child(5) > td > table > tbody > tr:nth-child(2) > td > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2) > b").text();
		if (Nombre==null || Nombre.contentEquals("")) {
			
			addLog("La cédula: " + Cedula + " No está registrada en el CNE");
			return "";
		}
		MFTUDriver Driver = new MFTUDriver(getCtx(),FTU_Driver_ID,get_TrxName());
				Driver.setName(Nombre);
				Driver.saveEx();
		return "";
		}
		
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		for (ProcessInfoParameter param: getParameter())
		{
			String name = param.getParameterName();
			
			if ("Value".equals(name))
				Cedula = param.getParameterAsString();
			else if ("FTU_Driver_ID".equals(name))
				FTU_Driver_ID = param.getParameterAsInt();
			
		}
	}

	@Override
	protected String doIt() throws Exception {
		ConsultarDatos();
		return null;
	}

}
