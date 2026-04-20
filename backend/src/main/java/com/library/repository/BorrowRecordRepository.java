package com.library.repository;

import com.library.entity.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    Page<BorrowRecord> findByMemberIdOrderByBorrowedAtDesc(Long memberId, Pageable pageable);

    @Query("SELECT b FROM BorrowRecord b JOIN FETCH b.book JOIN FETCH b.member WHERE b.status = 'BORROWED' AND b.dueDate < :now")
    List<BorrowRecord> findOverdue(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(b) FROM BorrowRecord b WHERE b.member.id = :memberId AND b.status = 'BORROWED'")
    long countActiveBorrowsByMember(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(b) FROM BorrowRecord b WHERE b.status = 'BORROWED'")
    long countAllActiveBorrows();

    @Query("SELECT COUNT(b) FROM BorrowRecord b WHERE b.status = 'OVERDUE'")
    long countOverdueBorrows();

    @Query("SELECT MONTH(b.borrowedAt), COUNT(b) FROM BorrowRecord b WHERE YEAR(b.borrowedAt) = :year GROUP BY MONTH(b.borrowedAt)")
    List<Object[]> countBorrowsByMonth(@Param("year") int year);
}
