package handball.core;
import handball.entities.equipment.ElbowPad;
import handball.entities.equipment.Equipment;
import handball.entities.equipment.Kneepad;
import handball.entities.gameplay.Gameplay;
import handball.entities.gameplay.Indoor;
import handball.entities.gameplay.Outdoor;
import handball.entities.team.Bulgaria;
import handball.entities.team.Germany;
import handball.entities.team.Team;
import handball.repositories.EquipmentRepository;
import handball.repositories.Repository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static handball.common.ConstantMessages.*;
import static handball.common.ExceptionMessages.*;

public class ControllerImpl implements Controller{
    private Repository equipment;
    private Collection <Gameplay> gameplays;

    public ControllerImpl() {
        this.equipment = new EquipmentRepository();
        this.gameplays = new ArrayList<>();
    }

    @Override
    public String addGameplay(String gameplayType, String gameplayName) {
        Gameplay gameplay;
        if (gameplayType.equals("Outdoor")){
            gameplay = new Outdoor(gameplayName);
        }else if (gameplayType.equals("Indoor")){
            gameplay = new Indoor(gameplayName);
        }else {
            throw new NullPointerException(INVALID_GAMEPLAY_TYPE);
        }
        gameplays.add(gameplay);
        return String.format(SUCCESSFULLY_ADDED_GAMEPLAY_TYPE,gameplayType);
    }

    @Override
    public String addEquipment(String equipmentType) {
        Equipment equipment1;
        if (equipmentType.equals("Kneepad")){
            equipment1 = new Kneepad();
        }else if (equipmentType.equals("ElbowPad")){
            equipment1 = new ElbowPad();
        }else{
            throw new IllegalArgumentException(INVALID_EQUIPMENT_TYPE);
        }
        equipment.add(equipment1);
        return String.format(SUCCESSFULLY_ADDED_EQUIPMENT_TYPE,equipmentType);
    }

    @Override
    public String equipmentRequirement(String gameplayName, String equipmentType) {
        Equipment equipment1 = this.equipment.findByType(equipmentType);
        if (equipment1==null){
            throw new IllegalArgumentException(String.format(NO_EQUIPMENT_FOUND,equipmentType));
        }
        Gameplay gameplay = getGameplay(gameplayName);
        gameplay.addEquipment(equipment1);
        equipment.remove(equipment1);
        return String.format(SUCCESSFULLY_ADDED_EQUIPMENT_IN_GAMEPLAY,equipmentType,gameplayName);
    }

    @Override
    public String addTeam(String gameplayName, String teamType, String teamName, String country, int advantage) {
        Team team;
        if (teamType.equals("Bulgaria")){
            team = new Bulgaria(teamName,country,advantage);
        }else if (teamType.equals("Germany")){
            team = new Germany (teamName,country,advantage);
        }else {
            throw new IllegalArgumentException (INVALID_TEAM_TYPE);
        }
        Gameplay gameplay = getGameplay(gameplayName);

        String output;
        if (!canPlay(teamType,gameplay)){
            output = GAMEPLAY_NOT_SUITABLE;
        }else {
            gameplay.addTeam(team);
            output = String.format(SUCCESSFULLY_ADDED_TEAM_IN_GAMEPLAY,teamType,gameplayName);
        }
        return output;
    }

    @Override
    public String playInGameplay(String gameplayName) {
        Gameplay gameplay = getGameplay(gameplayName);
        gameplay.teamsInGameplay();
        return String.format(TEAMS_PLAYED,gameplays.size());
    }

    @Override
    public String percentAdvantage(String gameplayName) {
        Gameplay gameplay = getGameplay(gameplayName);
        int sum = gameplay.getTeam().stream().mapToInt(Team::getAdvantage).sum();
        return String.format(ADVANTAGE_GAMEPLAY,gameplayName,sum);
    }

    @Override
    public String getStatistics() {
        return this.gameplays.stream()
                .map(Gameplay::toString)
                .collect(Collectors.joining(System.lineSeparator())).trim();
    }
    private Gameplay getGameplay(String gameplayName) {
        return gameplays.stream().filter(e -> e.getName().equals(gameplayName)).findFirst().get();
    }
    private static boolean canPlay (String teamType, Gameplay gameplay) {
        String gameType = gameplay.getClass().getSimpleName();
        if (teamType.equals("Bulgaria") && gameType.equals("Outdoor")){
            return true;
        }else if (teamType.equals("Germany") && gameType.equals("Indoor")){
            return true;
        }
        return false;
    }
}
