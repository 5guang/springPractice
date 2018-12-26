package spring2.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/17 0017 21:53
 */
public class ValidationResult {

    private boolean hasErrors = false;

    /**
     * 存放错误信息
     */
    private Map<String , String> errorMsgMap = new HashMap<String, String>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    public String getErrMsg() {
        return StringUtils.join(errorMsgMap.values().toArray(), ",");
    }
}
