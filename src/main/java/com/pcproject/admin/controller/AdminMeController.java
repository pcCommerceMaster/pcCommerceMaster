package com.pcproject.admin.controller;

import com.pcproject.admin.dto.ChangePasswordRequest;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.admin.dto.MeResponse;
import com.pcproject.admin.dto.MeUpdateRequest;
import com.pcproject.admin.service.AdminMeService;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins/me")
public class AdminMeController {

    private final AdminMeService adminMeService;

    private Long myId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) throw new CustomException(ErrorCode.UNAUTHORIZED);

        LoginAdmin loginAdmin = (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);
        if (loginAdmin == null) throw new CustomException(ErrorCode.UNAUTHORIZED);

        return loginAdmin.getId();
    }

    @GetMapping
    public ApiResponse<MeResponse> me(HttpServletRequest request) {
        return ApiResponse.success("내 프로필 조회 성공", adminMeService.me(myId(request)));
    }

    @PutMapping
    public ApiResponse<Void> updateMe(@Valid @RequestBody MeUpdateRequest req, HttpServletRequest request) {
        adminMeService.updateMe(myId(request), req);
        return ApiResponse.success("내 프로필 수정 성공", null);
    }

    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest req, HttpServletRequest request) {
        adminMeService.changePassword(myId(request), req);
        return ApiResponse.success("비밀번호 변경 성공", null);
    }
}
