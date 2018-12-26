package spring2.service;

import spring2.error.BusinessException;
import spring2.service.model.OrderModel;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/22 0022 13:47
 */
public interface OrderService {
    /**
     * 使用1、前端通过url上传过来秒杀活动id，然后下单接口内校验id是否属于对应商品且活动已开始开始
     * 2、直接在下单接口内判断对应商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单
     * @param userId
     * @param itemId
     * @param promoId
     * @param amount
     * @return
     * @throws BusinessException
     */
    OrderModel createOrder(Integer promoId, Integer userId, Integer itemId, Integer amount) throws BusinessException;
}
