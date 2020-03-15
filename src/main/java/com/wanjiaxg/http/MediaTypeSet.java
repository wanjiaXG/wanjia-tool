package com.wanjiaxg.http;

import okhttp3.MediaType;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public final class MediaTypeSet {

    private static HashMap<String, MediaType> mediaTypes;

    public static MediaType getMediaTypeBySuffix(String suffix){
        MediaType mediaType = mediaTypes.get("stream");
        if(suffix != null && mediaTypes.containsKey(suffix.toLowerCase())){
            mediaType = mediaTypes.get(suffix);
        }
        return mediaType;
    }

    public static MediaType getMediaTypeByFullName(String name){
        String suffix = null;
        if(name != null){
            int index = name.lastIndexOf(".");
            if(index != -1){
                suffix = name.substring(index + 1);
            }
        }
        return getMediaTypeBySuffix(suffix);
    }

    static {
        mediaTypes = new HashMap<>();
        mediaTypes.put("stream", MediaType.parse("application/octet-stream"));

        //text
        mediaTypes.put("html", MediaType.parse("text/html"));
        mediaTypes.put("css", MediaType.parse("text/css"));
        mediaTypes.put("js", MediaType.parse("text/javascript"));
        mediaTypes.put("txt", MediaType.parse("text/plain"));

        //image
        mediaTypes.put("jpg", MediaType.parse("image/jpeg"));
        mediaTypes.put("jpeg", MediaType.parse("image/jpeg"));
        mediaTypes.put("png", MediaType.parse("image/png"));
        mediaTypes.put("gif", MediaType.parse("image/gif"));
        mediaTypes.put("tiff", MediaType.parse("image/tiff"));
        mediaTypes.put("bmp", MediaType.parse("image/bmp"));
        mediaTypes.put("webp", MediaType.parse("image/webp"));
        mediaTypes.put("ico", MediaType.parse("image/x-icon"));

        //audio
        mediaTypes.put("mp3", MediaType.parse("audio/mp3"));
        mediaTypes.put("ogg", MediaType.parse("audio/ogg"));
        mediaTypes.put("wav", MediaType.parse("audio/wav"));
        mediaTypes.put("mid", MediaType.parse("audio/mid"));
        mediaTypes.put("midi", MediaType.parse("audio/mid"));

        //video
        mediaTypes.put("mp4", MediaType.parse("video/mp4"));
        mediaTypes.put("avi", MediaType.parse("video/avi"));

        //microsoft office
        mediaTypes.put(".doc", MediaType.parse("application/msword"));
        mediaTypes.put(".dot", MediaType.parse("application/msword"));
        mediaTypes.put(".docx", MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        mediaTypes.put(".dotx", MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.template"));
        mediaTypes.put(".docm", MediaType.parse("application/vnd.ms-word.document.macroEnabled.12"));
        mediaTypes.put(".dotm", MediaType.parse("application/vnd.ms-word.template.macroEnabled.12"));
        mediaTypes.put(".xls", MediaType.parse("application/vnd.ms-excel"));
        mediaTypes.put(".xlt", MediaType.parse("application/vnd.ms-excel"));
        mediaTypes.put(".xla", MediaType.parse("application/vnd.ms-excel"));
        mediaTypes.put(".xlsx", MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        mediaTypes.put(".xltx", MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.template"));
        mediaTypes.put(".xlsm", MediaType.parse("application/vnd.ms-excel.sheet.macroEnabled.12"));
        mediaTypes.put(".xltm", MediaType.parse("application/vnd.ms-excel.template.macroEnabled.12"));
        mediaTypes.put(".xlam", MediaType.parse("application/vnd.ms-excel.addin.macroEnabled.12"));
        mediaTypes.put(".xlsb", MediaType.parse("application/vnd.ms-excel.sheet.binary.macroEnabled.12"));
        mediaTypes.put(".ppt", MediaType.parse("application/vnd.ms-powerpoint"));
        mediaTypes.put(".pot", MediaType.parse("application/vnd.ms-powerpoint"));
        mediaTypes.put(".pps", MediaType.parse("application/vnd.ms-powerpoint"));
        mediaTypes.put(".ppa", MediaType.parse("application/vnd.ms-powerpoint"));
        mediaTypes.put(".pptx", MediaType.parse("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
        mediaTypes.put(".potx", MediaType.parse("application/vnd.openxmlformats-officedocument.presentationml.template"));
        mediaTypes.put(".ppsx", MediaType.parse("application/vnd.openxmlformats-officedocument.presentationml.slideshow"));
        mediaTypes.put(".ppam", MediaType.parse("application/vnd.ms-powerpoint.addin.macroEnabled.12"));
        mediaTypes.put(".pptm", MediaType.parse("application/vnd.ms-powerpoint.presentation.macroEnabled.12"));
        mediaTypes.put(".potm", MediaType.parse("application/vnd.ms-powerpoint.presentation.macroEnabled.12"));
        mediaTypes.put(".ppsm", MediaType.parse("application/vnd.ms-powerpoint.slideshow.macroEnabled.12"));

        //application
        mediaTypes.put("json", MediaType.parse("application/json"));
        mediaTypes.put("xml", MediaType.parse("application/xml"));
        mediaTypes.put("pdf", MediaType.parse("application/pdf"));
        mediaTypes.put("apk", MediaType.parse("application/vnd.android.package-archive"));
    }

}
