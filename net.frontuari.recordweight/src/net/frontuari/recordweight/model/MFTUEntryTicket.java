package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.PeriodClosedException;
import org.compiere.model.MDocType;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
//import org.compiere.model.MInvoice;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MFTUEntryTicket extends X_FTU_EntryTicket implements DocAction, DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8055602654407679580L;
	private boolean m_justPrepared;
	private String m_processMsg;

	public MFTUEntryTicket(Properties ctx, int FTU_EntryTicket_ID, String trxName) {
		super(ctx, FTU_EntryTicket_ID, trxName);
	}

	public MFTUEntryTicket(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	@Override
	public boolean processIt(String processAction) throws Exception {
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(processAction, getDocAction());
	}

	@Override
	public boolean unlockIt() {
		if (log.isLoggable(Level.INFO))
			log.info("unlockIt - " + toString());
		setProcessed(false);
		return true;
	}

	@Override
	public boolean invalidateIt() {
		if (log.isLoggable(Level.INFO))
			log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}

	@Override
	public String prepareIt() {

		if (log.isLoggable(Level.INFO))
			log.info(toString());
		
		MPeriod.testPeriodOpen(getCtx(), getDateDoc(), getC_DocType_ID(), getAD_Org_ID());

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;


		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction())) {
			setDocAction(DOCACTION_Complete);
		}
		return STATUS_InProgress;
	}

	@Override
	public boolean approveIt() {
		log.info((new StringBuilder("approveIt - ")).append(toString()).toString());
		setIsApproved(true);
		return true;
	}

	@Override
	public boolean rejectIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		setIsApproved(false);
		return true;
	}

	@Override
	public String completeIt() {
		// Re-Check
		if (!m_justPrepared) {
			String status = prepareIt();
			if (!STATUS_InProgress.equals(status)) {
				return status;
			}
		}
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		// Implicit Approval
		if (!isApproved()) {
			approveIt();
		}
		if (log.isLoggable(Level.INFO))
			log.info(toString());

		// User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null) {
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		setDefiniteDocumentNo();
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1) {
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			}
			if (index != -1) {
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			}
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	@Override
	public boolean voidIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());

		if (DOCSTATUS_Closed.equals(getDocStatus()) || DOCSTATUS_Reversed.equals(getDocStatus())
				|| DOCSTATUS_Voided.equals(getDocStatus())) {
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		// Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus()) || DOCSTATUS_Invalid.equals(getDocStatus())
				|| DOCSTATUS_InProgress.equals(getDocStatus()) || DOCSTATUS_Approved.equals(getDocStatus())
				|| DOCSTATUS_NotApproved.equals(getDocStatus())) {
			// Before Void
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID);
			if (m_processMsg != null)
				return false;

			addDescription(Msg.getMsg(getCtx(), "Voided"));

			
			m_processMsg = validRWReference();
			if (m_processMsg != null) {
				return false;
			}
//			Valid Load Order Completed
			if (getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
					|| getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)
					|| getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)) {
				m_processMsg = validLOReference();
				if (m_processMsg != null)
					return false;
			}
			//

		} else {
			boolean accrual = false;
			try {
				MPeriod.testPeriodOpen(getCtx(), getDateDoc(), getC_DocType_ID(), getAD_Org_ID());
			} catch (PeriodClosedException e) {
				accrual = true;
			}

			if (accrual)
				return reverseAccrualIt();
			else
				return reverseCorrectIt();
		}

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;

	}

	private void addDescription(String description) {

		String desc = getDescription();
		if (desc == null) {
			setDescription(description);
		} else {
			setDescription((new StringBuilder(String.valueOf(desc))).append(" | ").append(description).toString());
		}

	}

	private String validLOReference() {
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(),
				"SELECT MAX(lo.FTU_LoadOrder_ID) FROM FTU_LoadOrder lo WHERE lo.DocStatus NOT IN("
						+ "'VO', 'RE') AND lo.FTU_EntryTicket_ID = ?",
				getFTU_EntryTicket_ID());
		if (m_ReferenceNo != null) {
			return (new StringBuilder("@SQLErrorReferenced@ @FTU_LoadOrder_ID@: ")).append(m_ReferenceNo).toString();
		} else {
			return null;
		}
	}

	private String validRWReference() {
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(),
				"SELECT MAX(rw.DocumentNo) FROM FTU_RecordWeight rw WHERE rw.DocStatus IN('CO', '"
						+ "CL') AND rw.FTU_EntryTicket_ID = ?",
				getFTU_EntryTicket_ID());
		if (m_ReferenceNo != null) {
			return (new StringBuilder("@SQLErrorReferenced@ @FTU_RecordWeight_ID@: ")).append(m_ReferenceNo).toString();
		} else {
			return null;
		}
	}
	
	private String validAnalysisReference() {
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(),
				"SELECT MAX(rw.DocumentNo) FROM HRS_Analysis rw WHERE rw.DocStatus IN('CO', '"
						+ "CL') AND rw.FTU_EntryTicket_ID = ?",
				getFTU_EntryTicket_ID());
		if (m_ReferenceNo != null) {
			return (new StringBuilder("@SQLErrorReferenced@ Analisis: ")).append(m_ReferenceNo).toString();
		} else {
			return null;
		}
	}

	@Override
	public boolean closeIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;
		return true;
	}

	@Override
	public boolean reverseCorrectIt() {

		if (log.isLoggable(Level.INFO))
			log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		m_processMsg = validRWReference();
		if (m_processMsg != null)
			return false;
		m_processMsg = validLOReference();
		if (m_processMsg != null)
			return false;
		m_processMsg = validAnalysisReference();
		if (m_processMsg != null)
			return false;

		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		m_processMsg = getDocumentNo();

		return true;
	}

	@Override
	public boolean reverseAccrualIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		m_processMsg = validRWReference();
		if (m_processMsg != null)
			return false;
		m_processMsg = validLOReference();
		if (m_processMsg != null)
			return false;
		m_processMsg = validAnalysisReference();
		if (m_processMsg != null)
			return false;
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		m_processMsg = getDocumentNo();

		return true;
	}

	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

		MPeriod.testPeriodOpen(getCtx(), getDateDoc(), getC_DocType_ID(), getAD_Org_ID());

		m_processMsg = validRWReference();
		if (m_processMsg != null) {
			return false;
		}
		// Valid Load Order Completed
		if (getOperationType().equals(OPERATIONTYPE_DeliveryBulkMaterial)
				|| getOperationType().equals(OPERATIONTYPE_DeliveryFinishedProduct)
				|| getOperationType().equals(OPERATIONTYPE_MaterialOutputMovement)) {
			m_processMsg = validLOReference();
			if (m_processMsg != null)
				return false;
		}
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}	//	reActivateIt
	

	@Override
	public String getSummary() {
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		// - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}

	@Override
	public String getDocumentInfo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		StringBuilder msgreturn = new StringBuilder().append(dt.getNameTrl()).append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}

	@Override
	public File createPDF() {
		return null;
	}

	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public int getDoc_User_ID() {
		return getCreatedBy();
	}

	@Override
	public int getC_Currency_ID() {
		return Env.getContextAsInt(getCtx(), "$C_Currency_ID");
	}

	@Override
	public BigDecimal getApprovalAmt() {
		return null;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
		// Valid Document Action
		if (AD_Table_ID == Table_ID) {
			if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid)) {
				options[index++] = DocumentEngine.ACTION_Prepare;
			}
			// Complete .. CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
				options[index++] = DocumentEngine.ACTION_Void;
				options[index++] = DocumentEngine.ACTION_ReActivate;
			}
		}

		return index;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		super.beforeSave(newRecord);
		if (newRecord)
			setIsPrinted(false);

		String msg = null;

		if (getOperationType() == null)
			msg = "@FTU_EntryTicket_ID@ @NotFound@";

		if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_RawMaterialReceipt)) {

			if (getC_BPartner_ID() == 0)
				msg = "@C_BPartner_ID@ @NotFound@";

		} else if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_MaterialInputMovement)) {

			if (getFTU_LoadOrder_ID() == 0)
				msg = "@FTU_LoadOrder_ID@ @NotFound@";

		} else if (getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_ProductBulkReceipt)
				|| getOperationType().equals(X_FTU_EntryTicket.OPERATIONTYPE_ReceiptMoreThanOneProduct)) {

			if (getC_Order_ID() == 0)
				msg = "@C_Order_ID@ @NotFound@";

			else if (getC_BPartner_ID() == 0)
				msg = "@C_BPartner_ID@ @NotFound@";
		}
		if (msg != null)
			throw new AdempiereException(msg);
		return true;
	}

	/**
	 * Get Load Order from Entry Ticket
	 * @param whereClause
	 * @return
	 * @return MFTULoadOrder[]
	 */
	public MFTULoadOrder[] getLoadOrder(String whereClause) {
		List<MFTULoadOrder> list = new Query(getCtx(), 
				I_FTU_LoadOrder.Table_Name, "FTU_EntryTicket_ID=?"
						+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(getFTU_EntryTicket_ID())
		.list();

		MFTULoadOrder[] m_lo = new MFTULoadOrder[list.size ()];
		list.toArray (m_lo);
		return m_lo;
	}	//	getLoadOrder
	
	/**
	 * Get Bill of Lading from Entry Ticket
	 * @author Jorge Colmenarez, 2021-08-31 16:02
	 * @param whereClause
	 * @return MFTUFreightCost[]
	 */
	public MFTUFreightCost[] getFreightCost(String whereClause) {
		List<MFTUFreightCost> list = new Query(getCtx(), 
				I_FTU_FreightCost.Table_Name, "FTU_EntryTicket_ID=?"
						+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(getFTU_EntryTicket_ID())
		.list();

		MFTUFreightCost[] m_bol = new MFTUFreightCost[list.size ()];
		list.toArray (m_bol);
		return m_bol;
	}	//	getFreightCost
	
	/**
	 * Get Record Weight from Entry Ticket
	 * @author Jorge Colmenarez, 2021-10-28 14:37
	 * @param whereClause
	 * @return MFTURecordWeight[]
	 */
	public MFTURecordWeight[] getRecordWeight(String whereClause) {
		List<MFTURecordWeight> list = new Query(getCtx(), 
				I_FTU_RecordWeight.Table_Name, "FTU_EntryTicket_ID=?"
						+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(getFTU_EntryTicket_ID())
		.list();

		MFTURecordWeight[] m_rw = new MFTURecordWeight[list.size ()];
		list.toArray (m_rw);
		return m_rw;
	}	//	getLoadOrder
	
}
