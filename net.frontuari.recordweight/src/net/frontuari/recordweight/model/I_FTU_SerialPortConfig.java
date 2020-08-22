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

/** Generated Interface for FTU_SerialPortConfig
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_FTU_SerialPortConfig 
{

    /** TableName=FTU_SerialPortConfig */
    public static final String Table_Name = "FTU_SerialPortConfig";

    /** AD_Table_ID=1000026 */
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

    /** Column name DataBits */
    public static final String COLUMNNAME_DataBits = "DataBits";

	/** Set DataBits	  */
	public void setDataBits (String DataBits);

	/** Get DataBits	  */
	public String getDataBits();

    /** Column name FlowControl */
    public static final String COLUMNNAME_FlowControl = "FlowControl";

	/** Set FlowControl	  */
	public void setFlowControl (String FlowControl);

	/** Get FlowControl	  */
	public String getFlowControl();

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

    /** Column name FTU_SerialPortConfig_UU */
    public static final String COLUMNNAME_FTU_SerialPortConfig_UU = "FTU_SerialPortConfig_UU";

	/** Set FTU_SerialPortConfig_UU	  */
	public void setFTU_SerialPortConfig_UU (String FTU_SerialPortConfig_UU);

	/** Get FTU_SerialPortConfig_UU	  */
	public String getFTU_SerialPortConfig_UU();

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
}
