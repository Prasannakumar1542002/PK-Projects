import React from "react";
import Cards from "../Cards/Cards";
import Header from "../user/Header";

//import "./MainDash.css";
const MainDash = () => {
  return (
    <div>
      <Header />
      <div className="container-fluid">
        <Cards />
      </div>
    </div>
  );
};

export default MainDash;
