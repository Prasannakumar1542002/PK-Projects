import React, { useState, useEffect, useContext } from 'react';
import { NavLink } from 'react-router-dom';
import { AppContext } from '../components/user/UserPage';
import Header from '../components/user/Header';

const Dashboard = () => {
  const examInfoUrl = "https://"+ window.location.hostname +":8443/OnlineExamPortal/control/exam-info";
  const [examData, setExamData] = useState([]);
  const [fetchError, setFetchError] = useState(null);
  const { questions, setQuestions } = useContext(AppContext);
  setQuestions(null);



  const fetchExamInfo = () => {
    fetch(examInfoUrl, {
      method: "GET",
      credentials: "include",


    })
      .then((response) => response.json())
      .then((result) => {
        if (result.exams === undefined) {
          console.error('Error  in fetching exam data:', fetchError);
          setFetchError('Error fetching exam data. Please try again.');
        } else {
          console.log(result.exams.examList);
          setExamData(result.exams.examList);
        }
      })
      .catch((error) => {
        console.error('Error  in fetching exam data:', error);
        setFetchError('Error  in fetching exam data. Please try again.');
      });
  };

  useEffect(() => { 
    fetchExamInfo();
  }, []);

  const renderExamCard = (exam) => {
    console.log("Entered render function for examId:", exam.examId);
    return (
      <ExamCard
        key={exam.examId}
        title={exam.examName}
        content={`ExamId: ${exam.examId}<br>ExamName: ${exam.examName}<br>Time: ${Number(exam.durationMinutes)}min<br>Description: ${exam.description}`}
        examId={exam.examId}
        noOfQuestions={exam.noOfQuestions}
        durationMinutes={exam.durationMinutes}
      />
    );
  };

  return (
    <div>
      <Header />
      <div className='container'>
      {fetchError && <p>{fetchError}</p>}
      {!fetchError && (
        <div className="row">
          {examData.length==0?<p>No exams has been assigned for you. Check back later.</p> :(examData.map((exam) => (
            <div key={exam.examId} className="col-md-4 mb-4">
              {renderExamCard(exam)}
            </div>
          )))}
        </div>
      )}
      </div>
    </div>
  );
};

const SessionStorage = (props) => {
  sessionStorage.setItem("exam", props.examId);
  sessionStorage.setItem("ques", props.noOfQuestions);
  console.log("examTimeBefore : ",props.durationMinutes);
  sessionStorage.setItem("examTime",props.durationMinutes);
};

const ExamCard = (props) => {
  return (
    <div className="card border border-dark">
      <div className="card-body" dangerouslySetInnerHTML={{ __html: props.content }} />

      <button className='btn btn-primary mt-2' onClick={() => { SessionStorage(props) }}>
        <NavLink to="/exam" className='text-light' style={{ textDecoration: "none" }}>
          Exam
        </NavLink>
      </button>
    </div>
  );
};

export default Dashboard;
