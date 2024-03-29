package net.frontuari.recordweight.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MProcess;
import org.compiere.model.MQuery;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_AD_ReportView;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUEntryTicket;

/**
 *
 */
public class ChangePrintedStatus extends FTUProcess {

	/**	Record Identifier	*/
	private int p_Record_ID = 0;
	@Override
	protected void prepare() {
		p_Record_ID = getRecord_ID();
		if(p_Record_ID <= 0)
			throw new AdempiereException("@FTU_EntryTicket_ID@ @NotFound@");
	}

	@Override
	protected String doIt() throws Exception {
		int m_AD_Process_ID = getProcessInfo().getAD_Process_ID();
		//	Get Table
		MProcess pr = MProcess.get(getCtx(), m_AD_Process_ID);
		if(pr != null){
			X_AD_ReportView rv = new X_AD_ReportView(getCtx(), pr.getAD_ReportView_ID(), get_TrxName());
			MTable reportTable = MTable.get(getCtx(), rv.getAD_Table_ID());
			MPrintFormat f = MPrintFormat.get(getCtx(), rv.getAD_ReportView_ID(), reportTable.getAD_Table_ID());
			//	for all Mobilization Guide
			if(f != null) {
				MTable modelTable = MTable.get(getCtx(), getTable_ID());
				MQuery q = new MQuery(reportTable.getTableName());
				q.addRestriction(modelTable.getTableName() + "_ID", "=", p_Record_ID);
				PrintInfo i = new PrintInfo(Msg.translate(getCtx(), reportTable.getTableName() + "_ID"), reportTable.getAD_Table_ID(), p_Record_ID);
				//i.setAD_Table_ID(reportTable.getAD_Table_ID());
				ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i, get_TrxName());
				//	Print
				//	Direct Print
				re.print();
				PO model = modelTable.getPO(p_Record_ID, get_TrxName());
				//	If it returns null check if the table exists
				if(model == null)
					return "";
				
				model.set_ValueOfColumn("IsPrinted", true);
				model.set_ValueOfColumn("DatePrinted", new Timestamp(System.currentTimeMillis()));
				model.saveEx();	
			} else {
				log.warning(Msg.parseTranslation(getCtx(), "@NoDocPrintFormat@ @AD_Table_ID@=" + reportTable.getTableName()));

				MFTUEntryTicket tiket = new MFTUEntryTicket(getCtx(),p_Record_ID,get_TrxName());
				tiket.setIsPrinted(true);
				tiket.set_ValueOfColumn("DatePrinted", new Timestamp(System.currentTimeMillis()));
				tiket.saveEx(get_TrxName());
			}

		}
		return "";
	}

}
