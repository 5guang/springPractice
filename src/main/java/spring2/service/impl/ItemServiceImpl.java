package spring2.service.impl;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring2.dao.ItemDOMapper;
import spring2.dao.ItemStockDOMapper;
import spring2.dao.SequenceDOMapper;
import spring2.dataobject.ItemDO;
import spring2.dataobject.ItemStockDO;
import spring2.error.BusinessException;
import spring2.error.EmBusinessError;
import spring2.service.ItemService;
import spring2.service.PromoService;
import spring2.service.model.ItemModel;
import spring2.service.model.PromoModel;
import spring2.validator.ValidationResult;
import spring2.validator.ValidatorImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/18 0018 22:05
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) {

       //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            try {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }

        // 转化itemmodel->dataobject
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);
        // 写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        System.out.println(itemModel.getId());

        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        System.out.println(itemStockDO.getItemId());
        System.out.println(itemStockDO.getId());
        itemStockDOMapper.insertSelective(itemStockDO);

        // 返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDoList = itemDOMapper.listItem();
        List<ItemModel> itemModelList = itemDoList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }


        //操作获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());

        // 将dataobject -> model

        ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);

        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());

        if (promoModel != null && promoModel.getStatus().intValue() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        /**
         * 获取活动信息
         */
        return itemModel;
    }

    @Override
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (affectedRow > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void increaseSales(Integer itemId, Integer amount) {
        itemDOMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }
}
