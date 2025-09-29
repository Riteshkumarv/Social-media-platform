import React, { useEffect, useState } from "react";
import apiFetch from "../../apiClient";

export default function Dashboard() {
  const [pictures, setPictures] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPictures = async () => {
      try {
        const { data } = await apiFetch.get("/api/pictures");
        setPictures(data);
      } catch (err) {
        console.error(err);
        setError("Failed to load pictures. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchPictures();
  }, []);

  if (loading) return <p style={{ textAlign: "center" }}>Loading pictures...</p>;
  if (error) return <p style={{ color: "red", textAlign: "center" }}>{error}</p>;

  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
        gap: "15px",
        padding: "20px"
      }}
    >
      {pictures.map((url, i) => (
        <img
          key={i}
          src={url}
          alt={`user upload ${i + 1}`}
          style={{ width: "100%", borderRadius: "8px", objectFit: "cover" }}
        />
      ))}
    </div>
  );
}
