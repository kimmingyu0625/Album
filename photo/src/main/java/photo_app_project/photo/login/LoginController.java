package photo_app_project.photo.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import photo_app_project.photo.comm.ResponseVo;
import photo_app_project.photo.entity.UserInfo;
import photo_app_project.photo.login.service.LoginService;
import photo_app_project.photo.security.vo.CustomUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/create")
    public ResponseEntity<ResponseVo> createUser(@RequestBody UserInfo userInfo) throws Exception {

        ResponseVo result = loginService.createUser(userInfo);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUser customUser){
        log.info("LoginController getUserInfo :: {}",customUser);
        Optional<UserInfo> user = customUser.getUserInfo();

        log.info("LoginController getUserInfo getUserInfo :: {}",user);

        if(user != null){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }

        return new ResponseEntity<>("UNAUTHORRIZED",HttpStatus.UNAUTHORIZED);
    }
}
