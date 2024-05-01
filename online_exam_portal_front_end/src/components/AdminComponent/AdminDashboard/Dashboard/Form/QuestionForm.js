import React, { useEffect, useState } from "react";
import { Combobox } from "react-widgets";
import MultipleChoice from "../Options/MultipleChoice";
import TrueFalse from "../Options/TrueFalse";
import SingleChoice from "../Options/SingleChoice";
import FillUp from "../Options/FillUp";
import Detail from "../Options/Detail";

export default function QuestionForm(props) {
  const [mcqOption, showMcqOption] = useState({ display: "none" });
  const [truefalseOption, showtruefalseOption] = useState({ display: "none" });
  const [singleChoiceOption, showsingleChoiceOption] = useState({ display: "none" });
  const [fillup, showFillUp] = useState({ display: "none" });
  const [detail, showDetail] = useState({ display: "none" });

  console.log("OOOOOOOOOOOOOOOOOOOOOOOOOO "+props.optionC)

  useEffect(() => {
    switch (props.questionType) {
      case "QT_MC":
        showMcqOption({ display: "block" });
        showtruefalseOption({ display: "none" });
        showsingleChoiceOption({ display: "none" });
        showDetail({ display: "none" });
        showFillUp({ display: "none" });
        break;
      case "QT_TF":
        // props.set
        showtruefalseOption({ display: "block" });
        showMcqOption({ display: "none" });
        showsingleChoiceOption({ display: "none" });
        showDetail({ display: "none" });
        showFillUp({ display: "none" });
        break;
      case "QT_SC":
        showsingleChoiceOption({ display: "block" });
        showMcqOption({ display: "none" });
        showtruefalseOption({ display: "none" });
        showDetail({ display: "none" });
        showFillUp({ display: "none" });
        break;
      case "QT_FIB":
        showsingleChoiceOption({ display: "none" });
        showMcqOption({ display: "none" });
        showtruefalseOption({ display: "none" });
        showDetail({ display: "none" });
        showFillUp({ display: "block" });
        break;
      case "QT_DA":
        showsingleChoiceOption({ display: "none" });
        showMcqOption({ display: "none" });
        showtruefalseOption({ display: "none" });
        showDetail({ display: "block" });
        showFillUp({ display: "none" });
        break;
      default:
        showsingleChoiceOption({ display: "none" });
        showMcqOption({ display: "none" });
        showtruefalseOption({ display: "none" });
        showDetail({ display: "none" });
        showFillUp({ display: "none" });
        break;
    }
  },[props.questionType]);

  return (
    <div>
      <form
        onSubmit={props.submitHandler}
        className="min-vw-50 p-4 rounded-1"
        style={{ background: "#D9EBFF" }}
        id="question"
      >
        <div className="container">
          <div className="row">
            <div className="col-12 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="questionDetail"
                className="col-sm-2 mt-3"
                style={{ fontWeight: "bolder" }}
              >
                Question Detail
              </label>
              <div className="col-12">
                <textarea
                  name="questionDetail"
                  className="form-control"
                  defaultValue={props.questionDetail}
                  onChange={(value) => props.changeQuestionDetailHandler(value)}
                ></textarea>
                <div
                  className="invalid-feedback mx-sm-5"
                  id="questiondetailerr"
                >
                  Please Enter Question Detail
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
          <label
            htmlFor="answer"
            className="col-sm-2 mt-3"
            style={{ fontWeight: "bolder" }}
          >
            Answer
          </label>
          <div className="col-auto">
            <textarea
              type="text"
              name="answer"
              className="form-control mx-sm-5"
              defaultValue={props.answer}
              onChange={(value) => props.changeAnswerHandler(value)}
            ></textarea>
            <div className="invalid-feedback mx-sm-5" id="answererr">
              Please Enter Answer
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="numAnswers"
                className="col-sm-2 mt-3"
                style={{ fontWeight: "bolder" }}
              >
                Num Of Answers
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="numAnswers"
                  className="form-control mx-sm-5"
                  defaultValue={props.numAnswers}
                  onChange={(value) => props.changeNumAnswersHandler(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="numanserr">
                  Please Enter Num Of Answers
                </div>
              </div>
            </div>

            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center ms-3">
              <label
                htmlFor="questionType"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Question Type
              </label>
              <div className="col-md-8">
                <Combobox
                className="ms-sm-5 w-100 mx-sm-5"
                disabled={props.questionType==="CHOOSE ONE"?false:true}
                  name="enumId"
                  data={props.setEnum}
                  dataKey="enumId"
                  textField="description"
                  id="questionType"
                  onChange={(value) => props.handleSelectQuesTypeChange(value)}
                  value={
                    props.questionType
                  }
/>
                <div className="invalid-feedback mx-sm-5" id="questiontypeerr">
                  Please Enter Question Type
                </div>
                <div className="mx-sm-5 mt-2"
                style={{display:props.display}}>
                <input type="button" className={props.questionType==="CHOOSE ONE"?"btn btn-success":props.questionType?"btn btn-success":"d-none"} value="CLEAR" onClick={props.resetQuestionType}/>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div style={mcqOption}>
          <MultipleChoice
            optionA={props.optionA}
            optionB={props.optionB}
            optionC={props.optionC}
            optionD={props.optionD}
            optionE={props.optionE}
            changedoptionA={props.changedoptionA}
            changedoptionB={props.changedoptionB}
            changedoptionC={props.changedoptionC}
            changedoptionD={props.changedoptionD}
            changedoptionE={props.changedoptionE}
            changeOptionAHandler={props.changeOptionAHandler}
            changeOptionBHandler={props.changeOptionBHandler}
            changeOptionCHandler={props.changeOptionCHandler}
            changeOptionDHandler={props.changeOptionDHandler}
            changeOptionEHandler={props.changeOptionEHandler}
          />
        </div>
        <div style={truefalseOption}>
          <TrueFalse
            optionA={props.optionA}
            optionB={props.optionB}
            optionC={props.optionC}
            optionD={props.optionD}
            optionE={props.optionE}
            changedoptionA={props.changedoptionA}
            changedoptionB={props.changedoptionB}
            changedoptionC={props.changedoptionC}
            changedoptionD={props.changedoptionD}
            changedoptionE={props.changedoptionE}
            changeOptionAHandler={props.changeOptionAHandler}
            changeOptionBHandler={props.changeOptionBHandler}
            changeOptionCHandler={props.changeOptionCHandler}
            
            changeOptionDHandler={props.changeOptionDHandler}
            changeOptionEHandler={props.changeOptionEHandler}
          />
        </div>
        <div style={singleChoiceOption}>
          <SingleChoice
            optionA={props.optionA}
            optionB={props.optionB}
            optionC={props.optionC}
            optionD={props.optionD}
            optionE={props.optionE}
            changedoptionA={props.changedoptionA}
            changedoptionB={props.changedoptionB}
            changedoptionC={props.changedoptionC}
            changedoptionD={props.changedoptionD}
            changedoptionE={props.changedoptionE}
            changeOptionAHandler={props.changeOptionAHandler}
            changeOptionBHandler={props.changeOptionBHandler}
            changeOptionCHandler={props.changeOptionCHandler}
            changeOptionDHandler={props.changeOptionDHandler}
            changeOptionEHandler={props.changeOptionEHandler}
          />
        </div>
        <div style={fillup}>
          <FillUp 
          optionA={props.optionA}
          optionB={props.optionB}
          optionC={props.optionC}
          optionD={props.optionD}
          optionE={props.optionE}
          changedoptionA={props.changedoptionA}
          changedoptionB={props.changedoptionB}
          changedoptionC={props.changedoptionC}
          changedoptionD={props.changedoptionD}
          changedoptionE={props.changedoptionE}
          changeOptionAHandler={props.changeOptionAHandler}
          changeOptionBHandler={props.changeOptionBHandler}
          changeOptionCHandler={props.changeOptionCHandler}
          
          changeOptionDHandler={props.changeOptionDHandler}
          changeOptionEHandler={props.changeOptionEHandler}/>
        </div>
        <div style={detail}>
          <Detail 
          optionA={props.optionA}
          optionB={props.optionB}
          optionC={props.optionC}
          optionD={props.optionD}
          optionE={props.optionE}
          changedoptionA={props.changedoptionA}
          changedoptionB={props.changedoptionB}
          changedoptionC={props.changedoptionC}
          changedoptionD={props.changedoptionD}
          changedoptionE={props.changedoptionE}
          changeOptionAHandler={props.changeOptionAHandler}
          changeOptionBHandler={props.changeOptionBHandler}
          changeOptionCHandler={props.changeOptionCHandler}
          
          changeOptionDHandler={props.changeOptionDHandler}
          changeOptionEHandler={props.changeOptionEHandler}/>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="difficultyLevel"
                className="col-sm-2 mt-3"
                style={{ fontWeight: "bolder" }}
              >
                Difficulty Level
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="difficultyLevel"
                  className="form-control mx-sm-5"
                  defaultValue={props.difficultyLevel}
                  onChange={(value) =>
                    props.changedifficultyLevelHandler(value)
                  }
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="difficultylevelerr"
                >
                  Please Enter Difficulty Level
                </div>
              </div>
            </div>
            <div className="col-6 row mt-3 ms-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="answerValue"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Answer Value
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="answerValue"
                  className="form-control mx-sm-5"
                  defaultValue={props.answerValue}
                  onChange={(value) => props.changeanswerValueHandler(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="answervalueerr">
                  Please Enter Answer Value
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="topicId"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Topic Name
              </label>
              <div className="col-md-8">
                <Combobox
                className="ms-sm-5 w-100 mx-sm-5"
                // style={{fontSize: "1vw"}}
                disabled={props.topicChange==="CHOOSE ONE"?false:true}
                  name="topicId"
                  data={props.topics}
                  dataKey="topicId"
                  textField="topicName"
                  onChange={(value) => props.handleSelectTopicChange(value)}
                  value={props.topicChange}
                />
                <div className="invalid-feedback mx-sm-5" id="topiciderr">
                  Please Choose Topic Name
                </div>
                <div className="mx-sm-5 mt-2" style={{display:props.display}}>
                <input type="button" className={props.topicChange==="CHOOSE ONE"?"btn btn-success":props.topicChange?"btn btn-success":"d-none"} value="CLEAR" onClick={props.resetTopic}/>
                  </div>
              </div>
            </div>
            <div className="col-6 row mt-3 ms-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="negativeMarkValue"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Negative Mark Value
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="negativeMarkValue"
                  className="form-control mx-sm-5"
                  defaultValue={props.negativeMarkValue}
                  onChange={(value) => props.changenegativeMarkHandler(value)}
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="negativemarkvalueeerr"
                >
                  Please Enter Negative Mark Value
                </div>
              </div>
            </div>
          </div>
        </div>
        <div
          className="mx-auto d-flex justify-content-between"
          style={{ width: "200px" }}
        >
          <input
            type="submit"
            name="submit"
            value={props.buttonName}
            className="border-none px-3 py-1 mt-4 mb-2 text-white"
            style={{
              fontWeight: "bolder",
              background:
                "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
            }}
          />
          <input
          type="button"
            onClick={props.handleCloseQuestion}
            style={{
              fontWeight: "bolder",
              background:
                "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
            }}
            className="border-none px-3 py-1 mt-4 mb-2 text-white"
        value="Close"
          />
        </div>
      </form>
    </div>
  );
}
