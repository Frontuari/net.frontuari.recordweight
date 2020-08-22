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

/** Generated Model for FTU_WeightScale
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_WeightScale extends PO implements I_FTU_WeightScale, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200504L;

    /** Standard Constructor */
    public X_FTU_WeightScale (Properties ctx, int FTU_WeightScale_ID, String trxName)
    {
      super (ctx, FTU_WeightScale_ID, trxName);
      /** if (FTU_WeightScale_ID == 0)
        {
			setC_UOM_ID (0);
			setFTU_ScreenConfig_ID (0);
			setFTU_SerialPortConfig_ID (0);
			setFTU_WeightScale_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_WeightScale (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_WeightScale[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException
    {
		return (org.compiere.model.I_C_UOM)MTable.get(getCtx(), org.compiere.model.I_C_UOM.Table_Name)
			.getPO(getC_UOM_ID(), get_TrxName());	}

	/** Set UOM.
		@param C_UOM_ID 
		Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else 
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	public net.frontuari.recordweight.model.I_FTU_ScreenConfig getFTU_ScreenConfig() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_ScreenConfig)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_ScreenConfig.Table_Name)
			.getPO(getFTU_ScreenConfig_ID(), get_TrxName());	}

	/** Set Screen Configuration.
		@param FTU_ScreenConfig_ID Screen Configuration	  */
	public void setFTU_ScreenConfig_ID (int FTU_ScreenConfig_ID)
	{
		if (FTU_ScreenConfig_ID < 1) 
			set_Value (COLUMNNAME_FTU_ScreenConfig_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_ScreenConfig_ID, Integer.valueOf(FTU_ScreenConfig_ID));
	}

	/** Get Screen Configuration.
		@return Screen Configuration	  */
	public int getFTU_ScreenConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_ScreenConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_SerialPortConfig getFTU_SerialPortConfig() throws RuntimeException
    {
		return (net.frontuari.recordweight.model.I_FTU_SerialPortConfig)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_SerialPortConfig.Table_Name)
			.getPO(getFTU_SerialPortConfig_ID(), get_TrxName());	}

	/** Set Serial Port Configuration.
		@param FTU_SerialPortConfig_ID 
		Serial Port Configuration example: COM1 or tty01, 9600...
	  */
	public void setFTU_SerialPortConfig_ID (int FTU_SerialPortConfig_ID)
	{
		if (FTU_SerialPortConfig_ID < 1) 
			set_Value (COLUMNNAME_FTU_SerialPortConfig_ID, null);
		else 
			set_Value (COLUMNNAME_FTU_SerialPortConfig_ID, Integer.valueOf(FTU_SerialPortConfig_ID));
	}

	/** Get Serial Port Configuration.
		@return Serial Port Configuration example: COM1 or tty01, 9600...
	  */
	public int getFTU_SerialPortConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_SerialPortConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Weight Scale.
		@param FTU_WeightScale_ID Weight Scale	  */
	public void setFTU_WeightScale_ID (int FTU_WeightScale_ID)
	{
		if (FTU_WeightScale_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_WeightScale_ID, Integer.valueOf(FTU_WeightScale_ID));
	}

	/** Get Weight Scale.
		@return Weight Scale	  */
	public int getFTU_WeightScale_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_WeightScale_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_WeightScale_UU.
		@param FTU_WeightScale_UU FTU_WeightScale_UU	  */
	public void setFTU_WeightScale_UU (String FTU_WeightScale_UU)
	{
		set_Value (COLUMNNAME_FTU_WeightScale_UU, FTU_WeightScale_UU);
	}

	/** Get FTU_WeightScale_UU.
		@return FTU_WeightScale_UU	  */
	public String getFTU_WeightScale_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_WeightScale_UU);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}
}