/**
 * file: AESCipher.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Lab 04
 * due date: April 4th, 2018
 * version: 1.0
 * Converts a 32 bit hexidecimal String
 * to 11 secure 32 bit hexidecimal Strings.
 */
package cryptography.com.vault.encryption

import encryption.aes.SBox

class AESCipher {

  /**
   * This method takes 32 bit hex key and generates
   * eleven secure 32 bit hex keys.
   *
   * @param keyHex the 32 bit hex key.
   * @return the String array containing the keys.
   */
  fun aesRoundKeys(keyHex: String): MutableList<String> {

    // Insure key is of length 32:
    if (keyHex.length != 32)
      return mutableListOf("Incorrect String length")

    // Groups chars into pairs:
    val hexValues: MutableList<String> = charsToHexPairs(keyHex.toMutableList())

    // Create 44x4 matrix:
    val w: MutableList<MutableList<String>> = mutableListOf()

    // Populate matrix with hex bytes:
    while (!hexValues.isEmpty()) {
      val column: MutableList<String> = mutableListOf()
      for (index in 0..3)
        column.add(hexValues.removeAt(0))
      w.add(column)
    }

    // Generate each new column:
    for (j in 4..43) {
      var temp: MutableList<String> = mutableListOf()

      // If the column is a multiple of 4, assign j to XOR of columns j - 1 and j - 4
      if (j % 4 != 0)
        for (index in 0..3)
          temp.add(exclusiveOr(w[j - 4][index], w[j - 1][index]))

      // Otherwise, start a new round:
      else {

        // SBox for hex string substitution:
        val sBox = SBox()

        // AES Rcon Hex String:
        val rCon = aesRcon(j / 4)

        // Construct new list with elements of previous column:
        for (index in 0..3)
          temp.add(w[j - 1][index])

        // Perform a shift on the new array:
        temp = shift(temp)

        // Transform each byte using the SBox:
        for (index in 0..3)
          temp[index] = sBox.convert(temp[index])

        // Perform XOR against the RCon and the first element on the top of the column:
        temp[0] = exclusiveOr(rCon, temp[0])

        // Set column to the contents of the new temp array XOR with column j - 4
        for (index in 0..3)
          temp[index] = exclusiveOr(w[j - 4][index], temp[index])
      }

      // Add new column to the matrix:
      w.add(temp)
    }

    // Convert the 11 4x4 matrices in the 44x4 matrix into 11 secure keys:
    val roundKeys = mutableListOf<String>()
    for (key in 0..10) {
      val sb = StringBuilder()
      while (sb.length != 32) {
        val list = w.removeAt(0)
        for (hex in list)
          sb.append(hex)
      }
      roundKeys.add(sb.toString())
    }

    return roundKeys
  }

  /**
   * Converts a list of hex characters to a list
   * hex pairs.
   *
   * @param chars the list of chars
   * @return the list containing the pairs of hex chars.
   */
  private fun charsToHexPairs(chars: MutableList<Char>): MutableList<String> {

    val array = mutableListOf<String>()
    while (!chars.isEmpty()) {
      val sb = StringBuilder()
      sb.append(chars.removeAt(0))
      sb.append(chars.removeAt(0))
      array.add(sb.toString())
    }
    return array
  }

  /**
   * Method that simplifies the XOR process and
   * returns the formatted hex byte string.
   *
   * @param first the first hex byte.
   * @param second the second hex byte.
   * @return the formatted post XOR hex byte string.
   */
  private fun exclusiveOr(first: String, second: String): String {

    val sb = StringBuilder("0")
    val hex = Integer.toHexString(Integer.parseInt(first, 16) xor Integer.parseInt(second, 16))
    when (hex.length) {
      0 -> return sb.append("0").toString()
      1 -> return sb.append(hex).toString()
    }
    return hex
  }

  /**
   * Simplified method that returns the correct RCon
   * for the specified round in AES.
   *
   * @param round the current round in AES.
   * @return the RCon hex byte.
   */
  private fun aesRcon(round: Int): String {

    val roundConstants = mutableListOf(
            "8d", "01", "02", "04", "08", "10",
            "20", "40", "80", "1b", "36"
    )
    return roundConstants[round]
  }

  /**
   * Method that simplifies the shift done in AES rounds.
   *
   * @param strings the list of strings.
   * @return the list of strings shifted over by 1.
   */
  private fun shift(strings: MutableList<String>): MutableList<String> {

    return mutableListOf(strings[1], strings[2], strings[3], strings[0])
  }
}
