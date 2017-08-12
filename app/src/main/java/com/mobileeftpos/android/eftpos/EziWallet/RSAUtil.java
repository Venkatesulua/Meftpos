package com.mobileeftpos.android.eftpos.EziWallet;

import java.lang.reflect.Field;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RSAUtil {
	/**
     * encryption algorithm
     */
    private static final String KEY_ALGORITHM = "RSA";
    
    /** 
     * sign algorithm
     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * sign the data by private key
     * 
     * @param signContent
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(String signContent, String privateKey)throws Exception {
		if (signContent == null || "".equals(signContent)) {
			return null;
		}
		if (privateKey == null || "".equals(privateKey)) {
			return null;
		}
        byte[] originalData = signContent.getBytes("UTF-8");
    	
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);

        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
        sign.initSign(privateK);
        sign.update(originalData);
        
        byte[] signedBtyes = sign.sign();
        String signedStr = Base64.encode(signedBtyes);
        
        return signedStr;
    }
    
    /**
     * sort the request parameters and generate the the string for sign
     * 
     * @param param
     * @return
     * @throws IllegalAccessException
     */
    public static String getSignContent(Object param) throws IllegalAccessException {
        SortedMap<String, String> map = new TreeMap<String, String>();
        Class<? extends Object> cls = param.getClass();

        Class superCls = cls.getSuperclass();
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(cls.getDeclaredFields()));
        fields.addAll(Arrays.asList(superCls.getDeclaredFields()));

        for(Field f : fields) {
            f.setAccessible(true);
            String filedName = f.getName();
            if (f.get(param) != null) {

                Object obj = f.get(param);
                String res = obj.toString();
                if(res != null && !"".equals(res)){
                    map.put(filedName, res);
                }
            }
            f.setAccessible(false);
        }
        StringBuffer str = new StringBuffer();
        for(Map.Entry<String, String> entry : map.entrySet()){

            str.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String signContent = str.substring(0, str.length()-1).toString();
        return signContent;
    }

}
