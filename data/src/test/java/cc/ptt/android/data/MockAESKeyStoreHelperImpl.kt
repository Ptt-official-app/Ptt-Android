package cc.ptt.android.data

import cc.ptt.android.common.security.AESKeyStoreHelper

class MockAESKeyStoreHelperImpl : AESKeyStoreHelper {
    override fun encrypt(plainText: String?): String = plainText.orEmpty()

    override fun decrypt(encryptedText: String?): String = encryptedText.toString()
}
