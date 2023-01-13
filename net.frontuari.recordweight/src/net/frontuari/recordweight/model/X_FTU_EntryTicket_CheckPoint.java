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

/** Generated Model for FTU_EntryTicket_CheckPoint
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_EntryTicket_CheckPoint extends PO implements I_FTU_EntryTicket_CheckPoint, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200518L;

    /** Standard Constructor */
    public X_FTU_EntryTicket_CheckPoint (Properties ctx, int FTU_EntryTicket_CheckPoint_ID, String trxName)
    {
      super (ctx, FTU_EntryTicket_CheckPoint_ID, trxName);
      /** if (FTU_EntryTicket_CheckPoint_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_FTU_EntryTicket_CheckPoint (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_EntryTicket_CheckPoint[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set FTU CheckPoint.
		@param FTU_CheckPoint_ID FTU CheckPoint	  */
	public void setFTU_CheckPoint_ID (int FTU_CheckPoint_ID)
	{
		if (FTU_CheckPoint_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPoint_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPoint_ID, Integer.valueOf(FTU_CheckPoint_ID));
	}

	/** Get FTU CheckPoint.
		@return FTU CheckPoint	  */
	public int getFTU_CheckPoint_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_CheckPoint_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU EntryTicket CheckPoint.
		@param FTU_EntryTicket_CheckPoint_ID FTU EntryTicket CheckPoint	  */
	public void setFTU_EntryTicket_CheckPoint_ID (int FTU_EntryTicket_CheckPoint_ID)
	{
		if (FTU_EntryTicket_CheckPoint_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_CheckPoint_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_CheckPoint_ID, Integer.valueOf(FTU_EntryTicket_CheckPoint_ID));
	}

	/** Get FTU EntryTicket CheckPoint.
		@return FTU EntryTicket CheckPoint	  */
	public int getFTU_EntryTicket_CheckPoint_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_EntryTicket_CheckPoint_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_EntryTicket_CheckPoint_UU.
		@param FTU_EntryTicket_CheckPoint_UU FTU_EntryTicket_CheckPoint_UU	  */
	public void setFTU_EntryTicket_CheckPoint_UU (String FTU_EntryTicket_CheckPoint_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_CheckPoint_UU, FTU_EntryTicket_CheckPoint_UU);
	}

	/** Get FTU_EntryTicket_CheckPoint_UU.
		@return FTU_EntryTicket_CheckPoint_UU	  */
	public String getFTU_EntryTicket_CheckPoint_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_EntryTicket_CheckPoint_UU);
	}

	/** Set Entry Ticket.
		@param FTU_EntryTicket_ID Entry Ticket	  */
	public void setFTU_EntryTicket_ID (int FTU_EntryTicket_ID)
	{
		if (FTU_EntryTicket_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, Integer.valueOf(FTU_EntryTicket_ID));
	}

	/** Get Entry Ticket.
		@return Entry Ticket	  */
	public int getFTU_EntryTicket_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_EntryTicket_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Motive ID.
		@param FTU_Motive_ID Motive ID	  */
	public void setFTU_Motive_ID (int FTU_Motive_ID)
	{
		if (FTU_Motive_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Motive_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Motive_ID, Integer.valueOf(FTU_Motive_ID));
	}

	/** Get Motive ID.
		@return Motive ID	  */
	public int getFTU_Motive_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Motive_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Line.
		@param LineNo 
		Line No
	  */
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

}