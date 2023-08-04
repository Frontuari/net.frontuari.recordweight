package net.frontuari.recordweight.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import org.adempiere.base.annotation.Process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MInventoryLineMA;
import org.compiere.model.MProduct;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;

import net.frontuari.recordweight.base.FTUProcess;

@Process
public class QAAssignmentCancellationSeals extends FTUProcess {
	
	private int p_HRS_Analysis_ID = -1;
	private int p_M_Product_ID = -1;
	private int p_QtyEntered = 0;
	private int p_M_AttributeSetInstance_ID = -1;
	private int p_C_Charge_ID = -1;
	private boolean p_IsVoided = false;
	private int p_InventoryDocType_ID = -1;
	private int p_M_Warehouse_ID = -1;
	private String p_DocStatus = null;

	@Override
	protected void prepare() {
		for (ProcessInfoParameter para : getParameter()) {
			String name = para.getParameterName();
			//
			if (para.getParameter() == null)
				;
			else if (name.equals("HRS_Analysis_ID"))
				p_HRS_Analysis_ID = para.getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para.getParameterAsInt();
			else if (name.equals("QtyEntered"))
				p_QtyEntered = para.getParameterAsInt();
			else if (name.equals("C_Charge_ID"))
				p_C_Charge_ID = para.getParameterAsInt();
			else if (name.equals("IsVoided"))
				p_IsVoided = para.getParameterAsBoolean();
			else if (name.equals("M_AttributeSetInstance_ID"))
				p_M_AttributeSetInstance_ID = para.getParameterAsInt();
			else if (name.equals("InventoryDocType_ID"))
				p_InventoryDocType_ID = para.getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para.getParameterAsInt();
			else if (name.equals("DocStatus"))
				p_DocStatus = (String) para.getParameter();
		}
	}

	@Override
	protected String doIt() throws Exception {

		if (p_HRS_Analysis_ID <= 0)
			throw new AdempiereException("@HRS_Analysis_ID@ @NotFound@");
		if (p_M_Product_ID <= 0)
			throw new AdempiereException("@M_Product_ID@ @NotFound@");
		if (!p_IsVoided && p_QtyEntered <= 0)
			throw new AdempiereException("@QtyEntered@ < @Zero@");
		if (!p_IsVoided && p_C_Charge_ID <= 0)
			throw new AdempiereException("@C_Charge_ID@ @NotFound@");
		if (p_IsVoided && p_M_AttributeSetInstance_ID <= 0)
			throw new AdempiereException("@M_AttributeSetInstance_ID@ @NotFound@");
		if (!p_IsVoided && p_InventoryDocType_ID <= 0)
			throw new AdempiereException("@InventoryDocType_ID@ @NotFound@");
		if (!p_IsVoided && p_M_Warehouse_ID <= 0)
			throw new AdempiereException("@M_Warehouse_ID@ @NotFound@");
		StringBuilder msg = new StringBuilder();
		if (p_IsVoided) {
			String sql = "SELECT il.M_InventoryLine_ID " + "FROM M_Inventory i "
					+ "INNER JOIN M_InventoryLine il ON (i.M_Inventory_ID = il.M_Inventory_ID) "
					+ "INNER JOIN M_InventoryLineMA ilma ON (il.M_InventoryLine_ID = ilma.M_InventoryLine_ID) "
					+ "WHERE i.HRS_Analysis_ID = ? AND il.M_Product_ID = ? AND ilma.M_AttributeSetInstance_ID = ?";
			int p_M_InventoryLine_ID = DB.getSQLValue(get_TrxName(), sql, p_HRS_Analysis_ID, p_M_Product_ID,
					p_M_AttributeSetInstance_ID);

			MInventoryLineMA[] lineMaArray = MInventoryLineMA.get(getCtx(), p_M_InventoryLine_ID, get_TrxName());
			for (MInventoryLineMA lineMa : lineMaArray) {
				if (lineMa.getM_AttributeSetInstance_ID() != p_M_AttributeSetInstance_ID)
					continue;
				lineMa.set_ValueOfColumn("IsVoided", true);
				lineMa.saveEx();
				msg.append("Atributo @Updated@");
				break;
			}

			// p_QtyEntered = 1;
		} else {
			MWarehouse wh = MWarehouse.get(getCtx(), p_M_Warehouse_ID);

			MStorageOnHand[] storage = getWarehouse(getCtx(), p_M_Warehouse_ID, p_M_Product_ID, 0, null, true, true, -1,
					get_TrxName(), false, 10);
			if (storage.length <= 0)
				throw new AdempiereException("@M_Product_ID@ @NotFound@ - @M_Warehouse_ID@");
			MInventory inventory = new MInventory(wh, get_TrxName());
			inventory.setC_DocType_ID(p_InventoryDocType_ID);
			inventory.setMovementDate(Env.getContextAsDate(getCtx(), "#Date"));
			inventory.set_ValueOfColumn("HRS_Analysis_ID", p_HRS_Analysis_ID);
			inventory.saveEx();

			if (inventory == null | inventory.get_ID() <= 0)
				throw new AdempiereException("@M_Inventory_ID@ @NotFound@");

			MInventoryLine invLine = new MInventoryLine(getCtx(), 0, get_TrxName());
			invLine.setM_Inventory_ID(inventory.getM_Inventory_ID());
			invLine.setAD_Org_ID(wh.getAD_Org_ID());
			invLine.setM_Product_ID(p_M_Product_ID);
			invLine.setC_Charge_ID(p_C_Charge_ID);
			invLine.setM_Locator_ID(storage[0].getM_Locator_ID());
			invLine.setQtyInternalUse(BigDecimal.valueOf(p_QtyEntered));
			invLine.set_ValueOfColumn("QtyOrdered",BigDecimal.valueOf(p_QtyEntered));
			invLine.saveEx();

			for (int i = 0; i < p_QtyEntered; i++) {
				MStorageOnHand st = storage[i];
				Timestamp dateMaterialPolicy = MStorageOnHand.getDateMaterialPolicy(p_M_Product_ID,
						st.getM_AttributeSetInstance_ID(), get_TrxName());
				dateMaterialPolicy = dateMaterialPolicy == null ? inventory.getMovementDate() : dateMaterialPolicy;
				MInventoryLineMA lineMa = MInventoryLineMA.addOrCreate(invLine, st.getM_AttributeSetInstance_ID(),
						Env.ONE, dateMaterialPolicy, false);
				lineMa.saveEx();
			}
			inventory.setDocAction(p_DocStatus);
			inventory.processIt(p_DocStatus);
			msg.append(inventory.getDocumentInfo());
		}
		return msg.toString();
	}

	/**
	 * Get Storage Info for Warehouse or locator
	 * 
	 * @param ctx                       context
	 * @param M_Warehouse_ID            ignore if M_Locator_ID > 0
	 * @param M_Product_ID              product
	 * @param M_AttributeSetInstance_ID instance id, 0 to retrieve all instance
	 * @param minGuaranteeDate          optional minimum guarantee date if all
	 *                                  attribute instances
	 * @param FiFo                      first in-first-out
	 * @param positiveOnly              if true, only return storage records with
	 *                                  qtyOnHand > 0
	 * @param M_Locator_ID              optional locator id
	 * @param trxName                   transaction
	 * @param forUpdate
	 * @return existing - ordered by location priority (desc) and/or guarantee date
	 */
	private MStorageOnHand[] getWarehouse(Properties ctx, int M_Warehouse_ID, int M_Product_ID,
			int M_AttributeSetInstance_ID, Timestamp minGuaranteeDate, boolean FiFo, boolean positiveOnly,
			int M_Locator_ID, String trxName, boolean forUpdate, int timeout) throws Exception {
		if ((M_Warehouse_ID == 0 && M_Locator_ID == 0) || M_Product_ID == 0)
			return new MStorageOnHand[0];

		boolean allAttributeInstances = false;
		if (M_AttributeSetInstance_ID == 0)
			allAttributeInstances = true;

		ArrayList<MStorageOnHand> list = new ArrayList<MStorageOnHand>();
		// Specific Attribute Set Instance
		String sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
				+ "s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
				+ "s.QtyOnHand,s.DateLastInventory,s.M_StorageOnHand_UU,s.DateMaterialPolicy "
				+ "FROM M_StorageOnHand s" + " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID)  ";
		if (M_Locator_ID > 0)
			sql += "WHERE l.M_Locator_ID = ?";
		else
			sql += "WHERE l.M_Warehouse_ID=?";
		sql += " AND NOT EXISTS ( " + "SELECT 1 " + "FROM M_AttributeSetInstance "
				+ "INNER JOIN M_InventoryLineMA ON (M_AttributeSetInstance.M_AttributeSetInstance_ID = M_InventoryLineMA.M_AttributeSetInstance_ID) "
				+ "INNER JOIN M_InventoryLine ON  (M_InventoryLineMA.M_InventoryLine_ID = M_InventoryLine.M_InventoryLine_ID) "
				+ "INNER JOIN M_Inventory ON (M_InventoryLine.M_Inventory_ID = M_Inventory.M_Inventory_ID) " + "WHERE "
				+ "	s.M_AttributeSetInstance_ID = M_AttributeSetInstance.M_AttributeSetInstance_ID "
				+ "	AND M_InventoryLine.M_Product_ID = s.M_Product_ID" + "	AND M_Inventory.HRS_Analysis_ID = "
				+ p_HRS_Analysis_ID + ")";

		sql += " AND s.M_Product_ID=?" + " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
		if (positiveOnly) {
			sql += " AND s.QtyOnHand > 0 ";
		} else {
			sql += " AND s.QtyOnHand <> 0 ";
		}
		sql += "ORDER BY l.PriorityNo DESC, DateMaterialPolicy ";
		if (!FiFo)
			sql += " DESC, s.M_AttributeSetInstance_ID DESC ";
		else
			sql += ", s.M_AttributeSetInstance_ID ";
		// All Attribute Set Instances
		if (allAttributeInstances) {
			sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
					+ " s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
					+ " s.QtyOnHand,s.DateLastInventory,s.M_StorageOnHand_UU,s.DateMaterialPolicy "
					+ " FROM M_StorageOnHand s" + " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID)"
					+ " LEFT OUTER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID) ";
			if (M_Locator_ID > 0)
				sql += "WHERE l.M_Locator_ID = ?";
			else
				sql += "WHERE l.M_Warehouse_ID=?";
			sql += " AND NOT EXISTS ( " + "SELECT 1 " + "FROM M_AttributeSetInstance "
					+ "INNER JOIN M_InventoryLineMA ON (M_AttributeSetInstance.M_AttributeSetInstance_ID = M_InventoryLineMA.M_AttributeSetInstance_ID) "
					+ "INNER JOIN M_InventoryLine ON  (M_InventoryLineMA.M_InventoryLine_ID = M_InventoryLine.M_InventoryLine_ID) "
					+ "INNER JOIN M_Inventory ON (M_InventoryLine.M_Inventory_ID = M_Inventory.M_Inventory_ID) "
					+ "WHERE " + "	s.M_AttributeSetInstance_ID = M_AttributeSetInstance.M_AttributeSetInstance_ID "
					+ "	AND M_InventoryLine.M_Product_ID = s.M_Product_ID" + "	AND M_Inventory.HRS_Analysis_ID = "
					+ p_HRS_Analysis_ID + ")";

			sql += " AND s.M_Product_ID=? ";
			if (positiveOnly) {
				sql += " AND s.QtyOnHand > 0 ";
			} else {
				sql += " AND s.QtyOnHand <> 0 ";
			}

			if (minGuaranteeDate != null) {
				sql += "AND (asi.GuaranteeDate IS NULL OR asi.GuaranteeDate>?) ";
			}

			MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);

			if (product.isUseGuaranteeDateForMPolicy()) {
				sql += "ORDER BY l.PriorityNo DESC, COALESCE(asi.GuaranteeDate,s.DateMaterialPolicy)";
				if (!FiFo)
					sql += " DESC, s.M_AttributeSetInstance_ID DESC ";
				else
					sql += ", s.M_AttributeSetInstance_ID ";
			} else {
				sql += "ORDER BY l.PriorityNo DESC, l.M_Locator_ID, s.DateMaterialPolicy";
				if (!FiFo)
					sql += " DESC, s.M_AttributeSetInstance_ID DESC ";
				else
					sql += ", s.M_AttributeSetInstance_ID ";
			}

			sql += ", s.QtyOnHand DESC";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, M_Locator_ID > 0 ? M_Locator_ID : M_Warehouse_ID);
			pstmt.setInt(2, M_Product_ID);
			if (!allAttributeInstances) {
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			} else if (minGuaranteeDate != null) {
				pstmt.setTimestamp(3, minGuaranteeDate);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getBigDecimal(11).signum() != 0) {
					MStorageOnHand storage = new MStorageOnHand(ctx, rs, trxName);
					if (!Util.isEmpty(trxName) && forUpdate) {
						DB.getDatabase().forUpdate(storage, timeout);
					}
					list.add(storage);
				}
			}
		} catch (Exception e) {
			throw new AdempiereException(e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		MStorageOnHand[] retValue = new MStorageOnHand[list.size()];
		list.toArray(retValue);
		return retValue;
	} // getWarehouse
}
