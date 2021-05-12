/**
 * 
 */
package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClientInfo;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_Invoice;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_M_Movement;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;
import net.frontuari.recordweight.model.MFTURecordWeight;
import net.frontuari.recordweight.model.X_FTU_LoadOrder;

/**
 * @author dixon
 *
 */
public class GenerateFromLoadOrder extends FTUProcess {

	private int p_FTU_LoadOrder_ID = -1;
	/** Current Shipment */
	private MInOut m_Current_Shipment = null;
	/** Current Warehouse */
	private int m_Current_Warehouse_ID = 0;
	/** Current Business Partner */
	private int m_Current_BPartner_ID = 0;
	/** Document Action */
	private String p_DocAction = null;
	/** Document Type */
	private int p_C_DocType_ID = 0;
	/** DateInvoiced */
	private Timestamp p_MovementDate = null;
	/** Is Immediate Delivery */
	private boolean m_Current_IsImmediateDelivery = false;
	/** Created Records */
	private int m_Created = 0;
	/** Print Document */
	private ArrayList<Integer> m_IDs = new ArrayList<Integer>();
	/** Print Log*/
	private ArrayList<String> m_DocShipNs = new ArrayList<String>();
	/** Print Log*/
	private ArrayList<String> m_DocInvNs = new ArrayList<String>();
	/** Print Document */
	private ArrayList<Integer[]> m_ArrayIDs = new ArrayList<Integer[]>();

	/** Current Invoice */
	private MInvoice m_Current_Invoice = null;
	/** Message */
	private StringBuilder msg = new StringBuilder();
	/** Current Shipment */
	private MMovement m_Current_Movement = null;

	private String p_IsGenerateDocument = null;

	private final static String GENERATE_ONLY_INVOICE = "GOI";
	private final static String GENERATE_ONLY_SHIPMENT = "GOS";
	private final static String GENERATE_ONLY_MOVEMENT = "GOM";
	private final static String GENERATE_INVOICE_AND_SHIPMENT = "GIS";

//	GIS -- Generate Invoice and Shipment
//	GOI -- Generate Only Invoice
//	GOM -- Generate Only Movement
//	GOS -- Generate Only Shipment

	@Override 
	protected void prepare() {
		for (ProcessInfoParameter para : getParameter()) {
			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if (name.equals("FTU_LoadOrder_ID"))
				p_FTU_LoadOrder_ID = para.getParameterAsInt();
			else if (name.equals("C_DocTypeShipment_ID"))
				p_C_DocType_ID = para.getParameterAsInt();
			else if (name.equals("MovementDate"))
				p_MovementDate = (Timestamp) para.getParameter();
			else if (name.equals("DocAction"))
				p_DocAction = (String) para.getParameter();
			else if (name.equals("IsGenerateDocument"))
				p_IsGenerateDocument = (String) para.getParameter();

		}
	}

	@Override
	protected String doIt() throws Exception {
		if (p_FTU_LoadOrder_ID <= 0)
			throw new AdempiereException("@FTU_LoadOrder_ID@ @NotFound@");
		
		if (p_IsGenerateDocument.equals(GENERATE_ONLY_SHIPMENT)) {
			// Return
			return createShipments();
		} else if (p_IsGenerateDocument.equals(GENERATE_ONLY_INVOICE)) {
			// Return
			return createInvoices();
		} else if (p_IsGenerateDocument.equals(GENERATE_INVOICE_AND_SHIPMENT)) {
			// Return
			StringBuilder message = new StringBuilder();
			message.append(createShipments());
			m_Current_BPartner_ID = -1;m_Created = 0;msg = new StringBuilder();
			message.append(createInvoices());
			
			return  message.toString() ;
		} else if (p_IsGenerateDocument.equals(GENERATE_ONLY_MOVEMENT)) {
			// Return
			return createMovements();
		} else {
			throw new AdempiereException("@IsGenerateDocument@ @NotFound@");
		}
	}

	/**
	 * Create Shipments
	 * <li>Improvement in method
	 * 
	 * @return
	 * @return String
	 */
	private String createShipments() {

		MFTULoadOrder m_FTU_LoadOrder = new MFTULoadOrder(getCtx(), p_FTU_LoadOrder_ID, get_TrxName());

		if (m_FTU_LoadOrder.isDelivered())
			return "@FTU_LoadOrder_ID@ @IsDelivered@";
		
		/*if(!(m_FTU_LoadOrder.isHandleRecordWeight() && m_FTU_LoadOrder.isWeightRegister())
				&& !m_FTU_LoadOrder.isImmediateDelivery())
			return "@FTU_RecordWeight_ID@ @NotFound@";

		if(!m_FTU_LoadOrder.isImmediateDelivery())
			return "@FTU_LoadOrder_ID@ No @IsImmediateDelivery@";*/
		
		if (m_FTU_LoadOrder.isHandleRecordWeight() && !m_FTU_LoadOrder.isWeightRegister() 
				&& !m_FTU_LoadOrder.isImmediateDelivery())
			return "@FTU_LoadOrder_ID@ no @isImmediateDelivery@ & no posee @isWeightRegister@";
		
		MFTULoadOrderLine[] lines = m_FTU_LoadOrder.getLines(true);

		double m_BreakValue = MSysConfig.getDoubleValue("FTU_BREAK_SHIPMENT_WEIGHT", 0, getAD_Client_ID());
		BigDecimal m_BreakWeight = new BigDecimal(m_BreakValue);
		BigDecimal m_CumulatedWeightLine = Env.ZERO;
		BigDecimal m_CumulatedWeightAll = Env.ZERO;
		BigDecimal m_QtySubtract = Env.ZERO;
		boolean m_Break = false;
		boolean m_Added = false;

		for (MFTULoadOrderLine m_FTU_LoadOrderLine : lines) {

			MOrder order = (MOrder) m_FTU_LoadOrderLine.getC_OrderLine().getC_Order();

			// Valid Document Order and Business Partner
			int m_C_BPartner_ID = order.getC_BPartner_ID();
			int m_M_Warehouse_ID = order.getM_Warehouse_ID();
			int m_C_Order_ID = order.getC_Order_ID();
			BigDecimal m_Qty = m_FTU_LoadOrderLine.getQty();
			BigDecimal m_TotalQty = m_Qty;

			// Valid
			if (m_TotalQty == null)
				m_TotalQty = Env.ZERO;
			// Record Weight Reference
			int m_FTU_RecordWeight_ID = 0;
			// Valid Immediate Delivery
			if (m_FTU_LoadOrder.getOperationType().equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial)
					&& m_FTU_LoadOrder.isHandleRecordWeight() && !m_FTU_LoadOrder.isWeightRegister()) {
				completeShipment();
				//
				continue;
			} else if (m_FTU_LoadOrder.getOperationType().equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial)
					&& m_FTU_LoadOrder.isHandleRecordWeight() && m_FTU_LoadOrder.isWeightRegister()) {
				MFTURecordWeight m_FTU_RecordWeight = m_FTU_LoadOrder.getRecordWeight();
				if (m_FTU_RecordWeight == null) {
					completeShipment();
					//
					continue;
				}
				//
				m_FTU_RecordWeight_ID = m_FTU_RecordWeight.getFTU_RecordWeight_ID();
				// Get Weight
				BigDecimal m_NetWeight = m_FTU_RecordWeight.getNetWeight();
				// Get Product
				MProduct product = MProduct.get(getCtx(), m_FTU_LoadOrder.getM_Product_ID());
				// Rate Convert
				BigDecimal rate = MUOMConversion.getProductRateTo(Env.getCtx(), product.getM_Product_ID(),
						m_FTU_RecordWeight.getC_UOM_ID());
				// Convert Quantity
				m_Qty = m_NetWeight.multiply(rate);
				// Set Total Quantity
				m_TotalQty = m_Qty;
			} else if (X_FTU_LoadOrder.OPERATIONTYPE_DeliveryFinishedProduct.equals(m_FTU_LoadOrder.getOperationType())
					&& m_FTU_LoadOrder.isHandleRecordWeight() && m_FTU_LoadOrder.isWeightRegister())
			{
				MFTURecordWeight recordWeight = m_FTU_LoadOrder.getRecordWeight();
				
				m_FTU_RecordWeight_ID = recordWeight.get_ID();
			}
			// Valid Null
			if (m_Qty == null)
				m_Qty = Env.ZERO;
			//
			if (m_Current_BPartner_ID != m_C_BPartner_ID || m_Current_Warehouse_ID != m_M_Warehouse_ID || m_Break) {
				// Complete Previous Shipment
				completeShipment();
				// Initialize Order and
				m_Current_Warehouse_ID = m_M_Warehouse_ID;
				m_Current_BPartner_ID = m_C_BPartner_ID;
				// Get Warehouse
				MWarehouse warehouse = MWarehouse.get(getCtx(), m_Current_Warehouse_ID, get_TrxName());
				// Valid Purchase Order and Business Partner
				if (m_C_Order_ID == 0)
					throw new AdempiereException("@C_Order_ID@ @NotFound@");
				if (m_Current_BPartner_ID == 0)
					throw new AdempiereException("@C_BPartner_ID@ @NotFound@");
				// Create Shipment From Order
				m_Current_Shipment = new MInOut(order, p_C_DocType_ID, p_MovementDate);
				m_Current_Shipment.setDateAcct(p_MovementDate);
				//m_Current_Shipment.setAD_Org_ID(warehouse.getAD_Org_ID());
				m_Current_Shipment.setAD_OrgTrx_ID(warehouse.getAD_Org_ID());
				m_Current_Shipment.setC_BPartner_ID(m_Current_BPartner_ID);
				// Set Warehouse
				//m_Current_Shipment.setM_Warehouse_ID(m_Current_Warehouse_ID);
				m_Current_Shipment.setDocStatus(X_M_InOut.DOCSTATUS_Drafted);
				// Set Record Weight Reference
				if (m_FTU_RecordWeight_ID > 0) {
					m_Current_Shipment.set_ValueOfColumn("FTU_RecordWeight_ID", m_FTU_RecordWeight_ID);
				}
				m_Current_Shipment.saveEx(get_TrxName());
				// Initialize Message
				if (msg.length() > 0)
					msg.append(Env.NL + m_Current_Shipment.getDocumentNo());
				else
					msg.append(m_Current_Shipment.getDocumentNo());
			}
			// Set Break
			m_Break = false;
			// Shipment Created?
			if (m_Current_Shipment != null) {
				// Create Shipment Line
				MInOutLine shipmentLine = new MInOutLine(getCtx(), 0, get_TrxName());
				// Get Order Line
				MOrderLine oLine = (MOrderLine) m_FTU_LoadOrderLine.getC_OrderLine();
				
				
				m_Current_Shipment.setAD_Org_ID(oLine.getM_Warehouse().getAD_Org_ID());
				m_Current_Shipment.setM_Warehouse_ID(oLine.getM_Warehouse_ID());
				
				// Instance MProduct
				MProduct product = MProduct.get(getCtx(), m_FTU_LoadOrderLine.getM_Product_ID());
				// Rate Convert
				BigDecimal rate = MUOMConversion.getProductRateTo(Env.getCtx(), product.getM_Product_ID(),
						oLine.getC_UOM_ID());
				// Validate Rate equals null
				if (rate == null) {
					MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
					MUOM oLineUOM = MUOM.get(getCtx(), oLine.getC_UOM_ID());
					throw new AdempiereException(
							"@NoUOMConversion@ @from@ " + oLineUOM.getName() + " @to@ " + productUOM.getName());
				}
				//
				if (m_BreakValue > 0 && m_FTU_LoadOrder.isImmediateDelivery()) {
					MClientInfo clientInfo = MClientInfo.get(getCtx(), getAD_Client_ID());
					// Rate From Product to Weigh
					BigDecimal rateCumulated = MUOMConversion.getProductRateTo(Env.getCtx(), product.getM_Product_ID(),
							clientInfo.getC_UOM_Weight_ID());
					// Rate from Weight to Product
					BigDecimal rateFromWeight = MUOMConversion.getProductRateFrom(Env.getCtx(),
							product.getM_Product_ID(), clientInfo.getC_UOM_Weight_ID());
					// Validate Rate equals null
					if (rateCumulated == null) {
						MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
						MUOM oLineUOM = MUOM.get(getCtx(), clientInfo.getC_UOM_Weight_ID());
						throw new AdempiereException(
								"@NoUOMConversion@ @from@ " + oLineUOM.getName() + " @to@ " + productUOM.getName());
					}
					//
					m_Qty = m_Qty.subtract(m_QtySubtract);
					BigDecimal nextWeight = m_CumulatedWeightAll.add(m_Qty.multiply(rateCumulated));
					if (nextWeight.doubleValue() > m_BreakWeight.doubleValue()
							&& product.get_ValueAsBoolean("IsBulk")) {
						BigDecimal diff = nextWeight.subtract(m_BreakWeight);
						m_Qty = m_Qty.subtract(diff.multiply(rateFromWeight));
						m_Break = true;
					}
					// Set Cumulate Weight
					m_CumulatedWeightLine = m_CumulatedWeightLine.add(m_Qty.multiply(rateCumulated));
					m_CumulatedWeightAll = m_CumulatedWeightAll.add(m_CumulatedWeightLine);
				}
				// Set Values for Lines
				
				//shipmentLine.setAD_Org_ID(m_Current_Shipment.getAD_Org_ID());
				
				shipmentLine.setAD_Org_ID(oLine.getAD_Org_ID());
				
				shipmentLine.setM_InOut_ID(m_Current_Shipment.getM_InOut_ID());
				// Quantity and Product
				shipmentLine.setM_Product_ID(product.getM_Product_ID());
				//shipmentLine.setM_Warehouse_ID(m_Current_Shipment.getM_Warehouse_ID());
				shipmentLine.setM_Warehouse_ID(oLine.getM_Warehouse_ID());
				// References
				shipmentLine.setC_OrderLine_ID(m_FTU_LoadOrderLine.getC_OrderLine_ID());
				// Quantity
				shipmentLine.setC_UOM_ID(oLine.getC_UOM_ID());
				shipmentLine.setQty(m_Qty);
				shipmentLine.setQtyEntered(m_Qty.multiply(rate));
				shipmentLine.setM_Locator_ID(m_Qty);
				// Save Line
				shipmentLine.saveEx(get_TrxName());

				// Manually Process Shipment
				// Added
				if (!m_Added) {
					m_FTU_LoadOrderLine.setConfirmedQty(m_TotalQty);
					m_FTU_LoadOrderLine.setM_InOutLine_ID(shipmentLine.get_ID());
				}
				m_FTU_LoadOrderLine.saveEx();
				// Set true Is Delivered and Is Weight Register
				m_FTU_LoadOrder.setIsDelivered(true);
				// Save
				m_FTU_LoadOrder.saveEx(get_TrxName());
				// Set Current Delivery
				m_Current_IsImmediateDelivery = m_FTU_LoadOrder.isImmediateDelivery();

				m_CumulatedWeightLine = Env.ZERO;
			} // End Invoice Line Created
			// Valid Break
			if (m_Break) {
				m_CumulatedWeightAll = Env.ZERO;
				m_QtySubtract = m_QtySubtract.add(m_Qty);
				completeShipment();
				m_Added = true;
			} else {
				m_QtySubtract = Env.ZERO;
				m_Added = false;
			}
		}

		// Complete Shipment
		completeShipment();
		// Commit Transaction

		// Info
	/*	if (m_Current_Shipment!=null) {
		/*	for(int x = 0; x < m_DocShipNs.size(); x++) {
				addLog(0, new Timestamp(System.currentTimeMillis()), null,m_DocShipNs.get(x),m_Current_Shipment.get_Table_ID(), m_Current_Shipment.get_ID());
			}
			addBufferLog(m_Current_Shipment.getM_InOut_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Shipment.getDocumentNo(), m_Current_Shipment.get_Table_ID(), m_Current_Shipment.get_ID());
		//addLog(0, new Timestamp(System.currentTimeMillis()), null, msg+": "+m_Current_Shipment.getDocumentNo(), m_Current_Shipment.get_Table_ID(), m_Current_Shipment.get_ID());
		//return "@M_InOut_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "]";
		}else {
			addLog("Error creando entrega");
		}*/
		
		return "";
	}

	/**
	 * Complete Document
	 * 
	 * @return void
	 */
	private void completeShipment() {
		if (m_Current_Shipment != null && m_Current_Shipment.getDocStatus().equals(X_M_InOut.DOCSTATUS_Drafted)) {
			m_Current_Shipment.setDocAction(p_DocAction);
			m_Current_Shipment.processIt(p_DocAction);
			m_Current_Shipment.saveEx();
			if (m_Current_Shipment.getDocStatus() != X_M_InOut.DOCSTATUS_Completed) {
				throw new AdempiereException(m_Current_Shipment.getProcessMsg());
			}
			/*addLog(m_Current_Shipment.getM_InOut_ID(), m_Current_Shipment.getDateAcct(), null,
					m_Current_Shipment.getDocumentNo() + (m_Current_Shipment.getProcessMsg() != null
							&& m_Current_Shipment.getProcessMsg().length() != 0
									? ": Error " + m_Current_Shipment.getProcessMsg()
									: " --> @OK@"));*/
			
			// Created
			m_Created++;
			// Is Printed?
			if (m_Current_Shipment.getDocStatus().equals(X_M_InOut.DOCSTATUS_Completed)
					&& m_Current_IsImmediateDelivery) {
				m_IDs.add(m_Current_Shipment.getM_InOut_ID());
				//log.log(Level.FINE, m_Current_Shipment.getDocumentNo());
				//addBufferLog(m_Current_Shipment.getM_InOut_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Shipment.getDocumentNo(), m_Current_Shipment.get_Table_ID(), m_Current_Shipment.get_ID());
			}
			addBufferLog(m_Current_Shipment.getM_InOut_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Shipment.getDocumentNo(), m_Current_Shipment.get_Table_ID(), m_Current_Shipment.get_ID());
		}
	}

	/**
	 * Create Invoice From Liquidations
	 * 
	 * @return String
	 */
	private String createInvoices() {

		MFTULoadOrder m_FTU_LoadOrder = new MFTULoadOrder(getCtx(), p_FTU_LoadOrder_ID, get_TrxName());

		if (m_FTU_LoadOrder.isInvoiced())
			return "@FTU_LoadOrder_ID@ @IsInvoiced@";

		MFTULoadOrderLine[] lines = m_FTU_LoadOrder.getLines(true);

		for (MFTULoadOrderLine m_FTU_LoadOrderLine : lines) {

			MOrder order = (MOrder) m_FTU_LoadOrderLine.getC_OrderLine().getC_Order();
			
			if(order.isInvoiced())
				return "@C_Order_ID@ @IsInvoiced@";
			// Valid Purchase Order and Business Partner
			if (order.get_ID() == 0)
				throw new AdempiereException("@C_Order_ID@ @NotFound@");

			MOrderLine orderLine = (MOrderLine) m_FTU_LoadOrderLine.getC_OrderLine();
			
			String sql = "SELECT 1 "
					+ "FROM C_Invoice "
					+ "INNER JOIN C_InvoiceLine ON (C_Invoice.C_Invoice_ID = C_InvoiceLine.C_Invoice_ID) "
					+ "WHERE C_InvoiceLine.C_OrderLine_ID = ? AND DocStatus IN('CO','CL') ";
			boolean isInvoicedLine = DB.getSQLValue(get_TrxName(), sql, orderLine.getC_OrderLine_ID()) == 1;
			if(isInvoicedLine) {
				continue;
			}

			// Valid Document Order and Business Partner
			int m_C_BPartner_ID = order.getC_BPartner_ID();
			int m_C_OrderLine_ID = m_FTU_LoadOrderLine.getC_OrderLine_ID();
			int m_C_Charge_ID = orderLine.getC_Charge_ID();
			int m_M_Product_ID = orderLine.getM_Product_ID();

			BigDecimal rate = Env.ZERO;
			BigDecimal m_Qty = m_FTU_LoadOrderLine.getQty();
			//
			if (m_Current_BPartner_ID != m_C_BPartner_ID) {
				// Complete Previous Invoice
				completeInvoice();
				m_Current_BPartner_ID = m_C_BPartner_ID;
				// Create Invoice From Order
				m_Current_Invoice = new MInvoice(order, p_C_DocType_ID, p_MovementDate);
				m_Current_Invoice.setDateAcct(p_MovementDate);
				m_Current_Invoice.setC_Currency_ID(order.getC_Currency_ID());
				m_Current_Invoice.setC_ConversionType_ID(order.getC_ConversionType_ID());
				// Set DocStatus
				m_Current_Invoice.setDocStatus(X_C_Invoice.DOCSTATUS_Drafted);
				m_Current_Invoice.saveEx(get_TrxName());
			}
			// Invoiced Created?
			if (m_Current_Invoice != null) {
				// Get Values from Result Set
				int m_FTU_LoadOrderLine_ID = m_FTU_LoadOrderLine.getFTU_LoadOrderLine_ID();

				// Get Lines From Load Order
				MFTULoadOrderLine line = new MFTULoadOrderLine(getCtx(), m_FTU_LoadOrderLine_ID, get_TrxName());

				MInvoiceLine invoiceLine = new MInvoiceLine(getCtx(), 0, get_TrxName());
				// Get Product
				MProduct product = MProduct.get(getCtx(), m_M_Product_ID);

				MOrderLine oLine = new MOrderLine(getCtx(), m_C_OrderLine_ID, get_TrxName());

				if (oLine.getM_Product_ID() != 0) {
					// Rate Convert
					rate = MUOMConversion.getProductRateTo(Env.getCtx(), product.getM_Product_ID(),
							oLine.getC_UOM_ID());
					// Validate Rate equals null
					if (rate == null) {
						MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
						MUOM oLineUOM = MUOM.get(getCtx(), oLine.getC_UOM_ID());
						throw new AdempiereException(
								"@NoUOMConversion@ @from@ " + oLineUOM.getName() + " @to@ " + productUOM.getName());
					}
					invoiceLine.setM_Product_ID(product.getM_Product_ID());
				} else if (oLine.getC_Charge_ID() != 0)
					invoiceLine.setC_Charge_ID(m_C_Charge_ID);

				if (m_FTU_LoadOrderLine_ID == 0) {
					invoiceLine.setQtyEntered(oLine.getQtyOrdered());
					invoiceLine.setQtyInvoiced(oLine.getQtyOrdered());
				}
				// Set Values For Line
				invoiceLine.setC_OrderLine_ID(oLine.getC_OrderLine_ID());

				invoiceLine.setC_UOM_ID(oLine.getC_UOM_ID());
				//
				if (line.getFTU_LoadOrder_ID() != 0) {
					if (m_FTU_LoadOrder.getOperationType()
							.equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryFinishedProduct)) {
						invoiceLine.setQtyEntered(m_Qty.multiply(rate));
						invoiceLine.setQtyInvoiced(m_Qty);
					} else if (m_FTU_LoadOrder.getOperationType()
							.equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial)) {
						sql = "SELECT FTU_RecordWeight_ID " + "FROM FTU_RecordWeight "
								+ "WHERE DocStatus IN('CO', 'CL') " + "AND FTU_LoadOrder_ID= ?";
						//
						int FTU_RecordWeight_ID = DB.getSQLValue(get_TrxName(), sql, p_FTU_LoadOrder_ID);
						// Valid Record Weight
						if (FTU_RecordWeight_ID <= 0)
							throw new AdempiereException("@FTU_RecordWeight_ID@ @NotFound@");

						MFTURecordWeight m_RecordWeight = new MFTURecordWeight(getCtx(), FTU_RecordWeight_ID,
								get_TrxName());
						// Get Rate for Weight

						BigDecimal rateWeight = MUOMConversion.getProductRateFrom(Env.getCtx(),
								product.getM_Product_ID(), m_RecordWeight.getC_UOM_ID());
						// Validate Rate equals null
						if (rateWeight == null) {
							MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
							MUOM oLineUOM = MUOM.get(getCtx(), m_RecordWeight.getC_UOM_ID());
							throw new AdempiereException(
									"@NoUOMConversion@ @from@ " + oLineUOM.getName() + " @to@ " + productUOM.getName());
						}
						//
						BigDecimal m_QtyWeight = m_RecordWeight.getNetWeight();
						BigDecimal m_QtyInvoced = m_QtyWeight.multiply(rateWeight);
						BigDecimal m_QtyEntered = m_QtyInvoced.multiply(rate);

						invoiceLine.setQtyEntered(m_QtyEntered);
						invoiceLine.setQtyInvoiced(m_QtyInvoced);

					}
				}
				invoiceLine.setAD_Org_ID(m_Current_Invoice.getAD_Org_ID());
				invoiceLine.setPriceList(oLine.getPriceList());
				invoiceLine.setPriceEntered(oLine.getPriceEntered());
				invoiceLine.setPriceActual(oLine.getPriceActual());
				invoiceLine.setC_Tax_ID(oLine.getC_Tax_ID());
				invoiceLine.setC_Invoice_ID(m_Current_Invoice.getC_Invoice_ID());
				invoiceLine.save(get_TrxName());
				//
				if (line.getFTU_LoadOrder_ID() != 0) {
					line.setC_InvoiceLine_ID(invoiceLine.getC_InvoiceLine_ID());
					line.saveEx();

					// Change Load Order
					MFTULoadOrder lo = new MFTULoadOrder(getCtx(), line.getFTU_LoadOrder_ID(), get_TrxName());
					lo.setIsInvoiced(true);
					lo.saveEx();
				}
			} // End Invoice Line Created
		} // End Invoice Generated

		completeInvoice();
		// Info
		/*if (m_Current_Invoice!=null) {
		
				for(int x = 0; x < m_DocInvNs.size(); x++) {
					addLog(0, new Timestamp(System.currentTimeMillis()), null,m_DocInvNs.get(x),m_Current_Shipment.get_Table_ID(), m_Current_Shipment.get_ID());
				}
		//addLog(m_Current_Invoice.get_ID(), new Timestamp(System.currentTimeMillis()), null, msg+": ", m_Current_Invoice.get_Table_ID(), m_Current_Invoice.get_ID());
		//return "@C_Invoice_ID@ @Created@ = " + msg.toString();
		//return "@C_Invoice_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "]";
		}else {
		addLog("Error creando factura");*/		
		return "";
	}

	/**
	 * Complete Invoice
	 * 
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 8/12/2014,
	 *         22:49:19
	 * @return void
	 */
	private void completeInvoice() {
		if (m_Current_Invoice != null && m_Current_Invoice.getDocStatus().equals(X_C_Invoice.DOCSTATUS_Drafted)) {
			m_Current_Invoice.setDocAction(p_DocAction);
			m_Current_Invoice.processIt(p_DocAction);
			m_Current_Invoice.saveEx();
			m_Current_Invoice.load(get_TrxName());
			//if (m_Current_Invoice.getDocStatus() != X_C_Invoice.DOCSTATUS_Completed) {
			//	throw new AdempiereException(m_Current_Invoice.getProcessMsg());
			//}
			addBufferLog(m_Current_Invoice.getC_Invoice_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Invoice.getDocumentInfo(), m_Current_Invoice.get_Table_ID(), m_Current_Invoice.getC_Invoice_ID());
			// Initialize Message
			if (msg.length() > 0)
				msg.append(" - " + m_Current_Invoice.getDocumentNo());
			else
				msg.append(m_Current_Invoice.getDocumentNo());
			// Created
			m_Created++;
			// Is Printed?
			if (m_Current_Invoice.getDocStatus().equals(X_C_Invoice.DOCSTATUS_Completed)) {
				m_IDs.add(m_Current_Invoice.getC_Invoice_ID());
				//log.log(Level.FINE, m_Current_Invoice.getDocumentNo());
				//addBufferLog(m_Current_Invoice.getC_Invoice_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Invoice.getDocumentNo(), m_Current_Invoice.get_Table_ID(), m_Current_Invoice.get_ID());
			}
			//addBufferLog(m_Current_Invoice.getC_Invoice_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Invoice.getDocumentNo(), m_Current_Invoice.get_Table_ID(), m_Current_Invoice.get_ID());
		}
	}

	/**
	 * Create Shipments
	 * <li>Improvement in method
	 * 
	 * @return String
	 */
	private String createMovements() {

		MFTULoadOrder m_FTU_LoadOrder = new MFTULoadOrder(getCtx(), p_FTU_LoadOrder_ID, get_TrxName());

		if (m_FTU_LoadOrder.isDelivered())
			return "@FTU_LoadOrder_ID@ @IsDelivered@";

		MFTULoadOrderLine[] lines = m_FTU_LoadOrder.getLines(true);

		for (MFTULoadOrderLine m_FTU_LoadOrderLine : lines) {

			// Create Order
			MDDOrder order = (MDDOrder) m_FTU_LoadOrderLine.getDD_OrderLine().getDD_Order();

			// Valid Purchase Order and Business Partner
			if (order.get_ID() == 0)
				throw new AdempiereException("@DD_Order_ID@ @NotFound@");
			// Valid Document Order and Business Partner
			int m_C_BPartner_ID = order.getC_BPartner_ID();
			int m_DD_Order_ID = order.getDD_Order_ID();

			// Get Document Type
			MDocType m_DocType = MDocType.get(getCtx(), order.getC_DocType_ID());
			// Get Document for Movement
			if (p_C_DocType_ID == 0) {
				p_C_DocType_ID = m_DocType.get_ValueAsInt("C_DocTypeMovement_ID");
			}
			// Valid Document
			if (p_C_DocType_ID == 0) {
				throw new AdempiereUserError(
						"@C_DocTypeMovement_ID@ @NotFound@ " + "[@C_DocType_ID@ " + m_DocType.getName() + "]");
			}
			//
			if (m_Current_BPartner_ID != m_C_BPartner_ID) {
				// Complete Previous Shipment
				completeMovement();
				// Initialize Order and
				m_Current_BPartner_ID = m_C_BPartner_ID;
				// Create Movement
				m_Current_Movement = new MMovement(getCtx(), 0, get_TrxName());
				//
				m_Current_Movement.setC_DocType_ID(p_C_DocType_ID);
				m_Current_Movement.setDateReceived(p_MovementDate);
				// Set Organization
				m_Current_Movement.setAD_Org_ID(order.getAD_Org_ID());
				m_Current_Movement.setDD_Order_ID(m_DD_Order_ID);
				if (order.getC_BPartner_ID() > 0) {
					m_Current_Movement.setC_BPartner_ID(order.getC_BPartner_ID());
					m_Current_Movement.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
					m_Current_Movement.saveEx();
				}
				m_Current_Movement.saveEx();
				//
				m_Created++;
				// Initialize Message
				if (msg.length() > 0)
					msg.append(" - " + m_Current_Movement.getDocumentNo());
				else
					msg.append(m_Current_Movement.getDocumentNo());
			}
			// Shipment Created?
			if (m_Current_Movement != null) {
				// Get Values from Result Set
				BigDecimal m_Qty = m_FTU_LoadOrderLine.getQty();
				// Valid Null
				if (m_Qty == null)
					m_Qty = Env.ZERO;

				MDDOrderLine m_DD_OrderLine = (MDDOrderLine) m_FTU_LoadOrderLine.getDD_OrderLine();
				MMovementLine m_MovementLine = new MMovementLine(m_Current_Movement);
				// Reference
				m_MovementLine.setM_Movement_ID(m_Current_Movement.getM_Movement_ID());
				m_MovementLine.setDD_OrderLine_ID(m_DD_OrderLine.getDD_OrderLine_ID());
				// Set Product
				m_MovementLine.setM_Product_ID(m_FTU_LoadOrderLine.getM_Product_ID());
				m_MovementLine.setM_Locator_ID(m_DD_OrderLine.getM_Locator_ID());
				m_MovementLine.setM_LocatorTo_ID(m_DD_OrderLine.getM_LocatorTo_ID());
				if (m_DD_OrderLine.getM_AttributeSetInstance_ID() > 0)
					m_MovementLine.setM_AttributeSetInstance_ID(m_DD_OrderLine.getM_AttributeSetInstance_ID());
				if (m_DD_OrderLine.getM_AttributeSetInstanceTo_ID() > 0)
					m_MovementLine.setM_AttributeSetInstanceTo_ID(m_DD_OrderLine.getM_AttributeSetInstanceTo_ID());
				m_MovementLine.setMovementQty(m_Qty);
				m_MovementLine.saveEx();
				m_FTU_LoadOrderLine.setM_MovementLine_ID(m_MovementLine.getM_MovementLine_ID());
				m_FTU_LoadOrderLine.setConfirmedQty(m_Qty);
				m_FTU_LoadOrderLine.saveEx();
				// Instance MFTULoadOrder
				MFTULoadOrder lo = new MFTULoadOrder(getCtx(), m_FTU_LoadOrderLine.getFTU_LoadOrder_ID(),
						get_TrxName());
				// Set true Is Delivered and Is Weight Register
				lo.setIsMoved(true);
				// Save
				lo.saveEx(get_TrxName());
				// Set Current Delivery
				m_Current_IsImmediateDelivery = lo.isImmediateDelivery();
				//
			} // End Invoice Line Created
		} // End Invoice Generated
		// Complete Shipment
		completeMovement();
		// Info
		if (m_Current_Movement!=null) {
		log.log(Level.SEVERE, m_Current_Movement.getDocumentNo());
		
		//return "@M_Movement_ID@ @Created@ = " + m_Created + " [" + msg.toString() + "]";
		}else {
		addLog("Error creando movimiento");
		}
		return "";
	}

	/**
	 * Complete Document
	 * 
	 * @return void
	 */
	private void completeMovement() {
		if (m_Current_Movement != null && m_Current_Movement.getDocStatus().equals(X_M_Movement.DOCSTATUS_Drafted)) {
			m_Current_Movement.setDocAction(p_DocAction);
			m_Current_Movement.processIt(p_DocAction);
			m_Current_Movement.saveEx();
			if (m_Current_Movement.getDocStatus() != X_M_InOut.DOCSTATUS_Completed) {
				throw new AdempiereException(m_Current_Movement.getProcessMsg());
			}
			/*addLog(m_Current_Movement.getM_Movement_ID(), m_Current_Movement.getDateReceived(), null,
					m_Current_Movement.getDocumentNo() + (m_Current_Movement.getProcessMsg() != null
							&& m_Current_Movement.getProcessMsg().length() != 0
									? ": Error " + m_Current_Movement.getProcessMsg()
									: " --> @OK@"));*/
			// Created
			m_Created++;
			// Is Printed?
			if (m_Current_Movement.getDocStatus().equals(X_M_Movement.DOCSTATUS_Completed)
					&& m_Current_IsImmediateDelivery) {
				m_ArrayIDs.add(
						new Integer[] { m_Current_Movement.getM_Movement_ID(), m_Current_Movement.getC_DocType_ID() });
			}
			addBufferLog(m_Current_Movement.get_ID(), new Timestamp(System.currentTimeMillis()), null, m_Current_Movement.getDocumentNo(), m_Current_Movement.get_Table_ID(), m_Current_Movement.get_ID());
		}
	}

}
