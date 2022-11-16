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

/** Generated Model for FTU_Analysis_Type
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_Analysis_Type extends PO implements I_FTU_Analysis_Type, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20221104L;

    /** Standard Constructor */
    public X_FTU_Analysis_Type (Properties ctx, int FTU_Analysis_Type_ID, String trxName)
    {
      super (ctx, FTU_Analysis_Type_ID, trxName);
      /** if (FTU_Analysis_Type_ID == 0)
        {
			setFTU_Analysis_Type_ID (0);
			setFTU_Analysis_Type_UU (null);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_Analysis_Type (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_Analysis_Type[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set FTU_Analysis_Type_UU.
		@param FTU_Analysis_Type_UU FTU_Analysis_Type_UU	  */
	public void setFTU_Analysis_Type_UU (String FTU_Analysis_Type_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_Analysis_Type_UU, FTU_Analysis_Type_UU);
	}

	/** Get FTU_Analysis_Type_UU.
		@return FTU_Analysis_Type_UU	  */
	public String getFTU_Analysis_Type_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_Analysis_Type_UU);
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