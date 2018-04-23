/**
 * file: Account.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * File contains code for creating the
 * data classes Account/AccountType.
 */
package vault.cryptography.banking.models

import android.util.Log
import com.google.firebase.database.*
import vault.cryptography.banking.Core

data class Account (var accountNum: String, var accountType: AccountType, var accountBal: Double) {

  /**
   * Pushes the account object to the database.
   * @return a boolean value based on success.
   */
  fun push(): Boolean {

    val mAccountNum = this.accountNum.toUpperCase()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val accounts: DatabaseReference = database.getReference("accounts")
    var success = false

    accounts.addValueEventListener(object : ValueEventListener{
      override fun onCancelled(error: DatabaseError?) {
        Log.d("Error: ", error.toString())
      }

      override fun onDataChange(snapshot: DataSnapshot?) {
        if (snapshot!!.hasChild(mAccountNum))
          success = true
      }
    })

    accounts.child(mAccountNum).setValue(this)
    return success
  }
}

enum class AccountType {
  SAVINGS, CHECKING
}