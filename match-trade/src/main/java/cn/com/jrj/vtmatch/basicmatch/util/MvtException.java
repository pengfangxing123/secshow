package cn.com.jrj.vtmatch.basicmatch.util;

public enum MvtException {
	UNIDENTIFIED_ERROR(700000, "现在交易的人太多，请您稍后再试"),
	/**
     * 爱投顾其他业务 accessToken续接走的是此错误码
     */
	ACCESSTOKEN_ERROR(-401, "accessToken failure"),
	NO_FUND_ERROR(-400, "暂不支持此基金交易"),
	INPUT_PARAMS_ERROR(700002, "入参有误！"),
	SYSTEM_REPAIR_ERROR(700005, "系统正在维护中！"),
    DATE_FOMAT_ERROR(700006, "日期格式化失败！"),
    /**
     * 委托价超出涨跌幅范围！
     */
    RESTRAINT_OVER_BORAD_ERROR(701003, "输入价格超过了涨跌停价"),
    ENTRUST_ERROR(701008, "委托失败！"),
    SOLD_OUT_EXCEPTION(701009, "系统正在清算数据，请在17:30后进行委托！！"),

    RESTRAINT_MAX_AMOUNT_ERROR(701010, "获取最大数量失败！"),
    RESTRAINT_ENTRUST_AMOUNT_ERROR(701011, "委托数量必须为100的整数倍！"),
    RESTRAINT_ENTRUST_BUY_MAX_AMOUNT_ERROR(701001, "可用资金不足"),
    RESTRAINT_ENTRUST_SELL_AMOUNT_ERROR(701021, "委托卖出时,零股需一次性卖出！"),
    /**
     * 委托撤单失败！
     */
    ORDER_CANCEL_ERROR(701007, "撤单失败，请重试"),

    QUERY_USERFUNDS_ERROR(701017, "查询账户信息失败！"),
    QUERY_CONCLUDELIST_ERROR(701018, "查询成交记录失败！"),
    /**
     * 可撤单的委托查询失败！
     */
    CANCEL_ERROR(701016, "查不到啊"),
    /**
     * 持仓查询失败！
     */
    POSITION_ERROR(701012, "查不到啊"),
    POSITION_COUNT_ERROR(701013, "持仓总数查询失败！"),
    OTC_ERROR(701014, "行情查询失败！"),
    /**
     * //当前查询位置有误！
     */
    POSITION_STR_ERROR(702001, "查不到啊"),
    /**
     * //每页条数有误！
     */
    ROW_ERROR(702002, "查不到啊"),
    /**
     * //比赛已结束！
     */
    MATCH_APPLY_ERROR(702003, "比赛已结束"),
    QUERY_USERCENTER_ERROR(701019, "用户信息有误！"),
    QUERY_NOTJOIN_ERROR(701020, "用户没有参加比赛！"),
    STOCK_TYPE_ERROR(701022, "模拟炒股只支持A股交易！"),
    SINGLE_THREE_ERROR(701023, "单股持仓30%以内才能参赛哦！"),
    QUERY_NOTBINDPHONE_ERROR(701024, "用户没有绑定手机号！");
	private int errCode;
    private String errMsg;
    
    public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private MvtException(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    

}
