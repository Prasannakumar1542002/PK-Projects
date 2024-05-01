import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Link, BrowserRouter, Routes, Route } from "react-router-dom";
import Exam from "./ExamComponent/Exam";
import StudentList from "./StudentList/StudentList";
import Question from "./QuestionComponent/Question";
import Dashboard from "./Dashboard/Dashboard";
import ExamTopicMapping from "./ExamComponent/ExamTopicMapping/ExamTopicMapping";
import Logo from "../../../components/image/exam3.png";
import Topic from "./SubjectComponent/Topic";
import LogOut from "../../../page/LogOut";
import { useEffect, useState } from "react";
import UserExamMappingForm from "./Dashboard/Form/UserExamMappingForm";

function AdminDashboard() {
  const [students, setStudents] = useState([]);
  useEffect(() => {
    async function getAllStudent() {
      try {
        const response = await fetch(
          "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/fetch-student-details",
          {
            credentials: "include",
          }
        );
        if (!response.ok) {
          throw new Error();
        }
        const data = await response.json();
        console.log("student",data);
        var list = data.StudentListInfo.StudentList;
        setStudents(list);
      } catch (error) {
        console.log(error);
      }
    }
    getAllStudent();
  }, []);


  return (
    <>
      <BrowserRouter>
        <Navbar
          collapseOnSelect
          expand="lg"
          className="bg-body-tertiary d-flex justify-content-center mw-100"
          bg="dark"
          data-bs-theme="dark"
        >
          <Container>
            <Navbar.Brand href="#" className="w-100">
              <img
                src={Logo}
                alt="Logo"
                className="img-fluid"
                style={{ width: "150px" }}
              />
            </Navbar.Brand>
            <Navbar.Toggle
              aria-controls="responsive-navbar-nav"
              className="w-100"
            />
            <Navbar.Collapse id="responsive-navbar-nav" className="w-100">
              <Nav className="me-auto d-flex justify-content-center w-100">
                <ul className="navbar-nav">
                  <Nav.Link className="me-auto d-flex justify-content-center w-100 nav-item">
                    <Link
                      exact
                      to="/AdminDashboard/Subject"
                      style={{ textDecoration: "none" }}
                      className="nav-link text-light bg-dark"
                    >
                      <li role="presentation">
                        {" "}
                        <span
                          id="exam-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#exam"
                          type="button"
                          role="tab"
                          aria-controls="exam"
                          aria-selected="false"
                        >
                          Topic{" "}
                        </span>
                      </li>
                    </Link>
                  </Nav.Link>
                  <Nav.Link className="me-auto d-flex justify-content-center w-100 nav-item">
                    <Link
                      exact
                      to="/AdminDashboard/Exam"
                      style={{ textDecoration: "none" }}
                      className="nav-link text-light bg-dark"
                    >
                      <li role="presentation">
                        {" "}
                        <span
                          id="exam-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#exam"
                          type="button"
                          role="tab"
                          aria-controls="exam"
                          aria-selected="false"
                        >
                          Exam{" "}
                        </span>
                      </li>
                    </Link>
                  </Nav.Link>
                  <Nav.Link className="me-auto d-flex justify-content-center w-100 nav-item">
                    <Link
                      exact
                      to="/AdminDashboard/Question"
                      style={{ textDecoration: "none" }}
                      className="nav-link text-light bg-dark"
                    >
                      <li className="nav-item" role="presentation">
                        {" "}
                        <span
                          id="question-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#question"
                          type="button"
                          role="tab"
                          aria-controls="question"
                          aria-selected="false"
                        >
                          {" "}
                          Question{" "}
                        </span>
                      </li>
                    </Link>
                  </Nav.Link>
                  <Nav.Link className="me-auto d-flex justify-content-center w-100 nav-item">
                    <Link
                      exact
                      to="/AdminDashboard/StudentList"
                      style={{ textDecoration: "none" }}
                      className="nav-link text-light bg-dark"
                    >
                      <li role="presentation">
                        {" "}
                        <span
                          id="student-list-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#student-list"
                          type="button"
                          role="tab"
                          aria-controls="student-list"
                          aria-selected="false"
                        >
                          StudentList{" "}
                        </span>
                      </li>
                    </Link>
                  </Nav.Link>
                  <Nav.Link className="me-auto d-flex justify-content-center w-100 nav-item">
                    <Link
                      exact
                      to="/AdminDashboard"
                      style={{ textDecoration: "none" }}
                      className="nav-link text-light bg-dark"
                    >
                      <li role="presentation">
                        {" "}
                        <span
                          id="student-list-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#dashboard"
                          type="button"
                          role="tab"
                          aria-controls="dashboard"
                          aria-selected="false"
                        >
                          {" "}
                          Dashboard
                        </span>{" "}
                      </li>
                    </Link>
                    <Link to="/logout" className='nav-link text-light bg-dark ml-3' style={{ textDecoration: "none", }}>
                      <i className='bi bi-power'></i> Logout
                    </Link>
                  </Nav.Link>
                </ul>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        <div className="hide-sm pt-3">
          <Routes>
            <Route exact path="/" element={<Dashboard />}></Route>
            <Route exact path="/AdminDashboard" element={<Dashboard />}></Route>

            <Route
              exact
              path="/AdminDashboard/Subject"
              element={<Topic />}
            ></Route>
            <Route exact path="/AdminDashboard/Exam" element={<Exam />}></Route>
            <Route
              exact
              path="/AdminDashboard/Question"
              element={<Question />}
            ></Route>
            <Route
              exact
              path="/AdminDashboard/StudentList"
              element={<StudentList />}
            ></Route>
            <Route
              exact
              path="/ExamComponent/ExamTopicMapping"
              element={<ExamTopicMapping />}
            ></Route>
            <Route path="/logout" element={<LogOut/>} />
            <Route
              exact
              path="/AdminDashboard/StudentList/AddUser"
              element={<UserExamMappingForm students={students}/>}
            ></Route>
          </Routes>
        </div>
      </BrowserRouter>
    </>
  );
}

export default AdminDashboard;
