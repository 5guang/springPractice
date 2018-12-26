package spring2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring2.error.BusinessException;
import spring2.error.EmBusinessError;
import spring2.request.ordermodel.CreateorderModel;
import spring2.response.CommonReturnType;
import spring2.service.OrderService;
import spring2.service.model.OrderModel;
import spring2.service.model.UserModel;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/22 0022 14:51
 */
@RestController
@RequestMapping("api/order")
@CrossOrigin(allowCredentials="true", allowedHeaders="*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @PostMapping(value = "/createorder", consumes = {CONTENT_TYPE_JSON})
    @ResponseBody
    public CommonReturnType createOrder(@RequestBody CreateorderModel createorderModel) throws BusinessException {

        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin.booleanValue()) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登陆，不能下单");
        }
        /**
         * 获取用户登录信息
         */

        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(createorderModel.getPromoId(), userModel.getId(), createorderModel.getItemId(),createorderModel.getAmount());

        return CommonReturnType.create(null);
    }
}
