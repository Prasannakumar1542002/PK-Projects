import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Combobox } from "react-widgets";
import Swal from 'sweetalert2';

export default function UserExamMappingForm(props) {
    const [exams, setExams] = useState([]);
    const [userExamMapping, setUserExamMapping] = useState([]);

    const option = [
        {
            id: 0,
            display: "Y",
        },
        {
            id: 1,
            display: "N",
        },
    ];

    useEffect(() => {
        fetchExam();
        userexammapping();
    }, []);

    const [timeoutDays, setTimeoutDays] = useState("");
    const [partyId, setPartyId] = useState("");
    const [examId, setExamId] = useState("");
    const [lastPerformanceDate, setlastPerformanceDate] = useState("");
    const [allowedAttempts, setallowedAttempts] = useState("");
    const [noOfAttempts, setnoOfAttempts] = useState("");
    const [passwordChangesAuto, setpasswordChangesAuto] = useState("");
    const [canSplitExams, setcanSplitExamsDays] = useState("Y");
    const [canSeeDetailedResults, setcanSeeDetailedResults] = useState("Y");
    const [maxSplitAttempts, setmaxSplitAttempts] = useState("");

    const fetchExam = async () => {
        try {
            const response = await fetch(
                "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/fetch-exams",
                {
                    method: "POST",
                    credentials: "include",
                }
            );
            if (!response.ok) {
                throw new Error();
            }
            const data = await response.json();
            console.log("ExamFetch",data);
            var list = data.ExamInfo.ExamList;
            setExams(list);
        } catch (error) {
            console.log(error);
        }
    };

    const userexammapping = async () => {
        try {
            const response = await fetch(
                "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/fetch-all-users",
                {
                    method: "POST",
                    credentials: "include",
                }
            );
            if (!response.ok) {
                throw new Error();
            }
            const data = await response.json();
            console.log("userexammappingfetch",data);
            var list = data.UserExamInfo.UserExamList;
            setUserExamMapping(list);
        } catch (error) {
            console.log(error);
        }
    };

    const submitHandler = (e) => {
        e.preventDefault();
        var form = document.getElementById("userexammapping");
        const formData = new FormData(form);
        const data_map = {
            partyId: formData.get("partyId"),
            examId: examId,
            allowedAttempts: formData.get("allowedAttempts"),
            noOfAttempts: formData.get("noOfAttempts"),
            lastPerformanceDate: formData.get("lastPerformanceDate"),
            timeoutDays: formData.get("timeoutDays"),
            passwordChangesAuto: formData.get("passwordChangesAuto"),
            canSplitExams: canSplitExams,
            canSeeDetailedResults: canSeeDetailedResults,
            maxSplitAttempts: maxSplitAttempts,
        };
        console.log(data_map);

        if (data_map.partyId === "Choose ONE") {
            document.getElementById("partyIderr").style.display = "block";
        } else {
            document.getElementById("partyIderr").style.display = "none";
        }
        if (data_map.examId === "Choose ONE") {
            document.getElementById("examIderr").style.display = "block";
        } else {
            document.getElementById("examIderr").style.display = "none";
        }
        if (data_map.noOfAttempts === "") {
            document.getElementById("noOfAttemptserr").style.display = "block";
        } else {
            document.getElementById("noOfAttemptserr").style.display = "none";
        }
        if (data_map.lastPerformanceDate === "") {
            document.getElementById("lastPerformanceDateerr").style.display = "block";
        } else {
            document.getElementById("lastPerformanceDateerr").style.display = "none";
        }
        if (data_map.timeoutDays === "") {
            document.getElementById("timeoutDayserr").style.display = "block";
        } else {
            document.getElementById("timeoutDayserr").style.display = "none";
        }
        if (data_map.allowedAttempts === "") {
            document.getElementById("allowedAttemptserr").style.display = "block";
        } else {
            document.getElementById("allowedAttemptserr").style.display = "none";
        }
        if (data_map.canSplitExams === "") {
            document.getElementById("canSplitExamserr").style.display = "block";
        } else {
            document.getElementById("canSplitExamserr").style.display = "none";
        }
        if (data_map.canSeeDetailedResults === "") {
            document.getElementById("canSeeDetailedResultserr").style.display = "block";
        } else {
            document.getElementById("canSeeDetailedResultserr").style.display = "none";
        }
        if (data_map.maxSplitAttempts === "") {
            document.getElementById("maxSplitAttemptserr").style.display = "block";
        } else {
            document.getElementById("maxSplitAttemptserr").style.display = "none";
        }

        if (data_map.passwordChangesAuto === "") {
            document.getElementById("passwordChangesAutoerr").style.display = "block";
        } else {
            document.getElementById("passwordChangesAutoerr").style.display = "none";
        }


        if (
            !(data_map.partyId === "" || data_map.examId === "" ||
                data_map.allowedAttempts === "" || data_map.noOfAttempts === ""
                || data_map.lastPerformanceDate === "" || data_map.timeoutDays === "" || data_map.canSplitExams === "" ||
                data_map.canSeeDetailedResults === "" || data_map.maxSplitAttempts === "" ||
                data_map.passwordChangesAuto === ""

            )
        ) {
            try {
                fetch(
                    "https://"+window.location.hostname + ":8443/OnlineExamPortal/control/create-exam-for-user",
                    {
                        method: "POST",
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
                        Swal.fire({
                            icon: "success",
                            title: "Success",
                            text: "Exam Assigned successfully",
                            footer: "Powerful People Make Places Powerful"
                        });
                        console.log(fetch_data);
                        userexammapping();
                    });
            } catch (error) {
                Swal.fire({
                    icon: "error",
                    title: "fail",
                    text: "Exam Assigned successfully",
                    footer: "Powerful People Make Places Powerful"
                });
                console.log(error);
            }
        }
        else {
            console.log("Some Fields are empty");
        }
        form.reset(); 
    };



    const changepasswordChangesAutoHandler = (e) => {
        setpasswordChangesAuto(e.display);
    };

    const changePartyIdHandler = (e) => {
        setPartyId(e.value);
    };

    const changeExamNameHandler = (e) => {
        setExamId(e.examId);
    };

    const changecanSplitExamsHandler = (e) => {
        setcanSplitExamsDays(e.display);
    };

    const changecanSeeDetailedResultsHandler = (e) => {
        setcanSeeDetailedResults(e.display);
    };

    const changeNoOfAttemptHandler = (e) => {
        setnoOfAttempts(e.display);
    };

    const changeAllowedAttemptsHandler = (e) => {
        setallowedAttempts(e.target.value);
    };

    const changeLastPerformanceDateHandler = (e) => {
        setlastPerformanceDate(e.target.value);
    };

    const changeTimeOutDays = (e) => {
        setTimeoutDays(e.display);
    };

    const changeMaxSplitAttempt = (e) => {
        setmaxSplitAttempts(e.target.value);
    };

    if (userExamMapping.length === 0) {

    }

    return (
        <div>
            <div className="table-responsive-sm">
                <table className="table table-responsive-sm table-borderless">
                    <thead>
                        <tr>
                            <th scope="col">Party ID</th>
                            <th scope="col">Exam ID</th>
                            <th scope="col">No Of Attempts</th>
                            <th scope="col">Allowed Attempts</th>
                            <th scope="col">Last Performance Date</th>
                            <th scope="col">Can Split Exams</th>
                            <th scope="col">Password Changes Auto</th>
                            <th scope="col">Max Split Attempts</th>
                            <th scope="col">Timeout Days</th>
                            <th scope="col">Can See Detailed Results</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userExamMapping &&
                            userExamMapping.map((data, i) => {
                                return (
                                    <tr key={i}>
                                        <td>{data.partyId}</td>
                                        <td>{data.examId}</td>
                                        <td>{data.noOfAttempts}</td>
                                        <td>{data.allowedAttempts}</td>
                                        <td>{data.lastPerformanceDate}</td>
                                        <td>{data.canSplitExams}</td>
                                        <td>{data.passwordChangesAuto}</td>
                                        <td>{data.maxSplitAttempts}</td>
                                        <td>{data.timeoutDays}</td>
                                        <td>{data.canSeeDetailedResults}</td>
                                    </tr>
                                );
                            })}
                    </tbody>
                </table>
            </div>
            <form
                onSubmit={submitHandler}
                className="rounded-1 row mt-3 p-5 d-flex align-items-center justify-content-center"
                id="userexammapping"
                style={{ background: "#D9EBFF" }}
            >
                <div className="container">
                    <div className="row">
                        <div className="col-6 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="partyId"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Party Id
                            </label>
                            <div className="col-auto">
                                <Combobox
                                    name="partyId"
                                    data={props.students}
                                    dataKey="partyId"
                                    textField="partyId"
                                    defaultValue={partyId ? partyId : "Choose ONE"}
                                    onChange={(value) => changePartyIdHandler(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="partyIderr">
                                    Please Choose Party ID
                                </div>
                            </div>
                        </div>
                        <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="examId"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Exam ID
                            </label>
                            <div className="col-auto">
                                <Combobox
                                    name="examId"
                                    data={exams}
                                    dataKey="examId"
                                    textField="examName"
                                    defaultValue={examId ? examId : "Choose ONE"}
                                    onChange={(value) => changeExamNameHandler(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="examIderr">
                                    Please Choose Exam ID
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="container">
                    <div className="row">
                        <div className="col-6 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="allowedAttempts"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Allowed Attempts
                            </label>
                            <div className="col-auto">
                                <input
                                    type="text"
                                    name="allowedAttempts"
                                    className="form-control mx-sm-5"
                                    defaultValue={allowedAttempts}
                                    onChange={(value) => changeAllowedAttemptsHandler(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="allowedAttemptserr">
                                    Please Enter Allowed Attempts
                                </div>
                            </div>
                        </div>
                        <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="noOfAttempts"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                No Of Attempts
                            </label>
                            <div className="col-auto">
                                <input
                                    type="text"
                                    name="noOfAttempts"
                                    className="form-control mx-sm-5"
                                    defaultValue={noOfAttempts}
                                    onChange={(value) => changeNoOfAttemptHandler(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="noOfAttemptserr">
                                    Please Enter No Of Attempts
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="container">
                    <div className="row">
                        <div className="col-6 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="lastPerformanceDate"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Last Performance Date
                            </label>
                            <div className="col-auto">
                                <input
                                    type="datetime-local"
                                    name="lastPerformanceDate"
                                    className="form-control mx-sm-5"
                                    defaultValue={lastPerformanceDate}
                                    onChange={(value) =>
                                        changeLastPerformanceDateHandler(value)
                                    }
                                />
                                <div className="invalid-feedback mx-sm-5" id="lastPerformanceDateerr">
                                    Please Enter Last Performance Date
                                </div>
                            </div>
                        </div>
                        <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="timeoutDays"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Timeout Days
                            </label>
                            <div className="col-auto">
                                <input
                                    type="text"
                                    name="timeoutDays"
                                    className="form-control mx-sm-5"
                                    defaultValue={timeoutDays}
                                    onChange={(value) => changeTimeOutDays(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="timeoutDayserr">
                                    Please Enter Timeout Days
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="container">
                    <div className="row">
                        <div className="col-6 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="allowedAttempts"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Password Change
                            </label>
                            <div className="col-auto">
                                <Combobox
                                    name="passwordChangesAuto"
                                    data={option}
                                    dataKey="id"
                                    textField="display"
                                    defaultValue={
                                        passwordChangesAuto ? passwordChangesAuto : "Y"
                                    }
                                    onChange={(value) =>
                                        changepasswordChangesAutoHandler(value)
                                    }
                                />
                                <div className="invalid-feedback mx-sm-5" id="passwordChangesAutoerr">
                                    Please Enter Allowed Attempts
                                </div>
                            </div>
                        </div>
                        <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="canSplitExams"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Can Split Exam
                            </label>
                            <div className="col-auto">
                                <Combobox
                                    name="canSplitExams"
                                    data={option}
                                    dataKey="id"
                                    textField="display"
                                    defaultValue={canSplitExams ? canSplitExams : "Y"}
                                    onChange={(value) => changecanSplitExamsHandler(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="canSplitExamserr">
                                    Please Choose Can Split Exams
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="container">
                    <div className="row">
                        <div className="col-6 col-sm-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="canSeeDetailedResults"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Get Detailed Results
                            </label>
                            <div className="col-auto">
                                <Combobox
                                    name="canSeeDetailedResults"
                                    data={option}
                                    dataKey="id"
                                    textField="display"
                                    defaultValue={
                                        canSeeDetailedResults ? canSeeDetailedResults : "Y"
                                    }
                                    onChange={(value) =>
                                        changecanSeeDetailedResultsHandler(value)
                                    }
                                />
                                <div className="invalid-feedback mx-sm-5" id="canSeeDetailedResultserr">
                                    Please Choose Can See Detailed Results
                                </div>
                            </div>
                        </div>
                        <div className="col-6 row mt-3 d-flex align-items-center justify-content-center">
                            <label
                                htmlFor="maxSplitAttempts"
                                className="col-sm-2 mt-2"
                                style={{ fontWeight: "bolder" }}
                            >
                                Max Split Attempts
                            </label>
                            <div className="col-auto">
                                <input
                                    type="text"
                                    name="maxSplitAttempts"
                                    className="form-control mx-sm-5"
                                    defaultValue={maxSplitAttempts}
                                    onChange={(value) => changeMaxSplitAttempt(value)}
                                />
                                <div className="invalid-feedback mx-sm-5" id="maxSplitAttemptserr">
                                    Please Enter Max Split Attempts
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className=" row display-flex justify-content-center">
                    <input
                        type="submit"
                        name="submit"
                        value="ADD"
                        className="border-none mt-4 mb-2 text-white col-1"
                        style={{
                            fontWeight: "bolder",
                            background:
                                "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                            padding: "9px",
                        }}
                    />
                </div>
                <Link
                    to={`/AdminDashboard/StudentList`}
                    style={{
                        textDecoration: "none",
                        fontWeight: "bolder",
                        background:
                            "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                        padding: "9px",
                        border: "2px solid gray",
                        width: "0.9in",
                    }}
                    className="ms-3 mt-4 mb-2 text-white d-flex justify-content-center col-6"
                >
                    Back
                </Link>
            </form>
        </div>
    );
}