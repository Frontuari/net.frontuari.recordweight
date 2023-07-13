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

/** Generated Model for FTU_FreightCost
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_FreightCost")
public class X_FTU_FreightCost extends PO implements I_FTU_FreightCost, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230713L;

    /** Standard Constructor */
    public X_FTU_FreightCost (Properties ctx, int FTU_FreightCost_ID, String trxName)
    {
      super (ctx, FTU_FreightCost_ID, trxName);
      /** if (FTU_FreightCost_ID == 0)
        {
        } */
    }

    /** Standard Constructor */
    public X_FTU_FreightCost (Properties ctx, int FTU_FreightCost_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_FreightCost_ID, trxName, virtualColumns);
      /** if (FTU_FreightCost_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_FTU_FreightCost (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_FreightCost[")
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

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
	{
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_ID)
			.getPO(getC_SalesRegion_ID(), get_TrxName());
	}

	/** Set Sales Region.
		@param C_SalesRegion_ID Sales coverage region
	*/
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1)
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
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

	/** Set Adjust Price.
		@param FTU_AdjustPrice Adjust Price
	*/
	public void setFTU_AdjustPrice (boolean FTU_AdjustPrice)
	{
		set_Value (COLUMNNAME_FTU_AdjustPrice, Boolean.valueOf(FTU_AdjustPrice));
	}

	/** Get Adjust Price.
		@return Adjust Price	  */
	public boolean isFTU_AdjustPrice()
	{
		Object oo = get_Value(COLUMNNAME_FTU_AdjustPrice);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Bill of Lading.
		@param FTU_FreightCost_ID Bill of Lading
	*/
	public void setFTU_FreightCost_ID (int FTU_FreightCost_ID)
	{
		if (FTU_FreightCost_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_FreightCost_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_FreightCost_ID, Integer.valueOf(FTU_FreightCost_ID));
	}

	/** Get Bill of Lading.
		@return Bill of Lading	  */
	public int getFTU_FreightCost_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_FreightCost_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Freight Cost UU.
		@param FTU_FreightCost_UU Freight Cost UU
	*/
	public void setFTU_FreightCost_UU (String FTU_FreightCost_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_FreightCost_UU, FTU_FreightCost_UU);
	}

	/** Get Freight Cost UU.
		@return Freight Cost UU	  */
	public String getFTU_FreightCost_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_FreightCost_UU);
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

	public net.frontuari.recordweight.model.I_FTU_PriceForTrip getFTU_PriceForTrip() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_PriceForTrip)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_PriceForTrip.Table_ID)
			.getPO(getFTU_PriceForTrip_ID(), get_TrxName());
	}

	/** Set Price For Trip.
		@param FTU_PriceForTrip_ID Price For Trip
	*/
	public void setFTU_PriceForTrip_ID (int FTU_PriceForTrip_ID)
	{
		if (FTU_PriceForTrip_ID < 1)
			set_Value (COLUMNNAME_FTU_PriceForTrip_ID, null);
		else
			set_Value (COLUMNNAME_FTU_PriceForTrip_ID, Integer.valueOf(FTU_PriceForTrip_ID));
	}

	/** Get Price For Trip.
		@return Price For Trip	  */
	public int getFTU_PriceForTrip_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_PriceForTrip_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Zero Cost.
		@param FTU_ZeroCost Zero Cost
	*/
	public void setFTU_ZeroCost (boolean FTU_ZeroCost)
	{
		set_Value (COLUMNNAME_FTU_ZeroCost, Boolean.valueOf(FTU_ZeroCost));
	}

	/** Get Zero Cost.
		@return Zero Cost	  */
	public boolean isFTU_ZeroCost()
	{
		Object oo = get_Value(COLUMNNAME_FTU_ZeroCost);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Grand Total.
		@param GrandTotal Total amount of document
	*/
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_ValueNoCheck (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Create.
		@param ProcCreate Create
	*/
	public void setProcCreate (String ProcCreate)
	{
		set_Value (COLUMNNAME_ProcCreate, ProcCreate);
	}

	/** Get Create.
		@return Create	  */
	public String getProcCreate()
	{
		return (String)get_Value(COLUMNNAME_ProcCreate);
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

	/** Set Weight.
		@param Weight Weight of a product
	*/
	public void setWeight (BigDecimal Weight)
	{
		set_Value (COLUMNNAME_Weight, Weight);
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