package net.frontuari.recordweight.process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.compiere.process.ProcessInfoParameter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import net.frontuari.recordweight.model.MFTUDriver;

import net.frontuari.recordweight.base.FTUProcess;

public class ValidateDriver extends FTUProcess{
	
	String Cedula;
	int FTU_Driver_ID;
	private String ConsultarDatos() throws IOException {
		String str=Cedula.toLowerCase();

		

        if(str.contains("v")) {
        	str = str.replace("v", "");
        }
		
		
		Document doc = Jsoup.connect("http://www.cne.gob.ve/web/registro_electoral/ce.php?nacionalidad=V&cedula="+str).get();
		//System.out.println((doc.title()));
		String Nombre = doc.select("body > table > tbody > tr > td > table > tbody > tr:nth-child(5) > td > table > tbody > tr:nth-child(2) > td > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2) > b").text();
		//System.out.println(Nombre);
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
