package net.frontuari.recordweight.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.adempiere.base.annotation.Callout;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUCallout;
import net.frontuari.recordweight.model.MFTUAnalysisType;
import net.frontuari.recordweight.model.X_FTU_FormuleFunction;
import net.frontuari.recordweight.model.X_FTU_QualityParam;

@Callout(tableName = "FTU_QualityParam", columnName = {"TranslateCode",
		"AD_Column_ID","FTU_Parent_ID","FTU_FormuleFunction_ID","FTU_AnalysisType_ID"})
public class CalloutQualityParam extends FTUCallout {
	String campoCodigo;
	@Override
	protected String start() {
		String colummName = getColumnName();
		String codigo;
		int id;
		switch(colummName) {
			case X_FTU_QualityParam.COLUMNNAME_TranslateCode:
				if(getValue()==null) return null;
				campoCodigo=(String) getTab().getValue("Code");
				actualizarCodigoHumano();
				actualizarCodigosCampos();
			break;
			case X_FTU_QualityParam.COLUMNNAME_AD_Column_ID:
				if(getValue()==null) return null;
				int idColumn=(int) getValue();
				codigo=(String) getTab().getValue("Code");
				if(codigo==null || codigo=="") {
					setValue("Code", "(&"+idColumn+"<100%)");
				}else {
					setValue("Code", codigo+" AND (&"+idColumn+"<100%)");
				}					
			break;
			case X_FTU_QualityParam.COLUMNNAME_FTU_Parent_ID:
				if(getValue()==null) return null;
				int FTU_Parent_ID=(int) getValue();
				codigo=(String) getTab().getValue("Code");
				if(codigo==null || codigo=="") {
					setValue("Code", "(F"+FTU_Parent_ID+"<100%)");
				}else {
					setValue("Code", codigo+" AND (F"+FTU_Parent_ID+"<100%)");
				}					
			break;
			case X_FTU_QualityParam.COLUMNNAME_FTU_FormuleFunction_ID:
				if(getValue()==null) return null;
				id=(int) getValue();
				X_FTU_FormuleFunction ff = new Query(getCtx(), X_FTU_FormuleFunction.Table_Name, "FTU_FormuleFunction_ID=?", null)
						.setParameters(id)
						.setOnlyActiveRecords(true)
						.first();
				
				String value=ff.getValue();
				codigo=(String) getTab().getValue("Code");
				if(codigo==null || codigo=="") {
					setValue("Code", "(@"+value+"(VAR)<100%)");
				}else {
					setValue("Code", codigo+" AND (@"+value+"(VAR)<100%)");
				}			
				break;
			case X_FTU_QualityParam.COLUMNNAME_FTU_AnalysisType_ID:
				if(getValue()==null) return null;
				id=(int) getValue();
				MFTUAnalysisType at = new Query(getCtx(), MFTUAnalysisType.Table_Name, "FTU_AnalysisType_ID=?", null)
						.setParameters(id)
						.setOnlyActiveRecords(true)
						.first();
				
				String valueAT=at.getValue();
				codigo=(String) getTab().getValue("Code");
				if(codigo==null || codigo=="") {
					setValue("Code", "(#"+valueAT+"<100%)");
				}else {
					setValue("Code", codigo+" AND (#"+valueAT+"<100%)");
				}	
				
				break;
		}
		
		return null;
	}
	
	/***
	 * Update Fields Codes
	 */
	private void actualizarCodigosCampos() {
		String idCampo;
		String columnName;
		String tableName;
		
		Pattern p = Pattern.compile("&\\d+");
		Matcher m = p.matcher(campoCodigo);
		
		while (m.find()) {
			idCampo=m.group().replaceAll("&", "");

			String sql="SELECT att.Name as table_name, act.Name as column_name "
					+ "FROM AD_Column ac "
					+ "INNER JOIN AD_Column_Trl act ON act.AD_Column_ID=ac.AD_Column_ID AND act.AD_Language = ?"
					+ "INNER JOIN AD_Table_Trl att ON att.AD_Table_ID=ac.AD_Table_ID AND att.AD_Language = ? "
					+ "WHERE ac.AD_Column_ID=?";
			PreparedStatement ps = null;
			ResultSet rs= null;
			try {
				ps = DB.prepareStatement(sql, null);
				ps.setString(1, Env.getAD_Language(getCtx()));
				ps.setString(2, Env.getAD_Language(getCtx()));
				ps.setString(3, idCampo);
				rs = ps.executeQuery();
				while(rs.next()) {
					columnName=rs.getString("column_name");
					tableName=rs.getString("table_name");
					campoCodigo=campoCodigo.replaceAll("&"+idCampo, "("+tableName+"->"+columnName+")");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs, ps);
				rs = null; ps = null;
			}
		}
		getTab().setValue("HumanCode", campoCodigo.toString());
	}

	/***
	 * Update Human Code
	 */
	private void actualizarCodigoHumano() {
		String sql="SELECT at.value, at.name FROM FTU_AnalysisType at";
		String sqlFunciones="SELECT value, name FROM FTU_FormuleFunction";
		String sqlFormulas="SELECT value, name, FTU_QualityParam_ID FROM FTU_QualityParam";
		
		campoCodigo=campoCodigo.toLowerCase();
		campoCodigo=campoCodigo.replaceAll(" or ", " O ");
		campoCodigo=campoCodigo.replaceAll(" and ", " Y ");
		campoCodigo=campoCodigo.replaceAll("if\\(", "SI(");
		campoCodigo=campoCodigo.replaceAll("case when \\(", "SI(");
		campoCodigo=campoCodigo.replaceAll(" then ", " mostrar ");
		campoCodigo=campoCodigo.replaceAll(" end", " ");
		campoCodigo=campoCodigo.replaceAll(" else ", " SiNo ");
		//	Search in Analysis Type
		campoCodigo = replaceCode(sql, campoCodigo, 1);
		//	Search in Functions Formule
		campoCodigo = replaceCode(sqlFunciones, campoCodigo, 2);
		//	Search in Quality Param
		campoCodigo = replaceCode(sqlFormulas, campoCodigo, 3);
		
		getTab().setValue("HumanCode", campoCodigo.toString());
	}
	
	/***
	 * Search and Replace Code from SQL Values
	 * @param sql
	 * @param code
	 * @return
	 */
	private String replaceCode(String sql, String code, int type) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value="";
		String name="";
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while(rs.next()) {
				value=rs.getString("value");
				name=rs.getString("name");
				if(type==1)
					code=code.replaceAll("#("+value+")", "("+name+")");
				else if(type==2)
					code=code.replaceAll("@("+value+")", "("+name+")");
				else if(type==3)
					code=code.replaceAll("f("+value+")", "("+name+")");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DB.close(rs, ps);
			rs = null; ps = null;
		}
		
		return code;
	}

}
