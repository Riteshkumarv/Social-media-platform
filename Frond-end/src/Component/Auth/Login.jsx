import React, { useState } from "react";
import apiFetch from "../../apiClient";
import { useNavigate } from "react-router-dom";

export default function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setLoading(true);

    try {

      const response = await apiFetch.post("/auth/login", { email, password });
      

      if (response.data.accessToken) {
        localStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("refreshToken", response.data.refreshToken);
        setSuccess("Login successful!");
        navigate("/Home", { state: { email } });
      } else {
        setError("Unexpected response from server.");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Login failed. Check credentials.");
      console.log("api geting error")
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        fontFamily: "Arial, sans-serif",
        background: "#f4f6f8",
        height: "100vh",
        display: "flex",
        flexDirection: "column"
      }}
    >
      {/* ✅ Top Bar with Sign Up */}
      <div
        style={{
          background: "#fff",
          padding: "10px 20px",
          display: "flex",
          justifyContent: "flex-end",
          borderBottom: "1px solid #ddd"
        }}
      >
        <button
          onClick={() => navigate("/signup")}
          style={{
            background: "#28a745",
            color: "white",
            border: "none",
            borderRadius: "5px",
            padding: "8px 16px",
            cursor: "pointer",
            fontWeight: "bold"
          }}
        >
          Sign Up
        </button>
      </div>

      {/* Centered Login Form */}
      <div
        style={{
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "center"
        }}
      >
        <form
          onSubmit={handleSubmit}
          style={{
            background: "#fff",
            padding: "30px",
            borderRadius: "10px",
            boxShadow: "0px 4px 15px rgba(0,0,0,0.1)",
            width: "320px"
          }}
        >
          <h2
            style={{ textAlign: "center", marginBottom: "20px", color: "#333" }}
          >
            LOGIN
          </h2>

          {error && (
            <div
              style={{
                background: "#ffe5e5",
                color: "#ff0000",
                padding: "8px",
                marginBottom: "10px",
                borderRadius: "5px",
                textAlign: "center"
              }}
            >
              {error}
            </div>
          )}

          {success && (
            <div
              style={{
                background: "#e5ffea",
                color: "#008000",
                padding: "8px",
                marginBottom: "10px",
                borderRadius: "5px",
                textAlign: "center"
              }}
            >
              {success}
            </div>
          )}

          <div style={{ marginBottom: "15px" }}>
            <label style={{ display: "block", marginBottom: "5px" }}>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "8px",
                borderRadius: "5px",
                border: "1px solid #ccc",
                outline: "none"
              }}
            />
          </div>

          <div style={{ marginBottom: "15px" }}>
            <label style={{ display: "block", marginBottom: "5px" }}>
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "8px",
                borderRadius: "5px",
                border: "1px solid #ccc",
                outline: "none"
              }}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            style={{
              width: "100%",
              padding: "10px",
              borderRadius: "5px",
              backgroundColor: "#007bff",
              color: "white",
              fontWeight: "bold",
              cursor: "pointer",
              border: "none",
              marginBottom: "10px"
            }}
          >
            {loading ? "Logging in..." : "Login"}
          </button>

          <button
            type="button"
            onClick={() => navigate("/forgot-password")}
            style={{
              width: "100%",
              padding: "10px",
              borderRadius: "5px",
              backgroundColor: "#6c757d",
              color: "white",
              cursor: "pointer",
              border: "none"
            }}
          >
            Forgot Password?
          </button>
        </form>
      </div>
    </div>
  );
}
