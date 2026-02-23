package com.pcproject.admin.service;

import com.pcproject.admin.config.PasswordEncoder;
import com.pcproject.admin.dto.AdminLoginRequest;
import com.pcproject.admin.dto.AdminSignupRequest;
import com.pcproject.admin.dto.AdminSignupResponse;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.entity.AdminStatus;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminSignupResponse signup(AdminSignupRequest req) {
        if (adminRepository.existsByEmailAndDeletedAtIsNull(req.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        String encoded = passwordEncoder.encode(req.getPassword());
        Admin admin = new Admin(req.getName(), req.getEmail(), encoded, req.getPhoneNumber(), req.getRole());
        Admin saved = adminRepository.save(admin);

        return new AdminSignupResponse(saved.getId(), saved.getStatus(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public LoginAdmin login(AdminLoginRequest req) {
        Admin admin = adminRepository.findByEmailAndDeletedAtIsNull(req.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_INVALID_PASSWORD));

        if (!passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.ADMIN_INVALID_PASSWORD);
        }

        // 상태별 로그인 불가
        if (admin.getStatus() != AdminStatus.ACTIVE) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        return new LoginAdmin(admin.getId(), admin.getEmail(), admin.getRole());
    }
}
