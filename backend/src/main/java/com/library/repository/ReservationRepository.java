package com.library.repository;

import com.library.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByMemberIdOrderByReservedAtDesc(Long memberId, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'PENDING' AND r.expiresAt < :now")
    List<Reservation> findExpired(@Param("now") LocalDateTime now);

    boolean existsByBookIdAndMemberIdAndStatus(Long bookId, Long memberId, Reservation.ReservationStatus status);
}
