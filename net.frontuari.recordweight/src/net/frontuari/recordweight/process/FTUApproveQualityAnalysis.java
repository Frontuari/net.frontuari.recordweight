package net.frontuari.recordweight.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.CustomProcess;
import net.frontuari.recordweight.model.MHRSAnalysis;

/**
 * 
 * @author Argenis Rodríguez
 *
 */
public class FTUApproveQualityAnalysis extends CustomProcess {

	@Override
	protected void prepare() {
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
				analysis.set_ValueOfColumn(MHRSAnalysis.COLUMNNAME_IsApprovedAnalysis
						, !analysis.get_ValueAsBoolean(MHRSAnalysis.COLUMNNAME_IsApprovedAnalysis));
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
