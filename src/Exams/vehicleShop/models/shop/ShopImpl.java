package vehicleShop.models.shop;
import vehicleShop.models.tool.Tool;
import vehicleShop.models.vehicle.Vehicle;
import vehicleShop.models.worker.Worker;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class ShopImpl implements Shop{
    @Override
    public void make(Vehicle vehicle, Worker worker) {
        Collection<Tool> tools = worker.getTools();
        Deque<Tool> toolDeque = new ArrayDeque<>(tools);

        while (worker.canWork() || !toolDeque.isEmpty()) {
            Tool currentTool = toolDeque.pop();
            while (!currentTool.isUnfit()) {
                worker.working();
                vehicle.making();
                currentTool.decreasesPower();
                if (vehicle.reached()) {
                    return;
                }
            }
        }
    }
}
