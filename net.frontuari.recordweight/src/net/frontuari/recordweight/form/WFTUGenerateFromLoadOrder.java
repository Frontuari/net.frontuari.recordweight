package net.frontuari.recordweight.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.panel.StatusBarPanel;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;

import net.frontuari.recordweight.model.I_FTU_LoadOrder;

public class WFTUGenerateFromLoadOrder extends FTUGenerateFromLoadOrder 
	implements IFormController, EventListener<Event>, WTableModelListener, ValueChangeListener {

	/**	Custom Form			*/
	private CustomForm form = new CustomForm();
	
	private Borderlayout mainLayout = new Borderlayout();
	private Panel parameterPanel = new Panel();
	private Panel actionPanel = new Panel();
	private Grid parameterLayout = GridFactory.newGridLayout();
	private Label matchModeLabel = new Label();
	private Listbox matchMode = ListboxFactory.newDropdownListbox(m_ModeList);
	private Button searchButton;
	private Button resetButton;
	private WListbox currentDataTable = (WListbox) ListboxFactory.newDataTable();
	/**	Confirm Panel		*/
	private ConfirmPanel confirmPanel;
	private Panel southPanel = new Panel();
	private Grid actionLayout = GridFactory.newGridLayout();
	
	/**	Organization			*/
	private WTableDirEditor organizationPick = null;
	
	/**	Entry Ticket			*/
	private Label 			entryTicketLabel = new Label();
	private WSearchEditor 	entryTicketPick = null;
	
	/**	Operation Type			*/
	private WTableDirEditor operationTypePick = null;
	
	/**	Product				*/
	private Label 			productLabel = new Label();
	private WSearchEditor 	productSearch = null;
	
	private StatusBarPanel statusBar = new StatusBarPanel();
	
	/**	Document Date			*/
	private Label dateFromLabel = new Label();
	private Label dateToLabel = new Label();
	private WDateEditor dateFromField = new WDateEditor("DateFrom", false, false, true, "DateFrom");
	private WDateEditor dateToField = new WDateEditor("DateTo", false, false, true, "DateTo");
	
	/**	Load Order			*/
	private Label 			labelLoadOrder = new Label();
	private WSearchEditor loadOrderSearch = null;
	
	/**	Business Partner	*/
	private Label 			bpartnerLabel = new Label();
	private WSearchEditor bpartnerSearch = null;
	
	/**	Sales Order				*/
	private WSearchEditor salesOrderSearch = null;
	/**	Distributoin Order		*/
	private WSearchEditor distributionOrderSearch = null;
	
	/**	Search				*/
	private Button 			bSearch = new Button(Msg.translate(Env.getCtx(), "Search"));
	
	/** Panels				*/
	private Panel 			loadOrderPanel = new Panel();
	private Label 			loadOrderLabel = new Label();
	private Borderlayout 	loadOrderLayout = new Borderlayout();
	
	private Borderlayout 	centerLayout = new Borderlayout();
	private Grid 			southLayout	= GridFactory.newGridLayout();
	
	
	
	public WFTUGenerateFromLoadOrder() {
		Env.setContext(Env.getCtx(), form.getWindowNo(), "IsSOTrx", "Y"); // defaults to no
		try {
			dyInit();
			zkInit();
			southPanel.appendChild(new Separator());
			southPanel.appendChild(statusBar);
		} catch (Exception e) {
			log.severe("Error:" + e.getLocalizedMessage());
		}
	}

	/**
	 * Static zkInit
	 * 
	 * @throws Exception
	 */
	private void zkInit() throws Exception {
		form.appendChild(mainLayout);
		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");
		
		parameterPanel.appendChild(parameterLayout);
		
		
		Rows rows = null;
		Row row = null;
		parameterLayout.setWidth("100%");
		//	
		North north = new North();
		north.setCollapsible(true);
		north.setTitle("Parameter");
		north.setSplittable(true);
		north.setStyle("border: none");
		north.appendChild(parameterPanel);
			
		rows = parameterLayout.newRows();
		row = rows.newRow();
		
		//	Organization
		organizationPick.getLabel().setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		row.appendChild(organizationPick.getLabel().rightAlign());
		row.appendChild(organizationPick.getComponent());
		row.appendChild(matchModeLabel.rightAlign());
		row.appendChild(matchMode);
		
		//	Entry Ticket
		entryTicketLabel.setText(Msg.translate(Env.getCtx(), "FTU_EntryTicket_ID"));
		row = rows.newRow();
		row.appendChild(entryTicketLabel.rightAlign());
		row.appendChild(entryTicketPick.getComponent());
		
		//	Load Order
		labelLoadOrder.setText(Msg.translate(Env.getCtx(), "FTU_LoadOrder_ID"));
		row = rows.newRow();
		row.appendChild(labelLoadOrder.rightAlign());
		row.appendChild(loadOrderSearch.getComponent());
		
		//	Business Partner
		bpartnerLabel.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		row.appendChild(bpartnerLabel.rightAlign());
		row.appendChild(bpartnerSearch.getComponent());
		
		//	Date Document
		dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateDoc"));
		dateToLabel.setText("-");
		row = rows.newRow();
		row.appendChild(dateFromLabel.rightAlign());
		row.appendChild(dateFromField.getComponent());
		row.appendChild(dateToLabel.rightAlign());
		row.appendChild(dateToField.getComponent());
		
		//	Operation Type
		row = rows.newRow();
		operationTypePick.getLabel().setText(Msg.translate(Env.getCtx(), "OperationType"));
		row.appendChild(operationTypePick.getLabel().rightAlign());
		row.appendChild(operationTypePick.getComponent());
		
		
		confirmPanel = new ConfirmPanel(true);
		resetButton = confirmPanel.createButton(ConfirmPanel.A_RESET);
		resetButton.addActionListener(this);
		row.appendChild(resetButton);
		searchButton = confirmPanel.createButton(ConfirmPanel.A_REFRESH);
		searchButton.addActionListener(this);
		row.appendChild(searchButton);
		confirmPanel.addActionListener(this);
		
		Center 	center = new Center();
		center.appendChild(currentDataTable);
		center.setAutoscroll(true);
		
		South south = new South();
		south.setSplittable(true);
		south.setStyle("border: none");
		south.appendChild(southPanel);
		southPanel.appendChild(actionPanel);
		
		actionPanel.appendChild(actionLayout);
		actionLayout.setWidth("100%");
		
/*		north.setStyle("border-style: solid; border-width: 1px; border-color: rgb(0,0,255)");
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		
//		Sales Order
//		salesOrderSearch.getLabel().setText(" " + Msg.translate(Env.getCtx(), "C_Order_ID"));
		
//		Product
//		productLabel.setText(Msg.translate(Env.getCtx(), "M_Product_ID"));
		
//		Distribution Order
//		distributionOrderSearch.getLabel().setText(" " + Msg.translate(Env.getCtx(), "DD_Order_ID"));
		
		//	Search
		row = rows.newRow();
		row.appendChild(new Space());
		row.appendChild(bSearch);
		bSearch.addActionListener(this);
		*/
		
	/*	
		
		rows = southLayout.newRows();
//		row.appendChild(confirmPanel);
		*/
		
		rows = actionLayout.newRows();
		row = rows.newRow();
//		row.appendChild(new Space());
		row.appendChild(confirmPanel);
		
		
		mainLayout.appendChild(north);
		mainLayout.appendChild(center);
		mainLayout.appendChild(south);
		
		
	}

	/**
	 * Dynamic Init (prepare dynamic fields)
	 * 
	 * @throws Exception if Lookups cannot be initialized
	 */
	public void dyInit() throws Exception {

		// Organization filter selection
		int AD_Column_ID = 	MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_AD_Org_ID);		//	FTU_LoadOrer.AD_Org_ID
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new WTableDirEditor("AD_Org_ID", true, false, true, lookupOrg);
		//organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addValueChangeListener(this);
		organizationPick.setMandatory(true);
		
		//  Operation Type
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_OperationType);//  FTU_LoadOrder.OperationType
		MLookup lookupTO = MLookupFactory.get(Env.getCtx(), getWindowNo(), 0, AD_Column_ID, DisplayType.List);
		operationTypePick = new WTableDirEditor("OperationType", true, false, true, lookupTO);
		operationTypePick.addValueChangeListener(this);
		
		//	Entry Ticket
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_FTU_EntryTicket_ID);//  FTU_LoadOrder.FTU_EntryTicket_ID
		MLookup lookupET = MLookupFactory.get(Env.getCtx(), getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		entryTicketPick = new WSearchEditor("FTU_EntryTicket_ID", false, false, true, lookupET);
		entryTicketPick.addValueChangeListener(this);
		
		//	Product
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_M_Product_ID);//	FTU_LoadOrer.M_Product_ID
		MLookup lookupProduct = MLookupFactory.get(Env.getCtx(), getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		productSearch = new WSearchEditor("M_Product_ID", true, false, true, lookupProduct);
		productSearch.addValueChangeListener(this);
		
		//	Business Partner
		AD_Column_ID = 2762;		//	C_Order.C_BPartner_ID
		MLookup lookupBPartner = MLookupFactory.get(Env.getCtx(), getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new WSearchEditor("C_BPartner_ID", true, false, true, lookupBPartner);
		bpartnerSearch.addValueChangeListener(this);
		
		//	Business Partner
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_FTU_LoadOrder_ID); //	FTU_LoadOrder.FTU_LoadOrder_ID
		MLookup lookup = MLookupFactory.get(Env.getCtx(), getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		loadOrderSearch = new WSearchEditor("FTU_LoadOrder_ID", true, false, true, lookup);
		loadOrderSearch.addValueChangeListener(this);
		
		//  Translation
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "FTU_LoadOrder_ID"));
		statusBar.setStatusDB("");
		
	}

	@Override
	public ADForm getForm() {
		return form;
	}

	@Override
	public void onEvent(Event e) throws Exception {
		log.config("");
		if (e.getTarget().getId().equals(ConfirmPanel.A_REFRESH)) {
			refresh();
		} else if (e.getTarget().getId().equals(ConfirmPanel.A_CANCEL)) {
			dispose();
		} else if (e.getTarget().getId().equals(ConfirmPanel.A_OK)) {
			saveData();
		}  else if (e.getTarget().getId().equals(ConfirmPanel.A_RESET)) {
        	resetParameters ();
        }
	}
	
	/**************************************************************************
	 *  Save Data
	 */
	public void saveData() {
		if(!isAvailableForSave()) {
			return;
		}
		try {
			Trx.run(new TrxRunnable() {
				public void run(String trxName) {
					statusBar.setStatusLine(saveData(getWindowNo(), trxName));
				}
			});
			//	If Ok
			refresh();
		} catch (Exception e) {
			FDialog.error(getWindowNo(), "Error", e.getLocalizedMessage());
//			Clients.showBusy(null, false);
			Clients.showBusy(null);
		} 
	}   //  saveData
	
	
	/**
	 * 	Dispose
	 */
	public void dispose() {
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose

	/**
	 * Refresh Data
	 */
	private void refresh() {
		clear();
		setIsAvailableForSave(false);
		getParameters();
		String message = validateParameters();
		if(Util.isEmpty(message)) {
//			Clients.showBusy(null, true);
			Clients.showBusy(Msg.getMsg(Env.getCtx(), "Processing"));
			loadLoadOrders();
//			Clients.showBusy(null, false);
			Clients.clearBusy();
		} else {
			FDialog.error(getWindowNo(), getForm(), "ValidationError", Msg.parseTranslation(Env.getCtx(), message));
		}
	}
	
	/**
	 * Load Payments from DB
	 */
	private void loadLoadOrders() {
		fillLoadOrders(getLoadOrderData());
	}
	
	
	/**
	 * Fill LoadOrder
	 * @param data
	 */
	private void fillLoadOrders(ArrayList<ArrayList<Object>> data) {
		//  Remove previous listeners
		currentDataTable.getModel().removeTableModelListener(this);
		// 
		configureCurrentPaymentTable(currentDataTable);
		

		List<ArrayList<Object>> list = data;
		ListModelTable model = new ListModelTable(data);
		//  Set Model
		model = new ListModelTable(list);
		model.addTableModelListener(this);
		currentDataTable.setData(model, getCurrentPaymentColumnNames());
		currentDataTable.autoSize();
	}
	
	
	/**
	 * Get parameters for search
	 */
	private void getParameters() {
		setAD_Org_ID(organizationPick.getValue() != null ? (Integer) organizationPick.getValue() : -1);
		setC_BPartner_ID(bpartnerSearch.getValue() != null ? (Integer) bpartnerSearch.getValue() : -1);
		
		
		setDateFrom((dateFromField.getValue() != null? (Timestamp) dateFromField.getValue(): null));
		setDateTo((dateToField.getValue() != null? (Timestamp) dateToField.getValue(): null));
		//	Get for matched
		setMode(matchMode.getSelectedIndex());
		//	
//		chageLayout();
		//	
		setHasSelection(false);
		statusBar.setStatusLine("");			
	}


	@Override
	public void valueChange(ValueChangeEvent e) {
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		log.config(name + "=" + value);
		if(name.equals("AD_Org_ID")) {
			setAD_Org_ID( (Integer) value);
		} else if(name.equals("FTU_LoadOrder_ID")) {
			setFTU_LoadOrder_ID( (Integer) value);
		} else if(name.equals("FTU_EntryTicket_ID")) {
			setFTU_EntryTicket_ID( (Integer) value);
		} else if(name.equals("C_BPartner_ID")) {
			setC_BPartner_ID( (Integer) value);
		} else if(name.equals("OperationType")) {
			setOperationType( (String) value);
		} else if(name.equals("AD_Org_ID")) {
			setAD_Org_ID( (Integer) value);
		} else if(name.equals("AD_Org_ID")) {
			setAD_Org_ID( (Integer) value);
		} else if(name.equals("AD_Org_ID")) {
			setAD_Org_ID( (Integer) value);
		}
		refresh();
	}

	@Override
	public void tableChanged(WTableModelEvent event) {
		// TODO Auto-generated method stub
		
	}
}
