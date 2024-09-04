package photo_app_project.photo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import photo_app_project.photo.entity.UserInfo;

import java.util.Collection;
import java.util.Optional;

@Getter
@Setter
@ToString
public class CustomUser implements UserDetails {

    private Optional<UserInfo> userInfo;

    public CustomUser(Optional<UserInfo> userInfo){
        this.userInfo = userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userInfo.get().getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.get().getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
