package harpoonDiver.core;
import harpoonDiver.models.diver.DeepWaterDiver;
import harpoonDiver.models.diver.Diver;
import harpoonDiver.models.diver.OpenWaterDiver;
import harpoonDiver.models.diver.WreckDiver;
import harpoonDiver.models.diving.Diving;
import harpoonDiver.models.diving.DivingImpl;
import harpoonDiver.models.divingSite.DivingSite;
import harpoonDiver.models.divingSite.DivingSiteImpl;
import harpoonDiver.repositories.DiverRepository;
import harpoonDiver.repositories.DivingSiteRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static harpoonDiver.common.ConstantMessages.*;
import static harpoonDiver.common.ExceptionMessages.*;

public class ControllerImpl implements Controller{

    private DiverRepository diverRepository;
    private DivingSiteRepository divingSiteRepository;
    private int divingCount = 0;

    public ControllerImpl() {
        this.diverRepository = new DiverRepository();
        this.divingSiteRepository = new DivingSiteRepository();
    }

    @Override
    public String addDiver(String kind, String diverName) {
        Diver diver;
        switch (kind){
            case "OpenWaterDiver":
            diver = new OpenWaterDiver(diverName);
                break;
            case "DeepWaterDiver":
            diver = new DeepWaterDiver(diverName);
                break;
            case "WreckDiver":
            diver = new WreckDiver(diverName);
                break;
            default:
            throw new IllegalArgumentException(DIVER_INVALID_KIND);
        }
        this.diverRepository.add(diver);
        return String.format(DIVER_ADDED,kind,diverName);
    }

    @Override
    public String addDivingSite(String siteName, String... seaCreatures) {
        DivingSite divingSite = new DivingSiteImpl(siteName);
        for (String seaCreature : seaCreatures) {
            divingSite.getSeaCreatures().add(seaCreature);
        }
        this.divingSiteRepository.add(divingSite);
        return String.format(DIVING_SITE_ADDED,siteName);
    }

    @Override
    public String removeDiver(String diverName) {
        Diver diver = this.diverRepository.byName(diverName);
        if (diver == null){
            throw new IllegalArgumentException(String.format(DIVER_DOES_NOT_EXIST,diverName));
        }
        this.diverRepository.remove(diver);
        return String.format(DIVER_REMOVE,diverName);
    }

    @Override
    public String startDiving(String siteName) {
        List<Diver> diversCanDive = this.diverRepository.getCollection().stream().filter(diver ->
                diver.getOxygen() > 30).collect(Collectors.toList());
        if (diversCanDive.isEmpty()){
            throw new IllegalArgumentException(SITE_DIVERS_DOES_NOT_EXISTS);
        }
        int removedDiverCount = 0;
        DivingSite divingSite = this.divingSiteRepository.byName(siteName);
        Diving diving = new DivingImpl();

        long countBefore = this.diverRepository.getCollection().stream().filter(diver -> !diver.canDive()).count();
        diving.searching(divingSite,diversCanDive);
        divingCount++;
        long afterCount = this.diverRepository.getCollection().stream().filter(diver -> !diver.canDive()).count();
        return String.format(SITE_DIVING,siteName,countBefore - afterCount);
    }

    @Override
    public String getStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(FINAL_DIVING_SITES,divingCount)).append(System.lineSeparator());
    sb.append(FINAL_DIVERS_STATISTICS);
    String diverCatch;

        for (Diver diver : this.diverRepository.getCollection()) {
            Collection<String> seaCreatures = diver.getSeaCatch().getSeaCreatures();
            if (seaCreatures.isEmpty()){
                diverCatch = "None";
            }else {
                diverCatch= String.join(", ", diver.getSeaCatch().getSeaCreatures()).trim();
            }

            sb.append(System.lineSeparator());
            sb.append(String.format(FINAL_DIVER_NAME,diver.getName())).append(System.lineSeparator())
                    .append(String.format(FINAL_DIVER_OXYGEN,diver.getOxygen())).append(System.lineSeparator())
                    .append(String.format(FINAL_DIVER_CATCH,diverCatch));

        }
        return String.valueOf(sb).trim();
    }
}
