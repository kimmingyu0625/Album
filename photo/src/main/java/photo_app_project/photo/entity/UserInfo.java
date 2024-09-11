package photo_app_project.photo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.internal.util.MathHelper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;
    @Column(nullable = false)
    private String password;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
