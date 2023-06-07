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

/** Generated Model for I_EntryTicket
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="I_EntryTicket")
public class X_I_EntryTicket extends PO implements I_I_EntryTicket, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230602L;

    /** Standard Constructor */
    public X_I_EntryTicket (Properties ctx, int I_EntryTicket_ID, String trxName)
    {
      super (ctx, I_EntryTicket_ID, trxName);
      /** if (I_EntryTicket_ID == 0)
        {
			setI_EntryTicket_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_I_EntryTicket (Properties ctx, int I_EntryTicket_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, I_EntryTicket_ID, trxName, virtualColumns);
      /** if (I_EntryTicket_ID == 0)
        {
			setI_EntryTicket_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_EntryTicket (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_I_EntryTicket[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set BP Search Key.
		@param BPValue Business Partner Key Value
	*/
	public void setBPValue (String BPValue)
	{
		set_ValueNoCheck (COLUMNNAME_BPValue, BPValue);
	}

	/** Get BP Search Key.
		@return Business Partner Key Value
	  */
	public String getBPValue()
	{
		return (String)get_Value(COLUMNNAME_BPValue);
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner .
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocType_ID(), get_TrxName());
	}

	/** Set Document Type.
		@param C_DocType_ID Document type or rules
	*/
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0)
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
	{
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_ID)
			.getPO(getC_Order_ID(), get_TrxName());
	}

	/** Set Order.
		@param C_Order_ID Order
	*/
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException
	{
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_ID)
			.getPO(getC_OrderLine_ID(), get_TrxName());
	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID Sales Order Line
	*/
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc Date of the Document
	*/
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Distribution Order Document Number.
		@param DD_OrderDocumentNo Distribution Order Document Number
	*/
	public void setDD_OrderDocumentNo (String DD_OrderDocumentNo)
	{
		set_Value (COLUMNNAME_DD_OrderDocumentNo, DD_OrderDocumentNo);
	}

	/** Get Distribution Order Document Number.
		@return Distribution Order Document Number	  */
	public String getDD_OrderDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DD_OrderDocumentNo);
	}

	public org.eevolution.model.I_DD_Order getDD_Order() throws RuntimeException
	{
		return (org.eevolution.model.I_DD_Order)MTable.get(getCtx(), org.eevolution.model.I_DD_Order.Table_ID)
			.getPO(getDD_Order_ID(), get_TrxName());
	}

	/** Set Distribution Order.
		@param DD_Order_ID Distribution Order
	*/
	public void setDD_Order_ID (int DD_Order_ID)
	{
		if (DD_Order_ID < 1)
			set_ValueNoCheck (COLUMNNAME_DD_Order_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_DD_Order_ID, Integer.valueOf(DD_Order_ID));
	}

	/** Get Distribution Order.
		@return Distribution Order	  */
	public int getDD_Order_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_DD_OrderLine getDD_OrderLine() throws RuntimeException
	{
		return (org.eevolution.model.I_DD_OrderLine)MTable.get(getCtx(), org.eevolution.model.I_DD_OrderLine.Table_ID)
			.getPO(getDD_OrderLine_ID(), get_TrxName());
	}

	/** Set Distribution Order Line.
		@param DD_OrderLine_ID Distribution Order Line
	*/
	public void setDD_OrderLine_ID (int DD_OrderLine_ID)
	{
		if (DD_OrderLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_DD_OrderLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_DD_OrderLine_ID, Integer.valueOf(DD_OrderLine_ID));
	}

	/** Get Distribution Order Line.
		@return Distribution Order Line	  */
	public int getDD_OrderLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Document Type Name.
		@param DocTypeName Name of the Document Type
	*/
	public void setDocTypeName (String DocTypeName)
	{
		set_Value (COLUMNNAME_DocTypeName, DocTypeName);
	}

	/** Get Document Type Name.
		@return Name of the Document Type
	  */
	public String getDocTypeName()
	{
		return (String)get_Value(COLUMNNAME_DocTypeName);
	}

	/** Set Document No.
		@param DocumentNo Document sequence number of the document
	*/
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Driver Value.
		@param DriverValue Drivers Value
	*/
	public void setDriverValue (String DriverValue)
	{
		set_Value (COLUMNNAME_DriverValue, DriverValue);
	}

	/** Get Driver Value.
		@return Drivers Value
	  */
	public String getDriverValue()
	{
		return (String)get_Value(COLUMNNAME_DriverValue);
	}

	public net.frontuari.recordweight.model.I_FTU_Driver getFTU_Driver() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_Driver)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Driver.Table_ID)
			.getPO(getFTU_Driver_ID(), get_TrxName());
	}

	/** Set Driver.
		@param FTU_Driver_ID Driver
	*/
	public void setFTU_Driver_ID (int FTU_Driver_ID)
	{
		if (FTU_Driver_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_Driver_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_Driver_ID, Integer.valueOf(FTU_Driver_ID));
	}

	/** Get Driver.
		@return Driver	  */
	public int getFTU_Driver_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Driver_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_EntryTicket getFTU_EntryTicket() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_EntryTicket)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_EntryTicket.Table_ID)
			.getPO(getFTU_EntryTicket_ID(), get_TrxName());
	}

	/** Set Entry Ticket.
		@param FTU_EntryTicket_ID Entry Ticket
	*/
	public void setFTU_EntryTicket_ID (int FTU_EntryTicket_ID)
	{
		if (FTU_EntryTicket_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_EntryTicket_ID, Integer.valueOf(FTU_EntryTicket_ID));
	}

	/** Get Entry Ticket.
		@return Entry Ticket	  */
	public int getFTU_EntryTicket_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_EntryTicket_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public net.frontuari.recordweight.model.I_FTU_Vehicle getFTU_Vehicle() throws RuntimeException
	{
		return (net.frontuari.recordweight.model.I_FTU_Vehicle)MTable.get(getCtx(), net.frontuari.recordweight.model.I_FTU_Vehicle.Table_ID)
			.getPO(getFTU_Vehicle_ID(), get_TrxName());
	}

	/** Set Vehicle.
		@param FTU_Vehicle_ID Vehicle
	*/
	public void setFTU_Vehicle_ID (int FTU_Vehicle_ID)
	{
		if (FTU_Vehicle_ID < 1)
			set_ValueNoCheck (COLUMNNAME_FTU_Vehicle_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_FTU_Vehicle_ID, Integer.valueOf(FTU_Vehicle_ID));
	}

	/** Get Vehicle.
		@return Vehicle	  */
	public int getFTU_Vehicle_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FTU_Vehicle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Import Entry Ticket_ID.
		@param I_EntryTicket_ID Import Entry Ticket_ID
	*/
	public void setI_EntryTicket_ID (int I_EntryTicket_ID)
	{
		if (I_EntryTicket_ID < 1)
			set_ValueNoCheck (COLUMNNAME_I_EntryTicket_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_I_EntryTicket_ID, Integer.valueOf(I_EntryTicket_ID));
	}

	/** Get Import Entry Ticket_ID.
		@return Import Entry Ticket_ID	  */
	public int getI_EntryTicket_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_EntryTicket_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Import Entry Ticket_UU.
		@param I_EntryTicket_UU Import Entry Ticket_UU
	*/
	public void setI_EntryTicket_UU (String I_EntryTicket_UU)
	{
		set_Value (COLUMNNAME_I_EntryTicket_UU, I_EntryTicket_UU);
	}

	/** Get Import Entry Ticket_UU.
		@return Import Entry Ticket_UU	  */
	public String getI_EntryTicket_UU()
	{
		return (String)get_Value(COLUMNNAME_I_EntryTicket_UU);
	}

	/** Set Import Error Message.
		@param I_ErrorMsg Messages generated from import process
	*/
	public void setI_ErrorMsg (String I_ErrorMsg)
	{
		set_Value (COLUMNNAME_I_ErrorMsg, I_ErrorMsg);
	}

	/** Get Import Error Message.
		@return Messages generated from import process
	  */
	public String getI_ErrorMsg()
	{
		return (String)get_Value(COLUMNNAME_I_ErrorMsg);
	}

	/** Set Imported.
		@param I_IsImported Has this import been processed
	*/
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported()
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Delivery Bulk Material = DBM */
	public static final String OPERATIONTYPE_DeliveryBulkMaterial = "DBM";
	/** Delivery Finished Product = DFP */
	public static final String OPERATIONTYPE_DeliveryFinishedProduct = "DFP";
	/** Delivery Multiple Products = DMP */
	public static final String OPERATIONTYPE_DeliveryMultipleProducts = "DMP";
	/** Import Raw Material = IRM */
	public static final String OPERATIONTYPE_ImportRawMaterial = "IRM";
	/** Material Input Movement = MIM */
	public static final String OPERATIONTYPE_MaterialInputMovement = "MIM";
	/** Material Output Movement = MOM */
	public static final String OPERATIONTYPE_MaterialOutputMovement = "MOM";
	/** Other Record Weight = ORW */
	public static final String OPERATIONTYPE_OtherRecordWeight = "ORW";
	/** Product Bulk Receipt = PBR */
	public static final String OPERATIONTYPE_ProductBulkReceipt = "PBR";
	/** Receipt More than one Product = RMP */
	public static final String OPERATIONTYPE_ReceiptMoreThanOneProduct = "RMP";
	/** Raw Material Receipt = RMR */
	public static final String OPERATIONTYPE_RawMaterialReceipt = "RMR";
	/** Set OperationType.
		@param OperationType OperationType
	*/
	public void setOperationType (String OperationType)
	{

		set_ValueNoCheck (COLUMNNAME_OperationType, OperationType);
	}

	/** Get OperationType.
		@return OperationType	  */
	public String getOperationType()
	{
		return (String)get_Value(COLUMNNAME_OperationType);
	}

	/** Set Order Document No.
		@param OrderDocumentNo Order Document No
	*/
	public void setOrderDocumentNo (String OrderDocumentNo)
	{
		set_Value (COLUMNNAME_OrderDocumentNo, OrderDocumentNo);
	}

	/** Get Order Document No.
		@return Order Document No	  */
	public String getOrderDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_OrderDocumentNo);
	}

	/** Set Processed.
		@param Processed The document has been processed
	*/
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed()
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now
	*/
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing()
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Product Name.
		@param ProductName Name of the Product
	*/
	public void setProductName (String ProductName)
	{
		set_ValueNoCheck (COLUMNNAME_ProductName, ProductName);
	}

	/** Get Product Name.
		@return Name of the Product
	  */
	public String getProductName()
	{
		return (String)get_Value(COLUMNNAME_ProductName);
	}

	/** Set Product Key.
		@param ProductValue Key of the Product
	*/
	public void setProductValue (String ProductValue)
	{
		set_ValueNoCheck (COLUMNNAME_ProductValue, ProductValue);
	}

	/** Get Product Key.
		@return Key of the Product
	  */
	public String getProductValue()
	{
		return (String)get_Value(COLUMNNAME_ProductValue);
	}

	/** Set Shipper Name.
		@param ShipperName Shipper Name
	*/
	public void setShipperName (String ShipperName)
	{
		set_Value (COLUMNNAME_ShipperName, ShipperName);
	}

	/** Get Shipper Name.
		@return Shipper Name	  */
	public String getShipperName()
	{
		return (String)get_Value(COLUMNNAME_ShipperName);
	}

	/** Set TrailerPlate.
		@param TrailerPlate TrailerPlate
	*/
	public void setTrailerPlate (String TrailerPlate)
	{
		set_ValueNoCheck (COLUMNNAME_TrailerPlate, TrailerPlate);
	}

	/** Get TrailerPlate.
		@return TrailerPlate	  */
	public String getTrailerPlate()
	{
		return (String)get_Value(COLUMNNAME_TrailerPlate);
	}

	/** Set Vehicle Plate.
		@param VehiclePlate Vehicle Plate
	*/
	public void setVehiclePlate (String VehiclePlate)
	{
		set_ValueNoCheck (COLUMNNAME_VehiclePlate, VehiclePlate);
	}

	/** Get Vehicle Plate.
		@return Vehicle Plate	  */
	public String getVehiclePlate()
	{
		return (String)get_Value(COLUMNNAME_VehiclePlate);
	}
}