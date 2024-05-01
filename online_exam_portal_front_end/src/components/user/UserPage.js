import DashBoard from '../../page/DashBoard';
import Report from '../../page/Report';
import Logout from '../../page/LogOut';
import {
    BrowserRouter as Router,
    Routes,
    Route,
} from "react-router-dom";
import Exam from '../../page/Exam';
import Header from '../user/Header';
import { createContext, useState } from 'react';
import MainDash from '../MainDash/MainDash';
export const AppContext=createContext(null);

function UserPage() {
    sessionStorage.setItem("role", "user");
    const [answers, setAnswers] = useState({});
    const [questions, setQuestions] = useState({});
    return (
        < AppContext.Provider value={{ answers, setAnswers, questions, setQuestions }}>
            <Router>
                <div>
                    <Routes>
                        <Route path="/dashboard" element={<DashBoard />} />
                        <Route path="/report" element={<Report />} />
                        <Route path="/logout" element={<Logout />} />
                        <Route path="/exam" element={<Exam />} />
                        <Route path='/resultdash' element={<MainDash />}/>
                    </Routes>
                </div>
            </Router>
        </AppContext.Provider>
    );
}

export default UserPage;