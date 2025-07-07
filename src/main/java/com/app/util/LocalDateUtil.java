package com.app.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;

public class LocalDateUtil {

    public static LocalDate getNullableLocalDate(ResultSet rs, String column) throws SQLException {
        Date date = rs.getDate(column);
        return (date != null) ? date.toLocalDate() : null;
    }

}
