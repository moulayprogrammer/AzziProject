package BddPackage;

import Models.Delivery;

import java.util.ArrayList;

public class DeliveryOperation extends BDD<Delivery> {

    @Override
    public boolean insert(Delivery o) {
        return false;
    }

    @Override
    public boolean update(Delivery o1, Delivery o2) {
        return false;
    }

    @Override
    public boolean delete(Delivery o) {
        return false;
    }

    @Override
    public boolean isExist(Delivery o) {
        return false;
    }

    @Override
    public ArrayList<Delivery> getAll() {
        return null;
    }
}
