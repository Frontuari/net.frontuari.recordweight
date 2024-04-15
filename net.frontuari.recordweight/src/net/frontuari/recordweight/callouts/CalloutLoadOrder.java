package net.frontuari.recordweight.callouts;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.MOrderLine;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.X_FTU_LoadOrderLine;

@Callout(tableName = "FTU_LoadOrderLine", columnName = "C_OrderLine_ID")
public class CalloutLoadOrder extends FTUCallout {

	@Override
	protected String start() {
		String tableName = getTableName();
		String columnName = getColumnName();
		if(tableName.equals(X_FTU_LoadOrderLine.Table_Name))
		{
			if(columnName.equals(X_FTU_LoadOrderLine.COLUMNNAME_C_OrderLine_ID))
			{
				if(getValue() != null)
				{
					int olID = (Integer)getValue();
					MOrderLine ol = new MOrderLine(getCtx(), olID, null);
					getTab().setValue("M_Product_ID", ol.getM_Product_ID());
					getTab().setValue("Qty", ol.getQtyReserved());
					getTab().setValue("C_UOM_ID", ol.getC_UOM_ID());
				}
			}
		}
		
		
		return null;
	}

}
