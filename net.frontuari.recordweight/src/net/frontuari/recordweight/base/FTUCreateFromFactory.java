/**
 * 
 */
package net.frontuari.recordweight.base;

import org.compiere.grid.ICreateFrom;
import org.compiere.grid.ICreateFromFactory;
import org.compiere.model.GridTab;
import org.compiere.model.MWindow;
import org.compiere.util.Env;

import net.frontuari.recordweight.form.WFTUGetWeightUI;
import net.frontuari.recordweight.model.I_FTU_RecordWeight;

/**
 * @author dmartinez
 *
 */
public class FTUCreateFromFactory implements ICreateFromFactory {

	@Override
	public ICreateFrom create(GridTab mTab) {
		String tableName = mTab.getTableName();
		MWindow window = MWindow.get(Env.getCtx(), mTab.getGridWindow().getAD_Window_ID());
		if (window.getEntityType().equals("FTU01")) {
			if(tableName.equals(I_FTU_RecordWeight.Table_Name))
				return new WFTUGetWeightUI(mTab);
		}
		return null;
	}

}
