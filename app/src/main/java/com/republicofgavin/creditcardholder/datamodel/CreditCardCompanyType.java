package com.republicofgavin.creditcardholder.datamodel;

/**
 * Enum for credit card companies. Contains number of digits in credit card and CVV.
 *
 * @author Gavin McNeely
 */
public enum CreditCardCompanyType {
    UNKNOWN(Integer.MAX_VALUE, Integer.MAX_VALUE),//TODO Check to see if we really need this.
    AMEX(15, 4),
    MASTERCARD(16, 3),
    VISA(16, 3),
    DISCOVER(16, 3),
    JCB(16, 3);

    private final int numberOfDigitsInCreditCard;
    private final int numberOfDigitsInCVV;

    private CreditCardCompanyType(final int numberOfDigitsInCreditCard, final int numberOfDigitsInCVV) {
        this.numberOfDigitsInCreditCard = numberOfDigitsInCreditCard;
        this.numberOfDigitsInCVV = numberOfDigitsInCVV;
    }

    /**
     * Returns the number of digits in the credit card number for the particular {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType}. Usually 15 or 16.
     *
     * @return The number of digits in the credit card number (15 for American Express, 16 for: Mastercard, Visa, Discover, and JCB).
     */
    public int getNumberOfDigitsInCreditCard() {
        return numberOfDigitsInCreditCard;
    }

    /**
     * Returns the number of digits in the CVV for the particular {@link com.republicofgavin.creditcardholder.datamodel.CreditCardCompanyType}. Usually 3 or 4 digits.
     *
     * @return The number of digits in the CVV (4 for American Express, 3 for: Mastercard, Visa, Discover, and JCB).
     */
    public int getNumberOfDigitsInCVV() {
        return numberOfDigitsInCVV;
    }
}
