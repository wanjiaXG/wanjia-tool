package com.wanjiaxg.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("ALL")
public final class RegexUtility {

    /**
     * 获取正则匹配的所有结果
     * @param str       待匹配的字符串
     * @param regex     正则表达式
     * @return          匹配结果集
     */
    public static List<String> matchs(String str, String regex) {
        List<String> list = new ArrayList<>();
        if(str != null && regex != null){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()){
                list.add(matcher.group());
            }
        }
        return list;
    }

    /**
     * 获取正则匹配的第一个结果
     * @param str       待匹配字符串
     * @param regex     正则表达式
     * @return          匹配结果
     * @return
     */
    public static String match(String str, String regex){
        return match(str, regex, 0);
    }

    /**
     * 获取正则匹配的结果
     * @param str       待匹配字符串
     * @param regex     正则表达式
     * @param offset    结果偏移
     * @return          匹配结果
     */
    public static String match(String str, String regex, int offset){
        int cnt = 0;
        String result = null;
        if(str != null && regex != null){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()){
                if(offset == cnt){
                    result = matcher.group();
                    break;
                }
                cnt++;
            }
        }
        return result;
    }

    /**
     * 获取无后缀的文件名
     * @param path 文件名或者文件路径
     * @return
     */
    public static String getFileNameIgnoreSuffix(String path){
        return match(path, "(?!.*[/|\\\\]).+(?=\\.)");
    }

    /**
     * 获取文件后缀名
     * @param path   文件名或文件路径
     * @return       后缀名
     */
    public static String getSuffix(String path){
        return match(path, "(?!.*\\.).*");
    }

    /**
     * 获取文件名
     * @param path  文件路径
     * @return
     */
    public static String getFileName(String path){
        return match(path, "(?!.*[/|\\\\]).+");
    }

    /**
     * 获取文件路径
     * @param path
     * @return
     */
    public static String getFilePath(String path) {
        return match(path, ".*[/|\\\\]");
    }

}
