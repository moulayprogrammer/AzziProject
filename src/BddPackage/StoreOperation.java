package BddPackage;

import Models.Store;

import java.util.ArrayList;

public class StoreOperation extends BDD<Store> {

    @Override
    public boolean insert(Store o) {
        return false;
    }

    @Override
    public boolean update(Store o1, Store o2) {
        return false;
    }

    @Override
    public boolean delete(Store o) {
        return false;
    }

    @Override
    public boolean isExist(Store o) {
        return false;
    }

    @Override
    public ArrayList<Store> getAll() {
        return null;
    }
}
