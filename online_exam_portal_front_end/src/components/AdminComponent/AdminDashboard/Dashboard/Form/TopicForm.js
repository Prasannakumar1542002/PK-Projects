import React from "react";

export default function TopicForm(props) {
  return (
    <form
      onSubmit={props.submitHandler}
      className="min-vw-50 p-4 rounded-1"
      style={{ background: "#D9EBFF" }}
      id="topic"
    >
      <div className="row mt-3 d-flex align-items-center justify-content-center">
        <label
          htmlFor="topicName"
          className="col-sm-2 mt-2"
          style={{ fontWeight: "bolder" }}
        >
          Topic Name
        </label>
        <div className="col-auto">
          <input
            type="text"
            name="topicName"
            className="form-control mx-sm-3"
            defaultValue={props.topicName?props.topicName:""}
            onChange={(value)=>props.changeHandler(value)}
          />
          <div className="invalid-feedback mx-sm-5" id="topicnameerr">
            Please Enter Topic Name
          </div>
        </div>
      </div>
      <div
        className="mx-auto d-flex justify-content-between"
        style={{ width: "200px" }}
      >
        <input
          type="submit"
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
          onClick={props.handleCloseAdd}
          style={{
            fontWeight: "bolder",
            background:
              "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
          }}
          className="border-none px-3 py-1 mt-4 mb-2 text-white"
          value="CLOSE"
/>
      </div>
    </form>
  );
}
