package net.frontuari.recordweight.form;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.window.FDialog;
import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;

import net.frontuari.recordweight.model.MFTULoadOrder;
import net.frontuari.recordweight.model.MFTULoadOrderLine;

public class WApplyDeliveredRute extends CreateFrom implements EventListener<Event> {
	
	private WCreateFromWindow window;
	/** Window No               */
	private int p_WindowNo;
	protected Label bDeliveryRuteLabel = new Label();
	protected WEditor bDeliveryRuteField;
	private MFTULoadOrder lo = null;
	protected WEditor deliveryRuteLookup;

	/**	Logger			*/
	private final static CLogger log = CLogger.getCLogger(WApplyDeliveredRute.class);

	public WApplyDeliveredRute(GridTab gridTab) {
		super(gridTab);
		log.info(getGridTab().toString());
		window = new WCreateFromWindow(this, getGridTab().getWindowNo());
		p_WindowNo = getGridTab().getWindowNo();
		
		try
		{
			if (!dynInit())
				return;
			zkInit();
			setInitOK(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
		}
		AEnv.showWindow(window);
		
	}

	@Override
	public Object getWindow() {
		return window;
	}

	@Override
	public boolean dynInit() throws Exception {
		log.config("");
		//Refresh button
		Button refreshButton = window.getConfirmPanel().createButton(ConfirmPanel.A_REFRESH);
		refreshButton.addEventListener(Events.ON_CLICK, this);
		window.getConfirmPanel().addButton(refreshButton);				
		if (getGridTab().getValue("FTU_LoadOrder_ID") == null)
		{
			FDialog.error(0, window, "SaveErrorRowNotFound");
			return false;
		}
		
		int loID = (int) getGridTab().getValue("FTU_LoadOrder_ID");
		lo = new MFTULoadOrder(Env.getCtx(), loID, null);
		window.setTitle("Agregar Rutas de Despacho");
		
		MLookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, getGridTab().getTabNo(), MColumn.getColumn_ID(MFTULoadOrderLine.Table_Name, MFTULoadOrderLine.COLUMNNAME_FTU_DeliveryRute_ID), DisplayType.TableDir);
		
		deliveryRuteLookup = new WTableDirEditor ("FTU_DeliveryRute_ID", false, false, true, lookup);
		
		return true;
	}
	
	protected void zkInit() throws Exception
	{
		bDeliveryRuteLabel.setText(Msg.getElement(Env.getCtx(), "FTU_DeliveryRute_ID"));
        
		Borderlayout parameterLayout = new Borderlayout();
		parameterLayout.setHeight("130px");
		parameterLayout.setWidth("100%");
    	Panel parameterPanel = window.getParameterPanel();
		parameterPanel.appendChild(parameterLayout);
		
		Grid parameterStdLayout = GridFactory.newGridLayout();
    	Panel parameterStdPanel = new Panel();
		parameterStdPanel.appendChild(parameterStdLayout);

		Center center = new Center();
		parameterLayout.appendChild(center);
		center.appendChild(parameterStdPanel);
		
		Rows rows = (Rows) parameterStdLayout.newRows();
		Row row = rows.newRow();
		row.appendChild(bDeliveryRuteLabel.rightAlign());
		row.appendChild(deliveryRuteLookup.getComponent());
		loadLines();
	}
	
	public void showWindow()
	{
		window.setVisible(true);
	}
	
	public void closeWindow()
	{
		window.dispose();
	}

	@Override
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		
	}

	@Override
	public boolean save(IMiniTable miniTable, String trxName) {
		//	Sql get LoadOrder Lines
		String sql = "SELECT FTU_LoadOrderLine_ID FROM FTU_LoadOrderLine "
				+ "WHERE C_OrderLine_ID IN (SELECT C_OrderLine_ID FROM C_OrderLine WHERE C_Order_ID = ?) "
				+ "AND FTU_LoadOrder_ID = ?";
		
		if(deliveryRuteLookup.getValue() == null)
			throw new AdempiereException("Debe seleccionar una ruta de despacho");
		
		int drID = (Integer)deliveryRuteLookup.getValue();
		
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 1);   //  1-DocumentNo Order ID
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					int OrderID = pp.getKey();
					pstmt = DB.prepareStatement(sql, trxName);
					pstmt.setInt(1, OrderID);
					pstmt.setInt(2, lo.get_ID());
					rs = pstmt.executeQuery();
					while (rs.next())
					{
						MFTULoadOrderLine lol = new MFTULoadOrderLine(Env.getCtx(), rs.getInt(1), trxName);
						lol.setFTU_DeliveryRute_ID(drID);
						lol.saveEx();
					}
				}catch(Exception e) {
					
				}finally {
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
			}
		}
		
		
		return false;
	}

	/**
	 *  Action Listener
	 *  @param e event
	 * @throws Exception 
	 */
	@Override
	public void onEvent(Event e) throws Exception
	{
		if (log.isLoggable(Level.CONFIG)) log.config("Action=" + e.getTarget().getId());
		if(e.getTarget().equals(window.getConfirmPanel().getButton(ConfirmPanel.A_REFRESH)))
		{
			loadLines();
			window.tableChanged(null);
		}
	}
	
	/**
	 *  Load Data - LoadOrder
	 */
	protected void loadLines ()
	{
		loadTableOIS(getLoadOrderLineData(lo.get_ID()));
	}   //  LoadLines
	
	/**
	 *  Load Data - Sales Order from LoadOrderLine
	 *  @param M_InOut_ID InOut
	 */
	protected Vector<Vector<Object>> getLoadOrderLineData(int loadOrderID)
	{
		//
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder sql = new StringBuilder("SELECT DISTINCT o.C_Order_ID,o.DocumentNo,bp.TaxID,bp.Name bpName,bpl.Name BPLocation  ");   //1-5
				sql.append(" FROM C_Order o " );
				sql.append(" join C_BPartner bp on (o.c_bpartner_id = bp.c_bpartner_id) " );
				sql.append(" join c_bpartner_location bpl on (o.c_bpartner_location_id = bpl.c_bpartner_location_id) ");
				sql.append(" join c_orderline ol on (o.c_order_id = ol.c_order_id) " );
				sql.append(" WHERE EXISTS (select 1 from ftu_loadorderline lol where ol.c_orderline_id = lol.c_orderline_id " );
				sql.append(" and lol.ftu_deliveryrute_id is null and lol.ftu_loadorder_id = ?) ");
		
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		if(AD_Client_ID>0)
			sql.append(" AND o.AD_Client_ID="+AD_Client_ID);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int i=1;
			
			pstmt = DB.prepareStatement(sql.toString(), null);
				pstmt.setInt(i++, loadOrderID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(5);
				line.add(Boolean.FALSE);           	//  0-Selection
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2).trim());
				line.add(pp);			 			//	1-DocumentNo
				line.add(rs.getString(3));			//  2-TaxID
				line.add(rs.getString(4));			//  3-BPName
				line.add(rs.getString(5));			//  4-BPLocation
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		return data;
	}   //  loadLoadOrderLineData

	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	protected void loadTableOIS (Vector<?> data)
	{
		window.getWListbox().clear();
		
		//  Remove previous listeners
		window.getWListbox().getModel().removeTableModelListener(window);
		//  Set Model
		ListModelTable model = new ListModelTable(data);
		model.addTableModelListener(window);
		window.getWListbox().setData(model, getOISColumnNames());
		//
		
		configureMiniTable(window.getWListbox());
	}   //  loadTableOIS
	
	/**
	 * OIS Column Names
	 * @return
	 */
	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
	    Vector<String> columnNames = new Vector<String>(5);
	    columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
	    columnNames.add(Msg.translate(Env.getCtx(), "Documentno"));
	    columnNames.add(Msg.translate(Env.getCtx(), "TaxID"));
	    columnNames.add(Msg.translate(Env.getCtx(), "BPName"));
	    columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_Location_ID"));

	    return columnNames;
	}
	
	/***
	 * Configure MiniTable
	 * @param miniTable
	 */
	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, String.class, true);        //  1-DocumentNo
		miniTable.setColumnClass(2, String.class, true);        //  2-TaxID
		miniTable.setColumnClass(3, String.class, true);        //  2-BPName
		miniTable.setColumnClass(4, String.class, true);        //  2-BPLocation
		
		//  Table UI
		miniTable.autoSize();
	}
	
}
