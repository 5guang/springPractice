package spring2.error;

/**
 * 包装器业务异常实现类
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 14:07
 */
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    /**
     * 直接接受EmBusinessError的传参用于构造业务异常
     * @param commonError
     */
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    /**
     * 接受自定义errMsg的方式构造业务异常
     *
     */
    public BusinessException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
