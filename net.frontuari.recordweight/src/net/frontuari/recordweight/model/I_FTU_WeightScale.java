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

/** Generated Interface for FTU_WeightScale
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_FTU_WeightScale 
{

    /** TableName=FTU_WeightScale */
    public static final String Table_Name = "FTU_WeightScale";

    /** AD_Table_ID=1000063 */
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

    /** Column name Bauds */
    public static final String COLUMNNAME_Bauds = "Bauds";

	/** Set Bauds	  */
	public void setBauds (String Bauds);

	/** Get Bauds	  */
	public String getBauds();

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

    /** Column name C_UOM_ID */
    public static final String COLUMNNAME_C_UOM_ID = "C_UOM_ID";

	/** Set UOM.
	  * Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID);

	/** Get UOM.
	  * Unit of Measure
	  */
	public int getC_UOM_ID();

	public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException;

    /** Column name DataBits */
    public static final String COLUMNNAME_DataBits = "DataBits";

	/** Set DataBits	  */
	public void setDataBits (String DataBits);

	/** Get DataBits	  */
	public String getDataBits();

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

    /** Column name FlowControl */
    public static final String COLUMNNAME_FlowControl = "FlowControl";

	/** Set FlowControl	  */
	public void setFlowControl (String FlowControl);

	/** Get FlowControl	  */
	public String getFlowControl();

    /** Column name FTU_WeightScale_ID */
    public static final String COLUMNNAME_FTU_WeightScale_ID = "FTU_WeightScale_ID";

	/** Set Weight Scale	  */
	public void setFTU_WeightScale_ID (int FTU_WeightScale_ID);

	/** Get Weight Scale	  */
	public int getFTU_WeightScale_ID();

    /** Column name FTU_WeightScale_UU */
    public static final String COLUMNNAME_FTU_WeightScale_UU = "FTU_WeightScale_UU";

	/** Set FTU_WeightScale_UU	  */
	public void setFTU_WeightScale_UU (String FTU_WeightScale_UU);

	/** Get FTU_WeightScale_UU	  */
	public String getFTU_WeightScale_UU();

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

    /** Column name Parity */
    public static final String COLUMNNAME_Parity = "Parity";

	/** Set Parity	  */
	public void setParity (String Parity);

	/** Get Parity	  */
	public String getParity();

    /** Column name Regex */
    public static final String COLUMNNAME_Regex = "Regex";

	/** Set Regex	  */
	public void setRegex (String Regex);

	/** Get Regex	  */
	public String getRegex();

    /** Column name SerialPort */
    public static final String COLUMNNAME_SerialPort = "SerialPort";

	/** Set SerialPort	  */
	public void setSerialPort (String SerialPort);

	/** Get SerialPort	  */
	public String getSerialPort();

    /** Column name StopBits */
    public static final String COLUMNNAME_StopBits = "StopBits";

	/** Set StopBits	  */
	public void setStopBits (String StopBits);

	/** Get StopBits	  */
	public String getStopBits();

    /** Column name StrLength */
    public static final String COLUMNNAME_StrLength = "StrLength";

	/** Set StrLength	  */
	public void setStrLength (int StrLength);

	/** Get StrLength	  */
	public int getStrLength();

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
	
	/** Column name FTU_ScreenConfig_ID */
    public static final String COLUMNNAME_FTU_ScreenConfig_ID = "FTU_ScreenConfig_ID";

	/** Set Screen Configuration	  */
	public void setFTU_ScreenConfig_ID (int FTU_ScreenConfig_ID);

	/** Get Screen Configuration	  */
	public int getFTU_ScreenConfig_ID();

	public net.frontuari.recordweight.model.I_FTU_ScreenConfig getFTU_ScreenConfig() throws RuntimeException;
	
	/** Column name FTU_SerialPortConfig_ID */
    public static final String COLUMNNAME_FTU_SerialPortConfig_ID = "FTU_SerialPortConfig_ID";

	/** Set Serial Port Configuration.
	  * Serial Port Configuration example: COM1 or tty01, 9600...
	  */
	public void setFTU_SerialPortConfig_ID (int FTU_SerialPortConfig_ID);

	/** Get Serial Port Configuration.
	  * Serial Port Configuration example: COM1 or tty01, 9600...
	  */
	public int getFTU_SerialPortConfig_ID();

	public net.frontuari.recordweight.model.I_FTU_SerialPortConfig getFTU_SerialPortConfig() throws RuntimeException;
}
