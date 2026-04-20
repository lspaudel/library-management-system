package com.library.service.impl;

import com.library.dto.response.FineResponse;
import com.library.entity.Fine;
import com.library.exception.BusinessException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FineServiceImpl {

    private final FineRepository fineRepository;

    @Transactional(readOnly = true)
    public Page<FineResponse> getByMember(Long memberId, int page, int size) {
        return fineRepository.findByBorrowRecordMemberIdOrderByCreatedAtDesc(
                memberId, PageRequest.of(page, size)).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalUnpaidByMember(Long memberId) {
        return fineRepository.getTotalUnpaidFinesByMember(memberId);
    }

    public FineResponse payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", fineId));
        if (fine.isPaid()) {
            throw new BusinessException("Fine is already paid");
        }
        fine.setPaid(true);
        fine.setPaidAt(LocalDateTime.now());
        return toResponse(fineRepository.save(fine));
    }

    private FineResponse toResponse(Fine f) {
        return FineResponse.builder()
                .id(f.getId())
                .borrowRecordId(f.getBorrowRecord().getId())
                .bookTitle(f.getBorrowRecord().getBook().getTitle())
                .memberName(f.getBorrowRecord().getMember().getName())
                .amount(f.getAmount()).paid(f.isPaid()).paidAt(f.getPaidAt())
                .createdAt(f.getCreatedAt())
                .build();
    }
}
