import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import apiFetch from "../../apiClient";
import axios from "axios";

export default function SignUpForm() {
  const [formData, setFormData] = useState({
    name: "",
    age: "",
    email: "",
    password: "",
    profilePictureUrl: ""
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [uploading, setUploading] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // File upload to Cloudinary
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);
    setError("");

    try {
      const data = new FormData();
      data.append("file", file);
      data.append("upload_preset", "ml_default"); // Make sure this exists in Cloudinary
      data.append("cloud_name", "dpdg2zia4");

      const res = await axios.post(
        "https://api.cloudinary.com/v1_1/dpdg2zia4/image/upload",
        data,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      setFormData({ ...formData, profilePictureUrl: res.data.secure_url });
    } catch (err) {
      console.error("Cloudinary upload error:", err.response || err);
      setError("Image upload failed. Check your preset or network.");
    } finally {
      setUploading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setLoading(true);

    try {
      await apiFetch.post("/auth/register", {
        name: formData.name,
        age: parseInt(formData.age, 10),
        email: formData.email,
        password: formData.password,
        profilePictureUrl: formData.profilePictureUrl
      });

      setSuccess("Account created successfully!");
      setTimeout(() => navigate("/"), 1500);
    } catch (err) {
      console.error("Signup error:", err.response || err);
      setError(err.response?.data?.message || "Signup failed.");
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
          width: "350px"
        }}
      >
        <h2 style={{ textAlign: "center", marginBottom: "20px", color: "#333" }}>
          Sign Up
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
          <label>Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              borderRadius: "5px",
              border: "1px solid #ccc"
            }}
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Age</label>
          <input
            type="number"
            name="age"
            value={formData.age}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              borderRadius: "5px",
              border: "1px solid #ccc"
            }}
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              borderRadius: "5px",
              border: "1px solid #ccc"
            }}
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              borderRadius: "5px",
              border: "1px solid #ccc"
            }}
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Profile Picture</label>
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            required
            style={{
              width: "100%",
              padding: "5px",
              borderRadius: "5px",
              border: "1px solid #ccc"
            }}
          />
          {uploading && <p style={{ fontSize: "12px", color: "#555" }}>Uploading...</p>}
          {formData.profilePictureUrl && (
            <img
              src={formData.profilePictureUrl}
              alt="Preview"
              style={{ width: "100px", marginTop: "10px", borderRadius: "5px" }}
            />
          )}
        </div>

        <button
          type="submit"
          disabled={loading || uploading}
          style={{
            width: "100%",
            padding: "10px",
            borderRadius: "5px",
            backgroundColor: "#28a745",
            color: "white",
            fontWeight: "bold",
            cursor: "pointer",
            border: "none"
          }}
        >
          {loading ? "Signing up..." : "Sign Up"}
        </button>

        <p style={{ marginTop: "15px", textAlign: "center" }}>
          Already have an account?{" "}
          <span
            onClick={() => navigate("/")}
            style={{ color: "#007bff", cursor: "pointer" }}
          >
            Login
          </span>
        </p>
      </form>
    </div>
  );
}
