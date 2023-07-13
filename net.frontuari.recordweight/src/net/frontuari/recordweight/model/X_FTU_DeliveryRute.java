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
import org.compiere.util.KeyNamePair;

/** Generated Model for FTU_DeliveryRute
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_DeliveryRute")
public class X_FTU_DeliveryRute extends PO implements I_FTU_DeliveryRute, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230713L;

    /** Standard Constructor */
    public X_FTU_DeliveryRute (Properties ctx, int FTU_DeliveryRute_ID, String trxName)
    {
      super (ctx, FTU_DeliveryRute_ID, trxName);
      /** if (FTU_DeliveryRute_ID == 0)
        {
			setFTU_DeliveryRute_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_FTU_DeliveryRute (Properties ctx, int FTU_DeliveryRute_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_DeliveryRute_ID, trxName, virtualColumns);
      /** if (FTU_DeliveryRute_ID == 0)
        {
			setFTU_DeliveryRute_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_DeliveryRute (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_DeliveryRute[")
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

	/** Set FTU_DeliveryRute_ID.
		@param FTU_DeliveryRute_ID FTU_DeliveryRute_ID
	*/
	public void setFTU_DeliveryRute_ID (int FTU_DeliveryRute_ID)
	{
		if (FTU_DeliveryRute_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_DeliveryRute_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_DeliveryRute_ID, Integer.valueOf(FTU_DeliveryRute_ID));
	}

	/** Get FTU_DeliveryRute_ID.
		@return FTU_DeliveryRute_ID	  */
	public int getFTU_DeliveryRute_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_DeliveryRute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_DeliveryRute_UU.
		@param FTU_DeliveryRute_UU FTU_DeliveryRute_UU
	*/
	public void setFTU_DeliveryRute_UU (String FTU_DeliveryRute_UU)
	{
		set_Value (COLUMNNAME_FTU_DeliveryRute_UU, FTU_DeliveryRute_UU);
	}

	/** Get FTU_DeliveryRute_UU.
		@return FTU_DeliveryRute_UU	  */
	public String getFTU_DeliveryRute_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_DeliveryRute_UU);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
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