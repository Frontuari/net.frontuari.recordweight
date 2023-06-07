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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for FTU_LoadOrderLineMA
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_LoadOrderLineMA")
public class X_FTU_LoadOrderLineMA extends PO implements I_FTU_LoadOrderLineMA, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_FTU_LoadOrderLineMA (Properties ctx, int FTU_LoadOrderLineMA_ID, String trxName)
    {
      super (ctx, FTU_LoadOrderLineMA_ID, trxName);
      /** if (FTU_LoadOrderLineMA_ID == 0)
        {
			setDateMaterialPolicy (new Timestamp( System.currentTimeMillis() ));
			setM_AttributeSetInstance_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_FTU_LoadOrderLineMA (Properties ctx, int FTU_LoadOrderLineMA_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_LoadOrderLineMA_ID, trxName, virtualColumns);
      /** if (FTU_LoadOrderLineMA_ID == 0)
        {
			setDateMaterialPolicy (new Timestamp( System.currentTimeMillis() ));
			setM_AttributeSetInstance_ID (0);
        } */
    }

    /** Load Constructor */
    public X_FTU_LoadOrderLineMA (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_LoadOrderLineMA[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Date  Material Policy.
		@param DateMaterialPolicy Time used for LIFO and FIFO Material Policy
	*/
	public void setDateMaterialPolicy (Timestamp DateMaterialPolicy)
	{
		set_ValueNoCheck (COLUMNNAME_DateMaterialPolicy, DateMaterialPolicy);
	}

	/** Get Date  Material Policy.
		@return Time used for LIFO and FIFO Material Policy
	  */
	public Timestamp getDateMaterialPolicy()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateMaterialPolicy);
	}

	public net.frontuari.recordweight.model.I_FTU_LoadOrderLine getFTU_LoadOrderLine() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_LoadOrderLine)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_LoadOrderLine.Table_ID)
			.getPO(getFTU_LoadOrderLine_ID(), get_TrxName());
	}

	/** Set Load Order Line.
		@param FTU_LoadOrderLine_ID Load Order Line
	*/
	public void setFTU_LoadOrderLine_ID (int FTU_LoadOrderLine_ID)
	{
		if (FTU_LoadOrderLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_LoadOrderLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_LoadOrderLine_ID, Integer.valueOf(FTU_LoadOrderLine_ID));
	}

	/** Get Load Order Line.
		@return Load Order Line	  */
	public int getFTU_LoadOrderLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_LoadOrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_LoadOrderLineMA_UU.
		@param FTU_LoadOrderLineMA_UU FTU_LoadOrderLineMA_UU
	*/
	public void setFTU_LoadOrderLineMA_UU (String FTU_LoadOrderLineMA_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_LoadOrderLineMA_UU, FTU_LoadOrderLineMA_UU);
	}

	/** Get FTU_LoadOrderLineMA_UU.
		@return FTU_LoadOrderLineMA_UU	  */
	public String getFTU_LoadOrderLineMA_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_LoadOrderLineMA_UU);
	}

	/** Set Auto Generated.
		@param IsAutoGenerated Auto Generated
	*/
	public void setIsAutoGenerated (boolean IsAutoGenerated)
	{
		set_ValueNoCheck (COLUMNNAME_IsAutoGenerated, Boolean.valueOf(IsAutoGenerated));
	}

	/** Get Auto Generated.
		@return Auto Generated	  */
	public boolean isAutoGenerated()
	{
		Object oo = get_Value(COLUMNNAME_IsAutoGenerated);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException
	{
		return (org.compiere.model.I_M_AttributeSetInstance)MTable.get(getCtx(), org.compiere.model.I_M_AttributeSetInstance.Table_ID)
			.getPO(getM_AttributeSetInstance_ID(), get_TrxName());
	}

	/** Set Attribute Set Instance.
		@param M_AttributeSetInstance_ID Product Attribute Set Instance
	*/
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
	{
		if (M_AttributeSetInstance_ID < 0)
			set_ValueNoCheck (COLUMNNAME_M_AttributeSetInstance_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
	}

	/** Get Attribute Set Instance.
		@return Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quantity.
		@param Qty Quantity
	*/
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}