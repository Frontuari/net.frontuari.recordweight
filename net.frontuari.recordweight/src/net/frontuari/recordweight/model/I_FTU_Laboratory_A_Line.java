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

/** Generated Interface for FTU_Laboratory_A_Line
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_FTU_Laboratory_A_Line 
{

    /** TableName=FTU_Laboratory_A_Line */
    public static final String Table_Name = "FTU_Laboratory_A_Line";

    /** AD_Table_ID=1000082 */
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

    /** Column name Code */
    public static final String COLUMNNAME_Code = "Code";

	/** Set Validation code.
	  * Validation Code
	  */
	public void setCode (String Code);

	/** Get Validation code.
	  * Validation Code
	  */
	public String getCode();

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

    /** Column name FTU_Analysis_Type_ID */
    public static final String COLUMNNAME_FTU_Analysis_Type_ID = "FTU_Analysis_Type_ID";

	/** Set Analysis_Type_ID	  */
	public void setFTU_Analysis_Type_ID (int FTU_Analysis_Type_ID);

	/** Get Analysis_Type_ID	  */
	public int getFTU_Analysis_Type_ID();

	public net.frontuari.recordweight.model.I_FTU_Analysis_Type getFTU_Analysis_Type() throws RuntimeException;

    /** Column name FTU_Laboratory_A_Line_ID */
    public static final String COLUMNNAME_FTU_Laboratory_A_Line_ID = "FTU_Laboratory_A_Line_ID";

	/** Set FTU_Laboratory_A_Line	  */
	public void setFTU_Laboratory_A_Line_ID (int FTU_Laboratory_A_Line_ID);

	/** Get FTU_Laboratory_A_Line	  */
	public int getFTU_Laboratory_A_Line_ID();

    /** Column name FTU_Laboratory_A_Line_UU */
    public static final String COLUMNNAME_FTU_Laboratory_A_Line_UU = "FTU_Laboratory_A_Line_UU";

	/** Set FTU_Laboratory_A_Line_UU	  */
	public void setFTU_Laboratory_A_Line_UU (String FTU_Laboratory_A_Line_UU);

	/** Get FTU_Laboratory_A_Line_UU	  */
	public String getFTU_Laboratory_A_Line_UU();

    /** Column name FTU_Laboratory_Analysis_ID */
    public static final String COLUMNNAME_FTU_Laboratory_Analysis_ID = "FTU_Laboratory_Analysis_ID";

	/** Set FTU_Laboratory_Analysis	  */
	public void setFTU_Laboratory_Analysis_ID (int FTU_Laboratory_Analysis_ID);

	/** Get FTU_Laboratory_Analysis	  */
	public int getFTU_Laboratory_Analysis_ID();

	public net.frontuari.recordweight.model.I_FTU_Laboratory_Analysis getFTU_Laboratory_Analysis() throws RuntimeException;

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

    /** Column name Result */
    public static final String COLUMNNAME_Result = "Result";

	/** Set Result.
	  * Result of the action taken
	  */
	public void setResult (BigDecimal Result);

	/** Get Result.
	  * Result of the action taken
	  */
	public BigDecimal getResult();

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
