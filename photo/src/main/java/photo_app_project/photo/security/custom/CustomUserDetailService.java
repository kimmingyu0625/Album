package photo_app_project.photo.security.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import photo_app_project.photo.entity.UserInfo;
import photo_app_project.photo.repository.UserRepository;
import photo_app_project.photo.security.vo.CustomUser;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername {}",username);

        Optional<UserInfo> userInfo = userRepository.findByUserId(username);

        if(userInfo.isEmpty()){
            log.info("userIsEmpty Error");
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }

        log.info("security userInfo :: {}",userInfo);

        //userInfo --> parse security
        CustomUser customUser = new CustomUser(userInfo);

        log.info("customUser :: {}",customUser);

        return customUser;
    }
}
