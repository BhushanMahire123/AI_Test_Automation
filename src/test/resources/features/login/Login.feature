# ============================================================
# Feature: TutorialsNinja Login
# ============================================================

@regression @login
 Feature: TutorialsNinja Login Functionality
  As a registered user
  I want to login to TutorialsNinja
  So that I can access my account

  # ── DataTable Format ──────────────────────────────────────

  @TC_LOGIN_001 @smoke
 Scenario Outline: Successful login with valid credentials
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Login option", "Link", "Click", "NA", "HomePage", "loginOption"
    And "Verify login page displayed", "Label", "WaitForDisplay", "NA", "LoginPage", "loginPageHeading"
    And "Enter email address", "TextField", "Type", "<email>", "LoginPage", "emailInput"
    And "Enter password", "TextField", "Type", "<password>", "LoginPage", "passwordInput"
    And "Click Login button", "Button", "Click", "NA", "LoginPage", "loginButton"
    And "Verify account page shown", "Label", "WaitForDisplay", "NA", "AccountPage", "accountPageHeading"

Examples:
| email                 | password    |
| test@gmail.com        | Test@1234   |
    
    
  @TC_LOGIN_002 @regression
  Scenario Outline: Login fails with invalid credentials
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Login option", "Link", "Click", "NA", "HomePage", "loginOption"
    And "Enter invalid email", "TextField", "Type", "<email>", "LoginPage", "emailInput"
    And "Enter wrong password", "TextField", "Type", "<password>", "LoginPage", "passwordInput"
    And "Click Login button", "Button", "Click", "NA", "LoginPage", "loginButton"
    And "Verify error message shown", "Label", "WaitForDisplay", "NA", "LoginPage", "errorMessage"

Examples:
| email                    | password        |
| invalid@test.com         | wrongpassword   |

  # ── Inline Step Format ────────────────────────────────────

  @TC_LOGIN_003 @smoke
  Scenario: Verify login page elements - Inline format
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Login option", "Link", "Click", "NA", "HomePage", "loginOption"
    And "Verify login page heading", "Label", "WaitForDisplay", "NA", "LoginPage", "loginPageHeading"
    And "Verify email field enabled", "TextField", "IsEnabled", "NA", "LoginPage", "emailInput"
    And "Verify password field enabled", "TextField", "IsEnabled", "NA", "LoginPage", "passwordInput"
    And "Verify login button displayed", "Button", "IsDisplayed", "NA", "LoginPage", "loginButton"

  @TC_LOGIN_004 @regression
  Scenario: Login with empty credentials - Inline format
    When "Click My Account", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Login", "Link", "Click", "NA", "HomePage", "loginOption"
    And "Leave email blank", "TextField", "Type", "", "LoginPage", "emailInput"
    And "Leave password blank", "TextField", "Type", "", "LoginPage", "passwordInput"
    And "Click Login button", "Button", "Click", "NA", "LoginPage", "loginButton"
    And "Verify error is shown", "Label", "WaitForDisplay", "NA", "LoginPage", "errorMessage"

  @TC_LOGIN_005 @regression
  Scenario: Get login button text - GetText action
    When "Click My Account", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Login option", "Link", "Click", "NA", "HomePage", "loginOption"
    And "Verify login page", "Label", "WaitForDisplay", "NA", "LoginPage", "loginPageHeading"
    And "Get login button text", "Button", "GetText", "NA", "LoginPage", "loginButton"
    Then the result should contain "Login"

     @TC_LOGIN_006
    Scenario Outline: Login with empty password
    When "Click My Account dropdown", "Link", "Click", "NA", "HomePage", "myAccountDropdown"
    And "Click Login option", "Link", "Click", "NA", "HomePage", "loginOption"
    And "Verify login page displayed", "Label", "WaitForDisplay", "NA", "LoginPage", "loginPageHeading"
    And "Enter email address", "TextField", "Type", "<email>", "LoginPage", "emailInput"
    And "Click Login button", "Button", "Click", "NA", "LoginPage", "loginButton"
    And "Verify warning message displayed", "Label", "WaitForDisplay", "NA", "LoginPage", "errorMessage"

Examples:
| email              |
| test@gmail.com     |

    