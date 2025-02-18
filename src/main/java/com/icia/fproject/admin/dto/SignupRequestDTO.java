package com.icia.fproject.admin.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequestDTO {

    private Long sReqId;                // 상담 신청서 id
    private String sReqName;            // 상담 신청자 성함 //TODO : 학부모 넣을시 제거
    private String sReqPhone;           // 신청자 연락처    //TODO : 학부모 넣을시 제거
    private String sReqEmail;           // 신청자 이메일    //TODO : 학부모 넣을시 제거
    private String sReqType;            // 원하는 상담 유형
    private LocalDate sReqDate;         // 상담 가능한 날짜
    private Long sReqStartTime;         // 상담 가능 시간대 (시작)
    private Long sReqEndTime;           // 상담 가능 시간대 (끝)
    private String sReqStatus;          // 상담신청 확인 상태

    private String pId;                 // 상담신청한 학부모

    public static SignupRequestDTO toDTO (SignupRequestEntity entity) {
        SignupRequestDTO dto = new SignupRequestDTO();

        dto.setSReqId(entity.getSReqId());
        dto.setSReqName(entity.getSReqName());      //TODO : 학부모 넣을시 제거
        dto.setSReqPhone(entity.getSReqPhone());    //TODO : 학부모 넣을시 제거
        dto.setSReqEmail(entity.getSReqEmail());    //TODO : 학부모 넣을시 제거
        dto.setSReqType(entity.getSReqType());
        dto.setSReqDate(entity.getSReqDate());
        dto.setSReqStartTime(entity.getSReqStartTime());
        dto.setSReqEndTime(entity.getSReqEndTime());
        dto.setSReqStatus(entity.getSReqStatus());

        dto.setPId(entity.getPId().getPId());

        return dto;
    }


}
