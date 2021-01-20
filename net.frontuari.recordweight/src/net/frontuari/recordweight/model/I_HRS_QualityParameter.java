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

/** Generated Interface for HRS_QualityParameter
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_HRS_QualityParameter 
{

    /** TableName=HRS_QualityParameter */
    public static final String Table_Name = "HRS_QualityParameter";

    /** AD_Table_ID=1000043 */
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

    /** Column name AnalysisNumber */
    public static final String COLUMNNAME_AnalysisNumber = "AnalysisNumber";

	/** Set Analysis Number	  */
	public void setAnalysisNumber (int AnalysisNumber);

	/** Get Analysis Number	  */
	public int getAnalysisNumber();

    /** Column name AttributeValue */
    public static final String COLUMNNAME_AttributeValue = "AttributeValue";

	/** Set Attribute Value.
	  * Value of the Attribute
	  */
	public void setAttributeValue (String AttributeValue);

	/** Get Attribute Value.
	  * Value of the Attribute
	  */
	public String getAttributeValue();

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

    /** Column name DegreeFactor */
    public static final String COLUMNNAME_DegreeFactor = "DegreeFactor";

	/** Set Degree Factor	  */
	public void setDegreeFactor (int DegreeFactor);

	/** Get Degree Factor	  */
	public int getDegreeFactor();

    /** Column name Factor1 */
    public static final String COLUMNNAME_Factor1 = "Factor1";

	/** Set Factor1	  */
	public void setFactor1 (BigDecimal Factor1);

	/** Get Factor1	  */
	public BigDecimal getFactor1();

    /** Column name Factor2 */
    public static final String COLUMNNAME_Factor2 = "Factor2";

	/** Set Factor2	  */
	public void setFactor2 (BigDecimal Factor2);

	/** Get Factor2	  */
	public BigDecimal getFactor2();

    /** Column name Factor3 */
    public static final String COLUMNNAME_Factor3 = "Factor3";

	/** Set Factor3	  */
	public void setFactor3 (BigDecimal Factor3);

	/** Get Factor3	  */
	public BigDecimal getFactor3();

    /** Column name HRS_QualityParameter_ID */
    public static final String COLUMNNAME_HRS_QualityParameter_ID = "HRS_QualityParameter_ID";

	/** Set Quality Parameter	  */
	public void setHRS_QualityParameter_ID (int HRS_QualityParameter_ID);

	/** Get Quality Parameter	  */
	public int getHRS_QualityParameter_ID();

    /** Column name HRS_QualityParameter_UU */
    public static final String COLUMNNAME_HRS_QualityParameter_UU = "HRS_QualityParameter_UU";

	/** Set HRS_QualityParameter_UU	  */
	public void setHRS_QualityParameter_UU (String HRS_QualityParameter_UU);

	/** Get HRS_QualityParameter_UU	  */
	public String getHRS_QualityParameter_UU();

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

    /** Column name LowerLimit */
    public static final String COLUMNNAME_LowerLimit = "LowerLimit";

	/** Set Lower Limit	  */
	public void setLowerLimit (BigDecimal LowerLimit);

	/** Get Lower Limit	  */
	public BigDecimal getLowerLimit();

    /** Column name M_Attribute_ID */
    public static final String COLUMNNAME_M_Attribute_ID = "M_Attribute_ID";

	/** Set Attribute.
	  * Product Attribute
	  */
	public void setM_Attribute_ID (int M_Attribute_ID);

	/** Get Attribute.
	  * Product Attribute
	  */
	public int getM_Attribute_ID();

	public org.compiere.model.I_M_Attribute getM_Attribute() throws RuntimeException;

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

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

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

    /** Column name UpperLimit */
    public static final String COLUMNNAME_UpperLimit = "UpperLimit";

	/** Set Upper Limit	  */
	public void setUpperLimit (BigDecimal UpperLimit);

	/** Get Upper Limit	  */
	public BigDecimal getUpperLimit();

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
