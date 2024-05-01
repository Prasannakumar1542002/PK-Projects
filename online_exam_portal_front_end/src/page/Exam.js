import React, { useState, useEffect, useContext } from 'react';
import { AppContext } from '../components/user/UserPage';
import { useNavigate } from 'react-router-dom';
import QuestionPalette from './QuestionPalette';

function Exam() {
  const { questions, setQuestions } = useContext(AppContext);
  const examId = sessionStorage.getItem("exam");
  const noOfQuestions = sessionStorage.getItem("ques");
  const [questionSequence, setQuestionSequence] = useState([]);

  useEffect(() => {
    if (examId) {
      console.log("==================exam")
      const questionInfoUrl = "https://"+ window.location.hostname +":8443/OnlineExamPortal/control/question-info";
      const requestBody = { examId: examId, noOfQuestions: noOfQuestions };

      fetch(questionInfoUrl, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      })
        .then((response) => response.json())
        .then((result) => {
          console.log("inside....................");
          if (Array.isArray(result.question.examquestion)) {
            console.log("======",result.question.examquestion);
            setQuestions(result.question.examquestion);
           console.log(result.userAttemptAnswerMaster)
            setQuestionSequence(result.userAttemptAnswerMaster);
          } else {
            console.error('Invalid format for questions:', result.question.examquestion);
          }
        })
        .catch((error) => {
          console.error('Error fetching questions:', error);
        });
    }
  }, [examId, noOfQuestions]);

  return (
    <div>
      <QuestionPalette data={questionSequence}/>
    </div>

  );
}

export default Exam;
