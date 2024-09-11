/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * This program is free software; you can redistribute it and/or modify it           *
 * under the terms version 2 of the GNU General Public License as published          *
 * by the Free Software Foundation. This program is distributed in the hope          *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied        *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                  *
 * See the GNU General Public License for more details.                              *
 * You should have received a copy of the GNU General Public License along           *
 * with this program; if not, write to the Free Software Foundation, Inc.,           *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                            *
 * For the text or an alternative of this public license, you may reach us           *
 * Copyright (C) 2012-2013 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpconsultoresyasociados.com                      *
 *************************************************************************************/
package net.frontuari.recordweight.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.lang.System;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.window.Dialog;
import org.compiere.model.GridTab;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.idempiere.ui.zk.annotation.Form;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.North;
import org.zkoss.zul.South;

import net.frontuari.recordweight.model.MFTURecordWeight;
import net.frontuari.recordweight.model.MFTUWeightScale;
import net.frontuari.recordweight.model.X_FTU_RecordWeight;

/**
 * @author Yamel Senih
 *
 */
@Form(name = "net.frontuari.recordweight.form.WFTUGetWeightUI")
public class WFTUGetWeightUI extends WFTUGetWeight implements java.util.EventListener {
	
	private static final long serialVersionUID = -204755295857442510L;

	/**
	 * *** Constructor de la Clase ***
	 * 
	 * @author Yamel Senih 25/03/2013, 19:08:33
	 * @param gridTab
	 */
	public WFTUGetWeightUI(GridTab gridTab) {
		super(gridTab);
		log.fine("VGetWeightUI()");
		setTitle(Msg.translate(Env.getCtx(), "GetWeightFromScale") + " .. ");

		log.info(getGridTab().toString());

		window = new Window();

		p_WindowNo = getGridTab().getWindowNo();
		
		try {
			loadWeightScale();
			if (!dynInit())
				return;
//			zkInit();
			boolean sys = MSysConfig.getBooleanValue("OPEN_PORT_AUTOMATIC_IN_GETWEIGHT", true,
					Env.getAD_Client_ID(Env.getCtx()));
			if (sys) {
				if (!startService()) {
					Dialog.error(p_WindowNo, "Error", getMessage());
					return;
				}
//				setInitOK(true);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
		}
		AEnv.showWindow(window);

	}

	private Window window;
	/** Window No */
	protected int p_WindowNo;

	/** Logger */
	private CLogger log = CLogger.getCLogger(getClass());
	//
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private Borderlayout mainLayout = new Borderlayout();
	private Grid parameterLayout = GridFactory.newGridLayout();
	private Panel parameterPanel = new Panel();

	/**
	 * Dynamic Init
	 * 
	 * @throws Exception if Lookups cannot be initialized
	 * @return true if initialized
	 */
	@Override
	public boolean dynInit() throws Exception {
		super.dynInit();
		log.config("dynInit()");

		System.setSecurityManager(null);
		zkInit();

		confirmPanel.addActionListener(this);
		window.setTitle(getTitle());
		window.setSizable(true);
		window.setBorder("normal");
		return true;
	}

	/**
	 * Create UI Panel
	 * 
	 * @throws Exception
	 * @return void
	 */
	private void zkInit() throws Exception {
		log.info("jbInit()");
		window.setWidth("600px");
		window.setHeight("155px");
		window.appendChild(mainLayout);
		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");
		mainLayout.setHeight("100%");
		mainLayout.setWidth("99%");
		window.appendChild(mainLayout);

		parameterLayout.setWidth("100%");
		parameterPanel.appendChild(parameterLayout);
		North north = new North();
		north.setStyle("border: none");
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);

		Rows rows = null;
		Row row = null;

		parameterLayout.setWidth("800px");
		rows = parameterLayout.newRows();
		row = rows.newRow();

		//

		//
		lDisplay.setText(Msg.translate(Env.getCtx(), "Weight"));
		fDisplay.setReadonly(true);
		lDisplay.setStyle("font-size:42px");
		fDisplay.setStyle("font-size:42px");
		fDisplay.setWidth("400px");
		fDisplay.setHeight("80px");
		fDisplay.setText("- - - - - - - - - - -  ");
		//

		row.appendChild(lDisplay);
		row.appendChild(fDisplay);

		loadButtons();
		// Add Pane
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		south.appendChild(confirmPanel);
		confirmPanel.addActionListener(this);
//		Panel southPanel = new Panel();
//		Borderlayout southLayout = new Borderlayout();
//    	southPanel.appendChild(southLayout);
//    	southPanel.appendChild(confirmPanel);
//    	//	
//		Panel mainPanel = new Panel();
//		Borderlayout manLayout = new Borderlayout();
//		mainPanel.appendChild(manLayout);
////		mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
////		CompiereColor.setBackground(mainPanel);
//		mainPanel.appendChild(displayPane);
//		mainPanel.appendChild(southPanel);
		//
//		dialog.getContentPane().app(mainPanel);    	
		//
		weight = Env.ZERO;
	}

	/**
	 * Load Options to choice as buttons
	 * 
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013,
	 *         03:54:28
	 * @return void
	 * @throws Exception
	 */
	private void loadButtons() throws Exception {
		log.info("loadButtons()");
		List<MFTUWeightScale> arrayWS = getArrayWeightScale();
		int p_FTU_WeightScale_ID = (int) getGridTab().getValue("FTU_WeightScale_ID");
		if (arrayWS.size() == 0) {
			closeWindow();
		}
		//
		for (int i = 0; i < arrayWS.size(); i++) {
			MFTUWeightScale weightScale = arrayWS.get(i);
			if(weightScale.get_ID() == p_FTU_WeightScale_ID)
			{
				Button aa = new Button(weightScale.getName());
				aa.setLabel(weightScale.getName());
				aa.setName(weightScale.getName());
				aa.setId(String.valueOf(i));
				confirmPanel.addButton(aa);
				aa.addEventListener(Events.ON_CLICK, this);
				aa.setName(String.valueOf(i));
				aa.addActionListener(this);
				log.fine("MFTUWeightScale " + weightScale.toString());
			}
			else
				continue;
		}

	}

//	@Override
//	public void actionPerformed(ActionEvent e) {
//		
//	}

	public void showWindow() {
		window.setVisible(true);
	}

	public void closeWindow() {
		window.dispose();
	}

	/**
	 * Process Value in the child
	 * 
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013,
	 *         15:12:28
	 * @param trxName
	 * @return
	 * @return boolean
	 */
	public boolean processValue(String trxName) {
		int p_FTU_RecordWeight_ID = getGridTab().getRecord_ID();
		MFTURecordWeight recordWeight = new MFTURecordWeight(Env.getCtx(), p_FTU_RecordWeight_ID, trxName);
		BigDecimal weightReaded = weight;
	    BigDecimal grossWeight = recordWeight.getGrossWeight(); 
	    BigDecimal tareWeight = recordWeight.getTareWeight();
	    
	    //	Valid Weight
	    if(weightReaded == null)
	    	weightReaded = Env.ZERO;
	    if(grossWeight == null)
	    	grossWeight = Env.ZERO;
	    if(tareWeight == null)
	    	tareWeight = Env.ZERO;
	    if(recordWeight.getWeightStatus().equals(X_FTU_RecordWeight.WEIGHTSTATUS_Completed))
	    	return false;
	    
	    Timestamp today = new Timestamp(System.currentTimeMillis());
	    
	    if(!recordWeight.isSOTrx()){
	    	if(grossWeight.compareTo(Env.ZERO) == 0){
	    		recordWeight.setGrossWeight(weightReaded);
	    		recordWeight.setInDate(today);
	    		grossWeight = weightReaded;
	    	} else{
	    		recordWeight.setTareWeight(weightReaded);
	    		recordWeight.setOutDate(today);
	    		tareWeight = weightReaded;
	    	}
    		//	Valid Weight Status
	    	if(grossWeight.compareTo(Env.ZERO) == 0)
	    		recordWeight.setWeightStatus(X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForGrossWeight);
	    	else if(tareWeight.compareTo(Env.ZERO) == 0)
	    		recordWeight.setWeightStatus(X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForTareWeight);
	    	else
	    		recordWeight.setWeightStatus(X_FTU_RecordWeight.WEIGHTSTATUS_Completed);
	    } else{
	    	if(tareWeight.compareTo(Env.ZERO) == 0){
	    		recordWeight.setTareWeight(weightReaded);
	    		recordWeight.setInDate(today);
	    		tareWeight = weightReaded;
	    	} else{
	    		recordWeight.setGrossWeight(weightReaded);
	    		recordWeight.setOutDate(today);
	    		grossWeight = weightReaded;
	    	}
    		//	Valid Weight Status
	    	if(tareWeight.compareTo(Env.ZERO) == 0)
	    		recordWeight.setWeightStatus(X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForTareWeight);
	    	else if(grossWeight.compareTo(Env.ZERO) == 0)
	    		recordWeight.setWeightStatus(X_FTU_RecordWeight.WEIGHTSTATUS_WaitingForGrossWeight);
	    	else
	    		recordWeight.setWeightStatus(X_FTU_RecordWeight.WEIGHTSTATUS_Completed);
	    }
	    //	Calculate Net Weight
	    recordWeight.setNetWeight(grossWeight.subtract(tareWeight));
	    //	Save
	    return recordWeight.save();
	}

	@Override
	public void onEvent(Event e) throws Exception {
		log.info("actionPerformed(ActionEvent e) " + e);

		if (e.getTarget().getId().equals(ConfirmPanel.A_OK)) {
			log.fine("Action Comand OK");
			try {
				Trx.run(new TrxRunnable() {
					public void run(String trxName) {
						if (save(trxName)) {
							log.fine("save(" + trxName + ")");
							processValue(trxName);
							window.dispose();
						} else
							Dialog.error(p_WindowNo, "Error", getMessage());
					}
				});
			} catch (Exception ex) {
				Dialog.error(p_WindowNo, "Error", ex.getLocalizedMessage());
			}
		}
		// Cancel
		else if (e.getTarget().getId().equals(ConfirmPanel.A_CANCEL)) {
			log.fine("Action Comand CANCEL");
			window.dispose();
		}
		// Serial Port Configuration
		else {
			log.fine("Action Comand Any");
//			Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			setCurrentWeightScale(Integer.parseInt(e.getTarget().getId()));
			stopService();
			boolean ok = startService();
//			Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
			if (!ok)
				Dialog.error(p_WindowNo, "Error", getMessage());
		}
		getGridTab().dataRefresh();
	}
	
	@Override
	public Object getWindow() {
		return window;
	}
}
