package zoo.core;

import zoo.entities.animals.Animal;
import zoo.entities.animals.AquaticAnimal;
import zoo.entities.animals.TerrestrialAnimal;
import zoo.entities.areas.Area;
import zoo.entities.areas.BaseArea;
import zoo.entities.areas.LandArea;
import zoo.entities.areas.WaterArea;
import zoo.entities.foods.Food;
import zoo.entities.foods.Meat;
import zoo.entities.foods.Vegetable;
import zoo.repositories.FoodRepository;
import zoo.repositories.FoodRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;


import static zoo.common.ConstantMessages.*;
import static zoo.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private FoodRepository foodRepository;
    private Collection <Area> areas;

    public ControllerImpl() {
        this.areas = new ArrayList<>();
        this.foodRepository = new FoodRepositoryImpl();
    }

    @Override
    public String addArea(String areaType, String areaName) {
        Area area;
        if (areaType.equals("WaterArea")){
            area = new WaterArea(areaName);
        }else if (areaType.equals("LandArea")){
            area = new LandArea(areaName);
        }else {
            throw new NullPointerException(INVALID_AREA_TYPE);
        }
        this.areas.add(area);
        return String.format(SUCCESSFULLY_ADDED_AREA_TYPE,areaType);
    }

    @Override
    public String buyFood(String foodType) {
        Food food;
        if (foodType.equals("Vegetable")){
            food = new Vegetable();
        }else if (foodType.equals("Meat")){
            food = new Meat();
        }else {
            throw new IllegalArgumentException(INVALID_FOOD_TYPE);
        }
        this.foodRepository.add(food);
        return String.format(SUCCESSFULLY_ADDED_FOOD_TYPE,foodType);
    }

    @Override
    public String foodForArea(String areaName, String foodType) {
        Food food = this.foodRepository.findByType(foodType);
        Area area = getAreaName(areaName);
        if (food == null){
            throw new IllegalArgumentException(String.format(NO_FOOD_FOUND,foodType));
        }
        area.addFood(food);
        this.foodRepository.remove(food);
        return String.format(SUCCESSFULLY_ADDED_FOOD_IN_AREA,foodType,areaName);
    }

    @Override
    public String addAnimal(String areaName, String animalType, String animalName, String kind, double price) {
        Animal animal;
        BaseArea  area = (BaseArea) getAreaName(areaName);
        if (animalType.equals("AquaticAnimal")){
            animal = new AquaticAnimal(animalName,kind,price);
        }else if (animalType.equals("TerrestrialAnimal")){
            animal = new TerrestrialAnimal(animalName,kind,price);
        }else {
            throw new IllegalArgumentException(INVALID_ANIMAL_TYPE);
        }

        if (area.getClass().getSimpleName().equals("WaterArea") && animalType.equals("AquaticAnimal")) {
            area.addAnimal(animal);
        } else if (area.getClass().getSimpleName().equals("LandArea") && animalType.equals("TerrestrialAnimal")) {
            area.addAnimal(animal);
        } else {
            return AREA_NOT_SUITABLE;
        }
        return String.format(SUCCESSFULLY_ADDED_ANIMAL_IN_AREA, animalType, areaName);
    }

    @Override
    public String feedAnimal(String areaName) {
        Area area = getAreaName(areaName);
        for (Animal animal : area.getAnimal()) {
            animal.eat();
        }
        int animalCount = area.getAnimal().size();
        return String.format(ANIMALS_FED,animalCount);
    }

    @Override
    public String calculateKg(String areaName) {
        Area area = getAreaName(areaName);
        double sum = area.getAnimal().stream().mapToDouble(Animal::getKg).sum();
        return String.format(KILOGRAMS_AREA,areaName,sum);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        for (Area area : areas) {
            sb.append(area.getInfo());
            sb.append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
    private Area getAreaName (String areaName){
        return this.areas.stream().filter(a -> a.getName().equals(areaName)).findFirst().get();
    }
}
