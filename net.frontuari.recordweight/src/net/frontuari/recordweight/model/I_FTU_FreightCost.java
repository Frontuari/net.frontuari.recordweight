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

/** Generated Interface for FTU_FreightCost
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_FTU_FreightCost 
{

    /** TableName=FTU_FreightCost */
    public static final String Table_Name = "FTU_FreightCost";

    /** AD_Table_ID=1000493 */
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

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_SalesRegion_ID */
    public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";

	/** Set Sales Region.
	  * Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID);

	/** Get Sales Region.
	  * Sales coverage region
	  */
	public int getC_SalesRegion_ID();

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException;

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

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

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

    /** Column name DocAction */
    public static final String COLUMNNAME_DocAction = "DocAction";

	/** Set Document Action.
	  * The targeted status of the document
	  */
	public void setDocAction (String DocAction);

	/** Get Document Action.
	  * The targeted status of the document
	  */
	public String getDocAction();

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name FTU_AdjustPrice */
    public static final String COLUMNNAME_FTU_AdjustPrice = "FTU_AdjustPrice";

	/** Set Adjust Price	  */
	public void setFTU_AdjustPrice (boolean FTU_AdjustPrice);

	/** Get Adjust Price	  */
	public boolean isFTU_AdjustPrice();

    /** Column name FTU_Driver_ID */
    public static final String COLUMNNAME_FTU_Driver_ID = "FTU_Driver_ID";

	/** Set Driver	  */
	public void setFTU_Driver_ID (int FTU_Driver_ID);

	/** Get Driver	  */
	public int getFTU_Driver_ID();

	public net.frontuari.recordweight.model.I_FTU_Driver getFTU_Driver() throws RuntimeException;

    /** Column name FTU_EntryTicket_ID */
    public static final String COLUMNNAME_FTU_EntryTicket_ID = "FTU_EntryTicket_ID";

	/** Set Entry Ticket	  */
	public void setFTU_EntryTicket_ID (int FTU_EntryTicket_ID);

	/** Get Entry Ticket	  */
	public int getFTU_EntryTicket_ID();

	public net.frontuari.recordweight.model.I_FTU_EntryTicket getFTU_EntryTicket() throws RuntimeException;

    /** Column name FTU_FreightCost_ID */
    public static final String COLUMNNAME_FTU_FreightCost_ID = "FTU_FreightCost_ID";

	/** Set Bill of Lading	  */
	public void setFTU_FreightCost_ID (int FTU_FreightCost_ID);

	/** Get Bill of Lading	  */
	public int getFTU_FreightCost_ID();

    /** Column name FTU_FreightCost_UU */
    public static final String COLUMNNAME_FTU_FreightCost_UU = "FTU_FreightCost_UU";

	/** Set Freight Cost UU	  */
	public void setFTU_FreightCost_UU (String FTU_FreightCost_UU);

	/** Get Freight Cost UU	  */
	public String getFTU_FreightCost_UU();

    /** Column name FTU_LoadOrder_ID */
    public static final String COLUMNNAME_FTU_LoadOrder_ID = "FTU_LoadOrder_ID";

	/** Set Load Order	  */
	public void setFTU_LoadOrder_ID (int FTU_LoadOrder_ID);

	/** Get Load Order	  */
	public int getFTU_LoadOrder_ID();

    /** Column name FTU_PriceForTrip_ID */
    public static final String COLUMNNAME_FTU_PriceForTrip_ID = "FTU_PriceForTrip_ID";

	/** Set Price For Trip	  */
	public void setFTU_PriceForTrip_ID (int FTU_PriceForTrip_ID);

	/** Get Price For Trip	  */
	public int getFTU_PriceForTrip_ID();

	public net.frontuari.recordweight.model.I_FTU_PriceForTrip getFTU_PriceForTrip() throws RuntimeException;

    /** Column name FTU_Vehicle_ID */
    public static final String COLUMNNAME_FTU_Vehicle_ID = "FTU_Vehicle_ID";

	/** Set Vehicle	  */
	public void setFTU_Vehicle_ID (int FTU_Vehicle_ID);

	/** Get Vehicle	  */
	public int getFTU_Vehicle_ID();

	public net.frontuari.recordweight.model.I_FTU_Vehicle getFTU_Vehicle() throws RuntimeException;

    /** Column name FTU_ZeroCost */
    public static final String COLUMNNAME_FTU_ZeroCost = "FTU_ZeroCost";

	/** Set Zero Cost	  */
	public void setFTU_ZeroCost (boolean FTU_ZeroCost);

	/** Get Zero Cost	  */
	public boolean isFTU_ZeroCost();

    /** Column name GrandTotal */
    public static final String COLUMNNAME_GrandTotal = "GrandTotal";

	/** Set Grand Total.
	  * Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal);

	/** Get Grand Total.
	  * Total amount of document
	  */
	public BigDecimal getGrandTotal();

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

    /** Column name ProcCreate */
    public static final String COLUMNNAME_ProcCreate = "ProcCreate";

	/** Set Create	  */
	public void setProcCreate (String ProcCreate);

	/** Get Create	  */
	public String getProcCreate();

    /** Column name SealNo */
    public static final String COLUMNNAME_SealNo = "SealNo";

	/** Set SealNo	  */
	public void setSealNo (String SealNo);

	/** Get SealNo	  */
	public String getSealNo();

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

    /** Column name Weight */
    public static final String COLUMNNAME_Weight = "Weight";

	/** Set Weight.
	  * Weight of a product
	  */
	public void setWeight (BigDecimal Weight);

	/** Get Weight.
	  * Weight of a product
	  */
	public BigDecimal getWeight();
}
