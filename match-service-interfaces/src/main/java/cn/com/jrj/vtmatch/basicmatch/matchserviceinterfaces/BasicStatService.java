package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

/**
 * 基础比赛统计 service.
 *
 * @author lei.ning
 */
public interface BasicStatService {
    /**
     * 持仓查询
     *
     * @return
     */
    void queryPosition();

    /**
     * 账户收益及排名查询
     *
     * @return
     */
    void accountIncome();

    /**
     * 团队收益及排名查询
     *
     * @return
     */
    void teamIncome();

    /**
     * 团队成员收益及排名查询
     *
     * @return
     */
    void teamMemberIncome();

    /**
     * 比赛收益及排名查询
     *
     * @return
     */
    void matchIncome();

}
