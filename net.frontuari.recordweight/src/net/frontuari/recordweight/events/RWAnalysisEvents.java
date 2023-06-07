package net.frontuari.recordweight.events;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.po.AfterChange;
import org.adempiere.base.event.annotations.po.AfterNew;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

import net.frontuari.recordweight.model.MFTUQualityParam;
import net.frontuari.recordweight.model.MHRSAnalysis;
import net.frontuari.recordweight.model.MHRSAnalysisLine;

@EventTopicDelegate
@ModelEventTopic(modelClass = MHRSAnalysis.class)
public class RWAnalysisEvents extends ModelEventDelegate<MHRSAnalysis> {

	CLogger log = CLogger.getCLogger(RWAnalysisEvents.class);
	
	public RWAnalysisEvents(MHRSAnalysis po, Event event) {
		super(po, event);
		log.info("Load Events from Record Weight Plugins for Analysis");
	}
	
	@AfterNew
	public void onAfterNew() {
		MHRSAnalysis a = getModel();
		createAnalysisLine(a.get_ID(),a.getM_Product_ID(),a.getAnalysis_ID(),a.getCtx(),a.get_TrxName());
	}
	
	@AfterChange
	public void onAfterChange() {
		MHRSAnalysis a = getModel();
		if(a.is_ValueChanged(MHRSAnalysis.COLUMNNAME_Analysis_ID) 
				|| a.is_ValueChanged(MHRSAnalysis.COLUMNNAME_M_Product_ID)) {
			if((a.get_ValueOld(MHRSAnalysis.COLUMNNAME_M_Product_ID) != null && a.getM_Product_ID()==(int)a.get_ValueOld(MHRSAnalysis.COLUMNNAME_M_Product_ID)) 
					&& (a.get_ValueOld(MHRSAnalysis.COLUMNNAME_Analysis_ID) != null && a.getAnalysis_ID()==(int)a.get_ValueOld(MHRSAnalysis.COLUMNNAME_Analysis_ID)))
				return;
			//	Delete last analysis lines
			DB.executeUpdate("DELETE FROM HRS_AnalysisLine WHERE HRS_Analysis_ID = ?", a.get_ID(), a.get_TrxName());
			createAnalysisLine(a.get_ID(),a.getM_Product_ID(),a.getAnalysis_ID(),a.getCtx(),a.get_TrxName());
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
		MFTUQualityParam[] qparams = MFTUQualityParam.getLines(ProductID, " AND FTU_AnalysisType_ID IS NOT NULL");
		if(qparams == null || qparams.length <= 0)
			return;
		//	Create Lines from Quality Params
		for(MFTUQualityParam qparam : qparams) {
			MHRSAnalysisLine line = new MHRSAnalysisLine(ctx, 0, trxName);
			line.setHRS_Analysis_ID(AnalysisID);
			line.setFTU_AnalysisType_ID(qparam.getFTU_AnalysisType_ID());
			line.setResult(getCondition(qparam.getFTU_AnalysisType_ID(),ProductID,LotID,trxName));
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
				+ "WHERE a.IsManufactured = 'Y' AND b.FTU_AnalysisType_ID = ? "
				+ "AND a.M_Product_ID = ? AND a.Analysis_ID = ? AND a.DocStatus IN ('CO','CL') ";
		
		value = DB.getSQLValueBD(trxName, sql, new Object[] {AnalysisTypeID,ProductID,LotID});
		if(value == null)
			value = BigDecimal.ZERO;
		
		return value;
	}

}
