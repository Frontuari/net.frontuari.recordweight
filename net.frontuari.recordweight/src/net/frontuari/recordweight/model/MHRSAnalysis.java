/**
 * 
 */
package net.frontuari.recordweight.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MPeriod;
import org.compiere.model.MQuery;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PrintInfo;
import org.compiere.model.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;

/**
 * @author jruiz
 * @author <a href="mailto:dixonalvarezm@gmail.com">Dixon Martinez</a></a>
 * @see
 *	<li><a href="https://bitbucket.org/djmartinez/record-weight/issues/8/validar-estatus-de-analisis"> ER [ 8 ] Validar estatus de Analisis</a></li>
 * @author Jorge Colmenarez, 2022-11-04 17:42, Frontuari, C.A.
 * @commentary Support for Analysis Type Result and Valuations
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
	
	/** Lines					*/
	private MHRSAnalysisLine[]		m_lines = null;
	/** Lines					*/
	private MHRSAnalysisValuation[]		m_vlines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@param whereClause
	 *	@return lines
	 */
	public MHRSAnalysisLine[] getLines (String whereClause)
	{
		List<MHRSAnalysisLine> list = new Query(getCtx(), I_HRS_AnalysisLine.Table_Name, "HRS_Analysis_ID=?"
				+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(get_ID())
		.setOrderBy(MHRSAnalysisLine.COLUMNNAME_HRS_AnalysisLine_ID)
		.list();
		
		return list.toArray(new MHRSAnalysisLine[list.size()]);
	}	//	getLines
	
	/**
	 * 	Get Lines of Order
	 * 	@param requery requery
	 * 	@param orderBy optional order by column
	 * 	@return lines
	 */
	public MHRSAnalysisLine[] getLines (boolean requery, String whereClause)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		m_lines = getLines(whereClause);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Get Valuation Lines
	 *	@param requery requery
	 *	@param whereClause
	 *	@return lines
	 */
	public MHRSAnalysisValuation[] getValuationLines (String whereClause)
	{
		List<MHRSAnalysisValuation> list = new Query(getCtx(), I_HRS_AnalysisValuation.Table_Name, "HRS_Analysis_ID=?"
				+ (whereClause != null && whereClause.length() != 0? " AND " + whereClause: ""), get_TrxName())
		.setParameters(get_ID())
		.setOrderBy(MHRSAnalysisValuation.COLUMNNAME_HRS_AnalysisValuation_ID)
		.list();
		
		return list.toArray(new MHRSAnalysisValuation[list.size()]);
	}	//	getLines
	
	/**
	 * 	Get Valuation Lines of Analysis
	 * 	@param requery requery
	 * 	@param orderBy optional order by column
	 * 	@return lines
	 */
	public MHRSAnalysisValuation[] getValuationLines (boolean requery, String whereClause)
	{
		if (m_vlines != null && !requery) {
			set_TrxName(m_vlines, get_TrxName());
			return m_vlines;
		}
		m_vlines = getValuationLines(whereClause);
		return m_vlines;
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
        			" LEFT JOIN HRS_Analysis ON HRS_Analysis.HRS_Analysis_ID = FTU_RecordWeight.HRS_Analysis_ID " + 
					" LEFT JOIN AD_Org ON AD_Org.AD_Org_ID =FTU_RecordWeight.AD_Org_ID " + 
					" LEFT JOIN AD_Client ON AD_Client.AD_Client_ID = FTU_RecordWeight.AD_Client_ID " +  
					" LEFT JOIN M_Product ON M_Product.M_Product_ID = HRS_Analysis.M_Product_ID " +
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
        			if(isInstance(rs.getObject(i), BigDecimal.class))
        				code=code.replaceAll("&("+name+")", rs.getObject(i).toString());
        			else
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
	 * Check Instance of Object
	 * @author Jorge Colmenarez, 2022-08-05 10:57
	 * @param obj
	 * @param type
	 * @return true or false
	 */
	private boolean isInstance(Object obj, Class<?> type)
	{
		return type.isInstance(obj);
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
        	
        	X_FTU_FormuleFunction ff = new X_FTU_FormuleFunction(getCtx(), ffID, get_TrxName());
        	
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
		for(MHRSAnalysisLine line : getLines(true, ""))
		{
			String value = line.getFTU_AnalysisType().getValue();
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
        	X_FTU_QualityParam qp = new X_FTU_QualityParam(getCtx(), idCampo, get_TrxName());
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
		String dSql = "DELETE FROM HRS_AnalysisValuation WHERE HRS_Analysis_ID=?";
		DB.executeUpdate(dSql, get_ID(), true, get_TrxName());
		//	Create Cultive Result
		String sql="SELECT qp.FTU_QualityParam_id,name,code "
				+ "FROM FTU_QualityParam qp WHERE qp.M_Product_ID=? AND qp.IsActive = 'Y'";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DB.prepareStatement(sql, get_TrxName());
			pst.setInt(1, getM_Product_ID());
			rs = pst.executeQuery();
			while(rs.next())
			{
				String code=rs.getString("code");
				int FTU_QualityParam_id=rs.getInt("FTU_QualityParam_id");
				MFTUQualityParam qparam = new MFTUQualityParam(getCtx(), FTU_QualityParam_id, get_TrxName());
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
					MHRSAnalysisValuation lcr = new MHRSAnalysisValuation(getCtx(), 0, get_TrxName());
					lcr.setAD_Org_ID(getAD_Org_ID());
					lcr.setHRS_Analysis_ID(get_ID());
					lcr.setFTU_QualityParam_ID(FTU_QualityParam_id);
					lcr.setResult_Human(result.toUpperCase());
					String SysResult = (result.equalsIgnoreCase(qparam.get_ValueAsString("Result")) ? "Aceptar" : "Rechazar");
					lcr.setResult_System(SysResult);
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
		//	Added by Jorge Colmenarez, 2022-12-03 12:03
		//	Validate Ticket Duplicated only for Receipt
		if(getOperationType() != null && (getOperationType().equalsIgnoreCase(OPERATIONTYPE_ImportRawMaterial)
				|| getOperationType().equalsIgnoreCase(OPERATIONTYPE_ReceiptMoreThanOneProduct)
				|| getOperationType().equalsIgnoreCase(OPERATIONTYPE_ProductBulkReceipt) 
				|| getOperationType().equalsIgnoreCase(OPERATIONTYPE_RawMaterialReceipt))) {
			m_processMsg = validateETReferenceDuplicated();
			if (m_processMsg != null)
				return STATUS_Invalid;
		}
		//	End Jorge Colmenarez
		//End By Argenis Rodríguez
		
		//	Clasificar
		clasification();
		setDescription(null);
		String valid = validateAnalysis();
		if (valid != null) {
			if(getDescription() != null 
					&& getDescription().length() > 0) {
				setDescription(getDescription() + valid );
			} else {
				setDescription(valid);
			}
		}
		setIsValidAnalysis(valid == null);
		
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
		
		setDefiniteDocumentNo();
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null) {
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	/**
	 * @return
	 */
	private String validateAnalysis() {
		StringBuilder msg = new StringBuilder();
		
		for (MHRSAnalysisValuation valuation : getValuationLines(true, "LOWER(Result_System) = (SELECT LOWER(Result) FROM FTU_QualityParam WHERE FTU_QualityParam.FTU_QualityParam_ID = HRS_AnalysisValuation.FTU_QualityParam_ID)")) {
			msg.append( valuation.getFTU_QualityParam().getName() )
				.append(" = " )
				.append(valuation.getResult_Human())
				.append(Env.NL); 
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

	/** Logger */
	private static CLogger log = CLogger.getCLogger(MHRSAnalysis.class);
	
	/**
	 * Create PDF file
	 * 
	 * @param file output file
	 * @return file if success
	 */
	public File createPDF(File file) {
		ReportEngine re = getDocumentPrintEngine(getCtx(), getHRS_Analysis_ID(), get_TrxName());
		if (re == null)
			return null;
		MPrintFormat format = re.getPrintFormat();
		// We have a Jasper Print Format
		// ==============================
		if(format.getJasperProcess_ID() > 0)
		{
			ProcessInfo pi = new ProcessInfo ("", format.getJasperProcess_ID());
			pi.setRecord_ID ( getHRS_Analysis_ID() );
			pi.setIsBatch(true);
			
			ServerProcessCtl.process(pi, null);
			
			return pi.getPDFReport();
		}
		// Standard Print Format (Non-Jasper)
		// ==================================
		return re.getPDF(file);
	} // createPDF
	
	/**************************************************************************
	 * 	Get Document Print Engine for Request Type Document Type.
	 *  @author Jorge Colmenarez, 2022-12-05 09:05, jcolmenarez@frontuari.net
	 * 	@param ctx context
	 * 	@param Record_ID id
	 *  @param trxName
	 * 	@return Report Engine or null
	 */
	public static ReportEngine getDocumentPrintEngine (Properties ctx, int Record_ID, String trxName)
	{
		if (Record_ID < 1)
		{
			log.log(Level.WARNING, "No PrintFormat for Record_ID=" + Record_ID);
			return null;
		}

		int AD_PrintFormat_ID = 0;
		int C_BPartner_ID = 0;
		String DocumentNo = null;
		int copies = 1;

		//	Language
		MClient client = MClient.get(ctx);
		Language language = client.getLanguage();	
		//	Get Document Info
		StringBuilder sql = new StringBuilder("SELECT COALESCE(dt.AD_PrintFormat_ID, pf.AD_PrintFormat_ID),")
				.append(" c.IsMultiLingualDocument,NULL AS AD_Language,a.C_BPartner_ID,a.DocumentNo ")
				.append("FROM HRS_Analysis a ")
				.append(" INNER JOIN C_DocType dt ON (a.C_DocType_ID=dt.C_DocType_ID)")
				.append(" INNER JOIN AD_Client c ON (a.AD_Client_ID=c.AD_Client_ID),")
				.append(" AD_PrintFormat pf ")
				.append("WHERE pf.AD_Client_ID IN (0,a.AD_Client_ID)")
				.append(" AND pf.AD_Table_ID="+Table_ID+" AND (pf.IsTableBased='N' OR pf.AD_PrintFormat_ID = dt.AD_PrintFormat_ID)")	//	from HRS_Analysis
				.append(" AND a.HRS_Analysis_ID=? ")				//	Info from HRS_Analysis
				.append("ORDER BY dt.AD_PrintFormat_ID, pf.AD_Client_ID DESC, pf.AD_Org_ID DESC");
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trxName);
			pstmt.setInt(1, Record_ID);
			rs = pstmt.executeQuery();
			if (rs.next())	//	first record only
			{
				AD_PrintFormat_ID = rs.getInt(1);
				copies = 1;
				//	Set Language when enabled
				String AD_Language = rs.getString(3);
				if (AD_Language != null)// && "Y".equals(rs.getString(2)))	//	IsMultiLingualDocument
					language = Language.getLanguage(AD_Language);
				C_BPartner_ID = rs.getInt(4);
				DocumentNo = rs.getString(5);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Record_ID=" + Record_ID + ", SQL=" + sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (AD_PrintFormat_ID == 0)
		{
			log.log(Level.SEVERE, "No PrintFormat found for Record_ID=" + Record_ID);
			return null;
		}

		//	Get Format & Data
		MPrintFormat format = MPrintFormat.get (ctx, AD_PrintFormat_ID, false);
		format.setLanguage(language);		//	BP Language if Multi-Lingual
		format.setTranslationLanguage(language);
		//	query
		MQuery query = new MQuery(format.getAD_Table_ID());
		query.addRestriction("HRS_Analysis", MQuery.EQUAL, Record_ID);
		//
		if (DocumentNo == null || DocumentNo.length() == 0)
			DocumentNo = "DocPrint";
		PrintInfo info = new PrintInfo(
			DocumentNo,
			Table_ID,
			Record_ID,
			C_BPartner_ID);
		info.setCopies(copies);
		info.setDocumentCopy(false);		//	true prints "Copy" on second
		info.setPrinterName(format.getPrinterName());
		
		//	Engine
		ReportEngine re = new ReportEngine(ctx, format, query, info, trxName);
		return re;
	}	//	getDocumentPrintEngine

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
		return getCreatedBy();
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
