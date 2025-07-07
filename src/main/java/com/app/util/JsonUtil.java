package com.app.util;

import com.app.exception.ErrorException;
import com.google.gson.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonUtil {
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy");

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(LOCAL_DATE_FORMATTER.format(src));
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString(), LOCAL_DATE_FORMATTER);
                }
            })
            .setDateFormat("MMM d, yyyy") // still used for java.util.Date
            .setPrettyPrinting()
            .create();

    private static ErrorException error;

    public static void writeOk(HttpServletResponse resp, int statusCode, Object obj) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = gson.toJson(obj);
        resp.getWriter().write(json);
    }

    public static void writeError(HttpServletResponse resp, int statusCode, String msg) throws IOException {
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
