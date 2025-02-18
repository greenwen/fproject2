package com.icia.fproject.vrp.controller;


import com.icia.fproject.admin.service.ClassService;
import com.icia.fproject.admin.service.ScheduleService;
import com.icia.fproject.user.parent.dao.ClassRequestRepository;
import com.icia.fproject.user.parent.dao.StudentInfoRepository;
import com.icia.fproject.user.parent.dto.ClassRequestDTO;
import com.icia.fproject.user.parent.dto.ClassRequestEntity;
import com.icia.fproject.user.parent.dto.StudentInfoEntity;
import com.icia.fproject.user.teacher.dao.TeacherInfoRepository;
import com.icia.fproject.user.teacher.dto.ScheduleEntity;
import com.icia.fproject.user.teacher.dto.TeacherInfoEntity;
import com.icia.fproject.user.teacher.service.TeacherInfoService;
import com.icia.fproject.vrp.entity.NodeCostEntity;
import com.icia.fproject.vrp.entity.NodeEntity;
import com.icia.fproject.vrp.service.NodeCostService;
import com.icia.fproject.vrp.service.NodeService;
import com.icia.fproject.vrp.util.JsonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final NodeService nodesvc;
    private final NodeCostService nodeCostsvc;
    private final ScheduleService schsvc;

    private final ClassService csvc;
    private final TeacherInfoService tsvc;

    private final StudentInfoRepository sturepo;
    private final TeacherInfoRepository trepo;
    private final ClassRequestRepository clrrepo;


    @GetMapping("/maptest")
    public String maptest() {
        return "maptest";
    }


    @PostMapping("/vrp")
    @ResponseBody
    public JsonResult optimizeRoute(@RequestBody List<NodeEntity> nodeList) throws IOException, InterruptedException {
        if (nodeList == null || nodeList.isEmpty()) {
            return JsonResult.error("노드 리스트가 비어 있습니다.");
        }
        return nodeCostsvc.add(nodeList); // NodeCostService.add 호출
    }

    @GetMapping("/teacherTest")
    @ResponseBody
    public JsonResult teacherTest(@RequestParam String tId, @RequestParam String day) {
        JsonResult result = new JsonResult();

        try {
            // 선생님 ID로 요일별 데이터를 DTO 형태로 가져옴
            Map<String, List<ClassRequestDTO>> groupedData = csvc.getGroupedAddressesByDay(tId);

            // 선택된 요일에 해당하는 데이터만 추출
            List<ClassRequestDTO> dayData = groupedData.getOrDefault(day, new ArrayList<>());

            // NodeEntity로 변환 및 필요한 정보를 설정
            List<NodeEntity> nodeList = new ArrayList<>();
            for (ClassRequestDTO data : dayData) {
                String address = data.getClReqPlace();
                Long sId = data.getSId();
                Long clReqId = data.getClReqId();

                System.out.println("학습 신청 아이디 확인 => clReqId : " + clReqId);

                if (address != null) {
                    // 주소 정보를 사용해 NodeEntity 생성 및 저장
                    NodeEntity node = nodesvc.saveNodeByClReqPlace(address);

                    // 학생 ID를 Node 이름으로 설정 (없으면 "학생 정보 없음" 설정)
//                    node.setName(sId != null ? "학생 ID: " + sId : "학생 정보 없음");
                    node.setName(sId != null ? String.valueOf(sId) : "학생 정보 없음");
                    node.setPhone(clReqId != null ? String.valueOf(clReqId) : "학습 신청 정보 못 불러왔습니다");
                    nodeList.add(node);
                }
            }

            // 전체 거리와 시간을 계산
            long totalDistance = 0L;
            long totalDuration = 0L;

            for (int i = 1; i < nodeList.size(); i++) {
                NodeEntity prev = nodeList.get(i - 1);
                NodeEntity next = nodeList.get(i);

                // Node 간 거리와 시간 정보를 계산
                NodeCostEntity nodeCost = nodeCostsvc.getNodeCost(prev, next);
                if (nodeCost != null) {
                    if (nodeCost.getDistanceMeter() == null) {
                        nodeCost.setDistanceMeter(0L);
                    }
                    if (nodeCost.getDurationSecond() == null) {
                        nodeCost.setDurationSecond(0L);
                    }
                    totalDistance += nodeCost.getDistanceMeter();
                    totalDuration += nodeCost.getDurationSecond();
                }
            }

            // 결과 데이터를 JsonResult에 추가
            result.addData("nodeList", nodeList);
            result.addData("totalVisit", nodeList.size()); // 총 경유지 개수
            result.addData("totalDistance", totalDistance); // 총 이동 거리
            result.addData("totalDuration", totalDuration); // 총 이동 시간
            result.setMsg("데이터 조회 성공");
            result.setCode(JsonResult.Code.SUCC);

            System.out.println("선생님 경로 최적화 이전 노드리스트 확인: " + nodeList);

        } catch (Exception e) {
            // 예외 처리 및 오류 메시지 반환
            e.printStackTrace();
            result = JsonResult.error("데이터 조회 중 오류 발생: " + e.getMessage());
        }

        return result;
    }


    // 일정 저장 기능 TEST 중
    @PostMapping("/saveSchedule")
    @ResponseBody
    public JsonResult saveSchedule(@RequestBody ScheduleEntity scheduleEntity,
                                   @RequestParam String tId,
                                   @RequestParam Long sId,
                                   @RequestParam Long clReqId) {

//        // ScheduleEntity에 TeacherInfoEntity, StudentInfoEntity, ClassRequestEntity 설정) {
//        try {
//            // TeacherInfoEntity 조회
//            TeacherInfoEntity teacher = trepo.findById(tId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 TeacherInfoEntity를 찾을 수 없습니다: " + tId));
//
//            // StudentInfoEntity 조회
//            StudentInfoEntity student = sturepo.findBysId(sId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 StudentInfoEntity를 찾을 수 없습니다: " + sId));
//
////            // ClassRequestEntity 조회
//            ClassRequestEntity classReq = clrrepo.findByclReqId(clReqId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 ClassRequestEntity를 찾을을 수 없습니다: " + clReqId));
//
//            // ScheduleEntity에 TeacherInfoEntity와 StudentInfoEntity 설정
//            scheduleEntity.setTId(teacher);
//            scheduleEntity.setSId(student);
//            scheduleEntity.setClReqId(classReq);
//
//            // 디버깅 로그
//            System.out.println("받은 데이터: " + scheduleEntity);
//            System.out.println("tId: " + scheduleEntity.getTId().getTId());
//            System.out.println("sId: " + scheduleEntity.getSId().getSId());
//            System.out.println("planDay: " + scheduleEntity.getPlanDay());
//
//            // 저장
//            tsvc.save(scheduleEntity);
//
//            return JsonResult.success("일정이 성공적으로 저장되었습니다.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JsonResult.error("일정 저장 중 오류가 발생했습니다: " + e.getMessage());
//        }
        return schsvc.saveSchedules(scheduleEntity, tId, sId, clReqId);
    }
}