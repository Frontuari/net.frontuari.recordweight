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

/** Generated Model for FTU_ShipperLiquidation
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_ShipperLiquidation")
public class X_FTU_ShipperLiquidation extends PO implements I_FTU_ShipperLiquidation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240913L;

    /** Standard Constructor */
    public X_FTU_ShipperLiquidation (Properties ctx, int FTU_ShipperLiquidation_ID, String trxName)
    {
      super (ctx, FTU_ShipperLiquidation_ID, trxName);
      /** if (FTU_ShipperLiquidation_ID == 0)
        {
			setAD_User_ID (0);
// @$AD_User_ID@
			setC_ConversionType_ID (0);
			setC_Currency_ID (0);
// @$C_Currency_ID@
			setC_DocType_ID (0);
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDiscountAmt (Env.ZERO);
// 0
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setFTU_ShipperLiquidation_ID (0);
			setGrandTotal (Env.ZERO);
// 0
			setIsApproved (false);
// N
			setM_Shipper_ID (0);
			setPayAmt (Env.ZERO);
// 0
			setPrePaymentAmt (Env.ZERO);
// 0
			setProcessed (false);
// N
			setProcessing (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_FTU_ShipperLiquidation (Properties ctx, int FTU_ShipperLiquidation_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_ShipperLiquidation_ID, trxName, virtualColumns);
      /** if (FTU_ShipperLiquidation_ID == 0)
        {
			setAD_User_ID (0);
// @$AD_User_ID@
			setC_ConversionType_ID (0);
			setC_Currency_ID (0);
// @$C_Currency_ID@
			setC_DocType_ID (0);
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDiscountAmt (Env.ZERO);
// 0
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setFTU_ShipperLiquidation_ID (0);
			setGrandTotal (Env.ZERO);
// 0
			setIsApproved (false);
// N
			setM_Shipper_ID (0);
			setPayAmt (Env.ZERO);
// 0
			setPrePaymentAmt (Env.ZERO);
// 0
			setProcessed (false);
// N
			setProcessing (false);
// N
        } */
    }

    /** Load Constructor */
    public X_FTU_ShipperLiquidation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_ShipperLiquidation[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
	{
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_ID)
			.getPO(getAD_User_ID(), get_TrxName());
	}

	/** Set User/Contact.
		@param AD_User_ID User within the system - Internal or Business Partner Contact
	*/
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1)
			set_Value (COLUMNNAME_AD_User_ID, null);
		else
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getapprova() throws RuntimeException
	{
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_ID)
			.getPO(getapprovalby(), get_TrxName());
	}

	/** Set approvalby.
		@param approvalby approvalby
	*/
	public void setapprovalby (int approvalby)
	{
		set_Value (COLUMNNAME_approvalby, Integer.valueOf(approvalby));
	}

	/** Get approvalby.
		@return approvalby	  */
	public int getapprovalby()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_approvalby);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
	{
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_ID)
			.getPO(getC_ConversionType_ID(), get_TrxName());
	}

	/** Set Currency Type.
		@param C_ConversionType_ID Currency Conversion Rate Type
	*/
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1)
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
	{
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_ID)
			.getPO(getC_Currency_ID(), get_TrxName());
	}

	/** Set Currency.
		@param C_Currency_ID The Currency for this record
	*/
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1)
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
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

	public org.compiere.model.I_AD_User getComplete() throws RuntimeException
	{
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_ID)
			.getPO(getCompletedBy(), get_TrxName());
	}

	/** Set Completed By.
		@param CompletedBy Completed By
	*/
	public void setCompletedBy (int CompletedBy)
	{
		set_Value (COLUMNNAME_CompletedBy, Integer.valueOf(CompletedBy));
	}

	/** Get Completed By.
		@return Completed By	  */
	public int getCompletedBy()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CompletedBy);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Create lines from.
		@param CreateLinesFrom Process which will generate a new document lines based on an existing document
	*/
	public void setCreateLinesFrom (String CreateLinesFrom)
	{
		set_Value (COLUMNNAME_CreateLinesFrom, CreateLinesFrom);
	}

	/** Get Create lines from.
		@return Process which will generate a new document lines based on an existing document
	  */
	public String getCreateLinesFrom()
	{
		return (String)get_Value(COLUMNNAME_CreateLinesFrom);
	}

	/** Set Approval Date.
		@param DateApproval Approval Date
	*/
	public void setDateApproval (Timestamp DateApproval)
	{
		set_Value (COLUMNNAME_DateApproval, DateApproval);
	}

	/** Get Approval Date.
		@return Approval Date
	  */
	public Timestamp getDateApproval()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateApproval);
	}

	/** Set Complete Date.
		@param DateComplete Completion Date
	*/
	public void setDateComplete (Timestamp DateComplete)
	{
		set_Value (COLUMNNAME_DateComplete, DateComplete);
	}

	/** Get Complete Date.
		@return Completion Date
	  */
	public Timestamp getDateComplete()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateComplete);
	}

	/** Set Transaction Date.
		@param DateTrx Transaction Date
	*/
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
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

	/** Set Discount Amount.
		@param DiscountAmt Calculated amount of discount
	*/
	public void setDiscountAmt (BigDecimal DiscountAmt)
	{
		set_Value (COLUMNNAME_DiscountAmt, DiscountAmt);
	}

	/** Get Discount Amount.
		@return Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Payment Request_ID.
		@param FTU_PaymentRequest_ID Payment Request_ID
	*/
	public void setFTU_PaymentRequest_ID (int FTU_PaymentRequest_ID)
	{
		if (FTU_PaymentRequest_ID < 1)
			set_Value (COLUMNNAME_FTU_PaymentRequest_ID, null);
		else
			set_Value (COLUMNNAME_FTU_PaymentRequest_ID, Integer.valueOf(FTU_PaymentRequest_ID));
	}

	/** Get Payment Request_ID.
		@return Payment Request_ID	  */
	public int getFTU_PaymentRequest_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_PaymentRequest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Shipper Liquidation.
		@param FTU_ShipperLiquidation_ID Shipper Liquidation
	*/
	public void setFTU_ShipperLiquidation_ID (int FTU_ShipperLiquidation_ID)
	{
		if (FTU_ShipperLiquidation_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_ShipperLiquidation_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_ShipperLiquidation_ID, Integer.valueOf(FTU_ShipperLiquidation_ID));
	}

	/** Get Shipper Liquidation.
		@return Shipper Liquidation
	  */
	public int getFTU_ShipperLiquidation_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_ShipperLiquidation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_ShipperLiquidation_UU.
		@param FTU_ShipperLiquidation_UU FTU_ShipperLiquidation_UU
	*/
	public void setFTU_ShipperLiquidation_UU (String FTU_ShipperLiquidation_UU)
	{
		set_Value (COLUMNNAME_FTU_ShipperLiquidation_UU, FTU_ShipperLiquidation_UU);
	}

	/** Get FTU_ShipperLiquidation_UU.
		@return FTU_ShipperLiquidation_UU	  */
	public String getFTU_ShipperLiquidation_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_ShipperLiquidation_UU);
	}

	/** Set Grand Total.
		@param GrandTotal Total amount of document
	*/
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_Value (COLUMNNAME_GrandTotal, GrandTotal);
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

	/** Set Approved.
		@param IsApproved Indicates if this document requires approval
	*/
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
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

	/** Set Prepayment.
		@param IsPrepayment The Payment/Receipt is a Prepayment
	*/
	public void setIsPrepayment (boolean IsPrepayment)
	{
		set_Value (COLUMNNAME_IsPrepayment, Boolean.valueOf(IsPrepayment));
	}

	/** Get Prepayment.
		@return The Payment/Receipt is a Prepayment
	  */
	public boolean isPrepayment()
	{
		Object oo = get_Value(COLUMNNAME_IsPrepayment);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Payment amount.
		@param PayAmt Amount being paid
	*/
	public void setPayAmt (BigDecimal PayAmt)
	{
		set_Value (COLUMNNAME_PayAmt, PayAmt);
	}

	/** Get Payment amount.
		@return Amount being paid
	  */
	public BigDecimal getPayAmt()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PayAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prepayment Amount.
		@param PrePaymentAmt Prepayment Amount
	*/
	public void setPrePaymentAmt (BigDecimal PrePaymentAmt)
	{
		set_Value (COLUMNNAME_PrePaymentAmt, PrePaymentAmt);
	}

	/** Get Prepayment Amount.
		@return Prepayment Amount	  */
	public BigDecimal getPrePaymentAmt()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrePaymentAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Processed On.
		@param ProcessedOn The date+time (expressed in decimal format) when the document has been processed
	*/
	public void setProcessedOn (BigDecimal ProcessedOn)
	{
		set_Value (COLUMNNAME_ProcessedOn, ProcessedOn);
	}

	/** Get Processed On.
		@return The date+time (expressed in decimal format) when the document has been processed
	  */
	public BigDecimal getProcessedOn()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ProcessedOn);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Rate.
		@param Rate Rate or Tax or Exchange
	*/
	public void setRate (BigDecimal Rate)
	{
		set_Value (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax Amount.
		@param TaxAmt Tax Amount for a document
	*/
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_Value (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	public BigDecimal getTaxAmt()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}