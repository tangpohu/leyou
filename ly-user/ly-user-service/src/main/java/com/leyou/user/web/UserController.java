package com.leyou.user.web;

import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    /*
    * 校验数据
    * */
    @GetMapping("/check{data}/{type}")
    public ResponseEntity<Boolean> checkData(
            @PathVariable("data")String data,@PathVariable("type")Integer type){
        return ResponseEntity.ok(userService.chackData(data,type));
    }
}