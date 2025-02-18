package com.icia.fproject.admin.dto;

import com.icia.fproject.user.parent.dto.ParentInfoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SIGNUP_REQUEST")
public class SignupRequestEntity {
    @Id
    @Column(name = "SREQID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동생성 수정
    private Long sReqId;        // 상담 신청서 ID

    @Nationalized
    @Column(name = "SREQNAME", length = 100)
    private String sReqName;    // 신청자명

    @Nationalized
    @Column(name = "SREQPHONE", length = 20)
    private String sReqPhone;   // 연락처

    @Nationalized
    @Column(name = "SREQEMAIL", length = 100)
    private String sReqEmail;   // 이메일

    @Nationalized
    @Column(name = "SREQTYPE", length = 50)
    private String sReqType;    // 상담유형

    @Column(name = "SREQDATE")
    @Temporal(TemporalType.DATE)
    private LocalDate sReqDate; // 신청일

    @Column(name = "SREQSTARTTIME")
    private Long sReqStartTime; // 희망시간대(시작)

    @Column(name = "SREQENDTIME")
    private Long sReqEndTime;   // 희망시간대(끝)

    @Nationalized
    @Column(name = "SREQSTATUS", length = 50)
    private String sReqStatus;  // 신청서 확인 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PID")
    private ParentInfoEntity pId;   // 신청한 학부모


    public static SignupRequestEntity toEntity(SignupRequestDTO dto) {
        SignupRequestEntity entity = new SignupRequestEntity();

        entity.setSReqId(dto.getSReqId());
        entity.setSReqName(dto.getSReqName());
        entity.setSReqPhone(dto.getSReqPhone());
        entity.setSReqEmail(dto.getSReqEmail());
        entity.setSReqType(dto.getSReqType());
        entity.setSReqStartTime(dto.getSReqStartTime());
        entity.setSReqEndTime(dto.getSReqEndTime());
        entity.setSReqStatus(dto.getSReqStatus());

        ParentInfoEntity parent = new ParentInfoEntity();
        parent.setPId(dto.getPId());
        entity.setPId(parent);

        return entity;
    }

}