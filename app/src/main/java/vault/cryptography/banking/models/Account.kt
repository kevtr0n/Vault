package vault.cryptography.banking.models

class Account () {

  private var accountNum: String? = null
  private var accountType: AccountType? = null
  private var accountBal: Double? = null

  constructor(accountNum: String, accountType: AccountType, accountBal: Double): this() {
    this.accountNum = accountNum
    this.accountBal = accountBal
    this.accountType = accountType
  }

  // Getters
  fun getAccountNumber(): String? {return this.accountNum}
  fun getAccountType(): AccountType? {return this.accountType}
  fun getAccountBalance(): Double? {return this.accountBal}

  // Setters
  fun setAccountNumber(number: String) {this.accountNum = number}
  fun setAccountType(type: AccountType) {this.accountType = type}
  fun setAccountBalance(amount: Double) {this.accountBal = amount}
}