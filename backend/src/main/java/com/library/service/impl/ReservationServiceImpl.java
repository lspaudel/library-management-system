package com.library.service.impl;

import com.library.dto.response.ReservationResponse;
import com.library.entity.*;
import com.library.exception.BusinessException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public ReservationResponse reserve(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));

        if (reservationRepository.existsByBookIdAndMemberIdAndStatus(bookId, memberId, Reservation.ReservationStatus.PENDING)) {
            throw new BusinessException("You already have a pending reservation for this book");
        }

        Reservation reservation = Reservation.builder()
                .book(book).member(member)
                .reservedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(3))
                .status(Reservation.ReservationStatus.PENDING)
                .build();
        return toResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            throw new BusinessException("Cannot cancel a reservation that is not pending");
        }
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        return toResponse(reservationRepository.save(reservation));
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponse> getByMember(Long memberId, int page, int size) {
        return reservationRepository.findByMemberIdOrderByReservedAtDesc(memberId, PageRequest.of(page, size))
                .map(this::toResponse);
    }

    private ReservationResponse toResponse(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                .bookId(r.getBook().getId()).bookTitle(r.getBook().getTitle())
                .memberId(r.getMember().getId()).memberName(r.getMember().getName())
                .reservedAt(r.getReservedAt()).expiresAt(r.getExpiresAt())
                .status(r.getStatus())
                .build();
    }
}
