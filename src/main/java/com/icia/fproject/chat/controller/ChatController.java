package com.icia.fproject.chat.controller;


import com.icia.fproject.chat.model.MessageDTO;

import com.icia.fproject.chat.service.ChatService;
import com.icia.fproject.user.parent.dto.ParentInfoEntity;

import com.icia.fproject.user.parent.dto.StudentInfoDTO;

import com.icia.fproject.user.parent.service.ClassRequestService;
import com.icia.fproject.user.parent.service.ParentInfoService;


import com.icia.fproject.user.teacher.dto.TeacherInfoEntity;
import com.icia.fproject.user.teacher.service.ClassProgressService;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService msvc;
    private final ClassProgressService cpsvc;
    private HttpSession session;
    private final ClassRequestService crsvc;
    private final ParentInfoService psvc;


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    // -> SimpMessagingTemplate : Spring에서 제공하는 메시지 전송 도구로, 특정 사용자나 특정 경로로 메시지를 보낼 때 사용


    @MessageMapping("/private-message")
    public void recMessage(@Payload MessageDTO message) {
        if (message.getMessage() != null && !message.getMessage().trim().isEmpty()) {
            System.out.println("송신자: " + message.getSenderName());
            System.out.println("수신자: " + message.getReceiverName());

            // 메시지 저장
            msvc.saveMessage(message);

            // 수신자에게 전송
            simpMessagingTemplate.convertAndSendToUser(
                    message.getReceiverName(),
                    "/private-message",
                    message
            );

            // 송신자에게도 전송
            simpMessagingTemplate.convertAndSendToUser(
                    message.getSenderName(),
                    "/private-message",
                    message
            );
        }
    }


    // 학생 채팅
    @GetMapping("/chat")
    public ModelAndView showChatPage(HttpSession session) {
        System.out.println("chat 페이지 이동 확인");
        ModelAndView mav = new ModelAndView();

        // 로그인 상태 확인
        String loginId = (String) session.getAttribute("parentLoginId");
        System.out.println("chat loginId 확인 :" + loginId);
        if (loginId == null) {
            mav.setViewName("redirect:/pLoginForm");
            return mav;
        }

        // 부모 정보와 자녀 목록 가져오기
        ParentInfoEntity parent = msvc.findParentById(loginId);
        List<StudentInfoDTO> studentList = msvc.findStudentByParentId(loginId);

        System.out.println("채팅 학부모 정보 불러오기 확인: " + parent.getPId());
        System.out.println("채팅 학생 정보 불러오기 확인: " + studentList);

        // Model에 데이터 추가
        mav.addObject("parentInfo", parent);
        mav.addObject("studentList", studentList);

        mav.setViewName("chat"); // chat.html로 이동
        return mav;
    }


    // 선생님 채팅
    @GetMapping("/teacher/tChat")
    public ModelAndView tChat(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        String loginId = (String) session.getAttribute("teacherLoginId");
        if (loginId == null) {
            mav.setViewName("redirect:/tLoginForm"); // 로그인x->로그인 페이지로 리다이렉트
            return mav;
        }
        TeacherInfoEntity teacher = msvc.findTeacherById(loginId);
        // Model에 데이터 추가
        mav.addObject("TeacherInfo", teacher);

        return mav;
    }

    @GetMapping("/teacher/tChat/{tId}")
    public ModelAndView tStu(@PathVariable String tId) {
        System.out.println("선생님 학생정보 보기 메소드 확인 || tId:" + tId);
        return cpsvc.tStu(tId);
    }


    @GetMapping("/chat/api/getTeachersByStudent/{sId}")
    public ResponseEntity<List<Map<String, Object>>> getTeachersByStudent(@PathVariable Long sId) {
        System.out.println("[Controller] Fetching teachers for studentId: " + sId);
        List<Map<String, Object>> teacherList = cpsvc.getTeacherListByStudent(sId);
        return ResponseEntity.ok(teacherList);
    }

    // 메시지 저장 API
    @PostMapping("/chat/api/send")
    public ResponseEntity<String> saveMessage(@RequestBody MessageDTO messageDTO) {
        msvc.saveMessage(messageDTO);
        return ResponseEntity.ok("메시지가 성공적으로 저장되었습니다.");
    }

    @GetMapping("/chat/api/history")
    public ResponseEntity<List<MessageDTO>> getChatHistory(
            @RequestParam Long sId,  // 학생 ID (Long)
            @RequestParam String tId // 선생님 ID (String)
    ) {
        System.out.println("[채팅 내역 요청] 학생 ID(sId): " + sId + ", 선생님 ID(tId): " + tId);

        //  서비스에 학생(sId)과 선생님(tId) 정보 전달
        List<MessageDTO> messages = msvc.getChatHistory(sId, tId);

        System.out.println("[조회된 메시지 개수] " + messages.size());
        return ResponseEntity.ok(messages);
    }

    // 전체 채팅 내역 불러오는 api
    @GetMapping("/chat/api/history/all")
    public ResponseEntity<List<MessageDTO>> getAllChatHistory(@RequestParam String username) {
        List<MessageDTO> messages = msvc.getAllChatHistory(username);
        return ResponseEntity.ok(messages);
    }

}








