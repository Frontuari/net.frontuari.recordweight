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

/** Generated Model for FTU_CheckPointSeqLine
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_FTU_CheckPointSeqLine extends PO implements I_FTU_CheckPointSeqLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200518L;

    /** Standard Constructor */
    public X_FTU_CheckPointSeqLine (Properties ctx, int FTU_CheckPointSeqLine_ID, String trxName)
    {
      super (ctx, FTU_CheckPointSeqLine_ID, trxName);
      /** if (FTU_CheckPointSeqLine_ID == 0)
        {
			setFTU_CheckPoint_ID (0);
			setFTU_CheckPointSeq_ID (0);
			setLineNo (0);
        } */
    }

    /** Load Constructor */
    public X_FTU_CheckPointSeqLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_FTU_CheckPointSeqLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set FTU CheckPoint.
		@param FTU_CheckPoint_ID FTU CheckPoint	  */
	public void setFTU_CheckPoint_ID (int FTU_CheckPoint_ID)
	{
		if (FTU_CheckPoint_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPoint_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPoint_ID, Integer.valueOf(FTU_CheckPoint_ID));
	}

	/** Get FTU CheckPoint.
		@return FTU CheckPoint	  */
	public int getFTU_CheckPoint_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_CheckPoint_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU Check Point Seq.
		@param FTU_CheckPointSeq_ID FTU Check Point Seq	  */
	public void setFTU_CheckPointSeq_ID (int FTU_CheckPointSeq_ID)
	{
		if (FTU_CheckPointSeq_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPointSeq_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPointSeq_ID, Integer.valueOf(FTU_CheckPointSeq_ID));
	}

	/** Get FTU Check Point Seq.
		@return FTU Check Point Seq	  */
	public int getFTU_CheckPointSeq_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_CheckPointSeq_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU Check Point Seq Line.
		@param FTU_CheckPointSeqLine_ID FTU Check Point Seq Line	  */
	public void setFTU_CheckPointSeqLine_ID (int FTU_CheckPointSeqLine_ID)
	{
		if (FTU_CheckPointSeqLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPointSeqLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_FTU_CheckPointSeqLine_ID, Integer.valueOf(FTU_CheckPointSeqLine_ID));
	}

	/** Get FTU Check Point Seq Line.
		@return FTU Check Point Seq Line	  */
	public int getFTU_CheckPointSeqLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_CheckPointSeqLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FTU_CheckPointSeqLine_UU.
		@param FTU_CheckPointSeqLine_UU FTU_CheckPointSeqLine_UU	  */
	public void setFTU_CheckPointSeqLine_UU (String FTU_CheckPointSeqLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_FTU_CheckPointSeqLine_UU, FTU_CheckPointSeqLine_UU);
	}

	/** Get FTU_CheckPointSeqLine_UU.
		@return FTU_CheckPointSeqLine_UU	  */
	public String getFTU_CheckPointSeqLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_FTU_CheckPointSeqLine_UU);
	}

	/** Set Line.
		@param LineNo 
		Line No
	  */
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}