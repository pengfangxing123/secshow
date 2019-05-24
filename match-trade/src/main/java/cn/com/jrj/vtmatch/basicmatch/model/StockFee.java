package cn.com.jrj.vtmatch.basicmatch.model;

import lombok.Data;

@Data
public class StockFee {
	/**
	 * 佣金比率
	 */
	private double cfee = 0.00025;
	/**
	 * 最低佣金
	 */
	private double cfeeMin = 5;
	
	/**
	 * 过户费
	 */
	private double tfee = 0.00002;
	
	/**
	 * 最低过户费
	 */
	private double tfeeMin = 1;
	
	/**
	 * 税率
	 */
	private double stax = 0.0001;

}
