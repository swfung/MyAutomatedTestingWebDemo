@BC-Web
Feature: BC Web Login testing
  Description: BC Web login automated regression test

  @BC-Web-001 @BC-Web @login @Regression @Web
  Scenario: BC-Web-001 Try login function
    Given Open BC login page
    Then User is on 'BC home' page
    And User clicks 'Login/Register'
    And User inputs 'Mobile Number'
    And User inputs 'Password'
    And User clicks 'Login'
    Then User can see '套票及積分 & Logout'