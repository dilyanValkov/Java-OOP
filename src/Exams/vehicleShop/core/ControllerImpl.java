package vehicleShop.core;

import vehicleShop.models.shop.Shop;
import vehicleShop.models.shop.ShopImpl;
import vehicleShop.models.tool.Tool;
import vehicleShop.models.tool.ToolImpl;
import vehicleShop.models.vehicle.Vehicle;
import vehicleShop.models.vehicle.VehicleImpl;
import vehicleShop.models.worker.FirstShift;
import vehicleShop.models.worker.SecondShift;
import vehicleShop.models.worker.Worker;
import vehicleShop.repositories.VehicleRepository;
import vehicleShop.repositories.WorkerRepository;

import java.util.List;
import java.util.stream.Collectors;

import static vehicleShop.common.ConstantMessages.*;
import static vehicleShop.common.ExceptionMessages.*;

public class ControllerImpl implements Controller{
    private WorkerRepository workers;
    private VehicleRepository vehicles;
    private Shop shop = new ShopImpl();
    private int reached = 0;

    public ControllerImpl() {
        this.workers = new WorkerRepository();
        this.vehicles = new VehicleRepository();
    }

    @Override
    public String addWorker(String type, String workerName) {
        Worker worker;
        if (type.equals("FirstShift")){
            worker = new FirstShift(workerName);
        }else if (type.equals("SecondShift")){
            worker = new SecondShift(workerName);
        }else {
            throw new IllegalArgumentException(WORKER_TYPE_DOESNT_EXIST);
        }
        workers.add(worker);
        return String.format(ADDED_WORKER,type,workerName);
    }

    @Override
    public String addVehicle(String vehicleName, int strengthRequired) {
        Vehicle vehicle = new VehicleImpl(vehicleName,strengthRequired);
        vehicles.add(vehicle);
        return String.format(SUCCESSFULLY_ADDED_VEHICLE,vehicleName);
    }

    @Override
    public String addToolToWorker(String workerName, int power) {
        Tool tool = new ToolImpl(power);
        Worker worker = this.workers.findByName(workerName);
        if (worker==null){
            throw new IllegalArgumentException(HELPER_DOESNT_EXIST);
        }
        worker.addTool(tool);
        return String.format(SUCCESSFULLY_ADDED_TOOL_TO_WORKER,power,workerName);
    }

    @Override
    public String makingVehicle(String vehicleName) {

        Vehicle vehicle = this.vehicles.findByName(vehicleName);
        List<Worker> selectedWorkers = this.workers.getWorkers().stream()
                                        .filter(worker -> worker.getStrength() > 70)
                                        .collect(Collectors.toList());

        if (selectedWorkers.isEmpty()){
            throw new IllegalArgumentException(NO_WORKER_READY);
        }
        int countBrokenTools = 0;
        for (Worker selectedWorker : selectedWorkers) {
            shop.make(vehicle,selectedWorker);
            countBrokenTools += selectedWorker.getTools().stream()
                    .filter(Tool::isUnfit)
                    .count();
            if (vehicle.reached()){
                break;
            }
        }
        String result;
        if (vehicle.reached()){
            result = "done";
            reached++;
        }else {
            result = "not done";
        }
        return String.format(VEHICLE_DONE + COUNT_BROKEN_INSTRUMENTS,vehicleName,result, countBrokenTools);
    }

    @Override
    public String statistics() {
        StringBuilder sb = new StringBuilder();

        sb.append(reached).append(" vehicles are ready!").append(System.lineSeparator());
        sb.append("Info for workers:");
        for (Worker worker : this.workers.getWorkers()) {
        sb.append(System.lineSeparator());
        sb.append("Name: ").append(worker.getName())
                           .append(", Strength: ").append(worker.getStrength())
                           .append(System.lineSeparator());
        sb.append("Tools: ").append(worker.getTools().stream().filter(tool -> tool.getPower()>0).count())
                            .append(" fit left");
        }
        return String.valueOf(sb).trim();
    }
}
