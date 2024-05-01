import React from 'react'
// import './style2.module.css';
import Video from './Video';
import Header from './Header';
import Footer from './Footer';

function Welcome({ onclk, title }) {
    return (
        <div className="bg-image shadow" style={{
            backgroundImage: 'linear-gradient(rgba(0,0,0, 0.5), rgba(0,0,0, 0.5)),url("online.gif")',
            height: "100vh", backgroundAttachment: 'initial', backgroundPosition: 'center', backgroundRepeat: 'no-repeat', backgroundSize: 'cover'
        }}>
            <Header onclk={onclk} title={title} />
            <Video />
        </div>
    )
}

export default Welcome
