package vehicleShop.models.vehicle;

import static vehicleShop.common.ExceptionMessages.VEHICLE_NAME_NULL_OR_EMPTY;
import static vehicleShop.common.ExceptionMessages.VEHICLE_STRENGTH_LESS_THAN_ZERO;

public class VehicleImpl implements Vehicle{
    private String name;
    private int strengthRequired;

    public VehicleImpl(String name, int strengthRequired) {
       setName(name);
       setStrengthRequired(strengthRequired);
    }

    protected void setName(String name) {
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException(VEHICLE_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    protected void setStrengthRequired(int strengthRequired) {
        if (strengthRequired < 0 ){
            throw new IllegalArgumentException(VEHICLE_STRENGTH_LESS_THAN_ZERO);
        }
        this.strengthRequired = strengthRequired;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStrengthRequired() {
        return strengthRequired;
    }

    @Override
    public boolean reached() {
        return getStrengthRequired() == 0 ;
    }

    @Override
    public void making() {
        setStrengthRequired(Math.max(0, getStrengthRequired() - 5));
    }
}
