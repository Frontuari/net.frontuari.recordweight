package net.frontuari.recordweight.callouts;

import java.math.BigDecimal;
import java.util.Optional;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.model.MUOMConversion;
import org.eevolution.model.I_DD_OrderLine;

import net.frontuari.recordweight.base.FTUCallout;

/**
 * Add Support for Conversion in DD Order Line
 * @author Jorge Colmenarez, 2023-06-07 15:19
 *
 */
@Callout(tableName = I_DD_OrderLine.Table_Name, columnName = {I_DD_OrderLine.COLUMNNAME_C_UOM_ID,
		I_DD_OrderLine.COLUMNNAME_QtyEntered,I_DD_OrderLine.COLUMNNAME_QtyOrdered,
		I_DD_OrderLine.COLUMNNAME_M_Product_ID})
public class CalloutDDOrderLine extends FTUCallout {

	private boolean isCalloutActive() {
		
		GridTab tab = getTab();
		
		return tab != null ? tab.getActiveCallouts().length > 1 : false;
	}
	
	@Override
	protected String start() {
		
		if  (isCalloutActive() || getValue() == null)
			return "";
		
		if (I_DD_OrderLine.COLUMNNAME_C_UOM_ID.equals(getColumnName()))
		{
			int C_UOM_ID = (Integer) getValue();
			
			BigDecimal qtyEntered = Optional.ofNullable((BigDecimal) getValue(I_DD_OrderLine.COLUMNNAME_QtyEntered))
					.orElse(BigDecimal.ZERO);
			
			int M_Product_ID = Optional.ofNullable((Integer) getValue(I_DD_OrderLine.COLUMNNAME_M_Product_ID))
					.orElse(0);
			
			BigDecimal qtyOrdered = MUOMConversion.convertProductFrom(getCtx(), M_Product_ID, C_UOM_ID, qtyEntered);
			
			setValue(I_DD_OrderLine.COLUMNNAME_QtyOrdered, qtyOrdered);
		}
		else if (I_DD_OrderLine.COLUMNNAME_QtyEntered.equals(getColumnName()))
		{
			BigDecimal qtyEntered = (BigDecimal) getValue();
			
			int C_UOM_ID = Optional.ofNullable((Integer) getValue(I_DD_OrderLine.COLUMNNAME_C_UOM_ID))
					.orElse(0);
			
			int M_Product_ID = Optional.ofNullable((Integer) getValue(I_DD_OrderLine.COLUMNNAME_M_Product_ID))
					.orElse(0);
			
			BigDecimal qtyOrdered = BigDecimal.ZERO;
			
			if (M_Product_ID == 0)
				qtyOrdered = qtyEntered;
			else
				qtyOrdered = MUOMConversion.convertProductFrom(getCtx(), M_Product_ID, C_UOM_ID, qtyEntered);
			
			setValue(I_DD_OrderLine.COLUMNNAME_QtyOrdered, qtyOrdered);
		}
		else if (I_DD_OrderLine.COLUMNNAME_QtyOrdered.equals(getColumnName()))
		{
			BigDecimal qtyOrdered = (BigDecimal) getValue();
			
			int C_UOM_ID = Optional.ofNullable((Integer) getValue(I_DD_OrderLine.COLUMNNAME_C_UOM_ID))
					.orElse(0);
			
			int M_Product_ID = Optional.ofNullable((Integer) getValue(I_DD_OrderLine.COLUMNNAME_M_Product_ID))
					.orElse(0);
			
			BigDecimal qtyEntered = BigDecimal.ZERO;
			
			if (M_Product_ID == 0)
				qtyEntered = qtyOrdered;
			else
				qtyEntered = MUOMConversion.convertProductTo(getCtx(), M_Product_ID, C_UOM_ID, qtyOrdered);
			
			setValue(I_DD_OrderLine.COLUMNNAME_QtyEntered, qtyEntered);
		}
		else if (I_DD_OrderLine.COLUMNNAME_M_Product_ID.equals(getColumnName()))
		{
			int M_Product_ID = (Integer) getValue();
			
			MProduct product = MProduct.get(getCtx(), M_Product_ID);
			
			setValue(I_DD_OrderLine.COLUMNNAME_C_UOM_ID, product.getC_UOM_ID());
		}
		
		return "";
	}

}
