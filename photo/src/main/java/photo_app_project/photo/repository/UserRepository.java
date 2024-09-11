package photo_app_project.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import photo_app_project.photo.entity.UserInfo;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserInfo, String> {
    Optional<UserInfo> findByUserId(String userId);

}
