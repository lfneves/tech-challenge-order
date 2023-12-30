//package com.mvp.order.utils
//
//import org.springframework.stereotype.Component
//import java.security.MessageDigest
//import java.security.NoSuchAlgorithmException
//import java.security.SecureRandom
//
//@Component
//class Sha512PasswordEncoder: PasswordEncoder {
//    override fun encode(rawPassword: CharSequence): String {
//        return getSecurePassword(rawPassword.toString())!!
//    }
//
//    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
//        return getSecurePassword(rawPassword.toString()) == encodedPassword
//    }
//
//    private fun getSecurePassword(password: String): String? {
//        var generatedPassword: String? = null
//        try {
//            val md = MessageDigest.getInstance("SHA-512")
//            val bytes = md.digest(password.toByteArray())
//            val sb = StringBuilder()
//            for (i in bytes.indices) {
//                sb.append(((bytes[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
//            }
//            generatedPassword = sb.toString()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        }
//        return generatedPassword
//    }
//
//    @Throws(NoSuchAlgorithmException::class)
//    private fun getSecurePassword(): ByteArray {
//        val random = SecureRandom()
//        val salt = ByteArray(16)
//        random.nextBytes(salt)
//        return salt
//    }
//}