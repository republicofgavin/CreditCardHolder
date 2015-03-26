package com.republicofgavin.creditcardholder.util;

import android.text.TextUtils;
import android.util.Log;

import com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Does validation of credit card data (credit card numbers, CVVs, expiration dates, and credit card types).
 *
 * @author Gavin McNeely
 */
public class CreditCardValidator {

    private static final String TAG = CreditCardValidator.class.getSimpleName();

    /**
     * Checks to see if the credit card number passes the Luhn formula test http://en.wikipedia.org/wiki/Luhn_algorithm).
     *
     * @param creditCardNumber String version of a number to run the algorithm on. If the input is: not a number, null, empty, or blank (ex:"   "). False is returned.
     * @return True if the credit card number is valid. False otherwise.
     */
    public static boolean isLuhnValidated(final String creditCardNumber) {
        if (creditCardNumber == null || TextUtils.isEmpty(creditCardNumber.trim()) || !TextUtils.isDigitsOnly(creditCardNumber)) {
            return false;
        }
        int sum = 0;
        //Iterate through the number doubling every other digit.
        boolean shouldDouble = false;
        for (int count = creditCardNumber.length() - 1; count >= 0; count--) {
            int currentDigit = Character.getNumericValue(creditCardNumber.charAt(count));
            //if we don't double the digit, we just add it to our total.
            if (!shouldDouble) {
                sum = sum + currentDigit;
            }
            // if we double our digit, check to see if it is 10 or greater. If not, just add the double to the sum otherwise add each digit
            //of the doubled number to the sum.
            else {
                currentDigit = currentDigit * 2;
                //At most the double of any single digit integer is 18(9*2), thus we can just always add one in the double digit case (the +1) The -10 removes the first digit in the number.
                //Effectively, adding the first digit and the second digit together.
                sum = sum + ((currentDigit <= 9) ? currentDigit
                        : (currentDigit - 10) + 1);
            }
            shouldDouble = !shouldDouble;
        }
        return (sum % 10 == 0);
    }

    /**
     * Checks the enum and the credit card number string to see if the credit card number has the correct number of digits. For example AMEX has only 15 digit CCNs while others can have 16 digits.
     *
     * @param creditCardCompanyType The {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType} you are checking against.
     * @param creditCardNumber      The String version of the CCN you are checking. If null, empty, blank, or not all numbers is passed in, it is always false.
     * @return True if the CCN has the correct number of digits for the {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType}
     */
    public static boolean isValidNumberOfCreditCardDigits(final CreditCardCompanyType creditCardCompanyType, final String creditCardNumber) {
        if (creditCardNumber == null || TextUtils.isEmpty(creditCardNumber.trim()) || !TextUtils.isDigitsOnly(creditCardNumber)) {
            Log.w(TAG, "Error: Invalid input given to isValidNumberOfCreditCardDigits. Returning false. creditCardCompanyType:" +
                    creditCardCompanyType.name() + " creditCardNumber:" + creditCardNumber);
            return false;
        }
        final int digitsInCard;
        switch (creditCardCompanyType) {
            case JCB:
            case MASTERCARD:
            case DISCOVER:
            case VISA:
            case UNKNOWN:
                digitsInCard = 16;
                break;
            case AMEX:
                digitsInCard = 15;
                break;
            default:
                digitsInCard = -1;
                Log.e(TAG, "Error: Invalid CreditCardCompanyType used for credit card digit verification. Returning false.");
        }
        return creditCardNumber.length() == digitsInCard;
    }

    /**
     * Checks the enum and the CVV string to see if the CCV has the correct number of digits. For example AMEX has only 4 digit CCVs while others can have 3 digits.
     *
     * @param creditCardCompanyType The {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType} you are checking against.
     * @param cvvNumber             The String version of the CCV you are checking. If null, empty, blank, or not all numbers is passed in, it is always false.
     * @return True if the CCV has the correct number of digits for the {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType}
     */
    public static boolean isValidNumberOfCVVDigits(final CreditCardCompanyType creditCardCompanyType, final String cvvNumber) {
        if (cvvNumber == null || TextUtils.isEmpty(cvvNumber.trim()) || !TextUtils.isDigitsOnly(cvvNumber)) {
            Log.w(TAG, "Error: Invalid input given to isValidNumberOfCreditCardDigits. Returning false. creditCardCompanyType:" +
                    creditCardCompanyType.name() + " cvvNumber:" + cvvNumber);
            return false;
        }
        final int digitsInCVV;
        switch (creditCardCompanyType) {
            case JCB:
            case MASTERCARD:
            case DISCOVER:
            case VISA:
                digitsInCVV = 3;
                break;
            case AMEX:
            case UNKNOWN:
                digitsInCVV = 4;
                break;
            default:
                digitsInCVV = -1;
                Log.e(TAG, "Error: Invalid CreditCardCompanyType used for CCV. Returning false.");
        }
        return cvvNumber.length() == digitsInCVV;
    }

    /**
     * Checks to see if the month and 2000+lastTwoDigitsInYear is expired based off of the date the JVM tells us. This is done by taking the expiration date and setting it
     * to the first moment in the next month as it is month precise.
     *
     * @param month               The integer version of month(1-12). Other numbers are considered invalid and will make the method return true.
     * @param lastTwoDigitsInYear The integer version of the last two digits of the year (00-99 00=0 01=1, and etc.). Other numbers are considered invalid and will make the method return true.
     * @return True if any of the input is invalid or if the date is expired.
     */
    public static boolean isExpired(final int month, final int lastTwoDigitsInYear) {
        if (month < 1 || month > 12 || lastTwoDigitsInYear < 0 || lastTwoDigitsInYear > 99) {
            //Instead of throwing an IllegalArgumentException, we just return true indicating that the value is not good. As this is a validator and not a data-model.
            return true;
        }
        //We check to see if it is before the first day hour, minute, second of next month. If so, it is still valid
        final GregorianCalendar expirationDate = new GregorianCalendar();
        expirationDate.set(2000 + lastTwoDigitsInYear, month, 1, 0, 0, 0);
        expirationDate.add(GregorianCalendar.MONTH, 1);
        //Consideration: Grab the date from a time server as the JVM's concept of time can be out of sync with reality. As in, the user does not have it
        //auto-updating or just sets it incorrectly.
        final GregorianCalendar today = new GregorianCalendar();
        today.setTime(new Date());
        return today.before(expirationDate);
    }
}

