import React, { useContext, useEffect, useState } from 'react';
import { AppContext } from '../components/user/UserPage';
import Header from '../components/user/Header';
import "../page/answer.css";
import '../page/answermobileview.css';
import '../page/individualquestion_mobileview.css';
import { Descriptions } from 'antd';
import Title from 'antd/es/skeleton/Title';
import { Button, Col, Modal, Row, Table } from 'react-bootstrap';
import { Tag } from 'antd';
import { unmountComponentAtNode } from 'react-dom';
import Icon from '@ant-design/icons/lib/components/Icon';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import tick from '../components/image/check.png'
import wrong from '../components/image/cancel.png'


const Report = (props) => {
  var user = sessionStorage.getItem("userId");
  const { answers, setAnswers } = useContext(AppContext);
  const { questions, setQuestions } = useContext(AppContext);
  const [score, setScore] = useState(0);
  const url = "https://" + window.location.hostname + ":8443/OnlineExamPortal/control/fetch-user-report";
  const fetchResult = () => {
    fetch(url, {
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include'
    })
      .then((res) => res.json())
      .then((fetchedData) => {
        console.log("fetched...date", fetchedData);
        setQuestions(fetchedData.questions);
        setAnswers(fetchedData.userAttemptAnswerMasterList);
        setScore(fetchedData.userAttemptMasterMap);

      })
      .catch((error) => {
        console.error('Error fetching data:', error);
      });
  }
  useEffect(() => {
    console.log("fetch called...");
    setQuestions(props.questions);
    setAnswers(props.userAttemptAnswerMasterList);
    setScore(props.userAttemptMasterMap);
    // fetchResult();
  }, []);
  const td = {
    name: "prasanna",
    emailid: "pras1542002@gmail.com",
    contact: 9342465971,
    TotalScore: 100
  }
  var seq = 1;
  return (
    <div>
      <div className="answer-table-outer" style={{ width: "100%" }}>
      <button className='btn-primary pl-3 pr-3' style={{marginLeft:"1250px"}}onClick={props.hideDetails}>Back</button>
        <Title className="answer-table-heading" level={4}>Result</Title>
        <div className="answer-table-wrapper">
          <Descriptions bordered title={null} border size="small">
            <Descriptions.Item label="Email Id">{user}</Descriptions.Item>
            <Descriptions.Item label="Score">{Number(score.score)}</Descriptions.Item>
            <Descriptions.Item label="Result">{score ? score.userPassed == "Y" ? "Pass" : "Fail" : td.TotalScore}</Descriptions.Item>
          </Descriptions>
          <br />


          <table class="table">
            <thead class="thead-light">
              <tr>
                <th scope="col">S.No</th>
                <th scope="col">Question</th>
                <th scope="col">CorrectAnswer</th>
                <th scope="col">GivenAnswer</th>
                <th scope="col">Mark</th>
                <th scope="col">Status</th>

              </tr>
            </thead>
            <tbody>
              {questions ? (questions.map((oneQuestion, index) => {
                console.log("index-", index);
                console.log("answer=", score);
                return (
                  <tr>
                    <th scope="row">{seq++}</th>
                    <td>{oneQuestion.questionDetail}</td>
                    <td><button type="button" class="btn btn-outline-success">{oneQuestion.answer}</button></td>
                    <td><button type="button" class="btn btn-outline-primary">{answers[index].submittedAnswer}</button></td>
                    <td>{oneQuestion.answerValue}</td>
                    <td>{oneQuestion.answer.trim() == answers[index].submittedAnswer.trim() ? <img src={tick} style={{ width: "20px" }} /> : <img src={wrong} style={{ width: "22px" }} />}</td>
                  </tr>
                )
              })
              ) : <p>No status to show</p>}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Report;
