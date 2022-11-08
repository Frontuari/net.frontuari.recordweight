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

/** Generated Model for FTU_Quality_Param
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_Quality_Param extends PO implements I_FTU_Quality_Param, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20221108L;

    /** Standard Constructor */
    public X_FTU_Quality_Param (Properties ctx, int FTU_Quality_Param_ID, String trxName)
    {
      super (ctx, FTU_Quality_Param_ID, trxName);
      /** if (FTU_Quality_Param_ID == 0)
        {
			setCode (null);
			setFTU_Quality_Param_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_Quality_Param (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_Quality_Param[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Column)MTable.get(getCtx(), org.compiere.model.I_AD_Column.Table_Name)
			.getPO(getAD_Column_ID(), get_TrxName());	}

	/** Set Column.
		@param AD_Column_ID 
		Column in the table
	  */
	public void setAD_Column_ID (int AD_Column_ID)
	{
		if (AD_Column_ID < 1) 
			set_Value (COLUMNNAME_AD_Column_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Column_ID, Integer.valueOf(AD_Column_ID));
	}

	/** Get Column.
		@return Column in the table
	  */
	public int getAD_Column_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Column_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_ValueNoCheck (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
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
			set_Value (COLUMNNAME_FTU_Analysis_Type_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_Analysis_Type_ID, Integer.valueOf(FTU_Analysis_Type_ID));
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

	public net.frontuari.recordweight.model.I_FTU_Functions_Formule getFTU_Functions_Formule() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Functions_Formule)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Functions_Formule.Table_Name)
			.getPO(getFTU_Functions_Formule_ID(), get_TrxName());	}

	/** Set FTU_Functions_Formule.
		@param FTU_Functions_Formule_ID FTU_Functions_Formule	  */
	public void setFTU_Functions_Formule_ID (int FTU_Functions_Formule_ID)
	{
		if (FTU_Functions_Formule_ID < 1) 
			set_Value (COLUMNNAME_FTU_Functions_Formule_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_Functions_Formule_ID, Integer.valueOf(FTU_Functions_Formule_ID));
	}

	/** Get FTU_Functions_Formule.
		@return FTU_Functions_Formule	  */
	public int getFTU_Functions_Formule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Functions_Formule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_Quality_Param getFTU_Parent() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Quality_Param)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Quality_Param.Table_Name)
			.getPO(getFTU_Parent_ID(), get_TrxName());	}

	/** Set FTU_Parent_ID.
		@param FTU_Parent_ID FTU_Parent_ID	  */
	public void setFTU_Parent_ID (int FTU_Parent_ID)
	{
		if (FTU_Parent_ID < 1) 
			set_Value (COLUMNNAME_FTU_Parent_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_Parent_ID, Integer.valueOf(FTU_Parent_ID));
	}

	/** Get FTU_Parent_ID.
		@return FTU_Parent_ID	  */
	public int getFTU_Parent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Parent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quality Param.
		@param FTU_Quality_Param_ID Quality Param	  */
	public void setFTU_Quality_Param_ID (int FTU_Quality_Param_ID)
	{
		if (FTU_Quality_Param_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Quality_Param_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Quality_Param_ID, Integer.valueOf(FTU_Quality_Param_ID));
	}

	/** Get Quality Param.
		@return Quality Param	  */
	public int getFTU_Quality_Param_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Quality_Param_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_Quality_Param_UU.
		@param FTU_Quality_Param_UU FTU_Quality_Param_UU	  */
	public void setFTU_Quality_Param_UU (String FTU_Quality_Param_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_Quality_Param_UU, FTU_Quality_Param_UU);
	}

	/** Get FTU_Quality_Param_UU.
		@return FTU_Quality_Param_UU	  */
	public String getFTU_Quality_Param_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_Quality_Param_UU);
	}

	/** Set Human Code.
		@param Human_Code Human Code	  */
	public void setHuman_Code (String Human_Code)
	{
		set_Value (COLUMNNAME_Human_Code, Human_Code);
	}

	/** Get Human Code.
		@return Human Code	  */
	public String getHuman_Code () 
	{
		return (String)get_Value(COLUMNNAME_Human_Code);
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Romana IsActive.
		@param Romana_IsActive Romana IsActive	  */
	public void setRomana_IsActive (boolean Romana_IsActive)
	{
		set_Value (COLUMNNAME_Romana_IsActive, Boolean.valueOf(Romana_IsActive));
	}

	/** Get Romana IsActive.
		@return Romana IsActive	  */
	public boolean isRomana_IsActive () 
	{
		Object oo = get_Value(COLUMNNAME_Romana_IsActive);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Romana SeqNo.
		@param Romana_SeqNo Romana SeqNo	  */
	public void setRomana_SeqNo (int Romana_SeqNo)
	{
		set_Value (COLUMNNAME_Romana_SeqNo, Integer.valueOf(Romana_SeqNo));
	}

	/** Get Romana SeqNo.
		@return Romana SeqNo	  */
	public int getRomana_SeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Romana_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Update Code Human.
		@param Update_Code_Human Update Code Human	  */
	public void setUpdate_Code_Human (String Update_Code_Human)
	{
		set_Value (COLUMNNAME_Update_Code_Human, Update_Code_Human);
	}

	/** Get Update Code Human.
		@return Update Code Human	  */
	public String getUpdate_Code_Human () 
	{
		return (String)get_Value(COLUMNNAME_Update_Code_Human);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}