package blockchain.utils;

import java.security.*;

public class KeyGenerator {

    private final PublicKey publicKey;
    private final Signature signatureAlgorithm;

    public KeyGenerator() throws NoSuchAlgorithmException, InvalidKeyException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        signatureAlgorithm = Signature.getInstance("SHA256WithRSA");
        signatureAlgorithm.initSign(privateKey);
    }

    public byte[] sign(String message) throws SignatureException {
        signatureAlgorithm.update(message.getBytes());
        return signatureAlgorithm.sign();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
