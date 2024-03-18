package net.frontuari.recordweight.event;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.doc.AfterReverseAccrual;
import org.adempiere.base.event.annotations.doc.AfterReverseCorrect;
import org.adempiere.base.event.annotations.doc.AfterVoid;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;

@EventTopicDelegate
@ModelEventTopic(modelClass = MInvoice.class)
public class RWInvoiceEvents extends ModelEventDelegate<MInvoice> {

	CLogger log = CLogger.getCLogger(RWInvoiceEvents.class);
	
	public RWInvoiceEvents(MInvoice po, Event event) {
		super(po, event);
		log.info("Load Events from Record Weight Plugins for Invoice");
	}

	@AfterVoid
	@AfterReverseCorrect
	@AfterReverseAccrual
	public void onAfterVRCA() {
		MInvoice inv = getModel();
		if(inv.isSOTrx()) {
			MInvoiceLine inv_Line [] =  inv.getLines(true);
			for (MInvoiceLine mInvoiceLine : inv_Line) {
				String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE C_InvoiceLine_ID = ?";
				int p_FTU_LoadOrderLine_ID = DB.getSQLValue(mInvoiceLine.get_TrxName(), sql, mInvoiceLine.get_ID());
				if(p_FTU_LoadOrderLine_ID <= 0)
					continue;
				
				MFTULoadOrderLine lin = new MFTULoadOrderLine(mInvoiceLine.getCtx(), p_FTU_LoadOrderLine_ID, mInvoiceLine.get_TrxName());
				lin.setC_InvoiceLine_ID(0);
				lin.saveEx();
				
				MFTULoadOrder lo = new MFTULoadOrder(lin.getCtx(),lin.getFTU_LoadOrder_ID(), lin.get_TrxName());
				lo.setIsInvoiced(false);
				lo.saveEx();
			}
		}
	}
	
}
