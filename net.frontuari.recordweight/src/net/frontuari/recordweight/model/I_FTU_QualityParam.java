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

/** Generated Interface for FTU_QualityParam
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_FTU_QualityParam 
{

    /** TableName=FTU_QualityParam */
    public static final String Table_Name = "FTU_QualityParam";

    /** AD_Table_ID=1000480 */
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

    /** Column name AD_Column_ID */
    public static final String COLUMNNAME_AD_Column_ID = "AD_Column_ID";

	/** Set Column.
	  * Column in the table
	  */
	public void setAD_Column_ID (int AD_Column_ID);

	/** Get Column.
	  * Column in the table
	  */
	public int getAD_Column_ID();

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException;

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

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException;

    /** Column name Code */
    public static final String COLUMNNAME_Code = "Code";

	/** Set Validation code.
	  * Validation Code
	  */
	public void setCode (String Code);

	/** Get Validation code.
	  * Validation Code
	  */
	public String getCode();

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

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

    /** Column name FTU_AnalysisType_ID */
    public static final String COLUMNNAME_FTU_AnalysisType_ID = "FTU_AnalysisType_ID";

	/** Set Analysis_Type_ID	  */
	public void setFTU_AnalysisType_ID (int FTU_AnalysisType_ID);

	/** Get Analysis_Type_ID	  */
	public int getFTU_AnalysisType_ID();

	public net.frontuari.recordweight.model.I_FTU_AnalysisType getFTU_AnalysisType() throws RuntimeException;

    /** Column name FTU_FormuleFunction_ID */
    public static final String COLUMNNAME_FTU_FormuleFunction_ID = "FTU_FormuleFunction_ID";

	/** Set FTU_Functions_Formule	  */
	public void setFTU_FormuleFunction_ID (int FTU_FormuleFunction_ID);

	/** Get FTU_Functions_Formule	  */
	public int getFTU_FormuleFunction_ID();

	public net.frontuari.recordweight.model.I_FTU_FormuleFunction getFTU_FormuleFunction() throws RuntimeException;

    /** Column name FTU_Parent_ID */
    public static final String COLUMNNAME_FTU_Parent_ID = "FTU_Parent_ID";

	/** Set FTU_Parent_ID	  */
	public void setFTU_Parent_ID (int FTU_Parent_ID);

	/** Get FTU_Parent_ID	  */
	public int getFTU_Parent_ID();

	public net.frontuari.recordweight.model.I_FTU_QualityParam getFTU_Parent() throws RuntimeException;

    /** Column name FTU_QualityParam_ID */
    public static final String COLUMNNAME_FTU_QualityParam_ID = "FTU_QualityParam_ID";

	/** Set Quality Param	  */
	public void setFTU_QualityParam_ID (int FTU_QualityParam_ID);

	/** Get Quality Param	  */
	public int getFTU_QualityParam_ID();

    /** Column name FTU_QualityParam_UU */
    public static final String COLUMNNAME_FTU_QualityParam_UU = "FTU_QualityParam_UU";

	/** Set FTU_Quality_Param_UU	  */
	public void setFTU_QualityParam_UU (String FTU_QualityParam_UU);

	/** Get FTU_Quality_Param_UU	  */
	public String getFTU_QualityParam_UU();

    /** Column name HumanCode */
    public static final String COLUMNNAME_HumanCode = "HumanCode";

	/** Set Human Code	  */
	public void setHumanCode (String HumanCode);

	/** Get Human Code	  */
	public String getHumanCode();

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

    /** Column name isQualitativeAnalysis */
    public static final String COLUMNNAME_isQualitativeAnalysis = "isQualitativeAnalysis";

	/** Set isQualitativeAnalysis	  */
	public void setisQualitativeAnalysis (boolean isQualitativeAnalysis);

	/** Get isQualitativeAnalysis	  */
	public boolean isQualitativeAnalysis();

    /** Column name IsQualityDiscount */
    public static final String COLUMNNAME_IsQualityDiscount = "IsQualityDiscount";

	/** Set Quality Discount	  */
	public void setIsQualityDiscount (boolean IsQualityDiscount);

	/** Get Quality Discount	  */
	public boolean isQualityDiscount();

    /** Column name IsUsedFor */
    public static final String COLUMNNAME_IsUsedFor = "IsUsedFor";

	/** Set Used for	  */
	public void setIsUsedFor (String IsUsedFor);

	/** Get Used for	  */
	public String getIsUsedFor();

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

    /** Column name Result */
    public static final String COLUMNNAME_Result = "Result";

	/** Set Result.
	  * Result of the action taken
	  */
	public void setResult (String Result);

	/** Get Result.
	  * Result of the action taken
	  */
	public String getResult();

    /** Column name TranslateCode */
    public static final String COLUMNNAME_TranslateCode = "TranslateCode";

	/** Set Translate Code for Human	  */
	public void setTranslateCode (String TranslateCode);

	/** Get Translate Code for Human	  */
	public String getTranslateCode();

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
