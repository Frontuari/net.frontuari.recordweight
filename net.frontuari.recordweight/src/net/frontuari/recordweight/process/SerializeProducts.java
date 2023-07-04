/**
 * 
 */
package net.frontuari.recordweight.process;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_M_AttributeSetInstance;
import org.compiere.model.I_M_InOutLine;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MInOut;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;

import net.frontuari.recordweight.base.FTUProcess;

/**
 * @author dmartinez
 *
 */
@Process
public class SerializeProducts extends FTUProcess {

	private int p_M_Product_ID = -1;
	private int p_M_AttributeSetInstance_ID = -1;
	private int p_M_InOut_ID = -1;
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			String name = para.getParameterName();
			//	
			if (para.getParameter() == null)
				;
			else if(name.equals(I_M_InOutLine.COLUMNNAME_M_Product_ID))
				p_M_Product_ID	= para.getParameterAsInt();
			else if(name.equals(I_M_InOutLine.COLUMNNAME_M_AttributeSetInstance_ID))
				p_M_AttributeSetInstance_ID= para.getParameterAsInt();
			else if(name.equals(I_M_InOutLine.COLUMNNAME_M_InOut_ID))
				p_M_InOut_ID= para.getParameterAsInt();
		}
	}

	@Override
	protected String doIt() throws Exception {
		if(p_M_Product_ID <= 0)
			throw new AdempiereException("@M_Product_ID@ @NotFound@");
		if(p_M_AttributeSetInstance_ID <= 0)
			throw new AdempiereException("@M_AttributeSetInstance_ID@ @NotFound@");
		if(p_M_InOut_ID <= 0)
			throw new AdempiereException("@M_InOutLine_ID@ @NotFound@");
		MAttributeSetInstance referenceInstance = MAttributeSetInstance.get(getCtx(), p_M_AttributeSetInstance_ID, p_M_Product_ID);
		
		MInOut inOut = new MInOut(getCtx(), p_M_InOut_ID, get_TrxName());
		
		StringBuffer sqlInsert = new StringBuffer("INSERT INTO M_InOutLineMA (" + 
				"	M_InOutLine_ID, M_AttributeSetInstance_ID, AD_Client_ID, AD_Org_ID, IsActive," + 
				"	Created, CreatedBy, Updated, UpdatedBy, MovementQty, M_InOutLineMA_UU,DateMaterialPolicy ) VALUES");
		
		StringBuffer insertAttribute = new StringBuffer("INSERT INTO M_AttributeSetInstance (" + 
				"	M_AttributeSetInstance_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, " + 
				"	CreatedBy, Updated, UpdatedBy, M_AttributeSet_ID, SerNo, Lot, GuaranteeDate, " + 
				"	Description, M_Lot_ID, M_AttributeSetInstance_UU ) VALUES ");
		
		int p_M_InOutLine_ID = DB.getSQLValue(get_TrxName(), "SELECT M_InOutLine_ID "
				+ "FROM M_InOutLine "
				+ "WHERE "
				+ "	M_Product_ID =? "
				+ "	AND M_InOut_ID = ?"
				+ " AND NOT EXISTS ("
				+ "		SELECT 1 "
				+ "		FROM M_InOutLineMA "
				+ "		WHERE M_InOutLineMA.M_InOutLine_ID = M_InOutLine.M_InOutLine_ID)", p_M_Product_ID, p_M_InOut_ID);
		if(p_M_InOutLine_ID <= 0)
			return "";
		int qty = DB.getSQLValue(get_TrxName(), "SELECT QtyEntered FROM M_InOutLine WHERE M_InOutLine_ID = ?", p_M_InOutLine_ID);
		
		StringBuffer sqlValues = null;
		StringBuffer sqlAtributeValues = null;
		int p_AD_Client_ID = getAD_Client_ID();
		int p_User_ID = getAD_User_ID();
		int p_AD_Org_ID = 0;
		int serial = Integer.valueOf(referenceInstance.getSerNo() != null ? referenceInstance.getSerNo() : "0");
		for (int i = 1; i <= qty; i++) {
			int seq =DB.getNextID(Env.getCtx(), I_M_AttributeSetInstance.Table_Name, get_TrxName()); 
			sqlAtributeValues = new StringBuffer("(")
				.append(seq)
				.append(",")
				.append(p_AD_Client_ID)
				.append(",")
				.append(p_AD_Org_ID)
				.append(",'Y', NOW(),")
				.append(p_User_ID)
				.append(",NOW(),")
				.append(p_User_ID)
				.append(",")
				.append(referenceInstance.getM_AttributeSet_ID())
				.append(",")
				.append(serial)
				.append(",'")
				.append(referenceInstance.getLot() != null ? referenceInstance.getLot(): "")
				.append("',NULL,'")
				.append(getDescrition(referenceInstance.getDescription(), serial) + "',")
				.append(referenceInstance.getM_Lot_ID() > 0 ? referenceInstance.getM_Lot_ID() : "NULL")
				.append(",NULL")
				.append(")")
				.append(qty > 1 && qty != i ? ",": "")
			; 
			sqlValues = new StringBuffer("(")
					.append(p_M_InOutLine_ID)
					.append(",")
					.append(seq)
					.append(",")
					.append(p_AD_Client_ID)
					.append(",")
					.append(p_AD_Org_ID)
					.append(",'Y', NOW(),")
					.append(p_User_ID)
					.append(",NOW(),")
					.append(p_User_ID)
					.append(",")
					.append(1)
					.append(",NULL,'")
					.append(inOut.getMovementDate())
					.append("')")
					.append(qty > 1 && qty != i ? ",": "")
				; 
			serial++;
			insertAttribute.append(sqlAtributeValues);		
			sqlInsert.append(sqlValues);
		}
		DB.executeUpdate(insertAttribute.toString(), get_TrxName());
		int val = DB.executeUpdate(sqlInsert.toString(), get_TrxName());
		referenceInstance.delete(true, get_TrxName());
		return "@M_AttributeSetInstance_ID@ @Created@ " + val;
	}
	
	private String getDescrition (String description, int seqNo) {
		if (description == null || description.length() == 0)
			return "";
		String inStr = description;
		StringBuilder outStr = new StringBuilder();

		int i = inStr.indexOf('#');
		while (i != -1)
		{
			outStr.append(inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf('_');						// next @
			if (j < 0)
			{
				outStr.append("#" + seqNo);
				return outStr.toString();
			}

			outStr.append("#" + seqNo);

			inStr = inStr.substring(j+1, inStr.length());	// from second @
			i = inStr.indexOf('_');
		}
		outStr.append( "_" + inStr);						// add the rest of the string

		return outStr.toString();
	}

}
