package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

public class MFTUFreightCostLine extends X_FTU_FreightCostLine{

	public MFTUFreightCostLine(Properties ctx, int FTU_BillOfLadingLine_ID, String trxName) {
		super(ctx, FTU_BillOfLadingLine_ID, trxName);
	}
	
	public MFTUFreightCostLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Parent						*/
	protected MFTUFreightCost m_parent = null;
	
	/**
	 * 	Parent Constructor
	 * 	@param shipper liquidation parent
	 */
	public MFTUFreightCostLine(MFTUFreightCost fc) {
		this(fc.getCtx(), 0, fc.get_TrxName());
		if(fc.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(fc.getAD_Client_ID(), fc.getAD_Org_ID());
		setFTU_FreightCost_ID(fc.get_ID());
		m_parent = fc;
	}	//	MFTUFreightCostLine
	
	/***
	 * get Parent
	 * @return
	 */
	public MFTUFreightCost getParent() {
		if(m_parent == null)
			m_parent = new MFTUFreightCost(getCtx(), getFTU_FreightCost_ID(), get_TrxName());
		return m_parent;
	}

	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		if (!success)
			return success;
		//	Update header GrandTotal
		if(newRecord 
				|| is_ValueChanged(X_FTU_FreightCostLine.COLUMNNAME_Costs)
				|| is_ValueChanged(X_FTU_FreightCostLine.COLUMNNAME_Weight)) {
			MFTUFreightCost parent = getParent();
			//	Get Cost
			BigDecimal oldValue = (newRecord ? BigDecimal.ZERO : (BigDecimal)get_ValueOld(COLUMNNAME_Costs));
			if(oldValue ==null)
				oldValue = BigDecimal.ZERO;
			//	Get Weight
			BigDecimal oldWeight = (newRecord ? BigDecimal.ZERO : (BigDecimal)get_ValueOld(COLUMNNAME_Weight));
			if(oldWeight ==null)
				oldWeight = BigDecimal.ZERO;
			
			BigDecimal weight = parent.getWeight();
			weight = weight.subtract(oldWeight);
			weight = weight.add(getWeight());
			parent.setWeight(weight);
			
			BigDecimal grandTotal = parent.getGrandTotal();
			grandTotal = grandTotal.subtract(oldValue);
			grandTotal = grandTotal.add(getCosts());
			parent.setGrandTotal(grandTotal);
			parent.saveEx();			
		}
		return success;
	}

	@Override
	protected boolean afterDelete(boolean success) {
		if (!success)
			return success;
		
		MFTUFreightCost parent = getParent();
		//	Get Cost
		BigDecimal oldValue = (BigDecimal)get_ValueOld(COLUMNNAME_Costs);
		if(oldValue ==null)
			oldValue = BigDecimal.ZERO;
		//	Get Weight
		BigDecimal oldWeight = (BigDecimal)get_ValueOld(COLUMNNAME_Weight);
		if(oldWeight ==null)
			oldWeight = BigDecimal.ZERO;
		
		BigDecimal weight = parent.getWeight();
		weight = weight.subtract(oldWeight);
		parent.setWeight(weight);
		
		BigDecimal grandTotal = parent.getGrandTotal();
		grandTotal = grandTotal.subtract(oldValue);
		parent.setGrandTotal(grandTotal);
		parent.saveEx();
		
		return success;
	}
	
	
}
