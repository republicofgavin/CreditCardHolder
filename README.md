# Credit Card Holder

An app designed to store credit card data (credit card type, credit card number, CVV number, and expiration date).

## User Experience

Upon entering the app, the user will be brought to a screen that has credit card data input fields. While
the user inputs data, it will be validated and a message will be displayed detailing if the input has any problems.
Example: American Express credit card CVVs require 4 digits.
If the user clicks the submit button while invalid(incomplete or incorrect) data is in the fields, a dialog will appear
listing all the problems currently wrong with the input. Once all input data is valid and the user clicks the submit button,
a toast will display saying, "Success!" and the screen will close.

## Functional Requirements

1. The system shall display the correct/chosen credit card logo on the screen. With a generic card being shown when the field is empty.
2. The system shall accept a valid credit card number. Valid meaning:

    a. Passes Luhn validation (http://en.wikipedia.org/wiki/Luhn_algorithm)

    b. A 15 digit number for American Express cards.

    c. A 16 digit number for: MasterCard, Visa, Discover, and JCB cards.

3. The system shall accept a non-expired credit card expiration date(format MM/YY).
4. The system shall accept a valid CVV(Card Verification Value). Valid meaning:

    a. A 4 digit number for American Express cards.

    b. A 3 digit number for: MasterCard, Visa, Discover, and JCB cards.

5. The system shall display a CVV hint icon indicating where the user can find the CVV code on his/her card.
6. The system shall display a submit button for the user to click. The submit button shall:

    a. Perform validation of all of the above fields.

    b. If all data is valid show a "Success!" message.

    c. If any data is invalid display a dialog indicating what is wrong.
## Hazard Analysis

    Although the app won't store credit card data at this time, it is important to make sure no one could read
    credit card data from the screen. In the case the user pressed the home button during input and another person got a hold
    of the phone and went back to the app for example. This will be mitigated with Android functionality that
hides input once it has been entered(functionality commonly used for passwords).

## Tech Design and Developer Notes

### App API Level

Because the current functionality of the app does not require abilities (hardware or software) of high level APIs, it was decided
    that a lower API level should be chosen to allow more people to benefit from the app. Froyo and higher is estimated to be 99.5%
    of the current share of phones in used in the Google Play Store. Therefore, Froyo was chosen as the min API level.

### Testing strategy

The app shall use Robolectric(http://robolectric.org/) as its primary testing tool. This allows the app to be tested in a modular
and white-box fashion. Also, because the tool does not require an emulator, code coverage tools such as
JaCoCo(http://www.eclemma.org/jacoco/) can be used to determine the app's code coverage percentage. To supplement Roboletric, the
app will also be integration tested by the developer.

### Non-UI implementation design

The app will have a class (CreditCardValidator) whose purpose is to take in various combinations of credit card parameters and validate
them. This approach was chosen due to:

1. We have no need for a CreditCard data-model at this time as we do not need to store credit card data or pass it around the app as a whole.
2. To keep separation of concerns between the UI code and the actual credit card validation algorithms. Thus reducing "code clutter."
3. Allow future consumers, a CreditCard data-model for example, to be able to use the same credit card validation logic.

### UI implementation design

The app will use an AddCreditCardFragment(http://developer.android.com/guide/components/fragments.html) to hold all of the input UI. This
will make the UI more portable and reusable.