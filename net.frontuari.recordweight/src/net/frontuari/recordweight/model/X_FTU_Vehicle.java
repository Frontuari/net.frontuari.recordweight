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

/** Generated Model for FTU_Vehicle
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_Vehicle extends PO implements I_FTU_Vehicle, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200504L;

    /** Standard Constructor */
    public X_FTU_Vehicle (Properties ctx, int FTU_Vehicle_ID, String trxName)
    {
      super (ctx, FTU_Vehicle_ID, trxName);
      /** if (FTU_Vehicle_ID == 0)
        {
			setFTU_Vehicle_ID (0);
			setFTU_VehicleType_ID (0);
			setLoadCapacity (Env.ZERO);
			setName (null);
			setVehiclePlate (null);
			setVolumeCapacity (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_FTU_Vehicle (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_Vehicle[")
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

	/** Set Vehicle.
		@param FTU_Vehicle_ID Vehicle	  */
	public void setFTU_Vehicle_ID (int FTU_Vehicle_ID)
	{
		if (FTU_Vehicle_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Vehicle_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Vehicle_ID, Integer.valueOf(FTU_Vehicle_ID));
	}

	/** Get Vehicle.
		@return Vehicle	  */
	public int getFTU_Vehicle_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Vehicle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_VehicleModel getFTU_VehicleModel() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_VehicleModel)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_VehicleModel.Table_Name)
			.getPO(getFTU_VehicleModel_ID(), get_TrxName());	}

	/** Set Vehicle Model.
		@param FTU_VehicleModel_ID Vehicle Model	  */
	public void setFTU_VehicleModel_ID (int FTU_VehicleModel_ID)
	{
		if (FTU_VehicleModel_ID < 1) 
			set_Value (COLUMNNAME_FTU_VehicleModel_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_VehicleModel_ID, Integer.valueOf(FTU_VehicleModel_ID));
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

	/** Set FTU_Vehicle_UU.
		@param FTU_Vehicle_UU FTU_Vehicle_UU	  */
	public void setFTU_Vehicle_UU (String FTU_Vehicle_UU)
	{
		set_Value (COLUMNNAME_FTU_Vehicle_UU, FTU_Vehicle_UU);
	}

	/** Get FTU_Vehicle_UU.
		@return FTU_Vehicle_UU	  */
	public String getFTU_Vehicle_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_Vehicle_UU);
	}

	/** Set Owner.
		@param IsOwner Owner	  */
	public void setIsOwner (boolean IsOwner)
	{
		set_Value (COLUMNNAME_IsOwner, Boolean.valueOf(IsOwner));
	}

	/** Get Owner.
		@return Owner	  */
	public boolean isOwner () 
	{
		Object oo = get_Value(COLUMNNAME_IsOwner);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LoadCapacity.
		@param LoadCapacity LoadCapacity	  */
	public void setLoadCapacity (BigDecimal LoadCapacity)
	{
		set_Value (COLUMNNAME_LoadCapacity, LoadCapacity);
	}

	/** Get LoadCapacity.
		@return LoadCapacity	  */
	public BigDecimal getLoadCapacity () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LoadCapacity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MinLoadCapacity.
		@param MinLoadCapacity MinLoadCapacity	  */
	public void setMinLoadCapacity (BigDecimal MinLoadCapacity)
	{
		set_Value (COLUMNNAME_MinLoadCapacity, MinLoadCapacity);
	}

	/** Get MinLoadCapacity.
		@return MinLoadCapacity	  */
	public BigDecimal getMinLoadCapacity () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinLoadCapacity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MinVolumeCapacity.
		@param MinVolumeCapacity MinVolumeCapacity	  */
	public void setMinVolumeCapacity (BigDecimal MinVolumeCapacity)
	{
		set_Value (COLUMNNAME_MinVolumeCapacity, MinVolumeCapacity);
	}

	/** Get MinVolumeCapacity.
		@return MinVolumeCapacity	  */
	public BigDecimal getMinVolumeCapacity () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinVolumeCapacity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_M_Shipper getM_Shipper() throws RuntimeException
    {
		return (org.compiere.model.I_M_Shipper)MTable.get(getCtx(), org.compiere.model.I_M_Shipper.Table_Name)
			.getPO(getM_Shipper_ID(), get_TrxName());	}

	/** Set Shipper.
		@param M_Shipper_ID 
		Method or manner of product delivery
	  */
	public void setM_Shipper_ID (int M_Shipper_ID)
	{
		if (M_Shipper_ID < 1) 
			set_Value (COLUMNNAME_M_Shipper_ID, null);
		else 
			set_Value (COLUMNNAME_M_Shipper_ID, Integer.valueOf(M_Shipper_ID));
	}

	/** Get Shipper.
		@return Method or manner of product delivery
	  */
	public int getM_Shipper_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Shipper_ID);
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

	public org.compiere.model.I_C_ElementValue getUser1() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getUser1_ID(), get_TrxName());	}

	/** Set User Element List 1.
		@param User1_ID 
		User defined list element #1
	  */
	public void setUser1_ID (int User1_ID)
	{
		if (User1_ID < 1) 
			set_Value (COLUMNNAME_User1_ID, null);
		else 
			set_Value (COLUMNNAME_User1_ID, Integer.valueOf(User1_ID));
	}

	/** Get User Element List 1.
		@return User defined list element #1
	  */
	public int getUser1_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_User1_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Vehicle Plate.
		@param VehiclePlate Vehicle Plate	  */
	public void setVehiclePlate (String VehiclePlate)
	{
		set_Value (COLUMNNAME_VehiclePlate, VehiclePlate);
	}

	/** Get Vehicle Plate.
		@return Vehicle Plate	  */
	public String getVehiclePlate () 
	{
		return (String)get_Value(COLUMNNAME_VehiclePlate);
	}

	/** Container = C */
	public static final String VEHICLETYPE_Container = "C";
	/** Vehicle = V */
	public static final String VEHICLETYPE_Vehicle = "V";
	/** Set Vehicle Type.
		@param VehicleType Vehicle Type	  */
	public void setVehicleType (String VehicleType)
	{

		set_Value (COLUMNNAME_VehicleType, VehicleType);
	}

	/** Get Vehicle Type.
		@return Vehicle Type	  */
	public String getVehicleType () 
	{
		return (String)get_Value(COLUMNNAME_VehicleType);
	}

	/** Set VolumeCapacity.
		@param VolumeCapacity VolumeCapacity	  */
	public void setVolumeCapacity (BigDecimal VolumeCapacity)
	{
		set_Value (COLUMNNAME_VolumeCapacity, VolumeCapacity);
	}

	/** Get VolumeCapacity.
		@return VolumeCapacity	  */
	public BigDecimal getVolumeCapacity () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VolumeCapacity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}