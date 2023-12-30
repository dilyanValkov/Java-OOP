package robotService.entities.services;
import robotService.entities.robot.Robot;
import robotService.entities.supplements.Supplement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import static robotService.common.ConstantMessages.NOT_ENOUGH_CAPACITY_FOR_ROBOT;
import static robotService.common.ExceptionMessages.SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY;

public abstract class BaseService implements Service{
    private String name;
    private int capacity;

    private Collection<Supplement> supplements;
    private Collection<Robot> robots;

    public BaseService(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
        this.supplements = new ArrayList<>();
        this.robots = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (name==null || name.isBlank()){
            throw new NullPointerException(SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public Collection<Robot> getRobots() {
        return robots;
    }

    @Override
    public Collection<Supplement> getSupplements() {
        return supplements;
    }

    @Override
    public void addRobot(Robot robot) {
        if (this.robots.size()>=capacity){
            throw new IllegalStateException (NOT_ENOUGH_CAPACITY_FOR_ROBOT);
        }
        this.robots.add(robot);
    }

    @Override
    public void removeRobot(Robot robot) {
        this.robots.remove(robot);
    }

    @Override
    public void addSupplement(Supplement supplement) {
        this.supplements.add(supplement);
    }

    @Override
    public void feeding() {
        this.robots.forEach(Robot::eating);
    }

    @Override
    public int sumHardness() {
        return this.supplements.stream().mapToInt(Supplement::getHardness).sum();
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s:\n", this.name, this.getClass().getSimpleName()));
        sb.append("Robots: ");
        if (this.robots.isEmpty()){
            sb.append("none");
        }else {
            String robotsName = this.robots.stream()
                    .map(Robot::getName)
                    .collect(Collectors.joining(" "));
            sb.append(robotsName);
        }
        sb.append(System.lineSeparator());
        sb.append("Supplements: ").append(this.supplements.size());
        sb.append(" ");
        sb.append("Hardness: ").append(sumHardness());
        return sb.toString();
    }
}
