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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Textbox;
import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.grid.ICreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MWindow;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import net.frontuari.recordweight.model.MFTUScreenConfig;
import net.frontuari.recordweight.model.MFTUSerialPortConfig;
import net.frontuari.recordweight.model.MFTUWeightScale;
import net.frontuari.recordweight.model.MFTUWeightScaleRole;

/**
 * @author Yamel Senih
 *
 */
public class WFTUGetWeight implements ICreateFrom {
	
	/**
	 * *** Constructor de la Clase ***
	 * @author Yamel Senih 25/03/2013, 19:01:45
	 * @param gridTab
	 */
	public WFTUGetWeight(GridTab gridTab) {
		this.gridTab = gridTab;
		log.info(gridTab.toString());
	}	//	GetWeight

	/**	Logger			*/
	protected CLogger log = CLogger.getCLogger(getClass());
	private String title;

	private GridTab gridTab;
	private boolean initOK = false;
	
	protected boolean isSOTrx = false;
	
	
	private InputStream 				i_Stream 				= null;
	private boolean 					started 				= false;
	private boolean						read					= false;
	private List<MFTUWeightScale> 		arrayWS					= null;
	private MFTUSerialPortConfig 		currentSerialPortConfig = null;
	private MFTUScreenConfig 			currentScreenConfig		= null;
	private MFTUWeightScale 			currentWeightScale		= null;
	private StringBuffer				m_StrReaded				= new StringBuffer();
	private StringBuffer				m_AsciiReaded			= new StringBuffer();
	/**	Label Display				*/
	public Label 			lDisplay 	= new Label();
	/**	Display						*/
	public Textbox 		fDisplay 	= new Textbox();
	/**	Weight Result				*/
	public BigDecimal 		weight		= Env.ZERO;
	/**	Message						*/
	public String			message 	= null;
	
	public boolean dynInit() throws Exception {
		log.config("");
		setTitle("Record Weight");
		return true;
	}
	
	
	public boolean save(String trxName) {
		log.fine("save(String)");
		processStr();
		return true;
	}
	
	/**
	 * Open the port and set Listeners
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 28/03/2013, 03:50:44
	 * @return boolean
	 */
	protected boolean startService() {
		log.fine("startService()");
		//	verify if exists configuration
		if(currentSerialPortConfig == null){
			message = Msg.translate(Env.getCtx(), "@PortNotConfiguredForUser@");
			return false;
		}
		started = true;
		readPort();
		//	
		return started;
	}

	/**
	 * Get Operation Message
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013, 02:01:26
	 * @return
	 * @return String
	 */
	protected String getMessage(){
		return message;
	}
	
	/**
	 * Load List Weight Scale
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013, 03:37:06
	 * @return void
	 */
	protected void loadWeightScale(){
		log.fine("loadSerialPortConfig()");
		//	User
		int m_AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
		arrayWS = MFTUWeightScaleRole.getWeightScaleOfRole(Env.getCtx(), m_AD_Role_ID, null); 
		int p_FTU_WeightScale_ID = (int) getGridTab().getValue("FTU_WeightScale_ID");
		//	Set Current Serial Port Configuration
		if(arrayWS.size() != 0){
			for(MFTUWeightScale wsr : arrayWS)
			{
				if(wsr.get_ID()==p_FTU_WeightScale_ID)
				{
					currentWeightScale = wsr;
					currentSerialPortConfig = wsr.getSerialPortConfig();
					wsr.getScreenConfig();
				}
				else
					continue;
			}
		}
	}
	
	/**
	 * Set Current Weight Scale
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013, 03:38:20
	 * @param index
	 * @return void
	 */
	protected void setCurrentWeightScale(int index){
		currentWeightScale = arrayWS.get(index);
		currentSerialPortConfig = arrayWS.get(index).getSerialPortConfig();
		currentScreenConfig = arrayWS.get(index).getScreenConfig();
	}
	
	/**
	 * Get Array of Serial Port Configuration
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013, 02:10:47
	 * @return
	 * @return List<MFTUWeightScale>
	 */
	protected List<MFTUWeightScale> getArrayWeightScale(){
		return arrayWS;
	}
	
	/**
	 * Get Current Serial Port Configuration
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 29/03/2013, 02:11:58
	 * @return
	 * @return MFTUWeightScale
	 */
	protected MFTUWeightScale getCurrentWeightScale(){
		return currentWeightScale;
	}
	/*
	public void serialEvent(SerialPortEvent ev) {
		if(ev.getEventType() == SerialPortEvent.DATA_AVAILABLE){
			//	Read Port
			log.info("SerialPortEvent.DATA_AVAILABLE");
			readPort();
		}
	}
	*/
	/**
	 * Read the port and set an Display field
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 28/03/2013, 03:47:25
	 * @return void
	 */
	private void readPort(){
		log.fine("readPort()");
		processStr();
	}	//	readPort
	
	/**
	 * Stop Service and close port
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 28/03/2013, 03:55:32
	 * @return boolean
	 */
	protected boolean stopService() {
		log.fine("stopService()");
		if(started){
			started = false;
		}
		return !started;
	}	//	stopService
	
	/**
	 * Process Str and return the getter value
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 28/03/2013, 04:52:51
	 * @return
	 * @return String
	 */
	protected boolean processStr() {
		String sql = "SELECT Value FROM FTU_Weight WHERE FTU_WeightScale_ID = ?";
		BigDecimal value = DB.getSQLValueBD(null, sql, currentWeightScale.getFTU_WeightScale_ID());
		if(value == null)
			value = Env.ZERO;
		if(value != null){
			weight = value;
			fDisplay.setText(value.toString());
			return true;
		}else{
//			message = Msg.translate(Env.getCtx(), "IncompleteStr");
			log.fine("message=" + message);
			return false;
		}
	}	//	processStr

	@Override
	public Object getWindow() {
		return null;
	}
	

	public GridTab getGridTab()
	{
		return gridTab;
	}

	public void setInitOK(boolean initOK)
	{
		this.initOK = initOK;
	}
	
	@Override
	public boolean isInitOK() {
		return initOK;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public void showWindow() {
		
	}


	@Override
	public void closeWindow() {
		
	}
}
