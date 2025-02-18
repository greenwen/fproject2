package com.icia.fproject.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageMoveController {

    private final HttpSession session;
    private final HttpServletRequest request;

    // 회원가입 페이지
    @GetMapping("/pJoinForm")
    public String joinForm() {
        return "join";
    }

    @GetMapping("teacher/myPageT")
    public String teacherMyPage() {

        return "mySchedule";
    }

    @GetMapping("/tLoginForm")
    public String teacherLogin() {
        return "teacher/login";
    }

    @GetMapping("/pLoginForm")
    public String parentLogin() {
        return "login";
    }

    // 학습신청페이지 이동
    @GetMapping("/parent/classRequest")
    public String classRequest() {
        return "/parent/classRequest";
    }

    // 선생님 Main페이지
    @GetMapping("teacher/Tmain")
    public String teacherMain() {
        return "/teacher/Tmain";
    }

    @GetMapping("/teacher/myStudents")
    public String teacherStudents() {
        return "/teacher/myStudents";
    }

    @GetMapping("/teacher/myInfo")
    public String teacherInfo() {
        return "myInfoT";
    }

    // 로딩 페이지
    @GetMapping("/BHstart")
    public String loadingScreen() {
        return "BHstart";
    }

}
