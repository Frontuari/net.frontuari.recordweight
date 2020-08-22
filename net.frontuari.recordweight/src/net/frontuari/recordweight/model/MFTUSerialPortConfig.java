package net.frontuari.recordweight.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFTUSerialPortConfig extends X_FTU_SerialPortConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1232646928116592833L;

	public MFTUSerialPortConfig(Properties ctx, int FTU_SerialPortConfig_ID, String trxName) {
		super(ctx, FTU_SerialPortConfig_ID, trxName);
	}

	public MFTUSerialPortConfig(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

}
