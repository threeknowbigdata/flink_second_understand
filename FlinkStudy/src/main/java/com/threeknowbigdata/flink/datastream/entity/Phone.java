package com.threeknowbigdata.flink.datastream.entity;

/**
 * 类描述：
 *
 * @ClassName Phone
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/31 下午5:10
 */
public class Phone {
    private int id;
    private String logo;
    private int  price;

    public Phone(int id, String logo, int price) {
        this.id = id;
        this.logo = logo;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
