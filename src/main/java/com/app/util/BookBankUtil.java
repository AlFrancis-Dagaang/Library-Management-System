package com.app.util;

import java.math.BigDecimal;

public class BookBankUtil {

    public static BigDecimal calculateServiceFee(BigDecimal bookPrice){
        return bookPrice.multiply(new BigDecimal("0.05"));
    }
}
