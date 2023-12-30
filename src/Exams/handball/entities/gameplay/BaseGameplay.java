package handball.entities.gameplay;
import handball.entities.equipment.Equipment;
import handball.entities.team.Team;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static handball.common.ExceptionMessages.GAMEPLAY_NAME_NULL_OR_EMPTY;

public abstract class BaseGameplay implements Gameplay{

    private String name;
    private int capacity;
    private Collection <Equipment> equipments;
    private Collection <Team> teams;

    public BaseGameplay(String name, int capacity) {
        setName(name);
        setCapacity(capacity);
        this.equipments = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public void setName(String name) {
        if (name == null || name.isBlank()){
            throw new NullPointerException(GAMEPLAY_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int allProtection() {
        return this.equipments.stream().mapToInt(Equipment::getProtection).sum();
    }

    @Override
    public void addTeam(Team team) {
        this.teams.add(team);
    }

    @Override
    public void removeTeam(Team team) {
        this.teams.remove(team);
    }

    @Override
    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    @Override
    public void teamsInGameplay() {
        this.teams.forEach(Team::play);
    }

    @Override
    public Collection<Team> getTeam() {
        return teams;
    }

    @Override
    public Collection<Equipment> getEquipments() {
        return equipments;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s\n", this.name, this.getClass().getSimpleName()));
        sb.append("Team: ");
        if (this.teams.isEmpty()){
            sb.append("none");
        }else {
            String teamsName = this.teams.stream()
                    .map(Team::getName)
                    .collect(Collectors.joining(" "));
            sb.append(teamsName);
        }
        sb.append(System.lineSeparator());
        sb.append("Equipment: ").append(this.equipments.size());
        sb.append(", ");
        sb.append("Protection: ").append(allProtection());
        return sb.toString();
    }
}
