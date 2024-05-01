import { createContext, useState } from 'react';
import SignIn from './components/signIn';
import Admin from './components/admin';
import UserPage from './components/user/UserPage';
import 'react-widgets/styles.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import AdminDashboard from './components/AdminComponent/AdminDashboard/AdminDashboard';
export const StateContext = createContext(null);
function App() {
  const [currentRole, setCurrentRole] = useState();
  var role = sessionStorage.getItem("role");
  console.log("role=", role);
  if (role === null || role === undefined) {
    setCurrentRole("login");
    sessionStorage.setItem("role", "login");
  }
  else {
    if (role != "login") {
      console.log("currentRole=", currentRole);
      if (currentRole !== "admin" && currentRole !== "user") {
        setCurrentRole(role);
      }
    }
    else {
      if (currentRole !== "login") {
        setCurrentRole(role);
      }
    }
  }
  return (
    <StateContext.Provider value={{ currentRole, setCurrentRole }}>
      <div>
        {currentRole === "login" ? <SignIn /> : (currentRole === "admin" ? <Admin /> : <UserPage />)}
      </div>
    </StateContext.Provider>
  );
}

export default App;
