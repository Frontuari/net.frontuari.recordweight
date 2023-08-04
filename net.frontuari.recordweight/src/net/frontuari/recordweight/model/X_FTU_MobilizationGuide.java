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

/** Generated Model for FTU_MobilizationGuide
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_MobilizationGuide")
public class X_FTU_MobilizationGuide extends PO implements I_FTU_MobilizationGuide, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230725L;

    /** Standard Constructor */
    public X_FTU_MobilizationGuide (Properties ctx, int FTU_MobilizationGuide_ID, String trxName)
    {
      super (ctx, FTU_MobilizationGuide_ID, trxName);
      /** if (FTU_MobilizationGuide_ID == 0)
        {
			setC_DocType_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setFTU_MobilizationGuide_ID (0);
			setFTU_VehicleType_ID (0);
			setQtyToDeliver (Env.ZERO);
// 0
        } */
    }

    /** Standard Constructor */
    public X_FTU_MobilizationGuide (Properties ctx, int FTU_MobilizationGuide_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_MobilizationGuide_ID, trxName, virtualColumns);
      /** if (FTU_MobilizationGuide_ID == 0)
        {
			setC_DocType_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setFTU_MobilizationGuide_ID (0);
			setFTU_VehicleType_ID (0);
			setQtyToDeliver (Env.ZERO);
// 0
        } */
    }

    /** Load Constructor */
    public X_FTU_MobilizationGuide (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_MobilizationGuide[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Trx Organization.
		@param AD_OrgTrx_ID Performing or initiating organization
	*/
	public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
	{
		if (AD_OrgTrx_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AD_OrgTrx_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_OrgTrx_ID, Integer.valueOf(AD_OrgTrx_ID));
	}

	/** Get Trx Organization.
		@return Performing or initiating organization
	  */
	public int getAD_OrgTrx_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgTrx_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner .
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
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

	/** Set Mobilization Guide.
		@param FTU_MobilizationGuide_ID Mobilization Guide
	*/
	public void setFTU_MobilizationGuide_ID (int FTU_MobilizationGuide_ID)
	{
		if (FTU_MobilizationGuide_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_MobilizationGuide_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_MobilizationGuide_ID, Integer.valueOf(FTU_MobilizationGuide_ID));
	}

	/** Get Mobilization Guide.
		@return Mobilization Guide	  */
	public int getFTU_MobilizationGuide_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_MobilizationGuide_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_MobilizationGuide_UU.
		@param FTU_MobilizationGuide_UU FTU_MobilizationGuide_UU
	*/
	public void setFTU_MobilizationGuide_UU (String FTU_MobilizationGuide_UU)
	{
		set_Value (COLUMNNAME_FTU_MobilizationGuide_UU, FTU_MobilizationGuide_UU);
	}

	/** Get FTU_MobilizationGuide_UU.
		@return FTU_MobilizationGuide_UU	  */
	public String getFTU_MobilizationGuide_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_MobilizationGuide_UU);
	}

	public net.frontuari.recordweight.model.I_FTU_RecordWeight getFTU_RecordWeight() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_RecordWeight)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_RecordWeight.Table_ID)
			.getPO(getFTU_RecordWeight_ID(), get_TrxName());
	}

	/** Set Record Weight.
		@param FTU_RecordWeight_ID Record Weight
	*/
	public void setFTU_RecordWeight_ID (int FTU_RecordWeight_ID)
	{
		if (FTU_RecordWeight_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_RecordWeight_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_RecordWeight_ID, Integer.valueOf(FTU_RecordWeight_ID));
	}

	/** Get Record Weight.
		@return Record Weight	  */
	public int getFTU_RecordWeight_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_RecordWeight_ID);
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

	/** Set Guide.
		@param Guide Guide
	*/
	public void setGuide (String Guide)
	{
		set_Value (COLUMNNAME_Guide, Guide);
	}

	/** Get Guide.
		@return Guide	  */
	public String getGuide()
	{
		return (String)get_Value(COLUMNNAME_Guide);
	}

	/** Set Sales Transaction.
		@param IsSOTrx This is a Sales Transaction
	*/
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_ValueNoCheck (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx()
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
			set_Value (COLUMNNAME_M_Warehouse_ID, null);
		else
			set_Value (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
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

	/** Set Qty to deliver.
		@param QtyToDeliver Qty to deliver
	*/
	public void setQtyToDeliver (BigDecimal QtyToDeliver)
	{
		set_Value (COLUMNNAME_QtyToDeliver, QtyToDeliver);
	}

	/** Get Qty to deliver.
		@return Qty to deliver	  */
	public BigDecimal getQtyToDeliver()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyToDeliver);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Valid to.
		@param ValidTo Valid to including this date (last day)
	*/
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo()
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
	}
}