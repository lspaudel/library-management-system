package com.library.service.impl;

import com.library.dto.request.BorrowRequest;
import com.library.dto.response.BorrowResponse;
import com.library.entity.*;
import com.library.exception.BusinessException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BorrowServiceImpl {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final FineRepository fineRepository;

    private static final int MAX_BORROWS_PER_MEMBER = 5;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");

    public BorrowResponse borrow(BorrowRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", request.getBookId()));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", request.getMemberId()));

        if (book.getAvailableQty() <= 0) {
            throw new BusinessException("Book '" + book.getTitle() + "' is not available");
        }
        long activeBorrows = borrowRecordRepository.countActiveBorrowsByMember(member.getId());
        if (activeBorrows >= MAX_BORROWS_PER_MEMBER) {
            throw new BusinessException("Member has reached the maximum borrow limit of " + MAX_BORROWS_PER_MEMBER);
        }

        book.setAvailableQty(book.getAvailableQty() - 1);
        bookRepository.save(book);

        BorrowRecord record = BorrowRecord.builder()
                .book(book).member(member)
                .borrowedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(request.getDurationDays()))
                .status(BorrowRecord.BorrowStatus.BORROWED)
                .build();
        return toResponse(borrowRecordRepository.save(record));
    }

    public BorrowResponse returnBook(Long borrowId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("BorrowRecord", borrowId));
        if (record.getStatus() == BorrowRecord.BorrowStatus.RETURNED) {
            throw new BusinessException("Book already returned");
        }

        record.setReturnedAt(LocalDateTime.now());
        record.setStatus(BorrowRecord.BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setAvailableQty(book.getAvailableQty() + 1);
        bookRepository.save(book);

        // Calculate fine if overdue
        if (LocalDateTime.now().isAfter(record.getDueDate()) && !fineRepository.existsByBorrowRecordId(record.getId())) {
            long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDateTime.now());
            BigDecimal fineAmount = FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
            Fine fine = Fine.builder().borrowRecord(record).amount(fineAmount).build();
            fineRepository.save(fine);
        }

        return toResponse(borrowRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public Page<BorrowResponse> getByMember(Long memberId, int page, int size) {
        return borrowRecordRepository.findByMemberIdOrderByBorrowedAtDesc(
                memberId, PageRequest.of(page, size)).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<BorrowResponse> getOverdue() {
        return borrowRecordRepository.findOverdue(LocalDateTime.now()).stream().map(this::toResponse).toList();
    }

    public BorrowResponse toResponse(BorrowRecord r) {
        Fine fine = r.getFine();
        return BorrowResponse.builder()
                .id(r.getId())
                .bookId(r.getBook().getId()).bookTitle(r.getBook().getTitle()).bookIsbn(r.getBook().getIsbn())
                .memberId(r.getMember().getId()).memberName(r.getMember().getName())
                .borrowedAt(r.getBorrowedAt()).dueDate(r.getDueDate()).returnedAt(r.getReturnedAt())
                .status(r.getStatus())
                .fineAmount(fine != null ? fine.getAmount() : BigDecimal.ZERO)
                .finePaid(fine != null && fine.isPaid())
                .build();
    }
}
