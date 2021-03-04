package MainPackage;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA
{

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private PublicKey publicKeyOfOtherSide;

    public void generateKeys()
    {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            keyGen.initialize(1024);
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String sign(byte[]hash)
    {
        try {
            Signature signAlgorithm = Signature.getInstance("DSA");

            signAlgorithm.initSign(privateKey);
            signAlgorithm.update(hash);

            return Base64.getEncoder().encodeToString(signAlgorithm.sign());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verify(byte[]hash, byte[]message)
    {
        try
        {
            Signature verifyAlgorithm = Signature.getInstance("DSA");

            verifyAlgorithm.initVerify(publicKeyOfOtherSide);
            verifyAlgorithm.update(message);

            return verifyAlgorithm.verify(hash);
        }
        catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e)
        {

        }
        return false;
    }

    public static PublicKey returnPublicKeyFromBase64ValueOfThisPublicKey(String base64)
    {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKeyOfOtherSide() {
        return publicKeyOfOtherSide;
    }

    public void setPublicKeyOfOtherSide(PublicKey publicKeyOfOtherSide) {
        this.publicKeyOfOtherSide = publicKeyOfOtherSide;
    }
}
