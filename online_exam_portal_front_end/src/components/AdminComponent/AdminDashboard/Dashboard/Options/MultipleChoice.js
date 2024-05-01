import React from "react";

export default function MultipleChoice(props) {
  return (
    <>
      <div className="container">
        <div className="row">
          <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
            <label
              htmlFor="optionA"
              className="col-sm-2 mt-3"
              style={{ fontWeight: "bolder" }}
            >
              Option A
            </label>
            <div className="col-auto">
              <textarea
                type="text"
                name="optionA"
                className="form-control mx-sm-5"
                defaultValue={props.optionA ? props.optionA : ""}
                onChange={(value) => props.changeOptionAHandler(value)}
              ></textarea>
              <div className="invalid-feedback mx-sm-5" id="optionaerr">
                Please Enter Option A
              </div>
            </div>
          </div>
          <div className="col-6 row mt-3 ms-3 d-flex align-items-center justify-content-center">
            <label
              htmlFor="optionB"
              className="col-sm-2 mt-2"
              style={{ fontWeight: "bolder" }}
            >
              Option B
            </label>
            <div className="col-auto">
              <textarea
                type="text"
                name="optionB"
                className="form-control mx-sm-5"
                defaultValue={props.optionB ? props.optionB : ""}
                onChange={(value) => props.changeOptionBHandler(value)}
              ></textarea>
              <div className="invalid-feedback mx-sm-5" id="optionberr">
                Please Enter Option B
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="container">
        <div className="row">
          <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
            <label
              htmlFor="optionC"
              className="col-sm-2 mt-3"
              style={{ fontWeight: "bolder" }}
            >
              Option C
            </label>
            <div className="col-auto">
              <textarea
                type="text"
                name="optionC"
                className="form-control mx-sm-5"
                defaultValue={props.optionC ? props.optionC : ""}
                onChange={(value) => props.changeOptionCHandler(value)}
              ></textarea>
              <div className="invalid-feedback mx-sm-5" id="optioncerr">
                Please Enter Option C
              </div>
            </div>
          </div>
          <div className="col-6 row mt-3 ms-3 d-flex align-items-center justify-content-center">
            <label
              htmlFor="optionD"
              className="col-sm-2 mt-2"
              style={{ fontWeight: "bolder" }}
            >
              Option D
            </label>
            <div className="col-auto">
              <textarea
                type="text"
                name="optionD"
                className="form-control mx-sm-5"
                defaultValue={props.optionD ? props.optionD : ""}
                onChange={(value) => props.changeOptionDHandler(value)}
              ></textarea>
              <div className="invalid-feedback mx-sm-5" id="optionderr">
                Please Enter Option D
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="container">
        <div className="row">
          <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
            <label
              htmlFor="optionE"
              className="col-sm-2 mt-3"
              style={{ fontWeight: "bolder" }}
            >
              Option E
            </label>
            <div className="col-auto">
              <textarea
                type="text"
                name="optionE"
                className="form-control mx-sm-5"
                defaultValue={props.optionE ? props.optionE : ""}
                onChange={(value) => props.changeOptionEHandler(value)}
              ></textarea>
              <div className="invalid-feedback mx-sm-5" id="optioneerr">
                Please Enter Option E
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
