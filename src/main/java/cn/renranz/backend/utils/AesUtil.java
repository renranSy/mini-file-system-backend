package cn.renranz.backend.utils;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * AES加密方式算法工具类
 */
public class AesUtil {
    /**
     * KEY 随机的后续可更改
     */
    private static final byte[] key = "chenziyi5201314.".getBytes(StandardCharsets.UTF_8);
    /**
     * 初始化加密(默认的AES加密方式)
     */
    private static final SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

    /**
     * 加密
     *
     * @param str 加密之前的字符串
     */
    public static String encryptHex(String str) {
        return aes.encryptHex(str);
    }

    /**
     * 解密
     *
     * @param str 加密后的字符串
     */
    public static String decryptStr(String str) {
        return aes.decryptStr(str);
    }

    /**
     * 加密文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void encryptFile(File sourceFile, File targetFile) {
        byte[] fileBytes = FileUtil.readBytes(sourceFile);
        byte[] encryptedBytes = aes.encrypt(fileBytes);
        FileUtil.writeBytes(encryptedBytes, targetFile);
    }

    /**
     * 解密文件
     *
     * @param sourceFile 加密的源文件
     * @param targetFile 解密后的目标文件
     */
    public static void decryptFile(File sourceFile, File targetFile) {
        byte[] encryptedBytes = FileUtil.readBytes(sourceFile);
        byte[] decryptedBytes = aes.decrypt(encryptedBytes);
        FileUtil.writeBytes(decryptedBytes, targetFile);
    }
}

