import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import styles from './style2.module.css';

function Header({ title }) {
    var link = "/login";
    if (title == "Home") {
        link = "/"
    }
    return (
        <div className={styles.navbar}>
            <nav className="navbar navbar-light ">
                <div className="container">
                    <a className="navbar-brand" href="#">
                        <img src="exam3.png" alt="" width="140"/>
                    </a>
                    <Link to={link} className={styles.hello} style={{fontFamily:'monospace',paddingLeft:25}}>{title}</Link>
                </div>

            </nav>
        </div >
    )
}
Header.defaultProps = {
    title: "Login"
};

export default Header
