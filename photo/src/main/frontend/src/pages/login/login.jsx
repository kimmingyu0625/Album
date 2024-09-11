import React, {useState, useCallback, useEffect, useContext} from 'react';
import photoSleep from '../../images/photo_1.jpg';
import loveIcon from '../../images/heart.png';
import '../../styles/login/Login.css';
import {LoginContext} from "../../components/contexts/LoginContextProvider";

function Login() {
    const [hearts, setHearts] = useState([]);
    const {login} = useContext(LoginContext);

    let heartCounter = 0;

    const addHeart = useCallback(() => {
        const heart = {
            id: heartCounter++,
            left: Math.random() * 100 + '%',
            animationDuration: Math.random() * 2 + 1 + 's'
        };
        setHearts(prevHearts => [...prevHearts, heart]);
        setTimeout(() => {
            setHearts(prevHearts => prevHearts.filter(h => h.id !== heart.id));
        }, 3000);
    }, []);

    useEffect(() => {
        // 초기에 몇 개의 하트를 생성
        for (let i = 0; i < 5; i++) {
            setTimeout(() => addHeart(), i * 300);
        }

        // 주기적으로 새 하트를 생성
        const interval = setInterval(addHeart, 100);

        // 컴포넌트가 언마운트될 때 인터벌 정리
        return () => clearInterval(interval);
    }, [addHeart]);

    const handleSubmit = (e) => {
        e.preventDefault();

        const form = e.target;
        const userId = form.userId.value;
        const password = form.password.value;
        console.log('Login attempt with:', userId, password);

        login(userId,password);
        // 여기에 로그인 로직을 구현합니다.

    };

    return (
        <form className="loginForm" onSubmit={(e) => handleSubmit(e)}>
            <div className="bg-blue-200">
                <div className="flex justify-center container mx-auto my-auto w-screen h-screen items-center flex-col">
                    <div className="relative w-64 h-64 mb-5" onMouseEnter={addHeart}>
                        <div className="w-full h-full rounded-full bg-white overflow-hidden mb-3">
                            <img
                                src={photoSleep}
                                alt="Email icon"
                                className="w-full h-full object-cover"
                            />
                        </div>
                        {hearts.map(heart => (
                            <div
                                key={heart.id}
                                className="absolute w-6 h-6 text-red-500 animate-float"
                                style={{
                                    left: heart.left,
                                    bottom: '0',
                                    animationDuration: heart.animationDuration
                                }}
                            >
                                <img src={loveIcon} alt="Icon" className="w-full h-full"/>
                            </div>
                        ))}
                    </div>
                    <div className="w-full md:w-3/4  lg:w-1/2 flex flex-col items-center bg-slate-50 rounded-md pt-12">
                        <div className="w-3/4 mb-6">
                            <input type="text" name="userId" id="userId"
                                   required={true}
                                   className="w-full py-4 px-8 bg-slate-200 placeholder:font-semibold rounded hover:ring-1 hover:ring-gray-600 outline-slate-500 border-solid border-2 border-slate-300"
                                   placeholder="아이디를 입력하세요."/>
                        </div>
                        <div className="w-3/4 mb-6">
                            <input type="password" name="password" id="password"
                                   required={true}
                                   className="w-full py-4 px-8 bg-slate-200 placeholder:font-semibold rounded hover:ring-1 hover:ring-gray-600 outline-slate-500  border-solid border-2 border-slate-300"
                                   placeholder="비밀번호를 입력하세요"/>
                        </div>
                        <div className="w-3/4 mb-12">
                            <button type="submit"
                                    className="py-4 bg-blue-500 w-full rounded text-blue-50 font-bold hover:bg-blue-700"> LOGIN
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Login;