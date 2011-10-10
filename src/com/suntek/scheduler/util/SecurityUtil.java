package com.suntek.scheduler.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {

	// singleton
	private static SecurityUtil securityUtil = null;
	
	private static Cipher encrypter = null;
	private static Cipher decrypter = null;
	private static BASE64Encoder encoder = null;
	private static BASE64Decoder decoder = null;

	static {
		try{
			byte[] keyData = "qij1@itssch3du!3".getBytes();
			SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
			
			encrypter = Cipher.getInstance("AES");
			encrypter.init(Cipher.ENCRYPT_MODE, keySpec);
			
			decrypter = Cipher.getInstance("AES");
			decrypter.init(Cipher.DECRYPT_MODE, keySpec);
			encoder = new BASE64Encoder();
			decoder = new BASE64Decoder();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
				
	public static String encrypt(String clearText)
	{
		if (clearText == null){
			return null;
		}else{
			try{
				int blockSize = encrypter.getBlockSize();
				int outputSize = encrypter.getOutputSize(blockSize);
				byte[] inBytes = new byte[blockSize];
				byte[] outBytes = new byte[outputSize];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream in = new ByteArrayInputStream(clearText.getBytes());
				
				int inLength = 0;
				boolean more = true;
				while (more){
					inLength = in.read(inBytes);
					if (inLength == blockSize){
						int outLength = encrypter.update(inBytes, 0, blockSize, outBytes);
						out.write(outBytes, 0, outLength);
					}else{
						more = false;
					}
				}
				if (inLength > 0){
					outBytes = encrypter.doFinal(inBytes, 0, inLength);				
				}else{
					outBytes = encrypter.doFinal();
				}
				out.write(outBytes);
				return encoder.encode(out.toByteArray());
			}catch(Exception e){
				e.printStackTrace();				
			}
			return null;
		}
	}
	
	public static String decrypt(String cipherText)	
	{
		if (cipherText == null){
			return null;
		}else{
			try{
				int blockSize = decrypter.getBlockSize();
				int outputSize = decrypter.getOutputSize(blockSize);
				byte[] inBytes = new byte[blockSize];
				byte[] outBytes = new byte[outputSize];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream in = new ByteArrayInputStream(decoder.decodeBuffer(cipherText));
				
				int inLength = 0;
				boolean more = true;
				while (more){
					inLength = in.read(inBytes);
					if (inLength == blockSize){
						int outLength = decrypter.update(inBytes, 0, blockSize, outBytes);
						out.write(outBytes, 0, outLength);
					}else{
						more = false;
					}
				}
				if (inLength > 0){
					outBytes = decrypter.doFinal(inBytes, 0, inLength);				
				}else{
					outBytes = decrypter.doFinal();
				}
				out.write(outBytes);
				return out.toString();
			}catch(Exception e){
				e.printStackTrace();				
			}
			return null;
		}		
	}
		
}
