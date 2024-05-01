import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

function StudentList() {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    async function getAllStudent() {
      try {
        const response = await fetch(
          "https://"+window.location.hostname+":8443/OnlineExamPortal/control/fetch-student-details",
          {
            method: "POST",
            credentials: "include",
          }
        );
        if (!response.ok) {
          throw new Error();
        }
        const data = await response.json();
        console.log("studentlist",data);
        var list = data.StudentListInfo.StudentList;
        setStudents(list);
      } catch (error) {
        console.log(error);

      }
    }
    getAllStudent();
  }, []);
if (students === undefined || students.length === 0)
return (
  <div>
  <h1 className="text-center">No Student datas</h1>
  </div>
)
  return (
    <>
      <div>
        <h2 align="center">Student List</h2>
      </div>

      <div className="container-fluid">
        <table className="table table-striped table-hover table-light">
          <thead className="thead-light">
            <tr>
              <th scope="col">Party ID</th>
              <th scope="col">User Name</th>
              <th scope="col">User Email</th>
              <th scope="col">Options</th>
            </tr>
          </thead>
          <tbody>
            {students?students.map((data) => {
              return (
                <tr key={data.partyId}>
                  <td id="party">{data.partyId}</td>
                  <td>{data.userName}</td>
                  <td>{data.userLoginId}</td>
                  <td>
                    <button  variant="link" style={{
                      fontWeight: "bolder",
                      background:
                        "radial-gradient(circle at 48.7% 44.3%, rgb(30, 144, 231) 0%, rgb(56, 113, 209) 22.9%, rgb(38, 76, 140) 76.7%, rgb(31, 63, 116) 100.2%)",
                    }}
                      className="border-none px-3 py-1 mt-4 mb-2 text-white">
                      <Link
                        to="/AdminDashboard/StudentList/AddUser" style={{color:"white", textDecoration:"none"}}
                      >
                        Assign Test
                      </Link></button>
                  </td>
                </tr>
              );
            }):<h3>No Students available</h3>}
          </tbody>
        </table>
      </div >
    </>
  );
}

export default StudentList;
