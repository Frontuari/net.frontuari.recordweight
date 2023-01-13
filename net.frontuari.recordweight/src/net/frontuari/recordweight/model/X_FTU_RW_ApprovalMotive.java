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

/** Generated Model for FTU_RW_ApprovalMotive
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_RW_ApprovalMotive extends PO implements I_FTU_RW_ApprovalMotive, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220803L;

    /** Standard Constructor */
    public X_FTU_RW_ApprovalMotive (Properties ctx, int FTU_RW_ApprovalMotive_ID, String trxName)
    {
      super (ctx, FTU_RW_ApprovalMotive_ID, trxName);
      /** if (FTU_RW_ApprovalMotive_ID == 0)
        {
			setFTU_RW_ApprovalMotive_ID (0);
			setIsApproved (false);
        } */
    }

    /** Load Constructor */
    public X_FTU_RW_ApprovalMotive (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_RW_ApprovalMotive[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Approval Motive (Weight).
		@param FTU_RW_ApprovalMotive_ID Approval Motive (Weight)	  */
	public void setFTU_RW_ApprovalMotive_ID (int FTU_RW_ApprovalMotive_ID)
	{
		if (FTU_RW_ApprovalMotive_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_RW_ApprovalMotive_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_RW_ApprovalMotive_ID, Integer.valueOf(FTU_RW_ApprovalMotive_ID));
	}

	/** Get Approval Motive (Weight).
		@return Approval Motive (Weight)	  */
	public int getFTU_RW_ApprovalMotive_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_RW_ApprovalMotive_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_RW_ApprovalMotive_UU.
		@param FTU_RW_ApprovalMotive_UU FTU_RW_ApprovalMotive_UU	  */
	public void setFTU_RW_ApprovalMotive_UU (String FTU_RW_ApprovalMotive_UU)
	{
		set_Value (COLUMNNAME_FTU_RW_ApprovalMotive_UU, FTU_RW_ApprovalMotive_UU);
	}

	/** Get FTU_RW_ApprovalMotive_UU.
		@return FTU_RW_ApprovalMotive_UU	  */
	public String getFTU_RW_ApprovalMotive_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_RW_ApprovalMotive_UU);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_ValueNoCheck (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
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
}