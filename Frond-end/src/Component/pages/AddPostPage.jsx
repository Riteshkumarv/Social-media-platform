import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import {
  FaBell,
  FaCommentDots,
  FaHome,
  FaSearch,
  FaPlusSquare,
  FaUserCircle,
} from "react-icons/fa";
import api from "../../api/api"; // axios instance

export default function AddPostPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  const [userId, setUserId] = useState(null);
  const [postContent, setPostContent] = useState("");
  const [postImage, setPostImage] = useState(null);
  const [imageUrl, setImageUrl] = useState("");
  const [uploading, setUploading] = useState(false);

  // Sidebar styles
  const activeMenuStyle = {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "10px 0",
    cursor: "pointer",
    fontWeight: "bold",
    color: "#007bff",
    backgroundColor: "rgba(0, 123, 255, 0.1)",
    borderRadius: "8px",
    paddingLeft: "10px",
  };
  const menuStyle = {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "10px 0",
    cursor: "pointer",
    color: "#000",
  };

  // Fetch user ID
  useEffect(() => {
    if (email) {
      api
        .get(`/getId-by-email?email=${email}`)
        .then((res) => setUserId(res.data))
        .catch((err) => console.error("Error fetching user ID:", err));
    }
  }, [email]);

  // Handle Cloudinary image upload
  const handleImageChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setPostImage(file);

    setUploading(true);
    const formData = new FormData();
    formData.append("file", file);
    formData.append("upload_preset", "ml_default");

    try {
      const res = await fetch(
        "https://api.cloudinary.com/v1_1/dpdg2zia4/image/upload",
        { method: "POST", body: formData }
      );
      const data = await res.json();
      setImageUrl(data.secure_url);
    } catch (err) {
      console.error("Cloudinary Upload Error:", err);
    } finally {
      setUploading(false);
    }
  };

  // Handle post creation
  const handlePost = async () => {
    if (!postContent && !imageUrl) {
      alert("Please write something or upload an image!");
      return;
    }
    if (!userId) {
      alert("User ID not loaded yet.");
      return;
    }
    if (uploading) {
      alert("Image is still uploading, please wait...");
      return;
    }

    try {
      const postRequestDTO = {
        content: postContent,
        imageUrl: imageUrl,
      };

      await api.post(`/user/post/${userId}`, postRequestDTO);
      alert("✅ Post Created!");
      setPostContent("");
      setImageUrl("");
      setPostImage(null);
    } catch (err) {
      console.error("Error creating post:", err);
    }
  };

  return (
    <div style={{ fontFamily: "Arial, sans-serif" }}>
      {/* Top Navbar */}
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          padding: "10px 20px",
          backgroundColor: "#fff",
          borderBottom: "1px solid #ddd",
          position: "sticky",
          top: 0,
          zIndex: 1000,
        }}
      >
        <h2
          style={{
            margin: 0,
            color: "#000000ff",
            cursor: "pointer",
          }}
          onMouseEnter={(e) => (e.target.style.color = "#0056b3")}
          onMouseLeave={(e) => (e.target.style.color = "#000000ff")}
        >
          Social-media-platform
        </h2>
        <div
          style={{
            display: "flex",
            gap: "15px",
            fontSize: "1.5rem",
            cursor: "pointer",
          }}
        >
          <FaBell />
          <FaCommentDots />
        </div>
      </div>

      {/* Content Area */}
      <div style={{ display: "flex" }}>
        {/* Left Sidebar */}
        <div
          style={{
            width: "200px",
            padding: "20px",
            borderRight: "1px solid #ddd",
            height: "calc(100vh - 50px)",
            position: "sticky",
            top: "50px",
          }}
        >
          <div style={menuStyle} onClick={() => navigate("/home", { state: { email } })}>
            <FaHome /> Home
          </div>
          <div
            style={menuStyle}
            onClick={() => navigate("/search", { state: { email } })}
          >
            <FaSearch /> Search
          </div>
          <div style={activeMenuStyle}>
            <FaPlusSquare /> Add Post
          </div>
          <div
            style={menuStyle}
            onClick={() => navigate("/profile", { state: { email } })}
          >
            <FaUserCircle /> Profile
          </div>
        </div>

        {/* Main Add Post Form */}
        <div style={{ flex: 1, padding: "20px" }}>
          <h2>Add New Post</h2>
          <textarea
            placeholder="Write something..."
            value={postContent}
            onChange={(e) => setPostContent(e.target.value)}
            rows="4"
            style={{
              width: "100%",
              padding: "10px",
              marginBottom: "10px",
            }}
          />

          <input
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            style={{ marginBottom: "10px" }}
          />

          {imageUrl && (
            <div style={{ marginBottom: "10px" }}>
              <img
                src={imageUrl}
                alt="Preview"
                style={{
                  maxWidth: "200px",
                  borderRadius: "8px",
                }}
              />
            </div>
          )}

          <button
            onClick={handlePost}
            disabled={uploading || (!imageUrl && !postContent)}
            style={{
              marginTop: "10px",
              padding: "10px 20px",
              backgroundColor: uploading ? "#ccc" : "#007bff",
              color: "#fff",
              border: "none",
              borderRadius: "5px",
              cursor: uploading ? "not-allowed" : "pointer",
            }}
          >
            {uploading ? "Uploading..." : "Post"}
          </button>
        </div>
      </div>
    </div>
  );
}
