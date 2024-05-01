import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import TopicForm from "../../Dashboard/Form/TopicForm";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPenToSquare } from '@fortawesome/free-solid-svg-icons';

function TopicModalEditSample(props) {
  // const [topic, setTopic] = useState();
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = async () => 
  {
      // try {
      //   const response = await fetch(
      //     "https://localhost:8443/OnlineExamPortal/control/fetch-one-topic",
      //     {
      //     method: "POST",
      //     credentials: "include",
      //     headers: {
      //       "content-type": "application/json",
      //     },
      //     body: JSON.stringify({topicId:props.fetchId}),
      //   })
          
      //   const data = await response.json();
      //   console.log(data);
      //   var list = data.TopicMaster.topicName;
      //   console.log("------------------------" + list);
      //   setTopic(list);
        setShow(true);
      // } catch (error) {
      //   console.log(error);
      // }
    };


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
        fetch("https://"+window.location.hostname + ":8443/OnlineExamPortal/control/update-topic", {
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
            console.log("UPDATED");
            props.fetchTopics(); // FETCH DONE BY PROPS
          });
      } catch (error) {
        console.log(error);
      }
    }
    setShow(!show);
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
      </Modal>
    </>
  );
}

export default TopicModalEditSample;
