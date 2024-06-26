package com.kosaf.core.common;

import com.kosaf.core.api.author.application.LoginUserComponent;
import com.kosaf.core.common.exception.InvalidClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthInterceptor implements HandlerInterceptor {

    private final LoginUserComponent loginUser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // MethodAnnotation 으로 컨트롤러에 Auth.class 로 지정한 값을 가지고온다
            Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

            if (auth == null) {
                return true; //Auth 어노테이션이 없는 컨트롤러는 interceptor타지 않아도 됨.
            }

            Long result = loginUser.authentication(request.getParameter("userId"));

            // 인증 로직에서 실패
            if (result == 0) {
                throw new InvalidClientException("사용 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
            }
            // return 이 true 일경우 해당 Controller로 넘어감
            return true;

        }
        else {
            return true;
        }
    }
}
