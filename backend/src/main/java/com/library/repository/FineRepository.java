package com.library.repository;

import com.library.entity.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface FineRepository extends JpaRepository<Fine, Long> {
    Page<Fine> findByBorrowRecordMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    boolean existsByBorrowRecordId(Long borrowRecordId);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.paid = false AND f.borrowRecord.member.id = :memberId")
    BigDecimal getTotalUnpaidFinesByMember(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.paid = false")
    BigDecimal getTotalUnpaidFines();
}
