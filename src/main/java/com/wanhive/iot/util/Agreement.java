package com.wanhive.iot.util;

import java.math.BigInteger;
import java.security.MessageDigest;

import com.nimbusds.srp6.BigIntegerUtils;
import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6Routines;

public class Agreement extends SRP6Routines {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SRP6CryptoParams config;

	public Agreement() {
		config = SRP6CryptoParams.getInstance(1024, "SHA-1");
	}

	public Agreement(SRP6CryptoParams config) {
		this.config = config;
	}

	// H(s | H ( I | ":" | p) )
	public BigInteger computeX(int rounds, final byte[] identity, final byte[] salt, final byte[] password) {
		MessageDigest digest = config.getMessageDigestInstance();
		digest.reset();
		if (identity != null && identity.length > 0) {
			digest.update(identity);
			digest.update((byte) ':');
		}
		digest.update(password);
		byte[] x = digest.digest(); // H ( I | ":" | p)

		do {
			digest.reset();
			digest.update(salt);
			digest.update(x);
			x = digest.digest();
		} while ((--rounds) > 0);

		BigInteger i = BigIntegerUtils.bigIntegerFromBytes(x);
		return i;
	}
	
	public BigInteger computeVerifier(BigInteger x) {
		return computeVerifier(config.N, config.g, x);
	}
}
