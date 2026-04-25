import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../../services/authService";

function LoginPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    rememberMe: false,
  });

  const [errorMessage, setErrorMessage] = useState("");

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    try {
      await loginUser(formData);
      navigate("/");
    } catch (error) {
      console.error("Eroare la autentificare:", error);
      setErrorMessage(
        error?.response?.data?.message || "Username sau parolă incorectă."
      );
    }
  };

  return (
    <div style={{
      minHeight: "100vh",
      background: "#eef3f9",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      padding: "24px",
    }}>
      <div style={{
        width: "100%",
        maxWidth: "460px",
        background: "#ffffff",
        borderRadius: "24px",
        padding: "40px 36px",
        boxShadow: "0 20px 50px rgba(15, 23, 42, 0.08)",
      }}>
        <p style={{ margin: "0 0 8px 0", color: "#64748b", fontSize: "15px" }}>
          Autentificare
        </p>

        <h1 style={{
          margin: "0 0 28px 0",
          fontSize: "48px",
          lineHeight: 1.05,
          color: "#0f172a",
        }}>
          Login
        </h1>

        {errorMessage && (
          <div style={{
            marginBottom: "16px",
            padding: "12px 14px",
            borderRadius: "12px",
            background: "#fee2e2",
            color: "#b91c1c",
            fontSize: "14px",
          }}>
            {errorMessage}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: "18px" }}>
            <label style={{
              display: "block",
              marginBottom: "8px",
              color: "#0f172a",
              fontWeight: 600,
            }}>
              Username
            </label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              style={{
                width: "100%",
                padding: "14px 16px",
                borderRadius: "14px",
                border: "1px solid #cbd5e1",
                fontSize: "16px",
                boxSizing: "border-box",
              }}
            />
          </div>

          <div style={{ marginBottom: "18px" }}>
            <label style={{
              display: "block",
              marginBottom: "8px",
              color: "#0f172a",
              fontWeight: 600,
            }}>
              Password
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              style={{
                width: "100%",
                padding: "14px 16px",
                borderRadius: "14px",
                border: "1px solid #cbd5e1",
                fontSize: "16px",
                boxSizing: "border-box",
              }}
            />
          </div>

          <div style={{ marginBottom: "20px" }}>
            <label style={{ color: "#334155", fontSize: "14px" }}>
              <input
                type="checkbox"
                name="rememberMe"
                checked={formData.rememberMe}
                onChange={handleChange}
                style={{ marginRight: "8px" }}
              />
              Remember me
            </label>
          </div>

          <button
            type="submit"
            style={{
              width: "100%",
              padding: "14px",
              borderRadius: "14px",
              border: "none",
              background: "#2563eb",
              color: "#ffffff",
              fontSize: "18px",
              fontWeight: 700,
              cursor: "pointer",
            }}
          >
            Sign in
          </button>
        </form>

        <p style={{ marginTop: "22px", color: "#475569", fontSize: "15px" }}>
          Nu ai cont?{" "}
          <Link to="/register" style={{ color: "#2563eb", fontWeight: 600 }}>
            Register
          </Link>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;