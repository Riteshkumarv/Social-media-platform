import React, { useState } from "react";
import { FaBell, FaCommentDots, FaHome, FaSearch, FaPlusSquare, FaUserCircle } from "react-icons/fa";
import { useNavigate, useLocation } from "react-router-dom";
import apiFetch from "../../apiClient";

export default function SearchPage() {
  const [searchQuery, setSearchQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  const menuStyle = {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "10px 0",
    cursor: "pointer",
    color: "#000",
  };

  const activeMenuStyle = {
    ...menuStyle,
    fontWeight: "bold",
    color: "#007bff",
    backgroundColor: "rgba(0, 123, 255, 0.1)",
    borderRadius: "8px",
    paddingLeft: "10px",
  };

  const handleSearch = async (e) => {
    const query = e.target.value;
    setSearchQuery(query);

    if (!query.trim()) {
      setResults([]);
      return;
    }

    try {
      setLoading(true);
      const token = localStorage.getItem("accessToken");
      const { data } = await apiFetch.get(`/api/users/search?name=${encodeURIComponent(query)}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setResults(data);
    } catch (err) {
      console.error("Search error:", err);
    } finally {
      setLoading(false);
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
          style={{ margin: 0, color: "#000", cursor: "pointer" }}
          onClick={() => navigate("/Home", { state: { email } })}
        >
          Social-media-platform
        </h2>
        <div style={{ display: "flex", gap: "15px", fontSize: "1.5rem", cursor: "pointer" }}>
          <FaBell />
          <FaCommentDots />
        </div>
      </div>

      {/* Content */}
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
          <div style={menuStyle} onClick={() => navigate("/Home", { state: { email } })}>
            <FaHome /> Home
          </div>
          <div style={activeMenuStyle}>
            <FaSearch /> Search
          </div>
          <div style={menuStyle} onClick={() => navigate("/add-post", { state: { email } })}>
            <FaPlusSquare /> Add Post
          </div>
          <div style={menuStyle} onClick={() => navigate("/profile", { state: { email } })}>
            <FaUserCircle /> Profile
          </div>
        </div>

        {/* Search Area */}
        <div style={{ flex: 1, padding: "20px" }}>
          <h3>Search</h3>
          <input
            type="text"
            value={searchQuery}
            onChange={handleSearch}
            placeholder="Search for people..."
            style={{
              width: "100%",
              padding: "10px",
              border: "1px solid #ccc",
              borderRadius: "8px",
              outline: "none",
            }}
          />

          {/* Search Results */}
          <div style={{ marginTop: "20px" }}>
            {loading && <p>Searching...</p>}
            {!loading && searchQuery && results.length === 0 && <p>No users found.</p>}
            {results.map((user) => (
              <div
                key={user.id}
                style={{
                  display: "flex",
                  alignItems: "center",
                  padding: "10px",
                  borderBottom: "1px solid #eee",
                  cursor: "pointer",
                }}
                onClick={() => navigate("/search-profile", { state: { email: user.email } })}
              >
                <img
                  src={user.profilePictureUrl || "https://via.placeholder.com/40"}
                  alt={user.name}
                  style={{ width: "40px", height: "40px", borderRadius: "50%", marginRight: "10px" }}
                />
                <div>
                  <strong>{user.name}</strong>
                  <p style={{ margin: 0, fontSize: "0.9rem", color: "#555" }}>{user.email}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
