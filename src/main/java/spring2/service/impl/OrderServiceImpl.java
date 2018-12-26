package spring2.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring2.dao.OrderDOMapper;
import spring2.dao.SequenceDOMapper;
import spring2.dataobject.ItemStockDO;
import spring2.dataobject.OrderDO;
import spring2.dataobject.SequenceDO;
import spring2.error.BusinessException;
import spring2.error.EmBusinessError;
import spring2.service.ItemService;
import spring2.service.OrderService;
import spring2.service.UserService;
import spring2.service.model.ItemModel;
import spring2.service.model.OrderModel;
import spring2.service.model.UserModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/22 0022 13:48
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer promoId, Integer userId, Integer itemId, Integer amount) throws BusinessException {

        ItemModel itemModel = itemService.getItemById(itemId);
        /**
         * 1、校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
         */
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }

        if (amount <= 0 || amount >99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }

        if (promoId != null) {
            if (itemModel.getPromoModel().getId() != promoId.intValue()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemModel.getPromoModel().getStatus() != 2) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }

        /**
         * 2、落单减库存，支付减库存
         */
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        /**
         * 3、订单入库
         */

        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        /**
         * 生成交易流水号，订单号
         */
        orderModel.setId(generateOrderNo());
        OrderDO orderDO = this.concertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        itemService.increaseSales(itemId,amount);
        /**
         * 返回前端
         */

        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo() {
        // 订单号16位
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replaceAll("-", "");
        stringBuilder.append(nowDate);

        // 中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6-sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        // 最后两位为分库分表位,暂时写死
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDO concertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }

        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
