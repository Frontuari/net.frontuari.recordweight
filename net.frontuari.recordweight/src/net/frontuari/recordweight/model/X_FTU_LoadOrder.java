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

/** Generated Model for FTU_LoadOrder
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_LoadOrder")
public class X_FTU_LoadOrder extends PO implements I_FTU_LoadOrder, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_FTU_LoadOrder (Properties ctx, int FTU_LoadOrder_ID, String trxName)
    {
      super (ctx, FTU_LoadOrder_ID, trxName);
      /** if (FTU_LoadOrder_ID == 0)
        {
			setC_DocType_ID (0);
			setC_UOM_Weight_ID (0);
// @SQL=SELECT C_UOM_Weight_ID FROM AD_ClientInfo WHERE AD_Client_ID = @#AD_Client_ID@
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setFTU_EntryTicket_ID (0);
			setFTU_LoadOrder_ID (0);
			setFTU_VehicleType_ID (0);
			setIsApproved (false);
// N
			setLoadCapacity (Env.ZERO);
			setOperationType (null);
			setProcessed (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_FTU_LoadOrder (Properties ctx, int FTU_LoadOrder_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_LoadOrder_ID, trxName, virtualColumns);
      /** if (FTU_LoadOrder_ID == 0)
        {
			setC_DocType_ID (0);
			setC_UOM_Weight_ID (0);
// @SQL=SELECT C_UOM_Weight_ID FROM AD_ClientInfo WHERE AD_Client_ID = @#AD_Client_ID@
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setFTU_EntryTicket_ID (0);
			setFTU_LoadOrder_ID (0);
			setFTU_VehicleType_ID (0);
			setIsApproved (false);
// N
			setLoadCapacity (Env.ZERO);
			setOperationType (null);
			setProcessed (false);
// N
        } */
    }

    /** Load Constructor */
    public X_FTU_LoadOrder (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_LoadOrder[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocType_ID(), get_TrxName());
	}

	/** Set Document Type.
		@param C_DocType_ID Document type or rules
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
	public int getC_DocType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ConfirmedWeight.
		@param ConfirmedWeight ConfirmedWeight
	*/
	public void setConfirmedWeight (BigDecimal ConfirmedWeight)
	{
		set_Value (COLUMNNAME_ConfirmedWeight, ConfirmedWeight);
	}

	/** Get ConfirmedWeight.
		@return ConfirmedWeight	  */
	public BigDecimal getConfirmedWeight()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ConfirmedWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Create lines from.
		@param CreateFrom Process which will generate a new document lines based on an existing document
	*/
	public void setCreateFrom (String CreateFrom)
	{
		set_Value (COLUMNNAME_CreateFrom, CreateFrom);
	}

	/** Get Create lines from.
		@return Process which will generate a new document lines based on an existing document
	  */
	public String getCreateFrom()
	{
		return (String)get_Value(COLUMNNAME_CreateFrom);
	}

	public org.compiere.model.I_C_UOM getC_UOM_Volume() throws RuntimeException
	{
		return (org.compiere.model.I_C_UOM)MTable.get(getCtx(), org.compiere.model.I_C_UOM.Table_ID)
			.getPO(getC_UOM_Volume_ID(), get_TrxName());
	}

	/** Set UOM for Volume.
		@param C_UOM_Volume_ID Standard Unit of Measure for Volume
	*/
	public void setC_UOM_Volume_ID (int C_UOM_Volume_ID)
	{
		if (C_UOM_Volume_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_UOM_Volume_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_UOM_Volume_ID, Integer.valueOf(C_UOM_Volume_ID));
	}

	/** Get UOM for Volume.
		@return Standard Unit of Measure for Volume
	  */
	public int getC_UOM_Volume_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_Volume_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_UOM getC_UOM_Weight() throws RuntimeException
	{
		return (org.compiere.model.I_C_UOM)MTable.get(getCtx(), org.compiere.model.I_C_UOM.Table_ID)
			.getPO(getC_UOM_Weight_ID(), get_TrxName());
	}

	/** Set UOM for Weight.
		@param C_UOM_Weight_ID Standard Unit of Measure for Weight
	*/
	public void setC_UOM_Weight_ID (int C_UOM_Weight_ID)
	{
		if (C_UOM_Weight_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_UOM_Weight_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_UOM_Weight_ID, Integer.valueOf(C_UOM_Weight_ID));
	}

	/** Get UOM for Weight.
		@return Standard Unit of Measure for Weight
	  */
	public int getC_UOM_Weight_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_Weight_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc Date of the Document
	*/
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** DeliveryRule AD_Reference_ID=151 */
	public static final int DELIVERYRULE_AD_Reference_ID=151;
	/** Availability = A */
	public static final String DELIVERYRULE_Availability = "A";
	/** Force = F */
	public static final String DELIVERYRULE_Force = "F";
	/** Complete Line = L */
	public static final String DELIVERYRULE_CompleteLine = "L";
	/** Manual = M */
	public static final String DELIVERYRULE_Manual = "M";
	/** Complete Order = O */
	public static final String DELIVERYRULE_CompleteOrder = "O";
	/** After Payment = R */
	public static final String DELIVERYRULE_AfterPayment = "R";
	/** Set Delivery Rule.
		@param DeliveryRule Defines the timing of Delivery
	*/
	public void setDeliveryRule (String DeliveryRule)
	{

		set_ValueNoCheck (COLUMNNAME_DeliveryRule, DeliveryRule);
	}

	/** Get Delivery Rule.
		@return Defines the timing of Delivery
	  */
	public String getDeliveryRule()
	{
		return (String)get_Value(COLUMNNAME_DeliveryRule);
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** &lt;None&gt; = -- */
	public static final String DOCACTION_None = "--";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Set Document Action.
		@param DocAction The targeted status of the document
	*/
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction()
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Set Document Status.
		@param DocStatus The current status of the document
	*/
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus()
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo Document sequence number of the document
	*/
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	public net.frontuari.recordweight.model.I_FTU_Driver getFTU_Driver() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_Driver)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Driver.Table_ID)
			.getPO(getFTU_Driver_ID(), get_TrxName());
	}

	/** Set Driver.
		@param FTU_Driver_ID Driver
	*/
	public void setFTU_Driver_ID (int FTU_Driver_ID)
	{
		if (FTU_Driver_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_Driver_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_Driver_ID, Integer.valueOf(FTU_Driver_ID));
	}

	/** Get Driver.
		@return Driver	  */
	public int getFTU_Driver_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Driver_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_EntryTicket getFTU_EntryTicket() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_EntryTicket)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_EntryTicket.Table_ID)
			.getPO(getFTU_EntryTicket_ID(), get_TrxName());
	}

	/** Set Entry Ticket.
		@param FTU_EntryTicket_ID Entry Ticket
	*/
	public void setFTU_EntryTicket_ID (int FTU_EntryTicket_ID)
	{
		if (FTU_EntryTicket_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, Integer.valueOf(FTU_EntryTicket_ID));
	}

	/** Get Entry Ticket.
		@return Entry Ticket	  */
	public int getFTU_EntryTicket_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_EntryTicket_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Load Order.
		@param FTU_LoadOrder_ID Load Order
	*/
	public void setFTU_LoadOrder_ID (int FTU_LoadOrder_ID)
	{
		if (FTU_LoadOrder_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_LoadOrder_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_LoadOrder_ID, Integer.valueOf(FTU_LoadOrder_ID));
	}

	/** Get Load Order.
		@return Load Order	  */
	public int getFTU_LoadOrder_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_LoadOrder_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_LoadOrder_UU.
		@param FTU_LoadOrder_UU FTU_LoadOrder_UU
	*/
	public void setFTU_LoadOrder_UU (String FTU_LoadOrder_UU)
	{
		set_Value (COLUMNNAME_FTU_LoadOrder_UU, FTU_LoadOrder_UU);
	}

	/** Get FTU_LoadOrder_UU.
		@return FTU_LoadOrder_UU	  */
	public String getFTU_LoadOrder_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_LoadOrder_UU);
	}

	public net.frontuari.recordweight.model.I_FTU_Vehicle getFTU_Vehicle() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_Vehicle)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Vehicle.Table_ID)
			.getPO(getFTU_Vehicle_ID(), get_TrxName());
	}

	/** Set Vehicle.
		@param FTU_Vehicle_ID Vehicle
	*/
	public void setFTU_Vehicle_ID (int FTU_Vehicle_ID)
	{
		if (FTU_Vehicle_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_Vehicle_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_Vehicle_ID, Integer.valueOf(FTU_Vehicle_ID));
	}

	/** Get Vehicle.
		@return Vehicle	  */
	public int getFTU_Vehicle_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Vehicle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_VehicleType getFTU_VehicleType() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_VehicleType)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_VehicleType.Table_ID)
			.getPO(getFTU_VehicleType_ID(), get_TrxName());
	}

	/** Set Vehicle Type.
		@param FTU_VehicleType_ID Vehicle Type
	*/
	public void setFTU_VehicleType_ID (int FTU_VehicleType_ID)
	{
		if (FTU_VehicleType_ID < 1)
			set_Value (COLUMNNAME_FTU_VehicleType_ID, null);
		else
			set_Value (COLUMNNAME_FTU_VehicleType_ID, Integer.valueOf(FTU_VehicleType_ID));
	}

	/** Get Vehicle Type.
		@return Vehicle Type	  */
	public int getFTU_VehicleType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_VehicleType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Generate Freight Cost.
		@param GenerateFreightCost Generate Freight Cost
	*/
	public void setGenerateFreightCost (String GenerateFreightCost)
	{
		set_Value (COLUMNNAME_GenerateFreightCost, GenerateFreightCost);
	}

	/** Get Generate Freight Cost.
		@return Generate Freight Cost	  */
	public String getGenerateFreightCost()
	{
		return (String)get_Value(COLUMNNAME_GenerateFreightCost);
	}

	/** InvoiceRule AD_Reference_ID=150 */
	public static final int INVOICERULE_AD_Reference_ID=150;
	/** After Delivery = D */
	public static final String INVOICERULE_AfterDelivery = "D";
	/** Immediate = I */
	public static final String INVOICERULE_Immediate = "I";
	/** After Order delivered = O */
	public static final String INVOICERULE_AfterOrderDelivered = "O";
	/** Customer Schedule after Delivery = S */
	public static final String INVOICERULE_CustomerScheduleAfterDelivery = "S";
	/** Set Invoice Rule.
		@param InvoiceRule Frequency and method of invoicing 
	*/
	public void setInvoiceRule (String InvoiceRule)
	{

		set_ValueNoCheck (COLUMNNAME_InvoiceRule, InvoiceRule);
	}

	/** Get Invoice Rule.
		@return Frequency and method of invoicing 
	  */
	public String getInvoiceRule()
	{
		return (String)get_Value(COLUMNNAME_InvoiceRule);
	}

	/** Set Approved.
		@param IsApproved Indicates if this document requires approval
	*/
	public void setIsApproved (boolean IsApproved)
	{
		set_ValueNoCheck (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved()
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

	/** Set Delivered.
		@param IsDelivered Delivered
	*/
	public void setIsDelivered (boolean IsDelivered)
	{
		set_ValueNoCheck (COLUMNNAME_IsDelivered, Boolean.valueOf(IsDelivered));
	}

	/** Get Delivered.
		@return Delivered	  */
	public boolean isDelivered()
	{
		Object oo = get_Value(COLUMNNAME_IsDelivered);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Generated.
		@param IsGenerated This Line is generated
	*/
	public void setIsGenerated (boolean IsGenerated)
	{
		set_ValueNoCheck (COLUMNNAME_IsGenerated, Boolean.valueOf(IsGenerated));
	}

	/** Get Generated.
		@return This Line is generated
	  */
	public boolean isGenerated()
	{
		Object oo = get_Value(COLUMNNAME_IsGenerated);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsHandleRecordWeight.
		@param IsHandleRecordWeight IsHandleRecordWeight
	*/
	public void setIsHandleRecordWeight (boolean IsHandleRecordWeight)
	{
		set_Value (COLUMNNAME_IsHandleRecordWeight, Boolean.valueOf(IsHandleRecordWeight));
	}

	/** Get IsHandleRecordWeight.
		@return IsHandleRecordWeight	  */
	public boolean isHandleRecordWeight()
	{
		Object oo = get_Value(COLUMNNAME_IsHandleRecordWeight);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsImmediateDelivery.
		@param IsImmediateDelivery IsImmediateDelivery
	*/
	public void setIsImmediateDelivery (boolean IsImmediateDelivery)
	{
		set_Value (COLUMNNAME_IsImmediateDelivery, Boolean.valueOf(IsImmediateDelivery));
	}

	/** Get IsImmediateDelivery.
		@return IsImmediateDelivery	  */
	public boolean isImmediateDelivery()
	{
		Object oo = get_Value(COLUMNNAME_IsImmediateDelivery);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Invoiced.
		@param IsInvoiced Is this invoiced?
	*/
	public void setIsInvoiced (boolean IsInvoiced)
	{
		set_ValueNoCheck (COLUMNNAME_IsInvoiced, Boolean.valueOf(IsInvoiced));
	}

	/** Get Invoiced.
		@return Is this invoiced?
	  */
	public boolean isInvoiced()
	{
		Object oo = get_Value(COLUMNNAME_IsInvoiced);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsMoved.
		@param IsMoved IsMoved
	*/
	public void setIsMoved (boolean IsMoved)
	{
		set_Value (COLUMNNAME_IsMoved, Boolean.valueOf(IsMoved));
	}

	/** Get IsMoved.
		@return IsMoved	  */
	public boolean isMoved()
	{
		Object oo = get_Value(COLUMNNAME_IsMoved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Processing.
		@param IsProcessing Processing
	*/
	public void setIsProcessing (String IsProcessing)
	{
		set_Value (COLUMNNAME_IsProcessing, IsProcessing);
	}

	/** Get Processing.
		@return Processing	  */
	public String getIsProcessing()
	{
		return (String)get_Value(COLUMNNAME_IsProcessing);
	}

	/** Set IsWeightRegister.
		@param IsWeightRegister IsWeightRegister
	*/
	public void setIsWeightRegister (boolean IsWeightRegister)
	{
		set_Value (COLUMNNAME_IsWeightRegister, Boolean.valueOf(IsWeightRegister));
	}

	/** Get IsWeightRegister.
		@return IsWeightRegister	  */
	public boolean isWeightRegister()
	{
		Object oo = get_Value(COLUMNNAME_IsWeightRegister);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LoadCapacity.
		@param LoadCapacity LoadCapacity
	*/
	public void setLoadCapacity (BigDecimal LoadCapacity)
	{
		set_Value (COLUMNNAME_LoadCapacity, LoadCapacity);
	}

	/** Get LoadCapacity.
		@return LoadCapacity	  */
	public BigDecimal getLoadCapacity()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LoadCapacity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
	{
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_ID)
			.getPO(getM_Product_ID(), get_TrxName());
	}

	/** Set Product.
		@param M_Product_ID Product, Service, Item
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
	public int getM_Product_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Shipper getM_Shipper() throws RuntimeException
	{
		return (org.compiere.model.I_M_Shipper)MTable.get(getCtx(), org.compiere.model.I_M_Shipper.Table_ID)
			.getPO(getM_Shipper_ID(), get_TrxName());
	}

	/** Set Shipper.
		@param M_Shipper_ID Method or manner of product delivery
	*/
	public void setM_Shipper_ID (int M_Shipper_ID)
	{
		if (M_Shipper_ID < 1)
			set_Value (COLUMNNAME_M_Shipper_ID, null);
		else
			set_Value (COLUMNNAME_M_Shipper_ID, Integer.valueOf(M_Shipper_ID));
	}

	/** Get Shipper.
		@return Method or manner of product delivery
	  */
	public int getM_Shipper_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Shipper_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException
	{
		return (org.compiere.model.I_M_Warehouse)MTable.get(getCtx(), org.compiere.model.I_M_Warehouse.Table_ID)
			.getPO(getM_Warehouse_ID(), get_TrxName());
	}

	/** Set Warehouse.
		@param M_Warehouse_ID Storage Warehouse and Service Point
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
	public int getM_Warehouse_ID()
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
	/** Delivery Multiple Products = DMP */
	public static final String OPERATIONTYPE_DeliveryMultipleProducts = "DMP";
	/** Import Raw Material = IRM */
	public static final String OPERATIONTYPE_ImportRawMaterial = "IRM";
	/** Material Input Movement = MIM */
	public static final String OPERATIONTYPE_MaterialInputMovement = "MIM";
	/** Movement Multiple Product = MMP */
	public static final String OPERATIONTYPE_MovementMultipleProduct = "MMP";
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
	/** Set OperationType.
		@param OperationType OperationType
	*/
	public void setOperationType (String OperationType)
	{

		set_Value (COLUMNNAME_OperationType, OperationType);
	}

	/** Get OperationType.
		@return OperationType	  */
	public String getOperationType()
	{
		return (String)get_Value(COLUMNNAME_OperationType);
	}

	/** Set Processed.
		@param Processed The document has been processed
	*/
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed()
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
		@param Processing Process Now
	*/
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing()
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

	/** Set SealNo.
		@param SealNo SealNo
	*/
	public void setSealNo (String SealNo)
	{
		set_Value (COLUMNNAME_SealNo, SealNo);
	}

	/** Get SealNo.
		@return SealNo	  */
	public String getSealNo()
	{
		return (String)get_Value(COLUMNNAME_SealNo);
	}

	/** Set Ship Date.
		@param ShipDate Shipment Date/Time
	*/
	public void setShipDate (Timestamp ShipDate)
	{
		set_Value (COLUMNNAME_ShipDate, ShipDate);
	}

	/** Get Ship Date.
		@return Shipment Date/Time
	  */
	public Timestamp getShipDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_ShipDate);
	}

	/** Set Volume.
		@param Volume Volume of a product
	*/
	public void setVolume (BigDecimal Volume)
	{
		set_ValueNoCheck (COLUMNNAME_Volume, Volume);
	}

	/** Get Volume.
		@return Volume of a product
	  */
	public BigDecimal getVolume()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Volume);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VolumeCapacity.
		@param VolumeCapacity VolumeCapacity
	*/
	public void setVolumeCapacity (BigDecimal VolumeCapacity)
	{
		set_Value (COLUMNNAME_VolumeCapacity, VolumeCapacity);
	}

	/** Get VolumeCapacity.
		@return VolumeCapacity	  */
	public BigDecimal getVolumeCapacity()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VolumeCapacity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Weight.
		@param Weight Weight of a product
	*/
	public void setWeight (BigDecimal Weight)
	{
		set_ValueNoCheck (COLUMNNAME_Weight, Weight);
	}

	/** Get Weight.
		@return Weight of a product
	  */
	public BigDecimal getWeight()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}