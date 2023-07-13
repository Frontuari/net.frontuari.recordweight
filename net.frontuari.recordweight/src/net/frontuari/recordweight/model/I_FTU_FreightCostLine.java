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

/** Generated Interface for FTU_FreightCostLine
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_FTU_FreightCostLine 
{

    /** TableName=FTU_FreightCostLine */
    public static final String Table_Name = "FTU_FreightCostLine";

    /** AD_Table_ID=1000492 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

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

    /** Column name C_Charge_ID */
    public static final String COLUMNNAME_C_Charge_ID = "C_Charge_ID";

	/** Set Charge.
	  * Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID);

	/** Get Charge.
	  * Additional document charges
	  */
	public int getC_Charge_ID();

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException;

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

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

    /** Column name Costs */
    public static final String COLUMNNAME_Costs = "Costs";

	/** Set Costs.
	  * Costs in accounting currency
	  */
	public void setCosts (BigDecimal Costs);

	/** Get Costs.
	  * Costs in accounting currency
	  */
	public BigDecimal getCosts();

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

    /** Column name DiscountWeight */
    public static final String COLUMNNAME_DiscountWeight = "DiscountWeight";

	/** Set Discount Weight	  */
	public void setDiscountWeight (BigDecimal DiscountWeight);

	/** Get Discount Weight	  */
	public BigDecimal getDiscountWeight();

    /** Column name FTU_DeliveryRute_ID */
    public static final String COLUMNNAME_FTU_DeliveryRute_ID = "FTU_DeliveryRute_ID";

	/** Set FTU_DeliveryRute_ID	  */
	public void setFTU_DeliveryRute_ID (int FTU_DeliveryRute_ID);

	/** Get FTU_DeliveryRute_ID	  */
	public int getFTU_DeliveryRute_ID();

	public net.frontuari.recordweight.model.I_FTU_DeliveryRute getFTU_DeliveryRute() throws RuntimeException;

    /** Column name FTU_FreightCostLine_ID */
    public static final String COLUMNNAME_FTU_FreightCostLine_ID = "FTU_FreightCostLine_ID";

	/** Set Bill of Lading Line	  */
	public void setFTU_FreightCostLine_ID (int FTU_FreightCostLine_ID);

	/** Get Bill of Lading Line	  */
	public int getFTU_FreightCostLine_ID();

    /** Column name FTU_FreightCost_ID */
    public static final String COLUMNNAME_FTU_FreightCost_ID = "FTU_FreightCost_ID";

	/** Set Bill of Lading	  */
	public void setFTU_FreightCost_ID (int FTU_FreightCost_ID);

	/** Get Bill of Lading	  */
	public int getFTU_FreightCost_ID();

	public net.frontuari.recordweight.model.I_FTU_FreightCost getFTU_FreightCost() throws RuntimeException;

    /** Column name FTU_RecordWeight_ID */
    public static final String COLUMNNAME_FTU_RecordWeight_ID = "FTU_RecordWeight_ID";

	/** Set Record Weight	  */
	public void setFTU_RecordWeight_ID (int FTU_RecordWeight_ID);

	/** Get Record Weight	  */
	public int getFTU_RecordWeight_ID();

	public net.frontuari.recordweight.model.I_FTU_RecordWeight getFTU_RecordWeight() throws RuntimeException;

    /** Column name Factor */
    public static final String COLUMNNAME_Factor = "Factor";

	/** Set Factor.
	  * Scaling factor.
	  */
	public void setFactor (BigDecimal Factor);

	/** Get Factor.
	  * Scaling factor.
	  */
	public BigDecimal getFactor();

    /** Column name FinalPrice */
    public static final String COLUMNNAME_FinalPrice = "FinalPrice";

	/** Set Final Price	  */
	public void setFinalPrice (BigDecimal FinalPrice);

	/** Get Final Price	  */
	public BigDecimal getFinalPrice();

    /** Column name M_InOut_ID */
    public static final String COLUMNNAME_M_InOut_ID = "M_InOut_ID";

	/** Set Shipment/Receipt.
	  * Material Shipment Document
	  */
	public void setM_InOut_ID (int M_InOut_ID);

	/** Get Shipment/Receipt.
	  * Material Shipment Document
	  */
	public int getM_InOut_ID();

	public org.compiere.model.I_M_InOut getM_InOut() throws RuntimeException;

    /** Column name Price */
    public static final String COLUMNNAME_Price = "Price";

	/** Set Price.
	  * Price
	  */
	public void setPrice (BigDecimal Price);

	/** Get Price.
	  * Price
	  */
	public BigDecimal getPrice();

    /** Column name PriceActual */
    public static final String COLUMNNAME_PriceActual = "PriceActual";

	/** Set Unit Price.
	  * Actual Price 
	  */
	public void setPriceActual (BigDecimal PriceActual);

	/** Get Unit Price.
	  * Actual Price 
	  */
	public BigDecimal getPriceActual();

    /** Column name Rate */
    public static final String COLUMNNAME_Rate = "Rate";

	/** Set Rate.
	  * Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate);

	/** Get Rate.
	  * Rate or Tax or Exchange
	  */
	public BigDecimal getRate();

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

    /** Column name ValueMax */
    public static final String COLUMNNAME_ValueMax = "ValueMax";

	/** Set Max. Value.
	  * Maximum Value for a field
	  */
	public void setValueMax (BigDecimal ValueMax);

	/** Get Max. Value.
	  * Maximum Value for a field
	  */
	public BigDecimal getValueMax();

    /** Column name ValueMin */
    public static final String COLUMNNAME_ValueMin = "ValueMin";

	/** Set Min. Value.
	  * Minimum Value for a field
	  */
	public void setValueMin (BigDecimal ValueMin);

	/** Get Min. Value.
	  * Minimum Value for a field
	  */
	public BigDecimal getValueMin();

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
