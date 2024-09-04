package photo_app_project.photo.login.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import photo_app_project.photo.comm.ResponseVo;
import photo_app_project.photo.constants.SecurityConstants;
import photo_app_project.photo.entity.UserInfo;
import photo_app_project.photo.login.service.LoginService;
import photo_app_project.photo.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseVo createUser(UserInfo userInfo) throws Exception {
        ResponseVo res = new ResponseVo();
        try {
            String encPassword = passwordEncoder.encode(userInfo.getPassword());
            userInfo.setPassword(encPassword);
            UserInfo result = repository.save(userInfo);

            res.setType("success");
            res.setMessage("회원가입에 성공했습니다.");
        } catch (Exception e) {
            res.setType("error");
            res.setMessage("회원가입에 실패했습니다: " + e.getMessage());
        }
        return res;
    }

    @Override
    public ResponseVo login(UserInfo userInfo, HttpServletRequest request) {

        ResponseVo res = new ResponseVo();
        try {
            log.info("login start :: userInfo {}", userInfo);
            //인증토큰생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userInfo.getUserId(), userInfo.getPassword());

            //토큰에 정보 등록
            token.setDetails(new WebAuthenticationDetails(request));

            //로그인
            Authentication authenticate = authenticationManager.authenticate(token);

            log.info("authenticate result:::: {}", authenticate.isAuthenticated());

            User authUser = (User) authenticate.getPrincipal();

            log.info("authUser :: {}", authUser.getUsername());

            //시큐리티 컨텍스트에 인증된 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            res.setType("success");
            res.setMessage("로그인 성공");

        } catch (Exception e) {
            res.setType("error");
            res.setMessage("로그인 실패");
        }
        return res;
    }
}
