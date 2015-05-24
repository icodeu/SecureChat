package com.icodeyou.securechat.model;

import java.io.Serializable;
import java.security.PublicKey;


public class PubkeyManager implements Serializable {
	
	byte[] publicKey;
	
	public PubkeyManager(byte[] zPublicKey){
		publicKey = zPublicKey;
	}
	
	public byte[] getPublicKey() {
		return publicKey;
	}

}
