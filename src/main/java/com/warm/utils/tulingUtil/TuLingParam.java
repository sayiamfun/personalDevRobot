package com.warm.utils.tulingUtil;

public class TuLingParam {
    public static String getParams(String text,String imageUrl){
        return "{\n" +
                "\t\"reqType\":0,\n" +
                "    \"perception\": {\n" +
                "        \"inputText\": {\n" +
                "            \"text\": \""+text+"\"\n" +
                "        },\n" +
                "        \"inputImage\": {\n" +
                "            \"url\": \""+imageUrl+"\"\n" +
                "        },\n" +
                "        \"selfInfo\": {\n" +
                "            \"location\": {\n" +
                "                \"city\": \"北京\",\n" +
                "                \"province\": \"北京\",\n" +
                "                \"street\": \"信息路\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"userInfo\": {\n" +
                "        \"apiKey\": \"f624cc9e68e940e4b45ef3f720c35273\",\n" +
                "        \"userId\": \"430528\"\n" +
                "    }\n" +
                "}";
    }
}
