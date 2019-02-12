package com.tensquare.test;

import sun.misc.BASE64Encoder;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * MD5加密：它是单向不可逆运算
 *    加密的字节数组转换成字符串，字符串中的字符是由可见字符和不可见字符组合而成的
 *    我们需要把不可见字符全都转成可见字符。
 *
 * 可见字符：通过键盘可以直接输入的字符。
 *
 * Base64编码规则：它是双向可逆的。
 *      BASE64Encoder   编码
 *      BASE64Decoder   解码
 *
 *
 *    编码前：s12
 *         s：对应的ascii码是115      01110011
 *         1：对应的ascii码是49       00110001
 *         2：对应的ascii码是50       00110010
 *
 *         00011100 00110011 00000100 00110010
 *             28      51        4        50
 *              c       z        E         y
 *    编码后：czEy
 *
 *
 *  散列算法：
 *      它就是通过一定的规则把我们的密码进行加密，规则是由使用者指定的。
 *      规则就是我们说的盐（salt）
 *
 *      String salt = "itcast";
 *      Char[] ch = salt.toCharArray();
 *      ch[0] = 'i';
 *
 *      1           2            3             4
 *      49          50          51             52
 *      00110001    00110010    00110011       01100100
 *
 *
 *   apache shiro安全框架中的MD5Hash的加密算法是在  上述加密过程中还可以继续扩展：撒几把盐
 */
public class MD5Test {

    public static void main(String[] args) {
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode("s12".getBytes());
        System.out.println(str);
    }


//    public static void main(String[] args) throws Exception{
//        String password = "子小子";
//
////        BASE64Encoder encoder = new BASE64Encoder();
////        String str = encoder.encode(password.getBytes());
////        System.out.println(str);
////
////        BASE64Decoder decoder = new BASE64Decoder();
////        byte[] by = decoder.decodeBuffer(str);
////        System.out.println(new String(by,0,by.length));
//
//        MessageDigest md5 = MessageDigest.getInstance("md5");
//        byte[] by =  md5.digest(password.getBytes());
//
//        BASE64Encoder encoder = new BASE64Encoder();
//        String md5Password = encoder.encode(by);
//
//        System.out.println(md5Password);
//
//
//
//    }
}
