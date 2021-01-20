/**
 * 
 */
package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.MAttribute;
import org.compiere.model.MAttributeInstance;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MDocType;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author jruiz
 * @author <a href="mailto:dixonalvarezm@gmail.com">Dixon Martinez</a></a>
 * @see
 *	<li><a href="https://bitbucket.org/djmartinez/record-weight/issues/8/validar-estatus-de-analisis"> ER [ 8 ] Validar estatus de Analisis</a></li>
*/
public class MHRSAnalysis extends X_HRS_Analysis implements DocAction, DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4930029951632955910L;
	
	public static final String COLUMNNAME_IsApprovedAnalysis = "IsApprovedAnalysis";

	/**
	 * @param ctx
	 * @param HRS_Analysis_ID
	 * @param trxName
	 */
	public MHRSAnalysis(Properties ctx, int HRS_Analysis_ID, String trxName) {
		super(ctx, HRS_Analysis_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MHRSAnalysis(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** Process Message */
	private String m_processMsg = null;
	/** Just Prepared Flag */
	private boolean m_justPrepared = false;

	@Override
	public boolean processIt(String processAction) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(processAction, getDocAction());
	}

	 
	@Override
	public boolean unlockIt() {
		log.info("unlockIt - " + toString());
		// setProcessing(false);
		return true;
	}

	@Override
	public boolean invalidateIt() {
		log.info("invalidateIt - " + toString());
		// setDocAction(DOCACTION_Prepare);
		return true;
	}
	
	/**
	 * @author Argenis Rodríguez
	 * @return Error Msg
	 */
	private String validateETReferenceDuplicated() {
		
		String referenceNo = DB.getSQLValueString(get_TrxName()
				, "SELECT DocumentNo FROM HRS_Analysis"
				 + " WHERE DocStatus NOT IN ('VO','RE')"
				 + " AND FTU_EntryTicket_ID = ? AND HRS_Analysis_ID<>?"
				, getFTU_EntryTicket_ID(), get_ID());
		
		if (referenceNo != null)
		{
			MFTUEntryTicket entryTicket = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			return "@SQLErrorReferenced@ @FTU_EntryTicket_ID@ " + entryTicket.getDocumentNo()
					+ " @Generate@ @from@ @HRS_Analisys_ID@ " + referenceNo;
		}
		
		return null;
	}

	@Override
	public String prepareIt() {
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		MPeriod.testPeriodOpen(getCtx(), getDateDoc(), getC_DocType_ID(), getAD_Org_ID());
		//	ER [ 8 ]
		//Add Validation by Argenis Rodríguez
		m_processMsg = validateETReferenceDuplicated();
		
		if (m_processMsg != null)
			return STATUS_Invalid;
		//End By Argenis Rodríguez
		
		String valid = validateAnalysis();
		String status = X_HRS_Analysis.STATUS_Completed;
		if (valid != null) {
			if(getDescription() != null 
					&& getDescription().length() > 0) {
				setDescription(getDescription() + valid );
			} else {
				setDescription(valid);
			}
			//	ER [ 8 ]
			status = X_HRS_Analysis.STATUS_Error;
		}
		//	ER [ 8 ]
		setStatus(status);
		setIsValidAnalysis(valid == null);
		//setStatus(X_HRS_Analysis.STATUS_InProgress);
		//setIsValidAnalysis(false);
		
		saveEx();
		
		// Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}

	@Override
	public boolean approveIt() {
		log.info("approveIt - " + toString());
		return true;
	}

	@Override
	public boolean rejectIt() {
		log.info("rejectIt - " + toString());
		return true;
	}

	@Override
	public String completeIt() {
		// Re-Check
		if (!m_justPrepared) {
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		setDescription(null);
		
		/*String valid = validateAnalysis();
		String status = X_HRS_Analysis.STATUS_Completed;
		if (valid != null) {
			if(getDescription() != null 
					&& getDescription().length() > 0) {
				setDescription(getDescription() + valid );
			} else {
				setDescription(valid);
			}
			//	ER [ 8 ]
			status = X_HRS_Analysis.STATUS_Error;
		}
		//	ER [ 8 ]
		setStatus(status);
		setIsValidAnalysis(valid == null);*/
		
		saveEx();
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null) {
			return DocAction.STATUS_Invalid;
		}

		setDefiniteDocumentNo();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	/**
	 * @return
	 */
	private String validateAnalysis() {
		StringBuilder msg = new StringBuilder();
		MAttributeSetInstance setInstance = new MAttributeSetInstance(getCtx(), getAnalysis_ID(), get_TrxName());
		MAttributeSet attributeSet = setInstance.getMAttributeSet();
		MAttribute [] atributes = attributeSet.getMAttributes(true);
		for (MAttribute mAttribute : atributes) {
			MHRSQualityParameter qualityParameter = MHRSQualityParameter.getMHRSQualityParameter(getCtx(), mAttribute.getM_Attribute_ID(), getM_Product_ID(), get_TrxName());
			if(qualityParameter == null)
				continue;
			MAttributeInstance attributeInstance = mAttribute.getMAttributeInstance(setInstance.getM_AttributeSetInstance_ID());
			if(attributeInstance != null) {
				BigDecimal value = attributeInstance.getValueNumber();
				
				if(value != null) {
					if(value.compareTo(qualityParameter.getLowerLimit()) < 0 
							|| value.compareTo(qualityParameter.getUpperLimit()) > 0) {
						msg
							.append( mAttribute.getName() )
							.append(" = " )
							.append(value)
							.append(" @LowerLimit@ = " )
							.append(qualityParameter.getLowerLimit())
							.append(" @AND@ @UpperLimit@ = ")
							.append(qualityParameter.getUpperLimit())
							.append(Env.NL); 
					}
				}
			}
		}
		if(msg.length() > 0) 
			return Msg.parseTranslation(getCtx(), msg.toString());
		return null;
	}

	/**
	 * Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1) // get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	@Override
	public boolean voidIt() {
		log.info("voidIt - " + toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}

	@Override
	public boolean closeIt() {
		log.info("closeIt - " + toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;
		// Add Support for closed Load Order

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
		log.info("reverseCorrectIt - " + toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		// Void It
		voidIt();
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		log.info("reverseAccrualIt - " + toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		// Void It
		voidIt();
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		return true;
	}

	/**
	 * Re-activate
	 * 
	 * @return true if success
	 */
	public boolean reActivateIt() {
		log.info("reActivateIt - " + toString());
		// Before reActivate

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		//	ER [ 8 ]
		setStatus(X_HRS_Analysis.STATUS_InProgress);
		setIsValidAnalysis(false);
		saveEx();
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	} // reActivateIt 

	/**
	 * Create PDF file
	 * 
	 * @param file output file
	 * @return file if success
	 */
	public File createPDF(File file) {
		return null;
	} // createPDF

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
		return dt.getName() + " " + getDocumentNo();
	}

	@Override
	public File createPDF() {
		try {
			File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
			return createPDF(temp);
		} catch (Exception e) {
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}

	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public int getDoc_User_ID() {
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		return 0;
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
				options[index++] = DocumentEngine.ACTION_Close;

			} else if (docStatus.equals(DocumentEngine.STATUS_Closed))
				options[index++] = DocumentEngine.ACTION_None;
		}

		return index;
	}

	public static int getByEntryTicket(int p_FTU_EntryTicket_ID) {
		String sql = "SELECT "
				+ "HRS_AnalySis_ID "
				+ "FROM HRS_Analysis "
				+ "WHERE "
				+ "FTU_EntryTicket_ID = ? "
				+ "AND DocStatus IN ('CO', 'IP') " 
				+ "AND (IsValidAnalysis = 'Y' OR IsApprovedAnalysis = 'Y')";
		return DB.getSQLValue(null, sql, p_FTU_EntryTicket_ID);
	}

}
