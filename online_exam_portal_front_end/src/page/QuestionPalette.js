import React, { useContext, useEffect, useState } from 'react'
import '../components/portal.css';
import $ from 'jquery';
import { Badge, Icon, Button, Row, Col, Popconfirm } from 'antd';
import Trainee from './Trainee';
import { useNavigate } from 'react-router-dom';
import { AppContext } from '../components/user/UserPage';
import Swal from 'sweetalert2';

function QuestionPalette({ data }) {
  var oneQues = <p>none</p>;
  const { answers, setAnswers } = useContext(AppContext);
  const { questions } = useContext(AppContext);
  const nav = useNavigate();
  var trigger = $('.hamburger'),
    overlay = $('.overlay'),
    isClosed = false;

  function hamburger_cross() {
    $('#wrapper').toggleClass('toggled');

    if (isClosed == true) {
      overlay.hide();
      trigger.removeClass('is-open');
      trigger.addClass('is-closed');
      isClosed = false;
    } else {
      overlay.show();
      trigger.removeClass('is-closed');
      trigger.addClass('is-open');
      isClosed = true;
    }
  }
  const FULL_DASH_ARRAY = 283;
  const WARNING_THRESHOLD = 600;
  const ALERT_THRESHOLD = 300;

  const COLOR_CODES = {
    info: {
      color: "green"
    },
    warning: {
      color: "orange",
      threshold: WARNING_THRESHOLD
    },
    alert: {
      color: "red",
      threshold: ALERT_THRESHOLD
    }
  };

  var exTime = Number(sessionStorage.getItem("examTime")) * 60;
  console.log("exam time : ", exTime);

  const TIME_LIMIT = exTime;
  let timePassed = 0;
  let timeLeft = TIME_LIMIT;
  let timerInterval = null;
  let remainingPathColor = COLOR_CODES.info.color;

  const [activeStatus, setActiveStatus] = useState();
  const [visitedStatus, setVisitedStatus] = useState([]);
  const examId = sessionStorage.getItem("exam");
  console.log("examid====report", examId);

  const selectionAnswer = () => {
    console.log("test", answers)
    try {
      const Array = [];
      questions.forEach(ele => {
        ele.forEach((element) => {
          const questionId = element.questionId;
          var ans = element.answer;
          var answer = null;
          if (element.questionType == "QT_MC") {
            var option = "";
            answers[element.questionId].map((ele) => {
              option += (element[ele] + ",");
            })
            answer = option.substring(0, option.length - 1);
            console.log("option-", option);
          }
          else if (element.questionType == "QT_FIB") {
            answer = answers[element.questionId];
          }
          else {
            answer = element[answers[element.questionId]]
          }
          Array.push({ questionId, answer });

        });

      });
      console.log("array", Array);
      return Array;
    } catch (error) {
      console.log(error);

    }
  }

  const url = "https://" + window.location.hostname + ":8443/OnlineExamPortal/control/update-result";

  const fetchInfo = () => {
    const selectionAnswerResult = selectionAnswer();
    const requestBody = { questions: questions }
    console.log("inside fetch...", selectionAnswerResult, "------", requestBody);

    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ selectionAnswerResult, questions: questions, examId: examId }),
      credentials: 'include'
    })
      .then((res) => res.json())
      .then((fetchedData) => {
        console.log("fetched...date", fetchedData);
        // setQuestions(fetchedData.questions);
        // setAnswers(fetchedData.selectionAnswerResult);

      })
      .catch((error) => {
        console.error('Error fetching data:', error);

      });
  };

  useEffect(() => {
    if (questions) {
      Swal.fire({
        title: "Are you sure?",
        text: "Once you started the exam you can't able to go back without completing it",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Start"
      }).then((result) => {
        if (result.isConfirmed) {
          document.getElementById("app").innerHTML = `
              <div class="base-timer">
                <svg class="base-timer__svg" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                  <g class="base-timer__circle">
                    <circle class="base-timer__path-elapsed" cx="50" cy="50" r="45"></circle>
                    <path
                      id="base-timer-path-remaining"
                      stroke-dasharray="283"
                      class="base-timer__path-remaining ${remainingPathColor}"
                      d="
                        M 50, 50
                        m -45, 0
                        a 45,45 0 1,0 90,0
                        a 45,45 0 1,0 -90,0
                      "
                    ></path>
                  </g>
                </svg>
                <span id="base-timer-label" class="base-timer__label">${formatTime(
            timeLeft
          )}</span>
              </div>
              `;
          // startTimer();
        }
        else {
          nav("/dashboard"); window.location.reload()
        }
      });
    }
  }, [data])




  function onTimesUp() {
    clearInterval(timerInterval);
  }

  function startTimer() {
    timerInterval = setInterval(() => {
      timePassed = timePassed += 1;
      timeLeft = TIME_LIMIT - timePassed;
      document.getElementById("base-timer-label").innerHTML = formatTime(
        timeLeft
      );
      setCircleDasharray();
      setRemainingPathColor(timeLeft);

      if (timeLeft === 0) {
        onTimesUp();
      }
    }, 1000);
  }

  function formatTime(time) {
    const minutes = Math.floor(time / 60);
    let seconds = time % 60;

    if (seconds < 10) {
      seconds = `0${seconds}`;
    }

    return `${minutes}:${seconds}`;
  }

  function setRemainingPathColor(timeLeft) {
    const { alert, warning, info } = COLOR_CODES;
    if (timeLeft <= alert.threshold) {
      document
        .getElementById("base-timer-path-remaining")
        .classList.remove(warning.color);
      document
        .getElementById("base-timer-path-remaining")
        .classList.add(alert.color);
    } else if (timeLeft <= warning.threshold) {
      document
        .getElementById("base-timer-path-remaining")
        .classList.remove(info.color);
      document
        .getElementById("base-timer-path-remaining")
        .classList.add(warning.color);
    }
  }

  function calculateTimeFraction() {
    const rawTimeFraction = timeLeft / TIME_LIMIT;
    return rawTimeFraction - (1 / TIME_LIMIT) * (1 - rawTimeFraction);
  }

  function setCircleDasharray() {
    const circleDasharray = `${(
      calculateTimeFraction() * FULL_DASH_ARRAY
    ).toFixed(0)} 283`;
    document
      .getElementById("base-timer-path-remaining")
      .setAttribute("stroke-dasharray", circleDasharray);
  }
  const handleOptionChange = (questionId, selectedOption, questionType) => {
    setAnswers((prevAnswers) => {
      console.log("prevAnswer : ", prevAnswers);
      if (questionType == "QT_MC") {
        const prevAnswer = prevAnswers[questionId] || [];
        const updatedAnswer = prevAnswer.includes(selectedOption)
          ? prevAnswer.filter((option) => option !== selectedOption)
          : [...prevAnswer, selectedOption];
        console.log("=====answers", answers)
        return {
          ...prevAnswers,
          [questionId]: updatedAnswer,
        };
      }
      else {
        return {
          ...prevAnswers,
          [questionId]: selectedOption
        }
      }
    });
    console.log("kowsi......", answers);
  };
  const renderQuestion = (question) => (

    <div key={question.questionId} className={`card mt-3 ${answers[question.questionId] ? 'border-success' : 'border-danger'}`}>
      <div className='card-body'>
        <h5 className='card-title'>{(sequence) + ". " + question.questionDetail}</h5>
        <div className='form-check'>
          {['A', 'B', 'C', 'D', 'E'].map((option) => {
            const optionKey = `option${option}`;
            const optionValue = question[optionKey];
            const questionType = question.questionType;

            switch (questionType) {
              case 'QT_SC':
                console.log("QT_SC")
                return optionValue && (
                  <div key={optionKey} className='form-check'>
                    <input
                      type='radio'
                      className='form-check-input'
                      id={`${optionKey}-${question.questionId}`}
                      name={`question${question.questionId}`}
                      value={optionKey}
                      onChange={() => handleOptionChange(question.questionId, optionKey, questionType)}
                      checked={answers[question.questionId] == optionKey}
                    />
                    <label className='form-check-label' htmlFor={`${optionKey}-${question.questionId}`}>
                      {optionValue}
                    </label>
                  </div>
                );
              case 'QT_MC':
                return optionValue && (
                  <div key={optionKey} className='form-check'>
                    <input
                      type='checkbox'
                      className='form-check-input'
                      id={`${optionKey}-${question.questionId}`}
                      name={`question${question.questionId}`}
                      value={optionKey}
                      onChange={() => handleOptionChange(question.questionId, optionKey, questionType)}
                      checked={answers[question.questionId] && answers[question.questionId].includes(optionKey)}
                    />
                    <label className='form-check-label' htmlFor={`${optionKey}-${question.questionId}`}>
                      {optionValue}
                    </label>
                  </div>
                );
              case 'QT_TF':
                if (option === 'A' || option === 'B') {
                  return optionValue && (
                    <div key={optionKey} className='form-check'>
                      <input
                        type='radio'
                        className='form-check-input'
                        id={`${optionKey}-${question.questionId}`}
                        name={`question${question.questionId}`}
                        value={optionKey}
                        onChange={() => handleOptionChange(question.questionId, optionKey, questionType)}
                        checked={answers[question.questionId] == optionKey}
                      />
                      <label className='form-check-label' htmlFor={`${optionKey}-${question.questionId}`}>
                        {optionValue}
                      </label>
                    </div>
                  );
                }
              case 'QT_FIB':
                console.log("Entering QT_FIB case...");
                if (option === 'A') {
                  return optionValue && (
                    <div key={optionKey} >
                      <input
                        type="text"
                        id={`${question.questionId}in`}
                        onChange={(e) => handleOptionChange(question.questionId, e.target.value, questionType)}
                      />
                    </div>
                  );
                }
              default:
                return null;
            }
          })}
        </div>
      </div>
    </div>
  );
  const [sequence, setSequence] = useState(1);
  // const renderQuestionGroup = (questionGroup) => (
  //   <React.Fragment key={questionGroup[0].questionId}>
  //     {Array.isArray(questionGroup) ? (
  //       questionGroup.map((question) => renderQuestion(question))
  //     ) : (
  //       <p>Error: Invalid format for questionGroup</p>
  //     )}
  //   </React.Fragment>
  // );
  return (
    <div>
      {questions ? (
        <div className='container'>
          <div className='row'>
            <div className='col-3'>
              <div id="wrapper">
                <div className="overlay"></div>

                {/* <!-- Sidebar   id="sidebar-wrapper" --> */}
                <nav className="navbar navbar-inverse fixed-top col-3" id="sidebar-wrapper" role="navigation">
                  <Trainee />
                  <div id="app"></div>
                  <div className="question-list-wrapper">
                    <div className="question-list-inner" style={{ background: "#F5F0EF" }}>
                      <Row style={{ padding: '5px' }}>
                        {data.map((ques) => {
                          return (
                            <Col span={6} style={{ padding: '10px' }}>
                              <button key={ques.sequenceNum} id={ques.questionId + "btn"} className='btn  pl-3 pr-3 pt-2 pb-2 ml-2  btn-light border border-dark' onClick={() => {
                                setActiveStatus(ques.sequenceNum);
                                if (visitedStatus == null || visitedStatus == undefined) {
                                  setVisitedStatus([ques.sequenceNum]);
                                  setVisitedStatus([...visitedStatus, sequence]);
                                }
                                else {
                                  setVisitedStatus([...visitedStatus, ques.sequenceNum]);
                                  setVisitedStatus([...visitedStatus, sequence]);
                                }
                                setSequence(ques.sequenceNum);//
                                // document.getElementById(sequence+"btn").style.backgroundColor="#525CEB";
                              }} style={sequence == ques.sequenceNum ? { background: "#F86F03", color: "white" } : (visitedStatus.includes(ques.sequenceNum) ? { background: "#525CEB", color: "white" } : { background: "#B6B0AF", color: "white" })}>{ques.sequenceNum}</button>
                            </Col>
                          )
                        })}
                      </Row>

                    </div>
                  </div>
                  <div className="End-test-container">
                    <Popconfirm
                      title="Are you sure to end the test"
                      onConfirm={() => {
                        var error = 0;
                        questions.forEach(ele => {
                          ele.forEach((element) => {
                            const questionId = element.questionId;
                            if (answers[questionId] == null || answers[questionId] == undefined) {
                              document.getElementById(questionId + "btn").style.backgroundColor = "red";
                              error = 1;
                            }
                            else {
                              document.getElementById(questionId + "btn").style.backgroundColor = "green";
                            }
                          });

                        });
                        if (error == 0) {
                          fetchInfo();
                          nav("/dashboard");
                          // window.location.reload();
                        }
                        else {
                          Swal.fire({
                            icon: "error",
                            title: "Unanswered questions",
                            text: "You need to answer all the questions",
                            footer: 'Incomplete Exam'
                          });
                        }
                      }}
                      okText="Yes"
                      cancelText="No"
                    >
                      <Button type="default">End Test</Button>
                    </Popconfirm>
                  </div>
                </nav>
              </div>
            </div>
            {/* <!-- /#sidebar-wrapper --> */}

            {/* <!-- Page Content  --> */}
            <div className='col-9'>
              <button type="button" className="hamburger animated fadeInLeft is-closed" data-toggle="offcanvas" onClick={hamburger_cross}>
                <span class="hamb-top"></span>
                <span class="hamb-middle"></span>
                <span class="hamb-bottom"></span>
              </button>
              <div class="container" id="page-content">
                <div className='container mt-4'>
                  {Array.isArray(questions) ? (
                    // questions.map((questionGroup) => renderQuestionGroup(questionGroup))
                    questions.forEach(element => {
                      data.forEach(seq => {
                        if (seq.sequenceNum == sequence) {
                          element.forEach(ques => {
                            if (seq.questionId == ques.questionId) {
                              oneQues = renderQuestion(ques);
                            }
                          });
                        }
                      });
                    })
                  ) : (
                    <p>Error: No questions assigned for this exam</p>
                  )}
                  {oneQues}

                  {sequence !== null ? (
                    <div class="mt-4">
                      {sequence > 1 ? (<button type="button" class="btn btn-secondary" onClick={
                        () => {

                          let count = sequence - 1;
                          //setActiveStatus(sequence);
                          setSequence(count);
                          //console.log(activeStatus);
                          setVisitedStatus([...visitedStatus, sequence]);
                          // document.getElementById(sequence+"btn").style.backgroundColor="#525CEB";

                        }
                      }>Previous</button>) : (<button type="button" class="btn btn-success" onClick={
                        () => {
                          if (sequence < data.length) {
                            let count = sequence + 1;
                            //setActiveStatus(sequence-1);
                            setSequence(count);
                            setVisitedStatus([...visitedStatus, sequence]);
                            // document.getElementById(sequence+"btn").style.backgroundColor="#525CEB";
                          }
                        }
                      }>Next</button>)}
                      {sequence == data.length || sequence == 1 ? "" : (<button type="button" class="btn btn-success ml-5" onClick={
                        () => {
                          if (sequence < data.length) {
                            let count = sequence + 1;
                            //setActiveStatus(sequence-1);
                            setSequence(count);
                            setVisitedStatus([...visitedStatus, sequence]);
                            // document.getElementById(sequence+"btn").style.backgroundColor="#525CEB";
                          }
                        }
                      }>Next</button>)}
                    </div>
                  ) : (
                    ""
                  )}
                </div>
              </div>
            </div>
            {/* <!-- /#page-content-wrapper --> */}

          </div>
          {/* <!-- /#wrapper --> */}

          {/* {data.map((ques)=>(<button className='btn pl-3 pr-3 pt-2 pb-2 ml-3 mb-3 btn-light border border-dark'>{ques.Seq}</button>))} */}
        </div>) : <div className='container-fluid'><p>Error: No questions assigned for this exam. Click here to go back to dashboard</p><button className='btn btn-primary' onClick={() => {
          nav("/dashboard"); window.location.reload()
        }}>Back</button></div>}
    </div>
  )
}

export default QuestionPalette
