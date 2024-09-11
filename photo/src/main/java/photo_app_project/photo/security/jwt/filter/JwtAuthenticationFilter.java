package photo_app_project.photo.security.jwt.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import photo_app_project.photo.security.jwt.constants.JwtConstants;
import photo_app_project.photo.security.jwt.provider.JwtTokenProvider;
import photo_app_project.photo.security.vo.CustomUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,JwtTokenProvider jwtTokenProvider){
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl(JwtConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, password);

        authentication = authenticationManager.authenticate(authentication);

        log.info("attemptAuthentication ::: {}" , authentication.isAuthenticated());

        if(!authentication.isAuthenticated()){
            log.info("아이디 또는 비밀번호가 일치하지 않습니다.");
            response.setStatus(401);
        }
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("successfulAuthentication start");

        CustomUser user = (CustomUser) authentication.getPrincipal();

        Long userNo = user.getUserInfo().get().getId();
        String userId = user.getUserInfo().get().getUserId();

        String jwt = jwtTokenProvider.createToekn(userNo,userId);

        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + jwt);
        response.setStatus(200);
    }
}
