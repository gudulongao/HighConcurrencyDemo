package demo.jvm.intern;

/**
 * 字符串 intern方法的样例
 */
public class InternDemo {

    public static void main(String[] args) {
        String str1 = new String("abc");
        String str2 = new String("abc");
        String str17 = "abc";
        //false
        System.out.println("str1 == str2: " + (str1 == str2));
        //false
        System.out.println("str1 == str17: " + (str1 == str17));
        //true
        System.out.println("str1.intern == str17: " + (str1.intern() == str17));
        //true
        System.out.println("str1.intern == str2.intern: " + (str1.intern() == str2.intern()));

        String str3 = new String("bcd");
        str3.intern();
        String str4 = new String("bcd");
        //false
        System.out.println("str3 == str4: " + (str3 == str4));

        String str5 = "str5";
        str5.intern();
        String str6 = "str5";
        //true
        System.out.println("str5 == str6: " + (str5 == str6));
        String str7 = new String("str5");
        //false
        System.out.println("str5 == str7: " + (str5 == str7));

        final String S = "s";
        final String T = "t";
        final String R = "r";
        final String EIGHT = "8";
        String str8 = "s" + "t" + "r" + "8";
        String str9 = "str8";
        String str10 = S + T + R + EIGHT;
        String str11 = new String("str8");
        //true
        System.out.println("str8 == str9: " + (str8 == str9));
        //true
        System.out.println("str8 == str10: " + (str8 == str10));
        //false
        System.out.println("str8 == str11: " + (str8 == str11));

        final String STR_12 = "str12";
        String str12 = "str12";
        String str13 = str12 + " is not abc";
        String str14 = "str12 is not abc";
        String str15 = STR_12+" is not abc";
        String str16 = new String("str12 is not abc");
        //false
        System.out.println("str13 == str14: " + (str13 == str14));
        //true
        System.out.println("str15 == str14: " + (str15 == str14));
        //false
        System.out.println("str16 == str14: " + (str16 == str14));
    }
}
