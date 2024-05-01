import { useState, useEffect } from "react";
import TopicForm from "../Dashboard/Form/TopicForm";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import TopicModalEditSample from "../Modal/Edit/TopicModalEditSample";

function Topic() {
  const [display, setDisplay] = useState({
    display: "none",
  });
  const [topics, setTopics] = useState([]);
  const [changedTopic, setChangedTopic] = useState("");

  const changeHandler = (e) => {
    setChangedTopic(e.target.value);
  };

  function handleAddSubject() {
    setDisplay({ display: "block" });
  }

  function handleCloseAdd() {
    setDisplay({ display: "none" });
  }
  const createSubmitHandler = (e) => {
    e.preventDefault();
    var form = document.getElementById("topic");
    const formData = new FormData(form);
    var data_map = {
      topicName: formData.get("topicName"),
    };
    // FETCH
    if (data_map.topicName === "") {
      document.getElementById("topicnameerr").style.display = "block";
    } else {
      try {
        document.getElementById("topicnameerr").style.display = "none";
        fetch("https://"+window.location.hostname + ":8443/OnlineExamPortal/control/create-topic", {
          method: "POST",
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
            fetchTopics();
            setDisplay({display:"none"});
          });
      } catch (error) {
        console.log(error);
      }
    }
    form.reset();
  };

  useEffect(() => {
    fetchTopics();
  }, []);


  const fetchTopics = async () => {
    try {
      const response = await fetch(
        "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/fetch-topics",
        {
          method: "POST",
          credentials: "include",
        }
      );
      if (!response.ok) {
        throw new Error();
      }
      const data = await response.json();
      console.log(data);
      setTopics(undefined);
      var list = data.TopicInfo.TopicList;
      setTopics(list);
      console.log(list);
    } catch (error) {
      console.log(error);
    }
  };
  console.log(topics);

  const handleDeleteTopic = async (id) => {
    const data_map={
      topicId:id,
    }
    try {
      const response = await fetch(
        "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/delete-topic",
        {
          method: "DELETE",
          credentials: "include",
          
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify(data_map),
        }
      );
      console.log(response);
      fetchTopics();
    } catch (error) {
      console.log(error);
    }
  };
  if (topics === undefined || topics.length === 0)
    return (
      <>
        <div>
          <div className="d-flex justify-content-center min-vh-2 text-black">
          <TopicForm
              type="submit"
              buttonName="CREATE"
              submitHandler={createSubmitHandler}
              handleCloseAdd={handleCloseAdd}
              changedTopic={changedTopic}
              changeHandler={changeHandler}
            />
          </div>
        </div>
      </>
    );

  return (
    <>
      <div>
        <div>
          <h2 align="center">Topic List</h2>
        </div>

        <div className="table-responsive-sm container-fluid">
          <table className="table table-striped table-hover table-light table-responsive-sm">
            <thead>
              <tr>
                <th scope="col">Topic ID</th>
                <th scope="col">Topic Name</th>
                <th scope="col">Edit</th>
                <th scope="col">Delete</th>
              </tr>
            </thead>
            <tbody>
              {topics ?
                topics.map((data, i) => {
                  return (
                    <tr key={i}>
                      <td>{data.topicId}</td>
                      <td>{data.topicName}</td>
                      <td
                      >
                        <TopicModalEditSample
                          type="button"
                          buttonName="UPDATE"
                          topicName={data.topicName}
                          fetchId={data.topicId}
                          changedTopic={changedTopic}
                          fetchTopics={fetchTopics}
                          changeHandler={changeHandler}
                        />
                      </td>
                      <td>
                        <FontAwesomeIcon
                          icon={faTrash}
                          style={{ color: "red", cursor: "pointer" }}
                          onClick={() => handleDeleteTopic(data.topicId)}
                        />
                      </td>
                    </tr>
                  );
                }):<>No more Topics</>}
            </tbody>
          </table>
        </div>

        <div className="text-center">
          <button
            onClick={handleAddSubject}
            className="border  px-3 py-1 mt-4 mb-2 text-white"
            style={{
              fontWeight: "bolder",
              background:
                "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
            }}
          >
            Add Topic
          </button>
        </div>
        <div style={display}>
          <div className="d-flex justify-content-center min-vh-2 text-black">
            <TopicForm
              type="submit"
              buttonName="CREATE"
              submitHandler={createSubmitHandler}
              handleCloseAdd={handleCloseAdd}
              changedTopic={changedTopic}
              changeHandler={changeHandler}
            />
          </div>
        </div>
      </div>
    </>
  );
}

export default Topic;