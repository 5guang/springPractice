package spring2.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring2.controller.viewobject.ItemVO;
import spring2.dataobject.ItemDO;
import spring2.request.itemmodel.CreateModel;
import spring2.response.CommonReturnType;
import spring2.service.ItemService;
import spring2.service.model.ItemModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/18 0018 22:37
 */
@RestController
@RequestMapping("api/item")
@CrossOrigin(allowCredentials="true", allowedHeaders="*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    /**
     * 创建商品的controller
     * @return
     */
    @PostMapping(value = "/create",consumes = {CONTENT_TYPE_JSON})
    @ResponseBody
    public CommonReturnType createItem(@RequestBody CreateModel createModel) {
        // 封装service请求用处创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(createModel.getName());
        itemModel.setDescription(createModel.getDescription());
        itemModel.setPrice(createModel.getPrice());
        itemModel.setStock(createModel.getStock());
        itemModel.setImgUrl(createModel.getImgUrl());

       ItemModel itemModelForReturn =  itemService.createItem(itemModel);

        ItemVO itemVO = convertVOFromModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);

    }

    @GetMapping(value = "/getItem")
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);

        ItemVO itemVO = convertVOFromModel(itemModel);

        return CommonReturnType.create(itemVO);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public CommonReturnType listItem() {
        List<ItemModel> itemModelList = itemService.listItem();

        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO =  this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);
    }

    private ItemVO convertVOFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        if(itemModel.getPromoModel() != null) {
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
