package photo_app_project.photo.comm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseVo {
    private String type;
    private String message;
}
