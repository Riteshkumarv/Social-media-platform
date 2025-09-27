import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import apiFetch from "../../apiClient";

export default function VerifyOtp() {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email || "";

  const [otp, setOtp] = useState("");
  const [message, setMessage] = useState("");
  const [resetDisabled, setResetDisabled] = useState(false);
  const [timer, setTimer] = useState(0);
  const [loading, setLoading] = useState(false);

  const handleVerifyOtp = async () => {
    if (!otp.trim()) {
      setMessage("Please enter the OTP.");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      const { data, status } = await apiFetch.post(
        `/enter-OTP?email=${encodeURIComponent(email)}&otp=${encodeURIComponent(otp)}`
      );

      setMessage(data);

      if (status === 200) {
        navigate("/newpassword", { state: { email } });
      } else {
        setMessage("Invalid OTP. Please try again.");
      }
    } catch (err) {
      console.error("Error verifying OTP:", err);
      setMessage("Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  const handleResetOtp = async () => {
    setMessage("");
    setResetDisabled(true);
    setTimer(60);

    try {
      const { data } = await apiFetch.get(
        `/forgot-password?email=${encodeURIComponent(email)}`
      );
      setMessage(data);
    } catch (err) {
      console.error("Error resetting OTP:", err);
      setMessage("Something went wrong while resetting OTP");
      setResetDisabled(false);
      setTimer(0);
    }
  };

  // Countdown effect
  useEffect(() => {
    if (timer > 0) {
      const interval = setInterval(() => setTimer((prev) => prev - 1), 1000);
      return () => clearInterval(interval);
    } else {
      setResetDisabled(false);
    }
  }, [timer]);

  return (
    <div style={{ textAlign: "center", marginTop: "50px" }}>
      <h2>Verify OTP for {email}</h2>

      <input
        type="text"
        placeholder="Enter OTP"
        value={otp}
        onChange={(e) => setOtp(e.target.value)}
        style={{
          padding: "8px",
          marginBottom: "10px",
          display: "block",
          width: "250px",
          margin: "0 auto",
        }}
      />

      <button
        onClick={handleVerifyOtp}
        disabled={loading}
        style={{
          padding: "8px 20px",
          cursor: loading ? "not-allowed" : "pointer",
          marginRight: "10px",
          backgroundColor: "#007bff",
          color: "white",
          border: "none",
          borderRadius: "5px",
        }}
      >
        {loading ? "Verifying..." : "Verify OTP"}
      </button>

      <button
        onClick={handleResetOtp}
        disabled={resetDisabled}
        style={{
          padding: "8px 20px",
          cursor: resetDisabled ? "not-allowed" : "pointer",
          background: resetDisabled ? "#ccc" : "#f39c12",
          color: "white",
          border: "none",
          borderRadius: "5px",
        }}
      >
        {resetDisabled ? `Reset OTP (${timer}s)` : "Reset OTP"}
      </button>

      {message && <p style={{ marginTop: "10px" }}>{message}</p>}
    </div>
  );
}
