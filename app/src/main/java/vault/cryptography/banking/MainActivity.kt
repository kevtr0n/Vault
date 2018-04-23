/**
 * file: MainActivity.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The main class that houses the code
 * for the Main Activity.
 */
package vault.cryptography.banking

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import vault.cryptography.R
import vault.cryptography.banking.models.AccountType
import vault.cryptography.login.LoginActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    populateAccounts()

    checkingLayout.visibility = View.GONE
    savingsLayout.visibility = View.GONE

    logoutButton.setOnClickListener { logout() }
    checkingLayout.setOnClickListener {  }
    savingsLayout.setOnClickListener {  }
  }

  private fun populateAccounts() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val accounts: DatabaseReference = database.getReference("accounts")
    val userID: String = Core.getInstance().user.userID

    val query = accounts.orderByChild("userID").equalTo(userID).limitToFirst(10)

    query.addValueEventListener(object : ValueEventListener {

      override fun onCancelled(error: DatabaseError?) {
        Log.d("Error: ", error.toString())
      }

      override fun onDataChange(snapshot: DataSnapshot?) {

        for (account in snapshot!!.children) {
          if (account.child("accountType").value.toString() == AccountType.CHECKING.toString()) {
            checkingLayout.visibility = View.VISIBLE
            checkingAccNumVal.text = account.child("accountNum").value.toString()
            checkingAccBalVal.text = account.child("accountBal").value.toString()
            if (checkingAccBalVal.text.toString().toDouble() >= 0)
              checkingAccStandVal.text = "Good Standing"
            else
              checkingAccStandVal.text = "Delinquent"
          }

          if (account.child("accountType").value.toString() == AccountType.SAVINGS.toString()) {
            savingsLayout.visibility = View.VISIBLE
            savingsAccNumVal.text = account.child("accountNum").value.toString()
            savingsAccBalVal.text = account.child("accountBal").value.toString()
            if (savingsAccBalVal.text.toString().toDouble() >= 0)
              savingAccStandVal.text = "Good Standing"
            else
              savingAccStandVal.text = "Delinquent"
          }
        }
      }
    })
  }

  private fun logout() {
    val intent = Intent(MainActivity@this, LoginActivity::class.java)
    Core.getInstance().clearUser()
    startActivity(intent)
    finish()
  }

  private fun viewTransaction() {

  }
}
