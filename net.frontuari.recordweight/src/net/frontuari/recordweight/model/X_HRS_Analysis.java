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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for HRS_Analysis
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_HRS_Analysis extends PO implements I_HRS_Analysis, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20221104L;

    /** Standard Constructor */
    public X_HRS_Analysis (Properties ctx, int HRS_Analysis_ID, String trxName)
    {
      super (ctx, HRS_Analysis_ID, trxName);
      /** if (HRS_Analysis_ID == 0)
        {
			setAnalysis_ID (0);
			setC_DocType_ID (0);
			setDocStatus (null);
// DR
			setHRS_Analysis_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HRS_Analysis (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRS_Analysis[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_M_AttributeSetInstance getAnalysis() throws RuntimeException
    {
		return (I_M_AttributeSetInstance)MTable.get(getCtx(), I_M_AttributeSetInstance.Table_Name)
			.getPO(getAnalysis_ID(), get_TrxName());	}

	/** Set Analysis_ID.
		@param Analysis_ID Analysis_ID	  */
	public void setAnalysis_ID (int Analysis_ID)
	{
		if (Analysis_ID < 1) 
			set_Value (COLUMNNAME_Analysis_ID, null);
		else 
			set_Value (COLUMNNAME_Analysis_ID, Integer.valueOf(Analysis_ID));
	}

	/** Get Analysis_ID.
		@return Analysis_ID	  */
	public int getAnalysis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Analysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	public org.eevolution.model.I_DD_Order getDD_Order() throws RuntimeException
    {
		return (org.eevolution.model.I_DD_Order)MTable.get(getCtx(), org.eevolution.model.I_DD_Order.Table_Name)
			.getPO(getDD_Order_ID(), get_TrxName());	}

	/** Set Distribution Order.
		@param DD_Order_ID Distribution Order	  */
	public void setDD_Order_ID (int DD_Order_ID)
	{
		if (DD_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DD_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DD_Order_ID, Integer.valueOf(DD_Order_ID));
	}

	/** Get Distribution Order.
		@return Distribution Order	  */
	public int getDD_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	public net.frontuari.recordweight.model.I_FTU_EntryTicket getFTU_EntryTicket() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_EntryTicket)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_EntryTicket.Table_Name)
			.getPO(getFTU_EntryTicket_ID(), get_TrxName());	}

	/** Set Entry Ticket.
		@param FTU_EntryTicket_ID Entry Ticket	  */
	public void setFTU_EntryTicket_ID (int FTU_EntryTicket_ID)
	{
		if (FTU_EntryTicket_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, Integer.valueOf(FTU_EntryTicket_ID));
	}

	/** Get Entry Ticket.
		@return Entry Ticket	  */
	public int getFTU_EntryTicket_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_EntryTicket_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Generate Document.
		@param GenerateDoc 
		Process generate document
	  */
	public void setGenerateDoc (String GenerateDoc)
	{
		set_Value (COLUMNNAME_GenerateDoc, GenerateDoc);
	}

	/** Get Generate Document.
		@return Process generate document
	  */
	public String getGenerateDoc () 
	{
		return (String)get_Value(COLUMNNAME_GenerateDoc);
	}

	/** Set Analysis.
		@param HRS_Analysis_ID Analysis	  */
	public void setHRS_Analysis_ID (int HRS_Analysis_ID)
	{
		if (HRS_Analysis_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRS_Analysis_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRS_Analysis_ID, Integer.valueOf(HRS_Analysis_ID));
	}

	/** Get Analysis.
		@return Analysis	  */
	public int getHRS_Analysis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRS_Analysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRS_Analysis_UU.
		@param HRS_Analysis_UU HRS_Analysis_UU	  */
	public void setHRS_Analysis_UU (String HRS_Analysis_UU)
	{
		set_Value (COLUMNNAME_HRS_Analysis_UU, HRS_Analysis_UU);
	}

	/** Get HRS_Analysis_UU.
		@return HRS_Analysis_UU	  */
	public String getHRS_Analysis_UU () 
	{
		return (String)get_Value(COLUMNNAME_HRS_Analysis_UU);
	}

	/** Set Aproved Analysis.
		@param IsApprovedAnalysis Aproved Analysis	  */
	public void setIsApprovedAnalysis (boolean IsApprovedAnalysis)
	{
		set_Value (COLUMNNAME_IsApprovedAnalysis, Boolean.valueOf(IsApprovedAnalysis));
	}

	/** Get Aproved Analysis.
		@return Aproved Analysis	  */
	public boolean isApprovedAnalysis () 
	{
		Object oo = get_Value(COLUMNNAME_IsApprovedAnalysis);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Manufactured.
		@param IsManufactured 
		This product is manufactured
	  */
	public void setIsManufactured (boolean IsManufactured)
	{
		set_Value (COLUMNNAME_IsManufactured, Boolean.valueOf(IsManufactured));
	}

	/** Get Manufactured.
		@return This product is manufactured
	  */
	public boolean isManufactured () 
	{
		Object oo = get_Value(COLUMNNAME_IsManufactured);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsValidAnalysis.
		@param IsValidAnalysis IsValidAnalysis	  */
	public void setIsValidAnalysis (boolean IsValidAnalysis)
	{
		set_Value (COLUMNNAME_IsValidAnalysis, Boolean.valueOf(IsValidAnalysis));
	}

	/** Get IsValidAnalysis.
		@return IsValidAnalysis	  */
	public boolean isValidAnalysis () 
	{
		Object oo = get_Value(COLUMNNAME_IsValidAnalysis);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_InOut getM_InOut() throws RuntimeException
    {
		return (org.compiere.model.I_M_InOut)MTable.get(getCtx(), org.compiere.model.I_M_InOut.Table_Name)
			.getPO(getM_InOut_ID(), get_TrxName());	}

	/** Set Shipment/Receipt.
		@param M_InOut_ID 
		Material Shipment Document
	  */
	public void setM_InOut_ID (int M_InOut_ID)
	{
		if (M_InOut_ID < 1) 
			set_Value (COLUMNNAME_M_InOut_ID, null);
		else 
			set_Value (COLUMNNAME_M_InOut_ID, Integer.valueOf(M_InOut_ID));
	}

	/** Get Shipment/Receipt.
		@return Material Shipment Document
	  */
	public int getM_InOut_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InOut_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_InOutLine getM_InOutLine() throws RuntimeException
    {
		return (org.compiere.model.I_M_InOutLine)MTable.get(getCtx(), org.compiere.model.I_M_InOutLine.Table_Name)
			.getPO(getM_InOutLine_ID(), get_TrxName());	}

	/** Set Shipment/Receipt Line.
		@param M_InOutLine_ID 
		Line on Shipment or Receipt document
	  */
	public void setM_InOutLine_ID (int M_InOutLine_ID)
	{
		if (M_InOutLine_ID < 1) 
			set_Value (COLUMNNAME_M_InOutLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_InOutLine_ID, Integer.valueOf(M_InOutLine_ID));
	}

	/** Get Shipment/Receipt Line.
		@return Line on Shipment or Receipt document
	  */
	public int getM_InOutLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InOutLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Locator getM_Locator() throws RuntimeException
    {
		return (org.compiere.model.I_M_Locator)MTable.get(getCtx(), org.compiere.model.I_M_Locator.Table_Name)
			.getPO(getM_Locator_ID(), get_TrxName());	}

	/** Set Locator.
		@param M_Locator_ID 
		Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID)
	{
		if (M_Locator_ID < 1) 
			set_Value (COLUMNNAME_M_Locator_ID, null);
		else 
			set_Value (COLUMNNAME_M_Locator_ID, Integer.valueOf(M_Locator_ID));
	}

	/** Get Locator.
		@return Warehouse Locator
	  */
	public int getM_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Locator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException
    {
		return (org.compiere.model.I_M_Warehouse)MTable.get(getCtx(), org.compiere.model.I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_ID(), get_TrxName());	}

	/** Set Warehouse.
		@param M_Warehouse_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1) 
			set_Value (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_Value (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Delivery Bulk Material = DBM */
	public static final String OPERATIONTYPE_DeliveryBulkMaterial = "DBM";
	/** Delivery Finished Product = DFP */
	public static final String OPERATIONTYPE_DeliveryFinishedProduct = "DFP";
	/** Material Input Movement = MIM */
	public static final String OPERATIONTYPE_MaterialInputMovement = "MIM";
	/** Material Output Movement = MOM */
	public static final String OPERATIONTYPE_MaterialOutputMovement = "MOM";
	/** Other Record Weight = ORW */
	public static final String OPERATIONTYPE_OtherRecordWeight = "ORW";
	/** Product Bulk Receipt = PBR */
	public static final String OPERATIONTYPE_ProductBulkReceipt = "PBR";
	/** Receipt More than one Product = RMP */
	public static final String OPERATIONTYPE_ReceiptMoreThanOneProduct = "RMP";
	/** Raw Material Receipt = RMR */
	public static final String OPERATIONTYPE_RawMaterialReceipt = "RMR";
	/** Delivery Multiple Products = DMP */
	public static final String OPERATIONTYPE_DeliveryMultipleProducts = "DMP";
	/** Set OperationType.
		@param OperationType OperationType	  */
	public void setOperationType (String OperationType)
	{

		set_Value (COLUMNNAME_OperationType, OperationType);
	}

	/** Get OperationType.
		@return OperationType	  */
	public String getOperationType () 
	{
		return (String)get_Value(COLUMNNAME_OperationType);
	}

	public org.eevolution.model.I_PP_Order getPP_Order() throws RuntimeException
    {
		return (org.eevolution.model.I_PP_Order)MTable.get(getCtx(), org.eevolution.model.I_PP_Order.Table_Name)
			.getPO(getPP_Order_ID(), get_TrxName());	}

	/** Set Manufacturing Order.
		@param PP_Order_ID 
		Manufacturing Order
	  */
	public void setPP_Order_ID (int PP_Order_ID)
	{
		if (PP_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PP_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PP_Order_ID, Integer.valueOf(PP_Order_ID));
	}

	/** Get Manufacturing Order.
		@return Manufacturing Order
	  */
	public int getPP_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PP_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Status AD_Reference_ID=53239 */
	public static final int STATUS_AD_Reference_ID=53239;
	/** In Progress = IP */
	public static final String STATUS_InProgress = "IP";
	/** Completed = CO */
	public static final String STATUS_Completed = "CO";
	/** Error = ER */
	public static final String STATUS_Error = "ER";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_ValueNoCheck (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}
}