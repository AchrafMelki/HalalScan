package com.example.halalscan;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.method.LinkMovementMethod;

import java.util.ArrayList;
import java.util.List;

public class ProductDialogFragment extends DialogFragment {

    private static final String ARG_PRODUCT_NAME = "product_name";
    private static final String ARG_INGREDIENTS = "ingredients";
    private static final String ARG_LABELS = "labels";
    private static final String ARG_CATEGORIES = "categories";
    private static final String TAG = "ProductDialogFragment";

    public static ProductDialogFragment newInstance(String productName, String ingredients, List<String> labels, List<String> categories) {
        ProductDialogFragment fragment = new ProductDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_NAME, productName);
        args.putString(ARG_INGREDIENTS, ingredients);
        args.putStringArrayList(ARG_LABELS, new ArrayList<>(labels));
        args.putStringArrayList(ARG_CATEGORIES, new ArrayList<>(categories));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_dialog, container, false);

        TextView productNameTextView = view.findViewById(R.id.product_name);
        TextView ingredientsTextView = view.findViewById(R.id.ingredients);
        TextView halalStatusTextView = view.findViewById(R.id.halal_status);
        TextView vinegarLinkTextView = view.findViewById(R.id.vinegar_link);

        if (getArguments() != null) {
            String productName = getArguments().getString(ARG_PRODUCT_NAME);
            String ingredients = getArguments().getString(ARG_INGREDIENTS);
            List<String> labels = getArguments().getStringArrayList(ARG_LABELS);
            List<String> categories = getArguments().getStringArrayList(ARG_CATEGORIES);

            productNameTextView.setText(productName);

            List<HalalChecker.IngredientStatus> ingredientStatuses = HalalChecker.checkIngredients(ingredients, labels, categories);
            SpannableStringBuilder spannableIngredients = new SpannableStringBuilder();

            HalalChecker.HalalStatus overallStatus = HalalChecker.HalalStatus.HALAL;
            boolean containsVinegar = false;

            for (HalalChecker.IngredientStatus status : ingredientStatuses) {
                Log.d(TAG, "Ingredient: " + status.getIngredient() + " - Status: " + status.getStatus());
                int start = spannableIngredients.length();
                spannableIngredients.append(status.getIngredient());
                int end = spannableIngredients.length();

                if (status.getStatus() == HalalChecker.HalalStatus.NOT_HALAL) {
                    spannableIngredients.setSpan(new ForegroundColorSpan(Color.RED), start, end, 0);
                    overallStatus = HalalChecker.HalalStatus.NOT_HALAL;
                } else if (status.getStatus() == HalalChecker.HalalStatus.RISKY) {
                    spannableIngredients.setSpan(new ForegroundColorSpan(Color.rgb(255, 165, 0)), start, end, 0); // orange color
                    if (status.getIngredient().contains("vinaigre")) {
                        containsVinegar = true;
                    }
                    if (overallStatus == HalalChecker.HalalStatus.HALAL) {
                        overallStatus = HalalChecker.HalalStatus.RISKY;
                    }
                }

                spannableIngredients.append(", ");
            }

            // Remove the last comma and space
            if (spannableIngredients.length() > 0) {
                spannableIngredients.delete(spannableIngredients.length() - 2, spannableIngredients.length());
            }

            ingredientsTextView.setText(spannableIngredients);

            switch (overallStatus) {
                case NOT_HALAL:
                    halalStatusTextView.setText("Pas Halal");
                    halalStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    vinegarLinkTextView.setVisibility(View.GONE); // Hide the link for NOT_HALAL
                    break;
                case RISKY:
                    halalStatusTextView.setText("Risque pas Halal");
                    halalStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    if (containsVinegar) {
                        vinegarLinkTextView.setVisibility(View.VISIBLE);
                        String linkText = "En savoir plus sur le vinaigre : https://islamqa.info/fr/answers/276185/le-statut-du-vinaigre-fabrique-a-parait-du-vin";
                        SpannableString spannableLink = new SpannableString(linkText);
                        spannableLink.setSpan(new URLSpan("https://islamqa.info/fr/answers/276185/le-statut-du-vinaigre-fabrique-a-parait-du-vin"), 25, linkText.length(), 0);
                        vinegarLinkTextView.setText(spannableLink);
                        vinegarLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    } else {
                        vinegarLinkTextView.setVisibility(View.GONE);
                    }
                    break;
                case HALAL:
                    halalStatusTextView.setText("Halal");
                    halalStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    vinegarLinkTextView.setVisibility(View.GONE); // Hide the link for HALAL
                    break;
            }
        }

        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showHomeScreen();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
