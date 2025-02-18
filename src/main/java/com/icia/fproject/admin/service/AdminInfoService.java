package com.icia.fproject.admin.service;

import com.icia.fproject.admin.dao.AdminInfoRepository;
import com.icia.fproject.admin.dto.AdminInfoDTO;
import com.icia.fproject.admin.dto.AdminInfoEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminInfoService {

    private final AdminInfoRepository arepo;

    private final HttpSession session;

    public ModelAndView aLogin(AdminInfoDTO admin) {
        System.out.println("[2] controller → service || admin : " + admin);
        ModelAndView mav = new ModelAndView();

        // (1) 아이디 존재여부 확인
        Optional<AdminInfoEntity> entity = arepo.findById(admin.getAId());
        if (entity.isPresent()) {
            // (2) 비번 일치 확인
            if (admin.getAPass().equals(entity.get().getAPass())) {
                // (3) entity → dto
                AdminInfoDTO login = AdminInfoDTO.toDTO(entity.get());            // entity에 저장된 데이터를 가져와서 dto로 변환, DTO타입의 login에 저장
                session.setAttribute("adminLoginId", login.getAId());

                mav.setViewName("/admin/main");
            } else {
                System.out.println("비밀번호가 틀렸습니다.");
                mav.setViewName("redirect:/adminlogin");
            }
        } else {
            System.out.println("아이디가 존재하지 않습니다.");
            mav.setViewName("redirect:/adminlogin");
        }
        return mav;
    }
}
