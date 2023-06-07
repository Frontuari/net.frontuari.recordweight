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

/** Generated Model for FTU_FormuleFunction
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_FormuleFunction")
public class X_FTU_FormuleFunction extends PO implements I_FTU_FormuleFunction, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_FTU_FormuleFunction (Properties ctx, int FTU_FormuleFunction_ID, String trxName)
    {
      super (ctx, FTU_FormuleFunction_ID, trxName);
      /** if (FTU_FormuleFunction_ID == 0)
        {
			setFTU_FormuleFunction_ID (0);
			setFTU_FormuleFunction_UU (null);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_FTU_FormuleFunction (Properties ctx, int FTU_FormuleFunction_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_FormuleFunction_ID, trxName, virtualColumns);
      /** if (FTU_FormuleFunction_ID == 0)
        {
			setFTU_FormuleFunction_ID (0);
			setFTU_FormuleFunction_UU (null);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_FormuleFunction (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_FormuleFunction[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set FTU_Functions_Formule.
		@param FTU_FormuleFunction_ID FTU_Functions_Formule
	*/
	public void setFTU_FormuleFunction_ID (int FTU_FormuleFunction_ID)
	{
		if (FTU_FormuleFunction_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_FormuleFunction_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_FormuleFunction_ID, Integer.valueOf(FTU_FormuleFunction_ID));
	}

	/** Get FTU_Functions_Formule.
		@return FTU_Functions_Formule	  */
	public int getFTU_FormuleFunction_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_FormuleFunction_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_Functions_Formule_UU.
		@param FTU_FormuleFunction_UU FTU_Functions_Formule_UU
	*/
	public void setFTU_FormuleFunction_UU (String FTU_FormuleFunction_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_FormuleFunction_UU, FTU_FormuleFunction_UU);
	}

	/** Get FTU_Functions_Formule_UU.
		@return FTU_Functions_Formule_UU	  */
	public String getFTU_FormuleFunction_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_FormuleFunction_UU);
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Qty Parameters.
		@param QtyParameter Qty Parameters
	*/
	public void setQtyParameter (BigDecimal QtyParameter)
	{
		set_Value (COLUMNNAME_QtyParameter, QtyParameter);
	}

	/** Get Qty Parameters.
		@return Qty Parameters	  */
	public BigDecimal getQtyParameter()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyParameter);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}