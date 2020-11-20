import java.security.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.math.BigInteger;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Assignment1{
    private static String pMod = "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323";
    private static String genG = "44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e88641a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f549664bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68";
    private static String publicKey = "5af3e806e0fa466dc75de60186760516792b70fdcd72a5b6238e6f6b76ece1f1b38ba4e210f61a2b84ef1b5dc4151e799485b2171fcf318f86d42616b8fd8111d59552e4b5f228ee838d535b4b987f1eaf3e5de3ea0c403a6c38002b49eade15171cb861b367732460e3a9842b532761c16218c4fea51be8ea0248385f6bac0d";

    private static BigInteger ltrmodoExpo(BigInteger a, BigInteger expo, BigInteger modn){
        int n = expo.bitLength();

        BigInteger c = BigInteger.ONE;
        for(int i = n-1; i >= 0; i--){
            c = c.multiply(c).mod(modn);
            if (expo.testBit(i)){
                c = c.multiply(a).mod(modn);
            }
        }
        return c;
    }

    // Code I used to check to see if encryption was successful
    /* public static String decryption(SecretKeySpec key, IvParameterSpec initVector, byte[] encrypted) throws GeneralSecurityException {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, initVector);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, Charset.forName("UTF-8"));
    } */
     
    public static void main(String[] args) {
        String file = (args[0]);

        // My Private Key [used for both ]
        BigInteger privKey = new BigInteger(1023, new SecureRandom());
        
        // Recipients Public Key in Big Integer form [used for shared Key generation]
        BigInteger pubKey = new BigInteger(publicKey, 16);

        // pMod Big Integer Creation
        BigInteger pModBI = new BigInteger(pMod, 16);

        // Generator G Big Integer Creation
        BigInteger genGBI = new BigInteger(genG, 16);

        // My public key
        BigInteger perPublicKey = ltrmodoExpo(genGBI, privKey, pModBI);

        // Shared Key
        BigInteger sharedKey = ltrmodoExpo(pubKey, privKey, pModBI);

        try {
            // Create AES key using sharedKey in byteArray form.
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = messageDigest.digest(sharedKey.toByteArray());
            SecretKeySpec prKey = new SecretKeySpec(byteArray, "AES");
            SecretKey key = prKey;

            // create the 128-bit IV in hex
            byte[] beforeIV = new byte[16];
            SecureRandom randomInt = new SecureRandom();
            randomInt.nextBytes(beforeIV);
            IvParameterSpec iv = new IvParameterSpec(beforeIV);

            // initialise the cipher
            Cipher cr = Cipher.getInstance("AES/CBC/NoPadding");
            cr.init(Cipher.ENCRYPT_MODE, key, iv);

            // read in the file you to be encrypted
            File input = new File(file);
            int lengthIn = (int) input.length();
            int lengthPad = 16 - (lengthIn % 16);
            byte[] fileBytes = new byte[lengthIn + lengthPad];
            FileInputStream fInputStream = new FileInputStream(input);
            fInputStream.read(fileBytes);
            fInputStream.close();

            // padding input
            fileBytes[lengthIn] = (byte) 128;
            for(int i = 1; i < lengthPad; i++){
                fileBytes[lengthIn + 1] = (byte) 0;
            }

            // encrypt the padding input
            byte[] finalOut = cr.doFinal(fileBytes);

            // write the encrypted data to file
            File outputF = new File("Encryption.txt");
            FileOutputStream fileOut = new FileOutputStream(outputF);
            fileOut.write(finalOut);
            fileOut.close();
            System.out.println("Encryption Completed");

            // printing byte array as hex based on code seen here
            // https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
            StringBuilder stringBuilder = new StringBuilder(beforeIV.length*2);
            for(byte a:beforeIV){
                stringBuilder.append(String.format("%02x", a & 0xff));
            }

            // Write IV to IV.txt in string format
            BufferedWriter ivOut = new BufferedWriter(new FileWriter("IV.txt"));
            ivOut.write(stringBuilder.toString());
            ivOut.close();
            
            // Write public key to DH.txt in string format
            String pubKeyHex = perPublicKey.toString(16);
            BufferedWriter dhOut = new BufferedWriter(new FileWriter("DH.txt"));
            dhOut.write(pubKeyHex);
            dhOut.close();

            //See decryption method above
            /*String decrypt = decryption(prKey, iv, finalOut);
            System.out.print(decrypt);*/
        } catch (GeneralSecurityException | IOException e){
            System.out.print("Error: " + e);
        }
    }
}