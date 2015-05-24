package com.icodeyou.securechat.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * DH 交换密钥工具类
 */
public class DHUtil {

    public static final String PUBLIC_KEY = "DHPublicKey";
    public static final String PRIVATE_KEY = "DHPrivateKey";

    private static DHUtil mInstance = null;

    private DHUtil(){}

    public DHUtil getInstance(){
        if (mInstance == null)
            mInstance = new DHUtil();
        return mInstance;
    }

    /*
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(512); //1024太慢了
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /*
     */
    public static Map<String, Object> initKey(byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        DHPublicKey dhPublicKey = (DHPublicKey) keyFactory.generatePublic(keySpec);
        DHParameterSpec dhParameterSpec = dhPublicKey.getParams();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(dhParameterSpec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /*
     *
     */
    public static byte[] getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(priKey);
        keyAgreement.doPhase(pubKey, true);
        SecretKey secretKey = keyAgreement.generateSecret("AES"); //DES 3DES AES
        return secretKey.getEncoded();
    }

    /*
     * 从 Map 中得到公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap){
        DHPublicKey key = (DHPublicKey) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /*
     * 从 Map 中得到私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap){
        DHPrivateKey key = (DHPrivateKey) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }
}
