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

/** Generated Interface for FTU_ScreenConfig
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_FTU_ScreenConfig 
{

    /** TableName=FTU_ScreenConfig */
    public static final String Table_Name = "FTU_ScreenConfig";

    /** AD_Table_ID=1000500 */
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

    /** Column name CutEnd */
    public static final String COLUMNNAME_CutEnd = "CutEnd";

	/** Set CutEnd.
	  * CutEnd
	  */
	public void setCutEnd (int CutEnd);

	/** Get CutEnd.
	  * CutEnd
	  */
	public int getCutEnd();

    /** Column name CutStart */
    public static final String COLUMNNAME_CutStart = "CutStart";

	/** Set CutStart.
	  * CutStart
	  */
	public void setCutStart (int CutStart);

	/** Get CutStart.
	  * CutStart
	  */
	public int getCutStart();

    /** Column name EndCharacter */
    public static final String COLUMNNAME_EndCharacter = "EndCharacter";

	/** Set EndCharacter	  */
	public void setEndCharacter (String EndCharacter);

	/** Get EndCharacter	  */
	public String getEndCharacter();

    /** Column name FTU_ScreenConfig_ID */
    public static final String COLUMNNAME_FTU_ScreenConfig_ID = "FTU_ScreenConfig_ID";

	/** Set Screen Configuration	  */
	public void setFTU_ScreenConfig_ID (int FTU_ScreenConfig_ID);

	/** Get Screen Configuration	  */
	public int getFTU_ScreenConfig_ID();

    /** Column name FTU_ScreenConfig_UU */
    public static final String COLUMNNAME_FTU_ScreenConfig_UU = "FTU_ScreenConfig_UU";

	/** Set FTU_ScreenConfig_UU	  */
	public void setFTU_ScreenConfig_UU (String FTU_ScreenConfig_UU);

	/** Get FTU_ScreenConfig_UU	  */
	public String getFTU_ScreenConfig_UU();

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

    /** Column name IsTest */
    public static final String COLUMNNAME_IsTest = "IsTest";

	/** Set Test.
	  * Execute in Test Mode
	  */
	public void setIsTest (boolean IsTest);

	/** Get Test.
	  * Execute in Test Mode
	  */
	public boolean isTest();

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

    /** Column name QtyDecimal */
    public static final String COLUMNNAME_QtyDecimal = "QtyDecimal";

	/** Set Qty Decimal	  */
	public void setQtyDecimal (BigDecimal QtyDecimal);

	/** Get Qty Decimal	  */
	public BigDecimal getQtyDecimal();

    /** Column name StartCharacter */
    public static final String COLUMNNAME_StartCharacter = "StartCharacter";

	/** Set StartCharacter	  */
	public void setStartCharacter (String StartCharacter);

	/** Get StartCharacter	  */
	public String getStartCharacter();

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
}
