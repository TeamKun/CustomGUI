package kotx.ktools

import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.Signature

fun String.sign(): Pair<ByteArray, PublicKey> {
    val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2024) }.generateKeyPair()
    val sign = Signature.getInstance("SHA256withRSA").apply {
        initSign(keyPair.private)
        update(encodeToByteArray())
    }.sign()

    return sign to keyPair.public
}

fun String.verifySign(sign: ByteArray, key: PublicKey) = Signature.getInstance("SHA256withRSA").apply {
    initVerify(key)
    update(encodeToByteArray())
}.verify(sign)