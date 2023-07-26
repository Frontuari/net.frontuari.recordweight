package net.frontuari.recordweight.event;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.doc.AfterReverseAccrual;
import org.adempiere.base.event.annotations.doc.AfterReverseCorrect;
import org.adempiere.base.event.annotations.doc.AfterVoid;
import org.adempiere.base.event.annotations.po.BeforeDelete;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

import net.frontuari.payselection.model.MFTUPaymentRequest;

@EventTopicDelegate
@ModelEventTopic(modelClass = MFTUPaymentRequest.class)
public class PaymentRequestEvent extends ModelEventDelegate<MFTUPaymentRequest> {

	CLogger log = CLogger.getCLogger(MFTUPaymentRequest.class);
	
	public PaymentRequestEvent(MFTUPaymentRequest po, Event event) {
		super(po, event);
		log.warning("Events from RecordWeight Plugins for payment request");
	}

	@BeforeDelete
	@AfterVoid
	@AfterReverseCorrect
	@AfterReverseAccrual
	public void BeforeDelete() {
		MFTUPaymentRequest pr = getModel();
		//	UnAllocated with Liquidations
		DB.executeUpdate("UPDATE FTU_ShipperLiquidation SET FTU_PaymentRequest_ID = NULL WHERE FTU_PaymentRequest_ID = ?", pr.get_ID(), pr.get_TrxName());
	}
}
