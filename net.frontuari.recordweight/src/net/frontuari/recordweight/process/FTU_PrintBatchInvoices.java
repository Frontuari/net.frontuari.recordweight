package net.frontuari.recordweight.process;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.SimplePDFViewer;
import org.compiere.model.MInvoice;
import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.print.ReportEngine;
import org.compiere.util.Msg;
import net.frontuari.recordweight.base.FTUProcess;

@org.adempiere.base.annotation.Process
public class FTU_PrintBatchInvoices extends FTUProcess {
	
	private int n_Invoice = 0;
	private List<File> pdfList = new ArrayList<File>();
	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		
		
		MQuery query = MQuery.get(getCtx(), getAD_PInstance_ID(), MInvoice.Table_Name);
		PrintInfo info = new PrintInfo(getProcessInfo());
		getSelection()
			.stream()
			.forEach(selection -> printInvoice(selection, query, info));
		
		return Msg.getMsg(getCtx(), "InvoicePrinted", new Object[] {n_Invoice});
	}
	
	protected void postProcess(boolean success)
	{
		if (success) {
			if (processUI != null) {
				AEnv.executeAsyncDesktopTask(new Runnable() {
					@Override
					public void run() {
						String title = getProcessInfo().getTitle();
						if (pdfList.size() > 1) {
							try {
								File outFile = File.createTempFile(title, ".pdf");					
								AEnv.mergePdf(pdfList, outFile);

								Window win = new SimplePDFViewer(title, new FileInputStream(outFile));
								SessionManager.getAppDesktop().showWindow(win, "center");
							} catch (Exception e) {
								log.log(Level.SEVERE, e.getLocalizedMessage(), e);
							}
						} else if (pdfList.size() > 0) {
							try {
								Window win = new SimplePDFViewer(title, new FileInputStream(pdfList.get(0)));
								SessionManager.getAppDesktop().showWindow(win, "center");
							} catch (Exception e)
							{
								log.log(Level.SEVERE, e.getLocalizedMessage(), e);
							}
						}
					//Invoices.forEach(inv -> setPrinted(inv));
					}
				});
			}
		}
	}
	
	private void printInvoice(String selection, MQuery query, PrintInfo info) {
		
		String [] str = selection.split("\\|");
		int C_Invoice_ID = Integer.parseInt(str[0].trim());
		ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.INVOICE, C_Invoice_ID);
		pdfList.add(re.getPDF());
		n_Invoice++;
		MInvoice inv = new MInvoice(getCtx(),C_Invoice_ID,get_TrxName());
		inv.setIsPrinted(true);
		inv.saveEx();
		//Invoices.add(inv);		
	}
	
	/*private void setPrinted(MInvoice inv) {
		inv.setIsPrinted(true);
		inv.saveEx();
	}*/
}
