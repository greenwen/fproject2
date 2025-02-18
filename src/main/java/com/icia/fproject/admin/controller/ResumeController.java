package com.icia.fproject.admin.controller;

import com.icia.fproject.admin.dto.ResumeRequestDTO;
import com.icia.fproject.admin.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {
    private final ResumeService rService;

    // 입사지원 작성 페이지 이동
    @GetMapping("/rwrite")
    public String rWriteForm() {
        return "resume/rwrite";
    }

    // 입사지원 제출
    @PostMapping("/submit")
    public ModelAndView submitResume(@ModelAttribute ResumeRequestDTO resume) {
        return rService.submitResume(resume);
    }

    // 입사지원서 목록 페이지
    @GetMapping("/list")
    public ModelAndView list() {
        return rService.list();
    }

    // rView : 입사지원서 상세보기
    @GetMapping("/rView/{resId}")
    public ModelAndView rView(@PathVariable Long resId) {
        return rService.rView(resId);
    }


    // 이력서파일 다운로드 기능 추가
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {

            Resource resource = rService.downloadResume(fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}



