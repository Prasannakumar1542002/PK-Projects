import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Dashboard() {
  const [exams, setExams] = useState([]);
  const [selectedExam, setSelectedExam] = useState(null);

  useEffect(() => {
    fetchExams();
  }, []);

  const fetchExams = async () => {
    try {
      const response = await fetch(
        "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/fetch-exams",
        {
          method: "GET",
          credentials: "include",
        }
      );
      if (!response.ok) {
        throw new Error();
      }
      const data = await response.json();
      var list = data.ExamInfo.ExamList;
      setExams(list);
    } catch (error) {
      console.log(error);
    }
  };

  const handleViewExam = (examId) => {
    setSelectedExam(examId === selectedExam ? null : examId);
  };

  return (
    <>
      <h1 align="center">Exam View</h1>
      <div className="m-5">
        <div className="container">
          <div className="row display-flex justify-content-center">
            {exams ? exams.map((data, i) => (
              <div
                key={i}
                className="col-md-2 border p-4 border-dark d-flex justify-content-center m-2"
                style={{ boxSizing: "content-box", height: "4in" }}
              >
                <div className="row">
                  <p className="fw-bolder text-center">{data.examName}</p>
                  <p className="fst-italic text-center">Duration : {data.durationMinutes}mins</p>
                  <p className="fst-italic text-center">Description : {data.description}</p>
                  <div className="mt-3 d-flex justify-content-center">
                    <button
                      className="fw-bold border p-2 border-secondary text-light w-100 h-100"
                      style={{ backgroundColor: "#00204a" }}
                      onClick={() => handleViewExam(data.examId)}
                    >
                      View Exam
                    </button>
                  </div>

                  {selectedExam === data.examId && (
                    <div className="accordion mt-3" id={`examAccordion${data.examId}`}>
                      <div className="accordion-item">
                        <h2 className="accordion-header" id={`heading${data.examId}`}>
                          <button
                            className="accordion-button"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target={`#collapse${data.examId}`}
                            aria-expanded="true"
                            aria-controls={`collapse${data.examId}`}
                          >
                            Exam Details
                          </button>
                        </h2>
                        <div
                          id={`collapse${data.examId}`}
                          className="accordion-collapse collapse show"
                          aria-labelledby={`heading${data.examId}`}
                          data-bs-parent={`#examAccordion${data.examId}`}
                        >
                          <div className="accordion-body"  style={{ overflowY: "auto", maxHeight: "200px"}}>
                            <p>{data.creationDate}</p>
                            <p>{data.expirationDate}</p>
                          </div>
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            )):<>No Exams</>}
          </div>
        </div>
      </div>
    </>
  );
}

export default Dashboard;
