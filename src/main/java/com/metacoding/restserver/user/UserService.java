package com.metacoding.restserver.user;

import com.metacoding.restserver._core.auth.LoginUser;
import com.metacoding.restserver._core.error.exception.Exception401;
import com.metacoding.restserver._core.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

}