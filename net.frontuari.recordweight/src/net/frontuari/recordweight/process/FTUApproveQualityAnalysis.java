package net.frontuari.recordweight.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MHRSAnalysis;

/**
 * 
 * @author Argenis Rodríguez
 *
 */
@org.adempiere.base.annotation.Process
public class FTUApproveQualityAnalysis extends FTUProcess {

	private String p_Help = "";
	
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("Help"))
				p_Help = para.getParameterAsString();
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int updated = 0;
		
		try {
			pstmt = DB.prepareStatement("SELECT T_Selection_ID FROM T_Selection WHERE AD_PInstance_ID = ?", get_TrxName());
			pstmt.setInt(1, getAD_PInstance_ID());
			
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				MHRSAnalysis analysis = new MHRSAnalysis(getCtx(), rs.getInt(1), get_TrxName());
				analysis.setIsApprovedAnalysis(!analysis.isApprovedAnalysis());
				analysis.setHelp(p_Help);
				analysis.saveEx();
				updated++;
			}
		} catch (SQLException e) {
			throw new AdempiereException(e.getLocalizedMessage(), e);
		} finally {
			DB.close(rs, pstmt);
		}
		
		return "@Updated@ " + updated;
	}

}
