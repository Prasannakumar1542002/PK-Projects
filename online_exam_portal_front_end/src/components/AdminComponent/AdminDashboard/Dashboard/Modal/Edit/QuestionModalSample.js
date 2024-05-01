import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import QuestionForm from "../../AdminComponent/AdminDashboard/Dashboard/Form/QuestionForm";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit } from "@fortawesome/free-solid-svg-icons";

function QuestionModalSample(props) {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const submitHandler = (e) => {
    e.preventDefault();
    const data_map = {
      questionId: props.fetchId.toString(),
      questionDetail: props.changedquestionDetail
        ? props.changedquestionDetail
        : props.questionDetail,
      optionA: props.changedoptionA ? props.changedoptionA : props.optionA,
      optionB: props.changedoptionB ? props.changedoptionB : props.optionB,
      optionC: props.changedoptionC ? props.changedoptionC : props.optionC,
      optionD: props.changedoptionD ? props.changedoptionD : props.optionD,
      optionE: props.changedoptionE ? props.changedoptionE : props.optionE,
      answer: props.changedanswer ? props.changedanswer : props.answer,
      numAnswers: props.changednumAnswers
        ? props.changednumAnswers
        : props.numAnswers.toString(),
      questionType: props.quesType ? props.quesType : props.questionType,
      difficultyLevel: props.changeddifficultyLevel
        ? props.changeddifficultyLevel
        : props.difficultyLevel.toString(),
      answerValue: props.changedanswerValue
        ? props.changedanswerValue
        : props.answerValue.toString(),
      topicId: props.topicChange ? props.topicChange : props.topicId,
      negativeMarkValue: props.changednegativeMarkValue
        ? props.changednegativeMarkValue
        : props.negativeMarkValue.toString(),
    };
    console.log(data_map);
    if (data_map.questionDetail === "") {
      document.getElementById("questiondetailerr").style.display = "block";
    } else {
      document.getElementById("questiondetailerr").style.display = "none";
    }
    if (data_map.optionA === "") {
      document.getElementById("optionaerr").style.display = "block";
    } else {
      document.getElementById("optionaerr").style.display = "none";
    }
    if (data_map.optionB === "") {
      document.getElementById("optionberr").style.display = "block";
    } else {
      document.getElementById("optionberr").style.display = "none";
    }
    if (data_map.optionC === "") {
      document.getElementById("optioncerr").style.display = "block";
    } else {
      document.getElementById("optioncerr").style.display = "none";
    }
    if (data_map.optionD === "") {
      document.getElementById("optionderr").style.display = "block";
    } else {
      document.getElementById("optionderr").style.display = "none";
    }
    if (data_map.optionE === "") {
      document.getElementById("optioneerr").style.display = "block";
    } else {
      document.getElementById("optioneerr").style.display = "none";
    }
    if (data_map.answer === "") {
      document.getElementById("answererr").style.display = "block";
    } else {
      document.getElementById("answererr").style.display = "none";
    }
    if (data_map.numAnswers === "") {
      document.getElementById("numanserr").style.display = "block";
    } else {
      document.getElementById("numanserr").style.display = "none";
    }
    if (data_map.questionType === "") {
      document.getElementById("questiontypeerr").style.display = "block";
    } else {
      document.getElementById("questiontypeerr").style.display = "none";
    }
    if (data_map.difficultyLevel === "") {
      document.getElementById("difficultylevelerr").style.display = "block";
    } else {
      document.getElementById("difficultylevelerr").style.display = "none";
    }
    if (data_map.answerValue === "") {
      document.getElementById("answervalueerr").style.display = "block";
    } else {
      document.getElementById("answervalueerr").style.display = "none";
    }
    if (data_map.topicId === "") {
      document.getElementById("topiciderr").style.display = "block";
    } else {
      document.getElementById("topiciderr").style.display = "none";
    }
    if (data_map.negativeMarkValue === "") {
      document.getElementById("negativemarkvalueeerr").style.display = "block";
    } else {
      document.getElementById("negativemarkvalueeerr").style.display = "none";
    }
    if (
      !(
        data_map.questionDetail === "" ||
        data_map.answer === "" ||
        data_map.numAnswers === "" ||
        data_map.questionType === "" ||
        data_map.difficultyLevel === "" ||
        data_map.answerValue === "" ||
        data_map.topicId === "" ||
        data_map.negativeMarkValue === ""
      )
    ) {
      if (data_map.optionA === "") {
        data_map.optionA = "null";
      }
      if (data_map.optionB === "") {
        data_map.optionB = "null";
      }
      if (data_map.optionC === "") {
        data_map.optionC = "null";
      }
      if (data_map.optionD === "") {
        data_map.optionD = "null";
      }
      if (data_map.optionE === "") {
        data_map.optionE = "null";
      }
      // FETCH
      fetch("https://"+window.location.hostname + ":8443/OnlineExamPortal/control/update-question-master", {
        method: "PUT",
        credentials: "include",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify(data_map),
      })
        .then((response) => {
          return response.json();
        })
        .then((fetch_data) => {
          console.log(fetch_data);
          props.fetchQuestions();
        });
    }
    setShow(!show);
  };

  return (
    <>
      <Button variant="link" onClick={handleShow}>
        <FontAwesomeIcon icon={faEdit} />
      </Button>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        fullscreen={true}
      >
        <Modal.Header closeButton>
          <Modal.Title>Edit Question Form</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <QuestionForm
            buttonName={props.buttonName}
            handleCloseQuestion={handleClose}
            submitHandler={submitHandler}
            handleSelectQuesTypeChange={props.handleSelectQuesTypeChange}
            quesType={props.quesType}
            setEnum={props.setEnum}
            handleSelectTopicChange={props.handleSelectTopicChange}
            topicChange={props.topicChange}
            topics={props.topics}
            questionDetail={props.questionDetail}
            topicId={props.topicId}
            questionType={props.questionType}
            optionA={props.optionA}
            optionB={props.optionB}
            optionC={props.optionC}
            optionD={props.optionD}
            optionE={props.optionE}
            answer={props.answer}
            numAnswers={props.numAnswers}
            difficultyLevel={props.difficultyLevel}
            answerValue={props.answerValue}
            negativeMarkValue={props.negativeMarkValue}
            changedoptionA={props.changedoptionA}
            changedoptionB={props.changedoptionB}
            changedoptionC={props.changedoptionC}
            changedoptionD={props.changedoptionD}
            changedoptionE={props.changedoptionE}
            changeQuestionDetailHandler={props.changeQuestionDetailHandler}
            changeOptionAHandler={props.changeOptionAHandler}
            changeOptionBHandler={props.changeOptionBHandler}
            changeOptionCHandler={props.changeOptionCHandler}
            changeOptionDHandler={props.changeOptionDHandler}
            changeOptionEHandler={props.changeOptionEHandler}
            changeAnswerHandler={props.changeAnswerHandler}
            changeNumAnswersHandler={props.changeNumAnswersHandler}
            changedifficultyLevelHandler={props.changedifficultyLevelHandler}
            changeanswerValueHandler={props.changeanswerValueHandler}
            changenegativeMarkHandler={props.changenegativeMarkHandler}
          />
        </Modal.Body>
      </Modal>
    </>
  );
}

export default QuestionModalSample;
