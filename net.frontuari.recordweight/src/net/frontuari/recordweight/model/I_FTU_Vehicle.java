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
package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for FTU_Vehicle
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_FTU_Vehicle 
{

    /** TableName=FTU_Vehicle */
    public static final String Table_Name = "FTU_Vehicle";

    /** AD_Table_ID=1000070 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name FTU_VehicleBrand_ID */
    public static final String COLUMNNAME_FTU_VehicleBrand_ID = "FTU_VehicleBrand_ID";

	/** Set Vehicle Brand	  */
	public void setFTU_VehicleBrand_ID (int FTU_VehicleBrand_ID);

	/** Get Vehicle Brand	  */
	public int getFTU_VehicleBrand_ID();

	public net.frontuari.recordweight.model.I_FTU_VehicleBrand getFTU_VehicleBrand() throws RuntimeException;

    /** Column name FTU_Vehicle_ID */
    public static final String COLUMNNAME_FTU_Vehicle_ID = "FTU_Vehicle_ID";

	/** Set Vehicle	  */
	public void setFTU_Vehicle_ID (int FTU_Vehicle_ID);

	/** Get Vehicle	  */
	public int getFTU_Vehicle_ID();

    /** Column name FTU_VehicleModel_ID */
    public static final String COLUMNNAME_FTU_VehicleModel_ID = "FTU_VehicleModel_ID";

	/** Set Vehicle Model	  */
	public void setFTU_VehicleModel_ID (int FTU_VehicleModel_ID);

	/** Get Vehicle Model	  */
	public int getFTU_VehicleModel_ID();

	public net.frontuari.recordweight.model.I_FTU_VehicleModel getFTU_VehicleModel() throws RuntimeException;

    /** Column name FTU_VehicleType_ID */
    public static final String COLUMNNAME_FTU_VehicleType_ID = "FTU_VehicleType_ID";

	/** Set Vehicle Type	  */
	public void setFTU_VehicleType_ID (int FTU_VehicleType_ID);

	/** Get Vehicle Type	  */
	public int getFTU_VehicleType_ID();

	public net.frontuari.recordweight.model.I_FTU_VehicleType getFTU_VehicleType() throws RuntimeException;

    /** Column name FTU_Vehicle_UU */
    public static final String COLUMNNAME_FTU_Vehicle_UU = "FTU_Vehicle_UU";

	/** Set FTU_Vehicle_UU	  */
	public void setFTU_Vehicle_UU (String FTU_Vehicle_UU);

	/** Get FTU_Vehicle_UU	  */
	public String getFTU_Vehicle_UU();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsOwner */
    public static final String COLUMNNAME_IsOwner = "IsOwner";

	/** Set Owner	  */
	public void setIsOwner (boolean IsOwner);

	/** Get Owner	  */
	public boolean isOwner();

    /** Column name LoadCapacity */
    public static final String COLUMNNAME_LoadCapacity = "LoadCapacity";

	/** Set LoadCapacity	  */
	public void setLoadCapacity (BigDecimal LoadCapacity);

	/** Get LoadCapacity	  */
	public BigDecimal getLoadCapacity();

    /** Column name MinLoadCapacity */
    public static final String COLUMNNAME_MinLoadCapacity = "MinLoadCapacity";

	/** Set MinLoadCapacity	  */
	public void setMinLoadCapacity (BigDecimal MinLoadCapacity);

	/** Get MinLoadCapacity	  */
	public BigDecimal getMinLoadCapacity();

    /** Column name MinVolumeCapacity */
    public static final String COLUMNNAME_MinVolumeCapacity = "MinVolumeCapacity";

	/** Set MinVolumeCapacity	  */
	public void setMinVolumeCapacity (BigDecimal MinVolumeCapacity);

	/** Get MinVolumeCapacity	  */
	public BigDecimal getMinVolumeCapacity();

    /** Column name M_Shipper_ID */
    public static final String COLUMNNAME_M_Shipper_ID = "M_Shipper_ID";

	/** Set Shipper.
	  * Method or manner of product delivery
	  */
	public void setM_Shipper_ID (int M_Shipper_ID);

	/** Get Shipper.
	  * Method or manner of product delivery
	  */
	public int getM_Shipper_ID();

	public org.compiere.model.I_M_Shipper getM_Shipper() throws RuntimeException;

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name User1_ID */
    public static final String COLUMNNAME_User1_ID = "User1_ID";

	/** Set User Element List 1.
	  * User defined list element #1
	  */
	public void setUser1_ID (int User1_ID);

	/** Get User Element List 1.
	  * User defined list element #1
	  */
	public int getUser1_ID();

	public org.compiere.model.I_C_ElementValue getUser1() throws RuntimeException;

    /** Column name VehiclePlate */
    public static final String COLUMNNAME_VehiclePlate = "VehiclePlate";

	/** Set Vehicle Plate	  */
	public void setVehiclePlate (String VehiclePlate);

	/** Get Vehicle Plate	  */
	public String getVehiclePlate();

    /** Column name VolumeCapacity */
    public static final String COLUMNNAME_VolumeCapacity = "VolumeCapacity";

	/** Set VolumeCapacity	  */
	public void setVolumeCapacity (BigDecimal VolumeCapacity);

	/** Get VolumeCapacity	  */
	public BigDecimal getVolumeCapacity();
}
