package net.frontuari.recordweight.callouts;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.MStorageOnHand;

import net.frontuari.recordweight.base.FTUCallout;

@Callout(tableName = "FTU_LoadOrderLineMA", columnName = "M_AttributeSetInstance_ID")
public class CalloutLoadOrderLineMA extends FTUCallout {

	@Override
	protected String start() {
		if(getValue()!=null) {
			int asi = (int) getValue();
			int ProductId = (int) getTab().getParentTab().getValue("M_Product_ID");
			int LocatorId = 0;
				LocatorId = (int) getTab().getParentTab().getValue("M_Locator_ID");
				
				MStorageOnHand sto = MStorageOnHand.get(getCtx(), LocatorId, ProductId, asi, null, null);
				if (sto != null) {
					getTab().setValue("MovementQty", sto.getQtyOnHand());
					getTab().setValue("DateMaterialPolicy", sto.getDateMaterialPolicy());
					getTab().setValue("M_Locator_ID", sto.getM_Locator_ID());
				}
		}
		
		return null;
	}

}
