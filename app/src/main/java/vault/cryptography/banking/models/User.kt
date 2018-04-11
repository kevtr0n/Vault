package vault.cryptography.banking.models

import java.util.*

class User() {

  private var firstName: String? = null
  private var birthDate: String? = null
  private var lastName: String? = null
  private var key: String? = null
  private var authenticationKey: String? = null
  private var email: String? = null
  private var accountNumbers = mutableListOf<String>()
  private var accounts = mutableListOf<Account>()

  /**
   * Constructor for creating a brand new
   * instance of the User class.
   */
  constructor(firstName: String, lastName: String, birthDate: String,
              email: String, authenticationKey: String): this() {
    this.firstName = firstName
    this.lastName = lastName
    this.birthDate = birthDate
    this.email = email
    this.authenticationKey = authenticationKey
  }

  /**
   * Generates a random 32 bit hexidecimal key.
   */
  public fun generateRandomKey() {
    val hex = "0123456789ABCDEF"
    val random = Random()
    val sb = StringBuilder()
    while (sb.length < 32)
      sb.append(hex[random.nextInt(16)])
    this.key = sb.toString()
  }

  /**
   * Adds a unique account to list of users accounts.
   * @param account the account to be added.
   * @return boolean whether account was added.
   */
  public fun addAccount(account: Account): Boolean {

    for (acc in getAccounts())
      if (acc.getAccountType() == account.getAccountType())
        return false

    this.accounts.add(account)
    this.accountNumbers.add(account.getAccountNumber()!!)
    return true
  }

  public fun getAccounts() = this.accounts
  public fun getAccountNumbers() = accountNumbers
  public fun getEmail() = this.email
  public fun getFirstName() = this.firstName
  public fun getLastName() = this.lastName
  public fun getAuthenticationKey() = this.authenticationKey
}