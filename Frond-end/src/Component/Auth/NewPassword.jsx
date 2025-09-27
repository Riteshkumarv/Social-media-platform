import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import apiFetch from "../../apiClient";

export default function NewPassword() {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email || "";

  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSetPassword = async () => {
    if (!password.trim()) {
      setMessage("Please enter a password.");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      const { data } = await apiFetch.post(
        `/enter-new-password?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
      );
      setMessage(data);

      navigate("/"); // ✅ Go to login after success
    } catch (err) {
      console.error("Error setting password:", err);
      setMessage("Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ textAlign: "center", marginTop: "50px" }}>
      <h2>Set New Password for {email}</h2>
      <input
        type="password"
        placeholder="Enter New Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        style={{
          padding: "8px",
          marginBottom: "10px",
          display: "block",
          width: "250px",
          margin: "0 auto",
        }}
      />
      <button
        onClick={handleSetPassword}
        disabled={loading}
        style={{
          padding: "8px 20px",
          cursor: loading ? "not-allowed" : "pointer",
          backgroundColor: loading ? "#6c757d" : "#007bff",
          color: "white",
          border: "none",
          borderRadius: "5px",
        }}
      >
        {loading ? "Saving..." : "Save Password"}
      </button>
      {message && <p style={{ marginTop: "10px" }}>{message}</p>}
    </div>
  );
}
