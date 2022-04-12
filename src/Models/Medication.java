package Models;

public class Medication {

    private int id;
    private String name;
    private String reference;
    private int limitQte;

    public Medication() {
    }

    public Medication(int id, String name, String reference, int limitQte) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.limitQte = limitQte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getLimitQte() {
        return limitQte;
    }

    public void setLimitQte(int limitQte) {
        this.limitQte = limitQte;
    }
}
