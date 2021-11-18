package com.example.frezzing.Model;

public class Cart {

    private String pid,Pname,price,quantaty,discount;

    public Cart() {
    }

    public Cart(String pid, String pname, String price, String quantaty, String discount) {
        this.pid = pid;
        Pname = pname;
        this.price = price;
        this.quantaty = quantaty;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantaty() {
        return quantaty;
    }

    public void setQuantaty(String quantaty) {
        this.quantaty = quantaty;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
