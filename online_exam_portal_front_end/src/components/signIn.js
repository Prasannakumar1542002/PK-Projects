import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Welcome from '../components/Wecome';
import Login2 from '../components/Login2';

function SignIn({rolestate}) {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path='/login' element={<Login2 title="Home"/>}/>
          <Route path='/' element={<Welcome/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default SignIn;
