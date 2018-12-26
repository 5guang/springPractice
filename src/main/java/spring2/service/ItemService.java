package spring2.service;

import spring2.error.BusinessException;
import spring2.service.model.ItemModel;
import java.util.List;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/18 0018 22:02
 */
public interface ItemService {

    /**
     * 创建商品
     */
    ItemModel createItem(ItemModel itemModel);

    /**
     * 商品列表浏览
     */
    List<ItemModel> listItem();

    /**
     * 商品详情浏览
     */
    ItemModel getItemById(Integer id);

    /**
     * 庫存扣減
     * @param itemId
     * @param amount
     * @return
     * @throws BusinessException
     */
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    /**
     * 商品銷量增加
     * @param itemId
     * @param amount
     * @throws BusinessException
     */
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
