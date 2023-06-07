package net.frontuari.recordweight.events;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.doc.AfterComplete;
import org.adempiere.base.event.annotations.doc.AfterReverseAccrual;
import org.adempiere.base.event.annotations.doc.AfterReverseCorrect;
import org.adempiere.base.event.annotations.doc.AfterVoid;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;

import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;

@EventTopicDelegate
@ModelEventTopic(modelClass = MMovement.class)
public class RWMovementEvents extends ModelEventDelegate<MMovement> {

	CLogger log = CLogger.getCLogger(RWMovementEvents.class);
	
	public RWMovementEvents(MMovement po, Event event) {
		super(po, event);
		log.info("Load Events from Record Weight Plugins for Movement");
	}

	@AfterVoid
	@AfterReverseCorrect
	@AfterReverseAccrual
	public void onAfterVRCA() {
		MMovement mMovement = getModel();
		MMovementLine mMovementLine [] =  mMovement.getLines(true);
		for (MMovementLine m_MovementLine : mMovementLine) {
			String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE M_MovementLine_ID = ?";
			int p_FTU_LoadOrderLine_ID = DB.getSQLValue(m_MovementLine.get_TrxName(), sql, m_MovementLine.get_ID());
			if(p_FTU_LoadOrderLine_ID <= 0)
				continue;
			
			MFTULoadOrderLine lin = 
					new MFTULoadOrderLine(m_MovementLine.getCtx(), p_FTU_LoadOrderLine_ID, m_MovementLine.get_TrxName());
			lin.setM_MovementLine_ID(0);
			lin.setConfirmedQty(lin.getConfirmedQty().subtract(m_MovementLine.getMovementQty()));
			lin.saveEx();
			
			MFTULoadOrder lo = new MFTULoadOrder(lin.getCtx(),lin.getFTU_LoadOrder_ID(), lin.get_TrxName());
			lo.setIsMoved(false);
			lo.saveEx();
		}
	}
	
	@AfterComplete
	public void onAfterComplete() {
		MMovement movement = getModel();
		MMovementLine [] lines = movement.getLines(true);
		
		Arrays
			.stream(lines)
			.filter(line -> line.getDD_OrderLine_ID() != 0)
			.forEach(line -> {
				MDDOrderLine ddOrderLine = (MDDOrderLine) line.getDD_OrderLine();
				
				BigDecimal qtyDelivered = Optional.ofNullable(ddOrderLine.getQtyDelivered())
						.orElse(BigDecimal.ZERO);
				
				ddOrderLine.setQtyDelivered(qtyDelivered.add(line.getMovementQty()));
				ddOrderLine.saveEx();
			});
	}
	
}
