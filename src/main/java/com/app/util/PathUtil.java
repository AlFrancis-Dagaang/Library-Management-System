package com.app.util;

public class PathUtil {
    public static boolean isNumeric(String str) {
        if (str == null) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String [] getPaths (String path){
        if(path != null && !path.isEmpty()){
            return path.split("/");
        }else{
            return null;
        }
    }
}
