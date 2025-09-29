import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../api/api"; // Your Axios instance

export default function EditProfile() {
  const navigate = useNavigate();
  const location = useLocation();
  const email = location.state?.email;

  const [formData, setFormData] = useState({
    name: "",
    age: "",
    profilePictureUrl: ""
  });
  const [loading, setLoading] = useState(true);
  const [uploadingImage, setUploadingImage] = useState(false);

  // ✅ Fetch existing user data
  useEffect(() => {
    if (!email) {
      navigate("/");
      return;
    }

    api.get(`/user/get?email=${encodeURIComponent(email)}`)
      .then((res) => {
        setFormData({
          name: res.data.name || "",
          age: res.data.age || "",
          profilePictureUrl: res.data.profilePictureUrl || ""
        });
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching user:", err);
        setLoading(false);
      });
  }, [email, navigate]);

  // ✅ Handle form changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // ✅ Upload to Cloudinary
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploadingImage(true);
    const data = new FormData();
    data.append("file", file);
    data.append("upload_preset", "ml_default"); // From Cloudinary settings

    try {
      const res = await fetch(`https://api.cloudinary.com/v1_1/dpdg2zia4/image/upload`, {
        method: "POST",
        body: data
      });
      const result = await res.json();
      setFormData((prev) => ({ ...prev, profilePictureUrl: result.secure_url }));
    } catch (err) {
      console.error("Cloudinary upload failed:", err);
      alert("Failed to upload image");
    } finally {
      setUploadingImage(false);
    }
  };

  // ✅ Submit profile update
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/user/update?email=${encodeURIComponent(email)}`, formData);
      alert("Profile updated successfully!");
      navigate("/profile", { state: { email } });
    } catch (err) {
      console.error("Error updating profile:", err);
      alert("Failed to update profile");
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div style={{ maxWidth: "500px", margin: "50px auto", fontFamily: "Arial" }}>
      <h2>Edit Profile</h2>
      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
        
        <label>Name:</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
          style={{ padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }}
        />

        <label>Age:</label>
        <input
          type="number"
          name="age"
          value={formData.age}
          onChange={handleChange}
          required
          style={{ padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }}
        />

        <label>Profile Picture:</label>
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          style={{ padding: "5px" }}
        />

        {uploadingImage && <p>Uploading image...</p>}

        {formData.profilePictureUrl && (
          <img
            src={formData.profilePictureUrl}
            alt="Preview"
            style={{ width: "120px", height: "120px", borderRadius: "50%", marginTop: "10px" }}
          />
        )}

        <button
          type="submit"
          disabled={uploadingImage}
          style={{
            padding: "10px",
            backgroundColor: uploadingImage ? "#ccc" : "#007bff",
            color: "#fff",
            border: "none",
            borderRadius: "5px",
            cursor: uploadingImage ? "not-allowed" : "pointer"
          }}
        >
          Save Changes
        </button>
      </form>
    </div>
  );
}
