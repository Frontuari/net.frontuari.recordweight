package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_M_InOut;
import org.compiere.model.I_M_Movement;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrderLine;

import net.frontuari.recordweight.base.FTUModelEvents;

public class FTUEvents extends FTUModelEvents {

	@Override
	protected void doHandleEvent() {
		System.out.println("entro");
		if(getEventType().equals(IEventTopics.DOC_AFTER_VOID)
				|| getEventType().equals(IEventTopics.DOC_AFTER_REVERSECORRECT)
				|| getEventType().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL)) {
			String tableName = getPO().get_TableName();
			if(tableName.equals(I_M_InOut.Table_Name)) {
				MInOut inout = (MInOut) getPO();
				if(inout.isSOTrx()) {
					MInOutLine inout_Line [] =  inout.getLines(true);
					for (MInOutLine mInOutLine : inout_Line) {
						String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE M_InOutLine_ID = ?";
						int p_FTU_LoadOrderLine_ID = DB.getSQLValue(mInOutLine.get_TrxName(), sql, mInOutLine.get_ID());
						if(p_FTU_LoadOrderLine_ID <= 0)
							continue;
						
						MFTULoadOrderLine lin = 
								new MFTULoadOrderLine(mInOutLine.getCtx(), p_FTU_LoadOrderLine_ID, mInOutLine.get_TrxName());
						lin.setM_InOutLine_ID(0);
						lin.setConfirmedQty(Env.ZERO);
						lin.saveEx();
						
						MFTULoadOrder lo = new MFTULoadOrder(lin.getCtx(),lin.getFTU_LoadOrder_ID(), lin.get_TrxName());
						lo.setIsDelivered(false);
						lo.saveEx();
					}
				}
			} else if(tableName.equals(I_C_Invoice.Table_Name)) {
				MInvoice inv = (MInvoice) getPO();
				if(inv.isSOTrx()) {
					MInvoiceLine inv_Line [] =  inv.getLines(true);
					for (MInvoiceLine mInvoiceLine : inv_Line) {
						String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE C_InvoiceLine_ID = ?";
						int p_FTU_LoadOrderLine_ID = DB.getSQLValue(mInvoiceLine.get_TrxName(), sql, mInvoiceLine.get_ID());
						if(p_FTU_LoadOrderLine_ID <= 0)
							continue;
						
						MFTULoadOrderLine lin = 
								new MFTULoadOrderLine(mInvoiceLine.getCtx(), p_FTU_LoadOrderLine_ID, mInvoiceLine.get_TrxName());
						lin.setC_InvoiceLine_ID(0);
						lin.saveEx();
						

						MFTULoadOrder lo = new MFTULoadOrder(lin.getCtx(),lin.getFTU_LoadOrder_ID(), lin.get_TrxName());
						lo.setIsInvoiced(false);
						lo.saveEx();
					}
				}
			} else if(tableName.equals(I_M_Movement.Table_Name)) {
				MMovement mMovement = (MMovement) getPO();
				MMovementLine mMovementLine [] =  mMovement.getLines(true);
				for (MMovementLine m_MovementLine : mMovementLine) {
					String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine WHERE M_MovementLine_ID = ?";
					int p_FTU_LoadOrderLine_ID = DB.getSQLValue(m_MovementLine.get_TrxName(), sql, m_MovementLine.get_ID());
					if(p_FTU_LoadOrderLine_ID <= 0)
						continue;
					
					MFTULoadOrderLine lin = 
							new MFTULoadOrderLine(m_MovementLine.getCtx(), p_FTU_LoadOrderLine_ID, m_MovementLine.get_TrxName());
					lin.setM_MovementLine_ID(0);
					lin.setConfirmedQty(Env.ZERO);
					lin.saveEx();
					
					MFTULoadOrder lo = new MFTULoadOrder(lin.getCtx(),lin.getFTU_LoadOrder_ID(), lin.get_TrxName());
					lo.setIsMoved(false);
					lo.saveEx();
				}
			}
			
		}
		//Add Support for Set Qty Delivered
		else if (IEventTopics.DOC_AFTER_COMPLETE.equals(getEventType())
				 && getPO().get_TableName().equals(MMovement.Table_Name))
			setQtyDelivered();		
	}
	
	/**
	 * @author Argenis Rodríguez
	 */
	private void setQtyDelivered() {
		
		MMovement movement = (MMovement) getPO();
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
