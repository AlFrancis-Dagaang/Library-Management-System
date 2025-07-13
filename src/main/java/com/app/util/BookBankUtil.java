package com.app.util;

import java.math.BigDecimal;

public class BookBankUtil {

    public static BigDecimal calculateServiceFee(BigDecimal bookPrice){
        return bookPrice.multiply(new BigDecimal("0.05"));
    }

    public static BigDecimal calculatePenaltyBaseOnCondition(String bookCondition, String bookType, BigDecimal bookPrice) {
        BigDecimal basePenaltyForCondition;

        switch (bookCondition) {
            case "Good":
                basePenaltyForCondition = BigDecimal.valueOf(0.00);
                break;

            case "Minor Damage":
                basePenaltyForCondition = BigDecimal.valueOf(75.00);
                break;

            case "Major Damage":
                basePenaltyForCondition = BigDecimal.valueOf(200.00);
                break;

            case "Lost":
                if ("BOOK_BANK".equalsIgnoreCase(bookType)) {
                    basePenaltyForCondition = bookPrice; // Full price
                } else if ("GENERAL".equalsIgnoreCase(bookType)) {
                    basePenaltyForCondition = BigDecimal.valueOf(500.00);
                } else {
                    throw new RuntimeException("Unknown book type for Lost condition");
                }
                return basePenaltyForCondition; // Return early, no extra charges
            default:
                throw new RuntimeException("Book Condition doesn't match any known value");
        }

        // Add ₱100 only if book is BOOK_BANK and condition is not Good and is not Lost
        if ("BOOK_BANK".equals(bookType) && !"Good".equals(bookCondition) && !"Lost".equals(bookCondition)) {
            basePenaltyForCondition = basePenaltyForCondition.add(BigDecimal.valueOf(100.00));
        }

        return basePenaltyForCondition;
    }


    public static BigDecimal calculatePenaltyBaseOnReturnTimeline(String bookReturnTimeline, String bookType) {
        BigDecimal basePenaltyForTimeline;

        switch (bookReturnTimeline) {
            case "On Time":
                basePenaltyForTimeline = BigDecimal.valueOf(0.00);
                break;
            case "Overdue":
                basePenaltyForTimeline = BigDecimal.valueOf(50.00);
                break;
            default:
                throw new RuntimeException("Book Condition doesn't match any known value");
        }

        // If book type is BOOK_BANK, add 100
        if ("BOOK_BANK".equalsIgnoreCase(bookType) && !bookReturnTimeline.equals("On Time")) {
            basePenaltyForTimeline= basePenaltyForTimeline.add(BigDecimal.valueOf(100.00));
        }

        return basePenaltyForTimeline;
    }
}
