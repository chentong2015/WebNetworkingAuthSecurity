package EncryptDecrypt;

// 加密算法总结：
// Hash加密: MD5, SHA-1
// 对称加密：AES(128 & 256), DES(已被破解)
// 非对称加密: RSA, DH
// 原则：
// 以上的加密方式(计算逻辑)，可以混合使用
// 非对称加密中生成一对密钥，公开其中的一部分，私钥一定在自己的手中
public class BaseEncryptDecrypt {

    // 三种模型：
    // 身份认证模型 Authentification
    // 加密算法模型 Chiffrement
    // 签名模型    Signature (私钥加密，公钥解)
}
