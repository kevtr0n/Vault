/**
 * file: Core.java
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * Singleton class used for housing static
 * variables across all activities.
 */

package vault.cryptography.banking;

import vault.cryptography.banking.models.Account;
import vault.cryptography.banking.models.User;

public class Core {

  private static Core core_instance = null;

  private Core() {

  }

  // Instance variables:
  private User user = null;
  private String email = null;
  private Account loan = null;
  private Account savings = null;
  private Account checking = null;
  private Account moneyMarket = null;

  /**
   * Returns the instance of the singleton class
   * @return the singleton
   */
  public static Core getInstance() {
    if (core_instance == null)
      core_instance = new Core();

    return core_instance;
  }

  public void clearUser() {
    this.user = null;
  }

  // Getters and Setters:
  public void setUser(User user) {
    this.user = user;
  }
  public User getUser() {
    return this.user;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getEmail() {
    return this.email;
  }
  public void setLoan(Account loan) {
    this.loan = loan;
  }
  public Account getLoan() {
    return this.loan;
  }
  public void setSavings(Account savings) {
    this.savings = savings;
  }
  public Account getSavings() {
    return this.savings;
  }
  public void setChecking(Account checking) {
    this.checking = checking;
  }
  public Account getChecking() {
    return checking;
  }
  public void setMoneyMarket(Account moneyMarket) {
    this.moneyMarket = moneyMarket;
  }
  public Account getMoneyMarket() {
    return moneyMarket;
  }
}
