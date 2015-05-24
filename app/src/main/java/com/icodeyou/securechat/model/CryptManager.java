package com.icodeyou.securechat.model;

/**
 * Created by huan on 15/5/21.
 */
public class CryptManager {

    private static CryptManager mInstance = null;

    private CryptManager(){}

    private byte[] privateKeyAccept;
    private byte[] publicKeyAccept;
    private byte[] privateKeySponsor;
    private byte[] publicKeySponsor;

    private byte[] secret;

    public static CryptManager getInstance(){
        if (mInstance == null)
            mInstance = new CryptManager();
        return mInstance;
    }

    public byte[] getPrivateKeyAccept() {
        return privateKeyAccept;
    }

    public void setPrivateKeyAccept(byte[] privateKeyAccept) {
        this.privateKeyAccept = privateKeyAccept;
    }

    public byte[] getPublicKeyAccept() {
        return publicKeyAccept;
    }

    public void setPublicKeyAccept(byte[] publicKeyAccept) {
        this.publicKeyAccept = publicKeyAccept;
    }

    public byte[] getPrivateKeySponsor() {
        return privateKeySponsor;
    }

    public void setPrivateKeySponsor(byte[] privateKeySponsor) {
        this.privateKeySponsor = privateKeySponsor;
    }

    public byte[] getPublicKeySponsor() {
        return publicKeySponsor;
    }

    public void setPublicKeySponsor(byte[] publicKeySponsor) {
        this.publicKeySponsor = publicKeySponsor;
    }

    public byte[] getSecret() {
        return secret;
    }

    public void setSecret(byte[] secret) {
        this.secret = secret;
    }
}
