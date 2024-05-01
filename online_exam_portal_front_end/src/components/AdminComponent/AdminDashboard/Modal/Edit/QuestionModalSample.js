import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import QuestionForm from "../../Dashboard/Form/QuestionForm";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit } from "@fortawesome/free-solid-svg-icons";




function QuestionModalSample(props) {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  // alert(props.questionType);
  console.log("&&&&&&&&&&&&&&&&&& " + props.changedOptionC);
  console.log(props.questionDetail);
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
      questionType: props.questionType,
      difficultyLevel: props.changeddifficultyLevel
        ? props.changeddifficultyLevel
        : props.difficultyLevel.toString(),
      answerValue: props.changedanswerValue
        ? props.changedanswerValue
        : props.answerValue.toString(),
      topicId: props.topicChange,
      negativeMarkValue: props.changednegativeMarkValue
        ? props.changednegativeMarkValue
        : props.negativeMarkValue.toString(),
    };
    console.log(data_map);

    // FETCH
    fetch(
      "https://" +
        window.location.hostname +
        ":8443/OnlineExamPortal/control/update-question",
      {
        method: "PUT",
        credentials: "include",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify(data_map),
      }
    )
      .then((response) => {
        return response.json();
      })
      .then((fetch_data) => {
        console.log(fetch_data);
        props.fetchQuestions();
      });
    // props.setQuesType("CHOOSE ONE");
    // props.setTopicChange("CHOOSE ONE");
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
          display="none"
          setQuesType={props.setQuesType}
            buttonName={props.buttonName}
            handleCloseQuestion={handleClose}
            submitHandler={submitHandler}
            handleSelectQuesTypeChange={props.handleSelectQuesTypeChange}
            // quesType={props.quesType}//question
            setEnum={props.setEnum}
            handleSelectTopicChange={props.handleSelectTopicChange}
            topicChange={props.topicChange}
            topics={props.topics}
            questionDetail={props.questionDetail}
            // topicId={props.topicId}
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
            changedquestionDetail={props.changedquestionDetail}
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
