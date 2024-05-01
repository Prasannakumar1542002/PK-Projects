import React from 'react'
import './style.css';

function Footer() {   
    return (
        <div className="row">
            <p className="copyrights">
                &copy;{new Date().getFullYear()} EXAMGROUND | © 2021 All rights reserved
                by VastPRO Technologies Private Limited
            </p>
        </div>
    )
}

export default Footer
