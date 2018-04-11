package vault.cryptography.banking

import vault.cryptography.banking.models.Account
import vault.cryptography.banking.models.AccountType
import vault.cryptography.banking.models.User

object Ledger {

  private var user: User? = null
  private var checking: Account? = null
  private var savings: Account? = null
  private var moneyMarket: Account? = null
  private var loan: Account? = null
  private var isAuth: Boolean? = null

  public fun getUser(): User? = this.user

  public fun setUser(user: User) {
    this.user = user
    this.isAuth = true
  }

  public fun isAuthentic() = this.isAuth

  private fun setAccounts(accounts: MutableList<Account>) {

    for (account in accounts)
      when (account.getAccountType()) {
        AccountType.MONEY_MARKET -> this.moneyMarket = account
        AccountType.CHECKING -> this.checking = account
        AccountType.SAVINGS -> this.savings = account
        AccountType.LOAN -> this.loan = account
    }
  }
}