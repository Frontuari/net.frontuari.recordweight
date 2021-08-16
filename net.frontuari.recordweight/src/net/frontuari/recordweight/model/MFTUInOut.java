package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MFTUInOut extends MInOut {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6755525115817369463L;

	public MFTUInOut(Properties ctx, int M_InOut_ID, String trxName) {
		super(ctx, M_InOut_ID, trxName);
	}

	public MFTUInOut(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MFTUInOut(MOrder order, int C_DocTypeShipment_ID, Timestamp movementDate) {
		super(order, C_DocTypeShipment_ID, movementDate);
	}

	public MFTUInOut(MInvoice invoice, int C_DocTypeShipment_ID, Timestamp movementDate, int M_Warehouse_ID) {
		super(invoice, C_DocTypeShipment_ID, movementDate, M_Warehouse_ID);
	}

	public MFTUInOut(MInOut original, int C_DocTypeShipment_ID, Timestamp movementDate) {
		super(original, C_DocTypeShipment_ID, movementDate);
	}
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	@Override
	public String prepareIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		//  Order OR RMA can be processed on a shipment/receipt
		if (getC_Order_ID() != 0 && getM_RMA_ID() != 0)
		{
		    m_processMsg = "@OrderOrRMA@";
		    return DocAction.STATUS_Invalid;
		}
		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		
		/***
		 * Remove Credit Check
		 * @author Jorge Colmenarez, 2021-08-05 09:14
		//	Credit Check
		if (isSOTrx() && !isReversal())
		{
			I_C_Order order = getC_Order();
			if (order != null && MDocType.DOCSUBTYPESO_PrepayOrder.equals(order.getC_DocType().getDocSubTypeSO())
					&& !MSysConfig.getBooleanValue(MSysConfig.CHECK_CREDIT_ON_PREPAY_ORDER, true, getAD_Client_ID(), getAD_Org_ID())) {
				// ignore -- don't validate Prepay Orders depending on sysconfig parameter
			} else {
				MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
				if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
				{
					m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
						+ bp.getTotalOpenBalance()
						+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
					return DocAction.STATUS_Invalid;
				}
				if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus()))
				{
					m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@="
						+ bp.getTotalOpenBalance()
						+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
					return DocAction.STATUS_Invalid;
				}
				if (!MBPartner.SOCREDITSTATUS_NoCreditCheck.equals(bp.getSOCreditStatus())
						&& Env.ZERO.compareTo(bp.getSO_CreditLimit()) != 0)
				{
					BigDecimal notInvoicedAmt = MBPartner.getNotInvoicedAmt(getC_BPartner_ID());
					if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus(notInvoicedAmt)))
					{
						m_processMsg = "@BPartnerOverSCreditHold@ - @TotalOpenBalance@="
							+ bp.getTotalOpenBalance() + ", @NotInvoicedAmt@=" + notInvoicedAmt
							+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
						return DocAction.STATUS_Invalid;
					}
				}
			}
		}*/

		//	Lines
		MInOutLine[] lines = getLines(true);
		if (lines == null || lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		BigDecimal Volume = Env.ZERO;
		BigDecimal Weight = Env.ZERO;

		//	Mandatory Attributes
		for (int i = 0; i < lines.length; i++)
		{
			MInOutLine line = lines[i];
			MProduct product = line.getProduct();
			if (product != null)
			{
				Volume = Volume.add(product.getVolume().multiply(line.getMovementQty()));
				Weight = Weight.add(product.getWeight().multiply(line.getMovementQty()));
			}
			//
			if (line.getM_AttributeSetInstance_ID() != 0)
				continue;
			if (product != null && product.isASIMandatory(isSOTrx()))
			{
				if (product.getAttributeSet() != null && !product.getAttributeSet().excludeTableEntry(MInOutLine.Table_ID, isSOTrx())) {
					m_processMsg = "@M_AttributeSet_ID@ @IsMandatory@ (@Line@ #" + lines[i].getLine() +
									", @M_Product_ID@=" + product.getValue() + ")";
					return DocAction.STATUS_Invalid;
				}
			}
		}
		setVolume(Volume);
		setWeight(Weight);

		if (!isReversal())	//	don't change reversal
		{
			createConfirmation();
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
		//	Warehouse Org
		/**
		 * Comment Code because Not Apply for company COPOSA
		 * @author Jorge Colmenarez, 2021-05-05 12:35, jcolmenarez@frontuari.net
		if (newRecord)
		{
			if (wh.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("WarehouseOrgConflict", "");
				return false;
			}
		}
		*/

		boolean disallowNegInv = wh.isDisallowNegativeInv();
		String DeliveryRule = getDeliveryRule();
		if((disallowNegInv && DELIVERYRULE_Force.equals(DeliveryRule)) ||
				(DeliveryRule == null || DeliveryRule.length()==0))
			setDeliveryRule(DELIVERYRULE_Availability);

        // Shipment/Receipt can have either Order/RMA (For Movement type)
        if (getC_Order_ID() != 0 && getM_RMA_ID() != 0)
        {
            log.saveError("OrderOrRMA", "");
            return false;
        }

		//	Shipment - Needs Order/RMA
		if (!getMovementType().contentEquals(MInOut.MOVEMENTTYPE_CustomerReturns) && isSOTrx() && getC_Order_ID() == 0 && getM_RMA_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.translate(getCtx(), "C_Order_ID"));
			return false;
		}

        if (isSOTrx() && getM_RMA_ID() != 0)
        {
            // Set Document and Movement type for this Receipt
            MRMA rma = new MRMA(getCtx(), getM_RMA_ID(), get_TrxName());
            MDocType docType = MDocType.get(getCtx(), rma.getC_DocType_ID());
            setC_DocType_ID(docType.getC_DocTypeShipment_ID());
        }

		return true;
	}	//	beforeSave

}
