import { Route, Routes } from "react-router-dom";
import { AuthProvider } from "./lib/auth";
import { Layout } from "./components/Layout";
import { ProtectedRoute } from "./components/ProtectedRoute";
import Landing from "./components/Landing";
import JoinQueue from "./components/JoinQueue";
import CheckStatus from "./components/CheckStatus";
import Login from "./components/Login";
import StaffDashboard from "./components/StaffDashboard";
import AdminDashboard from "./components/AdminDashboard";

function App() {
  return (
    <AuthProvider>
      <Layout>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/join" element={<JoinQueue />} />
          <Route path="/status" element={<CheckStatus />} />
          <Route path="/login" element={<Login />} />

          <Route
            path="/staff"
            element={
              <ProtectedRoute allow={["STAFF", "ADMIN"]}>
                <StaffDashboard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin"
            element={
              <ProtectedRoute allow={["ADMIN"]}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Layout>
    </AuthProvider>
  );
}

export default App;
