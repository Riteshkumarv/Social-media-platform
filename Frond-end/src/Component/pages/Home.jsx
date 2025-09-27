import React, { useEffect, useState } from "react";
import { FaBell, FaCommentDots, FaHome, FaSearch, FaPlusSquare, FaUserCircle } from "react-icons/fa";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../api/api";

export default function HomePage() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();
  const location = useLocation();
  const email = location.state?.email;

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
    paddingLeft: "10px"
  };
  
  const menuStyle = {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "10px 0",
    cursor: "pointer",
    color: "#000"
  };

  // ✅ Fetch all posts except logged-in user
  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const res = await api.get("/posts/others");
        setPosts(res.data);
      } catch (err) {
        console.error("Error fetching posts:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchPosts();
  }, []);

  return (
    <div style={{ fontFamily: "Arial, sans-serif" }}>
      {/* Top Navbar */}
      <div style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "10px 20px",
        backgroundColor: "#fff",
        borderBottom: "1px solid #ddd",
        position: "sticky",
        top: 0,
        zIndex: 1000
      }}>
        <h2
          style={{
            margin: 0,
            color: "#000000ff",
            transition: "color 0.3s",
            cursor: "pointer"
          }}
          onMouseEnter={(e) => e.target.style.color = "#0056b3"}
          onMouseLeave={(e) => e.target.style.color = "#000000ff"}
        >
          Social-media-platform
        </h2>
        <div style={{ display: "flex", gap: "15px", fontSize: "1.5rem", cursor: "pointer" }}>
          <FaBell />
          <FaCommentDots />
        </div>
      </div>

      {/* Content Area */}
      <div style={{ display: "flex" }}>
        {/* Left Sidebar */}
        <div style={{
          width: "200px",
          padding: "20px",
          borderRight: "1px solid #ddd",
          height: "calc(100vh - 50px)",
          position: "sticky",
          top: "50px"
        }}>
          <div style={activeMenuStyle}>
            <FaHome /> Home
          </div>
          <div style={menuStyle} onClick={() => navigate("/search", { state: { email } })}>
            <FaSearch /> Search
          </div>
          <div style={menuStyle} onClick={() => navigate("/add-post", { state: { email } })}>
            <FaPlusSquare /> Add Post
          </div>
          <div style={menuStyle} onClick={() => navigate("/profile", { state: { email } })}>
            <FaUserCircle /> Profile
          </div>
        </div>

        {/* Main Feed */}
        <div style={{ flex: 1, padding: "20px" }}>
          <h3>All Users' Posts (Except Me)</h3>
          {loading ? (
            <p>Loading posts...</p>
          ) : posts.length === 0 ? (
            <p>No posts found.</p>
          ) : (
            posts.map((post) => (
              <div
                key={post.id}
                style={{
                  border: "1px solid #ddd",
                  padding: "10px",
                  marginBottom: "15px",
                  borderRadius: "8px"
                }}
              >
                {/* User Info */}
                <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                  <img
                    src={post.imageUrl || "https://via.placeholder.com/40"}
                    alt={post.content}
                    style={{ width: "25%", height: "25%" }}
                  />
                  <div>
                    <strong>{post.content}</strong>
                    <p style={{ fontSize: "12px", color: "#666" }}>{post.email}</p>
                  </div>
                </div>

                {/* Post Content */}
                <p style={{ marginTop: "10px" }}>{post.content}</p>
                <small style={{ color: "#888" }}>
                  {new Date(post.time).toLocaleString()}
                </small>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
