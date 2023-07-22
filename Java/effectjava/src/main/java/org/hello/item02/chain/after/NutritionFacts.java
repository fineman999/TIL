package org.hello.item02.chain.after;

public class NutritionFacts {
    private final int servingSize; // (mL) required
    private final int servings; // (per container) required
    private final int calories; // (per serving) optional
    private final int fat; // (g/serving) optional
    private final int sodium; // (mg/serving) optional
    private final int carbohydrate; // (g/serving) optional

    public static class Builder {
        // required parameters
        private final int servingSize;
        private final int servings;

        // optional parameters - initialized to default values
        private int calories = 0; // (per serving) optional
        private int fat = 0; // (g/serving) optional
        private int sodium = 0; // (mg/serving) optional
        private int carbohydrate = 0; // (g/serving) optional

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories; // (per serving) optional
        fat = builder.fat; // (g/serving) optional
        sodium = builder.sodium; // (mg/serving) optional
        carbohydrate = builder.carbohydrate; // (g/serving) optional
    }

    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
            .calories(100).sodium(35).carbohydrate(27).build();
    }

}
