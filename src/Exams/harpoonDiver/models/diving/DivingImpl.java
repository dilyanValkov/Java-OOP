package harpoonDiver.models.diving;
import harpoonDiver.models.diver.Diver;
import harpoonDiver.models.divingSite.DivingSite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DivingImpl implements Diving {
    @Override
    public void searching(DivingSite divingSite, Collection<Diver> divers) {
        for (Diver diver : divers) {
            while (diver.canDive()) {
                Collection<String> seaCreatures = divingSite.getSeaCreatures();
                List<String> creatures = new ArrayList<>(seaCreatures);
                Collection<String> diverCatchRepo = diver.getSeaCatch().getSeaCreatures();
                for (String creature : creatures) {
                    diver.shoot();
                    diverCatchRepo.add(creature);
                    seaCreatures.remove(creature);
                    if (!diver.canDive()) {
                        break;
                    }
                }
                if (seaCreatures.isEmpty()) {
                    break;
                }
            }
        }
    }
}


