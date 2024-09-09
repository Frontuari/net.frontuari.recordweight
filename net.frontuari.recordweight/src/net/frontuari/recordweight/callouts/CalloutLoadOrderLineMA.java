package net.frontuari.recordweight.callouts;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.Query;
import org.compiere.util.CLogger;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.MFTULoadOrder;

@Callout(tableName = "FTU_LoadOrderLineMA", columnName = {"M_AttributeSetInstance_ID","Qty"})
public class CalloutLoadOrderLineMA extends FTUCallout {

	CLogger log = CLogger.getCLogger(CalloutLoadOrderLineMA.class);
	
	@Override
	protected String start() {
		String columnName = getColumnName();
		if(getValue()!=null && columnName.equalsIgnoreCase("M_AttributeSetInstance_ID")) {
			int asi = (int) getValue();
			int ProductId = (int) getTab().getParentTab().getValue("M_Product_ID");
			int LocatorId = (int) getTab().getParentTab().getValue("M_Locator_ID");
			
			MStorageOnHand sto = getStorage(getCtx(), LocatorId, ProductId, asi, null, null, null);
			if (sto != null) {
				getTab().setValue("MovementQty", sto.getQtyOnHand());
				getTab().setValue("DateMaterialPolicy", sto.getDateMaterialPolicy());
				getTab().setValue("M_Locator_ID", sto.getM_Locator_ID());
				getTab().setValue("DateLastInventory", sto.getDateLastInventory());
				int loadOrderID = (int) getTab().getParentTab().getValue("FTU_LoadOrder_ID");
				MFTULoadOrder lo = new MFTULoadOrder(getCtx(), loadOrderID, null);
				BigDecimal reserved = lo.getReservedforLoadOrder(sto);
				BigDecimal available = sto.getQtyOnHand().subtract(reserved);
				if(available.compareTo(BigDecimal.ZERO)<0)
					available = BigDecimal.ZERO;
				getTab().setValue("Qty", available);
			}
		}
		if(getValue()!=null && columnName.equalsIgnoreCase("Qty")) {
			BigDecimal qty = (BigDecimal)getValue();
			int asi = (int) getTab().getValue("M_AttributeSetInstance_ID");
			int ProductId = (int) getTab().getParentTab().getValue("M_Product_ID");
			int LocatorId = (int) getTab().getParentTab().getValue("M_Locator_ID");
			Timestamp dateMPolicy = (Timestamp) getTab().getValue("DateMaterialPolicy");
			Timestamp dateLastInventory = (Timestamp) getTab().getValue("DateLastInventory");
			MStorageOnHand sto = getStorage(getCtx(), LocatorId, ProductId, asi, dateMPolicy, dateLastInventory, null);
			if(sto != null) {
				int loadOrderID = (int) getTab().getParentTab().getValue("FTU_LoadOrder_ID");
				MFTULoadOrder lo = new MFTULoadOrder(getCtx(), loadOrderID, null);
				BigDecimal reserved = lo.getReservedforLoadOrder(sto);
				BigDecimal available = sto.getQtyOnHand().subtract(reserved);
				if(available.compareTo(qty)<0) {
					getTab().setValue("Qty", (BigDecimal)getOldValue());
					return "La cantidad ingresada: "+qty+" no puede ser mayor a la cantidad disponible: "+sto.getQtyOnHand()+", reservada para despachar: "+reserved+", disponible para cargar: "+available;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 	Get Storage Info
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance
	 *  @param dateMPolicy
	 *	@param trxName transaction
	 *	@return existing or null
	 */
	private MStorageOnHand getStorage (Properties ctx, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,Timestamp dateMPolicy, Timestamp dateLastInv, String trxName)
	{
		String sqlWhere = "M_Locator_ID=? AND M_Product_ID=? AND ";
		if (M_AttributeSetInstance_ID == 0)
			sqlWhere += "(M_AttributeSetInstance_ID=? OR M_AttributeSetInstance_ID IS NULL)";
		else
			sqlWhere += "M_AttributeSetInstance_ID=?";

		if (dateMPolicy != null)
			sqlWhere += " AND DateMaterialPolicy<=trunc(cast(? as date))";

		if (dateLastInv != null)
			sqlWhere += " AND DateLastInventory<=trunc(cast(? as date))";
		
		Query query = new Query(ctx, MStorageOnHand.Table_Name, sqlWhere, trxName);
		if (dateMPolicy != null && dateLastInv == null)
			query.setParameters(M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, dateMPolicy);
		else if (dateMPolicy == null && dateLastInv != null)
			query.setParameters(M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, dateLastInv);
		else if (dateMPolicy != null && dateLastInv != null)
			query.setParameters(M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, dateMPolicy, dateLastInv);
		else
			query.setParameters(M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID);									 
		MStorageOnHand retValue = query.first();
		
		if (retValue == null) {
			if (log.isLoggable(Level.FINE)) log.fine("Not Found - M_Locator_ID=" + M_Locator_ID 
					+ ", M_Product_ID=" + M_Product_ID + ", M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
		} else {
			if (log.isLoggable(Level.FINE)) log.fine("M_Locator_ID=" + M_Locator_ID 
					+ ", M_Product_ID=" + M_Product_ID + ", M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
		}
		return retValue;
	}	//	get

}
