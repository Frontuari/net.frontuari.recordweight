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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for FTU_SLLine
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_SLLine")
public class X_FTU_SLLine extends PO implements I_FTU_SLLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240116L;

    /** Standard Constructor */
    public X_FTU_SLLine (Properties ctx, int FTU_SLLine_ID, String trxName)
    {
      super (ctx, FTU_SLLine_ID, trxName);
      /** if (FTU_SLLine_ID == 0)
        {
			setAmount (Env.ZERO);
			setFTU_ShipperLiquidation_ID (0);
			setFTU_SLLine_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_FTU_SLLine (Properties ctx, int FTU_SLLine_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_SLLine_ID, trxName, virtualColumns);
      /** if (FTU_SLLine_ID == 0)
        {
			setAmount (Env.ZERO);
			setFTU_ShipperLiquidation_ID (0);
			setFTU_SLLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_FTU_SLLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_SLLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amount Amount in a defined currency
	*/
	public void setAmount (BigDecimal Amount)
	{
		set_ValueNoCheck (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
	{
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_ID)
			.getPO(getC_Invoice_ID(), get_TrxName());
	}

	/** Set Invoice.
		@param C_Invoice_ID Invoice Identifier
	*/
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1)
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
	{
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_ID)
			.getPO(getC_Order_ID(), get_TrxName());
	}

	/** Set Order.
		@param C_Order_ID Order
	*/
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1)
			set_Value (COLUMNNAME_C_Order_ID, null);
		else
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
	{
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_ID)
			.getPO(getC_Payment_ID(), get_TrxName());
	}

	/** Set Payment.
		@param C_Payment_ID Payment identifier
	*/
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1)
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Deduction by AP = 01 */
	public static final String DEDUCTIONTYPE_DeductionByAP = "01";
	/** Deduction by AC = 02 */
	public static final String DEDUCTIONTYPE_DeductionByAC = "02";
	/** PrePayments = 03 */
	public static final String DEDUCTIONTYPE_PrePayments = "03";
	/** Set Deduction Type.
		@param DeductionType Deduction Type
	*/
	public void setDeductionType (String DeductionType)
	{

		set_Value (COLUMNNAME_DeductionType, DeductionType);
	}

	/** Get Deduction Type.
		@return Deduction Type	  */
	public String getDeductionType()
	{
		return (String)get_Value(COLUMNNAME_DeductionType);
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

	public net.frontuari.recordweight.model.I_FTU_FreightCost getFTU_FreightCost() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_FreightCost)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_FreightCost.Table_ID)
			.getPO(getFTU_FreightCost_ID(), get_TrxName());
	}

	/** Set Bill of Lading.
		@param FTU_FreightCost_ID Bill of Lading
	*/
	public void setFTU_FreightCost_ID (int FTU_FreightCost_ID)
	{
		if (FTU_FreightCost_ID < 1)
			set_Value (COLUMNNAME_FTU_FreightCost_ID, null);
		else
			set_Value (COLUMNNAME_FTU_FreightCost_ID, Integer.valueOf(FTU_FreightCost_ID));
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

	public net.frontuari.recordweight.model.I_FTU_ShipperLiquidation getFTU_ShipperLiquidation() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_ShipperLiquidation)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_ShipperLiquidation.Table_ID)
			.getPO(getFTU_ShipperLiquidation_ID(), get_TrxName());
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

	/** Set Shipper Liquidation Line.
		@param FTU_SLLine_ID Shipper Liquidation Line
	*/
	public void setFTU_SLLine_ID (int FTU_SLLine_ID)
	{
		if (FTU_SLLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_SLLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_SLLine_ID, Integer.valueOf(FTU_SLLine_ID));
	}

	/** Get Shipper Liquidation Line.
		@return Shipper Liquidation Line
	  */
	public int getFTU_SLLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_SLLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_SL_Line_UU.
		@param FTU_SLLine_UU FTU_SL_Line_UU
	*/
	public void setFTU_SLLine_UU (String FTU_SLLine_UU)
	{
		set_Value (COLUMNNAME_FTU_SLLine_UU, FTU_SLLine_UU);
	}

	/** Get FTU_SL_Line_UU.
		@return FTU_SL_Line_UU	  */
	public String getFTU_SLLine_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_SLLine_UU);
	}

	public org.compiere.model.I_M_Inventory getM_Inventory() throws RuntimeException
	{
		return (org.compiere.model.I_M_Inventory)MTable.get(getCtx(), org.compiere.model.I_M_Inventory.Table_ID)
			.getPO(getM_Inventory_ID(), get_TrxName());
	}

	/** Set Phys.Inventory.
		@param M_Inventory_ID Parameters for a Physical Inventory
	*/
	public void setM_Inventory_ID (int M_Inventory_ID)
	{
		if (M_Inventory_ID < 1)
			set_Value (COLUMNNAME_M_Inventory_ID, null);
		else
			set_Value (COLUMNNAME_M_Inventory_ID, Integer.valueOf(M_Inventory_ID));
	}

	/** Get Phys.Inventory.
		@return Parameters for a Physical Inventory
	  */
	public int getM_Inventory_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Inventory_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}