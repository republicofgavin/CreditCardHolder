package com.republicofgavin.creditcardholder.datamodel;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType}
 *
 * @author Gavin McNeely
 */
public class CreditCardCompanyTypeTest {

    @Test
    public void testDigitsInCreditCardNumber() {
        Assert.assertEquals("Number of digits for UNKNOWN credit card number is not max", Integer.MAX_VALUE, CreditCardCompanyType.UNKNOWN.getNumberOfDigitsInCreditCard());
        Assert.assertEquals("Number of digits for AMEX credit card number is not 15", 15, CreditCardCompanyType.AMEX.getNumberOfDigitsInCreditCard());
        Assert.assertEquals("Number of digits for VISA credit card number is not 16", 16, CreditCardCompanyType.VISA.getNumberOfDigitsInCreditCard());
        Assert.assertEquals("Number of digits for Discover credit card number is not 16", 16, CreditCardCompanyType.DISCOVER.getNumberOfDigitsInCreditCard());
        Assert.assertEquals("Number of digits for JCB credit card number is not 16", 16, CreditCardCompanyType.JCB.getNumberOfDigitsInCreditCard());
        Assert.assertEquals("Number of digits for Mastercard credit card number is not 16", 16, CreditCardCompanyType.MASTERCARD.getNumberOfDigitsInCreditCard());
    }

    @Test
    public void testDigitsInCVV() {
        Assert.assertEquals("Number of digits for UNKNOWN CVV is not max", Integer.MAX_VALUE, CreditCardCompanyType.UNKNOWN.getNumberOfDigitsInCVV());
        Assert.assertEquals("Number of digits for AMEX CVV is not 4", 4, CreditCardCompanyType.AMEX.getNumberOfDigitsInCVV());
        Assert.assertEquals("Number of digits for VISA CVV is not 3", 3, CreditCardCompanyType.VISA.getNumberOfDigitsInCVV());
        Assert.assertEquals("Number of digits for Discover CVV is not 3", 3, CreditCardCompanyType.DISCOVER.getNumberOfDigitsInCVV());
        Assert.assertEquals("Number of digits for JCB CVV is not 3", 3, CreditCardCompanyType.JCB.getNumberOfDigitsInCVV());
        Assert.assertEquals("Number of digits for Mastercard CVV is not 3", 3, CreditCardCompanyType.MASTERCARD.getNumberOfDigitsInCVV());
    }
}
