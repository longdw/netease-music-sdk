package com.smart.neteasemusic.sdk.base.oauth

import android.util.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

/**
 * 接口鉴权
 * @author david.long 2026/1/9 16:35
 */
object SignUtils {
    /**
     * 获取所有请求参数，不包括字节类型参数，如文件、字节流，剔除sign字段，剔除值为空的参数，
     * 并按照第一个字符的键值ASCII码递增排序（字母升序排序），如果遇到相同字符则按照第二个字符的键值ASCII码递增排序，
     * 以此类推
     *
     * 将排序后的参数与其对应值，组合成“参数=参数值”的格式，并且把这些参数用&字符连接起来，此时生成的字符串为待签名字符串。
     *
     * @param params 请求参数
     * @return 待签名字符串
     */
    private fun getSignCheckContent(params: MutableMap<String, String?>?): String? {
        if (params.isNullOrEmpty()) {
            return null
        }
        // 删除sign参数，防止入参sign污染后续的加签流程
        params.remove("sign")
        val content = StringBuffer()
        val keys: List<String> = ArrayList(params.keys)
        val sortedKeys = keys.sorted()
        for (i in sortedKeys.indices) {
            val key = sortedKeys[i]
            val value = params[key]
            content.append((if (i == 0) "" else "&") + key + "=" + value)
        }
        return content.toString()
    }

    /**
     * 使用各自语言对应的SHA256WithRSA签名函数利用商户私钥对待签名字符串进行签名
     * 并进行Base64编码并且需要encode
     *
     * @param content 待签名字符串
     * @param privateKey RSA私钥
     * @return 生成的签名
     */
    private fun rsa256Sign(content: String?, privateKey: String): String? {
        return try {
            val priKey = getPrivateKeyFromPKCS8(key = privateKey)
            val signature = Signature.getInstance("SHA256WithRSA")
            signature.initSign(priKey)
            signature.update(content?.toByteArray())
            val signed = signature.sign()
            Base64.encodeToString(signed, Base64.DEFAULT)
        } catch (e: Exception) {
            throw RuntimeException("RSA content = $content", e)
        }
    }

    /**
     * 获取私钥对象
     *
     * @param algorithm 签名算法
     * @param key 私钥字符串
     * @return 私钥对象
     */
    private fun getPrivateKeyFromPKCS8(algorithm: String? = "RSA", key: String?): PrivateKey? {
        if (algorithm.isNullOrBlank() || key.isNullOrBlank()) {
            return null
        }
        val keyFactory = KeyFactory.getInstance(algorithm)
        val encodedKey = Base64.decode(key, Base64.DEFAULT)
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(encodedKey))
    }

    fun sign(params: MutableMap<String, String?>?): String {
        val signCheckContent = getSignCheckContent(params)
        return rsa256Sign(signCheckContent, OAuthConst.priKey) ?: ""
    }
}