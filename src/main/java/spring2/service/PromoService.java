package spring2.service;

import spring2.service.model.PromoModel;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/22 0022 19:37
 */
public interface PromoService {

    /**
     * 根据itemid获取即将进行的或正在进行的秒杀活动
     * @param itemId
     * @return
     */
    PromoModel getPromoByItemId(Integer itemId);
}
