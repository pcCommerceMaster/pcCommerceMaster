package com.pcproject.global.interceptor;

import com.pcproject.admin.controller.SessionConst;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AdminRepository adminRepository;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        // 기존 세션이 있는지 확인 (새 세션 생성 방지)
        HttpSession session = request.getSession(false);

        // 세션이 없으면 로그인하지 않은 상태
        if (session == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 세션에 저장된 로그인 관리자 정보 조회
        LoginAdmin loginAdmin =
                (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);

        // 로그인 정보가 없으면 인증 실패
        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // CS_ADMIN 권한 검증
        // 주문 CUD API는 CS 담당 관리자만 접근 가능
        if (loginAdmin.getRole() != AdminRole.CS_ADMIN) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 실제 Admin 엔티티 조회
        // 세션 DTO를 신뢰하지 않고 DB에서 다시 조회 (보안 강화)
        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        // 이후 Controller에서 사용할 수 있도록 request attribute에 저장
        request.setAttribute("admin", admin);

        // 인증/인가 성공 → Controller 진입 허용
        return true;
    }

}
