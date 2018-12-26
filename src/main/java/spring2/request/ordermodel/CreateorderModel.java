package spring2.request.ordermodel;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/26 0026 20:41
 */
public class CreateorderModel {
    private Integer itemId;

    private Integer amount;

    private Integer promoId = -1;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
