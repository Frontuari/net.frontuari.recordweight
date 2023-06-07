package net.frontuari.recordweight.events;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.doc.AfterReverseAccrual;
import org.adempiere.base.event.annotations.doc.AfterReverseCorrect;
import org.adempiere.base.event.annotations.doc.AfterVoid;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;

@EventTopicDelegate
@ModelEventTopic(modelClass = MInOut.class)
public class RWInOutEvents extends ModelEventDelegate<MInOut> {

	CLogger log = CLogger.getCLogger(RWInOutEvents.class);
	
	public RWInOutEvents(MInOut po, Event event) {
		super(po, event);
		log.info("Load Events from Record Weight Plugins for InOut");
	}
	
	@AfterVoid
	@AfterReverseCorrect
	@AfterReverseAccrual
	public void onAfterVRCA() {
		MInOut inout = getModel();
		if(inout.isSOTrx()) {
			MInOutLine inout_Line [] =  inout.getLines(true);
			for (MInOutLine mInOutLine : inout_Line) {
				String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE M_InOutLine_ID = ?";
				int p_FTU_LoadOrderLine_ID = DB.getSQLValue(mInOutLine.get_TrxName(), sql, mInOutLine.get_ID());
				if(p_FTU_LoadOrderLine_ID <= 0)
					continue;
				
				MFTULoadOrderLine lin = new MFTULoadOrderLine(mInOutLine.getCtx(), p_FTU_LoadOrderLine_ID, mInOutLine.get_TrxName());
				lin.setM_InOutLine_ID(0);
				lin.setConfirmedQty(lin.getConfirmedQty().subtract(mInOutLine.getQtyEntered()));
				lin.saveEx();
				
				MFTULoadOrder lo = new MFTULoadOrder(lin.getCtx(),lin.getFTU_LoadOrder_ID(), lin.get_TrxName());
				lo.setIsDelivered(false);
				lo.saveEx();
			}
		}
	}

}
