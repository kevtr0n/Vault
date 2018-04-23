/**
 * file: User.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The file that contains the code for
 * creating the data class User.
 */

package vault.cryptography.banking.models
import android.util.Log
import com.google.firebase.database.*
import java.util.*

data class User(var firstName: String, var lastName: String, var birthDate: String,
                var email: String, var key: String, var userID: String,
                var authenticationKey: String, var maidenName: String?, var firstJob: String?) {

  /**
   * Pushes the user object to the database.
   * @return a boolean value
   */
  fun push(): Boolean {

    val mEmail = this.email.replace("[@.]".toRegex(), "").toUpperCase()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val users: DatabaseReference = database.getReference("users")
    var success = false

    users.addValueEventListener(object : ValueEventListener{

      override fun onCancelled(error: DatabaseError?) {
        Log.d("Error: ", error.toString())
      }

      override fun onDataChange(snapshot: DataSnapshot?) {
        if (snapshot!!.hasChild(mEmail))
          success = true
      }
    })

    users.child(mEmail).setValue(this)
    return success
  }

  companion object {

    /**
     * Generates a random 32 bit hexidecimal key.
     */
    fun generateRandomKey(): String {
      val hex = "0123456789ABCDEF"
      val random = Random()
      val sb = StringBuilder()
      while (sb.length < 32)
        sb.append(hex[random.nextInt(16)])
      return sb.toString()
    }
  }
}