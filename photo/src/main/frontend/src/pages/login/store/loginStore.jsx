import {create} from 'zustand';
import {userInfo} from "../../../apis/auth";

// 상태 저장소 생성
export const loginStore = create((set) => ({
    isLogin: false,
    setIsLogin: (value) => set({ isLogin: value }),
}));

export const userInfoStore = create((set) => ({
    users: {},
    setUsers: (value) => set({ users: value }),
}));