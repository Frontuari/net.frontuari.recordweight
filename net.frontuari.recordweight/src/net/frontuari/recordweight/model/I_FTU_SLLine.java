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

/** Generated Interface for FTU_SLLine
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_FTU_SLLine 
{

    /** TableName=FTU_SLLine */
    public static final String Table_Name = "FTU_SLLine";

    /** AD_Table_ID=1000933 */
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

    /** Column name Amount */
    public static final String COLUMNNAME_Amount = "Amount";

	/** Set Amount.
	  * Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount);

	/** Get Amount.
	  * Amount in a defined currency
	  */
	public BigDecimal getAmount();

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException;

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

    /** Column name DeductionType */
    public static final String COLUMNNAME_DeductionType = "DeductionType";

	/** Set Deduction Type	  */
	public void setDeductionType (String DeductionType);

	/** Get Deduction Type	  */
	public String getDeductionType();

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

    /** Column name FTU_FreightCost_ID */
    public static final String COLUMNNAME_FTU_FreightCost_ID = "FTU_FreightCost_ID";

	/** Set Bill of Lading	  */
	public void setFTU_FreightCost_ID (int FTU_FreightCost_ID);

	/** Get Bill of Lading	  */
	public int getFTU_FreightCost_ID();

	public net.frontuari.recordweight.model.I_FTU_FreightCost getFTU_FreightCost() throws RuntimeException;

    /** Column name FTU_ShipperLiquidation_ID */
    public static final String COLUMNNAME_FTU_ShipperLiquidation_ID = "FTU_ShipperLiquidation_ID";

	/** Set Shipper Liquidation.
	  * Shipper Liquidation
	  */
	public void setFTU_ShipperLiquidation_ID (int FTU_ShipperLiquidation_ID);

	/** Get Shipper Liquidation.
	  * Shipper Liquidation
	  */
	public int getFTU_ShipperLiquidation_ID();

	public net.frontuari.recordweight.model.I_FTU_ShipperLiquidation getFTU_ShipperLiquidation() throws RuntimeException;

    /** Column name FTU_SLLine_ID */
    public static final String COLUMNNAME_FTU_SLLine_ID = "FTU_SLLine_ID";

	/** Set Shipper Liquidation Line.
	  * Shipper Liquidation Line
	  */
	public void setFTU_SLLine_ID (int FTU_SLLine_ID);

	/** Get Shipper Liquidation Line.
	  * Shipper Liquidation Line
	  */
	public int getFTU_SLLine_ID();

    /** Column name FTU_SLLine_UU */
    public static final String COLUMNNAME_FTU_SLLine_UU = "FTU_SLLine_UU";

	/** Set FTU_SL_Line_UU	  */
	public void setFTU_SLLine_UU (String FTU_SLLine_UU);

	/** Get FTU_SL_Line_UU	  */
	public String getFTU_SLLine_UU();

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
