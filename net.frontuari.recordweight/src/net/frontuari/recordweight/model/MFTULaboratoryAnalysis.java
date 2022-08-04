package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MColumn;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;

import net.frontuari.mfta.model.MFTULabCultiveResult;
import net.frontuari.mfta.model.X_FTU_Functions_Formule;
import net.frontuari.mfta.model.X_FTU_Quality_Param;

public class MFTULaboratoryAnalysis extends X_FTU_Laboratory_Analysis implements DocOptions, DocAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4276061755271094110L;

	public MFTULaboratoryAnalysis(Properties ctx, int FTU_Laboratory_Analysis_ID, String trxName) {
		super(ctx, FTU_Laboratory_Analysis_ID, trxName);
	}

	public MFTULaboratoryAnalysis(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
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
	
	/** Process Message */
	private String m_processMsg = null;
	/** Just Prepared Flag */
	private boolean m_justPrepared = false;

	@Override
	public boolean processIt(String action) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(action, getDocAction());
	}

	@Override
	public boolean unlockIt() {
		log.info("unlockIt - " + toString());
		return true;
	}

	@Override
	public boolean invalidateIt() {
		log.info("invalidateIt - " + toString());
		return true;
	}
	
	/**
	 * @author Argenis Rodríguez
	 * @return Error Msg
	 */
	private String validateETReferenceDuplicated() {
		
		String referenceNo = DB.getSQLValueString(get_TrxName()
				, "SELECT DocumentNo FROM FTU_Laboratory_Analysis"
				 + " WHERE DocStatus NOT IN ('VO','RE')"
				 + " AND FTU_EntryTicket_ID = ? AND FTU_Laboratory_Analysis_ID<>?"
				, getFTU_EntryTicket_ID(), get_ID());
		
		if (referenceNo != null)
		{
			MFTUEntryTicket entryTicket = new MFTUEntryTicket(getCtx(), getFTU_EntryTicket_ID(), get_TrxName());
			return "@SQLErrorReferenced@ @FTU_EntryTicket_ID@ " + entryTicket.getDocumentNo()
					+ " @Generate@ @from@ @FTU_Laboratory_Analysis_ID@ " + referenceNo;
		}
		
		return null;
	}
	
	/** Lines					*/
	private MFTULaboratoryAnalisysLine[]		m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@param whereClause
	 *	@return lines
	 */
	public MFTULaboratoryAnalisysLine[] getLines (String whereClause)
	{
		List<MFTULaboratoryAnalisysLine> list = new Query(getCtx(), I_FTU_Laboratory_A_Line.Table_Name, "FTU_Laboratory_Analysis_ID=?"
				+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(get_ID())
		.setOrderBy(MFTULaboratoryAnalisysLine.COLUMNNAME_FTU_Laboratory_A_Line_ID)
		.list();
		
		return list.toArray(new MFTULaboratoryAnalisysLine[list.size()]);
	}	//	getLines
	
	/**
	 * 	Get Lines of Order
	 * 	@param requery requery
	 * 	@param orderBy optional order by column
	 * 	@return lines
	 */
	public MFTULaboratoryAnalisysLine[] getLines (boolean requery, String whereClause)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		m_lines = getLines(whereClause);
		return m_lines;
	}	//	getLines
	
	/***
	 * Clean input Code
	 * @author Jorge Colmenarez, 2022-08-04 19:05
	 * @param code
	 * @return code cleaned
	 */
	private String cleanCode(String code) {
		code=code.toLowerCase();
		code=code.replaceAll("#(\\d+)", "0");
		code=code.replaceAll("&(\\d+)", "0");
		code=code.replaceAll("@(\\d+)", "0");
		code=code.replaceAll("%", "");
		code=code.replaceAll(";", "");
		return code;
	}
	
	/***
	 * get ID Function from code
	 * @param code
	 * @return ID Function
	 */
	private String getIdFunction(String code) {
		Pattern regex = Pattern.compile("@(\\d+)");
        Matcher regexMatcher = regex.matcher(code);
        regexMatcher.find();
        
		return regexMatcher.group().replaceAll("@", "");	
	}
	
	/***
	 * get ID Formula from code
	 * @author Jorge Colmenarez, 2022-08-04 19:06
	 * @param code
	 * @return ID Formula
	 */
	private String getIdFormula(String code) {
		Pattern regex = Pattern.compile("F(\\d+)");
        Matcher regexMatcher = regex.matcher(code);
        regexMatcher.find();
        
		return regexMatcher.group().replaceAll("F", "");	
	}
	
	/***
	 * Replace Table and Column from Script Code
	 * @author Jorge Colmenarez, 2022-08-04 18:49
	 * @param code
	 * @param FTU_EntryTicket_ID
	 * @return code
	 */
	private String replaceTableAndColumn(String code, int FTU_EntryTicket_ID) {
		Pattern regex = Pattern.compile("&\\d+");
        Matcher regexMatcher = regex.matcher(code);
        int ColumnID;
        String ColumnName;
        String TableName;
        String ColumnSql="";
        
        while (regexMatcher.find()) {
        	ColumnID=Integer.parseInt(regexMatcher.group().replaceAll("&", ""));
        	MColumn col = new MColumn(getCtx(), ColumnID, get_TrxName());
        	TableName = col.getAD_Table().getTableName();
        	ColumnName = col.getColumnName();
        	ColumnSql +=TableName+"."+ColumnName+" as \""+ColumnID+"\",";
        }
        //	Remove last comma (,)
        if(ColumnSql!="")
        	ColumnSql = ColumnSql.substring(0,ColumnSql.length()-1);
        
        if(ColumnSql!="")
        {
        	String sql = "SELECT "+ColumnSql+" FROM FTU_RecordWeight " +
        			" LEFT JOIN FTU_Laboratory_Analysis ON FTU_Laboratory_Analysis.FTU_Laboratory_Analysis_ID = FTU_RecordWeight.FTU_Laboratory_Analysis_ID " + 
					" LEFT JOIN AD_Org ON AD_Org.AD_Org_ID =FTU_RecordWeight.AD_Org_ID " + 
					" LEFT JOIN AD_Client ON AD_Client.AD_Client_ID = FTU_RecordWeight.AD_Client_ID " + 
					" LEFT JOIN C_BPartner ON C_BPartner.C_BPartner_ID  = FTU_RecordWeight.C_BPartner_ID " + 
					" LEFT JOIN FTU_Recipe ON FTU_Recipe.FTU_Recipe_ID = FTU_Laboratory_Analysis.FTU_Recipe_ID " + 
					" LEFT JOIN AD_User ON AD_User.C_BPartner_ID = C_BPartner.C_BPartner_ID " +
					" WHERE FTU_RecordWeight.FTU_EntryTicket_ID="+FTU_EntryTicket_ID;
        	PreparedStatement pstmt = null;
        	ResultSet rs = null;
        	ResultSetMetaData rsMetaData = null;
        	try {
        		pstmt = DB.prepareStatement(sql, get_TrxName());
        		rs = pstmt.executeQuery();
        		rsMetaData = rs.getMetaData();
        		final int columnCount = rsMetaData.getColumnCount();
        		rs.next();
        		String name;
        		for(int i = 1;i<=columnCount;i++){
        			name = rsMetaData.getColumnName(i);
			        code=code.replaceAll("&("+name+")", "'"+rs.getObject(i).toString()+"'");
        		}
        	}catch(Exception e)
        	{
        		throw new AdempiereException(e.getMessage());
        	}
        	finally {
        		DB.close(rs, pstmt);
        		rs = null; pstmt = null;
        		rsMetaData = null;
        	}
        }
		return code;
	}
	
	/***
	 * Get parameter to function from code
	 * @author Jorge Colmenarez, 2022-08-04 19:07
	 * @param code
	 * @return parameter function
	 */
	private String getFunctionParameter(String code) {
		Pattern regex = Pattern.compile("'.+\\'");
        Matcher regexMatcher = regex.matcher(code);
        regexMatcher.find();
		return regexMatcher.group().replaceAll("'", "");	
	}

	/***
	 * Added Variable into Function from FuntionCode
	 * @author Jorge Colmenarez, 2022-08-04 19:07
	 * @param functionCode
	 * @param code
	 * @return code with variable
	 */
	private String addVariableIntoFunction(String functionCode,String code) {
		String param=	getFunctionParameter(code);
		functionCode	=	functionCode.replaceAll("\\$var1", param);
		code	=	code.replaceAll("'.+\\'", functionCode);
		return code;
	}
	
	/***
	 * processing Functions from code
	 * @author Jorge Colmenarez, 2022-08-04 19:07
	 * @param code
	 * @return code processed
	 */
	private String processFunctions(String code) {
		Pattern regex = Pattern.compile("@(\\d+\\(.+\\))");
        Matcher regexMatcher = regex.matcher(code);
        
        while (regexMatcher.find()) {
        	int ffID=Integer.parseInt(getIdFunction(regexMatcher.group()));
        	
        	X_FTU_Functions_Formule ff = new X_FTU_Functions_Formule(getCtx(), ffID, get_TrxName());
        	
        	if(ff.get_ID()>0)
        	{
        		code = addVariableIntoFunction(ff.getDescription().toLowerCase(), code);
        		return code.replaceAll("@(\\d+)", "");
        	}
        }
        
	    return code;	
	}
	
	/***
	 * Replace Basic Code
	 * @param code
	 * @return code clean
	 */
	private String replaceBasicCode(String code) {
		//	Get Lines to Lab Analysis
		for(MFTULaboratoryAnalisysLine line : getLines(true, ""))
		{
			String value = line.getFTU_Analysis_Type().getValue();
			String result = line.getResult().toString();
			code=code.replaceAll("#("+value+")", result);
		}
		return code;
	}
	
	/***
	 * Extract Formulas from Code
	 * @param code
	 * @return code with formulas
	 */
	private String getFormulas(String code)
	{
		Pattern regex = Pattern.compile("F(\\d+)");
        Matcher regexMatcher = regex.matcher(code);
        String contenidoFormula;
        while (regexMatcher.find()) {
        	int idCampo=Integer.parseInt(getIdFormula(regexMatcher.group()));
        	X_FTU_Quality_Param qp = new X_FTU_Quality_Param(getCtx(), idCampo, get_TrxName());
        	if(qp.get_ID()>0) {
        		contenidoFormula = qp.getCode().toLowerCase();
            	code=code.replaceAll("F"+idCampo, "("+contenidoFormula+")");
        	}
        }
		
		return code;
	}
	
	/***
	 * Process Clasifications of Analysis
	 * @author Jorge Colmenarez, 2022-08-04 17:42
	 */
	private void clasification() {
		//	Delete Cultive Result
		String dSql = "DELETE FROM FTU_Lab_Cultive_Result WHERE FTU_Laboratory_Analysis_ID=?";
		DB.executeUpdate(dSql, get_ID(), true, get_TrxName());
		//	Create Cultive Result
		String sql="SELECT qp.ftu_quality_param_id,name,code "
				+ "FROM FTU_Quality_Param qp WHERE qp.FTU_Recipe_ID=? AND qp.IsActive = 'Y'";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DB.prepareStatement(sql, get_TrxName());
			pst.setInt(1, getFTU_Recipe_ID());
			rs = pst.executeQuery();
			while(rs.next())
			{
				String code=rs.getString("code");
				int ftu_quality_param_id=rs.getInt("ftu_quality_param_id");
				//	Replace Code
				code = getFormulas(code);
				code = replaceBasicCode(code);
				code = replaceTableAndColumn(code, getFTU_EntryTicket_ID());
				code = processFunctions(code);
				code = cleanCode(code);
				//	Execute Code
				String sqlCode = "SELECT ("+code+") AS result";
				String result = DB.getSQLValueString(get_TrxName(), sqlCode);
				if(result!=null) {
					MFTULabCultiveResult lcr = new MFTULabCultiveResult(getCtx(), 0, get_TrxName());
					lcr.setAD_Org_ID(getAD_Org_ID());
					lcr.setFTU_Laboratory_Analysis_ID(get_ID());
					lcr.setFTU_Quality_Param_ID(ftu_quality_param_id);
					lcr.setresult_human(result.toUpperCase());
					lcr.setresult_system(result);
					lcr.saveEx();
				}
			}
		}catch(Exception e)
		{
			throw new AdempiereException(e.getLocalizedMessage());
		}finally {
			DB.close(rs, pst);
			rs = null;
			pst = null;
		}
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
		
		//	Clasificar
		clasification();
		
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
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null) {
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
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

		return true;
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

	@Override
	public boolean reActivateIt() {
		log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}

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
		return getDocumentNo();
	}
	
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

}
