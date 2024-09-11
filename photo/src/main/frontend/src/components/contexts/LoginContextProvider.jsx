import React, {createContext, useEffect, useState} from 'react';
import {loginStore, userInfoStore} from '../../pages/login/store/loginStore'
import api from '../../apis/api';
import Cookies from 'js-cookie';
import * as auth from '../../apis/auth';
import {useNavigate} from 'react-router-dom';

export const LoginContext = createContext();

LoginContext.displayName = "LoginContextName";

const LoginContextProvider = ({children}) => {
    const {isLogin, setIsLogin} = loginStore();
    const {users, setUsers} = userInfoStore();
    const navigate = useNavigate();
    const loginCheck = async () => {

        const accessToken = Cookies.get("accessToken");
        console.log(`loginCheck accessToken : ${accessToken}`);

        if (!accessToken) {
            console.log(`쿠키에 토큰이 없습니다.`);
            loginOutSetting();
            return;
        }

        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

        let response;
        let data;

        try {
            response = await auth.userInfo();
        } catch (error) {
            console.log("login error", error);
            return;
        }

        data = response.data;
        console.log(`loginCheck data : ${data}`);

        if (data === 'UNAUTHRIZED' || response.status === 401) {
            console.log("login UNAUTHRIZED");
            return;
        }

        loginSetting(data, accessToken)
    }

    const login = async (userId, password) => {
        console.log("login userId", userId);
        console.log("login password", password);

        try {
            const response = await auth.login(userId, password);

            const data = response.data;
            const status = response.status;
            const headers = response.headers;
            const authorization = headers.authorization;
            const accessToken = authorization.replace("Bearer ", "");

            console.log(`accessToken : ${accessToken}`);

            if (status === 200) {
                Cookies.set("accessToken", accessToken);
                await loginCheck()
                navigate("/main")
            }
        } catch (error) {
            alert("로그인 실패");
        }
    }

    const logout = () => {

        const check = window.confirm(`로그아웃 하시겠습니까?`);

        if (check) {
            loginOutSetting();
            navigate("/");
        }
    }

    const loginSetting = (userData, accessToken) => {
        const {no, userId} = userData;

        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

        setIsLogin(true);

        const updateUserInfo = {no, userId};
        setUsers(updateUserInfo);

        console.log("loginSetting no :: ", no);
        console.log("loginSetting userId :: ", userId);
    }

    const loginOutSetting = () => {
        api.defaults.headers.common.Authorization = undefined;

        Cookies.remove("accessToken");

        setIsLogin(false);

        setUsers(null);
    }


    useEffect( () => {
        const reloadLogin = async () => {
            await loginCheck()
        }
        reloadLogin();

    },[]);

    return (
        <div>
            <LoginContext.Provider value={{isLogin, users, login, logout}}>
                {children}
            </LoginContext.Provider>
        </div>
    )
}

export default LoginContextProvider;