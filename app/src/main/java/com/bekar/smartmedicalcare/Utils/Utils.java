package com.bekar.smartmedicalcare.Utils;

public class Utils {
   public static boolean isEmail(String email){
        if(email.contains("@")&&email.substring(email.length()-4).equals(".com")){
            return true;
        }else return false;
    }
}
