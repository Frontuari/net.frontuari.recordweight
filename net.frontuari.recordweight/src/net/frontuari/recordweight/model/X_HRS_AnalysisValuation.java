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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for HRS_AnalysisValuation
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="HRS_AnalysisValuation")
public class X_HRS_AnalysisValuation extends PO implements I_HRS_AnalysisValuation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230709L;

    /** Standard Constructor */
    public X_HRS_AnalysisValuation (Properties ctx, int HRS_AnalysisValuation_ID, String trxName)
    {
      super (ctx, HRS_AnalysisValuation_ID, trxName);
      /** if (HRS_AnalysisValuation_ID == 0)
        {
			setHRS_AnalysisValuation_ID (0);
			setHRS_AnalysisValuation_UU (null);
        } */
    }

    /** Standard Constructor */
    public X_HRS_AnalysisValuation (Properties ctx, int HRS_AnalysisValuation_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, HRS_AnalysisValuation_ID, trxName, virtualColumns);
      /** if (HRS_AnalysisValuation_ID == 0)
        {
			setHRS_AnalysisValuation_ID (0);
			setHRS_AnalysisValuation_UU (null);
        } */
    }

    /** Load Constructor */
    public X_HRS_AnalysisValuation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HRS_AnalysisValuation[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public net.frontuari.recordweight.model.I_FTU_QualityParam getFTU_QualityParam() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_QualityParam)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_QualityParam.Table_ID)
			.getPO(getFTU_QualityParam_ID(), get_TrxName());
	}

	/** Set Quality Param.
		@param FTU_QualityParam_ID Quality Param
	*/
	public void setFTU_QualityParam_ID (int FTU_QualityParam_ID)
	{
		if (FTU_QualityParam_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_QualityParam_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_QualityParam_ID, Integer.valueOf(FTU_QualityParam_ID));
	}

	/** Get Quality Param.
		@return Quality Param	  */
	public int getFTU_QualityParam_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_QualityParam_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_HRS_Analysis getHRS_Analysis() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_HRS_Analysis)MTable.get(getCtx(), net.frontuari.recordweight.model.I_HRS_Analysis.Table_ID)
			.getPO(getHRS_Analysis_ID(), get_TrxName());
	}

	/** Set Analysis.
		@param HRS_Analysis_ID Analysis
	*/
	public void setHRS_Analysis_ID (int HRS_Analysis_ID)
	{
		if (HRS_Analysis_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HRS_Analysis_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HRS_Analysis_ID, Integer.valueOf(HRS_Analysis_ID));
	}

	/** Get Analysis.
		@return Analysis	  */
	public int getHRS_Analysis_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRS_Analysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Analysis Valuation.
		@param HRS_AnalysisValuation_ID Analysis Valuation
	*/
	public void setHRS_AnalysisValuation_ID (int HRS_AnalysisValuation_ID)
	{
		if (HRS_AnalysisValuation_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HRS_AnalysisValuation_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HRS_AnalysisValuation_ID, Integer.valueOf(HRS_AnalysisValuation_ID));
	}

	/** Get Analysis Valuation.
		@return Analysis Valuation	  */
	public int getHRS_AnalysisValuation_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRS_AnalysisValuation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRS_AnalysisValuation_UU.
		@param HRS_AnalysisValuation_UU HRS_AnalysisValuation_UU
	*/
	public void setHRS_AnalysisValuation_UU (String HRS_AnalysisValuation_UU)
	{
		set_ValueNoCheck (COLUMNNAME_HRS_AnalysisValuation_UU, HRS_AnalysisValuation_UU);
	}

	/** Get HRS_AnalysisValuation_UU.
		@return HRS_AnalysisValuation_UU	  */
	public String getHRS_AnalysisValuation_UU()
	{
		return (String)get_Value(COLUMNNAME_HRS_AnalysisValuation_UU);
	}

	/** Set Human Result.
		@param HumanResult Human Result
	*/
	public void setHumanResult (String HumanResult)
	{
		set_Value (COLUMNNAME_HumanResult, HumanResult);
	}

	/** Get Human Result.
		@return Human Result	  */
	public String getHumanResult()
	{
		return (String)get_Value(COLUMNNAME_HumanResult);
	}

	/** Set System Result.
		@param SystemResult System Result
	*/
	public void setSystemResult (String SystemResult)
	{
		set_Value (COLUMNNAME_SystemResult, SystemResult);
	}

	/** Get System Result.
		@return System Result	  */
	public String getSystemResult()
	{
		return (String)get_Value(COLUMNNAME_SystemResult);
	}
}