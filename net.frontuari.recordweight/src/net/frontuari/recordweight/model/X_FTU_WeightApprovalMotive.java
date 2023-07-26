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
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for FTU_WeightApprovalMotive
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_WeightApprovalMotive")
public class X_FTU_WeightApprovalMotive extends PO implements I_FTU_WeightApprovalMotive, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230726L;

    /** Standard Constructor */
    public X_FTU_WeightApprovalMotive (Properties ctx, int FTU_WeightApprovalMotive_ID, String trxName)
    {
      super (ctx, FTU_WeightApprovalMotive_ID, trxName);
      /** if (FTU_WeightApprovalMotive_ID == 0)
        {
			setFTU_WeightApprovalMotive_ID (0);
			setIsApproved (false);
        } */
    }

    /** Standard Constructor */
    public X_FTU_WeightApprovalMotive (Properties ctx, int FTU_WeightApprovalMotive_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_WeightApprovalMotive_ID, trxName, virtualColumns);
      /** if (FTU_WeightApprovalMotive_ID == 0)
        {
			setFTU_WeightApprovalMotive_ID (0);
			setIsApproved (false);
        } */
    }

    /** Load Constructor */
    public X_FTU_WeightApprovalMotive (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_WeightApprovalMotive[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_ID(), get_TrxName());
	}

	/** Set Charge.
		@param C_Charge_ID Additional document charges
	*/
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1)
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocTypeInventory() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocTypeInventory_ID(), get_TrxName());
	}

	/** Set C_DocTypeInventory_ID.
		@param C_DocTypeInventory_ID C_DocTypeInventory_ID
	*/
	public void setC_DocTypeInventory_ID (int C_DocTypeInventory_ID)
	{
		if (C_DocTypeInventory_ID < 1)
			set_Value (COLUMNNAME_C_DocTypeInventory_ID, null);
		else
			set_Value (COLUMNNAME_C_DocTypeInventory_ID, Integer.valueOf(C_DocTypeInventory_ID));
	}

	/** Get C_DocTypeInventory_ID.
		@return C_DocTypeInventory_ID	  */
	public int getC_DocTypeInventory_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeInventory_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Approval Motive (Weight).
		@param FTU_WeightApprovalMotive_ID Approval Motive (Weight)
	*/
	public void setFTU_WeightApprovalMotive_ID (int FTU_WeightApprovalMotive_ID)
	{
		if (FTU_WeightApprovalMotive_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_WeightApprovalMotive_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_WeightApprovalMotive_ID, Integer.valueOf(FTU_WeightApprovalMotive_ID));
	}

	/** Get Approval Motive (Weight).
		@return Approval Motive (Weight)	  */
	public int getFTU_WeightApprovalMotive_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WeightApprovalMotive_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_WeightApprovalMotive_UU.
		@param FTU_WeightApprovalMotive_UU FTU_WeightApprovalMotive_UU
	*/
	public void setFTU_WeightApprovalMotive_UU (String FTU_WeightApprovalMotive_UU)
	{
		set_Value (COLUMNNAME_FTU_WeightApprovalMotive_UU, FTU_WeightApprovalMotive_UU);
	}

	/** Get FTU_WeightApprovalMotive_UU.
		@return FTU_WeightApprovalMotive_UU	  */
	public String getFTU_WeightApprovalMotive_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_WeightApprovalMotive_UU);
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

	/** Set Generate Loss.
		@param IsGenerateLoss Generate loss inventory
	*/
	public void setIsGenerateLoss (boolean IsGenerateLoss)
	{
		set_Value (COLUMNNAME_IsGenerateLoss, Boolean.valueOf(IsGenerateLoss));
	}

	/** Get Generate Loss.
		@return Generate loss inventory
	  */
	public boolean isGenerateLoss()
	{
		Object oo = get_Value(COLUMNNAME_IsGenerateLoss);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}
}