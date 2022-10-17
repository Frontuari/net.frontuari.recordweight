/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Copyright (C) 2020 FRONTUARI <https://www.frontuari.net> and contributors (see README.md file).
 */

package net.frontuari.recordweight.component;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_M_InOut;
import org.compiere.model.I_M_Movement;
import org.compiere.model.MMovement;

import net.frontuari.recordweight.base.FTUEventFactory;
import net.frontuari.recordweight.model.FTUEvents;
import net.frontuari.recordweight.model.MFTULoadOrderLine;

/**
 * Event Factory
 */
public class EventFactory extends FTUEventFactory {

	/**
	 * For initialize class. Register the custom events to build
	 * 
	 * <pre>
	 * protected void initialize() {
	 * 	registerEvent(IEventTopics.DOC_BEFORE_COMPLETE, MTableExample.Table_Name, EPrintPluginInfo.class);
	 * }
	 * </pre>
	 */
	@Override
	protected void initialize() {
		registerEvent(IEventTopics.DOC_AFTER_VOID, I_M_InOut.Table_Name, FTUEvents.class);
		registerEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, I_M_InOut.Table_Name, FTUEvents.class);
		registerEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, I_M_InOut.Table_Name, FTUEvents.class);
		
		registerEvent(IEventTopics.DOC_AFTER_VOID, I_M_Movement.Table_Name, FTUEvents.class);
		registerEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, I_M_Movement.Table_Name, FTUEvents.class);
		registerEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, I_M_Movement.Table_Name, FTUEvents.class);
		
		registerEvent(IEventTopics.DOC_AFTER_VOID, I_C_Invoice.Table_Name, FTUEvents.class);
		registerEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, I_C_Invoice.Table_Name, FTUEvents.class);
		registerEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, I_C_Invoice.Table_Name, FTUEvents.class);
		
		//Add Event After Complete Movement for Update Qty Delivered in Distribution Order Line By Argenis Rodríguez
		registerEvent(IEventTopics.DOC_AFTER_COMPLETE, MMovement.Table_Name, FTUEvents.class);
		//End By Argenis Rodríguez
		registerEvent(IEventTopics.PO_BEFORE_DELETE, MFTULoadOrderLine.Table_Name, FTUEvents.class);
	}

}
