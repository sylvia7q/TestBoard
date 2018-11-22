package com.board.testboard.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/** 
 * gson解析类
 */
public class GsonUtils {

    //使用Gson进行解析Person
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }


    // 使用Gson进行解析 List<Person>
    public static <T> List<T> getObjects(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
//            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
//            }.getType());
            JsonArray arr = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arr) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
        }
        return list;
    }
}
