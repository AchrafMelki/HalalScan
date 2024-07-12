package com.example.halalscan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HalalChecker {
    private static final List<String> NOT_HALAL_INGREDIENTS = Arrays.asList("porc", "lard", "jambon", "bacon");
    private static final List<String> RISKY_INGREDIENTS = Arrays.asList("alcool", "vin", "bière", "vinaigre");
    private static final List<String> MEATS = Arrays.asList("boeuf", "veau", "agneau", "poulet", "dinde");
    private static final String HALAL_LABEL = "halal";

    public static List<IngredientStatus> checkIngredients(String ingredients, List<String> labels, List<String> categories) {
        List<IngredientStatus> ingredientStatuses = new ArrayList<>();
        String[] ingredientArray = ingredients.toLowerCase().split(",\\s*");

        for (String ingredient : ingredientArray) {
            if (containsAny(ingredient, NOT_HALAL_INGREDIENTS)) {
                ingredientStatuses.add(new IngredientStatus(ingredient, HalalStatus.NOT_HALAL));
            } else if (containsAny(ingredient, RISKY_INGREDIENTS)) {
                ingredientStatuses.add(new IngredientStatus(ingredient, HalalStatus.RISKY));
            } else if (containsAny(ingredient, MEATS) && !labels.contains(HALAL_LABEL)) {
                ingredientStatuses.add(new IngredientStatus(ingredient, HalalStatus.NOT_HALAL));
            } else {
                ingredientStatuses.add(new IngredientStatus(ingredient, HalalStatus.HALAL));
            }
        }

        if (categories != null && categories.contains("en:alcoholic-beverages")) {
            for (IngredientStatus status : ingredientStatuses) {
                status.setStatus(HalalStatus.NOT_HALAL);
            }
        }

        return ingredientStatuses;
    }

    private static boolean containsAny(String ingredient, List<String> keywords) {
        for (String keyword : keywords) {
            if (ingredient.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public enum HalalStatus {
        NOT_HALAL,
        RISKY,
        HALAL
    }

    public static class IngredientStatus {
        private final String ingredient;
        private HalalStatus status;

        public IngredientStatus(String ingredient, HalalStatus status) {
            this.ingredient = ingredient;
            this.status = status;
        }

        public String getIngredient() {
            return ingredient;
        }

        public HalalStatus getStatus() {
            return status;
        }

        public void setStatus(HalalStatus status) {
            this.status = status;
        }
    }
}
