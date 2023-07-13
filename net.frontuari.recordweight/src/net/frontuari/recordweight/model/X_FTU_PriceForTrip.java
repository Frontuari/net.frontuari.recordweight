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
import org.compiere.util.KeyNamePair;

/** Generated Model for FTU_PriceForTrip
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_PriceForTrip")
public class X_FTU_PriceForTrip extends PO implements I_FTU_PriceForTrip, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230713L;

    /** Standard Constructor */
    public X_FTU_PriceForTrip (Properties ctx, int FTU_PriceForTrip_ID, String trxName)
    {
      super (ctx, FTU_PriceForTrip_ID, trxName);
      /** if (FTU_PriceForTrip_ID == 0)
        {
			setValueMax (Env.ZERO);
// 0
			setValueMin (Env.ZERO);
// 0
        } */
    }

    /** Standard Constructor */
    public X_FTU_PriceForTrip (Properties ctx, int FTU_PriceForTrip_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_PriceForTrip_ID, trxName, virtualColumns);
      /** if (FTU_PriceForTrip_ID == 0)
        {
			setValueMax (Env.ZERO);
// 0
			setValueMin (Env.ZERO);
// 0
        } */
    }

    /** Load Constructor */
    public X_FTU_PriceForTrip (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
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
      StringBuilder sb = new StringBuilder ("X_FTU_PriceForTrip[")
        .append(get_ID()).append("]");
      return sb.toString();
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
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
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
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
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

	/** Set Distance on Km.
		@param Distance Distance on Km
	*/
	public void setDistance (BigDecimal Distance)
	{
		set_Value (COLUMNNAME_Distance, Distance);
	}

	/** Get Distance on Km.
		@return Distance on Km	  */
	public BigDecimal getDistance()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Distance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public net.frontuari.recordweight.model.I_FTU_DeliveryRute getFTU_DeliveryRute() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_DeliveryRute)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_DeliveryRute.Table_ID)
			.getPO(getFTU_DeliveryRute_ID(), get_TrxName());
	}

	/** Set FTU_DeliveryRute_ID.
		@param FTU_DeliveryRute_ID FTU_DeliveryRute_ID
	*/
	public void setFTU_DeliveryRute_ID (int FTU_DeliveryRute_ID)
	{
		if (FTU_DeliveryRute_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_DeliveryRute_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_DeliveryRute_ID, Integer.valueOf(FTU_DeliveryRute_ID));
	}

	/** Get FTU_DeliveryRute_ID.
		@return FTU_DeliveryRute_ID	  */
	public int getFTU_DeliveryRute_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_DeliveryRute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Price For Trip.
		@param FTU_PriceForTrip_ID Price For Trip
	*/
	public void setFTU_PriceForTrip_ID (int FTU_PriceForTrip_ID)
	{
		if (FTU_PriceForTrip_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_PriceForTrip_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_PriceForTrip_ID, Integer.valueOf(FTU_PriceForTrip_ID));
	}

	/** Get Price For Trip.
		@return Price For Trip	  */
	public int getFTU_PriceForTrip_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_PriceForTrip_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Price For Trip UU.
		@param FTU_PriceForTrip_UU Price For Trip UU
	*/
	public void setFTU_PriceForTrip_UU (String FTU_PriceForTrip_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_PriceForTrip_UU, FTU_PriceForTrip_UU);
	}

	/** Get Price For Trip UU.
		@return Price For Trip UU	  */
	public String getFTU_PriceForTrip_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_PriceForTrip_UU);
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
		set_Value (COLUMNNAME_PriceActual, PriceActual);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getPriceActual()));
    }

	/** Set Max. Value.
		@param ValueMax Maximum Value for a field
	*/
	public void setValueMax (BigDecimal ValueMax)
	{
		set_Value (COLUMNNAME_ValueMax, ValueMax);
	}

	/** Get Max. Value.
		@return Maximum Value for a field
	  */
	public BigDecimal getValueMax()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ValueMax);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Min. Value.
		@param ValueMin Minimum Value for a field
	*/
	public void setValueMin (BigDecimal ValueMin)
	{
		set_Value (COLUMNNAME_ValueMin, ValueMin);
	}

	/** Get Min. Value.
		@return Minimum Value for a field
	  */
	public BigDecimal getValueMin()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ValueMin);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}