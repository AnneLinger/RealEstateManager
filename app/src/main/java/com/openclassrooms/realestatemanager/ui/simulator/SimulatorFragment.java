package com.openclassrooms.realestatemanager.ui.simulator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSimulatorBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;

import java.util.Objects;

/**
 * Activity for home loan simulator
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class SimulatorFragment extends Fragment {

    //For ui
    private FragmentSimulatorBinding mBinding;

    //For data
    private TextInputEditText propertyPriceEditText;
    private TextInputEditText contributionEditText;
    private TextInputEditText rateEditText;
    private TextInputEditText durationEditText;
    private int propertyPrice;
    private int contribution;
    private int rate;
    private int duration;

    public static SimulatorFragment newInstance() {
        return new SimulatorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSimulatorBinding.inflate(inflater, container, false);
        propertyPriceEditText = mBinding.etPropertyPriceSimulator;
        contributionEditText = mBinding.etContribution;
        rateEditText = mBinding.etRate;
        durationEditText = mBinding.etDuration;
        mBinding.btSimulate.setEnabled(false);
        //noinspection deprecation
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
        configureBottomNav();
        getDataFromForm();
        simulateHomeLoan();
    }

    private void configureToolbar() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void configureBottomNav() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void getDataFromForm() {
        getDataFromEditText(propertyPriceEditText);
        getDataFromEditText(contributionEditText);
        getDataFromEditText(rateEditText);
        getDataFromEditText(durationEditText);
    }

    private void getDataFromEditText(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == propertyPriceEditText.getEditableText()) {
                    propertyPrice = Integer.parseInt(Objects.requireNonNull(textInputEditText.getText()).toString());
                    enableButtonSimulate();
                } else if (s == contributionEditText.getEditableText()) {
                    contribution = Integer.parseInt(Objects.requireNonNull(textInputEditText.getText()).toString());
                    enableButtonSimulate();
                } else if (s == rateEditText.getEditableText()) {
                    rate = Integer.parseInt(Objects.requireNonNull(textInputEditText.getText()).toString());
                    enableButtonSimulate();
                    convertPercentValue();
                } else if (s == durationEditText.getEditableText()) {
                    duration = Integer.parseInt(Objects.requireNonNull(textInputEditText.getText()).toString());
                    enableButtonSimulate();
                    convertYearsInMonths();
                }
            }
        });
    }

    private void convertPercentValue() {
        rate = 1 + (rate / 100);
    }

    private void convertYearsInMonths() {
        duration = duration * 12;
    }

    //The button simulate is enabled only when all fields required are filled
    private void enableButtonSimulate() {
        mBinding.btSimulate.setEnabled(!Objects.requireNonNull(mBinding.etPropertyPriceSimulator.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mBinding.etContribution.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mBinding.etDuration.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mBinding.etRate.getText()).toString().isEmpty());
    }

    private void simulateHomeLoan() {
        mBinding.btSimulate.setOnClickListener(v -> calculateMonthlyPayments());
    }

    private void calculateMonthlyPayments() {
        int monthlyPayment = (propertyPrice - contribution) * rate / duration;
        String monthlyPaymentString = monthlyPayment + " " + getString(R.string.dollars);
        mBinding.tvResult.setText(monthlyPaymentString);
        mBinding.tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_white));
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
