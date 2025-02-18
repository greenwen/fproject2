package com.icia.fproject.admin.dto;

import com.icia.fproject.user.teacher.dto.ScheduleEntity;
import lombok.Data;

@Data
public class ClassPaymentDTO {

    private Long clPayId;           // 학습신청 결제 id
    private int clPayPrice;         // 총가격
    private String clStatus;        // 결제 상태

    private Long clReqId;           // 학습신청 id
    private String pId;             // 학습신청한 학부모 id
    private Long sId;               // 학생 id
    private String sName;           // 학생이름
    private String tName;           // 선생님 성함
    private String planDay;         // 요일
    private String planStartTime;   // 시작 시간
    private String planEndTime;     // 끝나는 시간

    public static ClassPaymentDTO toDTO(ClassPaymentEntity paymentEntity, ScheduleEntity entity) {
        ClassPaymentDTO dto = new ClassPaymentDTO();

        dto.setClPayId(paymentEntity.getClPayId());
        dto.setClPayPrice(paymentEntity.getClPayPrice());
        dto.setClStatus(paymentEntity.getClStatus());

        dto.setClReqId(entity.getClReqId().getClReqId());
        dto.setSId(entity.getSId().getSId());
        dto.setSName(entity.getSId().getSName());
        dto.setTName(entity.getTId().getTName());
        dto.setPlanDay(entity.getPlanDay());
        dto.setPlanStartTime(entity.getPlanStartTime());
        dto.setPlanEndTime(entity.getPlanEndTime());
        dto.setPId(entity.getSId().getPId().getPId());

        return dto;
    }

}
