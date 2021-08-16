package net.frontuari.recordweight.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.PeriodClosedException;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.Env;

public class MFTUMovement extends MMovement implements DocOptions {
	
	private static final long serialVersionUID = 1L;

	public MFTUMovement (Properties ctx, int M_Movement_ID, String trxName)
	{
		super (ctx, M_Movement_ID, trxName);
		if (M_Movement_ID == 0)
		{
		//	setC_DocType_ID (0);
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setIsApproved (false);
			setIsInTransit (false);
			setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setPosted (false);
			super.setProcessed (false);
		}	
	}	//	MMovement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MFTUMovement (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MMovement

	/**
	 * 
	 */
	
	@Override
	public boolean voidIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
				
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
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
			
			//	Set lines to 0
			MMovementLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MMovementLine line = lines[i];
				BigDecimal old = line.getMovementQty();
				if (old.compareTo(Env.ZERO) != 0)
				{
					line.setMovementQty(Env.ZERO);
					line.addDescription("Void (" + old + ")");
					line.saveEx(get_TrxName());
				}
			}
		}
		else
		{
			boolean accrual = false;
			try 
			{
				MPeriod.testPeriodOpen(getCtx(), getMovementDate(), getC_DocType_ID(), getAD_Org_ID());
			}
			catch (PeriodClosedException e) 
			{
				accrual = true;
			}
			
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
	}	//	voidIt
	
	@Override
	public boolean reverseCorrectIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reverseCorrect
		
		if(get_ValueAsInt("FTU_RecordWeight_ID") > 0) {
			m_processMsg = "No puedes Reversar este movimiento, tiene un registro de peso Asociado";
			return false;
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		MMovement reversal = reverse(false);
		if (reversal == null)
			return false;
		
		m_processMsg = reversal.getDocumentNo();
		
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		return true;
	}	//	reverseCorrectionIt/	rejectIt
	
	@Override
	public boolean reverseAccrualIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		if(get_ValueAsInt("FTU_RecordWeight_ID") > 0) {
			m_processMsg = "No puedes Reversar este movimiento, tiene un registro de peso Asociado";
			return false;
		}
		
		MMovement reversal = reverse(true);
		if (reversal == null)
			return false;
		
		m_processMsg = reversal.getDocumentNo();
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		return true;
	}	//	reverseAccrualIt
	
	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
		// Valid Document Action
		if (AD_Table_ID == Table_ID) {
			if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid)) {
				options[index++] = DocumentEngine.ACTION_Prepare;
				options[index++] = DocumentEngine.ACTION_Complete;
				options[index++] = DocumentEngine.ACTION_Void;
			}
			// Complete .. CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
				options[index++] = DocumentEngine.ACTION_Close;
				options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}

		return index;
	}

}
