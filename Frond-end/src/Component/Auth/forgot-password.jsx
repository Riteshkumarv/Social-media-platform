import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import apiFetch from "../../apiClient";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSendOtp = async () => {
    if (!email.trim()) {
      setMessage("Please enter your email.");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      const { data } = await apiFetch.get(
        `/forgot-password?email=${encodeURIComponent(email)}`
      );
      setMessage(data);
      navigate("/VerifyOtp", { state: { email } });
    } catch (err) {
      console.error(err);
      setMessage("Error sending OTP. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <div
        style={{
          background: "#fff",
          padding: "20px",
          borderRadius: "8px",
          boxShadow: "0px 4px 10px rgba(0,0,0,0.1)",
        }}
      >
        <h2>Forgot Password</h2>
        <input
          type="email"
          placeholder="Enter your registered email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={{
            width: "100%",
            padding: "8px",
            marginBottom: "10px",
            border: "1px solid #ccc",
            borderRadius: "5px",
          }}
        />
        <button
          onClick={handleSendOtp}
          disabled={loading}
          style={{
            width: "100%",
            padding: "10px",
            backgroundColor: loading ? "#6c757d" : "#007bff",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: loading ? "not-allowed" : "pointer",
          }}
        >
          {loading ? "Sending..." : "Send OTP"}
        </button>
        {message && <p style={{ marginTop: "10px" }}>{message}</p>}
      </div>
    </div>
  );
}
