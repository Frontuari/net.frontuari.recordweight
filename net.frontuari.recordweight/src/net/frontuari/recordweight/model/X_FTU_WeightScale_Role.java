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

/** Generated Model for FTU_WeightScale_Role
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_WeightScale_Role")
public class X_FTU_WeightScale_Role extends PO implements I_FTU_WeightScale_Role, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230725L;

    /** Standard Constructor */
    public X_FTU_WeightScale_Role (Properties ctx, int FTU_WeightScale_Role_ID, String trxName)
    {
      super (ctx, FTU_WeightScale_Role_ID, trxName);
      /** if (FTU_WeightScale_Role_ID == 0)
        {
			setAD_Role_ID (0);
			setFTU_WeightScale_ID (0);
			setFTU_WeightScale_Role_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_FTU_WeightScale_Role (Properties ctx, int FTU_WeightScale_Role_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_WeightScale_Role_ID, trxName, virtualColumns);
      /** if (FTU_WeightScale_Role_ID == 0)
        {
			setAD_Role_ID (0);
			setFTU_WeightScale_ID (0);
			setFTU_WeightScale_Role_ID (0);
        } */
    }

    /** Load Constructor */
    public X_FTU_WeightScale_Role (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_WeightScale_Role[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_ID)
			.getPO(getAD_Role_ID(), get_TrxName());
	}

	/** Set Role.
		@param AD_Role_ID Responsibility Role
	*/
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0)
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_WeightScale getFTU_WeightScale() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_WeightScale)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_WeightScale.Table_ID)
			.getPO(getFTU_WeightScale_ID(), get_TrxName());
	}

	/** Set Weight Scale.
		@param FTU_WeightScale_ID Weight Scale
	*/
	public void setFTU_WeightScale_ID (int FTU_WeightScale_ID)
	{
		if (FTU_WeightScale_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_ID, Integer.valueOf(FTU_WeightScale_ID));
	}

	/** Get Weight Scale.
		@return Weight Scale	  */
	public int getFTU_WeightScale_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WeightScale_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_WeightScale_Role.
		@param FTU_WeightScale_Role_ID FTU_WeightScale_Role
	*/
	public void setFTU_WeightScale_Role_ID (int FTU_WeightScale_Role_ID)
	{
		if (FTU_WeightScale_Role_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_Role_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_Role_ID, Integer.valueOf(FTU_WeightScale_Role_ID));
	}

	/** Get FTU_WeightScale_Role.
		@return FTU_WeightScale_Role	  */
	public int getFTU_WeightScale_Role_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WeightScale_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_WeightScale_Role_UU.
		@param FTU_WeightScale_Role_UU FTU_WeightScale_Role_UU
	*/
	public void setFTU_WeightScale_Role_UU (String FTU_WeightScale_Role_UU)
	{
		set_Value (COLUMNNAME_FTU_WeightScale_Role_UU, FTU_WeightScale_Role_UU);
	}

	/** Get FTU_WeightScale_Role_UU.
		@return FTU_WeightScale_Role_UU	  */
	public String getFTU_WeightScale_Role_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_WeightScale_Role_UU);
	}
}