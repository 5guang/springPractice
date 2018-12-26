package spring2.error;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 14:00
 */
public interface CommonError {
    int getErrorCode();
    String getErrorMsg();
    CommonError setErrMsg(String errMsg);
}
