package com.spots.bella.constants;

import org.apache.commons.lang3.text.StrBuilder;

public class StringManipulation {
    public static String getTags(String s) {
        if (s.indexOf("#") > 0) {
            StringBuilder sb = new StringBuilder();
            char[] charArray = s.toCharArray();
            boolean foundWord = false;
            for (char c : charArray) {
                if (c == '#') {
                    foundWord = true;
                    sb.append(c);
                } else {
                    if (foundWord) {
                        sb.append(c);
                    }
                }
                if (c == ' '
                        ) {
                    foundWord = false;
                }
            }
            String s1 = sb.toString().replace(" ","").replace("#",",#");
            return s1.substring(1,s1.length());
        }
        return s;
    }
    /*In -> #tag2,#tag3*/
}
