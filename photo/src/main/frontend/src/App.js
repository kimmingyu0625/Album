import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Login from './pages/login/login.jsx';
import MainPage from './pages/main/main.jsx';
import LoginContextProvider from "./components/contexts/LoginContextProvider";

function App() {
    return (
        <Router>
            <LoginContextProvider>
                <Routes>
                    <Route path="/" element={<Login/>}/>
                    <Route path="/main" element={<MainPage/>}/>
                </Routes>
            </LoginContextProvider>
        </Router>
    );
}

export default App;