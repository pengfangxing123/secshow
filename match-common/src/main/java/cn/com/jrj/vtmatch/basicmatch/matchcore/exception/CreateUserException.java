package cn.com.jrj.vtmatch.basicmatch.matchcore.exception;


/**
 * 创建账户时的异常
 *
 * @author NL
 */
public class CreateUserException extends RuntimeException {

    private final String userId;

    public CreateUserException(String userId, String msg) {
        super(msg);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
