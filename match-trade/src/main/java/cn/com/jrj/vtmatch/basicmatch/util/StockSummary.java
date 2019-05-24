/**
 * 
 */
package cn.com.jrj.vtmatch.basicmatch.util;

import java.math.BigDecimal;

/**  
 * 股票行情
 * @author yuanlong.wang 2013-1-17 
 *  
 */

public class StockSummary {
	/**
	 * 股票ID
	 */
	private String id;
	/**
	 * 股票代码
	 */
	private String code;
	/**
	 * 股票名称
	 */
	private String name;
	/**
	 * 昨收价
	 */
	private BigDecimal lcp;
	/**
	 * 当前价
	 */
	private BigDecimal np;
	/**
	 * 当日高价
	 */
	private BigDecimal hp;
	/**
	 * 当日低价
	 */
	private BigDecimal lp;
	/**
	 * 涨跌额
	 */
	private BigDecimal hlp;
	/**
	 * 涨跌幅
	 */
	private BigDecimal pl;
	/**
	 * 股票类型
	 */
	private String type;
	
	/**
	 * 
	 */
	public StockSummary() {
		// TODO Auto-generated constructor stub
	}
	
	public StockSummary(String id,String code, String name, BigDecimal lcp,
			BigDecimal np, BigDecimal hlp, BigDecimal pl,String type) {
		super();
		this.id=id;
		this.code = code;
		this.name = name;
		this.lcp = lcp;
		this.np = np;
		this.hlp = hlp;
		this.pl = pl;
		this.type=type;
	}
	
	public StockSummary(String id,String code, String name, BigDecimal lcp,
			BigDecimal np, BigDecimal hlp, BigDecimal pl) {
		super();
		this.id=id;
		this.code = code;
		this.name = name;
		this.lcp = lcp;
		this.np = np;
		this.hlp = hlp;
		this.pl = pl;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getLcp() {
		return lcp;
	}
	public void setLcp(BigDecimal lcp) {
		this.lcp = lcp;
	}
	public BigDecimal getNp() {
		return np;
	}
	public void setNp(BigDecimal np) {
		this.np = np;
	}
	public BigDecimal getHlp() {
		return hlp;
	}
	public void setHlp(BigDecimal hlp) {
		this.hlp = hlp;
	}
	public BigDecimal getPl() {
		return pl;
	}
	public void setPl(BigDecimal pl) {
		this.pl = pl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getHp() {
		return hp;
	}

	public void setHp(BigDecimal hp) {
		this.hp = hp;
	}

	public BigDecimal getLp() {
		return lp;
	}

	public void setLp(BigDecimal lp) {
		this.lp = lp;
	}
	
	
}
