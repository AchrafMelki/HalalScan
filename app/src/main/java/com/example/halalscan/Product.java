package com.example.halalscan;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Product {
    @SerializedName("product_name")
    private String productName;

    @SerializedName("ingredients_text")
    private String ingredientsText;

    @SerializedName("labels_tags")
    private List<String> labelsTags;

    @SerializedName("categories_hierarchy")
    private List<String> categoriesHierarchy;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIngredientsText() {
        return ingredientsText;
    }

    public void setIngredientsText(String ingredientsText) {
        this.ingredientsText = ingredientsText;
    }

    public List<String> getLabelsTags() {
        return labelsTags;
    }

    public void setLabelsTags(List<String> labelsTags) {
        this.labelsTags = labelsTags;
    }

    public List<String> getCategoriesHierarchy() {
        return categoriesHierarchy;
    }

    public void setCategoriesHierarchy(List<String> categoriesHierarchy) {
        this.categoriesHierarchy = categoriesHierarchy;
    }
}
