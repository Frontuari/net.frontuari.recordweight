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

/** Generated Model for FTU_VehicleModel
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_VehicleModel extends PO implements I_FTU_VehicleModel, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200504L;

    /** Standard Constructor */
    public X_FTU_VehicleModel (Properties ctx, int FTU_VehicleModel_ID, String trxName)
    {
      super (ctx, FTU_VehicleModel_ID, trxName);
      /** if (FTU_VehicleModel_ID == 0)
        {
			setFTU_VehicleBrand_ID (0);
			setFTU_VehicleModel_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_VehicleModel (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_VehicleModel[")
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

	public net.frontuari.recordweight.model.I_FTU_VehicleBrand getFTU_VehicleBrand() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_VehicleBrand)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_VehicleBrand.Table_Name)
			.getPO(getFTU_VehicleBrand_ID(), get_TrxName());	}

	/** Set Vehicle Brand.
		@param FTU_VehicleBrand_ID Vehicle Brand	  */
	public void setFTU_VehicleBrand_ID (int FTU_VehicleBrand_ID)
	{
		if (FTU_VehicleBrand_ID < 1) 
			set_Value (COLUMNNAME_FTU_VehicleBrand_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_VehicleBrand_ID, Integer.valueOf(FTU_VehicleBrand_ID));
	}

	/** Get Vehicle Brand.
		@return Vehicle Brand	  */
	public int getFTU_VehicleBrand_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_VehicleBrand_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Vehicle Model.
		@param FTU_VehicleModel_ID Vehicle Model	  */
	public void setFTU_VehicleModel_ID (int FTU_VehicleModel_ID)
	{
		if (FTU_VehicleModel_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_VehicleModel_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_VehicleModel_ID, Integer.valueOf(FTU_VehicleModel_ID));
	}

	/** Get Vehicle Model.
		@return Vehicle Model	  */
	public int getFTU_VehicleModel_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_VehicleModel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_VehicleModel_UU.
		@param FTU_VehicleModel_UU FTU_VehicleModel_UU	  */
	public void setFTU_VehicleModel_UU (String FTU_VehicleModel_UU)
	{
		set_Value (COLUMNNAME_FTU_VehicleModel_UU, FTU_VehicleModel_UU);
	}

	/** Get FTU_VehicleModel_UU.
		@return FTU_VehicleModel_UU	  */
	public String getFTU_VehicleModel_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_VehicleModel_UU);
	}

	public net.frontuari.recordweight.model.I_FTU_VehicleType getFTU_VehicleType() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_VehicleType)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_VehicleType.Table_Name)
			.getPO(getFTU_VehicleType_ID(), get_TrxName());	}

	/** Set Vehicle Type.
		@param FTU_VehicleType_ID Vehicle Type	  */
	public void setFTU_VehicleType_ID (int FTU_VehicleType_ID)
	{
		if (FTU_VehicleType_ID < 1) 
			set_Value (COLUMNNAME_FTU_VehicleType_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_VehicleType_ID, Integer.valueOf(FTU_VehicleType_ID));
	}

	/** Get Vehicle Type.
		@return Vehicle Type	  */
	public int getFTU_VehicleType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_VehicleType_ID);
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
}