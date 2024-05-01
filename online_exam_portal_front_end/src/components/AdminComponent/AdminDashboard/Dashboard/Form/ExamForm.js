import React from "react";
import { Combobox } from "react-widgets";

export default function ExamForm(props) {
  return (
    <div className="w-100">
      <form
        onSubmit={props.submitHandler}
        className="rounded-1 row mt-3 p-5 d-flex align-items-center justify-content-center"
        id="exam"
        style={{ background: "#D9EBFF" }}
      >
        <div className="container">
          <div className="row">
            <div className="col-6 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="description"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Description
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="description"
                  className="form-control mx-sm-5"
                  defaultValue={props.description}
                  onChange={(value) => props.changeDescHandler(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="descriptionerr">
                  Please Enter Description
                </div>
              </div>
            </div>
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="examName"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Exam Name
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="examName"
                  className="form-control mx-sm-5"
                  defaultValue={props.examName}
                  onChange={(value) => props.changeExamNameHandler(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="examnameerr">
                  Please Enter Exam Name
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="creationDate"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Creation Date
              </label>
              <div className="col col-sm-7">
                <input
                  type="datetime-local"
                  name="creationDate"
                  className="form-control mx-sm-5"
                  defaultValue={props.creationDate}
                  onChange={(value) => props.changeCreateDateHandler(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="creationdateerr">
                  Please Choose Creation Date
                </div>
              </div>
            </div>
            <div className="col-12 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="expirationDate"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Expiration Date
              </label>
              <div className="col col-sm-7">
                <input
                  type="datetime-local"
                  name="expirationDate"
                  className="form-control mx-sm-5"
                  defaultValue={props.expirationDate}
                  onChange={(value) => props.changeExpireDateHandler(value)}
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="expirationdateerr"
                >
                  Please Choose Expiration Date
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="noOfQuestions"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                No Of Questions
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="noOfQuestions"
                  className="form-control mx-sm-5"
                  defaultValue={props.noOfQuestions}
                  onChange={(value) => props.changeQuesHandler(value)}
                />
                <div className="invalid-feedback mx-sm-5" id="noofquestionserr">
                  Please Enter No Of Questions
                </div>
              </div>
            </div>
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="durationMinutes"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Duration Minutes
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="durationMinutes"
                  className="form-control mx-sm-5"
                  defaultValue={props.durationMinutes}
                  onChange={(value) => props.changeDurationHandler(value)}
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="durationminuteserr"
                >
                  Please Enter Duration Minutes
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="passPercentage"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Pass Percentage
              </label>
              <div className="col-auto">
                <input
                  type="text"
                  name="passPercentage"
                  className="form-control mx-sm-5"
                  defaultValue={props.passPercentage}
                  onChange={(value) => props.changePassPercentHandler(value)}
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="passpercentageerr"
                >
                  Please Enter Pass Percentage
                </div>
              </div>
            </div>
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                htmlFor="questionsRandomized"
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Questions Randomized
              </label>
              <div className="col-auto">
                <div className="form-check form-check-inline mx-sm-5">
                  <Combobox
                    name="questionsRandomized"
                    data={props.option}
                    dataKey="id"
                    textField="display"
                    defaultValue={
                      props.questionsRandomized
                        ? props.questionsRandomized
                        : "Y"
                    }
                    onChange={(value) => props.changeQuesRandHandler(value)}
                  />
                  <div
                    className="invalid-feedback mx-sm-5"
                    id="questionsrandomizederr"
                  >
                    Please Choose Questions Randomized
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row"></div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
              <label
                className="col-sm-2 mt-2"
                style={{ fontWeight: "bolder" }}
              >
                Enable Negative Mark
              </label>
              <div className="col-auto">
                <div className="col-auto">
                  <div className="form-check form-check-inline mx-sm-5">
                    <Combobox
                      name="enableNegativeMark"
                      data={props.option}
                      dataKey="id"
                      textField="display"
                      defaultValue={
                        props.enableNegativeMark
                          ? props.enableNegativeMark
                          : "Y"
                      }
                      onChange={(value) =>
                        props.changeEnableNegMarkHandler(value)
                      }
                    />
                    <div
                      className="invalid-feedback mx-sm-5"
                      id="enablenegativemarkerr"
                    >
                      Please Choose Negative Mark
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
            <label
              htmlFor="answersMust"
              className="col-sm-2 mt-2"
              style={{ fontWeight: "bolder" }}
            >
              Answers Must
            </label>
            <div className="col-auto">
              <div className="form-check form-check-inline mx-sm-5">
                <Combobox
                  name="answersMust"
                  data={props.option}
                  dataKey="id"
                  textField="display"
                  defaultValue={props.answersMust ? props.answersMust : "Y"}
                  onChange={(value) => props.changeEnableAnsMustHandler(value)}
                />
                <div
                  className="invalid-feedback ms-5"
                  id="answersmusterr"
                  style={{ display: "none" }}
                >
                  Please Choose Answers Must
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
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
                  onChange={(value) => props.changeNegMarkHandler(value)}
                />
                <div
                  className="invalid-feedback mx-sm-5"
                  id="negativemarkvalueerr"
                >
                  Please Enter Negative Mark Value
                </div>
              </div>
              <div className="row mx-auto d-flex justify-content-center ml-5">
                <input
                  type="submit"
                  name="submit"
                  value={props.buttonName}
                  className="border-none mt-4 mb-2 text-white col-4"
                  style={{
                    fontWeight: "bolder",
                    background:
                      "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                    padding: "9px",
                  }}
                />
                <button
                  onClick={props.handleCloseExam}
                  style={{
                    fontWeight: "bolder",
                    background:
                      "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                  }}
                  className="border-none px-3 py-1 mt-4 mb-2 text-white col-4"
                >
                  CLOSE
                </button>
              </div>
              <div>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}
