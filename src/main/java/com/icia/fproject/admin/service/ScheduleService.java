package com.icia.fproject.admin.service;

import com.icia.fproject.admin.dao.ClassPaymentRepository;
import com.icia.fproject.admin.dto.ClassPaymentEntity;
import com.icia.fproject.user.parent.dao.ClassRequestRepository;
import com.icia.fproject.user.parent.dao.StudentInfoRepository;
import com.icia.fproject.user.parent.dto.ClassRequestEntity;
import com.icia.fproject.user.parent.dto.StudentInfoEntity;
import com.icia.fproject.user.teacher.dao.ClassProgressRepository;
import com.icia.fproject.user.teacher.dao.ScheduleRepository;
import com.icia.fproject.user.teacher.dao.TeacherInfoRepository;
import com.icia.fproject.user.teacher.dto.ClassProgressEntity;
import com.icia.fproject.user.teacher.dto.ScheduleEntity;
import com.icia.fproject.user.teacher.dto.SchedulesDTO;
import com.icia.fproject.user.teacher.dto.TeacherInfoEntity;
import com.icia.fproject.user.teacher.service.TeacherInfoService;
import com.icia.fproject.vrp.util.JsonResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository schrepo;
    private final TeacherInfoRepository trepo;
    private final StudentInfoRepository sturepo;
    private final ClassRequestRepository clreqrepo;
    private final ClassPaymentRepository clpayrepo;
    private final ClassProgressRepository clprogrepo;
    private final TeacherInfoService tsvc;


    private ModelAndView mav;

    // 한국어 요일을 DayOfWeek와 일치시키기
    private static final Map<String, DayOfWeek> KOREAN_DAY_MAP = new HashMap<>();

    static {
        KOREAN_DAY_MAP.put("일요일", DayOfWeek.SUNDAY);
        KOREAN_DAY_MAP.put("월요일", DayOfWeek.MONDAY);
        KOREAN_DAY_MAP.put("화요일", DayOfWeek.TUESDAY);
        KOREAN_DAY_MAP.put("수요일", DayOfWeek.WEDNESDAY);
        KOREAN_DAY_MAP.put("목요일", DayOfWeek.THURSDAY);
        KOREAN_DAY_MAP.put("금요일", DayOfWeek.FRIDAY);
        KOREAN_DAY_MAP.put("토요일", DayOfWeek.SATURDAY);
    }

    // 선생님 일정표 불러오기 메소드
    @Transactional
    public ModelAndView getScheduleList() {
        System.out.println("선생님 일정표 불러오기 메소드 || [2] controller → service");
        mav = new ModelAndView();

        List<ScheduleEntity> scheduleEntities = schrepo.findAll();

        List<SchedulesDTO> scheduleList = new ArrayList<>();

        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            SchedulesDTO schedule = SchedulesDTO.toDTO(scheduleEntity);
            schedule.setSName(scheduleEntity.getSId().getSName());
            scheduleList.add(schedule);
        }

        mav.addObject("scheduleList", scheduleList);
        mav.setViewName("admin/scheduleList");

        return mav;
    }

    // 일정 저장
    @Transactional
    public JsonResult saveSchedules(ScheduleEntity scheduleEntity, String tId, Long sId, Long clReqId) {
        // ScheduleEntity에 TeacherInfoEntity, StudentInfoEntity, ClassRequestEntity 설정) {

        // 일정 중 신청서로 이미 만들어진 일정이 있는지 확인: 여러 개 등록 돼있어서 현재 사용하는 코드, 삭제 예정
        ScheduleEntity existingSched = schrepo.findFirstByclReqId_clReqId(clReqId);

        // ScheduleEntity existingSched = schrepo.findByclReqId_clReqId(clReqId);

        // 있다면 저장 취소
        if (existingSched != null) {
            return JsonResult.success("이미 일정이 지정된 학습입니다");
        } else {
            try {
                // TeacherInfoEntity 조회
                TeacherInfoEntity teacher = trepo.findById(tId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 TeacherInfoEntity를 찾을 수 없습니다: " + tId));

                // StudentInfoEntity 조회
                StudentInfoEntity student = sturepo.findBysId(sId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 StudentInfoEntity를 찾을 수 없습니다: " + sId));

//            // ClassRequestEntity 조회
                ClassRequestEntity classReq = clreqrepo.findByclReqId(clReqId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 ClassRequestEntity를 찾을을 수 없습니다: " + clReqId));

                // ScheduleEntity에 TeacherInfoEntity와 StudentInfoEntity 설정
                scheduleEntity.setTId(teacher);
                scheduleEntity.setSId(student);
                scheduleEntity.setClReqId(classReq);

                // 디버깅 로그
//                System.out.println("받은 데이터: " + scheduleEntity);
//                System.out.println("clReqId: " + scheduleEntity.getClReqId().getClReqId());
//                System.out.println("tId: " + scheduleEntity.getTId().getTId());
//                System.out.println("sId: " + scheduleEntity.getSId().getSId());
//                System.out.println("planDay: " + scheduleEntity.getPlanDay());

                // 저장
                tsvc.save(scheduleEntity);

                return JsonResult.success("일정이 성공적으로 저장되었습니다.");

            } catch (Exception e) {
                e.printStackTrace();
                return JsonResult.error("일정 저장 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }

    // 결제 정보 DB에 저장 (ajax)
    @Transactional
    public String sendPaymentInfo(Long planId) {
        // 확인
//        System.out.println("결제 정보 저장 메소드 확인 : [2] controller → service || planId : " + planId);
        String result = "NO";

        // 일정 존재 여부 확인
        Optional<ScheduleEntity> scheduleCheck = schrepo.findById(planId);
        if (scheduleCheck.isPresent()) {
            ScheduleEntity scheduleEntity = scheduleCheck.get();

            // 일정 상태 변경
            schrepo.updateScheduleStatus(scheduleEntity.getPlanId());

            // 새로운 결제정보 설정 및 저장
            ClassPaymentEntity classPayment = new ClassPaymentEntity();
            classPayment.setClPayPrice(99000);
            classPayment.setClStatus("결제 진행 중");
            classPayment.setClReqId(scheduleEntity.getClReqId());
            classPayment.setPId(scheduleEntity.getClReqId().getPId());

            // 결제 정보 저장
            clpayrepo.save(classPayment);

            // 해당 학습신청 정보 변경
            clreqrepo.updateClReqStatus(scheduleEntity.getClReqId().getClReqId(), "결제 가능");
//            clreqrepo.updateClReqStatus(scheduleEntity.getClReqId().getClReqId(), "결제 가능");

            result = "OK";
            // System.out.println(scheduleEntity);
        }
        return result;
    }

    // 결제 정보 수정
    @Transactional
    public boolean updatePaymentStatus(Long clPayId, String clStatus) {
        Optional<ClassPaymentEntity> paymentOpt = clpayrepo.findById(clPayId);
        ClassProgressEntity classProg = new ClassProgressEntity();
        if (paymentOpt.isPresent()) {
            ClassPaymentEntity payment = paymentOpt.get();
            payment.setClStatus(clStatus); //  "결제 완료"로 변경
            clpayrepo.save(payment);

            // Class Request 결제 상태 변경
            clreqrepo.updateClReqStatus(payment.getClReqId().getClReqId(), "결제 완료");

            ScheduleEntity schedule = schrepo.findFirstByclReqId_clReqId(payment.getClReqId().getClReqId());
            schrepo.updateScheduleStatusComplete(schedule.getPlanId()); // 관리자 측 일정 정보 "결제 완료"로 변경

            classProg.setClProgPercent(0.0);
            classProg.setTId(schedule.getTId());
            classProg.setSId(schedule.getSId());
            classProg.setClProgSubject(schedule.getTId().getTSubject());
            classProg.setClProgStatus("진행 중");
            classProg.setClProgLevel(schedule.getTId().getTLevel());
            classProg.setClProgDay(schedule.getPlanDay());
//            classProg.setClProgBook("교재용"); // 재용씨..

            LocalDate startDate = getNextDayOfWeek(schedule.getPlanDay()); // 시작일
            LocalDate endDate = calculateEndDate(startDate, schedule.getPlanDay()); // 3개월 뒤 종료일

            classProg.setClProgStartDate(startDate);
            classProg.setClProgEndDate(endDate);

            clprogrepo.save(classProg); // ClassProgressEntity에 "진행중"로 저장

//            System.out.println(" 결제 상태 변경 완료: " + clPayId + " → " + clStatus);
            return true;
        }
        return false;
    }

    // 시작일 설정 메소드
    public LocalDate getNextDayOfWeek(String dayName) {
        DayOfWeek dayOfWeek = KOREAN_DAY_MAP.get(dayName);
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("요일 이름 설정 오류: " + dayName);
        }
        // 시작일을 오늘 기준으로 다음 요일 날짜로 설정
        LocalDate startDay = LocalDate.now().with(TemporalAdjusters.next(dayOfWeek));
        // 날짜가 오늘 기준으로 1~4일 후라면 다음주 날짜로 설정
        if (ChronoUnit.DAYS.between(LocalDate.now(), startDay) + 1 < 5) {
            startDay = startDay.with(TemporalAdjusters.next(dayOfWeek));
        }
        return startDay;
    }

    // 종료일 설정 메소드
    public LocalDate calculateEndDate(LocalDate startDate, String dayName) {
        LocalDate endDate;
        // 3개월 뒤 날짜 확인
        LocalDate checkEndDate = startDate.plusMonths(3);
//        System.out.println("3개월 뒤 날짜 확인: " + checkEndDate);
        // 날짜의 요일 확인
        DayOfWeek rawEndDay = checkEndDate.getDayOfWeek();
//        System.out.println("요일 확인: " + rawEndDay);
        // 시작일 요일 불러오기
        DayOfWeek startDayName = KOREAN_DAY_MAP.get(dayName);
        // 시작일과 계산된 종료일의 요일 이름 비교
        if (rawEndDay.equals(startDayName)) {
            // 같은 요일이면 날짜 그대로
            endDate = checkEndDate;
        } else {
            // 다음 요일의 날짜 확인 + 설정
            endDate = checkEndDate.with(TemporalAdjusters.previous(startDayName));
//            System.out.println("요일 설정 된 날짜 확인: " + endDate);
            // 횟수 확인
            long howManyDays = ChronoUnit.WEEKS.between(startDate, endDate) + 1;
//            System.out.println("요일 갯수 확인: " + howManyDays);
            if (howManyDays < 12) {
                // 12회 이하라면 다음주의 날짜로 설정
                endDate = checkEndDate.with(TemporalAdjusters.next(startDayName));
            } else if (howManyDays > 12) {
                // 12회 넘는다면 전 주의 날짜로 설정
                endDate = endDate.with(TemporalAdjusters.previous(startDayName));
            }
        }
//        System.out.println("최종 날짜 확인 : " + endDate);
        return endDate;
    }

    @Transactional
    public String deleteClass(Long classReqId) {
        String result = null;
        Optional<ClassRequestEntity> checkRequest = clreqrepo.findById(classReqId);
        if (checkRequest.isPresent()) {
            try {
                Optional<ScheduleEntity> checkSched = Optional.ofNullable(schrepo.findByclReqId_clReqId(classReqId));
                if (checkSched.isPresent()) {
                    ScheduleEntity sched = checkSched.get();
                    StudentInfoEntity student = sched.getSId();
                    TeacherInfoEntity teacher = sched.getTId();

                    if (student != null && teacher != null) {
                        Long sId = student.getSId();
                        String tId = teacher.getTId();

                        System.out.println("sId: " + sId + ", tId: " + tId);

                        Optional<ClassProgressEntity> checkClProg = clprogrepo.findBysIdAndtId(sId, tId);
                        if (checkClProg.isPresent()) {
                            clprogrepo.deleteBysIdAndtId(sId, tId);
                        }
                    }
                }
                clreqrepo.delete(checkRequest.get());
                result = "OK";
            } catch (Exception e) {
                result = "NO";
                e.printStackTrace();
            }
        }
        return result;
    }
}
