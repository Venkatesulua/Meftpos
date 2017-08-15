package com.mobileeftpos.android.eftpos.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/6/19.
 */
public class Utils {
    private final static String TAG = Utils.class.getName();

    static final char[] HEX = "0123456789ABCDEF".toCharArray();


    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        if (sz == 0) {
            return false;
        }
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i)) && (str.charAt(i) != '.')) {
                return false;
            }
        }
        return true;
    }

    public static String hexEncode(byte[] buffer, int start, int length) {
        if (buffer.length == 0) {
            return "";
        }
        int holder = 0;
        char[] chars = new char[length * 2];
        int pos = -1;
        for (int i = start; i < start + length; i++) {
            holder = (buffer[i] & 0xF0) >> 4;
            chars[++pos * 2] = HEX[holder];
            holder = buffer[i] & 0x0F;
            chars[(pos * 2) + 1] = HEX[holder];
        }
        return new String(chars);
    }

    public static void BCDEncode(String value, byte[] buf) {
        int charpos = 0; //char where we start
        int bufpos = 0;
//        if (value.length() % 2 == 1) {
//            //for odd lengths we encode just the first digit in the first byte
//            buf[0] = (byte)(value.charAt(0) - 48);
//            charpos = 1;
//            bufpos = 1;
//        }
        //encode the rest of the string
        if (value.length() % 2 > 0) {
            value = value + "0";
        }

        while (charpos < value.length()) {
            buf[bufpos] = (byte) ((((value.charAt(charpos) > '?') ? (value.charAt(charpos) - 55) : (value.charAt(charpos) - 48)) << 4)
                    | ((value.charAt(charpos + 1) > '?') ? (value.charAt(charpos + 1) - 55) : (value.charAt(charpos + 1) - 48)));
            charpos += 2;
            bufpos++;
        }
    }

    /**
     * Decodes a TYPE_BCD-encoded number as a String.
     *
     * @param buf The byte buffer containing the TYPE_BCD data.
     */
    public static String Bcd2String(byte[] buf) throws IndexOutOfBoundsException {
        int length = buf.length;
        char[] digits = new char[length * 2];
        int start = 0;

        for (int i = 0; i < length; i++) {
            digits[start++] = (char) ((((buf[i] & 0x00f0) >> 4) > 9) ?
                    ((buf[i] & 0x00f0) >> 4) + 55 : ((buf[i] & 0x00f0) >> 4) + 48);
            digits[start++] = (char) (((buf[i] & 0x000f) > 9) ?
                    (buf[i] & 0x000f) + 55 : (buf[i] & 0x000f) + 48);
        }

        return new String(digits);
    }

    /**
     * BASE64 编码
     *
     * @param buff
     * @return
     */
    public static String encodeBufferBase64(byte[] buff) {
        return buff == null ? null : Base64.encodeToString(buff, Base64.NO_WRAP);
    }

    /**
     * BASE64解码
     *
     * @param s
     * @return
     */
    public static byte[] decodeBufferBase64(String s) {
        try {
            return s == null ? null : Base64.decode(s, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64 字节数组编码
     *
     * @param s
     * @return String
     */
    public static String encodeBase64(byte[] s) {
        if (s == null)
            return null;

        String res = Base64.encodeToString(s, Base64.NO_WRAP);
        res = res.replace("\n", "");
        res = res.replace("\r", "");

        return res;
    }

    /**
     * BASE64解码
     *
     * @param buff
     * @return
     */
    public static byte[] decodeBase64(byte[] buff) {
        if (buff == null)
            return null;

        try {
            byte[] key = Base64.decode(buff, Base64.DEFAULT);

            return key;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getEigthBitsStringFromByte(int b) {
        // if this is a positive number its bits number will be less
        // than 8
        // so we have to fill it to be a 8 digit binary string
        // b=b+100000000(2^8=256) then only get the lower 8 digit
        b |= 256; // mark the 9th digit as 1 to make sure the string
        // has at
        // least 8 digits
        String str = Integer.toBinaryString(b);
        int len = str.length();
        return str.substring(len - 8, len);
    }

    public static byte getByteFromEigthBitsString(String str) {
        // if(str.length()!=8)
        // throw new Exception("It's not a 8 length string");
        byte b;
        // check if it's a minus number
        if (str.substring(0, 1).equals("1")) {
            // get lower 7 digits original code
            str = "0" + str.substring(1);
            b = Byte.valueOf(str, 2);
            // then recover the 8th digit as 1 equal to plus
            // 1000000
            b |= 128;
        } else {
            b = Byte.valueOf(str, 2);
        }
        return b;
    }

    /**
     * 将一个8/16字节数组转成128二进制数组
     */
    public static boolean[] getBinaryFromByte(byte[] b) {
        boolean[] binary = new boolean[b.length * 8 + 1];
        String str = "";
        for (int i = 0; i < b.length; i++) {
            str += getEigthBitsStringFromByte(b[i]);
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).equalsIgnoreCase("1")) {
                binary[i + 1] = true;
            } else {
                binary[i + 1] = false;
            }
        }
        return binary;
    }

    /**
     * 将一个128二进制数组转成16字节数组
     *
     * @param binary
     * @return
     */
    public static byte[] getByteFromBinary(boolean[] binary) {
        int num = (binary.length - 1) / 8;
        if (((binary.length - 1) % 8) != 0) {
            num = num + 1;
        }

        byte[] b = new byte[num];
        String s = "";
        for (int i = 1; i < binary.length; i++) {
            if (binary[i]) {
                s += "1";
            } else {
                s += "0";
            }
        }

        String tmpstr;
        int j = 0;
        for (int i = 0; i < s.length(); i = i + 8) {
            tmpstr = s.substring(i, i + 8);
            b[j] = getByteFromEigthBitsString(tmpstr);
            j = j + 1;
        }

        return b;
    }

    /**
     * 将一个byte位图转成字符串
     *
     * @param b
     * @return
     */
    public static String getStrFromBitMap(byte[] b) {
        String strsum = "";
        for (int i = 0; i < b.length; i++) {
            strsum += getEigthBitsStringFromByte(b[i]);
        }
        return strsum;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);

        return ret;
    }

    /**
     * 十六进制字符串转换成bytes
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStr2Bytes(String hexStr) {
        int l = hexStr.length();
        if (l % 2 != 0) {
            StringBuilder sb = new StringBuilder(hexStr);
            sb.insert(hexStr.length() - 1, '0');
            hexStr = sb.toString();
        }
        byte[] b = new byte[hexStr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexStr.charAt(j++);
            char c1 = hexStr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    /**
     * 将String转成BCD码
     *
     * @param s
     * @return
     */
    public static byte[] StrToBCDBytes(String s) {
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }

        return baos.toByteArray();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 将BCD码转成int
     *
     * @param b
     * @return
     */
    public static int bcdToint(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int h = ((b[i] & 0xF0) >> 4) + 48;
            sb.append((char) h);
            int l = (b[i] & 0x0F) + 48;
            sb.append((char) l);
        }
        return Integer.parseInt(sb.toString());
    }


    public static byte[] asciiStr2Bytes(String ascii) {
        byte[] dat = null;
        try {
            dat = ascii.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dat;
    }

    public static String asciiStr2HexStr(String ascii) {
        byte[] dat = asciiStr2Bytes(ascii);
        return byte2HexStr(dat);
    }

    public static String hexStr2AsciiStr(String hex) {
        String rec= null;
        try {
            rec = new String(hexStr2Bytes(hex), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rec;
    }

    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * java中char数组转换成对应字节数组
     * @param chars
     * @return
     */
    public static byte[] chars2Bytes(char[] chars) {
        byte[] rec = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            rec[i * 2] = (byte) ((c & 0xFF00) >> 8);
            rec[i * 2 + 1] = (byte) (c & 0xFF);
        }
        return rec;
    }

    /**
     * java中char数组对应字节数组转换成字符
     * @param bytes
     * @return
     */
    public static char[] bytes2Chars(byte[] bytes) {
        char[] rec = new char[bytes.length /2];
        for (int i = 0; i < rec.length; i++) {
            int h=(bytes[i * 2]&0xff)<<8;
            int l=bytes[i * 2 + 1]&0xff;
            rec[i]= (char) (h|l);
        }
        return rec;
    }
}
