import React from 'react';
import Login from './Component/Auth/Login';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Dashboard from './Component/pages/Dashboard'; // ✅ lowercase filename to match actual file
import Home from './Component/pages/Home'; // ✅ lowercase filename to match actual file
import ForgotPassword from './Component/Auth/forgot-password'; // ✅ lowercase filename to match actual file
import VerifyOtp from './Component/Auth/VerifyOtp'; // ✅ lowercase filename to match actual file
import NewPassword from './Component/Auth/NewPassword'; // ✅ lowercase filename to match actual file
import SearchPage from './Component/pages/SearchPage'; // ✅ Ensure this import matches the actual file path
import AddPostPage from './Component/pages/AddPostPage'; // ✅ Ensure this import matches the actual file path
import ProfilePage from './Component/pages/ProfilePage'; // ✅ Ensure this import matches the actual file path
import SignUpForm from './Component/Auth/SignUpForm'; // ✅ Ensure this import matches the actual file path
import EditProfile from './Component/Profile/EditProfile'; // ✅ Ensure this import matches the actual file path
import SearchProfile from './Component/Profile/SearchProfile'; // ✅ Ensure this import matches the actual file path


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/Dashboard" element={<Dashboard />} /> {/* ✅ lowercase route */}
        <Route path="/Home" element={<Home />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/VerifyOtp" element={<VerifyOtp/>} />
        <Route path="/NewPassword" element={<NewPassword/>} />
        <Route path="/search" element={<SearchPage />} /> {/* Ensure SearchPage is imported correctly */}
        <Route path="/add-post" element={<AddPostPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/signup" element={<SignUpForm />} />
        <Route path="/edit-profile" element={<EditProfile />} /> {/* Ensure EditProfile is imported correctly */}
        <Route path="/search-profile" element={<SearchProfile />} />


      </Routes>
    </Router>
  );
}

export default App;

