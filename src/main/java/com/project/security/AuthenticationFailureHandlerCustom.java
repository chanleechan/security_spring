package com.project.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailureHandlerCustom implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //TODO : 에러코드 매핑하는 로직 점검 필요 임시로 파싱
        String errorMsg = responseMsg(exception.getMessage());
        /*errorMsg = URLEncoder.encode(errorMsg, "UTF-8");*/

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(errorMsg);
        response.getWriter().flush();
    }

    private String responseMsg(String errorMsg) {
        String resultMsg;
        if (errorMsg.indexOf("Bad credentials") == 0) {
            resultMsg = SecurityErrorCode.BadCredentials.getMsg();
        } else if (errorMsg.indexOf("UsernameNotFound") == 0) {
            resultMsg = SecurityErrorCode.UsernameNotFound.getMsg();
        } else {
            resultMsg = "로그인에 문제가 있습니다. 관리자에게 문의하세요.";
        }
        return resultMsg;
    }
}
