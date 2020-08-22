package net.frontuari.recordweight.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import net.frontuari.recordweight.model.MFTULoadOrder;

public class FTUGenerateFromLoadOrder {
	
	/** Logger */
	public static CLogger log = CLogger.getCLogger(FTUGenerateFromLoadOrder.class);
	/**	Window No			*/
	private int         	m_WindowNo = 0;

	protected DateFormat 		dateFormat 		 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**	is available for save	*/
	private boolean isAvailableForSave = false;
	
	/** Organization ID               */
	private int             m_AD_Org_ID = 0;
	private String m_OperationType;
	private Integer m_C_BParther_ID;
	private Integer m_FTU_EntryTicket_ID;
	private Integer m_FTU_LoadOrder_ID;
	private Timestamp m_DateFrom;
	private Timestamp m_DateTo;
	private boolean m_HasSelection;
	private int m_Mode = -1;
	
	/** Match Mode              	*/
	public String[] m_ModeList = new String[] {
		Msg.translate(Env.getCtx(), "GenerateInvoice"),
		Msg.translate(Env.getCtx(), "GenerateMovement"),
		Msg.translate(Env.getCtx(), "GenerateInvoiceShipment"),
		Msg.translate(Env.getCtx(), "GenerateShipment")};
	
	/**
	 * Indicate mode not matched
	 */
	public static final int		MODE_INVOICE = 0;

	/**
	 * Indicate mode matched
	 */
	public static final int		MODE_INVOICE_SHIPMENT = 1;
	
	/**
	 * Indicate mode matched
	 */
	public static final int		MODE_MOVEMENT = 2;
	
	/**
	 * Indicate mode matched
	 */
	public static final int		MODE_SHIPMENT = 3;
	
	/**	Payment list	*/
	private ArrayList<ArrayList<Object>> loadOrderData = new ArrayList<ArrayList<Object>>();
	/**	Payment Bank Statement to match	*/
	private Map<Integer, MFTULoadOrder> currentLoadOrderHashMap = new HashMap<Integer, MFTULoadOrder>();
	
	public FTUGenerateFromLoadOrder() {
		// TODO Auto-generated constructor stub
	}
	
	public int getWindowNo() {
		return m_WindowNo;
	}

	/**
	 * Set if is available for save
	 * @param isAvailableForSave
	 */
	public void setIsAvailableForSave(boolean isAvailableForSave) {
		this.isAvailableForSave = isAvailableForSave;
	}
	
	/**
	 * Is Available for save
	 * @return
	 */
	public boolean isAvailableForSave() {
		return isAvailableForSave;
	}
	
	public int getAD_Org_ID() {
		return m_AD_Org_ID;
	}
	
	public void setAD_Org_ID(int m_AD_Org_ID) {
		this.m_AD_Org_ID = m_AD_Org_ID;
	}
	
	
	protected void setOperationType(String m_OperationType) {
		this.m_OperationType = m_OperationType ;		
	}

	protected void setC_BPartner_ID(Integer m_C_BParther_ID) {
		this.m_C_BParther_ID = m_C_BParther_ID;
	}

	protected void setFTU_EntryTicket_ID(Integer m_FTU_EntryTicket_ID) {
		this.m_FTU_EntryTicket_ID = m_FTU_EntryTicket_ID;
	}

	protected void setFTU_LoadOrder_ID(Integer m_FTU_LoadOrder_ID) {
		this.m_FTU_LoadOrder_ID = m_FTU_LoadOrder_ID;
	}
	
	protected void setDateFrom(Timestamp m_DateFrom) {
		this.m_DateFrom = m_DateFrom;
	}

	protected void setDateTo(Timestamp m_DateTo) {
		this.m_DateTo = m_DateTo;
	}
	
	protected void setHasSelection(boolean m_HasSelection) {
		this.m_HasSelection = m_HasSelection;
	}
	
	/**
	 * Reset parameter to default value or to empty value? implement at
	 * inheritance class when reset parameter maybe need init again parameter,
	 * reset again default value
	 */
	protected void resetParameters() {
	}
	
	/**
	 * Clear values
	 */
	public void clear() {

	}
	
	/**
	 * Validate parameters, it return null is nothing happens, else return a translated message
	 * @return
	 */
	public String validateParameters() {
		StringBuffer message = new StringBuffer();
		/*if(bankAccountId <= 0) {
			message.append("@C_BankAccount_ID@ @NotFound@");
		}*/
		//	
		//	Match Mode
		if(m_Mode < 0) {
			if(message.length() > 0) {
				message.append(Env.NL);
			}
			//	
			message.append("@MatchMode@ @NotFound@");
		}
		if(message.length() > 0) {
			return message.toString();
		}
		
		//	Default
		return null;
	}
	
	/**
	 * Get payments columns
	 * @return
	 */
	public Vector<String> getCurrentPaymentColumnNames() {
		//  Header Info
		Vector<String> columnNames = new Vector<String>(9);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "FTU_LoadOrder_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "FTU_EntryTicket_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "DateDoc"));
		columnNames.add(Msg.translate(Env.getCtx(), "OperationType"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_Order_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "Qty"));
		return columnNames;
	}
	
	/**
	 * Configure payment table
	 * @param miniTable
	 */
	public void configureCurrentPaymentTable(IMiniTable miniTable) {
//		TODO: Error por no existir
//		miniTable.setKeyColumnIndex(0);
		miniTable.setColumnClass(0, IDColumn.class, false, getCurrentPaymentColumnNames().get(0));     //  0-Selection
		miniTable.setColumnClass(1, Integer.class, true);     //  1-TrxDate
		miniTable.setColumnClass(2, Integer.class, true);     //  1-TrxDate
		miniTable.setColumnClass(3, Integer.class, true);     //  1-TrxDate
		miniTable.setColumnClass(4, Timestamp.class, true);     //  1-TrxDate
		miniTable.setColumnClass(5, String.class, true);    	//  3-DocumentNo
		miniTable.setColumnClass(6, Integer.class, true);     //  1-TrxDate
		miniTable.setColumnClass(7, Integer.class, true);     //  1-TrxDate
		miniTable.setColumnClass(8, BigDecimal.class, true);    //  6-Amount
		
		//  Table UI
		miniTable.autoSize();
	}
	
	/**
	 * Get Load Orders Data for show on two tables
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getLoadOrderData() {
		loadOrderData = new  ArrayList<ArrayList<Object>>();
		//	
		StringBuffer sql = new StringBuffer("");
		//	Where Clause
		if(MODE_SHIPMENT == getMode()) {
			
			sql.append("SELECT FTU_LoadOrder_ID, FTU_EntryTicket_ID, AD_Org_ID, C_BPartner_ID, DateDoc, OperationType, C_Order_ID, M_Product_ID, Qty ")
				.append(" FROM FTU_RV_LoadOrderGenerateInvoice gi " );
			
			
			//	Generate Shipment of Load Order
			sql.append( " WHERE ")
				.append("(NOT EXISTS (SELECT 1 FROM FTU_WeightScale WHERE FTU_WeightScale.AD_Org_ID =GI.AD_Org_ID  AND FTU_WeightScale.IsActive = 'Y')")
				.append(" OR (GI.IsImmediateDelivery = 'Y' AND EXISTS (SELECT 1 FROM FTU_WeightScale WHERE FTU_WeightScale.AD_Org_ID =GI.AD_Org_ID  AND FTU_WeightScale.IsActive = 'Y')") 
				.append(")) AND GI.DocStatus = 'CO' AND GI.OperationType IN ('DFP','DBM')  AND GI.M_InOutLine_ID IS NULL " );
		} else {
			return loadOrderData;
		}
		
		//	For parameters
		//	Date Trx
		if(getDateFrom() != null) {
			sql.append("AND p.DateTrx >= ? ");
		}
		if(getDateTo() != null) {
			sql.append("AND p.DateTrx <= ? ");
		}

		//	for BP
		if(getBpartnerId() > 0) {
			sql.append("AND p.C_BPartner_ID = ? ");
		}
		// role security
		sql = new StringBuffer(MRole.getDefault(Env.getCtx(), false)
				.addAccessSQL(sql.toString(), "gi", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO));
		//	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//	fill hash
		currentLoadOrderHashMap = new HashMap<Integer, MFTULoadOrder>();
		try {
			int i= 1;
			pstmt = DB.prepareStatement(sql.toString(), null);
			if(getDateFrom() != null) {
				pstmt.setTimestamp(i++, getDateFrom());
			}
			if(getDateTo() != null) {
				pstmt.setTimestamp(i++, getDateTo());
			}
			if(getBpartnerId() > 0) {
				pstmt.setInt(i++, getBpartnerId());
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ArrayList<Object> line = new ArrayList<Object>();
				line.add(new IDColumn(rs.getInt("FTU_LoadOrder_ID")));      //  0-Selection
				line.add(rs.getInt("FTU_EntryTicket_ID"));       			//  1-DateTrx
				line.add(rs.getInt("AD_Org_ID"));       			//  1-DateTrx
				line.add(rs.getInt("C_BPartner_ID"));       			//  1-DateTrx
				line.add(rs.getTimestamp("DateDoc"));       			//  1-DateTrx
				line.add(rs.getString("OperationType"));      	//  2-IsReceipt
				line.add(rs.getInt("C_Order_ID"));      				//  3-DocumentNo
//				KeyNamePair pp = new KeyNamePair(rs.getInt("C_BPartner_ID"), rs.getString("BPName"));
//				line.add(pp); 											//	4-BPName
				line.add(rs.getInt("M_Product_ID"));      				//  5-TenderType
				line.add(rs.getBigDecimal("Qty"));					//  7-PayAmt
				//	Add model class
				currentLoadOrderHashMap.put(rs.getInt("FTU_LoadOrder_ID"), new MFTULoadOrder(Env.getCtx(), rs, null));
				loadOrderData.add(line);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//	
		return loadOrderData;
	}
	
	
	private String createShipments(){
		/*PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer msg = new StringBuffer();
		double m_BreakValue = MSysConfig.getDoubleValue("FTU_BREAK_SHIPMENT_WEIGHT", 0, Env.getAD_Client_ID(Env.getCtx()));
		BigDecimal m_BreakWeight = new BigDecimal(m_BreakValue);
		BigDecimal m_CumulatedWeightLine = Env.ZERO;
		BigDecimal m_CumulatedWeightAll = Env.ZERO;
		BigDecimal m_QtySubtract	= Env.ZERO;
		boolean m_Break = false;
		boolean m_Added = false;
		try {
			ps = DB.prepareStatement(sql.toString(), 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, get_TrxName());
			ps.setInt(1, m_Parent_Instance_ID);
			rs = ps.executeQuery();
			//	
			while(rs.next()){
				//	Valid Document Order and Business Partner
				int m_C_BPartner_ID 		= rs.getInt("C_BPartner_ID");
				int m_M_Warehouse_ID 		= rs.getInt("M_Warehouse_ID");
				int m_C_Order_ID 			= rs.getInt("C_Order_ID");
				int m_FTU_LoadOrderLine_ID 	= rs.getInt("FTU_LoadOrderLine_ID");
				BigDecimal m_Qty 			= rs.getBigDecimal("Qty");
				BigDecimal m_TotalQty		= m_Qty;
				
				//	Valid
				if(m_TotalQty == null)
					m_TotalQty = Env.ZERO;
				//	Record Weight Reference
				int m_FTU_RecordWeight_ID 	= 0;
				//	Instance MLoadOrderLine
				MFTULoadOrderLine m_FTU_LoadOrderLine = 
						new MFTULoadOrderLine(getCtx(), m_FTU_LoadOrderLine_ID, get_TrxName());
				//	Instance MFTULoadOrder
				MFTULoadOrder m_FTU_LoadOrder = new MFTULoadOrder(getCtx(), 
						m_FTU_LoadOrderLine.getFTU_LoadOrder_ID(), get_TrxName());
				//	Valid Immediate Delivery
				if(m_FTU_LoadOrder.getOperationType()
						.equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial)
						&& m_FTU_LoadOrder.isHandleRecordWeight()
						&& !m_FTU_LoadOrder.isWeightRegister()) {
					completeShipment();
					//	
					continue;
				} else if(m_FTU_LoadOrder.getOperationType()
						.equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial)
						&& m_FTU_LoadOrder.isHandleRecordWeight()
						&& m_FTU_LoadOrder.isWeightRegister()) {
					MFTURecordWeight m_FTU_RecordWeight = m_FTU_LoadOrder.getRecordWeight();
					if(m_FTU_RecordWeight == null) {
						completeShipment();
						//	
						continue;
					}
					//	
					m_FTU_RecordWeight_ID = m_FTU_RecordWeight.getFTU_RecordWeight_ID();
					//	Get Weight
					BigDecimal m_NetWeight = m_FTU_RecordWeight.getNetWeight();
					//	Get Product
					MProduct product = MProduct.get(getCtx(), m_FTU_LoadOrder.getM_Product_ID());
					//	Rate Convert
					BigDecimal rate = MUOMConversion.getProductRateTo(Env.getCtx(), 
							product.getM_Product_ID(), m_FTU_RecordWeight.getC_UOM_ID());
					//	Convert Quantity
					m_Qty = m_NetWeight.multiply(rate);
					//	Set Total Quantity
					m_TotalQty = m_Qty;
				}
				//	Valid Null
				if(m_Qty == null)
					m_Qty = Env.ZERO;
				//	
				if (m_Current_BPartner_ID != m_C_BPartner_ID
						|| m_Current_Warehouse_ID != m_M_Warehouse_ID
						|| m_Break) {
					//	Complete Previous Shipment
					completeShipment();
					//	Initialize Order and 
					m_Current_Warehouse_ID 	= m_M_Warehouse_ID;
					m_Current_BPartner_ID 	= m_C_BPartner_ID;
					//	Get Warehouse
					MWarehouse warehouse = MWarehouse.get(getCtx(), m_Current_Warehouse_ID, get_TrxName());
					//	Valid Purchase Order and Business Partner
					if(m_C_Order_ID == 0)
						throw new AdempiereException("@C_Order_ID@ @NotFound@");
					if(m_Current_BPartner_ID == 0)
						throw new AdempiereException("@C_BPartner_ID@ @NotFound@");
					//	Create Order
					MOrder order = new MOrder(getCtx(), m_C_Order_ID, get_TrxName());
					//Create Shipment From Order
					m_Current_Shipment = new MInOut(order, p_C_DocTypeShipment_ID, p_MovementDate);
					m_Current_Shipment.setDateAcct(p_MovementDate);
					m_Current_Shipment.setAD_Org_ID(warehouse.getAD_Org_ID());
					m_Current_Shipment.setAD_OrgTrx_ID(warehouse.getAD_Org_ID());
					m_Current_Shipment.setC_BPartner_ID(m_Current_BPartner_ID);
					//	Set Warehouse
					m_Current_Shipment.setM_Warehouse_ID(m_Current_Warehouse_ID);
					m_Current_Shipment.setDocStatus(X_M_InOut.DOCSTATUS_Drafted);
					//	Set Record Weight Reference
					if(m_FTU_RecordWeight_ID > 0) {
						m_Current_Shipment.set_ValueOfColumn("FTU_RecordWeight_ID", m_FTU_RecordWeight_ID);
					}
					m_Current_Shipment.saveEx(get_TrxName());
					//	Initialize Message
					if(msg.length() > 0)
						msg.append(" - " + m_Current_Shipment.getDocumentNo());
					else
						msg.append(m_Current_Shipment.getDocumentNo());					
				}
				//	Set Break
				m_Break = false;
				//	Shipment Created?
				if (m_Current_Shipment != null) {
					//	Create Shipment Line
					MInOutLine shipmentLine = 
							new MInOutLine(getCtx(), 0, get_TrxName());
					//	Get Order Line
					MOrderLine oLine = (MOrderLine) m_FTU_LoadOrderLine.getC_OrderLine();
					//	Instance MProduct
					MProduct product = MProduct.get(getCtx(), m_FTU_LoadOrderLine.getM_Product_ID());
					//	Rate Convert
					BigDecimal rate = MUOMConversion.getProductRateTo(Env.getCtx(), 
							product.getM_Product_ID(), oLine.getC_UOM_ID());
					//	Validate Rate equals null
					if(rate == null) {
						MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
						MUOM oLineUOM = MUOM.get(getCtx(), oLine.getC_UOM_ID());
						throw new AdempiereException("@NoUOMConversion@ @from@ " 
										+ oLineUOM.getName() + " @to@ " + productUOM.getName());
					}
					//	
					if(m_BreakValue > 0
							&& m_FTU_LoadOrder.isImmediateDelivery()) {
						MClientInfo clientInfo = MClientInfo.get(getCtx(), getAD_Client_ID());
						//	Rate From Product to Weigh
						BigDecimal rateCumulated = MUOMConversion.getProductRateTo(Env.getCtx(), 
								product.getM_Product_ID(), clientInfo.getC_UOM_Weight_ID());
						//	Rate from Weight to Product
						BigDecimal rateFromWeight = MUOMConversion.getProductRateFrom(Env.getCtx(), 
								product.getM_Product_ID(), clientInfo.getC_UOM_Weight_ID());
						//	Validate Rate equals null
						if(rateCumulated == null) {
							MUOM productUOM = MUOM.get(getCtx(), product.getC_UOM_ID());
							MUOM oLineUOM = MUOM.get(getCtx(), clientInfo.getC_UOM_Weight_ID());
							throw new AdempiereException("@NoUOMConversion@ @from@ " 
											+ oLineUOM.getName() + " @to@ " + productUOM.getName());
						}
						//
						//2015-05-14 Carlos Parada Fix Error in Shipment
						m_Qty = m_Qty.subtract(m_QtySubtract);
						//BigDecimal m_QtyWeight = m_Qty;
						//m_QtyWeight = m_QtyWeight.subtract(m_CumulatedWeightLine.multiply(rateFromWeight));
						BigDecimal nextWeight = m_CumulatedWeightAll.add(m_Qty.multiply(rateCumulated));
						//End Carlos Parada
						if(nextWeight.doubleValue() > m_BreakWeight.doubleValue()
								//2015-05-21 Carlos Parada Divide Shipment only Product Bulk
								&& product.get_ValueAsBoolean("IsBulk")
								//End Carlos Parada
								) {
							BigDecimal diff = nextWeight.subtract(m_BreakWeight);
							m_Qty = m_Qty.subtract(diff.multiply(rateFromWeight));
							m_Break = true;
						}
						//	Set Cumulate Weight
						m_CumulatedWeightLine = m_CumulatedWeightLine.add(m_Qty.multiply(rateCumulated));
						m_CumulatedWeightAll = m_CumulatedWeightAll.add(m_CumulatedWeightLine);
					}
					//	Set Values for Lines
					shipmentLine.setAD_Org_ID(m_Current_Shipment.getAD_Org_ID());
					shipmentLine.setM_InOut_ID(m_Current_Shipment.getM_InOut_ID());
					//	Quantity and Product
					shipmentLine.setM_Product_ID(product.getM_Product_ID());
					shipmentLine.setM_Warehouse_ID(m_Current_Shipment.getM_Warehouse_ID());
					//	References
					shipmentLine.setC_OrderLine_ID(m_FTU_LoadOrderLine.getC_OrderLine_ID());
					//	Quantity
					shipmentLine.setC_UOM_ID(oLine.getC_UOM_ID());
					shipmentLine.setQty(m_Qty);
					shipmentLine.setQtyEntered(m_Qty.multiply(rate));
					shipmentLine.setM_Locator_ID(m_Qty);
					//	Save Line
					shipmentLine.saveEx(get_TrxName());
					
					//	Manually Process Shipment
					//	Added
					if(!m_Added) {
						m_FTU_LoadOrderLine.setConfirmedQty(m_TotalQty);
						m_FTU_LoadOrderLine.setM_InOutLine_ID(shipmentLine.get_ID());
					}
					m_FTU_LoadOrderLine.saveEx();
					//	Set true Is Delivered and Is Weight Register
					m_FTU_LoadOrder.setIsDelivered(true);
					//	Save
					m_FTU_LoadOrder.saveEx(get_TrxName());
					//	Set Current Delivery
					m_Current_IsImmediateDelivery = m_FTU_LoadOrder.isImmediateDelivery();
					
					m_CumulatedWeightLine = Env.ZERO;
				}	//End Invoice Line Created
				//	Valid Break
				if(m_Break) {
					//2015-05-14 Carlos Parada Fix Error in Shipment
					m_CumulatedWeightAll = Env.ZERO;
					m_QtySubtract = m_QtySubtract.add(m_Qty);
					//End Carlos Parada
					completeShipment();
					m_Added = true;
					rs.previous();
				} else {
					//2015-05-14 Carlos Parada Fix Error in Shipment
					m_QtySubtract = Env.ZERO;
					//End Carlos Parada
					m_Added = false;
				}
			}	//	End Invoice Generated
			//	Complete Shipment
			completeShipment();
			//	Commit Transaction
			if(!m_IsFromParent) {
				commitEx();
			}
			//	Print Documents
			printDocuments();
			//	
		} catch(Exception ex) {
			if(!m_IsFromParent) {
				rollback();
			} else {
				throw new AdempiereException(ex);
			}
			return ex.getMessage();
		} finally {
			  DB.close(rs, ps);
		      rs = null; ps = null;
		}
		//	Info
		return "@M_InOut_ID@ @Created@ = "+ m_Created + " [" + msg.toString() + "]";
		
		*/
		
		return null;
	}
	
	
	protected int getMode() {
		return m_Mode;
	}
	
	protected void setMode(int m_Mode) {
		this.m_Mode = m_Mode;
	}
	

	protected boolean isMatchedMode() {
		return false;
	}

	protected int getBpartnerId() {
		return m_C_BParther_ID;
	}

	protected Timestamp getDateTo() {
		return m_DateTo;
	}

	protected Timestamp getDateFrom() {
		return m_DateFrom;
	}

	/**************************************************************************
	 *  Save Data
	 */
	public String saveData(int m_WindowNo, String trxName) {
		//	Return processed
		return Msg.translate(Env.getCtx(), "BankStatementMatch.MatchedProcessed") + ": ";
	}   //  saveData
	
}
