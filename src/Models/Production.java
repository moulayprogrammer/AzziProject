package Models;

import java.time.LocalDate;

public class Production {

    private int id;
    private int idProduct;
    private LocalDate date;
    private int qteProduct;
    private Double price;

    public Production() {
    }

    public Production(int id, int idProduct, LocalDate date, int qteProduct, Double price) {
        this.id = id;
        this.idProduct = idProduct;
        this.date = date;
        this.qteProduct = qteProduct;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQteProduct() {
        return qteProduct;
    }

    public void setQteProduct(int qteProduct) {
        this.qteProduct = qteProduct;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
