package com.republicofgavin.creditcardholder.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.republicofgavin.creditcardholder.R;
import com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType;
import com.republicofgavin.creditcardholder.util.CreditCardValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that handles UI for adding a credit card for storage.
 *
 * @author Gavin McNeely
 */
public class AddCreditCardFragment extends Fragment implements View.OnFocusChangeListener, Spinner.OnItemSelectedListener, Button.OnClickListener {
    private static final String TAG = AddCreditCardFragment.class.getSimpleName();

    private Spinner creditCardTypeSpinner;
    private ImageView creditCardImage;
    private TextView creditCardTypeHelpText;

    private EditText creditCardNumberEditText;
    private int maxCCNLength = 16;//Current API level does not let us programmatically grab the maxLength.
    private TextView creditCardNumberHelpText;

    private EditText creditCardCVVEditText;
    private int maxCVVLength = 4;
    private TextView creditCardCVVHelpText;

    private EditText creditCardDateMonthEditText;
    private EditText creditCardDateYearEditText;
    private TextView creditCardDateHelpText;

    private Button submitButton;

    private List<CreditCardCompanyType> creditCardCompanyTypeList;//List of values that sequentially spinner list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_credit_card, container, false);

        creditCardTypeSpinner = (Spinner) rootView.findViewById(R.id.credit_card_type_spinner);
        creditCardImage = (ImageView) rootView.findViewById(R.id.credit_card_type_image);
        creditCardTypeHelpText = (TextView) rootView.findViewById(R.id.credit_card_type_help_text);

        creditCardNumberEditText = (EditText) rootView.findViewById(R.id.credit_card_number);
        creditCardNumberHelpText = (TextView) rootView.findViewById(R.id.credit_card_number_help_text);

        creditCardCVVEditText = (EditText) rootView.findViewById(R.id.credit_card_cvv);
        creditCardCVVHelpText = (TextView) rootView.findViewById(R.id.credit_card_cvv_help_text);

        creditCardDateMonthEditText = (EditText) rootView.findViewById(R.id.credit_card_month);
        creditCardDateYearEditText = (EditText) rootView.findViewById(R.id.credit_card_year);
        creditCardDateHelpText = (TextView) rootView.findViewById(R.id.credit_card_date_help_text);

        submitButton = (Button) rootView.findViewById(R.id.submit_button);

        creditCardCompanyTypeList = new ArrayList<>();
        creditCardCompanyTypeList.add(CreditCardCompanyType.AMEX);
        creditCardCompanyTypeList.add(CreditCardCompanyType.DISCOVER);
        creditCardCompanyTypeList.add(CreditCardCompanyType.JCB);
        creditCardCompanyTypeList.add(CreditCardCompanyType.MASTERCARD);
        creditCardCompanyTypeList.add(CreditCardCompanyType.VISA);
        creditCardCompanyTypeList.add(CreditCardCompanyType.UNKNOWN);

        creditCardNumberEditText.addTextChangedListener(new CCNTextWatcher());
        creditCardCVVEditText.setOnFocusChangeListener(this);
        creditCardTypeSpinner.setOnItemSelectedListener(this);
        submitButton.setOnClickListener(this);

        final ExpirationDateTextWatcher textWatcher = new ExpirationDateTextWatcher();
        creditCardDateMonthEditText.addTextChangedListener(textWatcher);
        creditCardDateYearEditText.addTextChangedListener(textWatcher);

        creditCardTypeSpinner.setSelection(creditCardCompanyTypeList.size() - 1);

        return rootView;
    }

    /**
     * {@inheritDoc}
     * Upon clicking the CVV field, the a CVV hint icon is shown. If they click something else, the icon goes away.
     */
    @Override
    public void onFocusChange(final View v, final boolean hasFocus) {
        if (v.getId() == R.id.credit_card_cvv && hasFocus) {
            setCVV();
        } else {
            creditCardCVVEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    /**
     * Sets the CVV icon to the current item in the spinner.
     */
    private void setCVV() {
        final CreditCardCompanyType currentCreditCardCompanyType = creditCardCompanyTypeList.get(creditCardTypeSpinner.getSelectedItemPosition());
        if (currentCreditCardCompanyType == CreditCardCompanyType.AMEX) {
            creditCardCVVEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.amex_cvv), null);
        } else {
            creditCardCVVEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.cvv), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final CreditCardCompanyType currentCreditCardCompanyType = creditCardCompanyTypeList.get(creditCardTypeSpinner.getSelectedItemPosition());
        Log.d(TAG, "onItemSelected called for currentCreditCardCompanyType:" + currentCreditCardCompanyType.name());
        final int cardDrawableId;
        switch (currentCreditCardCompanyType) {
            case AMEX:
                cardDrawableId = R.drawable.amex;
                creditCardTypeHelpText.setText("");
                break;
            case DISCOVER:
                cardDrawableId = R.drawable.discover;
                creditCardTypeHelpText.setText("");
                break;
            case JCB:
                cardDrawableId = R.drawable.jcb;
                creditCardTypeHelpText.setText("");
                break;
            case MASTERCARD:
                cardDrawableId = R.drawable.mastercard;
                creditCardTypeHelpText.setText("");
                break;
            case VISA:
                cardDrawableId = R.drawable.visa;
                creditCardTypeHelpText.setText("");
                break;
            case UNKNOWN:
            default:
                cardDrawableId = R.drawable.generic_card;
                break;
        }

        maxCVVLength = currentCreditCardCompanyType.getNumberOfDigitsInCVV();
        creditCardCVVEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCVVLength)});

        maxCCNLength = currentCreditCardCompanyType.getNumberOfDigitsInCreditCard();
        creditCardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCCNLength)});

        //The user can interact with the adapter, but still have focus on the CVV. Thus, we should change its picture if visible.
        if (creditCardCVVEditText.getCompoundDrawables()[2] != null) {
            setCVV();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.d(TAG, "API level:" + Build.VERSION.SDK_INT + " calling older background method");
            //Deprecated API. Non-deprecated is not available in all supported versions.
            creditCardImage.setBackgroundDrawable(getResources().getDrawable(cardDrawableId));
        } else {
            Log.d(TAG, "API level:" + Build.VERSION.SDK_INT + " calling newer background method");
            creditCardImage.setBackground(getResources().getDrawable(cardDrawableId));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //no-op
    }


    @Override
    public void onClick(View v) {
        //submit button
        Log.d(TAG, "Submit button clicked");
        boolean shouldShowErrorDialog = false;
        final CreditCardCompanyType currentCreditCardCompanyType = creditCardCompanyTypeList.get(creditCardTypeSpinner.getSelectedItemPosition());
        if (currentCreditCardCompanyType == CreditCardCompanyType.UNKNOWN) {
            shouldShowErrorDialog = true;
            creditCardTypeHelpText.setText(getString(R.string.credit_card_unknown_type_error));
            Log.d(TAG, "Invalid CC type");
        }
        //CCN
        if (!CreditCardValidator.isValidNumberOfCreditCardDigits(currentCreditCardCompanyType, creditCardNumberEditText.getText().toString())) {
            creditCardNumberHelpText.setText(getString(R.string.credit_card_number_error_invalid_digit_number));
            shouldShowErrorDialog = true;
            Log.d(TAG, "Invalid CCN digit number");
        } else if (!CreditCardValidator.isLuhnValidated(creditCardNumberEditText.getText().toString())) {
            creditCardNumberHelpText.setText(AddCreditCardFragment.this.getString(R.string.credit_card_number_error_luhn_validation));
            shouldShowErrorDialog = true;
            Log.d(TAG, "Invalid Luhn number");
        } else {
            creditCardNumberHelpText.setText("");
        }

        //CCV
        if (!CreditCardValidator.isValidNumberOfCVVDigits(currentCreditCardCompanyType, creditCardCVVEditText.getText().toString())) {
            creditCardCVVHelpText.setText(getString(R.string.credit_card_cvv_invalid_digit_number));
            shouldShowErrorDialog = true;
            Log.d(TAG, "Invalid CVV digit number");
        } else {
            creditCardCVVHelpText.setText("");
        }

        //Date
        if (creditCardDateMonthEditText.getText().length() != 2 && creditCardDateYearEditText.getText().length() != 2) {
            shouldShowErrorDialog = true;
            creditCardDateHelpText.setText(getString(R.string.credit_card_incomplete_date_error));
            Log.d(TAG, "Incomplete expire date");
        } else {
            final int month = Integer.parseInt(creditCardDateMonthEditText.getText().toString());
            final int year = Integer.parseInt(creditCardDateYearEditText.getText().toString());
            if (CreditCardValidator.isExpired(month, year)) {
                creditCardDateHelpText.setText(getString(R.string.credit_card_date_error));
                Log.d(TAG, "Invalid expire date");
            } else {
                creditCardDateHelpText.setText("");
            }
        }
        final boolean shouldFinishActivity = shouldShowErrorDialog;//needs a final variable to access.
        final String dialogTitle = (shouldShowErrorDialog) ? getString(R.string.credit_card_error_dialog_title)
                : getString(R.string.credit_card_success_title);
        final String dialogMessage = (shouldShowErrorDialog) ? getString(R.string.credit_card_error_dialog_message)
                : getString(R.string.credit_card_success_dialog_message);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (!shouldFinishActivity) {
                    getActivity().finish();
                    Log.d(TAG, "Finishing activity with success");
                }
            }
        });
        final AlertDialog errorDialog = builder.create();
        errorDialog.show();
    }

    /**
     * Handles the text change events for the CCN(Credit Card Number) field. It performs input validation
     */
    private class CCNTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //no-op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //no-op
        }

        /**
         * {@inheritDoc}
         * Once the user has inputted enough digits for the credit card number, validation is performed.
         */
        @Override
        public void afterTextChanged(Editable s) {
            final String newText = s.toString();
            //Has reached the max number of digits for this credit card type
            if (newText.length() == maxCCNLength) {
                if (!CreditCardValidator.isLuhnValidated(newText)) {
                    creditCardNumberHelpText.setText(AddCreditCardFragment.this.getString(R.string.credit_card_number_error_luhn_validation));
                } else {
                    creditCardNumberHelpText.setText("");//Seems to be a valid CCN.
                }
            } else {
                creditCardNumberHelpText.setText("");//They don't have enough digits to qualify for validation.
            }
        }
    }

    /**
     * Handles the text change events for the expiration date field. It performs date validation
     */
    private class ExpirationDateTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //no-op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //no-op
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (creditCardDateMonthEditText.getText().length() == 2 && creditCardDateYearEditText.getText().length() == 2) {
                final int month = Integer.parseInt(creditCardDateMonthEditText.getText().toString());
                final int year = Integer.parseInt(creditCardDateYearEditText.getText().toString());
                if (CreditCardValidator.isExpired(month, year)) {
                    creditCardDateHelpText.setText(getString(R.string.credit_card_date_error));
                } else {
                    creditCardDateHelpText.setText("");// date is valid and non-expired
                }
            } else {
                creditCardDateHelpText.setText("");//data is incomplete at this time, no error message.
            }

        }
    }
}
