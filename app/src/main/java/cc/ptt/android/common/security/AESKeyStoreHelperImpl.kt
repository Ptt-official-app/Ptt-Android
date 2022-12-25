package cc.ptt.android.common.security

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import cc.ptt.android.common.utils.log
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(api = Build.VERSION_CODES.M)
class AESKeyStoreHelperImpl : AESKeyStoreHelper {

    companion object {
        private val TAG = AESKeyStoreHelper::class.java.simpleName
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val AES_MODE = "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}" // "AES/GCM/NoPadding"
        private const val KEYSTORE_ALIAS_AES = "KEYSTORE_AES"
        private const val SEPARATOR = "\u0000"
    }

    private var keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)

    init {
        keyStore.load(null)
        if (!keyStore.containsAlias(KEYSTORE_ALIAS_AES)) {
            generateKey()
        }
    }

    @Throws(Exception::class)
    private fun generateKey() {
        val keyGenerator = KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEYSTORE_ALIAS_AES,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setKeySize(256)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    @Throws(Exception::class)
    private fun getAESKey(): SecretKey? {
        val secretKeyEntry = keyStore.getEntry(KEYSTORE_ALIAS_AES, null) as KeyStore.SecretKeyEntry
        // if no key was found -> generate new
        return secretKeyEntry.secretKey
    }

    override fun encrypt(plainText: String?): String {
        return plainText?.let {
            try {
                encryptAES(it)
            } catch (e: Exception) {
                log(TAG, "encrypt error: $e")
                ""
            }
        } ?: ""
    }

    override fun decrypt(encryptedText: String?): String {
        return encryptedText?.let {
            try {
                decryptAES(it)
            } catch (e: Exception) {
                log(TAG, "encrypt error: $e")
                ""
            }
        } ?: ""
    }

    @Throws(Exception::class)
    private fun encryptAES(toEncrypt: String): String? {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, getAESKey())
        val iv = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
        val encrypted = Base64.encodeToString(
            cipher.doFinal(toEncrypt.toByteArray(StandardCharsets.UTF_8)),
            Base64.DEFAULT
        )
        val out = encrypted + SEPARATOR + iv
        return String(
            Base64.encode(out.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT),
            StandardCharsets.UTF_8
        )
    }

    @Throws(Exception::class)
    private fun decryptAES(toDecrypt: String): String? {
        val toDecrypt64 = Base64.decode(toDecrypt, Base64.DEFAULT)
        val parts =
            String(toDecrypt64, StandardCharsets.UTF_8).split(SEPARATOR.toRegex()).toTypedArray()
        if (parts.size != 2) throw Exception("String to decrypt must be of the form: 'BASE64_DATA" + SEPARATOR + "BASE64_IV'")
        val encrypted = Base64.decode(parts[0], Base64.DEFAULT)
        val iv = Base64.decode(parts[1], Base64.DEFAULT)
        val cipher = Cipher.getInstance(AES_MODE)
        val spec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, getAESKey(), spec)
        return String(cipher.doFinal(encrypted), StandardCharsets.UTF_8)
    }
}
