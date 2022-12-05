package net.frontuari.recordweight.events;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUModelEvents;
import net.frontuari.recordweight.model.MFTUQualityParam;
import net.frontuari.recordweight.model.MHRSAnalysis;
import net.frontuari.recordweight.model.MHRSAnalysisLine;

/**
 * Analysis Events
 * @author Jorge Colmenarez, 2022-12-03
 * @version AnalysisEvent.java $Id 1.0.0, Exp jlctmaster, Frontuari, C.A.
 */
public class AnalysisEvent extends FTUModelEvents {

	@Override
	protected void doHandleEvent() {
		PO po = getPO();		
		String type = getEventType();
		if(po instanceof MHRSAnalysis) {
			MHRSAnalysis a = (MHRSAnalysis) po;
			if(type.equalsIgnoreCase(IEventTopics.PO_AFTER_NEW)) {
				createAnalysisLine(a.get_ID(),a.getM_Product_ID(),a.getAnalysis_ID(),a.getCtx(),a.get_TrxName());
			}
			if(type.equalsIgnoreCase(IEventTopics.PO_AFTER_CHANGE)) {
				if(a.is_ValueChanged(MHRSAnalysis.COLUMNNAME_Analysis_ID) 
						|| a.is_ValueChanged(MHRSAnalysis.COLUMNNAME_M_Product_ID)) {
					if(a.getM_Product_ID()==(int)a.get_ValueOld(MHRSAnalysis.COLUMNNAME_M_Product_ID) 
							&& a.getAnalysis_ID()==(int)a.get_ValueOld(MHRSAnalysis.COLUMNNAME_Analysis_ID))
						return;
					//	Delete last analysis lines
					DB.executeUpdate("DELETE FROM HRS_AnalysisLine WHERE HRS_Analysis_ID = ?", a.get_ID(), a.get_TrxName());
					createAnalysisLine(a.get_ID(),a.getM_Product_ID(),a.getAnalysis_ID(),a.getCtx(),a.get_TrxName());
				}
			}
		}
	}
	
	/***
	 * Create Analysis Lines from Product and Lot
	 * @param AnalysisID
	 * @param ProductID
	 * @param LoteID
	 * @param trxName
	 */
	private void createAnalysisLine(int AnalysisID, int ProductID, int LotID, Properties ctx, String trxName) {
		MFTUQualityParam[] qparams = MFTUQualityParam.getLines(ProductID, " AND FTU_Analysis_Type_ID IS NOT NULL");
		if(qparams == null || qparams.length <= 0)
			return;
		//	Create Lines from Quality Params
		for(MFTUQualityParam qparam : qparams) {
			MHRSAnalysisLine line = new MHRSAnalysisLine(ctx, 0, trxName);
			line.setHRS_Analysis_ID(AnalysisID);
			line.setFTU_Analysis_Type_ID(qparam.getFTU_Analysis_Type_ID());
			line.setResult(getCondition(qparam.getFTU_Analysis_Type_ID(),ProductID,LotID,trxName));
			line.saveEx(trxName);
		}
	}
	
	/****
	 * get Average from Analysis Productions
	 * @param AnalysisTypeID
	 * @param ProductID
	 * @param LotID
	 * @param trxName
	 * @return
	 */
	private BigDecimal getCondition(int AnalysisTypeID,int ProductID, int LotID, String trxName) {
		BigDecimal value = BigDecimal.ZERO;
		
		String sql = "SELECT AVG(b.Result) FROM HRS_Analysis a "
				+ "JOIN HRS_AnalysisLine b ON (a.HRS_Analysis_ID = b.HRS_Analysis_ID) "
				+ "WHERE a.IsManufactured = 'Y' AND b.FTU_Analysis_Type_ID = ? "
				+ "AND a.M_Product_ID = ? AND a.Analysis_ID = ? AND a.DocStatus IN ('CO','CL') ";
		
		value = DB.getSQLValueBD(trxName, sql, new Object[] {AnalysisTypeID,ProductID,LotID});
		if(value == null)
			value = BigDecimal.ZERO;
		
		return value;
	}

}
