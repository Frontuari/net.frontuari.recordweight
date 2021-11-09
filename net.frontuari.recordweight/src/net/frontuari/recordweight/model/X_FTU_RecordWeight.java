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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for FTU_RecordWeight
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_RecordWeight extends PO implements I_FTU_RecordWeight, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200504L;

    /** Standard Constructor */
    public X_FTU_RecordWeight (Properties ctx, int FTU_RecordWeight_ID, String trxName)
    {
      super (ctx, FTU_RecordWeight_ID, trxName);
      /** if (FTU_RecordWeight_ID == 0)
        {
			setC_DocType_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocStatus (null);
// DR
			setFTU_EntryTicket_ID (0);
			setFTU_RecordWeight_ID (0);
			setGrossWeight (Env.ZERO);
			setIsApproved (false);
// N
			setIsSOTrx (false);
// N
			setNetWeight (Env.ZERO);
			setOperationType (null);
			setProcessed (false);
			setTareWeight (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_FTU_RecordWeight (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_RecordWeight[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Create lines from.
		@param CreateFrom 
		Process which will generate a new document lines based on an existing document
	  */
	public void setCreateFrom (String CreateFrom)
	{
		set_Value (COLUMNNAME_CreateFrom, CreateFrom);
	}

	/** Get Create lines from.
		@return Process which will generate a new document lines based on an existing document
	  */
	public String getCreateFrom () 
	{
		return (String)get_Value(COLUMNNAME_CreateFrom);
	}

	public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException
    {
		return (org.compiere.model.I_C_UOM)MTable.get(getCtx(), org.compiere.model.I_C_UOM.Table_Name)
			.getPO(getC_UOM_ID(), get_TrxName());	}

	/** Set UOM.
		@param C_UOM_ID 
		Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else 
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
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

	/** Set Document Print.
		@param DocumentPrint Document Print	  */
	public void setDocumentPrint (String DocumentPrint)
	{
		set_Value (COLUMNNAME_DocumentPrint, DocumentPrint);
	}

	/** Get Document Print.
		@return Document Print	  */
	public String getDocumentPrint () 
	{
		return (String)get_Value(COLUMNNAME_DocumentPrint);
	}

	public net.frontuari.recordweight.model.I_FTU_Chute getFTU_Chute() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Chute)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Chute.Table_Name)
			.getPO(getFTU_Chute_ID(), get_TrxName());	}

	/** Set Chute.
		@param FTU_Chute_ID Chute	  */
	public void setFTU_Chute_ID (int FTU_Chute_ID)
	{
		if (FTU_Chute_ID < 1) 
			set_Value (COLUMNNAME_FTU_Chute_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_Chute_ID, Integer.valueOf(FTU_Chute_ID));
	}

	/** Get Chute.
		@return Chute	  */
	public int getFTU_Chute_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Chute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_Driver getFTU_Driver() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Driver)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Driver.Table_Name)
			.getPO(getFTU_Driver_ID(), get_TrxName());	}

	/** Set Driver.
		@param FTU_Driver_ID Driver	  */
	public void setFTU_Driver_ID (int FTU_Driver_ID)
	{
		if (FTU_Driver_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_Driver_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_Driver_ID, Integer.valueOf(FTU_Driver_ID));
	}

	/** Get Driver.
		@return Driver	  */
	public int getFTU_Driver_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Driver_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public net.frontuari.recordweight.model.I_FTU_LoadOrder getFTU_LoadOrder() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_LoadOrder)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_LoadOrder.Table_Name)
			.getPO(getFTU_LoadOrder_ID(), get_TrxName());	}

	/** Set Load Order.
		@param FTU_LoadOrder_ID Load Order	  */
	public void setFTU_LoadOrder_ID (int FTU_LoadOrder_ID)
	{
		if (FTU_LoadOrder_ID < 1) 
			set_Value (COLUMNNAME_FTU_LoadOrder_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_LoadOrder_ID, Integer.valueOf(FTU_LoadOrder_ID));
	}

	/** Get Load Order.
		@return Load Order	  */
	public int getFTU_LoadOrder_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_LoadOrder_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Record Weight.
		@param FTU_RecordWeight_ID Record Weight	  */
	public void setFTU_RecordWeight_ID (int FTU_RecordWeight_ID)
	{
		if (FTU_RecordWeight_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_RecordWeight_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_RecordWeight_ID, Integer.valueOf(FTU_RecordWeight_ID));
	}

	/** Get Record Weight.
		@return Record Weight	  */
	public int getFTU_RecordWeight_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_RecordWeight_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_RecordWeight_UU.
		@param FTU_RecordWeight_UU FTU_RecordWeight_UU	  */
	public void setFTU_RecordWeight_UU (String FTU_RecordWeight_UU)
	{
		set_Value (COLUMNNAME_FTU_RecordWeight_UU, FTU_RecordWeight_UU);
	}

	/** Get FTU_RecordWeight_UU.
		@return FTU_RecordWeight_UU	  */
	public String getFTU_RecordWeight_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_RecordWeight_UU);
	}

	public net.frontuari.recordweight.model.I_FTU_RW_ApprovalMotive getFTU_RW_ApprovalMotive() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_RW_ApprovalMotive)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_RW_ApprovalMotive.Table_Name)
			.getPO(getFTU_RW_ApprovalMotive_ID(), get_TrxName());	}

	/** Set Approval Motive (Weight).
		@param FTU_RW_ApprovalMotive_ID Approval Motive (Weight)	  */
	public void setFTU_RW_ApprovalMotive_ID (int FTU_RW_ApprovalMotive_ID)
	{
		if (FTU_RW_ApprovalMotive_ID < 1) 
			set_Value (COLUMNNAME_FTU_RW_ApprovalMotive_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_RW_ApprovalMotive_ID, Integer.valueOf(FTU_RW_ApprovalMotive_ID));
	}

	/** Get Approval Motive (Weight).
		@return Approval Motive (Weight)	  */
	public int getFTU_RW_ApprovalMotive_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_RW_ApprovalMotive_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_Vehicle getFTU_Vehicle() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_Vehicle)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Vehicle.Table_Name)
			.getPO(getFTU_Vehicle_ID(), get_TrxName());	}

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

	public net.frontuari.recordweight.model.I_FTU_WeightScale getFTU_WeightScale() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_WeightScale)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_WeightScale.Table_Name)
			.getPO(getFTU_WeightScale_ID(), get_TrxName());	}

	/** Set Weight Scale.
		@param FTU_WeightScale_ID Weight Scale	  */
	public void setFTU_WeightScale_ID (int FTU_WeightScale_ID)
	{
		if (FTU_WeightScale_ID < 1) 
			set_Value (COLUMNNAME_FTU_WeightScale_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_WeightScale_ID, Integer.valueOf(FTU_WeightScale_ID));
	}

	/** Get Weight Scale.
		@return Weight Scale	  */
	public int getFTU_WeightScale_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WeightScale_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set GrossWeight.
		@param GrossWeight GrossWeight	  */
	public void setGrossWeight (BigDecimal GrossWeight)
	{
		set_Value (COLUMNNAME_GrossWeight, GrossWeight);
	}

	/** Get GrossWeight.
		@return GrossWeight	  */
	public BigDecimal getGrossWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrossWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public net.frontuari.recordweight.model.I_HRS_Analysis getHRS_Analysis() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_HRS_Analysis)MTable.get(getCtx(), net.frontuari.recordweight.model.I_HRS_Analysis.Table_Name)
			.getPO(getHRS_Analysis_ID(), get_TrxName());	}

	/** Set Analysis.
		@param HRS_Analysis_ID Analysis	  */
	public void setHRS_Analysis_ID (int HRS_Analysis_ID)
	{
		if (HRS_Analysis_ID < 1) 
			set_Value (COLUMNNAME_HRS_Analysis_ID, null);
		else 
			set_Value (COLUMNNAME_HRS_Analysis_ID, Integer.valueOf(HRS_Analysis_ID));
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

	/** Set Imported.
		@param I_IsImported 
		Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported () 
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ImportWeight.
		@param ImportWeight ImportWeight	  */
	public void setImportWeight (BigDecimal ImportWeight)
	{
		set_Value (COLUMNNAME_ImportWeight, ImportWeight);
	}

	/** Get ImportWeight.
		@return ImportWeight	  */
	public BigDecimal getImportWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ImportWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set InDate.
		@param InDate InDate	  */
	public void setInDate (Timestamp InDate)
	{
		set_Value (COLUMNNAME_InDate, InDate);
	}

	/** Get InDate.
		@return InDate	  */
	public Timestamp getInDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_InDate);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_ValueNoCheck (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Printed.
		@param IsPrinted 
		Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted)
	{
		set_ValueNoCheck (COLUMNNAME_IsPrinted, Boolean.valueOf(IsPrinted));
	}

	/** Get Printed.
		@return Indicates if this document / line is printed
	  */
	public boolean isPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_Value (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOTrx);
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

	/** Set MaxWeight.
		@param MaxWeight MaxWeight	  */
	public void setMaxWeight (BigDecimal MaxWeight)
	{
		set_Value (COLUMNNAME_MaxWeight, MaxWeight);
	}

	/** Get MaxWeight.
		@return MaxWeight	  */
	public BigDecimal getMaxWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MaxWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MinWeight.
		@param MinWeight MinWeight	  */
	public void setMinWeight (BigDecimal MinWeight)
	{
		set_Value (COLUMNNAME_MinWeight, MinWeight);
	}

	/** Get MinWeight.
		@return MinWeight	  */
	public BigDecimal getMinWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public org.compiere.model.I_M_ProductionLine getM_ProductionLine() throws RuntimeException
    {
		return (org.compiere.model.I_M_ProductionLine)MTable.get(getCtx(), org.compiere.model.I_M_ProductionLine.Table_Name)
			.getPO(getM_ProductionLine_ID(), get_TrxName());	}

	/** Set Production Line.
		@param M_ProductionLine_ID 
		Document Line representing a production
	  */
	public void setM_ProductionLine_ID (int M_ProductionLine_ID)
	{
		if (M_ProductionLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ProductionLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ProductionLine_ID, Integer.valueOf(M_ProductionLine_ID));
	}

	/** Get Production Line.
		@return Document Line representing a production
	  */
	public int getM_ProductionLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductionLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
		throw new IllegalArgumentException ("M_Shipper_ID is virtual column");	}

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
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
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

	/** Set NetWeight.
		@param NetWeight NetWeight	  */
	public void setNetWeight (BigDecimal NetWeight)
	{
		set_Value (COLUMNNAME_NetWeight, NetWeight);
	}

	/** Get NetWeight.
		@return NetWeight	  */
	public BigDecimal getNetWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_NetWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Delivery Bulk Material = DBM */
	public static final String OPERATIONTYPE_DeliveryBulkMaterial = "DBM";
	/** Delivery Finished Product = DFP */
	public static final String OPERATIONTYPE_DeliveryFinishedProduct = "DFP";
	/** Delivery Multiples Products = DMP */
	public static final String OPERATIONTYPE_DeliveryMultiplesProducts = "DMP";
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
	/** Material Output Movement = MOM */
	public static final String OPERATIONTYPE_MultipleProductMovement = "MMP";
	
	
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

	/** Set OutDate.
		@param OutDate OutDate	  */
	public void setOutDate (Timestamp OutDate)
	{
		set_Value (COLUMNNAME_OutDate, OutDate);
	}

	/** Get OutDate.
		@return OutDate	  */
	public Timestamp getOutDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_OutDate);
	}

	/** Set PayWeight.
		@param PayWeight PayWeight	  */
	public void setPayWeight (BigDecimal PayWeight)
	{
		set_Value (COLUMNNAME_PayWeight, PayWeight);
	}

	/** Get PayWeight.
		@return PayWeight	  */
	public BigDecimal getPayWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PayWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Product Attribute.
		@param ProductAttribute 
		Product Attribute Instance Description
	  */
	public void setProductAttribute (boolean ProductAttribute)
	{
		set_ValueNoCheck (COLUMNNAME_ProductAttribute, Boolean.valueOf(ProductAttribute));
	}

	/** Get Product Attribute.
		@return Product Attribute Instance Description
	  */
	public boolean isProductAttribute () 
	{
		Object oo = get_Value(COLUMNNAME_ProductAttribute);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Import Weight = I */
	public static final String SELECTIONWEIGHT_ImportWeight = "I";
	/** Payment Weight = P */
	public static final String SELECTIONWEIGHT_PaymentWeight = "P";
	/** Set SelectionWeight.
		@param SelectionWeight SelectionWeight	  */
	public void setSelectionWeight (String SelectionWeight)
	{

		set_Value (COLUMNNAME_SelectionWeight, SelectionWeight);
	}

	/** Get SelectionWeight.
		@return SelectionWeight	  */
	public String getSelectionWeight () 
	{
		return (String)get_Value(COLUMNNAME_SelectionWeight);
	}

	/** Set TareWeight.
		@param TareWeight TareWeight	  */
	public void setTareWeight (BigDecimal TareWeight)
	{
		set_Value (COLUMNNAME_TareWeight, TareWeight);
	}

	/** Get TareWeight.
		@return TareWeight	  */
	public BigDecimal getTareWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TareWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TrailerPlate.
		@param TrailerPlate TrailerPlate	  */
	public void setTrailerPlate (String TrailerPlate)
	{
		set_Value (COLUMNNAME_TrailerPlate, TrailerPlate);
	}

	/** Get TrailerPlate.
		@return TrailerPlate	  */
	public String getTrailerPlate () 
	{
		return (String)get_Value(COLUMNNAME_TrailerPlate);
	}

	/** Completed = C */
	public static final String WEIGHTSTATUS_Completed = "C";
	/** Waiting For Gross Weight = G */
	public static final String WEIGHTSTATUS_WaitingForGrossWeight = "G";
	/** Waiting For Tare Weight = T */
	public static final String WEIGHTSTATUS_WaitingForTareWeight = "T";
	/** Set Weight Status.
		@param WeightStatus Weight Status	  */
	public void setWeightStatus (String WeightStatus)
	{

		set_Value (COLUMNNAME_WeightStatus, WeightStatus);
	}

	/** Get Weight Status.
		@return Weight Status	  */
	public String getWeightStatus () 
	{
		return (String)get_Value(COLUMNNAME_WeightStatus);
	}
}