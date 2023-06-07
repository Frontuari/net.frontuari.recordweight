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

/** Generated Model for FTU_SerialPortConfig
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="FTU_SerialPortConfig")
public class X_FTU_SerialPortConfig extends PO implements I_FTU_SerialPortConfig, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_FTU_SerialPortConfig (Properties ctx, int FTU_SerialPortConfig_ID, String trxName)
    {
      super (ctx, FTU_SerialPortConfig_ID, trxName);
      /** if (FTU_SerialPortConfig_ID == 0)
        {
			setBauds (null);
			setDataBits (null);
			setFlowControl (null);
			setFTU_SerialPortConfig_ID (0);
			setName (null);
			setParity (null);
			setSerialPort (null);
			setStopBits (null);
        } */
    }

    /** Standard Constructor */
    public X_FTU_SerialPortConfig (Properties ctx, int FTU_SerialPortConfig_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, FTU_SerialPortConfig_ID, trxName, virtualColumns);
      /** if (FTU_SerialPortConfig_ID == 0)
        {
			setBauds (null);
			setDataBits (null);
			setFlowControl (null);
			setFTU_SerialPortConfig_ID (0);
			setName (null);
			setParity (null);
			setSerialPort (null);
			setStopBits (null);
        } */
    }

    /** Load Constructor */
    public X_FTU_SerialPortConfig (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_FTU_SerialPortConfig[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Bauds.
		@param Bauds Bauds
	*/
	public void setBauds (String Bauds)
	{
		set_Value (COLUMNNAME_Bauds, Bauds);
	}

	/** Get Bauds.
		@return Bauds	  */
	public String getBauds()
	{
		return (String)get_Value(COLUMNNAME_Bauds);
	}

	/** Set DataBits.
		@param DataBits DataBits
	*/
	public void setDataBits (String DataBits)
	{
		set_Value (COLUMNNAME_DataBits, DataBits);
	}

	/** Get DataBits.
		@return DataBits	  */
	public String getDataBits()
	{
		return (String)get_Value(COLUMNNAME_DataBits);
	}

	/** Set FlowControl.
		@param FlowControl FlowControl
	*/
	public void setFlowControl (String FlowControl)
	{
		set_Value (COLUMNNAME_FlowControl, FlowControl);
	}

	/** Get FlowControl.
		@return FlowControl	  */
	public String getFlowControl()
	{
		return (String)get_Value(COLUMNNAME_FlowControl);
	}

	/** Set Serial Port Configuration.
		@param FTU_SerialPortConfig_ID Serial Port Configuration example: COM1 or tty01, 9600...
	*/
	public void setFTU_SerialPortConfig_ID (int FTU_SerialPortConfig_ID)
	{
		if (FTU_SerialPortConfig_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_SerialPortConfig_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_SerialPortConfig_ID, Integer.valueOf(FTU_SerialPortConfig_ID));
	}

	/** Get Serial Port Configuration.
		@return Serial Port Configuration example: COM1 or tty01, 9600...
	  */
	public int getFTU_SerialPortConfig_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_SerialPortConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_SerialPortConfig_UU.
		@param FTU_SerialPortConfig_UU FTU_SerialPortConfig_UU
	*/
	public void setFTU_SerialPortConfig_UU (String FTU_SerialPortConfig_UU)
	{
		set_Value (COLUMNNAME_FTU_SerialPortConfig_UU, FTU_SerialPortConfig_UU);
	}

	/** Get FTU_SerialPortConfig_UU.
		@return FTU_SerialPortConfig_UU	  */
	public String getFTU_SerialPortConfig_UU()
	{
		return (String)get_Value(COLUMNNAME_FTU_SerialPortConfig_UU);
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

	/** Set Parity.
		@param Parity Parity
	*/
	public void setParity (String Parity)
	{
		set_Value (COLUMNNAME_Parity, Parity);
	}

	/** Get Parity.
		@return Parity	  */
	public String getParity()
	{
		return (String)get_Value(COLUMNNAME_Parity);
	}

	/** Set SerialPort.
		@param SerialPort SerialPort
	*/
	public void setSerialPort (String SerialPort)
	{
		set_Value (COLUMNNAME_SerialPort, SerialPort);
	}

	/** Get SerialPort.
		@return SerialPort	  */
	public String getSerialPort()
	{
		return (String)get_Value(COLUMNNAME_SerialPort);
	}

	/** Set StopBits.
		@param StopBits StopBits
	*/
	public void setStopBits (String StopBits)
	{
		set_Value (COLUMNNAME_StopBits, StopBits);
	}

	/** Get StopBits.
		@return StopBits	  */
	public String getStopBits()
	{
		return (String)get_Value(COLUMNNAME_StopBits);
	}
}