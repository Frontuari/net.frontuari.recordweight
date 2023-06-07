/**
 * 
 */
package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MProduct;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;


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
				else {
					BigDecimal oldConfirmedQty = (BigDecimal)get_ValueOld("ConfirmedQty");
					if(oldConfirmedQty==null) 
						oldConfirmedQty = BigDecimal.ZERO;
					m_ConfirmedQty = m_ConfirmedQty.subtract(oldConfirmedQty);
					m_ConfirmedWeight = m_ConfirmedWeight.add(m_ConfirmedQty.multiply(m_Weight));
					setConfirmedWeight(m_ConfirmedWeight);
				}
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
	
	/** Lines					*/
	private MFTULoadOrderLineMA[]		m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@param whereClause
	 *	@return lines
	 */
	public MFTULoadOrderLineMA[] getLines (boolean requery, String whereClause)
	{
		if (m_lines != null && !requery)
		{
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MFTULoadOrderLineMA> list = new Query(getCtx(), MFTULoadOrderLineMA.Table_Name, "FTU_LoadOrderLine_ID=?"
				+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(getFTU_LoadOrderLine_ID())
		.setOrderBy(MFTULoadOrderLineMA.COLUMNNAME_DateMaterialPolicy)
		.list();
		
		m_lines = new MFTULoadOrderLineMA[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
	/***
	 * Delete lines MA
	 * @author Jorge Colmenarez, 2022-12-21 10:20
	 */
	@Override
	protected boolean beforeDelete() {
		//	Delete Lines
		MFTULoadOrderLineMA[] lines = getLines(true, "");
		if(lines.length > 0)
			for(MFTULoadOrderLineMA line : lines)
				line.deleteEx(true);
		
		return true;
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
		int M_Locator_ID = getM_Locator_ID (getM_Warehouse_ID(),
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
	/**************************************************************************
	 * 	Get Location with highest Locator Priority and a sufficient OnHand Qty
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_Product_ID product
	 * 	@param M_AttributeSetInstance_ID asi
	 * 	@param Qty qty
	 *	@param trxName transaction
	 * 	@return id
	 */
	public static int getM_Locator_ID (int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, BigDecimal Qty,
		String trxName)
	{
		int M_Locator_ID = 0;
		int firstM_Locator_ID = 0;
		String sql = "SELECT s.M_Locator_ID, s.QtyOnHand "
			+ "FROM M_StorageOnHand s"
			+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)"
			+ " LEFT JOIN M_LocatorType lt ON (l.M_LocatorType_ID=lt.M_LocatorType_ID)"
			+ " INNER JOIN M_Product p ON (s.M_Product_ID=p.M_Product_ID)"
			+ " LEFT OUTER JOIN M_AttributeSet mas ON (p.M_AttributeSet_ID=mas.M_AttributeSet_ID) "
			+ "WHERE l.M_Warehouse_ID=?"
			+ " AND s.M_Product_ID=?";
			if (M_AttributeSetInstance_ID > 0)
			sql = sql+ " AND (mas.IsInstanceAttribute IS NULL OR mas.IsInstanceAttribute='N' OR s.M_AttributeSetInstance_ID=?)";
			
			sql = sql+ " AND l.IsActive='Y' AND 'Y' = (CASE WHEN l.M_LocatorType_ID is not null THEN lt.IsAvailableForShipping ELSE 'N' END)  "
			+ "ORDER BY l.PriorityNo DESC, s.QtyOnHand DESC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setInt(2, M_Product_ID);
			if (M_AttributeSetInstance_ID > 0)
			pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				BigDecimal QtyOnHand = rs.getBigDecimal(2);
				if (QtyOnHand != null && Qty.compareTo(QtyOnHand) <= 0)
				{
					M_Locator_ID = rs.getInt(1);
					break;
				}
				if (firstM_Locator_ID == 0)
					firstM_Locator_ID = rs.getInt(1);
			}
		}
		catch (SQLException ex)
		{

		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (M_Locator_ID != 0)
			return M_Locator_ID;
		return firstM_Locator_ID;
	}	//	getM_Locator_ID

}
