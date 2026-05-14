package org.sparta.demo2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test1")
    public ResponseEntity<String> test1() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test1.txt"); // 출력의 방향이 test1.txt로 변경 -> 다 출력이 되면 다운로드 된다.
        return new ResponseEntity<>("Hello", headers, HttpStatus.OK);
    }
}
