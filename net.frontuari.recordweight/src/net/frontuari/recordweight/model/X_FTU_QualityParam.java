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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for FTU_QualityParam
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="FTU_QualityParam")
public class X_FTU_QualityParam extends PO implements I_FTU_QualityParam, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240917L;

    /** Standard Constructor */
    public X_FTU_QualityParam (Properties ctx, int FTU_QualityParam_ID, String trxName)
    {
      super (ctx, FTU_QualityParam_ID, trxName);
      /** if (FTU_QualityParam_ID == 0)
        {
			setCode (null);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setFTU_QualityParam_ID (0);
			setIsCalculated (false);
// N
			setIsQualityDiscount (false);
// N
			setIsShowReport (true);
// Y
			setIsUsedFor (null);
// BO
			setName (null);
			setisQualitativeAnalysis (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_FTU_QualityParam (Properties ctx, int FTU_QualityParam_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_QualityParam_ID, trxName, virtualColumns);
      /** if (FTU_QualityParam_ID == 0)
        {
			setCode (null);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setFTU_QualityParam_ID (0);
			setIsCalculated (false);
// N
			setIsQualityDiscount (false);
// N
			setIsShowReport (true);
// Y
			setIsUsedFor (null);
// BO
			setName (null);
			setisQualitativeAnalysis (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_FTU_QualityParam (Properties ctx, String FTU_QualityParam_UU, String trxName)
    {
      super (ctx, FTU_QualityParam_UU, trxName);
      /** if (FTU_QualityParam_UU == null)
        {
			setCode (null);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setFTU_QualityParam_ID (0);
			setIsCalculated (false);
// N
			setIsQualityDiscount (false);
// N
			setIsShowReport (true);
// Y
			setIsUsedFor (null);
// BO
			setName (null);
			setisQualitativeAnalysis (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_FTU_QualityParam (Properties ctx, String FTU_QualityParam_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_QualityParam_UU, trxName, virtualColumns);
      /** if (FTU_QualityParam_UU == null)
        {
			setCode (null);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setFTU_QualityParam_ID (0);
			setIsCalculated (false);
// N
			setIsQualityDiscount (false);
// N
			setIsShowReport (true);
// Y
			setIsUsedFor (null);
// BO
			setName (null);
			setisQualitativeAnalysis (false);
// N
        } */
    }

    /** Load Constructor */
    public X_FTU_QualityParam (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_QualityParam[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Column)MTable.get(getCtx(), org.compiere.model.I_AD_Column.Table_ID)
			.getPO(getAD_Column_ID(), get_TrxName());
	}

	/** Set Column.
		@param AD_Column_ID Column in the table
	*/
	public void setAD_Column_ID (int AD_Column_ID)
	{
		if (AD_Column_ID < 1)
			set_Value (COLUMNNAME_AD_Column_ID, null);
		else
			set_Value (COLUMNNAME_AD_Column_ID, Integer.valueOf(AD_Column_ID));
	}

	/** Get Column.
		@return Column in the table
	  */
	public int getAD_Column_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Column_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_ID)
			.getPO(getAD_Table_ID(), get_TrxName());
	}

	/** Set Table.
		@param AD_Table_ID Database Table information
	*/
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1)
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Validation code.
		@param Code Validation Code
	*/
	public void setCode (String Code)
	{
		set_Value (COLUMNNAME_Code, Code);
	}

	/** Get Validation code.
		@return Validation Code
	  */
	public String getCode()
	{
		return (String)get_Value(COLUMNNAME_Code);
	}

	/** Set Document Date.
		@param DateDoc Date of the Document
	*/
	public void setDateDoc (Timestamp DateDoc)
	{
		set_ValueNoCheck (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	public net.frontuari.recordweight.model.I_FTU_AnalysisType getFTU_AnalysisType() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_AnalysisType)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_AnalysisType.Table_ID)
			.getPO(getFTU_AnalysisType_ID(), get_TrxName());
	}

	/** Set Analysis_Type_ID.
		@param FTU_AnalysisType_ID Analysis_Type_ID
	*/
	public void setFTU_AnalysisType_ID (int FTU_AnalysisType_ID)
	{
		if (FTU_AnalysisType_ID < 1)
			set_Value (COLUMNNAME_FTU_AnalysisType_ID, null);
		else
			set_Value (COLUMNNAME_FTU_AnalysisType_ID, Integer.valueOf(FTU_AnalysisType_ID));
	}

	/** Get Analysis_Type_ID.
		@return Analysis_Type_ID	  */
	public int getFTU_AnalysisType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_AnalysisType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_FormuleFunction getFTU_FormuleFunction() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_FormuleFunction)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_FormuleFunction.Table_ID)
			.getPO(getFTU_FormuleFunction_ID(), get_TrxName());
	}

	/** Set FTU_Functions_Formule.
		@param FTU_FormuleFunction_ID FTU_Functions_Formule
	*/
	public void setFTU_FormuleFunction_ID (int FTU_FormuleFunction_ID)
	{
		if (FTU_FormuleFunction_ID < 1)
			set_Value (COLUMNNAME_FTU_FormuleFunction_ID, null);
		else
			set_Value (COLUMNNAME_FTU_FormuleFunction_ID, Integer.valueOf(FTU_FormuleFunction_ID));
	}

	/** Get FTU_Functions_Formule.
		@return FTU_Functions_Formule	  */
	public int getFTU_FormuleFunction_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_FormuleFunction_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_QualityParam getFTU_Parent() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_QualityParam)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_QualityParam.Table_ID)
			.getPO(getFTU_Parent_ID(), get_TrxName());
	}

	/** Set FTU_Parent_ID.
		@param FTU_Parent_ID FTU_Parent_ID
	*/
	public void setFTU_Parent_ID (int FTU_Parent_ID)
	{
		if (FTU_Parent_ID < 1)
			set_Value (COLUMNNAME_FTU_Parent_ID, null);
		else
			set_Value (COLUMNNAME_FTU_Parent_ID, Integer.valueOf(FTU_Parent_ID));
	}

	/** Get FTU_Parent_ID.
		@return FTU_Parent_ID	  */
	public int getFTU_Parent_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Parent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_ProductAnalysis getFTU_ProductAnalysis() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_ProductAnalysis)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_ProductAnalysis.Table_ID)
			.getPO(getFTU_ProductAnalysis_ID(), get_TrxName());
	}

	/** Set Product Analysis.
		@param FTU_ProductAnalysis_ID Product Analysis
	*/
	public void setFTU_ProductAnalysis_ID (int FTU_ProductAnalysis_ID)
	{
		if (FTU_ProductAnalysis_ID < 1)
			set_Value (COLUMNNAME_FTU_ProductAnalysis_ID, null);
		else
			set_Value (COLUMNNAME_FTU_ProductAnalysis_ID, Integer.valueOf(FTU_ProductAnalysis_ID));
	}

	/** Get Product Analysis.
		@return Product Analysis
	  */
	public int getFTU_ProductAnalysis_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_ProductAnalysis_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quality Param.
		@param FTU_QualityParam_ID Quality Param
	*/
	public void setFTU_QualityParam_ID (int FTU_QualityParam_ID)
	{
		if (FTU_QualityParam_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_QualityParam_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_QualityParam_ID, Integer.valueOf(FTU_QualityParam_ID));
	}

	/** Get Quality Param.
		@return Quality Param	  */
	public int getFTU_QualityParam_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_QualityParam_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_Quality_Param_UU.
		@param FTU_QualityParam_UU FTU_Quality_Param_UU
	*/
	public void setFTU_QualityParam_UU (String FTU_QualityParam_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_QualityParam_UU, FTU_QualityParam_UU);
	}

	/** Get FTU_Quality_Param_UU.
		@return FTU_Quality_Param_UU	  */
	public String getFTU_QualityParam_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_QualityParam_UU);
	}

	/** Set Human Code.
		@param HumanCode Human Code
	*/
	public void setHumanCode (String HumanCode)
	{
		set_Value (COLUMNNAME_HumanCode, HumanCode);
	}

	/** Get Human Code.
		@return Human Code	  */
	public String getHumanCode()
	{
		return (String)get_Value(COLUMNNAME_HumanCode);
	}

	/** Set Calculated.
		@param IsCalculated The value is calculated by the system
	*/
	public void setIsCalculated (boolean IsCalculated)
	{
		set_Value (COLUMNNAME_IsCalculated, Boolean.valueOf(IsCalculated));
	}

	/** Get Calculated.
		@return The value is calculated by the system
	  */
	public boolean isCalculated()
	{
		Object oo = get_Value(COLUMNNAME_IsCalculated);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Quality Discount.
		@param IsQualityDiscount Quality Discount
	*/
	public void setIsQualityDiscount (boolean IsQualityDiscount)
	{
		set_Value (COLUMNNAME_IsQualityDiscount, Boolean.valueOf(IsQualityDiscount));
	}

	/** Get Quality Discount.
		@return Quality Discount	  */
	public boolean isQualityDiscount()
	{
		Object oo = get_Value(COLUMNNAME_IsQualityDiscount);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Show In Report.
		@param IsShowReport Show In Report
	*/
	public void setIsShowReport (boolean IsShowReport)
	{
		set_Value (COLUMNNAME_IsShowReport, Boolean.valueOf(IsShowReport));
	}

	/** Get Show In Report.
		@return Show In Report	  */
	public boolean isShowReport()
	{
		Object oo = get_Value(COLUMNNAME_IsShowReport);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Both = BO */
	public static final String ISUSEDFOR_Both = "BO";
	/** Laboratory Analysis = LA */
	public static final String ISUSEDFOR_LaboratoryAnalysis = "LA";
	/** Quality Analysis = QA */
	public static final String ISUSEDFOR_QualityAnalysis = "QA";
	/** Set Used for.
		@param IsUsedFor Used for
	*/
	public void setIsUsedFor (String IsUsedFor)
	{

		set_Value (COLUMNNAME_IsUsedFor, IsUsedFor);
	}

	/** Get Used for.
		@return Used for	  */
	public String getIsUsedFor()
	{
		return (String)get_Value(COLUMNNAME_IsUsedFor);
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

	/** Set MeasurementParameter.
		@param MeasurementParameter MeasurementParameter
	*/
	public void setMeasurementParameter (String MeasurementParameter)
	{
		set_Value (COLUMNNAME_MeasurementParameter, MeasurementParameter);
	}

	/** Get MeasurementParameter.
		@return MeasurementParameter	  */
	public String getMeasurementParameter()
	{
		return (String)get_Value(COLUMNNAME_MeasurementParameter);
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

	/** Set Reference Value.
		@param ReferenceValue Reference Value
	*/
	public void setReferenceValue (String ReferenceValue)
	{
		set_Value (COLUMNNAME_ReferenceValue, ReferenceValue);
	}

	/** Get Reference Value.
		@return Reference Value	  */
	public String getReferenceValue()
	{
		return (String)get_Value(COLUMNNAME_ReferenceValue);
	}

	/** Set Result.
		@param Result Result of the action taken
	*/
	public void setResult (String Result)
	{
		set_Value (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	public String getResult()
	{
		return (String)get_Value(COLUMNNAME_Result);
	}

	/** Set Sequence.
		@param SeqNo Method of ordering records; lowest number comes first
	*/
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Translate Code for Human.
		@param TranslateCode Translate Code for Human
	*/
	public void setTranslateCode (String TranslateCode)
	{
		set_Value (COLUMNNAME_TranslateCode, TranslateCode);
	}

	/** Get Translate Code for Human.
		@return Translate Code for Human	  */
	public String getTranslateCode()
	{
		return (String)get_Value(COLUMNNAME_TranslateCode);
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set isQualitativeAnalysis.
		@param isQualitativeAnalysis isQualitativeAnalysis
	*/
	public void setisQualitativeAnalysis (boolean isQualitativeAnalysis)
	{
		set_Value (COLUMNNAME_isQualitativeAnalysis, Boolean.valueOf(isQualitativeAnalysis));
	}

	/** Get isQualitativeAnalysis.
		@return isQualitativeAnalysis	  */
	public boolean isQualitativeAnalysis()
	{
		Object oo = get_Value(COLUMNNAME_isQualitativeAnalysis);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}