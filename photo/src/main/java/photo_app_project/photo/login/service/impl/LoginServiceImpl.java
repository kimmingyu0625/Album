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
import org.springframework.transaction.annotation.Transactional;
import photo_app_project.photo.comm.ResponseVo;
import photo_app_project.photo.entity.UserInfo;
import photo_app_project.photo.login.service.LoginService;
import photo_app_project.photo.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public ResponseVo createUser(UserInfo userInfo) {
        ResponseVo res = new ResponseVo();
        try {
            String encPassword = passwordEncoder.encode(userInfo.getPassword());
            userInfo.setPassword(encPassword);

            UserInfo savedUser = repository.save(userInfo);
            entityManager.flush();

            if (savedUser != null && savedUser.getId() != null) {
                res.setType("success");
                res.setMessage("회원가입에 성공했습니다.");
            } else {
                throw new RuntimeException("사용자 저장 실패");
            }
        } catch (Exception e) {
            res.setType("error");
            res.setMessage("회원가입에 실패했습니다: " + e.getMessage());
            // 로깅 추가
            log.error("회원가입 중 오류 발생", e);
            // 트랜잭션 롤백을 위해 예외를 다시 던집니다.
            throw new RuntimeException("회원가입 처리 중 오류 발생", e);
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
