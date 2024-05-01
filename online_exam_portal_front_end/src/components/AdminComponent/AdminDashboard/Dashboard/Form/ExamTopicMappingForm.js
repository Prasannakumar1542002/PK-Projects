import React from "react";
import { Link } from "react-router-dom";
import { Combobox } from "react-widgets";

export default function ExamTopicMappingForm(props) {
  return (
    <div>
      <form
        onSubmit={props.submitHandler}
        className="rounded-1 row mt-3 d-flex align-items-center justify-content-aroundmin-vw-100"
        style={{ background: "#D9EBFF" }}
        id="examtopping"
      >
        <div className="container">
          <div className="row ms-1 p-5">
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="examId"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Exam Name
              </label>
              <div className="col col-sm-7">
                <Combobox
                  name="examID"
                  data={props.exams}
                  dataKey="examId"
                  textField="examName"
                  value={props.examID}
                  onChange={(value) => props.handleSelectExamChange(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="examIDerr">
                  Please Choose Exam Name
                </div>
              </div>
            </div>
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="TopicName"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Topic Name
              </label>
              <div className="col col-sm-7">
                <Combobox
                  data={props.selectedTopics}
                  dataKey="topicId"
                  textField="topicName"
                  value={props.topicChange}
                  onChange={(value) => props.handleSelectTopicChange(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="topicIDerr">
                  Please Choose Topic Name
                </div>
              </div>
            </div>
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="percentage"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Percentage
              </label>
              <div className="col col-sm-7">
                <input
                  type="text"
                  name="percentage"
                  className="form-control mx-sm-1"
                  defaultValue={props.percentage}
                  onChange={(value) => props.handleChangePercentage(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="percentageerr">
                  Please Enter Percentage divisible by 5 or 10
                </div>
              </div>
              <input
                className="col-3 ms-5"
                type="button"
                value="CALCULATE"
                onClick={props.calculatePercentage}
                style={{
                  fontWeight: "bolder",
                  color: "white",
                  background:
                    "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                }}
              />
            </div>
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="topicPassPercentage"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Topic Pass Percentage
              </label>
              <div className="col col-sm-7">
                <input
                  type="text"
                  name="topicPassPercentage"
                  className="form-control mx-sm-1"
                  defaultValue={props.topicPassPercentage}
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="topicpasspercentageerr"
                >
                  Please Enter Topic Pass Percentage
                </div>
              </div>
            </div>
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="questionsPerExam"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Questions Per Exam
              </label>
              <div className="col col-sm-7">
                {/* <input
                  type="text"
                  name="questionsPerExam"
                  className="form-control mx-sm-5"
                  defaultValue={props.questionsPerExam}
                  // (percentage/100)*questionsPerExam
                  // onChange={(value) => props.handleSelectCountChange(value)}
                /> */}
                <label className="form-control mx-sm-1">
                  {props.questionsPerExam}
                </label>
                <div
                  className="invalid-feedback mx-sm-5"
                  id="questionsperexamerr"
                >
                  Please Enter Questions Per Exam
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          {/* <h5 style={{ color: "red" }} className="text-center">
            {props.message}
          </h5>
          <br />
          <h1 className="hidden">{props.message === undefined ? "" : ""}</h1> */}
          <h4
            style={{ color: "blue", fontWeight: "bolder" }}
            className="text-center"
          >
            {props.message.startsWith("N") ? (
              <span style={{ color: "black" }}>
                CoveredPercentage: {props.showPercentage}
                <span style={{ color: "navy" }}>%</span>
              </span>
            ) : props.message.startsWith("Exceeding") ? (
              <span style={{ color: "black" }}>
                {props.message} Remaining Percentage: {props.showPercentage}
                <span style={{ color: "navy" }}></span>
                <span style={{ color: "navy" }}>%</span>
              </span>
            ) : (
              <span style={{ color: "black" }}>
                Covered Percentage: {props.showPercentage}
                <span style={{ color: "navy" }}>%</span>
                <h4>{props.questions-props.questionsPerExam}</h4>
              </span>
            )}
          </h4>
          <h4 className="text-center">
            {props.questions === 0 || props.questions === undefined
              ? "No remaining questions"
              : props.message.startsWith("Exceeding") ||
                props.message.startsWith("Covered")
              ? ""
              : props.questions
              ? "Remaining Questions:" + props.questions
              : ""}
          </h4>
          <div className="row">
            <div
              className="mx-auto d-flex justify-content-center p-5"
              style={{ width: "500px" }}
            >
              <input
                type="submit"
                value="CREATE"
                className="border-none px-3 py-1 mt-4 mb-2 text-white"
                style={{
                  fontWeight: "bolder",
                  background:
                    "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                }}
              />
              <Link
                exact
                to="/AdminDashboard/Exam"
                style={{
                  textDecoration: "none",
                  fontWeight: "bolder",
                  background:
                    "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                  padding: "9px",
                  border: "2px solid gray",
                  width: "0.9in",
                }}
                className="ms-3 mt-4 mb-2 text-white d-flex justify-content-center"
              >
                CLOSE
              </Link>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}
