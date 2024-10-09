package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.PeriodClosedException;
import org.compiere.model.MConversionRate;
import org.compiere.model.MDocType;
import org.compiere.model.MInventory;
import org.compiere.model.MInvoice;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

public class MFTUShipperLiquidation extends X_FTU_ShipperLiquidation implements DocAction, DocOptions {

	private static final long serialVersionUID = -1573520670473483651L;

	public MFTUShipperLiquidation(Properties ctx, int FTU_ShipperLiquidation_ID, String trxName) {
		super(ctx, FTU_ShipperLiquidation_ID, trxName);
		
	}

	public MFTUShipperLiquidation(Properties ctx, int FTU_ShipperLiquidation_ID, String trxName,
			String... virtualColumns) {
		super(ctx, FTU_ShipperLiquidation_ID, trxName, virtualColumns);
		
	}

	public MFTUShipperLiquidation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		
	}
	
	/**	Liquidation Lines			*/
	private MFTUShipperLiquidationLine[]	m_lines;
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;
	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else{
			StringBuilder msgd = new StringBuilder(desc).append(" | ").append(description);
			setDescription(msgd.toString());
		}
	}	//	addDescription
	
	@Override
	protected boolean beforeDelete() {
		boolean success = true;
		//	Delete Lines
		for(MFTUShipperLiquidationLine line : getLines())
			line.deleteEx(true);
		
		return success;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
		// Valid Document Action
		if (AD_Table_ID == Table_ID) {
			if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_NotApproved)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid)) {
				options[index++] = DocumentEngine.ACTION_Prepare;
				options[index++] = DocumentEngine.ACTION_Approve;
				options[index++] = DocumentEngine.ACTION_Complete;
				options[index++] = DocumentEngine.ACTION_Void;
			}
			else if (docStatus.equals(DocumentEngine.STATUS_InProgress)) {
				options[index++] = DocumentEngine.ACTION_Approve;
				options[index++] = DocumentEngine.ACTION_Complete;
				options[index++] = DocumentEngine.ACTION_Void;
			}
			// Complete .. CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
				options[index++] = DocumentEngine.ACTION_Close;
				options[index++] = DocumentEngine.ACTION_Void;
				options[index++] = DocumentEngine.ACTION_ReActivate;
			}
		}

		return index;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MFTUShipperLiquidation[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append(",GrandTotal=").append(getGrandTotal());
		if (m_lines != null)
			sb.append(" (#").append(m_lines.length).append(")");
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateTrx(TimeUtil.getDay(0));
			MPeriod.testPeriodOpen(getCtx(), getDateTrx(), getC_DocType_ID(), getAD_Org_ID());
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
			if (value != null)
				setDocumentNo(value);
		}
	}
	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	@Override
	public boolean processIt(String action) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (action, getDocAction());
	}

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	@Override
	public boolean unlockIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	@Override
	public boolean invalidateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	@Override
	public String prepareIt() { 
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		MPeriod.testPeriodOpen(getCtx(), getDateTrx(), getC_DocType_ID(), getAD_Org_ID());

		//	Lines
		MFTUShipperLiquidationLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		
		if (getC_DocType_ID() == 0)
		{
			m_processMsg = "No Document Type";
			return DocAction.STATUS_Invalid;
		}
		
		if (get_ValueAsBoolean("isPrepayment")) {
		//	Delete Deductions
		deleteDeduction(get_TrxName());
		//	Get Deductions of CxP
		createDeduction(get_TrxName());
		//	Get Inventory of CxC
		createInventory(get_TrxName());
		//	Get Inventory of CxC
		createInvoice(get_TrxName());
		//	Get PrePayments
		createPrepayments(get_TrxName());
		}
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Add up Amounts
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	@Override
	public boolean  approveIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	@Override
	public boolean rejectIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	@Override
	public String completeIt() {
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	@Override
	public boolean voidIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			// Before Void
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
			if (m_processMsg != null)
				return false;
			log.warning("Documento Actual="+this.getDocumentNo());
			//	Set lines to 0
			MFTUShipperLiquidationLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MFTUShipperLiquidationLine line =  lines[i];
				log.warning("Linea (ID)="+line.getFTU_ShipperLiquidation_ID());
				
				line.setAmount(BigDecimal.ZERO);
				line.saveEx(get_TrxName());
			}
			addDescription(Msg.getMsg(getCtx(), "Voided"));
			//	Delete Deductions
			deleteDeduction(get_TrxName());
		}
		else
		{
			boolean accrual = false;
			try 
			{
				MPeriod.testPeriodOpen(getCtx(), getDateTrx(), getC_DocType_ID(), getAD_Org_ID());
			}
			catch (PeriodClosedException e) 
			{
				accrual = true;
			}
			
			//	Added by Jorge Colmenarez, 2024-09-05 14:34
			//	Validated that not paid
			if(getFTU_PaymentRequest_ID()>0) {
				int cnt = DB.getSQLValue(get_TrxName(), "SELECT COUNT(*) FROM FTU_PaymentRequest WHERE DocStatus NOT IN ('RE','VO') AND FTU_PaymentRequest_ID = ?", getFTU_PaymentRequest_ID());
				if(cnt>0)
					throw new AdempiereException("No es posible anular la liquidacion, porque esta asociada a una solicitud de pagos.");
			}else {
				int cnt = DB.getSQLValue(get_TrxName(), "SELECT COUNT(prl.*) FROM FTU_PaymentRequest pr JOIN FTU_PaymentRequestLine prl ON (pr.FTU_PaymentRequest_ID = prl.FTU_PaymentRequest_ID) WHERE pr.DocStatus NOT IN ('RE','VO') AND prl.FTU_Liquidation_ID = ?", get_ID());
				if(cnt>0)
					throw new AdempiereException("No es posible anular la liquidacion, porque esta asociada a una solicitud de pagos.");
			}
			//	End Jorge Colmenarez
			
			if (accrual)
				return reverseAccrualIt();
			else
				return reverseCorrectIt();
		}

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	@Override
	public boolean closeIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;
		return true;
	}

	/**
	 * 	Reverse Correction - same date
	 * 	@return true if success
	 */
	@Override
	public boolean reverseCorrectIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		setDocStatus(DOCSTATUS_Reversed);
		setDocAction(DOCACTION_None);
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		return true;
	}

	/**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	@Override
	public boolean reverseAccrualIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		setDocStatus(DOCSTATUS_Reversed);
		setDocAction(DOCACTION_None);
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		return true;
	}

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		// Test period
		MPeriod.testPeriodOpen(getCtx(), getDateTrx(), getC_DocType_ID(), getAD_Org_ID());
		
		//	Added by Jorge Colmenarez, 2024-09-05 14:34
		//	Validated that not paid
		if(getFTU_PaymentRequest_ID()>0) {
			int cnt = DB.getSQLValue(get_TrxName(), "SELECT COUNT(*) FROM FTU_PaymentRequest WHERE DocStatus NOT IN ('RE','VO') AND FTU_PaymentRequest_ID = ?", getFTU_PaymentRequest_ID());
			if(cnt>0)
				throw new AdempiereException("No es posible anular la liquidacion, porque esta asociada a una solicitud de pagos.");
		}else {
			int cnt = DB.getSQLValue(get_TrxName(), "SELECT COUNT(prl.*) FROM FTU_PaymentRequest pr JOIN FTU_PaymentRequestLine prl ON (pr.FTU_PaymentRequest_ID = prl.FTU_PaymentRequest_ID) WHERE pr.DocStatus NOT IN ('RE','VO') AND prl.FTU_Liquidation_ID = ?", get_ID());
			if(cnt>0)
				throw new AdempiereException("No es posible anular la liquidacion, porque esta asociada a una solicitud de pagos.");
		}
		//	End Jorge Colmenarez

		//	Delete Deductions
		deleteDeduction(get_TrxName());
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}

	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		//	: Grand Total = 123.00 (#1)
		sb.append(": ").
			append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	@Override
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		StringBuilder msgreturn = new StringBuilder()
				.append(dt.getNameTrl()).append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}	//	getDocumentInfo

	/**************************************************************************
	 * 	Create PDF
	 *	@return File or null
	 */
	@Override
	public File createPDF() {
		try
		{
			StringBuilder msgfile = new StringBuilder().append(get_TableName()).append(get_ID()).append("_");
			File temp = File.createTempFile(msgfile.toString(), ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		ReportEngine re = ReportEngine.get (getCtx(), 99, getFTU_ShipperLiquidation_ID(), get_TrxName());
		if (re == null)
			return null;
		MPrintFormat format = re.getPrintFormat();
		// We have a Jasper Print Format
		// ==============================
		if(format.getJasperProcess_ID() > 0)	
		{
			ProcessInfo pi = new ProcessInfo ("", format.getJasperProcess_ID());
			pi.setRecord_ID ( getFTU_ShipperLiquidation_ID() );
			pi.setIsBatch(true);
			
			ServerProcessCtl.process(pi, null);
			
			return pi.getPDFReport();
		}
		// Standard Print Format (Non-Jasper)
		// ==================================
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	@Override
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//getProcessMsg

	/**
	 * Set process message
	 * @param processMsg
	 */
	public void setProcessMessage(String processMsg)
	{
		m_processMsg = processMsg;
	}
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	@Override
	public int getDoc_User_ID() {
		return getAD_User_ID();
	}

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	@Override
	public BigDecimal getApprovalAmt() {
		return getGrandTotal();
	}
	
	/**
	 * 	Get Liquidation Lines of Liquidation
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MFTUShipperLiquidationLine[] getLines (String whereClause)
	{
		String whereClauseFinal = " FTU_ShipperLiquidation_ID=? AND (FTU_FreightCost_ID IS NOT NULL OR C_ORDER_ID IS NOT NULL) ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		log.warning("Documento Actual="+getFTU_ShipperLiquidation_ID());
		List<MFTUShipperLiquidationLine> list = new Query(getCtx(), I_FTU_SLLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getFTU_ShipperLiquidation_ID())
										.setOrderBy("FTU_SLLine_ID")
										.list();		
		return list.toArray(new MFTUShipperLiquidationLine[list.size()]);
	}	//	getLines
	
	/**
	 * 	Get Liquidation Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MFTUShipperLiquidationLine[] getLines (boolean requery)
	{
		if (m_lines == null || m_lines.length == 0 || requery)
		{
			m_lines = getLines(null);
		}
		set_TrxName(m_lines, get_TrxName());
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Get Lines of Shipper Liquidations
	 * 	@return lines
	 */
	public MFTUShipperLiquidationLine[] getLines()
	{
		return getLines(false);
	}	//	getLines

	/****
	 * Create Deduction of API
	 */
	private void createDeduction(String trxName) {
		String whereClause = "C_BPartner_ID = (SELECT C_BPartner_ID FROM M_Shipper WHERE M_Shipper_ID = ?) AND DocStatus = 'CO' AND IsSOTrx = 'N' "
				+ "AND C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE IsAffectedBook = 'N' AND DocBaseType = 'APC') "
				+ "AND invoiceopen(C_Invoice_ID,0) < 0 "
				+ "AND NOT EXISTS (SELECT 1 FROM FTU_SLLine JOIN FTU_ShipperLiquidation ON (FTU_ShipperLiquidation.FTU_ShipperLiquidation_ID = FTU_SLLine.FTU_ShipperLiquidation_ID) "
				+ "WHERE FTU_SLLine.C_Invoice_ID = C_Invoice.C_Invoice_ID AND FTU_SLLine.DeductionType='01' AND FTU_ShipperLiquidation.DocStatus NOT IN ('RE','VO'))";
		List<MInvoice> list = new Query(getCtx(), MInvoice.Table_Name, whereClause, trxName)
				.setParameters(getM_Shipper_ID())
				.setOrderBy("DateInvoiced,DocumentNo")
				.list();
		if(list.size()>0) {
			MInvoice[] invoices = list.toArray(new MInvoice[list.size()]);
			for(MInvoice i : invoices) {
				MFTUShipperLiquidationLine d = new MFTUShipperLiquidationLine(getCtx(),0,trxName);
				d.setAD_Org_ID(getAD_Org_ID());
				d.setFTU_ShipperLiquidation_ID(get_ID());
				d.setDeductionType(MFTUShipperLiquidationLine.DEDUCTIONTYPE_DeductionByAP);
				d.setC_Invoice_ID(i.get_ID());
				d.setDescription(i.getDocumentInfo());
				BigDecimal grandTotal = MConversionRate.convert(getCtx(), i.getGrandTotal(), i.getC_Currency_ID()
						, getC_Currency_ID(), i.getDateInvoiced(),getC_ConversionType_ID()
						, getAD_Client_ID(), getAD_Org_ID());
				if(grandTotal==null)
					grandTotal = BigDecimal.ZERO;
				d.setAmount(grandTotal);
				d.saveEx(trxName);
			}
		}
	}
	
	/****
	 * Create Invoice of ARI
	 */
	private void createInvoice(String trxName) {
		String whereClause = "C_BPartner_ID = (SELECT C_BPartner_ID FROM M_Shipper WHERE M_Shipper_ID = ?) AND DocStatus = 'CO' AND IsSOTrx = 'Y' "
				+ "AND C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE IsAffectedBook = 'N' AND DocBaseType = 'ARI') "
				+ "AND invoiceopen(C_Invoice_ID,0) > 0 "
				+ "AND NOT EXISTS (SELECT 1 FROM FTU_SLLine JOIN FTU_ShipperLiquidation ON (FTU_ShipperLiquidation.FTU_ShipperLiquidation_ID = FTU_SLLine.FTU_ShipperLiquidation_ID) "
				+ "WHERE FTU_SLLine.C_Invoice_ID = C_Invoice.C_Invoice_ID AND FTU_SLLine.DeductionType='02' AND FTU_ShipperLiquidation.DocStatus NOT IN ('RE','VO'))";
		List<MInvoice> list = new Query(getCtx(), MInvoice.Table_Name, whereClause, trxName)
				.setParameters(getM_Shipper_ID())
				.setOrderBy("DateInvoiced,DocumentNo")
				.list();
		if(list.size()>0) {
			MInvoice[] invoices = list.toArray(new MInvoice[list.size()]);
			for(MInvoice i : invoices) {
				MFTUShipperLiquidationLine d = new MFTUShipperLiquidationLine(getCtx(),0,trxName);
				d.setAD_Org_ID(getAD_Org_ID());
				d.setFTU_ShipperLiquidation_ID(get_ID());
				d.setDeductionType(MFTUShipperLiquidationLine.DEDUCTIONTYPE_DeductionByAC);
				d.setC_Invoice_ID(i.get_ID());
				d.setDescription(i.getDocumentInfo());
				BigDecimal grandTotal = MConversionRate.convert(getCtx(), i.getGrandTotal(), i.getC_Currency_ID()
						, getC_Currency_ID(), i.getDateInvoiced(),getC_ConversionType_ID()
						, getAD_Client_ID(), getAD_Org_ID());
				if(grandTotal==null)
					grandTotal = BigDecimal.ZERO;
				d.setAmount(grandTotal);
				d.saveEx(trxName);
			}
		}
	}
	
	/****
	 * Create Inventory of CxC
	 */
	private void createInventory(String trxName) {
		String whereClause = "M_Shipper_ID = ? AND DocStatus = 'CO' AND IsInternal = 'Y' "
				+ "AND C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE DocBaseType = 'MMI' AND DocSubTypeInv = 'IU') "
				+ "AND NOT EXISTS (SELECT 1 FROM FTU_SLLine JOIN FTU_ShipperLiquidation ON (FTU_ShipperLiquidation.FTU_ShipperLiquidation_ID = FTU_SLLine.FTU_ShipperLiquidation_ID) "
				+ "WHERE FTU_SLLine.M_Inventory_ID = M_Inventory.M_Inventory_ID AND FTU_SLLine.DeductionType = '02' AND FTU_ShipperLiquidation.DocStatus NOT IN ('RE','VO')) ";
		List<MInventory> list = new Query(getCtx(), MInventory.Table_Name, whereClause, trxName)
				.setParameters(getM_Shipper_ID())
				.setOrderBy("MovementDate,DocumentNo")
				.list();
		if(list.size()>0) {
			MInventory[] invoices = list.toArray(new MInventory[list.size()]);
			for(MInventory i : invoices) {
				MFTUShipperLiquidationLine d = new MFTUShipperLiquidationLine(getCtx(),0,trxName);
				d.setAD_Org_ID(getAD_Org_ID());
				d.setFTU_ShipperLiquidation_ID(get_ID());
				d.setDeductionType(MFTUShipperLiquidationLine.DEDUCTIONTYPE_DeductionByAC);
				d.setM_Inventory_ID(i.get_ID());
				d.setDescription(i.getDocumentInfo());
				d.setAmount(getInventoryCost(i.get_ID(),getC_Currency_ID(),get_TrxName()));
				d.saveEx(trxName);
			}
		}
	}
	
	/****
	 * Create Prepayments
	 */
	private void createPrepayments(String trxName) {
		String sql = "SELECT C_Payment_ID,DocumentNo,DateTrx,paymentavailable(C_Payment_ID) * -1 AS Prepayment,C_Currency_ID "
				+ "FROM C_Payment "
				+ "WHERE C_BPartner_ID = (SELECT C_BPartner_ID FROM M_Shipper WHERE M_Shipper_ID = ?) AND DocStatus = 'CO' AND IsReceipt = 'N' "
				+ "AND paymentavailable(C_Payment_ID) < 0 "
				+ "AND NOT EXISTS (SELECT 1 FROM FTU_SLLine JOIN FTU_ShipperLiquidation ON (FTU_ShipperLiquidation.FTU_ShipperLiquidation_ID = FTU_SLLine.FTU_ShipperLiquidation_ID) "
				+ "WHERE FTU_SLLine.C_Payment_ID = C_Payment.C_Payment_ID AND FTU_SLLine.DeductionType = '03' AND FTU_ShipperLiquidation.DocStatus NOT IN ('RE','VO')) "
				+ "ORDER BY DateTrx,DocumentNo ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, getM_Shipper_ID());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MFTUShipperLiquidationLine d = new MFTUShipperLiquidationLine(getCtx(),0,trxName);
				d.setAD_Org_ID(getAD_Org_ID());
				d.setFTU_ShipperLiquidation_ID(get_ID());
				d.setDeductionType(MFTUShipperLiquidationLine.DEDUCTIONTYPE_PrePayments);
				d.setC_Payment_ID(rs.getInt(1));
				d.setDescription(rs.getString(2)+" - "+rs.getTimestamp(3));
				BigDecimal grandTotal = MConversionRate.convert(getCtx(), rs.getBigDecimal(4), rs.getInt(5)
						, getC_Currency_ID(), rs.getTimestamp(3),getC_ConversionType_ID()
						, getAD_Client_ID(), getAD_Org_ID());
				if(grandTotal==null)
					grandTotal = BigDecimal.ZERO;
				d.setAmount(grandTotal);
				d.saveEx(trxName);
			}
		}catch (Exception e) {
			setProcessMessage("Error al crear los anticipos: "+e.getLocalizedMessage());
		}finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}		
	}
	
	public void deleteDeduction(String trxName) {
		String whereClauseFinal = "FTU_ShipperLiquidation_ID=? AND FTU_FreightCost_ID IS NULL AND C_Order_ID IS NULL";
		List<MFTUShipperLiquidationLine> list = new Query(getCtx(), MFTUShipperLiquidationLine.Table_Name, whereClauseFinal, trxName)
										.setParameters(getFTU_ShipperLiquidation_ID())
										.setOrderBy("FTU_SLLine_ID")
										.list();		
		if(list.size()>0) {
			MFTUShipperLiquidationLine[] deductions = list.toArray(new MFTUShipperLiquidationLine[list.size()]);
			for(MFTUShipperLiquidationLine line : deductions)
				line.deleteEx(true,trxName);
		}
	}
	
	/***
	 * get Inventory Cost
	 * @param InventoryID
	 * @param CurrencyID
	 * @param trxName
	 * @return
	 */
	private BigDecimal getInventoryCost(int InventoryID, int CurrencyID, String trxName) {
		BigDecimal costs = BigDecimal.ZERO;
		
		String sql = "SELECT SUM(cd.CurrentCostPrice) FROM M_CostDetail cd "
				+ "JOIN M_InventoryLine il ON (cd.M_InventoryLine_ID = il.M_InventoryLine_ID) "
				+ "JOIN C_AcctSchema a ON (cd.C_AcctSchema_ID = a.C_AcctSchema_ID) "
				+ "WHERE il.M_Inventory_ID = ? AND a.C_Currency_ID = ?";
		
		costs = DB.getSQLValueBD(trxName, sql, new Object[] {InventoryID,CurrencyID});
		if(costs ==null)
			costs = BigDecimal.ZERO;
		
		return costs;
	}
	
}
