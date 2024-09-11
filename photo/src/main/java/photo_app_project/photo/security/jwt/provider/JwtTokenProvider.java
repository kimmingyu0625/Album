package photo_app_project.photo.security.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import photo_app_project.photo.entity.UserInfo;
import photo_app_project.photo.prop.JwtProp;
import photo_app_project.photo.repository.UserRepository;
import photo_app_project.photo.security.jwt.constants.JwtConstants;
import photo_app_project.photo.vo.CustomUser;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProp jwtProp;
    private final UserRepository userRepository;

    public String createToekn(Long userNo, String userId) {

        String jwt = Jwts.builder()
                .signWith(getShaKey(), Jwts.SIG.HS512)
                .header()
                .add("typ", JwtConstants.TOKEN_TYPE)
                .and()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
                .claim("uid", userId)
                .claim("uno", userNo)
                .compact();

        return jwt;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {

        if (authHeader == null || authHeader.length() == 0) {
            return null;
        }

        try {

            String jwt = authHeader.replace(JwtConstants.TOKEN_PREFIX, "");

            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            String userNo = parsedToken.getPayload().get("uno").toString();
            Long no = (userNo == null ? 0 : Long.parseLong(userNo));

            String userId = parsedToken.getPayload().get("uid").toString();

            if (userId == null || userId.length() == 0) {
                return null;
            }

            UserInfo user = new UserInfo();

            user.setId(no);
            user.setUserId(userId);

            Optional<UserInfo> userInfo = userRepository.findByUserId(userId);
            user.setCreatedDt(userInfo.get().getCreatedDt());

            UserDetails userDetails = new CustomUser(Optional.of(user));
            return new UsernamePasswordAuthenticationToken(userDetails, null, null);
        } catch (Error e) {
            log.info("getAuthentication error :: {}", e.getMessage());
        }
        return null;
    }

    public boolean validateToken(String jwt) {
        try {
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("#### 토큰 만료기간 ##### :: {}", parsedToken.getPayload().getExpiration());

            Date exp = parsedToken.getPayload().getExpiration();

            return !exp.before(new Date());
        } catch (Exception e){
            log.error("validateToekn Error :: {}",e.getMessage());
            return false;
        }
    }

    private byte[] getSigningKey() {
        return jwtProp.getSecretKey().getBytes();
    }

    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSigningKey());
    }
}
