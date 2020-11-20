public static String decryption(SecretKeySpec key, IvParameterSpec initVector, byte[] encrypted) throws GeneralSecurityException {

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    
        cipher.init(Cipher.DECRYPT_MODE, key, initVector);
    
        byte[] original = cipher.doFinal(encrypted);

        return new String(original, Charset.forName("UTF-8"));
  }
