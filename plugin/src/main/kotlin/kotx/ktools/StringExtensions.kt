package kotx.ktools

import java.security.MessageDigest
import java.text.Normalizer
import java.util.*


fun String.normalize(form: Normalizer.Form = Normalizer.Form.NFKC) = Normalizer.normalize(this, form)!!
fun <T> T.toString(block: T.() -> String) = block()
fun <T> T.toStringRun(block: (T) -> String) = block(this)
fun String.getDigest(algorithm: HashAlgorithms) = MessageDigest.getInstance(algorithm.type)
    .digest(toByteArray())
    .joinToString(separator = "") {
        "%02x".format(it)
    }

fun String.sha256() = getDigest(HashAlgorithms.SHA256)
fun String.sha512() = getDigest(HashAlgorithms.SHA512)

fun randomUUID() = UUID.randomUUID().toString().replace("-", "")

enum class HashAlgorithms(val type: String) {
    SHA256("SHA-256"), SHA512("SHA-512")
}