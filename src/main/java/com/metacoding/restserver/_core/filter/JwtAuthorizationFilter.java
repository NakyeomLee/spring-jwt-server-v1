package com.metacoding.restserver._core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.restserver._core.auth.LoginUser;
import com.metacoding.restserver._core.util.JwtUtil;
import com.metacoding.restserver._core.util.Resp;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("check : JwtAuthorizationFilter 작동됨");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String jwt = request.getHeader("Authorization");

        if (jwt == null) {
            onError(response, "토큰 없음");
            return; // 밑에 chain.doFilter 타지않도록 종료시킴 / doFilter 타게되면 터짐
        }

        if (!jwt.startsWith("Bearer ")) {
            onError(response, "프로토콜 잘못됨 혹은 공백일 수 없음");
            return;
        }

        try {
            LoginUser loginUser = jwtUtil.verify(jwt);

            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", loginUser);

        } catch (JWTDecodeException jwtDecodeException) {
            onError(response, "토큰 검증 실패");
            return;
        }

        chain.doFilter(request, response);
    }

    private void onError(HttpServletResponse response, String msg) throws IOException {
        try {
            String responseBody = new ObjectMapper().writeValueAsString(Resp.fail("토큰 없음"));

            response.setStatus(401); // 401 : 인가 안됨
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(responseBody);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}