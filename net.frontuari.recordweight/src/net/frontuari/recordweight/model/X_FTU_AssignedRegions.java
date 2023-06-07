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

/** Generated Model for FTU_AssignedRegions
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_AssignedRegions")
public class X_FTU_AssignedRegions extends PO implements I_FTU_AssignedRegions, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_FTU_AssignedRegions (Properties ctx, int FTU_AssignedRegions_ID, String trxName)
    {
      super (ctx, FTU_AssignedRegions_ID, trxName);
      /** if (FTU_AssignedRegions_ID == 0)
        {
        } */
    }

    /** Standard Constructor */
    public X_FTU_AssignedRegions (Properties ctx, int FTU_AssignedRegions_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_AssignedRegions_ID, trxName, virtualColumns);
      /** if (FTU_AssignedRegions_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_FTU_AssignedRegions (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_AssignedRegions[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
	{
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_ID)
			.getPO(getC_SalesRegion_ID(), get_TrxName());
	}

	/** Set Sales Region.
		@param C_SalesRegion_ID Sales coverage region
	*/
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1)
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Assigned Regions.
		@param FTU_AssignedRegions_ID Assigned Regions
	*/
	public void setFTU_AssignedRegions_ID (int FTU_AssignedRegions_ID)
	{
		if (FTU_AssignedRegions_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_AssignedRegions_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_AssignedRegions_ID, Integer.valueOf(FTU_AssignedRegions_ID));
	}

	/** Get Assigned Regions.
		@return Assigned Regions	  */
	public int getFTU_AssignedRegions_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_AssignedRegions_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}