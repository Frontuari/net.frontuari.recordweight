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
 * Copyright (C) 2021 Frontuari and contributors (see README.md file).
 */

package net.frontuari.recordweight.component;

import net.frontuari.recordweight.base.CustomProcessFactory;
import net.frontuari.recordweight.process.ChangePrintedStatus;
import net.frontuari.recordweight.process.ChangePrintedStatusRecordWeight;
import net.frontuari.recordweight.process.EntryTicketChange;
import net.frontuari.recordweight.process.FTUApproveQualityAnalysis;
import net.frontuari.recordweight.process.FTUGenerateFreightCost;
import net.frontuari.recordweight.process.GenerateFromLoadOrder;
import net.frontuari.recordweight.process.GenerateMovementFromAnalysis;
import net.frontuari.recordweight.process.ImportEntryTicket;
import net.frontuari.recordweight.process.LoadOrderGuideGenerate;
import net.frontuari.recordweight.process.ValidateDriver;

/**
 * Process Factory
 */
public class ProcessFactory extends CustomProcessFactory {

	/**
	 * For initialize class. Register the process to build
	 * 
	 * <pre>
	 * protected void initialize() {
	 * 	registerProcess(PPrintPluginInfo.class);
	 * }
	 * </pre>
	 */
	@Override
	protected void initialize() {
		registerProcess(LoadOrderGuideGenerate.class);
		registerProcess(GenerateFromLoadOrder.class);
		registerProcess(EntryTicketChange.class);
		registerProcess(ChangePrintedStatus.class);
		registerProcess(ChangePrintedStatusRecordWeight.class);
		registerProcess(FTUGenerateFreightCost.class);
		registerProcess(GenerateMovementFromAnalysis.class);
		registerProcess(ImportEntryTicket.class);
		registerProcess(ValidateDriver.class);
		registerProcess(FTUApproveQualityAnalysis.class);
	}

}
