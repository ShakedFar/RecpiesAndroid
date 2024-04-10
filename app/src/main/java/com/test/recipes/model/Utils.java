package com.test.recipes.model;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static int user_id;
    public static List<String> CATEGORIES;
    public static List<String> INGREDIENTS;

    static {
        CATEGORIES = new ArrayList<>();
        INGREDIENTS = new ArrayList<>();

        CATEGORIES.add("Breakfast recipes");
        CATEGORIES.add("Lunch recipes");
        CATEGORIES.add("Dinner recipes");
        CATEGORIES.add("Appetizer recipes");
        CATEGORIES.add("Salad recipes");
        CATEGORIES.add("Main-course recipes");
        CATEGORIES.add("Side-dish recipes");
        CATEGORIES.add("Baked-goods recipes");
        CATEGORIES.add("Dessert recipes");
        CATEGORIES.add("Snack recipes");
        CATEGORIES.add("Soup recipes");
        CATEGORIES.add("Holiday recipes");
        CATEGORIES.add("Vegetarian Dishes");
        CATEGORIES.add("Cookbook Reviews");

        INGREDIENTS.add("Vegetable");
        INGREDIENTS.add("Spices and Herbs");
        INGREDIENTS.add("Cereals and Pulses");
        INGREDIENTS.add("Meat");
        INGREDIENTS.add("Dairy Products");
        INGREDIENTS.add("Fruits");
        INGREDIENTS.add("Seafood");
        INGREDIENTS.add("Sugar and Sugar Products");
        INGREDIENTS.add("Nuts and Oilseeds");
        INGREDIENTS.add("Other Ingredients");

    }

}
