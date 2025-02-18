package com.icia.fproject.admin.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class FaqAnswerDTO {
    private Long ansId;             // 답변 id
    private String ansContents;     // 답변 내용
    private Instant ansDate;        // 작성일
    private String aId;             // 답변한 관리자의 id
    private Long qId;               // 답변한 문의사항의 id

    public static FaqAnswerDTO toDTO (FaqAnswerEntity entity) {
        FaqAnswerDTO dto = new FaqAnswerDTO();
        dto.setAnsId(entity.getAnsId());
        dto.setAnsContents(entity.getAnsContents());
        dto.setAnsDate(entity.getAnsDate());

        dto.setAId(entity.getAId().getAId());
        dto.setQId(entity.getQId().getQId());
        return dto;
    }
}
