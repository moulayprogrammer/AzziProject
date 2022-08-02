package Models;

import java.time.LocalDate;

public class Damage {

    private int id;
    private int idProduct;
    private LocalDate date;
    private int qte;
    private String raison;

    public Damage() {
    }

    public Damage(int id, int idProduct, LocalDate date, int qte, String raison) {
        this.id = id;
        this.idProduct = idProduct;
        this.date = date;
        this.qte = qte;
        this.raison = raison;
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

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }
}
