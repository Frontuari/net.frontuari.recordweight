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

/** Generated Model for HRS_AnalysisLine
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_HRS_AnalysisLine extends PO implements I_HRS_AnalysisLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20221104L;

    /** Standard Constructor */
    public X_HRS_AnalysisLine (Properties ctx, int HRS_AnalysisLine_ID, String trxName)
    {
      super (ctx, HRS_AnalysisLine_ID, trxName);
      /** if (HRS_AnalysisLine_ID == 0)
        {
			setHRS_AnalysisLine_ID (0);
			setHRS_AnalysisLine_UU (null);
			setResult (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_HRS_AnalysisLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRS_AnalysisLine[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public net.frontuari.recordweight.model.I_HRS_Analysis getHRS_Analysis() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_HRS_Analysis)MTable.get(getCtx(), net.frontuari.recordweight.model.I_HRS_Analysis.Table_Name)
			.getPO(getHRS_Analysis_ID(), get_TrxName());	}

	/** Set Analysis.
		@param HRS_Analysis_ID Analysis	  */
	public void setHRS_Analysis_ID (int HRS_Analysis_ID)
	{
		if (HRS_Analysis_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRS_Analysis_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRS_Analysis_ID, Integer.valueOf(HRS_Analysis_ID));
	}

	/** Get Analysis.
		@return Analysis	  */
	public int getHRS_Analysis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRS_Analysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Analysis Line.
		@param HRS_AnalysisLine_ID Analysis Line	  */
	public void setHRS_AnalysisLine_ID (int HRS_AnalysisLine_ID)
	{
		if (HRS_AnalysisLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRS_AnalysisLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRS_AnalysisLine_ID, Integer.valueOf(HRS_AnalysisLine_ID));
	}

	/** Get Analysis Line.
		@return Analysis Line	  */
	public int getHRS_AnalysisLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRS_AnalysisLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRS_AnalysisLine_UU.
		@param HRS_AnalysisLine_UU HRS_AnalysisLine_UU	  */
	public void setHRS_AnalysisLine_UU (String HRS_AnalysisLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_HRS_AnalysisLine_UU, HRS_AnalysisLine_UU);
	}

	/** Get HRS_AnalysisLine_UU.
		@return HRS_AnalysisLine_UU	  */
	public String getHRS_AnalysisLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_HRS_AnalysisLine_UU);
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