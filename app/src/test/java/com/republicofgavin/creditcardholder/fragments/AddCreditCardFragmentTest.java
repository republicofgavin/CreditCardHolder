package com.republicofgavin.creditcardholder.fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.republicofgavin.creditcardholder.R;
import com.republicofgavin.creditcardholder.activities.AddCreditCardActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

/**
 * Tests {@link com.republicofgavin.creditcardholder.fragments.AddCreditCardFragment}
 * Created by gavin on 3/26/15.
 */
//@Config(emulateSdk = 18, reportSdk = 18)
@Config(emulateSdk = 18, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
@TargetApi(18) //needed for IDE errors for the callOnClick
public class AddCreditCardFragmentTest {
    private AddCreditCardActivity currentActivity;
    private Spinner creditCardTypeSpinner;
    private ImageView creditCardImage;
    private TextView creditCardTypeHelpText;

    private EditText creditCardNumberEditText;

    private TextView creditCardNumberHelpText;

    private EditText creditCardCVVEditText;
    private TextView creditCardCVVHelpText;

    private EditText creditCardMonthEditText;
    private EditText creditCardYearEditText;
    private TextView creditCardDateHelpText;

    private Button submitButton;

    @Before
    public void setup() {
        currentActivity = Robolectric.buildActivity(AddCreditCardActivity.class).create().start().resume().get();

        creditCardTypeSpinner = (Spinner) currentActivity.findViewById(R.id.credit_card_type_spinner);
        creditCardImage = (ImageView) currentActivity.findViewById(R.id.credit_card_type_image);
        creditCardTypeHelpText = (TextView) currentActivity.findViewById(R.id.credit_card_type_help_text);

        creditCardNumberEditText = (EditText) currentActivity.findViewById(R.id.credit_card_number);
        creditCardNumberHelpText = (TextView) currentActivity.findViewById(R.id.credit_card_number_help_text);

        creditCardCVVEditText = (EditText) currentActivity.findViewById(R.id.credit_card_cvv);
        creditCardCVVHelpText = (TextView) currentActivity.findViewById(R.id.credit_card_cvv_help_text);

        creditCardMonthEditText = (EditText) currentActivity.findViewById(R.id.credit_card_month);
        creditCardYearEditText = (EditText) currentActivity.findViewById(R.id.credit_card_year);
        creditCardDateHelpText = (TextView) currentActivity.findViewById(R.id.credit_card_date_help_text);

        submitButton = (Button) currentActivity.findViewById(R.id.submit_button);
    }

    @Test
    public void testInitialUI() {
        Assert.assertEquals("Spinner is not set to unknown position", currentActivity.getResources().getStringArray(R.array.card_card_list)[5], creditCardTypeSpinner.getSelectedItem());
        Assert.assertEquals("Generic card is not set", Robolectric.application.getResources().getDrawable(R.drawable.generic_card), creditCardImage.getBackground());
        Assert.assertNull("CVV icon is not null", creditCardCVVEditText.getCompoundDrawables()[2]);
        //help text
        Assert.assertEquals("creditCardTypeHelpText is not set to empty", 0, creditCardTypeHelpText.getText().length());
        Assert.assertEquals("creditCardNumberHelpText is not set to empty", 0, creditCardNumberHelpText.getText().length());
        Assert.assertEquals("creditCardCVVHelpText is not set to empty", 0, creditCardCVVHelpText.getText().length());
        Assert.assertEquals("creditCardDateHelpText is not set to empty", 0, creditCardDateHelpText.getText().length());
        //edit text fields
        Assert.assertEquals("creditCardNumberEditText is not set to empty", 0, creditCardNumberEditText.getText().length());
        Assert.assertEquals("creditCardCVVEditText is not set to empty", 0, creditCardCVVEditText.getText().length());
        Assert.assertEquals("creditCardMonthEditText is not set to empty", 0, creditCardMonthEditText.getText().length());
        Assert.assertEquals("creditCardYearEditText is not set to empty", 0, creditCardYearEditText.getText().length());
        //Input types
        Assert.assertEquals("Wrong input type for CCN", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD, creditCardNumberEditText.getInputType());
        Assert.assertEquals("Wrong input type for CVV", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD, creditCardCVVEditText.getInputType());
        Assert.assertEquals("Wrong input type for year", InputType.TYPE_CLASS_NUMBER, creditCardYearEditText.getInputType());
        Assert.assertEquals("Wrong input type for month", InputType.TYPE_CLASS_NUMBER, creditCardMonthEditText.getInputType());
    }

    @Test
    public void testCreditCardSpinner() {
        creditCardCVVEditText.requestFocus();
        creditCardTypeSpinner.setSelection(1);//Discover card
        Assert.assertEquals("Discover card is not set", Robolectric.application.getResources().getDrawable(R.drawable.discover), creditCardImage.getBackground());
        //Comparison with Compound drawables does not work the other way
        Assert.assertEquals("Discover CVV is not set",
                ((BitmapDrawable) Robolectric.application.getResources().getDrawable(R.drawable.cvv)).getBitmap(),
                ((BitmapDrawable) creditCardCVVEditText.getCompoundDrawables()[2]).getBitmap());

        //Check to see if last digit is removed during setting to AMEX
        creditCardNumberEditText.setText("3379513561108791");
        creditCardTypeSpinner.setSelection(0);

        Assert.assertEquals("AMEX card is not set", Robolectric.application.getResources().getDrawable(R.drawable.amex), creditCardImage.getBackground());
        Assert.assertEquals("AMEX CVV is not set",
                ((BitmapDrawable) Robolectric.application.getResources().getDrawable(R.drawable.amex_cvv)).getBitmap(),
                ((BitmapDrawable) creditCardCVVEditText.getCompoundDrawables()[2]).getBitmap());

        //Test the shrinking of CVV
        creditCardCVVEditText.setText("4444");
        creditCardTypeSpinner.setSelection(2);//JCB

        Assert.assertEquals("JCB card is not set", Robolectric.application.getResources().getDrawable(R.drawable.jcb), creditCardImage.getBackground());
        Assert.assertEquals("JCB CVV is not set",
                ((BitmapDrawable) Robolectric.application.getResources().getDrawable(R.drawable.cvv)).getBitmap(),
                ((BitmapDrawable) creditCardCVVEditText.getCompoundDrawables()[2]).getBitmap());
    }

    @Test
    public void testCCN() {
        //Adding text
        creditCardTypeSpinner.setSelection(2);//Discover card

        creditCardNumberEditText.setText("6011111111111117");
        Assert.assertEquals("Luhn error message is set", "", creditCardNumberHelpText.getText().toString());

        creditCardNumberEditText.setText("3379513561108791");
        Assert.assertEquals("Luhn error message was not set", currentActivity.getString(R.string.credit_card_number_error_luhn_validation), creditCardNumberHelpText.getText().toString());

        creditCardNumberEditText.setText("337951356110879");
        Assert.assertEquals("Luhn error message was not cleared during incomplete input", "", creditCardNumberHelpText.getText().toString());
    }

    @Test
    public void testExpirationDate() {
        creditCardMonthEditText.setText("11");
        creditCardYearEditText.setText("11");
        Assert.assertEquals("Expiration error string not showing", currentActivity.getString(R.string.credit_card_date_error), creditCardDateHelpText.getText().toString());

        creditCardYearEditText.setText("99");
        Assert.assertEquals("Expiration error string showing", "", creditCardDateHelpText.getText().toString());
    }

    @Test
    public void testSubmitButton() {
        submitButton.callOnClick();//This should make all error message fields be filled.

        Assert.assertEquals("Unknown card error did not show", currentActivity.getString(R.string.credit_card_unknown_type_error), creditCardTypeHelpText.getText().toString());
        Assert.assertEquals("CCN digit error did not show", currentActivity.getString(R.string.credit_card_number_error_invalid_digit_number), creditCardNumberHelpText.getText().toString());
        Assert.assertEquals("CVV digit error did not show", currentActivity.getString(R.string.credit_card_cvv_invalid_digit_number), creditCardCVVHelpText.getText().toString());
        Assert.assertEquals("Incomplete date error did not show", currentActivity.getString(R.string.credit_card_incomplete_date_error), creditCardDateHelpText.getText().toString());

        ShadowAlertDialog shadowAlertDialog = Robolectric.shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        Assert.assertEquals("Error title not showing", currentActivity.getString(R.string.credit_card_error_dialog_title), shadowAlertDialog.getTitle());
        Assert.assertEquals("Error message not showing", currentActivity.getString(R.string.credit_card_error_dialog_message), shadowAlertDialog.getMessage());

        shadowAlertDialog.dismiss();

        //Check success case
        creditCardTypeSpinner.setSelection(2);//Discover card
        creditCardNumberEditText.setText("6011111111111118");
        creditCardCVVEditText.setText("761");
        creditCardYearEditText.setText("99");
        creditCardMonthEditText.setText("13");

        submitButton.callOnClick();//check Luhn and date validation

        Assert.assertEquals("Luhn error did not show", currentActivity.getString(R.string.credit_card_number_error_luhn_validation), creditCardNumberHelpText.getText().toString());
        Assert.assertEquals("Invalid date error did not show", currentActivity.getString(R.string.credit_card_date_error), creditCardDateHelpText.getText().toString());

        creditCardNumberEditText.setText("6011111111111117");//correct CCN
        creditCardMonthEditText.setText("11");

        submitButton.callOnClick(); //test success

        Assert.assertEquals("card type error shown", "", creditCardTypeHelpText.getText().toString());
        Assert.assertEquals("CCN error did shown", "", creditCardNumberHelpText.getText().toString());
        Assert.assertEquals("CVV error did shown", "", creditCardCVVHelpText.getText().toString());
        Assert.assertEquals("date error did shown", "", creditCardDateHelpText.getText().toString());

        shadowAlertDialog = Robolectric.shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        Assert.assertEquals("Success title not showing", currentActivity.getString(R.string.credit_card_success_title), shadowAlertDialog.getTitle());
        Assert.assertEquals("Success message not showing", currentActivity.getString(R.string.credit_card_success_dialog_message), shadowAlertDialog.getMessage());

        Robolectric.clickOn(ShadowAlertDialog.getLatestAlertDialog().getButton(AlertDialog.BUTTON_POSITIVE));

        Assert.assertTrue("activity is not finishing", Robolectric.shadowOf(currentActivity).isFinishing());
    }

}
