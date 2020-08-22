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

/** Generated Model for FTU_WS_Warehouse
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_WS_Warehouse extends PO implements I_FTU_WS_Warehouse, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200504L;

    /** Standard Constructor */
    public X_FTU_WS_Warehouse (Properties ctx, int FTU_WS_Warehouse_ID, String trxName)
    {
      super (ctx, FTU_WS_Warehouse_ID, trxName);
      /** if (FTU_WS_Warehouse_ID == 0)
        {
			setFTU_WeightScale_ID (0);
			setFTU_WS_Warehouse_ID (0);
			setM_Warehouse_ID (0);
        } */
    }

    /** Load Constructor */
    public X_FTU_WS_Warehouse (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_WS_Warehouse[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public net.frontuari.recordweight.model.I_FTU_WeightScale getFTU_WeightScale() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_WeightScale)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_WeightScale.Table_Name)
			.getPO(getFTU_WeightScale_ID(), get_TrxName());	}

	/** Set Weight Scale.
		@param FTU_WeightScale_ID Weight Scale	  */
	public void setFTU_WeightScale_ID (int FTU_WeightScale_ID)
	{
		if (FTU_WeightScale_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_ID, Integer.valueOf(FTU_WeightScale_ID));
	}

	/** Get Weight Scale.
		@return Weight Scale	  */
	public int getFTU_WeightScale_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WeightScale_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_WS_Warehouse.
		@param FTU_WS_Warehouse_ID FTU_WS_Warehouse	  */
	public void setFTU_WS_Warehouse_ID (int FTU_WS_Warehouse_ID)
	{
		if (FTU_WS_Warehouse_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_WS_Warehouse_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_WS_Warehouse_ID, Integer.valueOf(FTU_WS_Warehouse_ID));
	}

	/** Get FTU_WS_Warehouse.
		@return FTU_WS_Warehouse	  */
	public int getFTU_WS_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WS_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_WS_Warehouse_UU.
		@param FTU_WS_Warehouse_UU FTU_WS_Warehouse_UU	  */
	public void setFTU_WS_Warehouse_UU (String FTU_WS_Warehouse_UU)
	{
		set_Value (COLUMNNAME_FTU_WS_Warehouse_UU, FTU_WS_Warehouse_UU);
	}

	/** Get FTU_WS_Warehouse_UU.
		@return FTU_WS_Warehouse_UU	  */
	public String getFTU_WS_Warehouse_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_WS_Warehouse_UU);
	}

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException
    {
		return (org.compiere.model.I_M_Warehouse)MTable.get(getCtx(), org.compiere.model.I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_ID(), get_TrxName());	}

	/** Set Warehouse.
		@param M_Warehouse_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}