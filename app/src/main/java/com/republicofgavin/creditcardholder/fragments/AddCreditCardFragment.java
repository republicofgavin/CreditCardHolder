package com.republicofgavin.creditcardholder.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class AddCreditCardFragment extends Fragment implements View.OnFocusChangeListener, Spinner.OnItemSelectedListener {
    private Spinner creditCardTypeSpinner;
    private ImageView creditCardImage;
    private TextView creditCardTypeHelpText;

    private EditText creditCardNumberEditText;
    //16 numbers + 3 dashes.
    private int maxCCNLength = 19;//Current API level does not let us programmatically grab the maxLength.
    private TextView creditCardNumberHelpText;

    private EditText creditCardCVVEditText;
    private int maxCVVLength = 4;
    private TextView creditCardCVVHelpText;

    private EditText creditCardMonthEditText;
    private EditText creditCardYearEditText;
    private TextView creditCardDateHelpText;

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

        creditCardMonthEditText = (EditText) rootView.findViewById(R.id.credit_card_month);
        creditCardYearEditText = (EditText) rootView.findViewById(R.id.credit_card_year);
        creditCardDateHelpText = (TextView) rootView.findViewById(R.id.credit_card_date_help_text);

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

        creditCardTypeSpinner.setSelection(creditCardCompanyTypeList.size() - 1);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

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
        final int cardDrawableId;
        switch (currentCreditCardCompanyType) {
            case AMEX:
                cardDrawableId = R.drawable.amex;
                break;
            case DISCOVER:
                cardDrawableId = R.drawable.discover;
                break;
            case JCB:
                cardDrawableId = R.drawable.jcb;
                break;
            case MASTERCARD:
                cardDrawableId = R.drawable.mastercard;
                break;
            case VISA:
                cardDrawableId = R.drawable.visa;
                break;
            case UNKNOWN:
            default:
                cardDrawableId = R.drawable.generic_card;
                break;
        }

        maxCVVLength = currentCreditCardCompanyType.getNumberOfDigitsInCVV();
        creditCardCVVEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCVVLength)});
        if (creditCardCVVEditText.getText().length() > maxCVVLength) {//Need to shrink the string it is too long for card type
            final String shortenedString = creditCardCVVEditText.getText().toString().substring(0, maxCVVLength);
            creditCardCVVEditText.setText(shortenedString);
        }

        maxCCNLength = currentCreditCardCompanyType.getNumberOfDigitsInCreditCard();
        creditCardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCCNLength)});
        if (creditCardNumberEditText.getText().length() > maxCCNLength) {
            final String shortenedString = creditCardNumberEditText.getText().toString().substring(0, maxCCNLength);
            creditCardNumberEditText.setText(shortenedString); //performs Luhn validation as well.

        }

        //The user can interact with the adapter, but still have focus on the CVV. Thus, we should change its picture if visible.
        if (creditCardCVVEditText.getCompoundDrawables()[2] != null) {
            setCVV();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //Deprecated API. Non-deprecated is not available in all supported versions.
            creditCardImage.setBackgroundDrawable(getResources().getDrawable(cardDrawableId));
        } else {
            creditCardImage.setBackground(getResources().getDrawable(cardDrawableId));
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //no-op
    }

    /**
     * Handles the text change events for the CCN(Credit Card Number) field. It add/removes dashes, performs input validation
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
            String newText = s.toString();
            //Has reached the max number of digits for this credit card type
            if (newText.length() == maxCCNLength) {
                //final CreditCardCompanyType currentCreditCardCompanyType=creditCardCompanyTypeList.get(creditCardTypeSpinner.getSelectedItemPosition());
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
}
