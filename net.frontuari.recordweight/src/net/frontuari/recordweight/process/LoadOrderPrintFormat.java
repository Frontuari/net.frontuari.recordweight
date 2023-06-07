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

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTULoadOrder;

@org.adempiere.base.annotation.Process
public class LoadOrderPrintFormat extends FTUProcess {

	private List<File> pdfList = new ArrayList<File>();
	
	public LoadOrderPrintFormat() {
	}

	@Override
	protected void prepare() {
	}

	@Override
	protected String doIt() throws Exception {
		MFTULoadOrder lo = new MFTULoadOrder(getCtx(), getRecord_ID(), get_TrxName());
		pdfList.add(lo.createPDF());
		return "OK";
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
					}
				});
			}
		}
	}

}
