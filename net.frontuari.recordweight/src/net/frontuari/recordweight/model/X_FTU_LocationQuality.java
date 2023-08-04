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

/** Generated Model for FTU_LocationQuality
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_LocationQuality")
public class X_FTU_LocationQuality extends PO implements I_FTU_LocationQuality, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230725L;

    /** Standard Constructor */
    public X_FTU_LocationQuality (Properties ctx, int FTU_LocationQuality_ID, String trxName)
    {
      super (ctx, FTU_LocationQuality_ID, trxName);
      /** if (FTU_LocationQuality_ID == 0)
        {
			setFTU_LocationQuality_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_FTU_LocationQuality (Properties ctx, int FTU_LocationQuality_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_LocationQuality_ID, trxName, virtualColumns);
      /** if (FTU_LocationQuality_ID == 0)
        {
			setFTU_LocationQuality_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_LocationQuality (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_LocationQuality[")
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

	/** Set Location Quality.
		@param FTU_LocationQuality_ID Location Quality
	*/
	public void setFTU_LocationQuality_ID (int FTU_LocationQuality_ID)
	{
		if (FTU_LocationQuality_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_LocationQuality_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_LocationQuality_ID, Integer.valueOf(FTU_LocationQuality_ID));
	}

	/** Get Location Quality.
		@return Location Quality
	  */
	public int getFTU_LocationQuality_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_LocationQuality_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_LocationQuality_UU.
		@param FTU_LocationQuality_UU FTU_LocationQuality_UU
	*/
	public void setFTU_LocationQuality_UU (String FTU_LocationQuality_UU)
	{
		set_Value (COLUMNNAME_FTU_LocationQuality_UU, FTU_LocationQuality_UU);
	}

	/** Get FTU_LocationQuality_UU.
		@return FTU_LocationQuality_UU	  */
	public String getFTU_LocationQuality_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_LocationQuality_UU);
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