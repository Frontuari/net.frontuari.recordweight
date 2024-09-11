package net.frontuari.recordweight.process;

import java.lang.System;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAttribute;
import org.compiere.model.MAttributeInstance;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MAttributeValue;
import org.compiere.model.MDocType;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MLocator;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Msg;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MHRSAnalysis;

@org.adempiere.base.annotation.Process
public class GenerateMovementFromAnalysis extends FTUProcess {

	/**	Parameters for generate Movement Document */
	private int p_C_DocTypeMovement_ID = 0;
	private int p_M_Locator_To_ID = 0;
	private BigDecimal p_MovementQty = BigDecimal.ZERO;
	private String p_DocAction = "CO";
	private int p_M_AttributeValue_ID = 0;
	
	@Override
	protected void prepare() {
		for (ProcessInfoParameter para:getParameter()){
			
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if(name.equals("C_DocTypeMovement_ID"))
				p_C_DocTypeMovement_ID = para.getParameterAsInt();
			else if(name.equals("M_LocatorTo_ID"))
				p_M_Locator_To_ID = para.getParameterAsInt();
			else if(name.equals("MovementQty"))
				p_MovementQty = para.getParameterAsBigDecimal();
			else if (name.equals("M_AttributeValue_ID"))
				p_M_AttributeValue_ID = para.getParameterAsInt();
			else if(name.equals("DocAction"))
				p_DocAction = para.getParameterAsString();
		}
	}

	@Override
	protected String doIt() throws Exception {		
		MHRSAnalysis a = new MHRSAnalysis(getCtx(), getRecord_ID(), get_TrxName());
		if(!a.getDocStatus().equals(MHRSAnalysis.DOCSTATUS_Completed))
			throw new AdempiereException("Error: "+Msg.parseTranslation(getCtx(), "@InvoiceCreateDocNotCompleted@"));
		
		if(!a.isValidAnalysis())
			throw new AdempiereException("Error: "+Msg.parseTranslation(getCtx(), "@HRS_Analysis_ID@")+" "+Msg.parseTranslation(getCtx(), "@NotValid@"));
		
		MDocType dt = new MDocType(getCtx(), p_C_DocTypeMovement_ID, get_TrxName());
		if(dt.get_ValueAsBoolean("IsChangeLotStatus") && a.getAnalysis_ID()<1) {
			throw new AdempiereException("Es requerido definir un lote");
		}
		
		if(dt.get_ValueAsBoolean("IsChangeLotStatus") && p_M_AttributeValue_ID==0) {
			throw new AdempiereException("Debe seleccionar un valor para el estatus de calidad");
		}
		
		MProduction pr = new MProduction(getCtx(), a.get_ValueAsInt("M_Production_ID"), get_TrxName());
		
		if(p_MovementQty.compareTo(pr.getProductionQty())>0)
			throw new AdempiereException("Error: "+Msg.parseTranslation(getCtx(), "@Parameter@")+" "+Msg.parseTranslation(getCtx(), "@MovementQty@")+" ["+p_MovementQty+"] > "+Msg.parseTranslation(getCtx(), "@MovementQty@")+" "+Msg.parseTranslation(getCtx(), "@of@")+" "+Msg.parseTranslation(getCtx(), "@M_Production_ID@")+" ["+pr.getProductionQty()+"]");
		
		//	Create Movement Header
		MMovement m = new MMovement(getCtx(), 0, get_TrxName());
		m.setAD_Org_ID(pr.getAD_Org_ID());
		m.setC_DocType_ID(p_C_DocTypeMovement_ID);
		m.setM_Warehouse_ID(pr.getM_Locator().getM_Warehouse_ID());
		MLocator locator_to = new MLocator(getCtx(), p_M_Locator_To_ID, get_TrxName());
		m.setM_WarehouseTo_ID(locator_to.getM_Warehouse_ID());
		m.saveEx(get_TrxName());
		//	Create Movement Line
		MMovementLine ml = new MMovementLine(m);
		ml.setM_Product_ID(a.getM_Product_ID());
		if(dt.get_ValueAsBoolean("IsChangeLotStatus")) {
			MProduct p = new MProduct(getCtx(), a.getM_Product_ID(), get_TrxName());
			MAttributeSetInstance i = new MAttributeSetInstance(getCtx(), a.getAnalysis_ID(), get_TrxName());
			MAttributeSetInstance newi = MAttributeSetInstance.create(getCtx(), p, get_TrxName());
			
			newi.setLot(i.getLot());
			newi.setSerNo(i.getSerNo());
			newi.saveEx();
			
			String[] attrVal = i.getDescription().split("_");
			MAttributeValue val = new MAttributeValue(getCtx(), p_M_AttributeValue_ID, get_TrxName());
			MAttributeSet as = new MAttributeSet(getCtx(), p.getM_AttributeSet_ID(), get_TrxName());
			MAttribute[] attributes = as.getMAttributes(true);
			//	xd
			StringBuilder description = new StringBuilder();
			String separador = "_";
			
			if(attributes.length > 0)
			{
				for(int j = 0; j < attributes.length; j++)
				{
					if (j > 0)
						description.append(separador);
					MAttributeInstance ai = new MAttributeInstance(getCtx(), 0, get_TrxName());
				
					ai.setM_AttributeSetInstance_ID(newi.getM_AttributeSetInstance_ID());
					ai.setM_Attribute_ID(attributes[j].getM_Attribute_ID());
					if (attributes[j].getAttributeValueType().equals(MAttribute.ATTRIBUTEVALUETYPE_List) &&
							attributes[j].getName().equals("Estatus de Calidad")) {					
						attrVal[j] = val.getDescription();
						ai.setValue(attrVal[j]);
						ai.setM_AttributeValue_ID(p_M_AttributeValue_ID);
					}
					description.append(attrVal[j]);
					ai.saveEx();
				}
			}
			newi.setDescription(description.toString());
			newi.saveEx();
			
			MStorageOnHand[] sto = MStorageOnHand.getOfProduct(getCtx(), p.getM_Product_ID(), get_TrxName());
			
			MStorageOnHand actualSto = null;
			for (MStorageOnHand check : sto) {
				if (check.getM_AttributeSetInstance_ID() == i.getM_AttributeSetInstance_ID())
					actualSto = check;
			}
			if(actualSto==null)
				return "No encontro registros de almacenamiento para el producto "+p.getName();
			if (actualSto.getQtyOnHand().compareTo(p_MovementQty) < 0) {
				return "La cantidad ingresada es mayor a la disponible";
			}
		}
		
		ml.setM_Locator_ID(pr.getM_Locator_ID());
		ml.setM_LocatorTo_ID(p_M_Locator_To_ID);
		ml.setMovementQty(p_MovementQty);
		ml.saveEx(get_TrxName());
		
		if(!m.processIt(p_DocAction))
		{
			throw new AdempiereException(m.getProcessMsg());
		}
		m.saveEx(get_TrxName());
		
		addBufferLog(m.get_ID(), new Timestamp(System.currentTimeMillis()), null, Msg.parseTranslation(getCtx(), "@M_Movement_ID@")+": "+m.getDocumentNo(), m.get_Table_ID(), m.get_ID());
		return "@OK@";
	}

}
