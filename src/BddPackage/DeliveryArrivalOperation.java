package BddPackage;

import java.util.ArrayList;

public class DeliveryArrivalOperation extends BDD<DeliveryOperation>{

    @Override
    public boolean insert(DeliveryOperation o) {
        return false;
    }

    @Override
    public boolean update(DeliveryOperation o1, DeliveryOperation o2) {
        return false;
    }

    @Override
    public boolean delete(DeliveryOperation o) {
        return false;
    }

    @Override
    public boolean isExist(DeliveryOperation o) {
        return false;
    }

    @Override
    public ArrayList<DeliveryOperation> getAll() {
        return null;
    }
}
