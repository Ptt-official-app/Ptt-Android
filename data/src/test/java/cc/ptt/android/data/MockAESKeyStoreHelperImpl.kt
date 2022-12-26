package cc.ptt.android.data

import cc.ptt.android.common.security.AESKeyStoreHelper

class MockAESKeyStoreHelperImpl : AESKeyStoreHelper {

    override fun encrypt(plainText: String?): String {
        return plainText.orEmpty()
    }

    override fun decrypt(encryptedText: String?): String {
        return encryptedText.toString()
    }
}
