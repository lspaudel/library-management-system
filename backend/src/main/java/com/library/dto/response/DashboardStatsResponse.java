package com.library.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class DashboardStatsResponse {
    private long totalBooks;
    private long totalMembers;
    private long activeBorrows;
    private long overdueBorrows;
    private long totalReservations;
    private BigDecimal totalUnpaidFines;
    private List<CategoryBookCount> booksByCategory;
    private List<MonthlyBorrowCount> borrowsPerMonth;

    @Data @Builder
    public static class CategoryBookCount {
        private String category;
        private long count;
    }

    @Data @Builder
    public static class MonthlyBorrowCount {
        private int month;
        private long count;
    }
}
