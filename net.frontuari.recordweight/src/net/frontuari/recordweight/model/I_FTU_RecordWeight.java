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

/** Generated Interface for FTU_RecordWeight
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_FTU_RecordWeight 
{

    /** TableName=FTU_RecordWeight */
    public static final String Table_Name = "FTU_RecordWeight";

    /** AD_Table_ID=1000039 */
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

    /** Column name CreateFrom */
    public static final String COLUMNNAME_CreateFrom = "CreateFrom";

	/** Set Create lines from.
	  * Process which will generate a new document lines based on an existing document
	  */
	public void setCreateFrom (String CreateFrom);

	/** Get Create lines from.
	  * Process which will generate a new document lines based on an existing document
	  */
	public String getCreateFrom();

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

    /** Column name DocumentPrint */
    public static final String COLUMNNAME_DocumentPrint = "DocumentPrint";

	/** Set Document Print	  */
	public void setDocumentPrint (String DocumentPrint);

	/** Get Document Print	  */
	public String getDocumentPrint();

    /** Column name FTU_Chute_ID */
    public static final String COLUMNNAME_FTU_Chute_ID = "FTU_Chute_ID";

	/** Set Chute	  */
	public void setFTU_Chute_ID (int FTU_Chute_ID);

	/** Get Chute	  */
	public int getFTU_Chute_ID();

	public net.frontuari.recordweight.model.I_FTU_Chute getFTU_Chute() throws RuntimeException;

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

    /** Column name FTU_LoadOrder_ID */
    public static final String COLUMNNAME_FTU_LoadOrder_ID = "FTU_LoadOrder_ID";

	/** Set Load Order	  */
	public void setFTU_LoadOrder_ID (int FTU_LoadOrder_ID);

	/** Get Load Order	  */
	public int getFTU_LoadOrder_ID();

	public net.frontuari.recordweight.model.I_FTU_LoadOrder getFTU_LoadOrder() throws RuntimeException;

    /** Column name FTU_RecordWeight_ID */
    public static final String COLUMNNAME_FTU_RecordWeight_ID = "FTU_RecordWeight_ID";
    
    /** Column name FTU_RecordWeightSource_ID */
    public static final String COLUMNNAME_FTU_RecordWeightSource_ID = "FTU_RecordWeightSource_ID";

	/** Set Record Weight	  */
	public void setFTU_RecordWeight_ID (int FTU_RecordWeight_ID);

	/** Get Record Weight	  */
	public int getFTU_RecordWeight_ID();

    /** Column name FTU_RecordWeight_UU */
    public static final String COLUMNNAME_FTU_RecordWeight_UU = "FTU_RecordWeight_UU";

	/** Set FTU_RecordWeight_UU	  */
	public void setFTU_RecordWeight_UU (String FTU_RecordWeight_UU);

	/** Get FTU_RecordWeight_UU	  */
	public String getFTU_RecordWeight_UU();

    /** Column name FTU_RW_ApprovalMotive_ID */
    public static final String COLUMNNAME_FTU_RW_ApprovalMotive_ID = "FTU_RW_ApprovalMotive_ID";

	/** Set Approval Motive (Weight)	  */
	public void setFTU_RW_ApprovalMotive_ID (int FTU_RW_ApprovalMotive_ID);

	/** Get Approval Motive (Weight)	  */
	public int getFTU_RW_ApprovalMotive_ID();

	public net.frontuari.recordweight.model.I_FTU_RW_ApprovalMotive getFTU_RW_ApprovalMotive() throws RuntimeException;

    /** Column name FTU_Vehicle_ID */
    public static final String COLUMNNAME_FTU_Vehicle_ID = "FTU_Vehicle_ID";

	/** Set Vehicle	  */
	public void setFTU_Vehicle_ID (int FTU_Vehicle_ID);

	/** Get Vehicle	  */
	public int getFTU_Vehicle_ID();

	public net.frontuari.recordweight.model.I_FTU_Vehicle getFTU_Vehicle() throws RuntimeException;

    /** Column name FTU_WeightScale_ID */
    public static final String COLUMNNAME_FTU_WeightScale_ID = "FTU_WeightScale_ID";

	/** Set Weight Scale	  */
	public void setFTU_WeightScale_ID (int FTU_WeightScale_ID);

	/** Get Weight Scale	  */
	public int getFTU_WeightScale_ID();

	public net.frontuari.recordweight.model.I_FTU_WeightScale getFTU_WeightScale() throws RuntimeException;

    /** Column name GrossWeight */
    public static final String COLUMNNAME_GrossWeight = "GrossWeight";

	/** Set GrossWeight	  */
	public void setGrossWeight (BigDecimal GrossWeight);

	/** Get GrossWeight	  */
	public BigDecimal getGrossWeight();

    /** Column name HRS_Analysis_ID */
    public static final String COLUMNNAME_HRS_Analysis_ID = "HRS_Analysis_ID";

	/** Set Analysis	  */
	public void setHRS_Analysis_ID (int HRS_Analysis_ID);

	/** Get Analysis	  */
	public int getHRS_Analysis_ID();

	public net.frontuari.recordweight.model.I_HRS_Analysis getHRS_Analysis() throws RuntimeException;

    /** Column name I_IsImported */
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";

	/** Set Imported.
	  * Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported);

	/** Get Imported.
	  * Has this import been processed
	  */
	public boolean isI_IsImported();

    /** Column name ImportWeight */
    public static final String COLUMNNAME_ImportWeight = "ImportWeight";

	/** Set ImportWeight	  */
	public void setImportWeight (BigDecimal ImportWeight);

	/** Get ImportWeight	  */
	public BigDecimal getImportWeight();

    /** Column name InDate */
    public static final String COLUMNNAME_InDate = "InDate";

	/** Set InDate	  */
	public void setInDate (Timestamp InDate);

	/** Get InDate	  */
	public Timestamp getInDate();

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

    /** Column name IsApproved */
    public static final String COLUMNNAME_IsApproved = "IsApproved";

	/** Set Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name IsPrinted */
    public static final String COLUMNNAME_IsPrinted = "IsPrinted";

	/** Set Printed.
	  * Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted);

	/** Get Printed.
	  * Indicates if this document / line is printed
	  */
	public boolean isPrinted();

    /** Column name IsSOTrx */
    public static final String COLUMNNAME_IsSOTrx = "IsSOTrx";

	/** Set Sales Transaction.
	  * This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx);

	/** Get Sales Transaction.
	  * This is a Sales Transaction
	  */
	public boolean isSOTrx();

    /** Column name IsValidAnalysis */
    public static final String COLUMNNAME_IsValidAnalysis = "IsValidAnalysis";

	/** Set IsValidAnalysis	  */
	public void setIsValidAnalysis (boolean IsValidAnalysis);

	/** Get IsValidAnalysis	  */
	public boolean isValidAnalysis();

    /** Column name MaxWeight */
    public static final String COLUMNNAME_MaxWeight = "MaxWeight";

	/** Set MaxWeight	  */
	public void setMaxWeight (BigDecimal MaxWeight);

	/** Get MaxWeight	  */
	public BigDecimal getMaxWeight();

    /** Column name MinWeight */
    public static final String COLUMNNAME_MinWeight = "MinWeight";

	/** Set MinWeight	  */
	public void setMinWeight (BigDecimal MinWeight);

	/** Get MinWeight	  */
	public BigDecimal getMinWeight();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name M_ProductionLine_ID */
    public static final String COLUMNNAME_M_ProductionLine_ID = "M_ProductionLine_ID";

	/** Set Production Line.
	  * Document Line representing a production
	  */
	public void setM_ProductionLine_ID (int M_ProductionLine_ID);

	/** Get Production Line.
	  * Document Line representing a production
	  */
	public int getM_ProductionLine_ID();

	public org.compiere.model.I_M_ProductionLine getM_ProductionLine() throws RuntimeException;

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

    /** Column name M_Warehouse_ID */
    public static final String COLUMNNAME_M_Warehouse_ID = "M_Warehouse_ID";

	/** Set Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID);

	/** Get Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID();

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException;

    /** Column name NetWeight */
    public static final String COLUMNNAME_NetWeight = "NetWeight";

	/** Set NetWeight	  */
	public void setNetWeight (BigDecimal NetWeight);

	/** Get NetWeight	  */
	public BigDecimal getNetWeight();

    /** Column name OperationType */
    public static final String COLUMNNAME_OperationType = "OperationType";

	/** Set OperationType	  */
	public void setOperationType (String OperationType);

	/** Get OperationType	  */
	public String getOperationType();

    /** Column name OutDate */
    public static final String COLUMNNAME_OutDate = "OutDate";

	/** Set OutDate	  */
	public void setOutDate (Timestamp OutDate);

	/** Get OutDate	  */
	public Timestamp getOutDate();

    /** Column name PayWeight */
    public static final String COLUMNNAME_PayWeight = "PayWeight";

	/** Set PayWeight	  */
	public void setPayWeight (BigDecimal PayWeight);

	/** Get PayWeight	  */
	public BigDecimal getPayWeight();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name ProductAttribute */
    public static final String COLUMNNAME_ProductAttribute = "ProductAttribute";

	/** Set Product Attribute.
	  * Product Attribute Instance Description
	  */
	public void setProductAttribute (boolean ProductAttribute);

	/** Get Product Attribute.
	  * Product Attribute Instance Description
	  */
	public boolean isProductAttribute();

    /** Column name SelectionWeight */
    public static final String COLUMNNAME_SelectionWeight = "SelectionWeight";

	/** Set SelectionWeight	  */
	public void setSelectionWeight (String SelectionWeight);

	/** Get SelectionWeight	  */
	public String getSelectionWeight();

    /** Column name TareWeight */
    public static final String COLUMNNAME_TareWeight = "TareWeight";

	/** Set TareWeight	  */
	public void setTareWeight (BigDecimal TareWeight);

	/** Get TareWeight	  */
	public BigDecimal getTareWeight();

    /** Column name TrailerPlate */
    public static final String COLUMNNAME_TrailerPlate = "TrailerPlate";

	/** Set TrailerPlate	  */
	public void setTrailerPlate (String TrailerPlate);

	/** Get TrailerPlate	  */
	public String getTrailerPlate();

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

    /** Column name WeightStatus */
    public static final String COLUMNNAME_WeightStatus = "WeightStatus";

	/** Set Weight Status	  */
	public void setWeightStatus (String WeightStatus);

	/** Get Weight Status	  */
	public String getWeightStatus();
}
