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

/** Generated Model for FTU_ScreenConfig
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_ScreenConfig extends PO implements I_FTU_ScreenConfig, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200504L;

    /** Standard Constructor */
    public X_FTU_ScreenConfig (Properties ctx, int FTU_ScreenConfig_ID, String trxName)
    {
      super (ctx, FTU_ScreenConfig_ID, trxName);
      /** if (FTU_ScreenConfig_ID == 0)
        {
			setEndCharacter (null);
			setFTU_ScreenConfig_ID (0);
			setName (null);
			setStartCharacter (null);
			setStrLength (0);
        } */
    }

    /** Load Constructor */
    public X_FTU_ScreenConfig (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_ScreenConfig[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set EndCharacter.
		@param EndCharacter EndCharacter	  */
	public void setEndCharacter (String EndCharacter)
	{
		set_Value (COLUMNNAME_EndCharacter, EndCharacter);
	}

	/** Get EndCharacter.
		@return EndCharacter	  */
	public String getEndCharacter () 
	{
		return (String)get_Value(COLUMNNAME_EndCharacter);
	}

	/** Set Screen Configuration.
		@param FTU_ScreenConfig_ID Screen Configuration	  */
	public void setFTU_ScreenConfig_ID (int FTU_ScreenConfig_ID)
	{
		if (FTU_ScreenConfig_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_ScreenConfig_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_ScreenConfig_ID, Integer.valueOf(FTU_ScreenConfig_ID));
	}

	/** Get Screen Configuration.
		@return Screen Configuration	  */
	public int getFTU_ScreenConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_ScreenConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_ScreenConfig_UU.
		@param FTU_ScreenConfig_UU FTU_ScreenConfig_UU	  */
	public void setFTU_ScreenConfig_UU (String FTU_ScreenConfig_UU)
	{
		set_Value (COLUMNNAME_FTU_ScreenConfig_UU, FTU_ScreenConfig_UU);
	}

	/** Get FTU_ScreenConfig_UU.
		@return FTU_ScreenConfig_UU	  */
	public String getFTU_ScreenConfig_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_ScreenConfig_UU);
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

	/** Set StartCharacter.
		@param StartCharacter StartCharacter	  */
	public void setStartCharacter (String StartCharacter)
	{
		set_Value (COLUMNNAME_StartCharacter, StartCharacter);
	}

	/** Get StartCharacter.
		@return StartCharacter	  */
	public String getStartCharacter () 
	{
		return (String)get_Value(COLUMNNAME_StartCharacter);
	}

	/** Set StrLength.
		@param StrLength StrLength	  */
	public void setStrLength (int StrLength)
	{
		set_Value (COLUMNNAME_StrLength, Integer.valueOf(StrLength));
	}

	/** Get StrLength.
		@return StrLength	  */
	public int getStrLength () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_StrLength);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}