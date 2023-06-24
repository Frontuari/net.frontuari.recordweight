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

/** Generated Interface for HRS_AnalysisValuation
 *  @author iDempiere (generated) 
 *  @version Release 10
 */
@SuppressWarnings("all")
public interface I_HRS_AnalysisValuation 
{

    /** TableName=HRS_AnalysisValuation */
    public static final String Table_Name = "HRS_AnalysisValuation";

    /** AD_Table_ID=1000506 */
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

    /** Column name FTU_QualityParam_ID */
    public static final String COLUMNNAME_FTU_QualityParam_ID = "FTU_QualityParam_ID";

	/** Set Quality Param	  */
	public void setFTU_QualityParam_ID (int FTU_QualityParam_ID);

	/** Get Quality Param	  */
	public int getFTU_QualityParam_ID();

	public net.frontuari.recordweight.model.I_FTU_QualityParam getFTU_QualityParam() throws RuntimeException;

    /** Column name HRS_AnalysisValuation_ID */
    public static final String COLUMNNAME_HRS_AnalysisValuation_ID = "HRS_AnalysisValuation_ID";

	/** Set Analysis Valuation	  */
	public void setHRS_AnalysisValuation_ID (int HRS_AnalysisValuation_ID);

	/** Get Analysis Valuation	  */
	public int getHRS_AnalysisValuation_ID();

    /** Column name HRS_AnalysisValuation_UU */
    public static final String COLUMNNAME_HRS_AnalysisValuation_UU = "HRS_AnalysisValuation_UU";

	/** Set HRS_AnalysisValuation_UU	  */
	public void setHRS_AnalysisValuation_UU (String HRS_AnalysisValuation_UU);

	/** Get HRS_AnalysisValuation_UU	  */
	public String getHRS_AnalysisValuation_UU();

    /** Column name HRS_Analysis_ID */
    public static final String COLUMNNAME_HRS_Analysis_ID = "HRS_Analysis_ID";

	/** Set Analysis	  */
	public void setHRS_Analysis_ID (int HRS_Analysis_ID);

	/** Get Analysis	  */
	public int getHRS_Analysis_ID();

	public net.frontuari.recordweight.model.I_HRS_Analysis getHRS_Analysis() throws RuntimeException;

    /** Column name HumanResult */
    public static final String COLUMNNAME_HumanResult = "HumanResult";

	/** Set Human Result	  */
	public void setHumanResult (String HumanResult);

	/** Get Human Result	  */
	public String getHumanResult();

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

    /** Column name SystemResult */
    public static final String COLUMNNAME_SystemResult = "SystemResult";

	/** Set System Result	  */
	public void setSystemResult (String SystemResult);

	/** Get System Result	  */
	public String getSystemResult();

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
}
