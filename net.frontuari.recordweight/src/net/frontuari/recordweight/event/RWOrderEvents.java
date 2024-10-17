package net.frontuari.recordweight.event;

import java.math.BigDecimal;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.doc.BeforeVoid;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

@EventTopicDelegate
@ModelEventTopic(modelClass = MOrder.class)
public class RWOrderEvents extends ModelEventDelegate<MOrder> {

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(RWOrderEvents.class);
	
	public RWOrderEvents(MOrder po, Event event) {
		super(po, event);
		log.warning("Load Event for Order from recordweight plugins");
	}
	
	@BeforeVoid
	public void onBeforeVoid(){
		MOrder order = getModel();
		String sqlload = "SELECT COUNT(lo.FTU_LoadOrder_ID) FROM FTU_LoadOrderLine fl JOIN FTU_LoadOrder AS lo ON lo.FTU_LoadOrder_ID = fl.FTU_LoadOrder_ID JOIN C_OrderLine co ON co.C_Orderline_ID = fl.C_OrderLine_ID WHERE lo.DocStatus  NOT IN ('RE','VO') AND co.C_Order_ID = " + order.getC_Order_ID();
		BigDecimal qtyload = DB.getSQLValueBD(order.get_TrxName(), sqlload);
			if (qtyload.compareTo(Env.ZERO) > 0)
				throw new AdempiereException("La orden posee " + qtyload + " Ordenes de Carga.");
	}
}
