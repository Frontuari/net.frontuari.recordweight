package net.frontuari.recordweight.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;

import net.frontuari.recordweight.base.FTUProcess;
import net.frontuari.recordweight.model.MFTUEntryTicket;
import net.frontuari.recordweight.model.X_I_EntryTicket;

/**
 * 
 * @author Argenis Rodríguez arodriguez@frontuari.net
 *
 */
public class ImportEntryTicket extends FTUProcess {
	
	private int p_AD_Client_ID = 0;
	
	private int p_AD_Org_ID = 0;
	
	private boolean p_DeleteOldImported = false;
	
	private String p_DocAction = MFTUEntryTicket.DOCACTION_Prepare;
	
	@Override
	protected void prepare() {
		
		for (ProcessInfoParameter param: getParameter())
		{
			String name = param.getParameterName();
			
			if ("AD_Client_ID".equals(name))
				p_AD_Client_ID = param.getParameterAsInt();
			else if ("AD_Org_ID".equals(name))
				p_AD_Org_ID = param.getParameterAsInt();
			else if ("DeleteOldImported".equals(name))
				p_DeleteOldImported = "Y".equals(param.getParameter());
			else if ("DocAction".equals(name))
				p_DocAction = param.getParameterAsString();
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		StringBuffer sql = null;
		int no = 0;
		StringBuffer clientCheck = new StringBuffer(" AND AD_Client_ID = ").append(p_AD_Client_ID);
		
		if (p_DeleteOldImported)
		{
			sql = new StringBuffer("DELETE FROM I_EntryTicket")
					.append(" WHERE I_IsImported = 'Y'").append(clientCheck);
			
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Delete Old Imported Records " + no);
		}
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET AD_Client_ID = COALESCE(AD_Client_ID, ").append(p_AD_Client_ID).append(")")
				.append(", AD_Org_ID = COALESCE(AD_Org_ID, ").append(p_AD_Org_ID).append(")")
				.append(", IsActive = COALESCE(IsActive, 'Y')")
				.append(", Created = COALESCE(Created, SysDate)")
				.append(", Updated = COALESCE(Updated, SysDate)")
				.append(", CreatedBy = COALESCE(CreatedBy, 0)")
				.append(", UpdatedBy = COALESCE(UpdatedBy, 0)")
				.append(", I_ErrorMsg = ' '")
				.append(", I_IsImported = 'N'")
				.append(" WHERE I_IsImported <> 'Y' OR I_IsImported IS NULL");
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.INFO)) log.info("Reset= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET I_ErrorMsg= I_ErrorMsg || 'ERR=Invalid Org, '")
				.append(" WHERE (AD_Org_ID IS NULL OR AD_Org_ID = 0")
				.append(" OR EXISTS(")
				.append("SELECT 1 FROM AD_Org org WHERE et.AD_Org_ID = org.AD_Org_ID AND (org.IsActive = 'N' OR org.IsSummary = 'Y'))")
				.append(") AND I_IsImported <> 'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid Org= " + no);
		
		//Document Type - FET
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET C_DocType_ID = (SELECT C_DocType_ID FROM C_DocType cd WHERE cd.Name = et.DocTypeName")
				.append(" AND cd.AD_Client_ID = et.AD_Client_ID AND cd.DocBaseType = 'FET')")
				.append(" WHERE DocTypeName IS NOT NULL AND C_DocType_ID IS NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set DocType= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket SET I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid DocTypeName, '")
				.append(", I_IsImported = 'E'")
				.append(" WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid DocTypeName= " + no);
		
		//Default Document Type - FET
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET C_DocType_ID = (SELECT MAX(C_DocType_ID) FROM C_DocType cd WHERE cd.DocBaseType = 'FET'")
				.append(" AND cd.AD_Client_ID = et.AD_Client_ID AND cd.IsDefault = 'Y')")
				.append(" WHERE C_DocType_ID IS NULL AND DocTypeName IS NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default DocType= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid DocType, '")
				.append(" WHERE C_DocType_ID IS NULL AND DocTypeName IS NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid DocType= " + no);
		
		//Search BPartner
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET C_BPartner_ID = (SELECT C_BPartner_ID FROM C_BPartner cb WHERE cb.Value = et.BPValue")
				.append(" AND cb.AD_Client_ID = et.AD_Client_ID)")
				.append(" WHERE C_BPartner_ID IS NULL AND BPValue IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET BPartner= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg= I_ErrorMsg || 'ERR=Invalid BPartnerValue, '")
				.append(" WHERE C_BPartner_ID IS NULL AND BPValue IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid BPartner= " + no);
		
		//Search Driver
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET FTU_Driver_ID = (SELECT MAX(FTU_Driver_ID) FROM FTU_Driver dr WHERE dr.Value = et.DriverValue")
				.append(" AND dr.AD_Client_ID = et.AD_Client_ID)")
				.append(" WHERE FTU_Driver_ID IS NULL AND DriverValue IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET Driver= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid DriverValue, '")
				.append(" WHERE FTU_Driver_ID IS NULL AND DriverValue IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid Driver= " + no);
		
		//Search Order
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET C_Order_ID = (SELECT MAX(C_Order_ID) FROM C_Order co WHERE co.DocumentNo = et.OrderDocumentNo")
				.append(" AND co.C_BPartner_ID = et.C_BPartner_ID AND co.AD_Client_ID = et.AD_Client_ID)")
				.append(" WHERE C_Order_ID IS NULL AND OrderDocumentNo IS NOT NULL AND C_BPartner_ID IS NOT NULL")
				.append(" AND I_IsImported <> 'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET Order= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid OrderDocumentNo, '")
				.append(" WHERE C_Order_ID IS NULL AND OrderDocumentNo IS NOT NULL AND C_BPartner_ID IS NOT NULL")
				.append(" AND I_IsImported <> 'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid Order= " + no);
		
		//Search Product ID
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET M_Product_ID = (SELECT M_Product_ID FROM M_Product prd WHERE prd.Value = et.ProductValue")
				.append(" AND prd.AD_Client_ID = et.AD_Client_ID)")
				.append(" WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET M_Product_ID = (SELECT MAX(M_Product_ID) FROM M_Product prd WHERE prd.Name = et.ProductName")
				.append(" AND prd.AD_Client_ID = et.AD_Client_ID)")
				.append(" WHERE M_Product_ID IS NULL AND ProductName IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no += DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET Product= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid ProductValue, '")
				.append(" WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid ProductName, '")
				.append(" WHERE M_Product_ID IS NULL AND ProductName IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no += DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid Product " + no);
		
		//Search Order Line
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET C_OrderLine_ID = (SELECT MAX(C_OrderLine_ID) FROM C_OrderLine ol WHERE ol.C_Order_ID = et.C_Order_ID")
				.append(" AND ol.M_Product_ID = et.M_Product_ID)")
				.append(" WHERE C_OrderLine_ID IS NULL AND C_Order_ID IS NOT NULL AND M_Product_ID IS NOT NULL")
				.append(" AND I_IsImported <> 'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET Order Line= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Not Found Order Line, '")
				.append(" WHERE C_OrderLine_ID IS NULL AND C_Order_ID IS NOT NULL AND M_Product_ID IS NOT NULL")
				.append(" AND I_IsImported <> 'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Order Lines Not Found= " + no);
		
		//Search Shipper ID
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET M_Shipper_ID = (SELECT MAX(M_Shipper_ID) FROM M_Shipper shp WHERE shp.Name = et.ShipperName")
				.append(" AND shp.AD_Client_ID = et.AD_Client_ID)")
				.append(" WHERE M_Shipper_ID IS NULL AND ShipperName IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET Shipper= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
			.append(" SET I_IsImported = 'E'")
			.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid ShiperName, '")
			.append(" WHERE M_Shipper_ID IS NULL AND ShipperName IS NOT NULL AND I_IsImported <> 'Y'")
			.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid Shipper= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket et")
				.append(" SET FTU_Vehicle_ID = (SELECT MAX(FTU_Vehicle_ID) FROM FTU_Vehicle vh WHERE vh.VehiclePlate = et.VehiclePlate")
				.append(" AND et.AD_Client_ID = vh.AD_Client_ID)")
				.append(" WHERE FTU_Vehicle_ID IS NULL AND VehiclePlate IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("SET Vehicle= " + no);
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'E'")
				.append(", I_ErrorMsg = I_ErrorMsg || 'ERR=Invalid Vehicle'")
				.append(" WHERE FTU_Vehicle_ID IS NULL AND VehiclePlate IS NOT NULL AND I_IsImported <> 'Y'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		if (no != 0)
			log.warning("Invalid Vehicle= " + no);
		
		commitEx();
		
		sql = new StringBuffer("SELECT * FROM I_EntryTicket")
				.append(" WHERE I_IsImported = 'N'");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int nInserted = 0;
		String errorMsg = null;
		
		try {
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
			
			while (rs.next() && errorMsg == null)
			{
				X_I_EntryTicket imp = new X_I_EntryTicket(getCtx(), rs, get_TrxName());
				MFTUEntryTicket entryTicket = new MFTUEntryTicket(getCtx(), 0, get_TrxName());
				entryTicket.setAD_Org_ID(imp.getAD_Org_ID());
				entryTicket.setDocumentNo(imp.getDocumentNo());
				entryTicket.setDateDoc(imp.getDateDoc());
				entryTicket.setDescription(imp.getDescription());
				entryTicket.setC_BPartner_ID(imp.getC_BPartner_ID());
				entryTicket.setC_DocType_ID(imp.getC_DocType_ID());
				entryTicket.setFTU_Driver_ID(imp.getFTU_Driver_ID());
				entryTicket.setOperationType(imp.getOperationType());
				entryTicket.setC_Order_ID(imp.getC_Order_ID());
				entryTicket.setM_Product_ID(imp.getM_Product_ID());
				entryTicket.setC_OrderLine_ID(imp.getC_OrderLine_ID());
				entryTicket.setM_Shipper_ID(imp.getM_Shipper_ID());
				entryTicket.setFTU_Vehicle_ID(imp.getFTU_Vehicle_ID());
				entryTicket.setTrailerPlate(imp.getTrailerPlate());
				entryTicket.saveEx();
				
				imp.setFTU_EntryTicket_ID(entryTicket.get_ID());
				imp.setI_IsImported(true);
				imp.setProcessed(true);
				imp.saveEx();
				
				if (!entryTicket.processIt(p_DocAction))
					errorMsg = "Failed Process Entry Ticket " + entryTicket.getDocumentNo() + " " +  entryTicket.getProcessMsg();
				else
					entryTicket.saveEx();
				nInserted++;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Entry Ticket " + sql, e);
			errorMsg = e.getLocalizedMessage();
		} finally {
			DB.close(rs, pstmt);
		}
		
		sql = new StringBuffer("UPDATE I_EntryTicket")
				.append(" SET I_IsImported = 'N'")
				.append(" WHERE I_IsImported <> 'Y'")
				.append(clientCheck);
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		return Optional.ofNullable(errorMsg).orElse("@FTU_EntryTicket_ID@= " + nInserted);
	}
}
