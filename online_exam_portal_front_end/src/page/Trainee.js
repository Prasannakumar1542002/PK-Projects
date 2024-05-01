import React from 'react';
import { connect } from 'react-redux';
import '../components/portal.css';
import user_icon from '../components/image/user.png'

function Trainee(props) {
    return (
        <div className="loggedin-trainee-container">
            <div className="loggedin-trainee-inner">
                <img alt="User Icon" src={user_icon} className="loggedin-trainee-logo"/>
                <div className="loggedin-trainee-details-container">
                    <p>{sessionStorage.getItem("userId")}</p>
                </div>
            </div>
        </div>
    )
}




export default Trainee