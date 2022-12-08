package Models;

import java.time.LocalDateTime;

public class CashOut {

    private int id;
    private int idClient;
    private int idInvoice;
    private LocalDateTime date;
    private double out;
    private String raison;

    public CashOut() {
    }

    public CashOut(int id, int idClient, int idInvoice, LocalDateTime date, double out, String raison) {
        this.id = id;
        this.idClient = idClient;
        this.idInvoice = idInvoice;
        this.date = date;
        this.out = out;
        this.raison = raison;
    }

    public CashOut(int idClient, int idInvoice, LocalDateTime date, double out, String raison) {
        this.idClient = idClient;
        this.idInvoice = idInvoice;
        this.date = date;
        this.out = out;
        this.raison = raison;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }
}
