// Logout.js
import React, { useContext, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { StateContext } from '../App';

const LogOut = () => {
  const {currentRole,setCurrentRole}=useContext(StateContext);
  const nav=useNavigate();
  useEffect(()=>{
    console.log("Entered logout");
    sessionStorage.setItem("role","login");
    setCurrentRole("login");
    nav("/");
  });
  return (
    <div>
     
    </div>
  );
};

export default LogOut;
