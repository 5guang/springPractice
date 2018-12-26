package spring2.request.itemmodel;

import java.math.BigDecimal;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/26 0026 20:27
 */
public class CreateModel {

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
