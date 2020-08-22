package net.frontuari.recordweight.util;

import org.compiere.util.NamePair;

public class StringNamePair extends NamePair {

	/**
	 * 
	 * *** Constructor ***
	 * @param key
	 * @param name
	 */
	public StringNamePair(String key, String name) {
		super(name);
		m_Key = key;
	}
	
	/**	Key					*/
	private String m_Key = null;

	/**
	 * Se tKey
	 * @param key
	 * @return void
	 */
	public void setKey(String key) {
		m_Key = key;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6329387905339792217L;

	@Override
	public String getID() {
		return m_Key;
	}

}
