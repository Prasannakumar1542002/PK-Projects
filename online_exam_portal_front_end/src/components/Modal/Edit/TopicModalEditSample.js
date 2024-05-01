import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import TopicForm from "../../AdminComponent/AdminDashboard/Dashboard/Form/TopicForm";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPenToSquare } from '@fortawesome/free-solid-svg-icons';

function TopicModalEditSample(props) {
  console.log("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII " + props.fetchId);
  const [show, setShow] = useState(false);
  // const [changedTopic, setChangedTopic] = useState(props.topicName);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);


  const submitHandler = (e) => {
    e.preventDefault();
    const data_map = {
      topicId: props.fetchId,
      topicName: props.changedTopic?props.changedTopic:props.topicName,
    };
    console.log(data_map);
    if (data_map.topicName === "") {
      document.getElementById("topicnameerr").style.display = "block";
    } else {
      try {
        document.getElementById("topicnameerr").style.display = "none";
        fetch("https://"+window.location.hostname + ":8443/OnlineExamPortal/control/UpdateTopicMaster", {
          method: "PUT",
          credentials: "include",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify(data_map),
        })
          .then((response) => {
            return response.json(); //  converts the response object to JSON to info
          })
          .then((fetch_data) => {
            console.log("UPDATED");
            props.fetchTopics(); // FETCH DONE BY PROPS
          });
      } catch (error) {
        console.log(error);
      }
    }
    data_map.topicName === props.topicName ? setShow(show) : setShow(!show);
  };
  return (
    <>
      <Button variant="link" onClick={handleShow}>
      <FontAwesomeIcon icon={faPenToSquare} />
      </Button>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
      >
        {/*  */}
        <Modal.Header closeButton>
          <Modal.Title>Edit Topic Form</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <TopicForm
            buttonName={props.buttonName}
            topicName={props.topicName}
            submitHandler={submitHandler}
            type={props.type}
            handleCloseAdd={handleClose}
            changeHandler={props.changeHandler}
          />
        </Modal.Body>
        {/* <Modal.Footer className='text-center'>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          {/* <Button variant="primary">Understood</Button>
        </Modal.Footer> */}
      </Modal>
    </>
  );
}

export default TopicModalEditSample;
