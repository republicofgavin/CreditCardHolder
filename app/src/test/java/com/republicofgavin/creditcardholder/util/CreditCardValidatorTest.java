package com.republicofgavin.creditcardholder.util;

import com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Tests {@link com.republicofgavin.creditcardholder.util.CreditCardValidator}
 * @author Gavin McNeely
 */
@RunWith(RobolectricTestRunner.class)
public class CreditCardValidatorTest {

    private final String validSixteenDigitCCNumber = "3379513561108795";
    //Test input from email
    private final String validAMEXCCNumber = "371449635398431";
    private final String validDiscoverCCNumber = "6011111111111117";
    private final String validJCBCCNumber = "3530111333300000";
    private final String validMasterCardCCNumber = "5555555555554444";
    private final String validVisaCCNumber = "4111111111111111";

    private final String invalidCCNumberSpace = "33795135611 08795";
    private final String invalidCCNumberNegative = "3379513561-108795";
    private final String invalidSixteenDigitCCNumber = "3379513561108791";
    private final String invalidFifteenDigitCCNumber = "337951356118791";

    private final String invalidCCVNumberSpace = "4 44";
    private final String invalidCCVNumberNegative = "44-4";

    @Test
    public void testIsLuhnValidated() {
        Assert.assertFalse("null case was true", CreditCardValidator.isLuhnValidated(null));
        Assert.assertFalse("empty case was true", CreditCardValidator.isLuhnValidated(""));
        Assert.assertFalse("blank case was true", CreditCardValidator.isLuhnValidated(" "));
        Assert.assertFalse("space in number case was true", CreditCardValidator.isLuhnValidated(invalidCCNumberSpace));
        Assert.assertFalse("negative in number case was true", CreditCardValidator.isLuhnValidated(invalidCCNumberNegative));
        Assert.assertFalse("non-zero check 16 digit sum case was true", CreditCardValidator.isLuhnValidated(invalidSixteenDigitCCNumber));
        Assert.assertFalse("non-zero check 15 digit sum case was true", CreditCardValidator.isLuhnValidated(invalidFifteenDigitCCNumber));

        Assert.assertTrue("generic 16 digit case was false", CreditCardValidator.isLuhnValidated(validSixteenDigitCCNumber));
        Assert.assertTrue("valid AMEX case was false", CreditCardValidator.isLuhnValidated(validAMEXCCNumber));
        Assert.assertTrue("valid Discover case was false", CreditCardValidator.isLuhnValidated(validDiscoverCCNumber));
        Assert.assertTrue("valid JCB case was false", CreditCardValidator.isLuhnValidated(validJCBCCNumber));
        Assert.assertTrue("valid MasterCard case was false", CreditCardValidator.isLuhnValidated(validMasterCardCCNumber));
        Assert.assertTrue("valid Visa case was false", CreditCardValidator.isLuhnValidated(validVisaCCNumber));
    }

    @Test
    public void testIsValidNumberOfCreditCardDigits() {
        Assert.assertFalse("null case was true", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.AMEX, null));
        Assert.assertFalse("empty case was true", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.DISCOVER, ""));
        Assert.assertFalse("blank case was true", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.JCB, " "));
        Assert.assertFalse("space in number case was true", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.MASTERCARD, invalidCCNumberSpace));
        Assert.assertFalse("negative in number case was true", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.UNKNOWN, invalidCCNumberNegative));

        Assert.assertTrue("AMEX case was false", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.AMEX, validAMEXCCNumber));
        Assert.assertTrue("Discover case was false", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.DISCOVER, validDiscoverCCNumber));
        Assert.assertTrue("JCB case was false", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.JCB, validJCBCCNumber));
        Assert.assertTrue("MasterCard case was false", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.MASTERCARD, validMasterCardCCNumber));
        Assert.assertTrue("VISA case was false", CreditCardValidator.isValidNumberOfCreditCardDigits(CreditCardCompanyType.VISA, validVisaCCNumber));
    }

    @Test
    public void testIsValidNumberOfCCVDigits() {
        Assert.assertFalse("null CVV case was true", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.AMEX, null));
        Assert.assertFalse("empty CVV case was true", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.DISCOVER, ""));
        Assert.assertFalse("blank CVV case was true", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.JCB, " "));
        Assert.assertFalse("space in number CVV case was true", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.MASTERCARD, invalidCCVNumberSpace));
        Assert.assertFalse("negative in number CVV case was true", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.UNKNOWN, invalidCCVNumberNegative));

        Assert.assertTrue("AMEX CVV case was false", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.AMEX, "4444"));
        Assert.assertTrue("Discover CCV case was false", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.DISCOVER, "444"));
        Assert.assertTrue("JCB CVV case was false", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.JCB, "444"));
        Assert.assertTrue("MasterCard CVV case was false", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.MASTERCARD, "444"));
        Assert.assertTrue("VISA CVV case was false", CreditCardValidator.isValidNumberOfCVVDigits(CreditCardCompanyType.VISA, "444"));
    }

    @Test
    public void testIsExpired() {
        Assert.assertTrue("negative month case was false", CreditCardValidator.isExpired(-1, 99));
        Assert.assertTrue("month overflow case was false", CreditCardValidator.isExpired(13, 99));
        Assert.assertTrue("negative year case was false", CreditCardValidator.isExpired(1, -1));
        Assert.assertTrue("year overflow case was false", CreditCardValidator.isExpired(1, 100));

        Assert.assertFalse("December 2099 case was false", CreditCardValidator.isExpired(12, 99));

        final GregorianCalendar lastMonth = new GregorianCalendar();
        lastMonth.setTime(new Date());
        lastMonth.add(GregorianCalendar.MONTH, -1);
        Assert.assertTrue("last month case was true", CreditCardValidator.isExpired(lastMonth.get(GregorianCalendar.MONTH), lastMonth.get(GregorianCalendar.YEAR) - 2000));

        final GregorianCalendar lastYear = new GregorianCalendar();
        lastYear.setTime(new Date());
        lastYear.add(GregorianCalendar.YEAR, -1);
        lastYear.add(GregorianCalendar.MONTH, 1);
        Assert.assertTrue("last year case was true", CreditCardValidator.isExpired(lastYear.get(GregorianCalendar.MONTH), lastYear.get(GregorianCalendar.YEAR) - 2000));

        final GregorianCalendar today = new GregorianCalendar();
        today.setTime(new Date());
        Assert.assertFalse("today case was false", CreditCardValidator.isExpired(today.get(GregorianCalendar.MONTH), today.get(GregorianCalendar.YEAR) - 2000));

        final GregorianCalendar nextYear = new GregorianCalendar();
        nextYear.setTime(new Date());
        lastYear.add(GregorianCalendar.YEAR, 1);
        lastYear.add(GregorianCalendar.MONTH, -1);
        Assert.assertFalse("nextYear case was false", CreditCardValidator.isExpired(nextYear.get(GregorianCalendar.MONTH), nextYear.get(GregorianCalendar.YEAR) - 2000));
    }

}
