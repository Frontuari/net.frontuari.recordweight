package net.frontuari.recordweight.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Vector;

import org.adempiere.util.Callback;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.WListbox;
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
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_C_SalesRegion;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MProduct;
import org.compiere.model.MQuery;
import org.compiere.model.MUOM;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_C_Order;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportCtl;
import org.compiere.print.ReportEngine;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

import net.frontuari.recordweight.model.I_FTU_LoadOrder;
import net.frontuari.recordweight.util.StringNamePair;

public class WFTULoadOrder extends FTULoadOrder
	implements IFormController, EventListener<Event>, WTableModelListener, ValueChangeListener
{
	
	/**
	 * 
	 * *** Constructor ***
	 */
	public WFTULoadOrder() {
		Env.setContext(Env.getCtx(), form.getWindowNo(), "IsSOTrx", "Y");   //  defaults to no
		try	{
			
			dyInit();
			zkInit();
			//	Load Default Values
			loadDefaultValues();
			
		} catch(Exception e) {
			log.severe("Error:" + e.getLocalizedMessage());
		}
	}

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	
	/**	Custom Form			*/
	private CustomForm form = new CustomForm();

	private Borderlayout mainLayout = new Borderlayout();

	private Grid 			parameterLayout		= GridFactory.newGridLayout();
	private Panel 			parameterPanel = new Panel();
	/**	Organization			*/
	private WTableDirEditor organizationPick = null;
	/**	Sales Region			*/
	private WTableDirEditor salesRegionPick = null;
	/**	Sales Representative	*/
	private Label 			salesRepLabel = new Label();
	private WTableDirEditor 		salesRepSearch = null;
	/**	Warehouse				*/
	private Label 			warehouseLabel = new Label();
	private Listbox 		warehouseSearch = ListboxFactory.newDropdownListbox();
	/**	Operation Type			*/
	private WTableDirEditor operationTypePick = null;
	/**	Document Type			*/
	private Label 			docTypeLabel = new Label();
	private Listbox 		docTypeSearch = ListboxFactory.newDropdownListbox();
	/**	Document Type Target	*/
	private WTableDirEditor	docTypeTargetPick = null;
	/**	Invoice Rule			*/
	private Label 			invoiceRuleLabel = new Label();
	private WTableDirEditor invoiceRulePick = null;
	/**	Delivery Rule			*/
	private Label 			deliveryRuleLabel = new Label();
	private WTableDirEditor deliveryRulePick = null;
	/**	Vehicle Type			*/
	private WTableDirEditor vehicleTypePick = null;
	/**	Document Date			*/
	private Label 			labelDateDoc = new Label();
	private Datebox 		dateDocField = new Datebox();
	/**	Shipment Date			*/
	private Label 			labelShipDate = new Label();
	private Datebox 		shipDateField = new Datebox();
	/**	Entry Ticket			*/
	private Label 			entryTicketLabel = new Label();
	private WSearchEditor 	entryTicketPick = null;
	/**	Shipper					*/
	private Label 			shipperLabel = new Label();
	private WTableDirEditor shipperPick = null;
	/**	Driver					*/
	private Label 			driverLabel = new Label();
	private Listbox 		driverSearch = ListboxFactory.newDropdownListbox();
	/**	Vehicle					*/
	private Label 			vehicleLabel = new Label();
	private Listbox 		vehicleSearch = ListboxFactory.newDropdownListbox();
	/**	Load Capacity			*/
	private Label 			loadCapacityLabel = new Label();
	private Doublebox 		loadCapacityField = new Doublebox();
	
	/**	Volume Capacity			*/
	private Label 			volumeCapacityLabel = new Label();
	private Doublebox 		volumeCapacityField = new Doublebox();
	/**	Bulk				*/
	private Checkbox 		isBulkCheck = new Checkbox();
	/**	Product				*/
	private Label 			productLabel = new Label();
	private WSearchEditor 	productSearch = null;
	/**	Business Partner	*/
	private Label 			bpartnerLabel = new Label();
	private WSearchEditor bpartnerSearch = null;

	private DateFormat 		dateFormat 		 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** Panels				*/
	private Panel 			orderPanel = new Panel();
	private Panel 			orderLinePanel = new Panel();
	private Label 			orderLabel = new Label();
	private Label 			stockLabel = new Label();
	private Label 			orderLineLabel = new Label();
	private Borderlayout 	orderLayout = new Borderlayout();
	private Borderlayout 	orderLineLayout = new Borderlayout();
	private Borderlayout 	stockLayout = new Borderlayout();
	private Panel 			stockPanel = new Panel();
	private Borderlayout 	medioLayout = new Borderlayout();
	private Panel 			medioPanel = new Panel();
	private Panel 			southPanel = new Panel();
	private Panel 			allocationPanel = new Panel();
	private Grid 			allocationLayout = GridFactory.newGridLayout();
	private Borderlayout 	infoLayout = new Borderlayout();
	private South 			south1 = new South();
	/**	Collapsible Panel for Parameter		*/
	private North 			north = new North();
	private North 			north1 = new North();
	/** Order Line			*/
	private Label 			orderLineInfo = new Label();
	/** Weight Difference	*/
	private Label 			weightDiffLabel = new Label();
	private NumberBox 		weightDiffField = null;
	/** Volume Difference	*/
	private Label 			volumeDiffLabel = new Label();
	private NumberBox 		volumeDiffField = null;
	/** Generate Load Order	*/
	private Button 			gLoadOrderButton = new Button(Msg.translate(Env.getCtx(), "GenerateOrder"));
	/** Select all Button   */
	private Button 			selectAllButton =  new Button();
	/**	Search				*/
	private Button 			bSearch = new Button(Msg.translate(Env.getCtx(), "Search"));
	/** Order Table 		*/
	private WListbox 		w_orderTable = ListboxFactory.newDataTable();
	/** Order Line Table 	*/
	private WListbox 		w_orderLineTable = ListboxFactory.newDataTable();
	/** Stock Table 	*/
	private WListbox		stockTable = ListboxFactory.newDataTable();;
	private ListModelTable  stockModel = null; 
	private int 			count = 0;
	/**	Payment Info		*/
	private Label 			paymentInfo = new Label();
	/**	Stock Info		*/
	private Label 			stockInfo = new Label();
	/**	Invoice Info		*/
	private Label 			invoiceInfo = new Label();
	private Label			invoiceLabel = new Label();

	private StatusBarPanel statusBar = new StatusBarPanel();
	/**
	 *  Static zkInit
	 *  @throws Exception
	 */
	private void zkInit() throws Exception
	{
		form.appendChild(mainLayout);

		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");
		parameterPanel.appendChild(parameterLayout);
		
		Rows rows = null;
		Row row = null;
		parameterLayout.setWidth("100%");
		rows = parameterLayout.newRows();
		row = rows.newRow();
		//
		driverLabel.setText(Msg.translate(Env.getCtx(), "FTU_Driver_ID"));
		shipperLabel.setText(Msg.translate(Env.getCtx(), "M_Shipper_ID"));
		vehicleLabel.setText(Msg.translate(Env.getCtx(), "FTU_Vehicle_ID"));
		salesRegionPick.getLabel().setText(Msg.translate(Env.getCtx(), "C_SalesRegion_ID"));
		salesRepLabel.setText(Msg.translate(Env.getCtx(), "SalesRep_ID"));
		loadCapacityLabel.setText(Msg.translate(Env.getCtx(), "LoadCapacity"));
		volumeCapacityLabel.setText(Msg.translate(Env.getCtx(), "VolumeCapacity"));
		vehicleTypePick.getLabel().setText(Msg.translate(Env.getCtx(), "FTU_VehicleType_ID"));
		entryTicketLabel.setText(Msg.translate(Env.getCtx(), "FTU_EntryTicket_ID"));
		orderPanel.appendChild(orderLayout);
		orderLinePanel.appendChild(orderLineLayout);
		medioPanel.appendChild(medioLayout);
		stockPanel.appendChild(stockLayout);
		//	Operation Type
		operationTypePick.getLabel().setText(Msg.translate(Env.getCtx(), "OperationType"));
		//	Document Type
		docTypeLabel.setText(Msg.translate(Env.getCtx(), "C_DocType_ID"));
		//	Document Type Target
		docTypeTargetPick.getLabel().setText(Msg.translate(Env.getCtx(), "C_DocTypeTarget_ID"));
		//	Invoice Rule
		invoiceRuleLabel.setText(Msg.translate(Env.getCtx(), "InvoiceRule"));
		//	Delivery Rule
		deliveryRuleLabel.setText(Msg.translate(Env.getCtx(), "DeliveryRule"));
		//	Warehouse
		warehouseLabel.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		//	Product
		productSearch.getLabel().setText(Msg.translate(Env.getCtx(), "M_Product_ID"));
		//	Business Partner
		bpartnerSearch.getLabel().setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		
		orderLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Order_ID"));
		stockLabel.setText("yruy " + Msg.translate(Env.getCtx(), "C_Order_ID"));
		orderLineLabel.setText(" " + Msg.translate(Env.getCtx(), "C_OrderLine_ID"));
		orderPanel.appendChild(orderLayout);
		orderPanel.setWidth("100%");
		orderPanel.setHeight("100%");
		orderLayout.setWidth("100%");
		orderLayout.setHeight("50%");
		orderLayout.setStyle("border: none");
		stockPanel.appendChild(stockLayout);
		stockPanel.setWidth("100%");
		stockPanel.setHeight("100%");
		stockLayout.setWidth("100%");
		stockLayout.setHeight("50%");
		stockLayout.setStyle("border: none");
		
		gLoadOrderButton.addActionListener(this);
		//	Weight Difference
		weightDiffLabel.setText("Diferencia de Peso");
		weightDiffField = new NumberBox(true);
		weightDiffField.setValue(Env.ZERO);
		//	Volume Difference
		volumeDiffLabel.setText("Diferencia de Volumen");
		volumeDiffField = new NumberBox(true);
		volumeDiffField.setValue(Env.ZERO);
		
		organizationPick.getLabel().setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		row.appendChild(organizationPick.getLabel().rightAlign());
		row.appendChild(organizationPick.getComponent());
		row.appendChild(salesRegionPick.getLabel().rightAlign());
		row.appendChild(salesRegionPick.getComponent());
		row.appendChild(salesRepLabel.rightAlign());
		row.appendChild(salesRepSearch.getComponent());
		//	Storage
		row = rows.newRow();
		row.appendChild(warehouseLabel.rightAlign());
		row.appendChild(warehouseSearch);
		warehouseSearch.setWidth("200px");
		//	Operation Type
		row.appendChild(operationTypePick.getLabel().rightAlign());
		row.appendChild(operationTypePick.getComponent());
		
		//	Document Type
		row.appendChild(docTypeLabel.rightAlign());
		row.appendChild(docTypeSearch);
		docTypeSearch.setWidth("200px");
		row = rows.newRow();
		//	Document Type Target
		row.appendChild(docTypeTargetPick.getLabel().rightAlign());
		row.appendChild(docTypeTargetPick.getComponent());
		//	Invoice Rule
		row.appendChild(invoiceRuleLabel.rightAlign());
		row.appendChild(invoiceRulePick.getComponent());
		//	Delivery Rule
		row.appendChild(deliveryRuleLabel.rightAlign());
		row.appendChild(deliveryRulePick.getComponent());
		row = rows.newRow();
		//	Vehicle Type
		row.appendChild(vehicleTypePick.getLabel().rightAlign());
		row.appendChild(vehicleTypePick.getComponent());
		
		//	Document Date
		row.appendChild(labelDateDoc.rightAlign());
		row.appendChild(dateDocField);
		//	Shipment Date
		row.appendChild(labelShipDate.rightAlign());
		row.appendChild(shipDateField);
		//	Entry Ticket
		row = rows.newRow();
		row.appendChild(entryTicketLabel.rightAlign());
		row.appendChild(entryTicketPick.getComponent());
		//	Shipper
		row.appendChild(shipperLabel.rightAlign());
		row.appendChild(shipperPick.getComponent());
		
		//	Driver
		row.appendChild(driverLabel.rightAlign());
		row.appendChild(driverSearch);
		driverSearch.setWidth("200px");
		row = rows.newRow();
		//	Vehicle
		row.appendChild(vehicleLabel.rightAlign());
		row.appendChild(vehicleSearch);
		vehicleSearch.setWidth("200px");
		//	Load Capacity
		row.appendChild(loadCapacityLabel.rightAlign());
		row.appendChild(loadCapacityField);
		loadCapacityField.setReadonly(true);
		//	Volume Capacity
		row.appendChild(volumeCapacityLabel.rightAlign());
		row.appendChild(volumeCapacityField);
		volumeCapacityField.setReadonly(true);
		row = rows.newRow();
		//	Bulk
		isBulkCheck.setSelected(false);
		//	Product
		row.appendChild(productSearch.getLabel().rightAlign());
		row.appendChild(productSearch.getComponent());
		productLabel.setVisible(false);
		productSearch.setVisible(false);
		//	Business Partner
		row.appendChild(bpartnerSearch.getLabel().rightAlign());
		row.appendChild(bpartnerSearch.getComponent());
		bpartnerLabel.setVisible(false);
		bpartnerSearch.setVisible(false);
		//	Search
		row.appendChild(new Space());
		row.appendChild(bSearch);
		bSearch.addActionListener(this);
		//	
		north1 = new North();
		north1.setCollapsible(true);
		north1.setTitle("Parameter");
			
		north1.setStyle("border-style: solid; border-width: 1px; border-color: rgb(0,0,255)");
		mainLayout.appendChild(north1);
		north1.appendChild(parameterPanel);
		
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		
		south.appendChild(southPanel);
		southPanel.appendChild(allocationPanel);
		southPanel.appendChild(statusBar);
		allocationPanel.appendChild(allocationLayout);
		allocationLayout.setWidth("100%");
		rows = allocationLayout.newRows();
		row = rows.newRow();
		row.appendChild(selectAllButton);
		selectAllButton.setImage("/theme/default/images/Report24.png");
		row.appendChild(weightDiffLabel.rightAlign());
		row.appendChild(weightDiffField);
		row.appendChild(volumeDiffLabel.rightAlign());
		row.appendChild(volumeDiffField);
		row.appendChild(gLoadOrderButton);
		volumeDiffField.setEnabled(false);
		weightDiffField.setEnabled(false);
		
		invoiceLabel.setText(" " + Msg.translate(Env.getCtx(), "C_OrderLine_ID"));

		invoiceInfo.setText(".");
		stockInfo.setText(".");
		paymentInfo.setText(".");
		orderPanel.appendChild(orderLayout);
		orderPanel.setWidth("100%");
		orderPanel.setHeight("100%");
		orderLayout.setWidth("100%");
		orderLayout.setHeight("100%");
		orderLayout.setStyle("border: none");
		
		orderLinePanel.appendChild(orderLineLayout);
		orderLinePanel.setWidth("100%");
		orderLinePanel.setHeight("100%");
		orderLineLayout.setWidth("100%");
		orderLineLayout.setHeight("100%");
		orderLineLayout.setStyle("border: none");
		
		stockPanel.appendChild(stockLayout);
		stockPanel.setWidth("100%");
		stockPanel.setHeight("100%");
		stockLayout.setWidth("100%");
		stockLayout.setHeight("100%");
		stockLayout.setStyle("border: none");
		
		medioPanel.appendChild(medioLayout);
		medioPanel.setWidth("100%");
		medioPanel.setHeight("100%");
		medioLayout.setWidth("100%");
		medioLayout.setHeight("100%");
		medioLayout.setStyle("border: none");
		
		
		north = new North();
		north.setStyle("border: none");
		orderLayout.appendChild(north);
		north.appendChild(orderLabel);
		south = new South();
		south.setStyle("border: none");
		orderLayout.appendChild(south);
		south.appendChild(paymentInfo.rightAlign());
		Center center = new Center();
		orderLayout.appendChild(center);
		center.appendChild(w_orderTable);
		w_orderTable.setWidth("99%");
		center.setStyle("border: none");
		north = new North();
		north.setStyle("border: none");
		orderLineLayout.appendChild(north);
		north.appendChild(invoiceLabel);
		south = new South();
		south.setStyle("border: none");
		south.appendChild(invoiceInfo.rightAlign());
		center = new Center();
		orderLineLayout.appendChild(center);
		center.appendChild(w_orderLineTable);
		orderLineLayout.appendChild(south);
		w_orderLineTable.setWidth("100%");
		center.setStyle("border: 1px solid #000; height:100%");
		north = new North();
		north.setStyle("border: none; height:90%;");
		stockLayout.appendChild(north);
		north.setTitle(Msg.translate(Env.getCtx(), "WarehouseStockGroup"));
		north.appendChild(invoiceLabel);
		south = new South();
		south.setStyle("border: none");
		south.appendChild(stockInfo.rightAlign());
		center = new Center();
		stockLayout.appendChild(center);
		center.appendChild(stockTable);
		stockLayout.appendChild(south);
		stockTable.setWidth("100%");
		center.setStyle("border: 1px solid #000; height:50%");

		north = new North();
		north.setStyle("border: none; height:90%;");
		medioLayout.appendChild(north);
		north.appendChild(w_orderLineTable);
		north.setHeight("100%");
		south1 = new South();
		medioLayout.appendChild(south1);
		south1.appendChild(stockTable);
		south1.setTitle(Msg.translate(Env.getCtx(), "WarehouseStockGroup"));
		south1.setStyle("border-style: solid; border-width: 1px; border-color: rgb(0,0,255)");
		south1.addEventListener("onClick", this);
		south1.setHeight("50%");
		south1.setZIndex(99);
		south1.setCollapsible(true);
		south1.setOpen(false);
		south1.setSplittable(true);
		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoLayout);
		center.setAutoscroll(true);

		infoLayout.setStyle("border: none");
		infoLayout.setWidth("100%");
		infoLayout.setHeight("100%");
		
		north = new North();
		north.setStyle("border: none");
		north.setHeight("50%");
		infoLayout.appendChild(north);
		north.appendChild(orderLayout);
		north.setSplittable(true);

		center = new Center();
		center.setStyle("border: none");
		infoLayout.appendChild(center);
		center.appendChild(medioLayout);
		center.setAutoscroll(true);
	}   //  jbInit

	/**
	 *  Dynamic Init (prepare dynamic fields)
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dyInit() throws Exception
	{
		//	Set Client
		m_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		//  Load Default Values
		loadDefaultValues();
		// Organization filter selection
		int AD_Column_ID = 	MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_AD_Org_ID);		//	FTU_LoadOrer.AD_Org_ID
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new WTableDirEditor("AD_Org_ID", true, false, true, lookupOrg);
		//organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addValueChangeListener(this);
		organizationPick.setMandatory(true);
		
		//	Sales Region
		AD_Column_ID = MColumn.getColumn_ID(I_C_SalesRegion.Table_Name, I_C_SalesRegion.COLUMNNAME_C_SalesRegion_ID);	;	//	C_SalesRegion.C_SalesRegion_ID
		MLookup lookupWar = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		salesRegionPick = new WTableDirEditor("C_SalesRegion_ID", false, false, true, lookupWar);
		//salesRegion.setValue(Env.getAD_Org_ID(Env.getCtx()));
		salesRegionPick.addValueChangeListener(this);
		
		//	Sales Representative
		AD_Column_ID = MColumn.getColumn_ID(I_C_Order.Table_Name, I_C_Order.COLUMNNAME_SalesRep_ID);//	C_Order.SalesRep_ID
		MLookup lookupSal = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		salesRepSearch = new WTableDirEditor("SalesRep_ID", false, false, true, lookupSal);
		//salesRepSearch.setValue(Env.getAD_Org_ID(Env.getCtx()));
		salesRepSearch.addValueChangeListener(this);
						
		//  Operation Type
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_OperationType);//  FTU_LoadOrder.OperationType
		MLookup lookupTO = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		operationTypePick = new WTableDirEditor("OperationType", true, false, true, lookupTO);
		operationTypePick.addValueChangeListener(this);

		//  Document Type Target
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_C_DocType_ID); //  FTU_LoadOrder.C_DocTypeTarget_ID
		MLookup lookupDTT = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Table);
		docTypeTargetPick = new WTableDirEditor("C_DocType_ID", true, false, true, lookupDTT);
		docTypeTargetPick.addValueChangeListener(this);
		
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_InvoiceRule);//  FTU_LoadOrder.InvoiceRule
		MLookup lookupIR = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		invoiceRulePick = new WTableDirEditor("InvoiceRule", false, false, true, lookupIR);
		//invoiceRulePick.setValue(X_C_Order.INVOICERULE_Immediate);
		invoiceRulePick.addValueChangeListener(this);
		
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_DeliveryRule);//  FTU_LoadOrder.DeliveryRule
		MLookup lookupDR = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		deliveryRulePick = new WTableDirEditor("DeliveryRule", false, false, true, lookupDR);
		//deliveryRulePick.setValue(X_C_Order.DELIVERYRULE_Availability);
		deliveryRulePick.addValueChangeListener(this);
		
		//	Entry Ticket
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_FTU_EntryTicket_ID);//  FTU_LoadOrder.FTU_EntryTicket_ID
		MLookup lookupET = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		entryTicketPick = new WSearchEditor("FTU_EntryTicket_ID", false, false, true, lookupET);
		entryTicketPick.addValueChangeListener(this);
		
		//  Shipper
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_M_Shipper_ID);//  FTU_LoadOrder.M_Shipper_ID
		MLookup lookupSP = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		shipperPick = new WTableDirEditor("M_Shipper_ID", false, true, true, lookupSP);
		shipperPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		shipperPick.addValueChangeListener(this);
		
		//  Vehicle Type
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_FTU_VehicleType_ID);//  FTU_LoadOrder.FTU_VehicleType_ID
		MLookup lookupVT = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Table);
		vehicleTypePick = new WTableDirEditor("FTU_VehicleType_ID", true, false, true, lookupVT);
		vehicleTypePick.addValueChangeListener(this);
		
		//	Product
		AD_Column_ID = MColumn.getColumn_ID(I_FTU_LoadOrder.Table_Name, I_FTU_LoadOrder.COLUMNNAME_M_Product_ID);//	FTU_LoadOrer.M_Product_ID
		MLookup lookupProduct = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		productSearch = new WSearchEditor("M_Product_ID", true, false, true, lookupProduct);
		productSearch.addValueChangeListener(this);
		
		//	Business Partner
		AD_Column_ID = 2762;		//	C_Order.C_BPartner_ID
		MLookup lookupBPartner = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new WSearchEditor("C_BPartner_ID", true, false, true, lookupBPartner);
		bpartnerSearch.addValueChangeListener(this);

		//	Visible
		productLabel.setVisible(false);
		productSearch.setVisible(false);
		bpartnerLabel.setVisible(false);
		bpartnerSearch.setVisible(false);
		
		driverSearch.setEnabled(false);
		vehicleSearch.setEnabled(false);

		//	Document Type Order
		docTypeSearch.addActionListener(this);
		
		//	Warehouse
		warehouseSearch.addActionListener(this);
		
		//	Select All Items
		selectAllButton.addActionListener(this);
		
		//  Translation
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "FTU_LoadOrder_ID"));
		statusBar.setStatusDB("");
		
	}   //  dynInit
 
	/**
	 * Set Capacity for Weight and Volume
	 * @return void
	 */
	private void setFillCapacity() {
		setCapacity();
		loadCapacityField.setValue(m_LoadCapacity.doubleValue());
		volumeCapacityField.setValue(m_VolumeCapacity.doubleValue());
	}
	
	/**
	 * Set Value on Is Bulk
	 * @return void
	 */
	private void setIsBulk() {
		//	Set Context
		productLabel.setVisible(m_IsBulk);
		productSearch.setVisible(m_IsBulk);
		bpartnerLabel.setVisible(m_IsBulk);
		bpartnerSearch.setVisible(m_IsBulk);
	}
	
	/**
	 * Clear Data of Table
	 * @return void
	 */
	private void clearData() {
		w_orderTable.getModel().removeTableModelListener(this);
		ListModelTable modelP = new ListModelTable();
		w_orderTable.setModel(modelP);
		modelP = new ListModelTable();
		w_orderLineTable.getModel().removeTableModelListener(this);
		w_orderLineTable.setModel(modelP);
		count=0;
		//	Set Stock Model
		if(stockTable.getColumnCount()>1){
			stockTable.setModel(stockModel);
			setStockColumnClass(stockTable);
		}
		//	Parameters
		salesRegionPick.setValue(null);
		salesRepSearch.setValue(null);
		invoiceRulePick.setValue(null);
		deliveryRulePick.setValue(null);
		dateDocField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		shipDateField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		entryTicketPick.setValue(null);
		shipperPick.setValue(null);
		vehicleTypePick.setValue(null);
		driverSearch.removeAllItems();
		loadCapacityField.setValue(0);
		volumeCapacityField.setValue(0);
		productSearch.setValue(null);
		bpartnerSearch.setValue(null);
	}

	/**
	 * Search Data
	 * @return void
	 */
	private void cmd_search() {
		getPanelValues();
		String msg = null;
		//	Valid Organization
		if(m_AD_Org_ID == -1)
			msg = "@AD_Org_ID@ @NotFound@";
		//	Valid Weight UOM
		else if(m_C_UOM_Weight_ID == 0)
			msg = "@C_UOM_Weight_ID@ @of@ @AD_Client_ID@ @NotFound@";
		//	Valid Volume UOM
//		else if(m_C_UOM_Volume_ID == 0)
//			msg = "@C_UOM_Volume_ID@ @of@ @AD_Client_ID@ @NotFound@";
		//	Valid Operation Type
		else if(m_OperationType == null)
			msg = "@OperationType@ @NotFound@";
		//	Vehicle Type
		else if(m_FTU_VehicleType_ID <= 0)
			msg = "@FTU_VehicleType_ID@ @NotFound@";
		//	Document Type Load Order
		else if(m_C_DocTypeTarget_ID <= 0)
			msg = "@C_DocTypeTarget_ID@ @NotFound@";
		else if(m_IsBulk) {
			if(m_M_Product_ID <= 0)
				msg = "@M_Product_ID@ @NotFound@";
			else if(m_C_BPartner_ID <= 0)
				msg = "@C_BPartner_ID@ @NotFound@";
		}
		//	
		if(msg != null) {
			FDialog.info(m_WindowNo, parameterPanel, null, Msg.parseTranslation(Env.getCtx(), msg));
			calculate();
			return;
		}
		//	Load Data
		if(loadDataOrder()){
			north1.setOpen(false);
		}
	}
	
	/**
	 * Get Values from Panel, refresh values
	 * @return void
	 */
	
	private void getPanelValues() {
		//	Organization
		Object value = organizationPick.getValue();
		m_AD_Org_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Sales Region
		value = salesRegionPick.getValue();
		m_C_SalesRegion_ID = ((Integer)(value != null? value: -1)).intValue();

		//	Sales Representative
		value = salesRepSearch.getValue();
		m_SalesRep_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Warehouse
		value = warehouseSearch.getSelectedItem().getValue();
		if (value instanceof Integer)
			m_M_Warehouse_ID = Optional.ofNullable((Integer) value).orElse(0);
		else
			m_M_Warehouse_ID = 0;
		//	Operation Type
		value = operationTypePick.getValue();
		m_OperationType = (String)value;
		//	Document Type
		value = docTypeSearch.getValue();
		if (value instanceof Integer)
			m_C_DocType_ID = (Integer) value;
		else
			m_C_DocType_ID = 0;
		//	Document Type Target
		value = docTypeTargetPick.getValue();
		m_C_DocTypeTarget_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Invoice Rule
		value = invoiceRulePick.getValue();
		m_InvoiceRule = (String) value;
		//	Delivery Rule
		value = deliveryRulePick.getValue();
		m_DeliveryRule = (String) value;
		//	Vehicle Type
		value = vehicleTypePick.getValue();
		m_FTU_VehicleType_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Document Date
		String hourString = "";
		Timestamp hourTime = null;
		if(dateDocField.getValue() != null)
			hourString = dateFormat.format(dateDocField.getValue());
		if(hourString.length() > 0)
			hourTime =  Timestamp.valueOf(hourString);
		m_DateDoc = hourTime;
		//	Shipment Date
		if(shipDateField.getValue() != null)
			hourString = dateFormat.format(shipDateField.getValue());
		if(hourString.length() > 0)
			hourTime = Timestamp.valueOf(hourString);
		m_ShipDate = hourTime;
		//	Entry Ticket
		value = entryTicketPick.getValue();
		m_FTU_EntryTicket_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Shipper
		value = shipperPick.getValue();
		m_M_Shipper_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Driver
		if(driverSearch.getName() != null)
			m_FTU_Driver_ID = Integer.parseInt(driverSearch.getName());
		else
			m_FTU_Driver_ID = 0;
		//	Vehicle
		if(vehicleSearch.getName() != null)
			m_FTU_Vehicle_ID =  Integer.parseInt(vehicleSearch.getName());
		else
			m_FTU_Vehicle_ID = 0;
		/*if(docTypeSearch.getName() != null)
			m_C_DocType_ID = Integer.parseInt(docTypeSearch.getName());
		else
			m_C_DocType_ID = -1;*/
		//	Capacity
		m_LoadCapacity = new BigDecimal((loadCapacityField.getValue() != null ? loadCapacityField.getValue() : 0));
		m_VolumeCapacity = new BigDecimal((volumeCapacityField.getValue() != null ? volumeCapacityField.getValue() : 0));
		//	Product
		value = productSearch.getValue();
		m_M_Product_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Business Partner
		value = bpartnerSearch.getValue();
		m_C_BPartner_ID = ((Integer)(value != null? value: -1)).intValue();
		//	Weight Symbol
		if(m_C_UOM_Weight_ID != 0) {
			MUOM uom = MUOM.get(Env.getCtx(), m_C_UOM_Weight_ID);
			m_UOM_Weight_Symbol = uom.getUOMSymbol();
			weightDiffLabel.setText(Msg.parseTranslation(Env.getCtx(), "@Diferencia de Peso@ (" + m_UOM_Weight_Symbol + ")"));
		}
		//	Volume Symbol
//		if(m_C_UOM_Volume_ID != 0) {
//			MUOM uom = MUOM.get(Env.getCtx(), m_C_UOM_Volume_ID);
//			m_UOM_Volume_Symbol = uom.getUOMSymbol();
//			volumeDiffLabel.setText(Msg.parseTranslation(Env.getCtx(), "@DiffVolume@ (" + m_UOM_Volume_Symbol + ")"));
//		}
	}
	
	/**
	 * Validate data
	 * @return
	 * @return boolean
	 */
	private boolean validData() {
		getPanelValues();
		String msg = null;
		//	Valid Organization
		if(m_AD_Org_ID == 0)
			msg = "@AD_Org_ID@ @NotFound@";
		//	Valid Weight UOM
		else if(m_C_UOM_Weight_ID == 0)
			msg = "@C_UOM_Weight_ID@ @of@ @AD_Client_ID@ @NotFound@";
		//	Valid Volume UOM
//		else if(m_C_UOM_Volume_ID == 0)
//			msg = "@C_UOM_Volume_ID@ @of@ @AD_Client_ID@ @NotFound@";
		//	Valid Operation Type
		else if(m_OperationType == null)
			msg = "@OperationType@ @NotFound@";
		//	Vehicle Type
		else if(m_FTU_VehicleType_ID == 0)
			msg = "@FTU_VehicleType_ID@ @NotFound@";
		//	Difference Capacity
		else if(totalWeight.doubleValue() == 0) {
			msg = "@Weight@ = @0@";
		} else if(totalVolume.doubleValue() == 0) {
			msg = "@Volume@ = @0@";
		} else if(totalWeight.doubleValue() > 0) {
			BigDecimal difference = (BigDecimal) (weightDiffField.getValue() != null
														? weightDiffField.getValue()
																: Env.ZERO);
			if(difference.compareTo(Env.ZERO) < 0)
				msg = "@Weight@ > @LoadCapacity@";
		} else if(totalVolume.doubleValue() > 0) {
			BigDecimal difference = (BigDecimal) (volumeDiffField.getValue() != null
														? volumeDiffField.getValue()
																: Env.ZERO);
			if(difference.compareTo(Env.ZERO) < 0)
				msg = "@Volume@ > @VolumeCapacity@";
		}
		//	Valid Message
		if(msg == null) {
			msg = validStock(stockTable);
		}
		//	
		if(msg != null) {
			FDialog.info(m_WindowNo, parameterPanel, null, Msg.parseTranslation(Env.getCtx(), msg));
			calculate();
			return false;
		}
		return true;
	}
	
	/**
	 * Load Order Data
	 * @return
	 * @return boolean
	 */
	public boolean loadDataOrder() {
		String name = organizationPick.getColumnName();
		Object value = organizationPick.getValue();
		m_AD_Org_ID = ((Integer)(value != null? value: -1)).intValue();
		log.config(name + "=" + value);
		
		name = salesRegionPick.getColumnName();
		value = salesRegionPick.getValue();
		m_C_SalesRegion_ID = ((Integer)(value != null? value: -1)).intValue();
		log.config(name + "=" + value);
		
		name = salesRepSearch.getColumnName();
		value = salesRepSearch.getValue();
		m_SalesRep_ID = ((Integer)(value != null? value: -1)).intValue();
		log.config(name + "=" + value);
		w_orderTable.clear();
		//	Load Data
		Vector<Vector<Object>> data = getOrderData(w_orderTable, m_OperationType);
		Vector<String> columnNames = getOrderColumnNames();

		//  Remove previous listeners
		w_orderTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelP = new ListModelTable(data);
		modelP.addTableModelListener(this);
		w_orderTable.setData(modelP, columnNames);
		setOrderColumnClass(w_orderTable);
		
		w_orderLineTable.clear();
		
		//  Remove previous listeners
		w_orderLineTable.getModel().removeTableModelListener(this);
		//  Set Model Line
		ListModelTable modelLine = new ListModelTable();
		w_orderLineTable.setData(modelLine, columnNames);
		//
		return !data.isEmpty();
	}
	
	/**
	 * Calculate difference
	 * @return void
	 */
	public void calculate() {
		int rows = w_orderLineTable.getRowCount();
		if(rows > 0) {
			m_LoadCapacity = Env.ZERO;
			m_VolumeCapacity = Env.ZERO;
			totalWeight = Env.ZERO;
			totalVolume = Env.ZERO;
			BigDecimal weight = Env.ZERO;
			BigDecimal diffWeight = Env.ZERO;
			BigDecimal volume = Env.ZERO;
			BigDecimal diffVolume = Env.ZERO;
			for (int i = 0; i < rows; i++) {
				if (((Boolean)w_orderLineTable.getValueAt(i, 0)).booleanValue()) {
					//	Weight
					weight = (BigDecimal) (w_orderLineTable.getValueAt(i, OL_WEIGHT) != null
							? w_orderLineTable.getValueAt(i, OL_WEIGHT)
									: Env.ZERO);
					//	Add Weight
					totalWeight = totalWeight.add(weight);
					//	Volume
					volume = (BigDecimal) (w_orderLineTable.getValueAt(i, OL_VOLUME) != null
							? w_orderLineTable.getValueAt(i, OL_VOLUME)
									: Env.ZERO);
					//	Add Volume
					totalVolume = totalVolume.add(volume);
				}
			}
			//	Weight
			if(totalWeight.compareTo(Env.ZERO) > 0) {
				if(loadCapacityField.getValue()!=null)
					m_LoadCapacity = new BigDecimal(loadCapacityField.getValue());
				else 
					m_LoadCapacity = Env.ZERO;
				//	Calculate Difference
				diffWeight = m_LoadCapacity.subtract(totalWeight);
			}
			//	Volume
			if(totalVolume.compareTo(Env.ZERO) > 0) {
				if(volumeCapacityField.getValue()!=null)
					m_VolumeCapacity = new BigDecimal(volumeCapacityField.getValue());
				else
					m_VolumeCapacity = Env.ZERO;
				//	Calculate Difference
				diffVolume = m_VolumeCapacity.subtract(totalVolume);
			}
			//	Set Differences
			weightDiffField.setValue(diffWeight.doubleValue());
			volumeDiffField.setValue(diffVolume.doubleValue());
			orderLineInfo.setText(
					"(" + Msg.parseTranslation(Env.getCtx(), "@C_Order_ID@ @Selected@"
					+ " = " +  m_RowsSelected
					+ ") "
					+ "[@Weight@ (" 
					+ m_UOM_Weight_Symbol
					+ ") = " + totalWeight.doubleValue()
					+ "] | [@Volume@ (") 
					+ m_UOM_Volume_Symbol
					+ ") = " + totalVolume.doubleValue()
					+ "]");
		} else {
			//	Set Difference
			weightDiffField.setValue(Env.ZERO);
			volumeDiffField.setValue(Env.ZERO);
			//	Msg
			orderLineInfo.setText(
					"(" + Msg.parseTranslation(Env.getCtx(), "@C_Order_ID@ @Selected@"
					+ " = " +  m_RowsSelected
					+ ") "
					+ "[@Weight@ (" 
					+ (m_UOM_Weight_Symbol != null? m_UOM_Weight_Symbol: "")
					+ ") = " + Env.ZERO.doubleValue()
					+ "] | [@Volume@ (") 
					+ (m_UOM_Volume_Symbol != null? m_UOM_Volume_Symbol: "")
					+ ") = " + Env.ZERO.doubleValue()
					+ "]");
		}
	}

	@Override
	public void valueChange(ValueChangeEvent evt) {
		String name = evt.getPropertyName();
		Object value = evt.getNewValue();
		log.config(name + " = " + value);
		if(name.equals("C_SalesRegion_ID") || 
				name.equals("SalesRep_ID")) {
			clearData();
		} else if(name.equals("AD_Org_ID")) {
			m_AD_Org_ID = ((Integer)(value != null? value: -1)).intValue();
			KeyNamePair[] data = getDataWarehouse();
			warehouseSearch.removeActionListener(this);
			m_M_Warehouse_ID = loadComboBoxW(warehouseSearch, data);
			warehouseSearch.addEventListener(Events.ON_SELECT, this);
			if (m_OperationType != null)
			{
				data = getDataDocumentType();
				docTypeSearch.removeActionListener(this);
				m_C_DocType_ID = loadComboBoxW(docTypeSearch, data);
				docTypeSearch.addActionListener(this);
			}
			Env.setContext(Env.getCtx(), m_WindowNo, "AD_Org_ID", m_AD_Org_ID);
			docTypeTargetPick.actionRefresh();
			clearData();
		} else if(name.equals("OperationType")) {
			m_OperationType = ((String)(value != null? value: 0));
			Env.setContext(Env.getCtx(), m_WindowNo, "OperationType", m_OperationType);
			if (m_AD_Org_ID >= 0)
			{
				KeyNamePair[] data = getDataDocumentType();
				docTypeSearch.removeActionListener(this);
				m_C_DocType_ID = loadComboBoxW(docTypeSearch, data);
				docTypeSearch.addActionListener(this);
			}
			//	Set Bulk
			m_IsBulk = isBulk();
			//	Set Product
			setIsBulk();
			clearData();
		} else if(name.equals("FTU_VehicleType_ID")) { 
			m_FTU_VehicleType_ID = ((Integer)(value != null? value: 0)).intValue();
			//	Set Capacity
			setFillCapacity();
			calculate();
		} else if(name.equals("FTU_EntryTicket_ID")) {
			m_FTU_EntryTicket_ID = ((Integer)(value != null? value: 0)).intValue();
			KeyNamePair[] data = getDataDriver();
			m_FTU_Driver_ID = loadComboBoxW(driverSearch, data, true);
			//	Vehicle
			data = getVehicleData();
			m_FTU_Vehicle_ID = loadComboBoxW(vehicleSearch, data, true);
			m_FTU_VehicleType_ID = getFTU_VehicleType_ID(m_FTU_EntryTicket_ID);
			vehicleTypePick.setValue(m_FTU_VehicleType_ID);
			vehicleTypePick.setReadWrite(!(m_FTU_EntryTicket_ID > 0));
			//	Set Capacity
			m_M_Shipper_ID = getM_Shipper_ID(m_FTU_EntryTicket_ID);
			shipperPick.setValue(m_M_Shipper_ID);
						
			setFillCapacity();
		}
		calculate();
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		if (arg0.getTarget() == bSearch){
			cmd_search();
		} else if(arg0.getTarget().equals(selectAllButton)) {
			int rows = w_orderLineTable.getRowCount();
			int selected = 0;
			
			for (int i = 0; i < rows; i++) {
				if(!((Boolean)w_orderLineTable.getValueAt(i, SELECT))) {
					//w_orderLineTable.setValueAt(true, i, SELECT);
				}else {
					selected ++;
				}
			}
		for (int x = 0; x < rows; x++) {
			if (selected < rows) {				
					
						w_orderLineTable.setValueAt(true, x, SELECT);
					}else {
						w_orderLineTable.setValueAt(false, x, SELECT);
					}
				}
	}	
		 else if(arg0.getTarget().equals(gLoadOrderButton)) {
			if(validData()) {
				
				FDialog.ask(m_WindowNo, form, Msg.translate(Env.getCtx(), "GenerateOrder"), new Callback<Boolean>() {

					@Override
					public void onCallback(Boolean result) 
					{
						if (result)
						{
							saveData();
						}
						
					}
				});	
				
				/*if (FDialog.ask(m_WindowNo, parameterPanel, null, 
						Msg.translate(Env.getCtx(), "GenerateOrder") + "?")) {
					saveData();
				}*/
			}
		}else if(arg0.getTarget().equals(docTypeSearch)) {
			Object value = docTypeSearch.getValue();
			if (value instanceof Integer)
				m_C_DocType_ID = Optional.ofNullable((Integer) value).orElse(0);
			else
				m_C_DocType_ID = 0;
			clearData();
		} else if(arg0.getTarget().equals(warehouseSearch)) {
			m_M_Warehouse_ID = warehouseSearch.getSelectedIndex();
			clearData();
		}
		
	}
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose


	@Override
	public ADForm getForm() {
		return form;
	}
	
	/**
	 * Load the Combo Box from ArrayList (Web Version)
	 * @param comboSearch
	 * @param data[]
	 * @param mandatory
	 * @return
	 * @return int
	 */
	protected int loadComboBoxW(Listbox comboSearch, KeyNamePair[] data, boolean mandatory) {
		comboSearch.removeAllItems();
		if(!mandatory){
			comboSearch.appendItem("", "0");
			comboSearch.setName(""+count++);
		}
		int m_ID = 0;
		for(KeyNamePair pp : data) {
			comboSearch.appendItem(String.valueOf(pp.getName()),pp.getKey());
			comboSearch.setName(String.valueOf(pp.getKey()));
		}
		//	Set Default
		if (comboSearch.getItemCount() != 0) {
			comboSearch.setSelectedIndex(0);
			m_ID =Integer.parseInt(comboSearch.getName());
		}
		return m_ID;
	}

	/**
	 * Load Combo Box from ArrayList (No Mandatory)
	 * @param comboSearch
	 * @param data[]
	 * @return
	 * @return int
	 */
	protected int loadComboBoxW(Listbox comboSearch, KeyNamePair[] data) {
		return loadComboBoxW(comboSearch, data, false);
	}

	@Override
	public void tableChanged(WTableModelEvent event) {
		boolean isUpdate = (event.getType() == WTableModelEvent.CONTENTS_CHANGED);
		int row = event.getFirstRow();
		int col = event.getColumn();
		//  Not a table update
		if (!isUpdate) {
			calculate();
			return;
		}
		
		boolean isOrder = (event.getModel().equals(w_orderTable.getModel()));
		boolean isOrderLine = (event.getModel().equals(w_orderLineTable.getModel()));
		if(isOrder) {
			if(col == SELECT
					&& m_IsBulk
					&& moreOneSelect(w_orderTable)) {
				FDialog.info(m_WindowNo, parameterPanel, Msg.translate(Env.getCtx(), "IsBulkMaxOne"));
				w_orderTable.setValueAt(false, row, SELECT);
				return;
			}
			//	Load Lines
			if(m_C_UOM_Weight_ID != 0) {
				StringBuffer sql = getQueryLine(w_orderTable, m_OperationType);
				Vector<Vector<Object>> data = getOrderLineData(w_orderTable, sql);
				Vector<String> columnNames = getOrderLineColumnNames();
				
				loadBuffer(w_orderLineTable);
				//  Remove previous listeners
				w_orderLineTable.getModel().removeTableModelListener(this);
				//  Set Model
				ListModelTable modelP = new ListModelTable(data);
				modelP.addTableModelListener(this);
				w_orderLineTable.setData(modelP, columnNames);
				setOrderLineColumnClass(w_orderLineTable);
				setValueFromBuffer(w_orderLineTable);	
			} else {
				FDialog.info(m_WindowNo, parameterPanel, "Error", Msg.parseTranslation(Env.getCtx(), "@C_UOM_ID@ @NotFound@"));
				//loadOrder();
				calculate();
			}
		} else if(isOrderLine) {
			if(col == OL_QTY) {	//	Quantity
				BigDecimal qty = (BigDecimal) w_orderLineTable.getValueAt(row, OL_QTY);
				BigDecimal weight = (BigDecimal) w_orderLineTable.getValueAt(row, OL_WEIGHT);
				BigDecimal volume = (BigDecimal) w_orderLineTable.getValueAt(row, OL_VOLUME);
				BigDecimal qtyOnHand = (BigDecimal) w_orderLineTable.getValueAt(row, OL_QTY_ON_HAND);
				BigDecimal qtyOrdered = (BigDecimal) w_orderLineTable.getValueAt(row, OL_QTY_ORDERED);
				BigDecimal qtyOrderLine = (BigDecimal) w_orderLineTable.getValueAt(row, OL_QTY_IN_TRANSIT);
				BigDecimal qtyDelivered = (BigDecimal) w_orderLineTable.getValueAt(row, OL_QTY_DELIVERED);
				
				//	Get Precision
				KeyNamePair uom = (KeyNamePair) w_orderLineTable.getValueAt(row, OL_UOM);
				KeyNamePair pr = (KeyNamePair) w_orderLineTable.getValueAt(row, OL_PRODUCT);
				StringNamePair dr = (StringNamePair) w_orderLineTable.getValueAt(row, OL_DELIVERY_RULE);
				int p_C_UOM_ID = uom.getKey();
				int p_M_Product_ID = pr.getKey();
				MProduct product = MProduct.get(Env.getCtx(), p_M_Product_ID);
				int precision = MUOM.getPrecision(Env.getCtx(), p_C_UOM_ID);
				BigDecimal unitWeight = product.getWeight();
				BigDecimal unitVolume = product.getVolume();
				String validError = null;
				//	Valid Quantity
				//	Valid Quantity onHand from Swing
				if((dr.getID().equals(X_C_Order.DELIVERYRULE_Availability ) 
						&& m_IsValidateQuantity)
							&& qty.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue()
							>
							qtyOnHand.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue()) {
					//	
					validError = "@Qty@ > @QtyOnHand@";
					//	
				} else if(qty.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue() 
						>
						qtyOrdered
						.subtract(qtyDelivered)
						.subtract(qtyOrderLine)
						.setScale(precision, BigDecimal.ROUND_HALF_UP)
						.doubleValue()) {
					//	
					validError = "@Qty@ > @QtyOrdered@";
					//	
				} else if(qty.compareTo(Env.ZERO) <= 0) {
					validError = "@Qty@ <= 0";
				}
				//	
				if(validError != null) {
					FDialog.warn(m_WindowNo, parameterPanel, null, Msg.parseTranslation(Env.getCtx(), validError));
					qty = qtyOrdered
							.subtract(qtyDelivered)
							.subtract(qtyOrderLine)
							.setScale(precision, BigDecimal.ROUND_HALF_UP);
					//	
					BigDecimal diff = qtyOnHand.subtract(qty).setScale(precision, BigDecimal.ROUND_HALF_UP);
					//	Set Quantity
					if(diff.doubleValue() < 0)
						qty = qty
							.subtract(diff.abs())
							.setScale(precision, BigDecimal.ROUND_HALF_UP);
					//	Remove listener
					w_orderLineTable.getModel().removeTableModelListener(this);
					//	Set quantity
					w_orderLineTable.setValueAt(qty, row, OL_QTY);
					//	Add listener
					w_orderLineTable.getModel().addTableModelListener(this);
				}
				//	Calculate Weight
				weight = qty.multiply(unitWeight).setScale(m_WeightPrecision, BigDecimal.ROUND_HALF_UP);
				w_orderLineTable.setValueAt(weight, row, OL_WEIGHT);
				//	Calculate Volume
				volume = qty.multiply(unitVolume).setScale(m_VolumePrecision, BigDecimal.ROUND_HALF_UP);
				w_orderLineTable.setValueAt(volume, row, OL_VOLUME);
				
				//  Load Stock Product
				stockModel = new ListModelTable();
				stockTable.setData(stockModel, getStockColumnNames());
				setStockColumnClass(stockTable);
				
			} else if(col == SELECT) {
				boolean select = (Boolean) w_orderLineTable.getValueAt(row, col);
				if(select) {
					m_MaxSeqNo += 10;
					w_orderLineTable.setValueAt(m_MaxSeqNo, row, OL_SEQNO);
				}
			} else if(col == OL_SEQNO) {
				int seqNo = (Integer) w_orderLineTable.getValueAt(row, OL_SEQNO);
				if(!exists_seqNo(w_orderLineTable, row, seqNo)) {
					if(seqNo > m_MaxSeqNo) {
						m_MaxSeqNo = seqNo;
					}
				} else {
					FDialog.warn(m_WindowNo, parameterPanel, null, Msg.translate(Env.getCtx(), "SeqNoEx"));
					m_MaxSeqNo += 10;
					w_orderLineTable.setValueAt(m_MaxSeqNo, row, OL_SEQNO);
				}
			}
			//	Load Group by Product
			loadStockWarehouse(w_orderLineTable);
		}
		
		calculate();		
	}
	/**
	 * Refresh Stock Values
	 * @param orderLineTable
	 * @return void
	 */
	private void loadStockWarehouse(IMiniTable orderLineTable) {
		
		log.info("Load StockWarehouse");
		int rows = orderLineTable.getRowCount();
		stockModel = new ListModelTable();
		
		for (int i = 0; i < rows; i++) {
			if (((Boolean)orderLineTable.getValueAt(i, SELECT)).booleanValue()) {
				loadProductsStock(orderLineTable, i, true);
			}
		}
		stockTable.setData(stockModel,getStockColumnNames());
		stockTable.autoSize();
		setStockColumnClass(stockTable);
	}
	
	/**
	 * Verify if exists the product on table
	 * @param p_Product_ID
	 * @param p_M_Warehouse_ID
	 * @return
	 * @return int
	 */
	private int existProductStock(int p_Product_ID, int p_M_Warehouse_ID) {
		for(int i = 0; i < stockModel.getRowCount(); i++) {
			if(((KeyNamePair) stockModel.getValueAt(i, SW_PRODUCT)).getKey() == p_Product_ID
					// Add Support to Warehouse Filter
					&& ((KeyNamePair) stockModel.getValueAt(i, SW_WAREHOUSE)).getKey() == p_M_Warehouse_ID
					) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Load Product Stock
	 * @param orderLineTable
	 * @param row
	 * @param isSelected
	 * @return void
	 */
	private void loadProductsStock(IMiniTable orderLineTable, int row, boolean isSelected) {
		KeyNamePair product = (KeyNamePair) orderLineTable.getValueAt(row, OL_PRODUCT);
		KeyNamePair uom = (KeyNamePair) orderLineTable.getValueAt(row, OL_UOM);
		KeyNamePair warehouse = (KeyNamePair) orderLineTable.getValueAt(row, OL_WAREHOUSE);
		BigDecimal qtyOnHand = (BigDecimal) orderLineTable.getValueAt(row, OL_QTY_ON_HAND);
		BigDecimal qtySet = (BigDecimal) orderLineTable.getValueAt(row, OL_QTY);
		//	
		int pos = existProductStock(product.getKey(), warehouse.getKey());
		//	
		if(pos > -1) {
			BigDecimal qtyInTransitOld = (BigDecimal) stockModel.getValueAt(pos, SW_QTY_IN_TRANSIT);
			BigDecimal qtySetOld = (BigDecimal) stockModel.getValueAt(pos, SW_QTY_SET);
			//	Negate
			if(!isSelected)
				qtySet = qtySet.negate();
			//	
			qtySet = qtySet.add(qtySetOld);
			stockModel.setValueAt(qtyOnHand, pos, SW_QTY_ON_HAND);
			stockModel.setValueAt(qtyInTransitOld, pos, SW_QTY_IN_TRANSIT);
			stockModel.setValueAt(qtySet, pos, SW_QTY_SET);
			stockModel.setValueAt(qtyOnHand
					.subtract(qtyInTransitOld)
					.subtract(qtySet)
					.setScale(2, BigDecimal.ROUND_HALF_UP), pos, SW_QTY_AVAILABLE);
		} else if(isSelected) {
			//	Get Quantity in Transit
			BigDecimal qtyInTransit = getQtyInTransit(product.getKey(), warehouse.getKey());
			Vector<Object> line = new Vector<Object>();
			line.add(product);
			line.add(uom);
			line.add(warehouse);
			line.add(qtyOnHand);
			line.add(qtyInTransit);
			line.add(qtySet);
			line.add(qtyOnHand
					.subtract(qtyInTransit)
					.subtract(qtySet)
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			//	
			stockModel.add(line);
		}
	}
	
	/**
	 * Print Document
	 * @return void
	 */
	private void printDocument() {
		//	Get Document Type
		MDocType m_DocType = MDocType.get(Env.getCtx(), 
				m_FTU_LoadOrder.getC_DocType_ID());
		if(m_DocType == null)
			return;
		//	
		if(m_DocType.getAD_PrintFormat_ID() == 0) {
			String msg = Msg.parseTranslation(Env.getCtx(), 
					"@NoDocPrintFormat@ @AD_Table_ID@=@FTU_LoadOrder@");
			log.warning(msg);
			//	
			FDialog.warn(m_WindowNo, parameterPanel, "Error", msg);
		}
		//	Get Print Format
		MPrintFormat f = MPrintFormat.get(Env.getCtx(), 
				m_DocType.getAD_PrintFormat_ID(), false);
		//	
		if(f != null) {
			MQuery q = new MQuery(I_FTU_LoadOrder.Table_Name);
			q.addRestriction(I_FTU_LoadOrder.Table_Name + "_ID", "=", m_FTU_LoadOrder.getFTU_LoadOrder_ID());
			PrintInfo i = new PrintInfo(Msg.translate(Env.getCtx(), 
					I_FTU_LoadOrder.Table_Name + "_ID"), I_FTU_LoadOrder.Table_ID, m_FTU_LoadOrder.getFTU_LoadOrder_ID());
			//	
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i, null);
			//	Print
			//	Direct Print
			re.print();
			ReportCtl.preview(re);
		}
	}
	/**
	 * Save Data
	 * @return void
	 */
	protected void saveData() {
		
		final String[] success = new String[] { "Error" };
		final TrxRunnable r = new TrxRunnable() {

			public void run(String trxName) {
				success[0] = generateLoadOrder(trxName, w_orderLineTable);
				statusBar.setStatusLine(success[0]);
				
			}
		};
		try
		{
			Trx.run(r);
		} catch (Exception e) {
			FDialog.error(m_WindowNo, parameterPanel, "Error", e.getLocalizedMessage());
			statusBar.setStatusLine("Error: " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		} finally {
			
		}
		//	Print Document
		FDialog.ask(m_WindowNo, parameterPanel,"print.order", Msg.parseTranslation(Env.getCtx(), 
				"@FTU_LoadOrder_ID@ " + m_FTU_LoadOrder.getDocumentNo()), new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean result) 
			{
				if (result)
				{	//	Print?
					printDocument();
				}
				
			}
		});	
		
		//	Clear
		shipperPick.setValue(null);
		driverSearch.removeAllItems();
		vehicleSearch.removeAllItems();
		north1.setOpen(true);
		//	Clear Data
		clearData();
		calculate();
		
	}   //  saveData
	
	
}