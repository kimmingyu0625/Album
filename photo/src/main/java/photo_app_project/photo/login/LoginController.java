package photo_app_project.photo.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import photo_app_project.photo.comm.ResponseVo;
import photo_app_project.photo.entity.UserInfo;
import photo_app_project.photo.login.service.LoginService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/createUser")
    public ResponseEntity<ResponseVo> createUser(@RequestBody UserInfo userInfo) throws Exception {

        ResponseVo result = loginService.createUser(userInfo);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseVo> login(@RequestBody UserInfo userInfo, HttpServletRequest request) throws Exception {

        loginService.login(userInfo, request);

        ResponseVo result = new ResponseVo();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
//
//        String username = authenticationRequest.getUsername();
//        String password = authenticationRequest.getPassword();
//
//        log.info("username :" + username);
//        log.info("password :" + password);
//
//        List<String> roles = new ArrayList<>();
//        roles.add("ROLE_USER");
//        roles.add("ROLE_ADMIN");
//
//        byte[] signingKey = jwtProp.getSecretKey().getBytes();
//
//        String jwt = Jwts.builder()
//                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)
//                .header()
//                .add("typ", SecurityConstants.TOKEN_TYPE)
//                .and()
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
//                .claim("uid", username)
//                .claim("rol", roles)
//                .compact();
//
//        log.info("jwt : " + jwt);
//
//        return new ResponseEntity<String>(jwt, HttpStatus.OK);
//    }
//
//    @GetMapping("/user/auth")
//    public ResponseEntity<?> userInfo(@RequestHeader(name = "Authorization") String header) {
//
//        log.info("header ==================");
//        log.info("Authorization :" + header);
//
//        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");
//
//        byte[] signingKey = jwtProp.getSecretKey().getBytes();
//
//        Jws<Claims> prasedToken = Jwts.parser()
//                .verifyWith(Keys.hmacShaKeyFor(signingKey))
//                .build()
//                .parseSignedClaims(jwt);
//
//        log.info("paprasedToken : " + prasedToken);
//
//        String username = prasedToken.getPayload().get("uid").toString();
//
//        log.info("username : " + username);
//
//        Claims claims = prasedToken.getPayload();
//        Object roles = claims.get("roles");
//
//        log.info("roles : " + roles);
//
//        return new ResponseEntity<String>(prasedToken.toString(),HttpStatus.OK);
//    }
}
