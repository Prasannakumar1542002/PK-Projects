import React, { useState } from 'react'
import Field from './Field';
import Header from './Header';
import Register from './Register';

function Login2({onclk,title}) {
    const[form,setform]=useState(true);
    var pagestate=()=>{
        setform(!form);
    }
    return (
        <div>
            <Header onclk={onclk} title={title}/>
            <div className="background-wrap">
                <div className="background"></div>
            </div>
            {form?<Field change={pagestate} title="Log In to Your Account"/>:<Register change={pagestate} title="Register With Us"/>}
        </div>
    )
}

export default Login2
