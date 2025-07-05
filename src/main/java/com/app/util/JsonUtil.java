package com.app.util;

import com.app.exception.ErrorException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("MMM d, yyyy")
            .setPrettyPrinting()// this matches "Jul 1, 2025"
            .create();

    private static ErrorException error;

    public static void writeOk(HttpServletResponse resp,int statusCode, Object obj) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = gson.toJson(obj);
        resp.getWriter().write(json);
    }

    public static void writeError(HttpServletResponse resp,int statusCode, String msg) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        error = new ErrorException(msg, statusCode);
        String json = gson.toJson(error);
        resp.getWriter().write(json);
    }

    public static <T> T parse(HttpServletRequest req, Class<T> clazz) throws IOException {
        BufferedReader reader = req.getReader();
        return gson.fromJson(reader, clazz);
    }





}
