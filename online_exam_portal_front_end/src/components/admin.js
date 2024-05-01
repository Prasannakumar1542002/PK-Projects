import AdminDashboard from "../components/AdminComponent/AdminDashboard/AdminDashboard";
// import StudentDashboard from "./components/StudentComponent/StudentDashboard/StudentDashboard";

function admin() {
  sessionStorage.setItem("role","admin");
  return (
    <>
      <AdminDashboard />
      {/* <StudentDashboard/> */}
    </>
  );

}



export default admin;