package com.library.service.impl;

import com.library.dto.response.DashboardStatsResponse;
import com.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final ReservationRepository reservationRepository;
    private final FineRepository fineRepository;
    private final CategoryRepository categoryRepository;

    public DashboardStatsResponse getStats() {
        int year = LocalDate.now().getYear();
        List<DashboardStatsResponse.CategoryBookCount> booksByCategory = categoryRepository.findAll()
                .stream()
                .map(c -> DashboardStatsResponse.CategoryBookCount.builder()
                        .category(c.getName())
                        .count(bookRepository.countByCategoryId(c.getId())).build())
                .filter(c -> c.getCount() > 0).toList();

        List<DashboardStatsResponse.MonthlyBorrowCount> borrowsPerMonth = borrowRecordRepository
                .countBorrowsByMonth(year).stream()
                .map(row -> DashboardStatsResponse.MonthlyBorrowCount.builder()
                        .month(((Number) row[0]).intValue())
                        .count(((Number) row[1]).longValue()).build())
                .toList();

        return DashboardStatsResponse.builder()
                .totalBooks(bookRepository.count())
                .totalMembers(memberRepository.count())
                .activeBorrows(borrowRecordRepository.countAllActiveBorrows())
                .overdueBorrows(borrowRecordRepository.countOverdueBorrows())
                .totalReservations(reservationRepository.count())
                .totalUnpaidFines(fineRepository.getTotalUnpaidFines())
                .booksByCategory(booksByCategory)
                .borrowsPerMonth(borrowsPerMonth)
                .build();
    }
}
