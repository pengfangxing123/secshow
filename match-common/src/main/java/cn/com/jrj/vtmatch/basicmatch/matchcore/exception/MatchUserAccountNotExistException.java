package cn.com.jrj.vtmatch.basicmatch.matchcore.exception;

/**
 * 有用户，没有账户的情况
 *
 * @author NL
 * @date 2018-10-30 16:26:13
 */
public class MatchUserAccountNotExistException extends RuntimeException {

    private final String userId;

    public MatchUserAccountNotExistException(String userId) {
        super("user account not exist");
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}