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

/** Generated Model for HRS_QualityParameter
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_HRS_QualityParameter extends PO implements I_HRS_QualityParameter, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210120L;

    /** Standard Constructor */
    public X_HRS_QualityParameter (Properties ctx, int HRS_QualityParameter_ID, String trxName)
    {
      super (ctx, HRS_QualityParameter_ID, trxName);
      /** if (HRS_QualityParameter_ID == 0)
        {
			setHRS_QualityParameter_ID (0);
			setM_Attribute_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_HRS_QualityParameter (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRS_QualityParameter[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Analysis Number.
		@param AnalysisNumber Analysis Number	  */
	public void setAnalysisNumber (int AnalysisNumber)
	{
		set_Value (COLUMNNAME_AnalysisNumber, Integer.valueOf(AnalysisNumber));
	}

	/** Get Analysis Number.
		@return Analysis Number	  */
	public int getAnalysisNumber () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AnalysisNumber);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Attribute Value.
		@param AttributeValue 
		Value of the Attribute
	  */
	public void setAttributeValue (String AttributeValue)
	{
		set_Value (COLUMNNAME_AttributeValue, AttributeValue);
	}

	/** Get Attribute Value.
		@return Value of the Attribute
	  */
	public String getAttributeValue () 
	{
		return (String)get_Value(COLUMNNAME_AttributeValue);
	}

	/** Set Degree Factor.
		@param DegreeFactor Degree Factor	  */
	public void setDegreeFactor (int DegreeFactor)
	{
		set_Value (COLUMNNAME_DegreeFactor, Integer.valueOf(DegreeFactor));
	}

	/** Get Degree Factor.
		@return Degree Factor	  */
	public int getDegreeFactor () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DegreeFactor);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Factor1.
		@param Factor1 Factor1	  */
	public void setFactor1 (BigDecimal Factor1)
	{
		set_Value (COLUMNNAME_Factor1, Factor1);
	}

	/** Get Factor1.
		@return Factor1	  */
	public BigDecimal getFactor1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Factor1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Factor2.
		@param Factor2 Factor2	  */
	public void setFactor2 (BigDecimal Factor2)
	{
		set_Value (COLUMNNAME_Factor2, Factor2);
	}

	/** Get Factor2.
		@return Factor2	  */
	public BigDecimal getFactor2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Factor2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Factor3.
		@param Factor3 Factor3	  */
	public void setFactor3 (BigDecimal Factor3)
	{
		set_Value (COLUMNNAME_Factor3, Factor3);
	}

	/** Get Factor3.
		@return Factor3	  */
	public BigDecimal getFactor3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Factor3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Quality Parameter.
		@param HRS_QualityParameter_ID Quality Parameter	  */
	public void setHRS_QualityParameter_ID (int HRS_QualityParameter_ID)
	{
		if (HRS_QualityParameter_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRS_QualityParameter_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRS_QualityParameter_ID, Integer.valueOf(HRS_QualityParameter_ID));
	}

	/** Get Quality Parameter.
		@return Quality Parameter	  */
	public int getHRS_QualityParameter_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRS_QualityParameter_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRS_QualityParameter_UU.
		@param HRS_QualityParameter_UU HRS_QualityParameter_UU	  */
	public void setHRS_QualityParameter_UU (String HRS_QualityParameter_UU)
	{
		set_Value (COLUMNNAME_HRS_QualityParameter_UU, HRS_QualityParameter_UU);
	}

	/** Get HRS_QualityParameter_UU.
		@return HRS_QualityParameter_UU	  */
	public String getHRS_QualityParameter_UU () 
	{
		return (String)get_Value(COLUMNNAME_HRS_QualityParameter_UU);
	}

	/** Set Lower Limit.
		@param LowerLimit Lower Limit	  */
	public void setLowerLimit (BigDecimal LowerLimit)
	{
		set_Value (COLUMNNAME_LowerLimit, LowerLimit);
	}

	/** Get Lower Limit.
		@return Lower Limit	  */
	public BigDecimal getLowerLimit () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LowerLimit);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_M_Attribute getM_Attribute() throws RuntimeException
    {
		return (org.compiere.model.I_M_Attribute)MTable.get(getCtx(), org.compiere.model.I_M_Attribute.Table_Name)
			.getPO(getM_Attribute_ID(), get_TrxName());	}

	/** Set Attribute.
		@param M_Attribute_ID 
		Product Attribute
	  */
	public void setM_Attribute_ID (int M_Attribute_ID)
	{
		if (M_Attribute_ID < 1) 
			set_Value (COLUMNNAME_M_Attribute_ID, null);
		else 
			set_Value (COLUMNNAME_M_Attribute_ID, Integer.valueOf(M_Attribute_ID));
	}

	/** Get Attribute.
		@return Product Attribute
	  */
	public int getM_Attribute_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Attribute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Upper Limit.
		@param UpperLimit Upper Limit	  */
	public void setUpperLimit (BigDecimal UpperLimit)
	{
		set_Value (COLUMNNAME_UpperLimit, UpperLimit);
	}

	/** Get Upper Limit.
		@return Upper Limit	  */
	public BigDecimal getUpperLimit () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_UpperLimit);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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