import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import Subject from "./SubjectComponent/Subject";
import { Link, BrowserRouter, Routes, Route } from "react-router-dom";
import Exam from "./ExamComponent/Exam";
import Student from "./StudentList/Student/Student";
import AddQuestion from "./ExamComponent/AddQuestion/AddQuestion";
import ViewQuestion from "./ExamComponent/ViewQuestion/ViewQuestion";
import Details from "./ExamComponent/DetailComponent/Details";
import StudentList from "./StudentList/StudentList";
import Result from "./ResultComponent/Result";
import Question from "./QuestionComponent/Question";
import Dashboard from "./Dashboard/Dashboard";

function NavScrollExample() {
  return (
    <>
      <BrowserRouter>
        <Navbar expand="lg" className="bg-body-tertiary">
          <Container fluid>
            <Navbar.Brand href="#">Navbar scroll</Navbar.Brand>
            <Navbar.Toggle aria-controls="navbarScroll" />
            <Navbar.Collapse id="navbarScroll">
              <Nav
                className="me-auto my-2 my-lg-0"
                style={{ maxHeight: "100px" }}
                navbarScroll
              >
                <Nav.Link>
                  <Link
                    exact
                    to="/AdminDashboard/Subject"
                    style={{ textDecoration: "none" }}
                  >
                    {" "}
                    <li className="nav-item" role="presentation">
                      {" "}
                      <span>Topic </span>
                    </li>
                  </Link>
                </Nav.Link>
                <Nav.Link>
                <Link exact to="/AdminDashboard/Exam" style={{textDecoration:"none"}}>
                      <li className="nav-item" role="presentation">
                        {" "}
                        <span
                          className="nav-link"
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
                <Nav.Link>
                <Link exact to="/AdminDashboard/Question" style={{textDecoration:"none"}}>
                      <li className="nav-item" role="presentation">
                        {" "}
                        <span
                          className="nav-link"
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
                    <Nav.Link>
                    <Link exact to="/AdminDashboard/Result" style={{textDecoration:"none"}}>
                      <li className="nav-item" role="presentation">
                        {" "}
                        <span
                          className="nav-link"
                          id="result-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#result"
                          type="button"
                          role="tab"
                          aria-controls="result"
                          aria-selected="false"
                        >
                          Result{" "}
                        </span>
                      </li>
                    </Link>
                    </Nav.Link>
                    <Nav.Link>
                    <Link exact to="/AdminDashboard/StudentList" style={{textDecoration:"none"}}>
                      <li className="nav-item" role="presentation">
                        {" "}
                        <span
                          className="nav-link"
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
                    <Nav.Link>
                    <Link exact to="/AdminDashboard" style={{textDecoration:"none"}}>
                      <li className="nav-item" role="presentation">
                        {" "}
                        <span
                          className="nav-link"
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
                    </Nav.Link>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        <div className="hide-sm pt-3">
            <Routes>
              <Route exact path="/" element={<Dashboard />}></Route>
              <Route
                exact
                path="/AdminDashboard"
                element={<Dashboard />}
              ></Route>

              <Route
                exact
                path="/AdminDashboard/Subject"
                element={<Subject />}
              ></Route>
              <Route
                exact
                path="/AdminDashboard/Exam"
                element={<Exam />}
              ></Route>
              <Route
                exact
                path="/AdminDashboard/Question"
                element={<Question />}
              ></Route>
              <Route
                exact
                path="/AdminDashboard/Result"
                element={<Result />}
              ></Route>
              <Route
                exact
                path="/AdminDashboard/StudentList"
                element={<StudentList />}
              ></Route>

              <Route
                exact
                path="/AdminDashboard/Exam/Details/:id"
                element={<Details />}
              ></Route>
              <Route
                exact
                path="/AdminDashboard/Exam/ViewQuestion/:id"
                element={<ViewQuestion />}
              ></Route>
              <Route
                exact
                path="/AdminDashboard/Exam/AddQuestion/:id/:total"
                element={<AddQuestion />}
              ></Route>

              <Route
                exact
                path="/AdminDashboard/StudentList/Details/:id/:total"
                element={<Student />}
              ></Route>
            </Routes>
            </div>
      </BrowserRouter>
    </>
  );
}

export default NavScrollExample;
