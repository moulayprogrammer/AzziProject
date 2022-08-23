package Models;

public class ComponentDamage {

    private int idDamage;
    private int idComponent;
    private int idReference;
    private double qte;

    public ComponentDamage() {
    }

    public ComponentDamage(int idDamage, int idComponent, int idReference, double qte) {
        this.idDamage = idDamage;
        this.idComponent = idComponent;
        this.idReference = idReference;
        this.qte = qte;
    }

    public int getIdDamage() {
        return idDamage;
    }

    public void setIdDamage(int idDamage) {
        this.idDamage = idDamage;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public int getIdReference() {
        return idReference;
    }

    public void setIdReference(int idReference) {
        this.idReference = idReference;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) {
        this.qte = qte;
    }
}
