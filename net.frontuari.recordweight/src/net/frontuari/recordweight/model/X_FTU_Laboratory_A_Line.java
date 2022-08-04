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

/** Generated Model for FTU_Laboratory_A_Line
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_Laboratory_A_Line extends PO implements I_FTU_Laboratory_A_Line, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220803L;

    /** Standard Constructor */
    public X_FTU_Laboratory_A_Line (Properties ctx, int FTU_Laboratory_A_Line_ID, String trxName)
    {
      super (ctx, FTU_Laboratory_A_Line_ID, trxName);
      /** if (FTU_Laboratory_A_Line_ID == 0)
        {
			setFTU_Laboratory_A_Line_ID (0);
			setFTU_Laboratory_A_Line_UU (null);
			setResult (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_FTU_Laboratory_A_Line (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_Laboratory_A_Line[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Validation code.
		@param Code 
		Validation Code
	  */
	public void setCode (String Code)
	{
		set_Value (COLUMNNAME_Code, Code);
	}

	/** Get Validation code.
		@return Validation Code
	  */
	public String getCode () 
	{
		return (String)get_Value(COLUMNNAME_Code);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	public net.frontuari.recordweight.model.I_FTU_Analysis_Type getFTU_Analysis_Type() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Analysis_Type)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Analysis_Type.Table_Name)
			.getPO(getFTU_Analysis_Type_ID(), get_TrxName());	}

	/** Set Analysis_Type_ID.
		@param FTU_Analysis_Type_ID Analysis_Type_ID	  */
	public void setFTU_Analysis_Type_ID (int FTU_Analysis_Type_ID)
	{
		if (FTU_Analysis_Type_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Analysis_Type_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Analysis_Type_ID, Integer.valueOf(FTU_Analysis_Type_ID));
	}

	/** Get Analysis_Type_ID.
		@return Analysis_Type_ID	  */
	public int getFTU_Analysis_Type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Analysis_Type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_Laboratory_A_Line.
		@param FTU_Laboratory_A_Line_ID FTU_Laboratory_A_Line	  */
	public void setFTU_Laboratory_A_Line_ID (int FTU_Laboratory_A_Line_ID)
	{
		if (FTU_Laboratory_A_Line_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Laboratory_A_Line_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Laboratory_A_Line_ID, Integer.valueOf(FTU_Laboratory_A_Line_ID));
	}

	/** Get FTU_Laboratory_A_Line.
		@return FTU_Laboratory_A_Line	  */
	public int getFTU_Laboratory_A_Line_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Laboratory_A_Line_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_Laboratory_A_Line_UU.
		@param FTU_Laboratory_A_Line_UU FTU_Laboratory_A_Line_UU	  */
	public void setFTU_Laboratory_A_Line_UU (String FTU_Laboratory_A_Line_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_Laboratory_A_Line_UU, FTU_Laboratory_A_Line_UU);
	}

	/** Get FTU_Laboratory_A_Line_UU.
		@return FTU_Laboratory_A_Line_UU	  */
	public String getFTU_Laboratory_A_Line_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_Laboratory_A_Line_UU);
	}

	public net.frontuari.recordweight.model.I_FTU_Laboratory_Analysis getFTU_Laboratory_Analysis() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Laboratory_Analysis)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Laboratory_Analysis.Table_Name)
			.getPO(getFTU_Laboratory_Analysis_ID(), get_TrxName());	}

	/** Set FTU_Laboratory_Analysis.
		@param FTU_Laboratory_Analysis_ID FTU_Laboratory_Analysis	  */
	public void setFTU_Laboratory_Analysis_ID (int FTU_Laboratory_Analysis_ID)
	{
		if (FTU_Laboratory_Analysis_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Laboratory_Analysis_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Laboratory_Analysis_ID, Integer.valueOf(FTU_Laboratory_Analysis_ID));
	}

	/** Get FTU_Laboratory_Analysis.
		@return FTU_Laboratory_Analysis	  */
	public int getFTU_Laboratory_Analysis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Laboratory_Analysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Result.
		@param Result 
		Result of the action taken
	  */
	public void setResult (BigDecimal Result)
	{
		set_Value (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	public BigDecimal getResult () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Result);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}