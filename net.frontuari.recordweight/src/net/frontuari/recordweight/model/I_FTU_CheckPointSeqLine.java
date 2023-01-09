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

/** Generated Interface for FTU_CheckPointSeqLine
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_FTU_CheckPointSeqLine 
{

    /** TableName=FTU_CheckPointSeqLine */
    public static final String Table_Name = "FTU_CheckPointSeqLine";

    /** AD_Table_ID=1000063 */
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

    /** Column name FTU_CheckPoint_ID */
    public static final String COLUMNNAME_FTU_CheckPoint_ID = "FTU_CheckPoint_ID";

	/** Set FTU CheckPoint	  */
	public void setFTU_CheckPoint_ID (int FTU_CheckPoint_ID);

	/** Get FTU CheckPoint	  */
	public int getFTU_CheckPoint_ID();

    /** Column name FTU_CheckPointSeq_ID */
    public static final String COLUMNNAME_FTU_CheckPointSeq_ID = "FTU_CheckPointSeq_ID";

	/** Set FTU Check Point Seq	  */
	public void setFTU_CheckPointSeq_ID (int FTU_CheckPointSeq_ID);

	/** Get FTU Check Point Seq	  */
	public int getFTU_CheckPointSeq_ID();

    /** Column name FTU_CheckPointSeqLine_ID */
    public static final String COLUMNNAME_FTU_CheckPointSeqLine_ID = "FTU_CheckPointSeqLine_ID";

	/** Set FTU Check Point Seq Line	  */
	public void setFTU_CheckPointSeqLine_ID (int FTU_CheckPointSeqLine_ID);

	/** Get FTU Check Point Seq Line	  */
	public int getFTU_CheckPointSeqLine_ID();

    /** Column name FTU_CheckPointSeqLine_UU */
    public static final String COLUMNNAME_FTU_CheckPointSeqLine_UU = "FTU_CheckPointSeqLine_UU";

	/** Set FTU_CheckPointSeqLine_UU	  */
	public void setFTU_CheckPointSeqLine_UU (String FTU_CheckPointSeqLine_UU);

	/** Get FTU_CheckPointSeqLine_UU	  */
	public String getFTU_CheckPointSeqLine_UU();

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

    /** Column name LineNo */
    public static final String COLUMNNAME_LineNo = "LineNo";

	/** Set Line.
	  * Line No
	  */
	public void setLineNo (int LineNo);

	/** Get Line.
	  * Line No
	  */
	public int getLineNo();

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
