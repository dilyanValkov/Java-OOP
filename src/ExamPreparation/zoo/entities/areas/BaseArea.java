package zoo.entities.areas;


import zoo.entities.animals.Animal;
import zoo.entities.foods.Food;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static zoo.common.ExceptionMessages.NOT_ENOUGH_CAPACITY;

public abstract class BaseArea implements Area{
    private String name;
    private int capacity;
    private Collection<Food> foods;
    private Collection <Animal> animals;

    public BaseArea(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.foods = new ArrayList<>();
        this.animals = new ArrayList<>();
    }
    public int sumCalories(){
        return this.foods.stream().mapToInt(Food::getCalories).sum();
    }
   public void addAnimal(Animal animal){
        if (animal.getKg()>=capacity){
            throw new IllegalStateException(NOT_ENOUGH_CAPACITY);
        }
        this.animals.add(animal);
    }
    public void removeAnimal(Animal animal){
        this.animals.remove(animal);
    }
   public void addFood(Food food){
        this.foods.add(food);
    }
    public void feed(){
        this.animals.forEach(Animal::eat);
    }
    public String getInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%s):\n", this.name, this.getClass().getSimpleName()));
        sb.append("Animals: ");
        if (this.animals.isEmpty()){
            sb.append("none");
        }else {
            String animalsNames = this.animals.stream()
                    .map(Animal::getName)
                    .collect(Collectors.joining(" "));
            sb.append(animalsNames);
            }
        sb.append(System.lineSeparator());
        sb.append("Foods: ").append(this.foods.size());
        sb.append(System.lineSeparator());
        sb.append("Calories: ").append(sumCalories());
        return sb.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Food> getFood() {
        return foods;
    }

    @Override
    public Collection<Animal> getAnimal() {
        return animals;
    }
}

