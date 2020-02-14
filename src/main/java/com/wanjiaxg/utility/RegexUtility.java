package com.wanjiaxg.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtility {

    public static List<String> getResults(String str, String regex){
        List<String> list = new ArrayList<>();
        if(str != null && regex != null){
            try {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()){
                    list.add(matcher.group());
                }
            }catch (Exception ignored){ }
        }
        return list;
    }

    public static String getFirstResult(String str, String regex){
        String result = null;
        if(str != null && regex != null){
            try{
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()){
                    result = matcher.group();
                    break;
                }
            }catch (Exception e){

            }
        }
        return result;
    }

    public static String getFileNameIgnoreSuffix(String path){
        return getFirstResult(path, "(?!.*[/|\\\\]).+(?=\\.)");
    }

    public static String getFileName(String path){
        return getFirstResult(path, "(?!.*[/|\\\\]).+");
    }

    public static String getFilePath(String path) {
        return getFirstResult(path, ".*[/|\\\\]");
    }

}
