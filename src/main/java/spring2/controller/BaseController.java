package spring2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import spring2.error.BusinessException;
import spring2.error.EmBusinessError;
import spring2.response.CommonReturnType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 14:38
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    /**
     * 定义exceptionhandler解决未被controller层吸收的exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String , Object> responseData = new HashMap<String, Object>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrorCode());
            responseData.put("errMsg", businessException.getErrorMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(responseData, "fail");

    }
}
