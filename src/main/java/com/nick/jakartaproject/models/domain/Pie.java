package com.nick.jakartaproject.models.domain;

import java.io.Serializable;

public class Pie implements Serializable {
    private int id;
    private String name;
    private double price;
    private String fileName;
    private String ingredients;
    private String description;

    public Pie() {}

    public Pie(int id, String name, double price, String fileName, String ingredients, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.fileName = fileName;
        this.ingredients = ingredients;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Pie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", fileName='" + fileName + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
