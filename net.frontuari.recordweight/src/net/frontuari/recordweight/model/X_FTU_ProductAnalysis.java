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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for FTU_ProductAnalysis
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_ProductAnalysis")
public class X_FTU_ProductAnalysis extends PO implements I_FTU_ProductAnalysis, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230825L;

    /** Standard Constructor */
    public X_FTU_ProductAnalysis (Properties ctx, int FTU_ProductAnalysis_ID, String trxName)
    {
      super (ctx, FTU_ProductAnalysis_ID, trxName);
      /** if (FTU_ProductAnalysis_ID == 0)
        {
			setFTU_ProductAnalysis_ID (0);
			setM_Product_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_FTU_ProductAnalysis (Properties ctx, int FTU_ProductAnalysis_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_ProductAnalysis_ID, trxName, virtualColumns);
      /** if (FTU_ProductAnalysis_ID == 0)
        {
			setFTU_ProductAnalysis_ID (0);
			setM_Product_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_ProductAnalysis (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_ProductAnalysis[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Product Analysis.
		@param FTU_ProductAnalysis_ID Product Analysis
	*/
	public void setFTU_ProductAnalysis_ID (int FTU_ProductAnalysis_ID)
	{
		if (FTU_ProductAnalysis_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_ProductAnalysis_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_ProductAnalysis_ID, Integer.valueOf(FTU_ProductAnalysis_ID));
	}

	/** Get Product Analysis.
		@return Product Analysis
	  */
	public int getFTU_ProductAnalysis_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_ProductAnalysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_ProductAnalysis_UU.
		@param FTU_ProductAnalysis_UU FTU_ProductAnalysis_UU
	*/
	public void setFTU_ProductAnalysis_UU (String FTU_ProductAnalysis_UU)
	{
		set_Value (COLUMNNAME_FTU_ProductAnalysis_UU, FTU_ProductAnalysis_UU);
	}

	/** Get FTU_ProductAnalysis_UU.
		@return FTU_ProductAnalysis_UU	  */
	public String getFTU_ProductAnalysis_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_ProductAnalysis_UU);
	}

	/** Set Comment/Help.
		@param Help Comment or Hint
	*/
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp()
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
	{
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_ID)
			.getPO(getM_Product_ID(), get_TrxName());
	}

	/** Set Product.
		@param M_Product_ID Product, Service, Item
	*/
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1)
			set_ValueNoCheck (COLUMNNAME_M_Product_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Valid from.
		@param ValidFrom Valid from including this date (first day)
	*/
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom()
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
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