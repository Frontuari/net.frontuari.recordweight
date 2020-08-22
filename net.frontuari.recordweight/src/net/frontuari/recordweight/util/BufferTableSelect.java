package net.frontuari.recordweight.util;

import java.math.BigDecimal;

import org.compiere.util.Env;

public class BufferTableSelect {
	/**
	 * 
	 * *** Constructor ***
	 * @param m_Record_ID
	 * @param qty
	 * @param seqNo
	 */
	public BufferTableSelect(int m_Record_ID, BigDecimal qty, Integer seqNo){
		this.m_Record_ID = m_Record_ID;
		this.qty = qty;
		this.seqNo = seqNo;
	}
	
	/**
	 * Set Record Identifier
	 * @param m_Record_ID
	 * @return void
	 */
	public void setRecord_ID(int m_Record_ID){
		this.m_Record_ID = m_Record_ID;
	}
	
	/**
	 * Get Record Identifier
	 * @return
	 * @return int
	 */
	public int getRecord_ID(){
		return this.m_Record_ID;
	}
	
	/**
	 * Set Quantity
	 * @param qty
	 * @return void
	 */
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	/**
	 * Get Quantity
	 * @return
	 * @return BigDecimal
	 */
	public BigDecimal getQty(){
		return this.qty;
	}
	
	/**
	 * Set Sequence
	 * @param seqNo
	 * @return void
	 */
	public void setSeqNo(Integer seqNo){
		this.seqNo = seqNo;
	}
	
	/**
	 * Get Sequence
	 * @return
	 * @return Integer
	 */
	public Integer getSeqNo(){
		return this.seqNo;
	}
	
	public String toString(){
		return "m_Record_ID = " + m_Record_ID 
				+ " qty = " + qty 
				+ "seqNo = " + seqNo;
	}
	
	/**	Record ID	*/
	private int m_Record_ID = 0;
	/**	Quantity	*/
	private BigDecimal qty = Env.ZERO;
	/**	Sequence	*/
	private Integer seqNo = 0;
}
