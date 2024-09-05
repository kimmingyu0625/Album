package photo_app_project.photo.security.jwt.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import photo_app_project.photo.security.jwt.constants.JwtConstants;
import photo_app_project.photo.security.jwt.provider.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtReqeustFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtReqeustFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JwtConstants.TOKEN_HEADER);

        log.info("JwtReqeustFilter header :: {}",header);

        if(header == null || header.length() == 0 || !header.startsWith(JwtConstants.TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt = header.replace(JwtConstants.TOKEN_PREFIX,"");

        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

        if(jwtTokenProvider.validateToken(jwt)){
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }
}
