package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MStorageOnHand;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;


public class MFTULoadOrderLineMA extends X_FTU_LoadOrderLineMA {
	

	public MFTULoadOrderLineMA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MFTULoadOrderLineMA(Properties ctx, int FTU_LoadOrderLineMA_ID, String trxName) {
		super(ctx, FTU_LoadOrderLineMA_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_InOutLine_ID line
	 *	@param trxName trx
	 *	@return allocations
	 */
	public static MFTULoadOrderLineMA[] get (Properties ctx, int FTU_LoadOrderLine_ID, String trxName)
	{
		Query query = MTable.get(ctx, MFTULoadOrderLineMA.Table_Name)
							.createQuery(I_FTU_LoadOrderLineMA.COLUMNNAME_FTU_LoadOrderLine_ID+"=?", trxName);
		query.setParameters(FTU_LoadOrderLine_ID);
		List<MFTULoadOrderLineMA> list = query.list();
		MFTULoadOrderLineMA[] retValue = new MFTULoadOrderLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**
	 * Total qty on LineMA for FTU_LoadOrderLine
	 * @param FTU_LoadOrderLine_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal getManualQty (int FTU_LoadOrderLine_ID, String trxName)
	{
		String sql = "SELECT SUM(Qty) FROM FTU_LoadOrderLineMA ma WHERE ma.FTU_LoadOrderLine_ID=? AND ma.IsAutoGenerated='N'";
		BigDecimal totalQty = DB.getSQLValueBD(trxName, sql, FTU_LoadOrderLine_ID);
		return totalQty==null?Env.ZERO:totalQty;
	} //totalLineQty
	
	/**
	 * 	Delete all Material Allocation for LoadOrderLine
	 *	@param FTU_LoadOrderLine
	 *	@param trxName transaction
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteLoadOrderLineMA (int FTU_LoadOrderLine, String trxName)
	{
		String sql = "DELETE FROM FTU_LoadOrderLineMA ma WHERE ma.FTU_LoadOrderLine_ID=? AND ma.IsAutoGenerated='Y'";
		return DB.executeUpdate(sql, FTU_LoadOrderLine, trxName);
	}	//	deleteInOutLineMA
	
	/**
	 * @param parent
	 * @param M_AttributeSetInstance_ID
	 * @param MovementQty
	 * @param DateMaterialPolicy
	 * @param isAutoGenerated
	 */
	public MFTULoadOrderLineMA (MFTULoadOrderLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty,Timestamp DateMaterialPolicy,boolean isAutoGenerated)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setFTU_LoadOrderLine_ID(parent.getFTU_LoadOrderLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setQty(MovementQty);
		if (DateMaterialPolicy == null)
		{
			if (M_AttributeSetInstance_ID > 0)
			{
				DateMaterialPolicy  = MStorageOnHand.getDateMaterialPolicy(parent.getM_Product_ID(), M_AttributeSetInstance_ID, parent.get_TrxName());
			}
			if (DateMaterialPolicy == null)
				DateMaterialPolicy = parent.getParent().getDateDoc();
		}
		setDateMaterialPolicy(DateMaterialPolicy);
		setIsAutoGenerated(isAutoGenerated);
	}	//	MInOutLineMA
	
	@Override
	public void setDateMaterialPolicy(Timestamp DateMaterialPolicy) {
		if (DateMaterialPolicy != null)
			DateMaterialPolicy = Util.removeTime(DateMaterialPolicy);
		super.setDateMaterialPolicy(DateMaterialPolicy);
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("ARILoadOrderLineMA[");
		sb.append("FTU_LoadOrderLine_ID=").append(getFTU_LoadOrderLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	public static MFTULoadOrderLineMA addOrCreate(MFTULoadOrderLine line, int M_AttributeSetInstance_ID, BigDecimal MovementQty, Timestamp DateMaterialPolicy){
		return addOrCreate(line,M_AttributeSetInstance_ID,MovementQty,DateMaterialPolicy,true);
	}
	
	public static MFTULoadOrderLineMA addOrCreate(MFTULoadOrderLine line, int M_AttributeSetInstance_ID, BigDecimal MovementQty, Timestamp DateMaterialPolicy,boolean isAutoGenerated)
	{
		Query query = new Query(Env.getCtx(), I_FTU_LoadOrderLineMA.Table_Name, "FTU_LoadOrderLine_ID=? AND M_AttributeSetInstance_ID=? AND DateMaterialPolicy=trunc(cast(? as date))", 
					line.get_TrxName());
		MFTULoadOrderLineMA po = query.setParameters(line.getFTU_LoadOrderLine_ID(), M_AttributeSetInstance_ID, DateMaterialPolicy).first();
		if (po == null)
			po = new MFTULoadOrderLineMA(line, M_AttributeSetInstance_ID, MovementQty, DateMaterialPolicy,isAutoGenerated);
		else
			po.setQty(po.getQty().add(MovementQty));
		return po;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
