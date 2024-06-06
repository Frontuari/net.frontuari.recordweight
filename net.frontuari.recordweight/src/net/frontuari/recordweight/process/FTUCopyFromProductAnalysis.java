package net.frontuari.recordweight.process;

import java.util.List;

import org.compiere.model.MProcessPara;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.X_FTU_ProductAnalysis;
import net.frontuari.recordweight.model.X_FTU_QualityParam;

/**
 *  Copy Formula Lines
 *
 *	@author Jose Vasquez
 *	@version $Id: CopyFormulas.java,v 1.0 2024/06/05$
 */
@org.adempiere.base.annotation.Process
public class FTUCopyFromProductAnalysis extends FTUProcess {
	
	/**	The Product Analysis				*/
	private int		p_FTU_ProductAnalysis_ID = 0;
	private int		p_FTU_ProductAnalysis_ID_To = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("FTU_ProductAnalysis_ID"))
				p_FTU_ProductAnalysis_ID = para[i].getParameterAsInt();
			else if (name.equals("FTU_ProductAnalysis_ID_To"))
				p_FTU_ProductAnalysis_ID_To = para[i].getParameterAsInt();
			else
				MProcessPara.validateUnknownParameter(getProcessInfo().getAD_Process_ID(), para[i]);
		}
	} //	prepare

	@Override
	protected String doIt() throws Exception {
		String WhereClause = " FTU_ProductAnalysis_ID = " + p_FTU_ProductAnalysis_ID;
		X_FTU_QualityParam[] FormulaLines = getFormulaLines(WhereClause);
		X_FTU_ProductAnalysis FTU_ProductAnalysis = new X_FTU_ProductAnalysis (getCtx(), getRecord_ID(), get_TrxName());
		if (FormulaLines != null && FormulaLines.length > 0) {
			for (X_FTU_QualityParam line : FormulaLines) {
				X_FTU_QualityParam copy = new X_FTU_QualityParam(getCtx(),0, get_TrxName());
				PO.copyValues(line, copy);
				copy.setCode(line.getCode());
				copy.setFTU_AnalysisType_ID(line.getFTU_AnalysisType_ID());
				copy.setHumanCode(line.getHumanCode());
				copy.setTranslateCode(line.getTranslateCode());
				copy.setM_Product_ID(FTU_ProductAnalysis.getM_Product_ID());
				copy.setFTU_ProductAnalysis_ID(p_FTU_ProductAnalysis_ID_To);
				copy.saveEx();
			}
		}
		
		return "Líneas de fórmulas copiadas: " + FormulaLines.length;
	}
	
	private X_FTU_QualityParam[] getFormulaLines (String whereClause) {
		List<X_FTU_QualityParam> list = new Query(getCtx(), X_FTU_QualityParam.Table_Name, whereClause, get_TrxName()).list();
		return list.toArray(new X_FTU_QualityParam[list.size()] );
	}
}