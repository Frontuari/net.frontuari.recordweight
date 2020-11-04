package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 */
public class MFTULoadOrder extends X_FTU_LoadOrder implements DocAction, DocOptions {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2132004667849638705L;

	/**
	 * *** Constructor ***
	 * @param ctx
	 * @param FTU_LoadOrder_ID
	 * @param trxName
	 */
	public MFTULoadOrder(Properties ctx, int FTU_LoadOrder_ID, String trxName) {
		super(ctx, FTU_LoadOrder_ID, trxName);
	}

	/**
	 * *** Constructor ***
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTULoadOrder(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** Lines					*/
	private MFTULoadOrderLine[]		m_lines = null;
	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), 0);
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
	//	setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
	//	setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateDoc(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		
		//	Set if Handle Record Weight
		setIsHandleRecordWeight(MFTUWeightScale
				.isWeightScaleOrg(getAD_Org_ID(), get_TrxName()));
		//	Set Immediate 
		MDocType m_DocType = MDocType.get(Env.getCtx(), getC_DocType_ID());

		setIsImmediateDelivery(m_DocType.get_ValueAsBoolean("IsImmediateDelivery"));
		
		//	Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Valid Volume Capacity
		if(getVolumeCapacity().compareTo(getVolume()) < 0) {
			m_processMsg = "@VolumeCapacity@ < @Volume@";
			return DocAction.STATUS_Invalid;
		}
		//	Valid Load Capacity
		if(getLoadCapacity().compareTo(getWeight()) < 0) {
			m_processMsg = "@LoadCapacity@ < @Weight@";
			return DocAction.STATUS_Invalid;
		}
		m_processMsg = validETReference();
	 	if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		//	Very Lines
		MFTULoadOrderLine[] lines = getLines(false);
		if (lines.length == 0){
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		} else {
			//	Verify Error
			m_processMsg = validStock();
			if(m_processMsg != null) {
				return DocAction.STATUS_Invalid;
			}
		}
		//	Valid Entry Ticket
		if(getFTU_EntryTicket_ID() == 0
				&& !MFTUWeightScale.isWeightScaleOrg(getAD_Org_ID(), get_TrxName())) {
			m_processMsg = "@FTU_EntryTicket_ID@ @NotFound@";
			return DocAction.STATUS_InProgress;
		}
		//	Valid Weight and Volume
		m_processMsg = validWeightVolume();
		if(m_processMsg != null)
			return DocAction.STATUS_Invalid;
			
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		setDefiniteDocumentNo();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	
	/**
	 * Valid Stock for Lines
	 * @return
	 * @return String
	 */
	private String validStock() {
		String sql = new String("SELECT lol.SeqNo, s.ProductName, SUM(s.QtyOnHand) QtyOnHand, "
				+ "SUM(CASE "
				+ "		WHEN s.OrderLine_ID = COALESCE(ol.C_OrderLine_ID, dol.DD_OrderLine_ID) "
				+ "			AND s.FTU_LoadOrderLine_ID <> lol.FTU_LoadOrderLine_ID "
				+ "			THEN s.QtyLoc "
				+ "			ELSE 0 "
				+ "		END"
				+ ") QtyLoc, "
				+ "SUM(CASE "
				+ "		WHEN s.FTU_LoadOrderLine_ID <> lol.FTU_LoadOrderLine_ID "
				+ "			THEN s.QtyLoc "
				+ "			ELSE 0 "
				+ "		END"
				+ ") QtyInTransit, "
				+ "COALESCE(ol.QtyOrdered, dol.QtyOrdered) QtyOrdered, "
				+ "COALESCE(ol.QtyDelivered, dol.QtyDelivered) QtyDelivered, "
				+ "lol.Qty "
				+ "FROM FTU_LoadOrder lo "
				+ "INNER JOIN FTU_LoadOrderLine lol ON(lol.FTU_LoadOrder_ID = lo.FTU_LoadOrder_ID) "
				+ "LEFT JOIN C_OrderLine ol ON(ol.C_OrderLine_ID = lol.C_OrderLine_ID) "
				+ "LEFT JOIN DD_OrderLine dol ON(dol.DD_OrderLine_ID = lol.DD_OrderLine_ID) "
				+ "LEFT JOIN (SELECT lc.FTU_LoadOrderLine_ID, COALESCE(ol.C_OrderLine_ID, dol.DD_OrderLine_ID) OrderLine_ID, "
				+ "				st.M_Product_ID, (p.Value || '-' || p.Name) ProductName, l.M_Warehouse_ID, "
				+ "				COALESCE(st.M_AttributeSetInstance_ID, 0) M_AttributeSetInstance_ID, st.QtyOnHand, "
				+ "				COALESCE(CASE "
				+ "					WHEN("
				+ "							(c.IsDelivered = 'N' AND c.OperationType IN('DBM', 'DFP')) "
				+ "							OR "
				+ "							(c.IsMoved = 'N' AND c.OperationType = 'MOM')"
				+ "						) "
				+ "						AND c.DocStatus = 'CO'"
				+ "						THEN lc.Qty "
				+ "						ELSE 0 "
				+ "					END, 0) "
				+ "				QtyLoc "
				+ "			FROM M_Storage st "
				+ "			INNER JOIN M_Product p ON(p.M_Product_ID = st.M_Product_ID) "
				+ "			INNER JOIN M_Locator l ON(l.M_Locator_ID = st.M_Locator_ID) "
				+ "			LEFT JOIN FTU_LoadOrderLine lc ON(lc.M_Product_ID = st.M_Product_ID AND lc.M_warehouse_ID = l.M_Warehouse_ID) "
				+ "			LEFT JOIN FTU_LoadOrder c ON(c.FTU_LoadOrder_ID = lc.FTU_LoadOrder_ID) "
				+ "			LEFT JOIN C_OrderLine ol ON(ol.C_OrderLine_ID = lc.C_OrderLine_ID) "
				+ "			LEFT JOIN DD_OrderLine dol ON(dol.DD_OrderLine_ID = lc.DD_OrderLine_ID)) "
				+ "		s ON(s.M_Product_ID = lol.M_Product_ID "
				+ "				AND s.M_Warehouse_ID = lol.M_Warehouse_ID "
				+ "				AND COALESCE(ol.M_AttributeSetInstance_ID, dol.M_AttributeSetInstance_ID, 0) = s.M_AttributeSetInstance_ID) "
				+ "WHERE lo.FTU_LoadOrder_ID = ? "
				+ "GROUP BY lol.SeqNo, s.ProductName, lol.Qty, ol.QtyOrdered, dol.QtyOrdered, "
				+ "ol.QtyDelivered, dol.QtyDelivered "
				+ "HAVING("
				+ "	("
				+ "		SUM(s.QtyOnHand) < ("
				+ "								SUM(CASE "
				+ "										WHEN s.FTU_LoadOrderLine_ID <> lol.FTU_LoadOrderLine_ID "
				+ "											THEN s.QtyLoc "
				+ "											ELSE 0 "
				+ "										END"
				+ "								) + lol.Qty"
				+ "							)"
				+ "	) "
				+ "	OR "
				+ "	("
				+ "		COALESCE(ol.QtyOrdered, dol.QtyOrdered) "
				+ "						< ("
				+ "								SUM(CASE "
				+ "										WHEN s.OrderLine_ID = COALESCE(ol.C_OrderLine_ID, dol.DD_OrderLine_ID) "
				+ "											AND s.FTU_LoadOrderLine_ID <> lol.FTU_LoadOrderLine_ID "
				+ "											THEN s.QtyLoc "
				+ "											ELSE 0 "
				+ "										END"
				+ "								) + lol.Qty "
				+ "							+ COALESCE(ol.QtyDelivered, dol.QtyDelivered)"
				+ "						)"
				+ "	)"
				+ ") "
				+ "ORDER BY lol.SeqNo");
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer msg = new StringBuffer();
		try {
			ps = DB.prepareStatement(sql.toString(), get_TrxName());
			ps.setInt(1, getFTU_LoadOrder_ID());
			rs = ps.executeQuery();
			//	
			while(rs.next()) {
				int m_SeqNo					= rs.getInt("SeqNo");
				String m_ProductName		= rs.getString("ProductName");
				BigDecimal m_QtyOnHand 		= rs.getBigDecimal("QtyOnHand");
				BigDecimal m_QtyLoc 		= rs.getBigDecimal("QtyLoc");
				BigDecimal m_QtyInTransit 	= rs.getBigDecimal("QtyInTransit");
				BigDecimal m_QtyOrdered 	= rs.getBigDecimal("QtyOrdered");
				BigDecimal m_QtyDelivered 	= rs.getBigDecimal("QtyDelivered");
				BigDecimal m_Qty			= rs.getBigDecimal("Qty");
				String errorMsg = null;
				
				//String m_DeliveryRule		= rs.getString("DeliveryRule");
				//	Valid Quantity Ordered
				BigDecimal m_AvailableForOrder = m_QtyOrdered
						.subtract(m_QtyDelivered)
						.subtract(m_QtyLoc)
						.subtract(m_Qty);
				//	
				BigDecimal m_DiffQtyOnHand = m_QtyOnHand
						.subtract(m_QtyInTransit)
						.subtract(m_Qty);
				//	Valid Order vs Delivered
				if(m_AvailableForOrder.signum() < 0) {
					errorMsg = "@Qty@ > (@QtyOrdered@ - @QtyDelivered@ - @QtyInTransit@) " +
							"[@SeqNo@:" + m_SeqNo + " " +
							"@M_Product_ID@:\"" + m_ProductName + "\" " +
							"@Qty@=" + m_Qty.doubleValue() + " " + 
							"@QtyOrdered@=" + m_QtyOrdered.doubleValue() + " " +
							"@QtyDelivered@=" + m_QtyDelivered.doubleValue() + " " +
							"@QtyInTransit@=" + m_QtyLoc.doubleValue() + " " +
							"@Difference@=" + m_AvailableForOrder.doubleValue() + "]";
				} else if(
						//m_DeliveryRule.equals(X_C_Order.DELIVERYRULE_Availability)
						//&& 
						m_DiffQtyOnHand.signum() < 0) {
					errorMsg = "@QtyOnHand@ < (@Qty@ + @QtyInTransit@) " +
							"[@SeqNo@:" + m_SeqNo + " " +
							"@M_Product_ID@:\"" + m_ProductName + "\" " +
							"@QtyOnHand@=" + m_QtyOnHand.doubleValue() + " " +
							"@Qty@=" + m_Qty.doubleValue() + " " + 
							"@QtyInTransit@=" + m_QtyInTransit.doubleValue() + " " +
							"@Difference@=" + m_DiffQtyOnHand.doubleValue() + "]";
				}
				//	Add Error Message
				if(errorMsg != null) {
					//	Add New Line
					if(msg.length() > 0)
						msg.append(Env.NL);
					//	Add Msg
					msg.append("*")
						.append(errorMsg);
				}
			}
		} catch(Exception ex) {
			log.severe("validExcedeed() Error: " + ex.getMessage());
		} finally {
			DB.close(rs, ps);
			rs = null; ps = null;
		}
		//	Return
		return msg.length() > 0
				? msg.toString()
						: null;
	}
	
	/**
	 * Validate Weight and Volume
	 * @return String
	 */
	private String validWeightVolume() {
		//	Validate Weight
		if(getWeight() == null
				|| getWeight().equals(Env.ZERO))
			return "@Weight@ = @0@";
		
		//	and Volume distinct of 0
		if(getVolume() == null
				|| getVolume().equals(Env.ZERO))
			return "@Volume@ = @0@";
		//	Correct Validation
		if((getLoadCapacity().subtract(getWeight()).doubleValue() < 0))
				return "@Weight@ > @LoadCapacity@";

		if((getVolumeCapacity().subtract(getVolume()).doubleValue() < 0))
			return "@Volume@ > @VolumeCapacity@";
		return null;
	}

	/**
     *  Set Processed.
     *  Propagate to Lines
     *  @param processed processed
     */
    public void setProcessed (boolean processed)
    {
        super.setProcessed (processed);
        if (get_ID() <= 0)
            return;
        int noLine = DB.executeUpdateEx("UPDATE FTU_LoadOrderLine " +
        		"SET Processed=? " +
        		"WHERE FTU_LoadOrder_ID=?",
        		new Object[]{processed, get_ID()},
        		get_TrxName());
        m_lines = null;
        log.fine("setProcessed - " + processed + " - Lines=" + noLine);
    }   //  setProcessed
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp (System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1)		//	get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		
		m_processMsg = validReference();
		if(m_processMsg != null)
			return false;
		//	Set Status
		addDescription(Msg.getMsg(getCtx(), "Voided"));
		
		//	Valid Invoice and Delivered Reference
		if(getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)){
			//	Valid Invoice Reference
			m_processMsg = validInvoiceReference();
			if (m_processMsg != null)
				return false;
			
			//	Valid Delivered Reference
			m_processMsg = validDeliveredReference();
			if (m_processMsg != null)
				return false;
			
		} 

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
        setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * Valid Invoice reference
	 * @return
	 * @return String
	 */
	private String validInvoiceReference(){
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT i.DocumentNO FROM FTU_LoadOrderLine lol "
				+ " INNER JOIN C_InvoiceLine il ON (lol.C_InvoiceLine_ID = il.C_InvoiceLine_ID )"
				+ " INNER JOIN C_Invoice i ON (il.C_Invoice_ID = i.C_Invoice_ID) "
				+ " WHERE i.DocStatus NOT IN ('VO','RE') AND lol.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
		if(m_ReferenceNo != null) 
			return "@SQLErrorReferenced@ @C_Invoice_ID@: " + m_ReferenceNo;
		return null;
	}
	
	
	/**
	 * Valid Delivered reference
	 * @return
	 * @return String
	 */
	private String validDeliveredReference(){
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT io.DocumentNO FROM FTU_LoadOrderLine lol "
				+ " INNER JOIN M_InOutLine iol ON (lol.M_InOutLine_ID = iol.M_InOutLine_ID )"
				+ " INNER JOIN M_InOut io ON (iol.M_InOut_ID = io.M_InOut_ID)"
				+ " WHERE io.DocStatus NOT IN ('VO','RE') AND lol.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
		if(m_ReferenceNo != null) 
			return "@SQLErrorReferenced@ @M_InOut_ID@: " + m_ReferenceNo;
		return null;
	}
	
	/**
	 * Valid Reference in another record
	 * @return
	 * @return String
	 */
	private String validReference(){
		String m_ReferenceNo = null;
		//	Valid Record weight
		if(isWeightRegister()) {
			m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT MAX(rw.DocumentNo) " +
					"FROM FTU_RecordWeight rw " +
					"WHERE rw.DocStatus NOT IN('VO', 'RE','CL') " + 	
					"AND rw.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
			if(m_ReferenceNo != null)
				return "@SQLErrorReferenced@ @FTU_RecordWeight_ID@: " + m_ReferenceNo;
		}
		//	
		if(getOperationType().equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial)) {
			//	Valid Invoice
			m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT MAX(i.DocumentNo) " +
					"FROM FTU_LoadOrderLine lol " +
					"INNER JOIN C_InvoiceLine il ON(il.C_InvoiceLine_ID = lol.C_InvoiceLine_ID) " +
					"INNER JOIN C_Invoice i ON(i.C_Invoice_ID = il.C_Invoice_ID)" + 
					"WHERE i.DocStatus NOT IN('VO', 'RE','CL') " +	
					"AND lol.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
			if(m_ReferenceNo != null)
				return "@SQLErrorReferenced@ @C_Invoice_ID@: " + m_ReferenceNo;
			//	Valid Delivery
			m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT MAX(io.DocumentNo) " +
					"FROM FTU_LoadOrderLine lol " +
					"INNER JOIN M_InOutLine iol ON(iol.M_InOutLine_ID = lol.M_InOutLine_ID) " +
					"INNER JOIN M_InOut io ON(io.M_InOut_ID = iol.M_InOut_ID)" + 
					"WHERE io.DocStatus NOT IN('VO', 'RE','CL') " + 	
					"AND lol.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
			if(m_ReferenceNo != null)
				return "@SQLErrorReferenced@ @M_InOut_ID@: " + m_ReferenceNo;
		} else if(getOperationType().equals(X_FTU_LoadOrder.OPERATIONTYPE_MaterialOutputMovement)) {
			//	Valid Delivery
			m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT MAX(mo.DocumentNo) " +
					"FROM FTU_LoadOrderLine lol " +
					"INNER JOIN M_MovementLine mol ON(mol.M_MovementLine_ID = lol.M_MovementLine_ID) " +
					"INNER JOIN M_Movement mo ON(mo.M_Movement_ID = mol.M_Movement_ID)" + 
					"WHERE mo.DocStatus NOT IN('VO', 'RE','CL') " + 	
					"AND lol.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
			if(m_ReferenceNo != null)
				return "@SQLErrorReferenced@ @M_Movement_ID@: " + m_ReferenceNo;
		}
		//	
		return null;
	}
	
	/**
	 * Get Current Record Weight
	 * @return
	 * @return MFTURecordWeight
	 */
	public MFTURecordWeight getRecordWeight(){
		int m_FTU_RecordWeight_ID = DB.getSQLValue(get_TrxName(), "SELECT MAX(rw.FTU_RecordWeight_ID) " +
				"FROM FTU_RecordWeight rw " +
				"WHERE rw.DocStatus = 'CO' " +
				"AND rw.FTU_LoadOrder_ID = ?", getFTU_LoadOrder_ID());
		if(m_FTU_RecordWeight_ID <= 0)
			return null;
		//	Instance
		return new MFTURecordWeight(getCtx(), m_FTU_RecordWeight_ID, get_TrxName());
	}
	
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Quantities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);

		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		//	Void It
		voidIt();
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		return false;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		//	Void It
		voidIt();
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		m_processMsg = validReference();
		if(m_processMsg != null)
			return false;
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false; 
		
		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
	//	return getSalesRep_ID();
		return 0;
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return null;	//getTotalLines();
	}	//	getApprovalAmt
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID
	
	/**
     *  Add to Description
     *  @param description text
     */
    public void addDescription (String description)
    {
        String desc = getDescription();
        if (desc == null)
            setDescription(description);
        else
            setDescription(desc + " | " + description);
    }   //  addDescription
    
    /**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds)
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@param whereClause
	 *	@return lines
	 */
	public MFTULoadOrderLine[] getLines (boolean requery, String whereClause)
	{
		if (m_lines != null && !requery)
		{
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MFTULoadOrderLine> list = new Query(getCtx(), MFTULoadOrderLine.Table_Name, "FTU_LoadOrder_ID=?"
				+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(getFTU_LoadOrder_ID())
		.list();

		m_lines = new MFTULoadOrderLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
	/**
	 * Get Lines for Generate In Out
	 * @return
	 * @return MFTULoadOrderLine[] 
	 */
	public MFTULoadOrderLine[] getLinesForInOut() {
		//	SQL
		String sql = new String("SELECT lol.* "
				+ "FROM FTU_LoadOrderLine lol "
				+ "INNER JOIN C_OrderLine ol ON(ol.C_OrderLine_ID = lol.C_OrderLine_ID) "
				+ "WHERE lol.FTU_LoadOrder_ID = ? "
				+ "ORDER BY ol.C_BPartner_ID, ol.M_Warehouse_ID");
		//	Get
		PreparedStatement ps = null;
		ResultSet rs = null;
		//	List
		ArrayList<MFTULoadOrderLine> list = new ArrayList<MFTULoadOrderLine>();
		//	
		try {
			ps = DB.prepareStatement(sql.toString(), get_TrxName());
			ps.setInt(1, getFTU_LoadOrder_ID());
			rs = ps.executeQuery();
			//	
			while(rs.next()){
				list.add(new MFTULoadOrderLine(getCtx(), rs, get_TrxName()));
			}
		} catch(Exception ex) {
			log.severe("getLinesForInOut() Error: " + ex.getMessage());
		} finally {
			  DB.close(rs, ps);
		      rs = null; ps = null;
		}
		//	Convert
		MFTULoadOrderLine [] lines = new MFTULoadOrderLine[list.size ()];
		list.toArray(lines);
		return lines;
	}
	
	/**
	 * Get Shipments from Load Order
	 * @param p_FTU_LoadOrder_ID
	 * @return
	 * @return MInOut[]
	 */
	public MInOut[] getInOutFromLoadOrder(int p_FTU_LoadOrder_ID ) {
		//	SQL
		/*String sql = new String("SELECT io.* "
				+ " FROM FTU_LoadOrderLine lol "
				+ " INNER JOIN M_InOutLine iol ON (lol.M_InOutLine_ID = iol.M_InOutLine_ID)"
				+ " INNER JOIN M_InOut io ON (io.M_InOut_ID = iol.M_InOut_ID )"
				+ " WHERE lol.FTU_LoadOrder_ID = ?");*/
		
		String sql = "SELECT io.*"
				+ " FROM M_Inout io"
				+ " WHERE EXISTS("
				+ " SELECT 1 FROM FTU_LoadOrderLine lol"
				+ " INNER JOIN M_InoutLine iol ON iol.M_InoutLine_ID = lol.M_InoutLine_ID"
				+ " WHERE iol.M_Inout_ID = io.M_Inout_ID AND lol.FTU_LoadOrder_ID = ?"
				+ ")";
		
		//	Get
		PreparedStatement ps = null;
		ResultSet rs = null;
		//	List
		ArrayList<MInOut> list = new ArrayList<MInOut>();
		//	
		try {
			ps = DB.prepareStatement(sql.toString(), get_TrxName());
			ps.setInt(1, p_FTU_LoadOrder_ID);
			rs = ps.executeQuery();
			//	
			while(rs.next()){
				list.add(new MInOut(getCtx(), rs, get_TrxName()));
			}
		} catch(Exception ex) {
			log.severe("getInOutOfLoadOrder() Error: " + ex.getMessage());
		} finally {
			  DB.close(rs, ps);
		      rs = null; ps = null;
		}
		if(list.size() == 0 )
			return null;
		//	Convert
		MInOut [] lines = new MInOut[list.size ()];
		list.toArray(lines);
		return lines;
	}
	
	/**
	 * Get Lines For Movements
	 * @return
	 * @return MFTULoadOrderLine[]
	 */
	public MFTULoadOrderLine[] getLinesForMovement() {
		//	SQL
		String sql = new String("SELECT lol.* "
				+ "FROM FTU_LoadOrderLine lol "
				+ "INNER JOIN DD_OrderLine ol ON(ol.DD_OrderLine_ID = lol.DD_OrderLine_ID) "
				+ "INNER JOIN DD_Order o ON(o.DD_Order_ID = ol.DD_Order_ID) "
				+ "WHERE lol.FTU_LoadOrder_ID = ? "
				+ "ORDER BY o.C_BPartner_ID");
		//	Get
		PreparedStatement ps = null;
		ResultSet rs = null;
		//	List
		ArrayList<MFTULoadOrderLine> list = new ArrayList<MFTULoadOrderLine>();
		//	
		try {
			ps = DB.prepareStatement(sql.toString(), get_TrxName());
			ps.setInt(1, getFTU_LoadOrder_ID());
			rs = ps.executeQuery();
			//	
			while(rs.next()){
				list.add(new MFTULoadOrderLine(getCtx(), rs, get_TrxName()));
			}
		} catch(Exception ex) {
			log.severe("getLinesForInOut() Error: " + ex.getMessage());
		} finally {
			  DB.close(rs, ps);
		      rs = null; ps = null;
		}
		//	Convert
		MFTULoadOrderLine [] lines = new MFTULoadOrderLine[list.size ()];
		list.toArray(lines);
		return lines;
	}
	
	/**
	 * @return
	 * @return String
	 */
	private String validETReference(){
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT lo.DocumentNo " +
				"FROM FTU_LoadOrder lo " +
				"WHERE  lo.DocStatus IN ('" + DOCACTION_Complete + "','" + DOCACTION_Close + "')"
				+ "AND lo.FTU_EntryTicket_ID = ? "
				+ "AND lo.FTU_LoadOrder_ID != ? ", getFTU_EntryTicket_ID(), getFTU_LoadOrder_ID());
		String m_ReferenceNoET = DB.getSQLValueString(get_TrxName(), "SELECT et.DocumentNo "
				+ "FROM FTU_EntryTicket et "
				+ "WHERE et.FTU_EntryTicket_ID= ? ", getFTU_EntryTicket_ID());
		if(m_ReferenceNo != null) 
			return "@SQLErrorReferenced@ @FTU_LoadOrder_ID@: " + m_ReferenceNo + " @Generate@ @from@ @FTU_EntryTicket_ID@:" +m_ReferenceNoET;
		return null;		
	}
	
	/**
	 * Get Lines
	 * @param requery
	 * @return
	 * @return MFTULoadOrderLine[]
	 */
	public MFTULoadOrderLine[] getLines (boolean requery)
	{
		return getLines(requery, null);
	}	//	getLines
	
	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		//	Valid Document Action
		if (AD_Table_ID == Table_ID){
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid))
				{
					options[index++] = DocumentEngine.ACTION_Prepare;
					options[index++] = DocumentEngine.ACTION_Close;
					options[index++] = DocumentEngine.ACTION_Complete;
				}

			else if(docStatus.equals(DocumentEngine.STATUS_Completed)){
				options[index++] = DocumentEngine.ACTION_Close;
				options[index++] = DocumentEngine.ACTION_Void;
			}else
				options[index++] = DocumentEngine.ACTION_None;
		}
		
		return index;
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		super.beforeSave(newRecord);
		//	Add validation for trying to save the charge by not permitted to do so if the vehicle type is null. 
			
		String msg = null;
		
		//	Valid Operation Type
		if(getOperationType() == null)
			msg = "@OperationType@ @NotFound@";
		
		if(msg != null)
			throw new AdempiereException(msg);
		
		//	Set Values from ticket
		if(getFTU_EntryTicket_ID() > 0) {
			MFTUEntryTicket m_EntryTicket = (MFTUEntryTicket) getFTU_EntryTicket();
			MFTUVehicle m_Vehicle = (MFTUVehicle) m_EntryTicket.getFTU_Vehicle();
			setFTU_Driver_ID(m_EntryTicket.getFTU_Driver_ID());
			setFTU_Vehicle_ID(m_EntryTicket.getFTU_Vehicle_ID());
			setFTU_VehicleType_ID(m_Vehicle.getFTU_VehicleType_ID());
			setLoadCapacity(m_Vehicle.getLoadCapacity());
			setVolumeCapacity(m_Vehicle.getVolumeCapacity());
		} else if(getFTU_VehicleType_ID() > 0) {
			MFTUVehicleType m_VehicleType = (MFTUVehicleType) getFTU_VehicleType();
			setLoadCapacity(m_VehicleType.getLoadCapacity());
			setVolumeCapacity(m_VehicleType.getVolumeCapacity());
		}
		return true;
	}//	End beforeSave
}
