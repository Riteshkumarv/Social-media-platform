import React, { useEffect, useState } from "react";
import {
  FaBell,
  FaCommentDots,
  FaHome,
  FaSearch,
  FaPlusSquare,
  FaUserCircle,
  FaHeart,
  FaShare,
  FaEllipsisH
} from "react-icons/fa";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../api/api";

export default function ProfilePage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState(null);
  const [posts, setPosts] = useState([]);
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const email = location.state?.email;

  const menuStyle = (isActive) => ({
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "10px 0",
    cursor: "pointer",
    fontWeight: isActive ? "bold" : "normal",
    color: isActive ? "#007bff" : "#000",
    backgroundColor: isActive ? "rgba(0, 123, 255, 0.1)" : "transparent",
    borderRadius: "8px",
    paddingLeft: "10px"
  });

  // 1️⃣ Fetch userId from email
  useEffect(() => {
    if (!email) return;
    api
      .get(`/getId-by-email?email=${email}`)
      .then((res) => setUserId(res.data))
      .catch((err) => setError("Error fetching user ID"));
  }, [email]);

  // 2️⃣ Fetch user data
  useEffect(() => {
    if (!email) {
      navigate("/");
      return;
    }

    setLoading(true);
    api
      .get(`/user/get?email=${encodeURIComponent(email)}`)
      .then((res) => setUser(res.data))
      .catch(() => setError("Error fetching user"))
      .finally(() => setLoading(false));
  }, [email, navigate]);

  // 3️⃣ Fetch posts when userId is ready
  useEffect(() => {
    if (!userId) return;

    api
      .get(`/user/post/${userId}`)
      .then((res) => setPosts(res.data))
      .catch(() => setError("Error fetching posts"));
  }, [userId]);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    navigate("/");
  };

  if (loading) return <p style={{ padding: "20px" }}>Loading profile...</p>;
  if (error) return <p style={{ padding: "20px", color: "red" }}>{error}</p>;

  return (
    <div style={{ fontFamily: "Arial, sans-serif" }}>
      {/* Navbar */}
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
          zIndex: 1000
        }}
      >
        <h2 style={{ margin: 0, color: "#000" }}>Social-media-platform</h2>
        <div style={{ display: "flex", gap: "15px", fontSize: "1.5rem", cursor: "pointer" }}>
          <FaBell />
          <FaCommentDots />
        </div>
      </div>

      <div style={{ display: "flex" }}>
        {/* Sidebar */}
        <div
          style={{
            width: "200px",
            padding: "20px",
            borderRight: "1px solid #ddd",
            height: "calc(100vh - 50px)",
            position: "sticky",
            top: "50px"
          }}
        >
          <div style={menuStyle(false)} onClick={() => navigate("/home", { state: { email } })}>
            <FaHome /> Home
          </div>
          <div style={menuStyle(false)} onClick={() => navigate("/search", { state: { email } })}>
            <FaSearch /> Search
          </div>
          <div style={menuStyle(false)} onClick={() => navigate("/add-post", { state: { email } })}>
            <FaPlusSquare /> Add Post
          </div>
          <div style={menuStyle(true)}>
            <FaUserCircle /> Profile
          </div>
        </div>

        {/* Profile Content */}
        <div style={{ flex: 1, padding: "20px" }}>
          {user && (
            <>
              {/* Profile Info */}
              <div style={{ display: "flex", alignItems: "center", gap: "30px" }}>
                <img
                  src={user.profilePictureUrl || "https://via.placeholder.com/150"}
                  alt="Profile"
                  style={{
                    borderRadius: "50%",
                    width: "120px",
                    height: "120px",
                    objectFit: "cover"
                  }}
                  onError={(e) => (e.target.src = "https://via.placeholder.com/150")}
                />
                <div>
                  <p><strong>Name:</strong> {user.name}</p>
                  <p><strong>Age:</strong> {user.age}</p>
                  <p><strong>Followers:</strong> {user.followersCount || 0}</p>
                  <p><strong>Following:</strong> {user.followingCount || 0}</p>
                </div>
              </div>

              {/* Buttons */}
              <div style={{ display: "flex", gap: "10px", marginTop: "15px" }}>
                <button
                  style={{
                    padding: "8px 15px",
                    backgroundColor: "#007bff",
                    color: "#fff",
                    border: "none",
                    borderRadius: "5px",
                    cursor: "pointer"
                  }}
                  onClick={() => navigate("/edit-profile", { state: { email } })}
                >
                  Edit Profile
                </button>
                <button
                  style={{
                    padding: "8px 15px",
                    backgroundColor: "#dc3545",
                    color: "#fff",
                    border: "none",
                    borderRadius: "5px",
                    cursor: "pointer"
                  }}
                  onClick={handleLogout}
                >
                  Logout
                </button>
              </div>

              {/* Posts */}
              <h3 style={{ marginTop: "30px" }}>My Posts</h3>
              {posts.length > 0 ? (
                <div
                  style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                    gap: "20px"
                  }}
                >
                  {posts.map((post) => (
                    <div
                      key={post.id}
                      style={{
                        border: "1px solid #ddd",
                        borderRadius: "8px",
                        overflow: "hidden",
                        backgroundColor: "#fff",
                        boxShadow: "0 2px 5px rgba(0,0,0,0.1)"
                      }}
                    >
                      {post.imageUrl && (
                        <img
                          src={post.imageUrl}
                          alt="Post"
                          style={{
                            width: "100%",
                            height: "92%",
                            objectFit: "cover"
                          }}
                          onError={(e) => (e.target.src = "https://via.placeholder.com/300")}
                        />
                      )}
                      {post.content && (
                        <p style={{ padding: "10px", margin: 0 }}>{post.content}</p>
                      )}
                      {post.createdAt && (
                        <p style={{ padding: "0 10px", fontSize: "0.85rem", color: "#888" }}>
                          Posted on {new Date(post.createdAt).toLocaleDateString()}
                        </p>
                      )}
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                          padding: "10px 15px",
                          borderTop: "1px solid #eee",
                          fontSize: "1.2rem",
                          color: "#555"
                        }}
                      >
                        <div style={{ display: "flex", gap: "15px" }}>
                          <FaHeart style={{ cursor: "pointer" }} />
                          <FaCommentDots style={{ cursor: "pointer" }} />
                          <FaShare style={{ cursor: "pointer" }} />
                        </div>
                        <FaEllipsisH style={{ cursor: "pointer" }} />
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <p>No posts yet.</p>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}
