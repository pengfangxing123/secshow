package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

/**
 * 基础比赛交易 service.
 */
public interface BasicTradeService {

    /**
     * 买入
     *
     * @return boolean
     */
    boolean buy();

    /**
     * 卖出
     *
     * @return boolean
     */
    boolean sell();

    /**
     * 查询
     *
     * @return boolean
     */
    void query();

    /**
     * 撤单
     *
     * @return boolean
     */
    boolean cancellation ();

}
