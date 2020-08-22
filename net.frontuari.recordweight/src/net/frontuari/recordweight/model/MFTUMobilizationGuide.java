package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.MDocType;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 *
 */
public class MFTUMobilizationGuide extends X_FTU_MobilizationGuide implements DocAction, DocOptions {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2057588336894788596L;

	/**
	 * *** Constructor ***
	 * @param ctx
	 * @param FTU_MobilizationGuide_ID
	 * @param trxName
	 */
	public MFTUMobilizationGuide(Properties ctx, int FTU_MobilizationGuide_ID,
			String trxName) {
		super(ctx, FTU_MobilizationGuide_ID, trxName);
	}

	/**
	 * *** Constructor ***
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MFTUMobilizationGuide(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
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
		//setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		//setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * Valid Reference in another record
	 */
	public void ValidRecordWeight()
	{
		
		String sql ;
		int p_FTU_RecordWeight_ID = 0;
		
		sql = "SELECT rw.FTU_RecordWeight_ID"
				+ " FROM FTU_RecordWeight rw"
				+ " WHERE"
				+ "		rw.DocStatus IN('IP', 'CO')"
				+ "		AND rw.FTU_LoadOrder_ID = ?"; 
		
		p_FTU_RecordWeight_ID = DB.getSQLValue(get_TrxName(), 
				sql,getFTU_LoadOrder_ID());
		setFTU_RecordWeight_ID(p_FTU_RecordWeight_ID);
		saveEx();
		//	
		if(p_FTU_RecordWeight_ID <= 0)
		{
			m_processMsg = "@FTU_RecordWeight_ID@ @NotFound@";
		}
	}
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
		
		//	Valid Qty
		/*m_processMsg = validQtyToDeliver();
		if(m_processMsg != null)
			return DocAction.STATUS_Invalid;*/
		//	Verify Mandatory References
		if(getFTU_VehicleType_ID() == 0)
			m_processMsg = "@FTU_LoadOrder_ID@ @NotFound@";
		if(isSOTrx()) {
			if(getFTU_LoadOrder_ID() == 0)
				m_processMsg = "@FTU_LoadOrder_ID@ @NotFound@";
			else if(getFTU_RecordWeight_ID() == 0) {
				MFTULoadOrder loadOrder = new MFTULoadOrder(getCtx(), getFTU_LoadOrder_ID(), get_TrxName());
				if(loadOrder.isHandleRecordWeight())
					ValidRecordWeight();
			} else 
				m_processMsg = validMGReference();
		}
		//	Return
		if(m_processMsg != null)
			return DocAction.STATUS_Invalid;
		//	Implicit Approval
		//if (!isApproved())
			//approveIt();
		log.info(toString());
		//
		
		//	Add Support to complete exists guide outside
		if(isSOTrx()
				&& (getExt_Guide() == null
						|| getExt_Guide().trim().length() == 0)){
			m_processMsg = "@Ext_Guide@ @NotFound@";
			return DOCACTION_Invalidate;
		}
		
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
		
		addDescription(Msg.getMsg(getCtx(), "Voided"));
		//setQtyToDeliver(Env.ZERO);

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
        setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
	
	/**
	 * Valid Reference in another record
	 * @return
	 * @return String
	 */
	private String validReference(){
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT MAX(et.DocumentNo) " +
				"FROM FTU_EntryTicket et " +
				"WHERE et.DocStatus NOT IN('VO', 'IP') " +
				"AND et.FTU_MobilizationGuide_ID = ?", getFTU_MobilizationGuide_ID());
		if(m_ReferenceNo != null)
			return "@SQLErrorReferenced@ @FTU_EntryTicket_ID@: " + m_ReferenceNo;
		return null;
	}
	
	/**
	 * Valid if exists another guide
	 * @return
	 * @return String
	 */
	private String validMGReference(){
		String m_ReferenceNo = DB.getSQLValueString(get_TrxName(), "SELECT MAX(mg.DocumentNo) " +
				"FROM FTU_MobilizationGuide mg " +
				"WHERE mg.DocStatus NOT IN('VO', 'RE','CL') " +
				"AND mg.FTU_LoadOrder_ID = ? AND mg.FTU_MobilizationGuide_ID != ? ", getFTU_LoadOrder_ID(), getFTU_MobilizationGuide_ID());
		if(m_ReferenceNo != null)
			return "@SQLErrorReferenced@ @FTU_MobilizationGuide_ID@: " + m_ReferenceNo + " @Generate@ @from@ @FTU_LoadOrder_ID@";
		return null;
	}
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
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
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"QtyToDeliver")).append("=").append(getQtyToDeliver());
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
	 * Valid Quantity to Deliver
	 * @return
	 * @return String
	 */
	/*
	private String validQtyToDeliver(){
		if(getQtyToDeliver() == null
				|| getQtyToDeliver().equals(Env.ZERO))
			return "@QtyToDeliver@ = @0@";

		MClientInfo m_ClientInfo = MClientInfo.get(getCtx());
		if(m_ClientInfo.getC_UOM_Weight_ID() == 0)
			return "@C_UOM_Weight_ID@ = @NotFound@";
		
		MFTUFarming m_Farming = new MFTUFarming(getCtx(), getFTU_Farming_ID(), get_TrxName());
		//	Get Category
		MProduct product = MProduct.get(getCtx(), m_Farming.getCategory_ID());
		//	Rate Convert
		BigDecimal rate = MUOMConversion.getProductRateFrom(Env.getCtx(), 
				product.getM_Product_ID(), m_ClientInfo.getC_UOM_Weight_ID());
		//	Valid Conversion
		if(rate == null)
			return "@NoUOMConversion@";
		
		if(getQtyToDeliver().multiply(rate)
				.compareTo(getFTU_VehicleType().getLoadCapacity()) > 0) 
			return "@QtyToDeliver@ > @LoadCapacity@ @of@ @FTU_VehicleType_ID@";
		
		//	Max Warehouse Receipt
		BigDecimal m_MaxReceipt = DB.getSQLValueBD(get_TrxName(), "SELECT rc.Qty - SUM(COALESCE(mg.QtyToDeliver, 0)) " +
				"FROM FTU_ReceptionCapacity rc " +
				"LEFT JOIN FTU_MobilizationGuide mg ON(mg.M_Warehouse_ID = rc.M_Warehouse_ID) " +
				"WHERE rc.AD_Org_ID = ? " +
				"AND rc.M_Warehouse_ID = ? " +
				"AND rc.ValidFrom <= ? " +
				"AND rc.IsActive = 'Y' " +
				"AND mg.DateDoc >= rc.ValidFrom " +
				"AND mg.DateDoc<COALESCE((SELECT Min(rcs.ValidFrom) FROM FTU_ReceptionCapacity rcs WHERE rc.M_WareHouse_ID= rcs.M_WareHouse_ID AND rc.AD_Org_ID=rcs.AD_Org_ID AND rcs.ValidFrom > rc.ValidFrom),now()) " +
				"AND (mg.DocStatus IN('CO', 'CL') OR mg.DocStatus IS NULL) " +
				"GROUP BY rc.Qty, rc.ValidFrom " +
				"ORDER BY rc.ValidFrom DESC", getAD_Org_ID(), getM_Warehouse_ID(), getDateDoc());
		
		log.fine("MaxReceipt=" + m_MaxReceipt);
		//	Valid Max Receipt
		if(m_MaxReceipt != null
				&& m_MaxReceipt.compareTo(Env.ZERO) <= 0)
			return "@FTU_ReceptionCapacity_ID@ <= @0@";
		//	Valid Quantity To Deliver
		BigDecimal m_Qty = m_Farming.getQty();
		BigDecimal m_Re_EstimatedQty = m_Farming.getRe_EstimatedQty();
		//	
		if(m_Re_EstimatedQty == null)
			m_Re_EstimatedQty = Env.ZERO;
		if(m_Re_EstimatedQty.compareTo(m_Qty) >= 0)
			m_Re_EstimatedQty = m_Re_EstimatedQty.subtract(m_Qty);
		
		//	Quantity Delivered
		BigDecimal m_QtyDelivered = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(mg.QtyToDeliver) " +
				"FROM FTU_MobilizationGuide mg " +
				"WHERE mg.FTU_Farming_ID = ?" +
				"AND mg.DocStatus IN('CO', 'CL') ", 
				getFTU_Farming_ID());
		
		log.fine("WeightDelivered=" + m_QtyDelivered);
		
		//	Valid Quantity Delivered
		if(m_QtyDelivered == null)
			m_QtyDelivered = Env.ZERO;
		
		//	Max Quantity to Generate
		BigDecimal m_MaxQtyToDeliver = m_Qty.add(m_Re_EstimatedQty)
									.subtract(m_QtyDelivered);
		//	Valid To Deliver
		if(m_MaxQtyToDeliver.compareTo(Env.ZERO) <= 0)
			return "@Qty@ <= @QtyToDeliver@";
		//	Valid the Minimum to Generate
		if(m_MaxReceipt != null
				&& m_MaxReceipt.compareTo(m_MaxQtyToDeliver) <= 0)
			m_MaxQtyToDeliver = m_MaxReceipt;
		//	Verify Quantity to Deliver with Max to Deliver
		if(getQtyToDeliver().compareTo(m_MaxQtyToDeliver) > 0)
			return "@QtyToDeliver@ > (@EstimatedQty@ - @QtyDelivered@):"
					+ " \n@EstimatedQty@=" + m_Qty.doubleValue() 
					+ " \n@QtyDelivered@=" + m_QtyDelivered.doubleValue() 
					+ " \n@QtyToDeliver@=" + getQtyToDeliver().doubleValue();
		return null;
	}*/

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
				}
				//	Complete                    ..  CO
				else if (docStatus.equals(DocumentEngine.STATUS_Completed))
				{
					options[index++] = DocumentEngine.ACTION_Void;
					options[index++] = DocumentEngine.ACTION_ReActivate;
				}
		}
		
		return index;
	}
	
	@Override
	public String toString() {
		return "DocumentNo=" + getDocumentNo();
	}
	@Override
	protected boolean beforeSave(boolean newRecord) {
		boolean ok = super.beforeSave(newRecord);
		if(newRecord
				|| (is_ValueChanged(X_FTU_LoadOrder.COLUMNNAME_FTU_LoadOrder_ID))){
			String sql ;
			int p_C_BPartner_ID = 0;
			
			sql = "SELECT o.C_BPartner_ID"
					+ " FROM C_Order o"
					+ " INNER JOIN C_OrderLine  ol ON (o.C_Order_ID = ol.C_Order_ID) "
					+ " INNER JOIN FTU_LoadOrderLine lol ON (ol.C_OrderLine_ID = lol.C_OrderLine_ID)"
					+ " INNER JOIN FTU_LoadOrder lo ON (lol.FTU_LoadOrder_ID = lo.FTU_LoadOrder_ID)"
					+ " WHERE"
					+ "		lo.OperationType = ?"
					+ "		AND lo.FTU_LoadOrder_ID = "
					+ getFTU_LoadOrder_ID(); 
			
			p_C_BPartner_ID = DB.getSQLValue(get_TrxName(), 
					sql,X_FTU_LoadOrder.OPERATIONTYPE_DeliveryBulkMaterial);
			
			if(p_C_BPartner_ID > 0){
				setC_BPartner_ID(p_C_BPartner_ID);
			}
		}
		/*
		if(newRecord
				|| (is_ValueChanged(X_FTU_Farming.COLUMNNAME_FTU_Farming_ID))){
			String sql ;
			int p_C_BPartner_ID = 0;
			
			sql = "SELECT f.C_BPartner_ID"
					+ " FROM FTU_Farm f"
					+ " INNER JOIN FTU_FarmDivision fd ON (f.FTU_Farm_ID = fd.FTU_Farm_ID)"
					+ " INNER JOIN FTU_Farming fa ON (fd.FTU_FarmDivision_ID = fa.FTU_FarmDivision_ID)"
					+ " WHERE"
					+ "		fa.FTU_Farming_ID = "
					+ getFTU_Farming_ID(); 
			
			p_C_BPartner_ID = DB.getSQLValue(null, sql);
			
			if(p_C_BPartner_ID > 0){
				setC_BPartner_ID(p_C_BPartner_ID);
			}
		}*/

		return ok;
	}//	End beforeSave
	
}
