package com.pcproject.admin.controller;

import com.pcproject.admin.dto.AdminLoginRequest;
import com.pcproject.admin.dto.AdminSignupRequest;
import com.pcproject.admin.dto.AdminSignupResponse;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.admin.service.AdminAuthService;
import com.pcproject.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/signup")
    public ApiResponse<AdminSignupResponse> signup(@Valid @RequestBody AdminSignupRequest req) {
        AdminSignupResponse res = adminAuthService.signup(req);
        return ApiResponse.created("관리자 회원가입 신청 완료", res);
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@Valid @RequestBody AdminLoginRequest req, HttpServletRequest request) {
        LoginAdmin loginAdmin = adminAuthService.login(req);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_ADMIN, loginAdmin);
        session.setMaxInactiveInterval(60 * 60 * 24);

        return ApiResponse.success("로그인 성공", null);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ApiResponse.success("로그아웃 성공", null);
    }
}
