package cc.ptt.android.common.security

interface AESKeyStoreHelper {
    fun encrypt(plainText: String?): String
    fun decrypt(encryptedText: String?): String
}
