import React, { useState } from 'react';
import { Link } from 'react-router-dom';

function Header() {
  const [navCollapse, setNavCollapse] = useState(false);
  var user=sessionStorage.getItem("userId");
  return (
    <nav className='navbar navbar-expand-lg navbar-dark bg-dark mb-5 w-100' >
      <div className='container'>
        <Link to="/" className='navbar-brand'>
          <img src="exam3.png" alt="Logo" style={{ width: 160 }} />
        </Link>

        <button
          className='navbar-toggler'
          type='button'
          onClick={() => setNavCollapse(!navCollapse)}
        >
          <span className='navbar-toggler-icon'></span>
        </button>

        <div className={`collapse navbar-collapse ${navCollapse ? '' : 'show'}`}>
          <div className='navbar-nav'>
            <Link to="/dashboard" className='nav-link'>
              <i className='bi bi-speedometer2'></i> Dashboard
            </Link>

            <Link to="/resultdash" className='nav-link'>
              <i className='bi bi-chat-square-text'></i> Report
            </Link>

            <Link to="/logout" className='nav-link'>
              <i className='bi bi-power'></i> Logout
            </Link>
          </div>
        </div>

        <h4 className='ml-auto' style={{ color: 'white', marginRight: 12, width:200 }}>
          {user}
        </h4>
      </div>
    </nav>
  );
}

export default Header;
