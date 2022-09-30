/**
 * 
 */
package net.frontuari.recordweight.model;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MProduct;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MWarehouse;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *
 **/

public class MFTULoadOrderLine extends X_FTU_LoadOrderLine {

	/** Parent					*/
	protected MFTULoadOrder	m_parent = null;
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MFTULoadOrder getParent()
	{
		if (m_parent == null)
			m_parent = new MFTULoadOrder(getCtx(), getFTU_LoadOrder_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	public void setHeaderInfo (MFTULoadOrder loadorder)
	{
		m_parent = loadorder;
	}	//	setHeaderInfo
	public void setLoadOrder (MFTULoadOrder loadorder)
	{
		setClientOrg(loadorder);
		//
		setHeaderInfo(loadorder);	//	sets MARILoadOrder
	}	//	setLoadOrder
	/**
	 * 
	 */
	private static final long serialVersionUID = 880647930300989209L;

	/**
	 * *** Constructor ***
	 * @param ctx
	 * @param FTU_LoadOrderLine_ID
	 * @param trxName
	 */
	public MFTULoadOrderLine(Properties ctx, int FTU_LoadOrderLine_ID,
			String trxName) {
		super(ctx, FTU_LoadOrderLine_ID, trxName);
	}

	/**
	 * *** Constructor ***
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTULoadOrderLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	public MFTULoadOrderLine (MFTULoadOrder loadorder)
	{
		this (loadorder.getCtx(), 0, loadorder.get_TrxName());
		if (loadorder.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setFTU_LoadOrder_ID(loadorder.getFTU_LoadOrder_ID());
		setLoadOrder(loadorder);
	}	//	MARILoadOrderLine
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		MProduct m_Product = MProduct.get(getCtx(), getM_Product_ID());
		if(m_Product != null) {
			BigDecimal m_Weight = m_Product.getWeight();
			BigDecimal m_Volume = m_Product.getVolume();
			//	Valid Weight
			if(m_Weight == null)
				m_Weight = Env.ZERO;
			//	Valid Volume
			if(m_Volume == null)
				m_Volume = Env.ZERO;
			//	For Quantity
			if(is_ValueChanged("Qty")) {
				BigDecimal m_Qty = getQty();
				//	Valid Quantity
				if(m_Qty == null)
					m_Qty = Env.ZERO;
				//	Set Weight and Volume
				setWeight(m_Qty.multiply(m_Weight));
				setVolume(m_Qty.multiply(m_Volume));
			} else if(is_ValueChanged("ConfirmedQty")) {
				BigDecimal m_ConfirmedQty = getConfirmedQty();
				BigDecimal m_ConfirmedWeight = getConfirmedWeight();
				//	Valid Quantity
				if(m_ConfirmedQty == null)
					m_ConfirmedQty = Env.ZERO;
				//	Set Confirmed Weight
				if(m_ConfirmedWeight == null)
					m_ConfirmedWeight = Env.ZERO;
				if (m_ConfirmedWeight.compareTo(Env.ZERO) == 0)
				setConfirmedWeight(m_ConfirmedQty.multiply(m_Weight));
			}	
		}
		//	Add Warehouse
		if(is_ValueChanged("C_OrderLine_ID")
				|| is_ValueChanged("DD_OrderLine_ID")
				|| is_ValueChanged("M_InOutLine_ID")
				|| is_ValueChanged("M_MovementLine_ID")) {
			int m_M_Warehouse_ID = 0;
			//	For Sales Order
			if(getC_OrderLine_ID() != 0) {
				m_M_Warehouse_ID = DB.getSQLValue(get_TrxName(), "SELECT ol.M_Warehouse_ID "
						+ "FROM C_OrderLine ol "
						+ "WHERE ol.C_OrderLine_ID = ?", getC_OrderLine_ID());
			} else if(getDD_OrderLine_ID() != 0) {
				m_M_Warehouse_ID = DB.getSQLValue(get_TrxName(), "SELECT l.M_Warehouse_ID "
						+ "FROM DD_OrderLine dol "
						+ "INNER JOIN M_Locator l ON(l.M_Locator_ID = dol.M_Locator_ID) "
						+ "WHERE dol.DD_OrderLine_ID = ?", getDD_OrderLine_ID());
			}
			//	Set Warehouse
			if(m_M_Warehouse_ID > 0) {
				setM_Warehouse_ID(m_M_Warehouse_ID);
			}
		}
		//	
		return super.beforeSave(newRecord);
	}
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		super.afterSave(newRecord, success);
		if(success)
			return updateHeader();
		return false;
	}//	End After Save
	
	@Override
	protected boolean afterDelete (boolean success) {
		super.afterDelete(success);
		if(success)
			return updateHeader();
		//	Return
		return false;
	} //	End After Delete
	
	/**
	 * Update Header
	 * @return
	 * @return boolean
	 */
	private boolean updateHeader() {
		//	Recalculate Header
		//	Update Load Order Header
		String sql = "UPDATE FTU_LoadOrder lo SET Weight=("
				+ "	SELECT COALESCE(SUM(lol.Weight),0) FROM FTU_LoadOrderLine lol WHERE lol.FTU_LoadOrder_ID=lo.FTU_LoadOrder_ID)"
				+ " ,Volume =( SELECT COALESCE(SUM(lol.Volume),0) FROM FTU_LoadOrderLine lol "
				+ " WHERE lol.FTU_LoadOrder_ID=lo.FTU_LoadOrder_ID)"
				+ " ,ConfirmedWeight =( SELECT COALESCE(SUM(lol.ConfirmedWeight),0) FROM FTU_LoadOrderLine lol "
				+ " WHERE lol.FTU_LoadOrder_ID=lo.FTU_LoadOrder_ID) WHERE lo.FTU_LoadOrder_ID = " + getFTU_LoadOrder_ID();
		//
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.warning("(1) #" + no);
		return no == 1;
	}	//	updateHeaderTax
	/**
	 * 	Set (default) Locator based on qty.
	 * 	@param Qty quantity
	 * 	Assumes Warehouse is set
	 */
	public void setM_Locator_ID(BigDecimal Qty)
	{
		//	Locator established
		if (getM_Locator_ID() != 0)
			return;
		//	No Product
		if (getM_Product_ID() == 0)
		{
			set_ValueNoCheck(COLUMNNAME_M_Locator_ID, null);
			return;
		}

		//	Get existing Location
		int M_Locator_ID = MStorageOnHand.getM_Locator_ID (getM_Warehouse_ID(),
				getM_Product_ID(), getM_AttributeSetInstance_ID(),
				Qty, get_TrxName());
		//	Get default Location
		if (M_Locator_ID == 0)
		{
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
		}
		setM_Locator_ID(M_Locator_ID);
	}	//	setM_Locator_ID

}
