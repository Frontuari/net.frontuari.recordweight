package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

public class MFTUShipperLiquidationLine extends X_FTU_SLLine {

	private static final long serialVersionUID = -4424212544652292348L;

	public MFTUShipperLiquidationLine(Properties ctx, int FTU_SLLine_ID, String trxName) {
		super(ctx, FTU_SLLine_ID, trxName);
	}

	public MFTUShipperLiquidationLine(Properties ctx, int FTU_SLLine_ID, String trxName, String... virtualColumns) {
		super(ctx, FTU_SLLine_ID, trxName, virtualColumns);
	}

	public MFTUShipperLiquidationLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/** Parent						*/
	protected MFTUShipperLiquidation	m_parent = null;
	
	/**
	 * 	Parent Constructor
	 * 	@param shipper liquidation parent
	 */
	public MFTUShipperLiquidationLine (MFTUShipperLiquidation liq)
	{
		this (liq.getCtx(), 0, liq.get_TrxName());
		if (liq.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(liq.getAD_Client_ID(), liq.getAD_Org_ID());
		setFTU_ShipperLiquidation_ID(liq.get_ID());
		m_parent = liq;
	}	//	MFTUShipperLiquidationLine

	/***
	 * get Parent
	 * @return
	 */
	public MFTUShipperLiquidation getParent() {
		if (m_parent == null)
			m_parent = new MFTUShipperLiquidation(getCtx(), getFTU_ShipperLiquidation_ID(), get_TrxName());
		return m_parent;
	}
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		if (!success)
			return success;
		//	Update header GrandTotal
		if(newRecord || is_ValueChanged(X_FTU_SLLine.COLUMNNAME_Amount)) {
			MFTUShipperLiquidation parent = getParent();
			//	Get LineNetAmt
			BigDecimal oldValue = (newRecord ? BigDecimal.ZERO : (BigDecimal)get_ValueOld(COLUMNNAME_Amount));
			if(oldValue ==null)
				oldValue = BigDecimal.ZERO;
			//	Switch Operations
			BigDecimal discount = parent.getDiscountAmt();
			BigDecimal grandTotal = parent.getGrandTotal();
			BigDecimal prepayamt = parent.getPrePaymentAmt();
			switch (getDeductionType()) {
				case DEDUCTIONTYPE_DeductionByAP:
					discount = discount.subtract(oldValue);
					discount = discount.add(getAmount());
					parent.setGrandTotal(discount);
					break;
				case DEDUCTIONTYPE_DeductionByAC:
					discount = discount.subtract(oldValue);
					discount = discount.add(getAmount());
					parent.setDiscountAmt(discount);
					break;
				case DEDUCTIONTYPE_PrePayments:
					prepayamt = prepayamt.subtract(oldValue);
					prepayamt = prepayamt.add(getAmount());
					parent.setPrePaymentAmt(prepayamt);
					break;
				default:
					grandTotal = grandTotal.subtract(oldValue);
					grandTotal = grandTotal.add(getAmount());
					parent.setGrandTotal(grandTotal);
					break;
			}
			BigDecimal payAmt = grandTotal.subtract(discount).subtract(prepayamt);
			parent.setPayAmt(payAmt);
			parent.saveEx();
		}
		return success;
	}

	@Override
	protected boolean afterDelete(boolean success) {
		if (!success)
			return success;
		
		MFTUShipperLiquidation parent = getParent();
		//	Get LineNetAmt
		BigDecimal oldValue = (BigDecimal)get_ValueOld(COLUMNNAME_Amount);
		if(oldValue ==null)
			oldValue = BigDecimal.ZERO;
		//	Switch Operations
		BigDecimal discount = parent.getDiscountAmt();
		BigDecimal grandTotal = parent.getGrandTotal();
		BigDecimal prepayamt = parent.getPrePaymentAmt();
		switch (getDeductionType()) {
			case DEDUCTIONTYPE_DeductionByAP:
				discount = discount.subtract(oldValue);
				parent.setGrandTotal(discount);
				break;
			case DEDUCTIONTYPE_DeductionByAC:
				discount = discount.subtract(oldValue);
				parent.setDiscountAmt(discount);
				break;
			case DEDUCTIONTYPE_PrePayments:
				prepayamt = prepayamt.subtract(oldValue);
				parent.setPrePaymentAmt(prepayamt);
				break;
			default:
				grandTotal = grandTotal.subtract(oldValue);
				parent.setGrandTotal(grandTotal);
				break;
		}
		BigDecimal payAmt = grandTotal.subtract(discount).subtract(prepayamt);
		parent.setPayAmt(payAmt);
		parent.saveEx();
		return success;
	}
}
