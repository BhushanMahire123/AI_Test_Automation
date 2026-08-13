# ============================================================
# Feature: TutorialsNinja Register
# ============================================================

@regression @register
Feature: TutorialsNinja Register Functionality
  As a new user
  I want to register on TutorialsNinja
  So that I can create my account

  @TC_REG_001 @smoke
Scenario Outline: Successful registration with valid details
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Register option", "Link", "Click", "NA", "HomePage", "registerOption"
    And "Verify register page displayed", "Label", "WaitForDisplay", "NA", "RegisterPage", "registerPageHeading"
    And "Enter first name", "TextField", "Type", "<firstName>", "RegisterPage", "firstNameInput"
    And "Enter last name", "TextField", "Type", "<lastName>", "RegisterPage", "lastNameInput"
    And "Enter email", "TextField", "Type", "<email>", "RegisterPage", "emailInput"
    And "Enter telephone", "TextField", "Type", "<telephone>", "RegisterPage", "telephoneInput"
    And "Enter password", "TextField", "Type", "<password>", "RegisterPage", "passwordInput"
    And "Enter confirm password", "TextField", "Type", "<confirmPassword>", "RegisterPage", "confirmPasswordInput"
    And "Select newsletter No", "RadioButton", "Click", "NA", "RegisterPage", "newsletterNoRadio"
    And "Click privacy policy checkbox", "Checkbox", "Click", "NA", "RegisterPage", "privacyPolicyCheckbox"
    And "Click Continue button", "Button", "Click", "NA", "RegisterPage", "continueButton"
    And "Verify success message shown", "Label", "WaitForDisplay", "NA", "RegisterPage", "successHeading"

Examples:
| firstName | lastName | email                    | telephone  | password    | confirmPassword |
| John      | Doe      | johndoe101@test.com      | 9876543210 | Test@1234   | Test@1234       |

  @TC_REG_002 @regression
Scenario Outline: Registration fails without privacy policy
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Register option", "Link", "Click", "NA", "HomePage", "registerOption"
    And "Verify register page displayed", "Label", "WaitForDisplay", "NA", "RegisterPage", "registerPageHeading"
    And "Enter first name", "TextField", "Type", "<firstName>", "RegisterPage", "firstNameInput"
    And "Enter last name", "TextField", "Type", "<lastName>", "RegisterPage", "lastNameInput"
    And "Enter email", "TextField", "Type", "<email>", "RegisterPage", "emailInput"
    And "Enter telephone", "TextField", "Type", "<telephone>", "RegisterPage", "telephoneInput"
    And "Enter password", "TextField", "Type", "<password>", "RegisterPage", "passwordInput"
    And "Enter confirm password", "TextField", "Type", "<confirmPassword>", "RegisterPage", "confirmPasswordInput"
    And "Click Continue without policy", "Button", "Click", "NA", "RegisterPage", "continueButton"
    And "Verify error message shown", "Label", "WaitForDisplay", "NA", "RegisterPage", "errorMessage"

Examples:
| firstName | lastName | email                    | telephone  | password   | confirmPassword |
| Jane      | Smith    | janesmith999@test.com    | 9876543211 | Test@1234  | Test@1234       |

  @TC_REG_003 @regression
  Scenario: Verify register page elements - Inline format
    When "Click My Account", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Register", "Link", "Click", "NA", "HomePage", "registerOption"
    And "Verify register page heading", "Label", "WaitForDisplay", "NA", "RegisterPage", "registerPageHeading"
    And "Verify first name field enabled", "TextField", "IsEnabled", "NA", "RegisterPage", "firstNameInput"
    And "Verify last name field enabled", "TextField", "IsEnabled", "NA", "RegisterPage", "lastNameInput"
    And "Verify email field enabled", "TextField", "IsEnabled", "NA", "RegisterPage", "emailInput"
    And "Verify continue button displayed", "Button", "IsDisplayed", "NA", "RegisterPage", "continueButton"

  @TC_REG_004 @regression
  Scenario: Registration fails when all fields empty - Inline format
    When "Click My Account", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Register", "Link", "Click", "NA", "HomePage", "registerOption"
    And "Verify register page", "Label", "WaitForDisplay", "NA", "RegisterPage", "registerPageHeading"
    And "Click Continue without filling", "Button", "Click", "NA", "RegisterPage", "continueButton"
    And "Verify error message shown", "Label", "WaitForDisplay", "NA", "RegisterPage", "errorMessage"

      @TC_REG_008
 Scenario Outline: Registration fails with existing email
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Register option", "Link", "Click", "NA", "HomePage", "registerOption"
    And "Verify register page displayed", "Label", "WaitForDisplay", "NA", "RegisterPage", "registerPageHeading"
    And "Enter first name", "TextField", "Type", "<firstName>", "RegisterPage", "firstNameInput"
    And "Enter last name", "TextField", "Type", "<lastName>", "RegisterPage", "lastNameInput"
    And "Enter existing email", "TextField", "Type", "<email>", "RegisterPage", "emailInput"
    And "Enter telephone", "TextField", "Type", "<telephone>", "RegisterPage", "telephoneInput"
    And "Enter password", "TextField", "Type", "<password>", "RegisterPage", "passwordInput"
    And "Enter confirm password", "TextField", "Type", "<confirmPassword>", "RegisterPage", "confirmPasswordInput"
    And "Click privacy policy checkbox", "Checkbox", "Click", "NA", "RegisterPage", "privacyPolicyCheckbox"
    And "Click Continue button", "Button", "Click", "NA", "RegisterPage", "continueButton"
    And "Verify existing email warning displayed", "Label", "WaitForDisplay", "NA", "RegisterPage", "errorMessage"

Examples:
| firstName | lastName | email                 | telephone  | password   | confirmPassword |
| John      | Doe      | test@gmail.com        | 9876543210 | Test@1234  | Test@1234       |