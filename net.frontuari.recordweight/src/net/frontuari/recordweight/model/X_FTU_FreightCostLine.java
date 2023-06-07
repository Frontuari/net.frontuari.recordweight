/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for FTU_FreightCostLine
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_FreightCostLine")
public class X_FTU_FreightCostLine extends PO implements I_FTU_FreightCostLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_FTU_FreightCostLine (Properties ctx, int FTU_FreightCostLine_ID, String trxName)
    {
      super (ctx, FTU_FreightCostLine_ID, trxName);
      /** if (FTU_FreightCostLine_ID == 0)
        {
        } */
    }

    /** Standard Constructor */
    public X_FTU_FreightCostLine (Properties ctx, int FTU_FreightCostLine_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_FreightCostLine_ID, trxName, virtualColumns);
      /** if (FTU_FreightCostLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_FTU_FreightCostLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_FTU_FreightCostLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_ID(), get_TrxName());
	}

	/** Set Charge.
		@param C_Charge_ID Additional document charges
	*/
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Charge_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
	{
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_ID)
			.getPO(getC_ConversionType_ID(), get_TrxName());
	}

	/** Set Currency Type.
		@param C_ConversionType_ID Currency Conversion Rate Type
	*/
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_ConversionType_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
	{
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_ID)
			.getPO(getC_Currency_ID(), get_TrxName());
	}

	/** Set Currency.
		@param C_Currency_ID The Currency for this record
	*/
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Currency_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
	{
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_ID)
			.getPO(getC_Invoice_ID(), get_TrxName());
	}

	/** Set Invoice.
		@param C_Invoice_ID Invoice Identifier
	*/
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Costs.
		@param Costs Costs in accounting currency
	*/
	public void setCosts (BigDecimal Costs)
	{
		set_ValueNoCheck (COLUMNNAME_Costs, Costs);
	}

	/** Get Costs.
		@return Costs in accounting currency
	  */
	public BigDecimal getCosts()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Costs);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
	{
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_ID)
			.getPO(getC_SalesRegion_ID(), get_TrxName());
	}

	/** Set Sales Region.
		@param C_SalesRegion_ID Sales coverage region
	*/
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1)
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Factor.
		@param Factor Scaling factor.
	*/
	public void setFactor (BigDecimal Factor)
	{
		set_Value (COLUMNNAME_Factor, Factor);
	}

	/** Get Factor.
		@return Scaling factor.
	  */
	public BigDecimal getFactor()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Factor);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Final Price.
		@param FinalPrice Final Price
	*/
	public void setFinalPrice (BigDecimal FinalPrice)
	{
		set_ValueNoCheck (COLUMNNAME_FinalPrice, FinalPrice);
	}

	/** Get Final Price.
		@return Final Price	  */
	public BigDecimal getFinalPrice()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_FinalPrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public net.frontuari.recordweight.model.I_FTU_DeliveryRute getFTU_DeliveryRute() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_DeliveryRute)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_DeliveryRute.Table_ID)
			.getPO(getFTU_DeliveryRute_ID(), get_TrxName());
	}

	/** Set Delivery Rute.
		@param FTU_DeliveryRute_ID Delivery Rute
	*/
	public void setFTU_DeliveryRute_ID (int FTU_DeliveryRute_ID)
	{
		if (FTU_DeliveryRute_ID < 1)
			set_Value (COLUMNNAME_FTU_DeliveryRute_ID, null);
		else
			set_Value (COLUMNNAME_FTU_DeliveryRute_ID, Integer.valueOf(FTU_DeliveryRute_ID));
	}

	/** Get Delivery Rute.
		@return Delivery Rute	  */
	public int getFTU_DeliveryRute_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_DeliveryRute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_FreightCost getFTU_FreightCost() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_FreightCost)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_FreightCost.Table_ID)
			.getPO(getFTU_FreightCost_ID(), get_TrxName());
	}

	/** Set Bill of Lading.
		@param FTU_FreightCost_ID Bill of Lading
	*/
	public void setFTU_FreightCost_ID (int FTU_FreightCost_ID)
	{
		if (FTU_FreightCost_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_FreightCost_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_FreightCost_ID, Integer.valueOf(FTU_FreightCost_ID));
	}

	/** Get Bill of Lading.
		@return Bill of Lading	  */
	public int getFTU_FreightCost_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_FreightCost_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Bill of Lading Line.
		@param FTU_FreightCostLine_ID Bill of Lading Line
	*/
	public void setFTU_FreightCostLine_ID (int FTU_FreightCostLine_ID)
	{
		if (FTU_FreightCostLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_FreightCostLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_FreightCostLine_ID, Integer.valueOf(FTU_FreightCostLine_ID));
	}

	/** Get Bill of Lading Line.
		@return Bill of Lading Line	  */
	public int getFTU_FreightCostLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_FreightCostLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_InOut getM_InOut() throws RuntimeException
	{
		return (org.compiere.model.I_M_InOut)MTable.get(getCtx(), org.compiere.model.I_M_InOut.Table_ID)
			.getPO(getM_InOut_ID(), get_TrxName());
	}

	/** Set Shipment/Receipt.
		@param M_InOut_ID Material Shipment Document
	*/
	public void setM_InOut_ID (int M_InOut_ID)
	{
		if (M_InOut_ID < 1)
			set_ValueNoCheck (COLUMNNAME_M_InOut_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_InOut_ID, Integer.valueOf(M_InOut_ID));
	}

	/** Get Shipment/Receipt.
		@return Material Shipment Document
	  */
	public int getM_InOut_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InOut_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Price.
		@param Price Price
	*/
	public void setPrice (BigDecimal Price)
	{
		set_Value (COLUMNNAME_Price, Price);
	}

	/** Get Price.
		@return Price
	  */
	public BigDecimal getPrice()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Price);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Unit Price.
		@param PriceActual Actual Price 
	*/
	public void setPriceActual (BigDecimal PriceActual)
	{
		set_ValueNoCheck (COLUMNNAME_PriceActual, PriceActual);
	}

	/** Get Unit Price.
		@return Actual Price 
	  */
	public BigDecimal getPriceActual()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceActual);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rate.
		@param Rate Rate or Tax or Exchange
	*/
	public void setRate (BigDecimal Rate)
	{
		set_ValueNoCheck (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Max. Value.
		@param ValueMax Maximum Value for a field
	*/
	public void setValueMax (String ValueMax)
	{
		set_Value (COLUMNNAME_ValueMax, ValueMax);
	}

	/** Get Max. Value.
		@return Maximum Value for a field
	  */
	public String getValueMax()
	{
		return (String)get_Value(COLUMNNAME_ValueMax);
	}

	/** Set Min. Value.
		@param ValueMin Minimum Value for a field
	*/
	public void setValueMin (String ValueMin)
	{
		set_Value (COLUMNNAME_ValueMin, ValueMin);
	}

	/** Get Min. Value.
		@return Minimum Value for a field
	  */
	public String getValueMin()
	{
		return (String)get_Value(COLUMNNAME_ValueMin);
	}

	/** Set Weight.
		@param Weight Weight of a product
	*/
	public void setWeight (BigDecimal Weight)
	{
		set_Value (COLUMNNAME_Weight, Weight);
	}

	/** Get Weight.
		@return Weight of a product
	  */
	public BigDecimal getWeight()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}