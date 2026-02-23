package com.pcproject.admin.service;

import com.pcproject.admin.config.PasswordEncoder;
import com.pcproject.admin.dto.ChangePasswordRequest;
import com.pcproject.admin.dto.MeResponse;
import com.pcproject.admin.dto.MeUpdateRequest;
import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMeService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MeResponse me(Long myId) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(myId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        return MeResponse.from(admin);
    }

    @Transactional
    public void updateMe(Long myId, MeUpdateRequest req) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(myId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (!req.getEmail().equals(admin.getEmail())
                && adminRepository.existsByEmailAndDeletedAtIsNull(req.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_INPUT); // ✅ ADMIN_DUPLICATE_EMAIL 추천
        }

        admin.updateProfile(req.getName(), req.getEmail(), req.getPhoneNumber());
    }

    @Transactional
    public void changePassword(Long myId, ChangePasswordRequest req) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(myId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (!passwordEncoder.matches(req.getCurrentPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.ADMIN_INVALID_PASSWORD);
        }

        admin.changePassword(passwordEncoder.encode(req.getNewPassword()));
    }
}
