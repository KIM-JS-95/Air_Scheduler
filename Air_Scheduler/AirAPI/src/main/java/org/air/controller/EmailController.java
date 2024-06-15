package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.StatusEnum;
import org.air.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmailController {

    @Autowired
    private CustomUserDetailService customUserDetailService;
    // 도메인 구매해야함 (http://~~?pilotcode=?)
    // login -> 디바이스가 변경되었을 경우 디바이스 인증 실행
    @GetMapping("/device")
    public ResponseEntity device_id_update(@RequestParam("userid") String userid, @RequestParam("androidid") String androidid) { // 인코딩된 파일럿 코드
        System.out.println(userid);
        boolean answer = customUserDetailService.certification_devide(userid, androidid);
        if (answer) {
            return ResponseEntity.ok()
                    .body("기기 인증이 완료되었습니다. 다시 로그인 해주세요!.");
        } else {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                    .body("기기 인증에 실패했습니다. 개발자에게 직접 문의주시기 바랍니다.");
        }
    }

}
