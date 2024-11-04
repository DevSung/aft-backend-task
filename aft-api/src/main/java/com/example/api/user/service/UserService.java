package com.example.api.user.service;

import com.example.api.exception.CustomException;
import com.example.auth.jwt.CustomUserDetails;
import com.example.auth.jwt.JwtTokenProvider;
import com.example.domain.entity.User;
import com.example.domain.entity.UserRole;
import com.example.domain.enums.RoleType;
import com.example.domain.payload.user.request.JoinRequest;
import com.example.domain.payload.user.request.LoginRequest;
import com.example.domain.payload.user.request.UpdatePasswordRequest;
import com.example.domain.payload.user.response.LoginResponse;
import com.example.domain.repository.user.UserQRepository;
import com.example.domain.repository.user.UserRepository;
import com.example.domain.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserQRepository userQRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입 API
     *
     * @param joinRequest 회원가입 요청 request
     */
    @Transactional
    public void joinUser(JoinRequest joinRequest) {
        if (isExistUserId(joinRequest.userId())) {
            throw new CustomException("이미 존재하는 ID 입니다.", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(createUser(joinRequest));
    }

    /**
     * 로그인 API
     *
     * @param loginRequest 로그인 요청 request
     * @return token
     */
    @Transactional
    public LoginResponse loginUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.userId(),
                            loginRequest.password()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String name = userDetails.getUsername();
            String userId = userDetails.getUserId();

            return new LoginResponse(name, userId, jwt);
        } catch (Exception e) {
            throw new CustomException("로그인 실패: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 회원 비밀번호 변경 API
     *
     * @param userIdx               사용자 idx
     * @param updatePasswordRequest 비밀번호 변경 요청 request
     */
    @Transactional
    public void updatePassword(Long userIdx, UpdatePasswordRequest updatePasswordRequest) {
        User user = getUser(userIdx);

        if (!passwordEncoder.matches(updatePasswordRequest.password(), user.getPassword())) {
            throw new CustomException("현재 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String newPassword = updatePasswordRequest.newPassword();

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);
        userRepository.save(user);
    }

    /**
     * 사용자 생성
     */
    private User createUser(JoinRequest joinRequest) {
        String encodePassword = bCryptPasswordEncoder.encode(joinRequest.password());
        User user = joinRequest.toEntity(encodePassword);
        userRepository.save(user);
        if (!CollectionUtils.isEmpty(joinRequest.role())) {
            createUserRoles(joinRequest.role(), user);
        }
        return user;
    }

    /**
     * 사용자 권한 생성
     */
    private void createUserRoles(List<RoleType> typeList, User user) {
        List<UserRole> userRoleList = typeList.stream()
                .map(t -> UserRole.builder().user(user).roleType(t).build())
                .toList();
        userRoleRepository.saveAll(userRoleList);
    }

    /**
     * 사용자 조회
     */
    private User getUser(Long idx) {
        return userRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 사용자 ID 존재 여부 확인
     */
    private boolean isExistUserId(String userId) {
        return userQRepository.existsByUserId(userId);
    }

}
