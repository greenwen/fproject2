package com.icia.fproject.user.teacher.dao;

import com.icia.fproject.user.teacher.dto.ScheduleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository <ScheduleEntity,Long> {

    List<ScheduleEntity>findBytId_tId(String tId);

    List<ScheduleEntity> findBytId_tIdOrderByPlanIdAsc(String tId);

    ScheduleEntity findByclReqId_clReqId(Long clReqId);

    // 결제 대기로 상태 변경
    @Transactional
    @Modifying
    @Query("UPDATE ScheduleEntity sch set sch.planStatus = '결제 대기' WHERE sch.planId = :planId")
    void updateScheduleStatus(Long planId);
    
    // 결제 완료로 상태 변경
    @Transactional
    @Modifying
    @Query("UPDATE ScheduleEntity sch set sch.planStatus = '결제 완료' WHERE sch.planId = :planId")
    void updateScheduleStatusComplete(Long planId);

    // 확인용, 삭제 예정
    ScheduleEntity findFirstByclReqId_clReqId(Long clReqId);
}
