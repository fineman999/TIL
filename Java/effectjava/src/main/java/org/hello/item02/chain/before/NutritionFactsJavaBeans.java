package org.hello.item02.chain.before;

public class NutritionFactsJavaBeans {
    private int servingSize; // (mL) required
    private int servings; // (per container) required
    private int calories; // (per serving) optional
    private int fat; // (g/serving) optional
    private int sodium; // (mg/serving) optional
    private int carbohydrate; // (g/serving) optional

    public NutritionFactsJavaBeans() {
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}
