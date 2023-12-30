package robotService.core;
import robotService.entities.robot.FemaleRobot;
import robotService.entities.robot.MaleRobot;
import robotService.entities.robot.Robot;
import robotService.entities.services.MainService;
import robotService.entities.services.SecondaryService;
import robotService.entities.services.Service;
import robotService.entities.supplements.MetalArmor;
import robotService.entities.supplements.PlasticArmor;
import robotService.entities.supplements.Supplement;
import robotService.repositories.SupplementRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static robotService.common.ConstantMessages.*;
import static robotService.common.ExceptionMessages.*;

public class ControllerImpl implements Controller{
    private SupplementRepository supplements;
    private Collection <Service> services;

    public ControllerImpl() {
        this.supplements = new SupplementRepository();
        this.services = new ArrayList<>();
    }

    @Override
    public String addService(String type, String name) {
        Service service = null;
        if (type.equals("MainService")){
            service = new MainService(name);
        }else if (type.equals("SecondaryService")){
            service = new SecondaryService(name);
        }else {
            throw new NullPointerException(INVALID_SERVICE_TYPE);
        }
        this.services.add(service);
        return String.format(SUCCESSFULLY_ADDED_SERVICE_TYPE,type);
    }

    @Override
    public String addSupplement(String type) {
        Supplement supplement = null;
        if (type.equals("PlasticArmor")){
            supplement = new PlasticArmor();
        }else if (type.equals("MetalArmor")){
            supplement = new MetalArmor();
        }else {
            throw new IllegalArgumentException(INVALID_SUPPLEMENT_TYPE);
        }
        this.supplements.addSupplement(supplement);
        return String.format(SUCCESSFULLY_ADDED_SUPPLEMENT_TYPE,type);
    }

    @Override
    public String supplementForService(String serviceName, String supplementType) {
        Supplement supplement = this.supplements.findFirst(supplementType);
        if (supplement == null){
            throw new IllegalArgumentException(String.format(NO_SUPPLEMENT_FOUND,supplementType));
        }
        Service service = getService(serviceName);
        service.addSupplement(supplement);
        this.supplements.removeSupplement(supplement);
        return String.format(SUCCESSFULLY_ADDED_SUPPLEMENT_IN_SERVICE,supplementType,serviceName);
    }

    @Override
    public String addRobot(String serviceName, String robotType, String robotName, String robotKind, double price) {
        Robot robot;
        if (robotType.equals("MaleRobot")){
            robot = new MaleRobot(robotName,robotKind,price);
        }else if (robotType.equals("FemaleRobot")){
            robot = new FemaleRobot(robotName,robotKind,price);
        }else {
            throw new IllegalArgumentException(INVALID_ROBOT_TYPE);
        }
        Service service = getService(serviceName);
        String output;
        if (!isSuitableService(robotType, service)){
            output = UNSUITABLE_SERVICE;
        }else {
            service.addRobot(robot);
            output = String.format(SUCCESSFULLY_ADDED_ROBOT_IN_SERVICE,robotType,serviceName);
        }
        return output;
    }

    private Service getService(String serviceName) {
        return this.services.stream().filter(service1 ->
                service1.getName().equals(serviceName)).findFirst().get();

    }

    private boolean isSuitableService(String robotType, Service service) {
        String serviceType = service.getClass().getSimpleName();
        if (robotType.equals("MaleRobot") && serviceType.equals("MainService")){
            return true;
        } else if (robotType.equals("FemaleRobot") && serviceType.equals("SecondaryService")){
            return true;
        }
        return false;
    }

    @Override
    public String feedingRobot(String serviceName) {
        Service service = getService(serviceName);
        service.feeding();
        int count = service.getRobots().size();
        return String.format(FEEDING_ROBOT,count);
    }

    @Override
    public String sumOfAll(String serviceName) {
        Service service = getService(serviceName);
        double sumSupplements = service.getSupplements().stream().mapToDouble(Supplement::getPrice).sum();
        double sumRobots = service.getRobots().stream().mapToDouble(Robot::getPrice).sum();
        return String.format(VALUE_SERVICE,serviceName, sumSupplements + sumRobots);
    }

    @Override
    public String getStatistics() {
       return this.services.stream()
                .map(Service::getStatistics)
                .collect(Collectors.joining(System.lineSeparator())).trim();
    }
}
