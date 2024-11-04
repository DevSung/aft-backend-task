package com.example.api.user.controller;


import com.example.api.user.service.UserService;
import com.example.auth.config.UserIdx;
import com.example.domain.payload.user.request.JoinRequest;
import com.example.domain.payload.user.request.LoginRequest;
import com.example.domain.payload.user.request.UpdatePasswordRequest;
import com.example.domain.payload.user.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     *
     * @return 성공
     */
    @PostMapping("/join")
    public ResponseEntity<Long> joinUser(@Valid @RequestBody JoinRequest joinRequest) {
        userService.joinUser(joinRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그인 API
     *
     * @return Token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    /**
     * 회원 비밀번호 변경 API
     */
    @PutMapping("/password")
    public ResponseEntity<Long> updateUser(@UserIdx Long userIdx, @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(userIdx, updatePasswordRequest);
        return ResponseEntity.ok().build();
    }


}
