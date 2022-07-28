package it.csi.iuffi.iuffiweb.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class IuffiCrypto
{
  private static String KEY         = "9ake63iY$$6%?^�";
  private static String INIT_VECTOR = "RandomInitVector";

  public static String defaultEncrypt(String value)
  {
    return encrypt(KEY, INIT_VECTOR, value);
  }

  public static String defaultDecrypt(String encrypted)
  {
    return decrypt(KEY, INIT_VECTOR, encrypted);
  }

  public static String encrypt(String key, String initVector, String value)
  {
    try
    {
      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

      byte[] encrypted = cipher.doFinal(value.getBytes());

      return Base64.encodeBase64String(encrypted);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return null;
  }

  public static String decrypt(String key, String initVector, String encrypted)
  {
    try
    {
      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

      byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

      return new String(original);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return null;
  }
}