package com.library.scheduler;

import com.library.entity.BorrowRecord;
import com.library.entity.Fine;
import com.library.entity.Reservation;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.FineRepository;
import com.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LibraryScheduler {

    private final BorrowRecordRepository borrowRecordRepository;
    private final FineRepository fineRepository;
    private final ReservationRepository reservationRepository;

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");

    /**
     * Runs every hour: detects overdue borrows, updates status, creates fines
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void processOverdueBorrows() {
        List<BorrowRecord> overdue = borrowRecordRepository.findOverdue(LocalDateTime.now());
        log.info("Overdue check: found {} overdue records", overdue.size());
        overdue.forEach(record -> {
            record.setStatus(BorrowRecord.BorrowStatus.OVERDUE);
            if (!fineRepository.existsByBorrowRecordId(record.getId())) {
                long days = ChronoUnit.DAYS.between(record.getDueDate(), LocalDateTime.now());
                Fine fine = Fine.builder()
                        .borrowRecord(record)
                        .amount(FINE_PER_DAY.multiply(BigDecimal.valueOf(Math.max(1, days))))
                        .build();
                fineRepository.save(fine);
                log.info("Created fine of {} for borrow record {}", fine.getAmount(), record.getId());
            }
            borrowRecordRepository.save(record);
        });
    }

    /**
     * Runs daily at midnight: expire pending reservations
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireReservations() {
        List<Reservation> expired = reservationRepository.findExpired(LocalDateTime.now());
        log.info("Expiring {} reservations", expired.size());
        expired.forEach(r -> {
            r.setStatus(Reservation.ReservationStatus.EXPIRED);
            reservationRepository.save(r);
        });
    }
}
