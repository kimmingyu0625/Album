package photo_app_project.photo.login.service;

import photo_app_project.photo.comm.ResponseVo;
import photo_app_project.photo.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    ResponseVo createUser(UserInfo userInfo) throws Exception;

    ResponseVo login(UserInfo userInfo, HttpServletRequest request);
}
