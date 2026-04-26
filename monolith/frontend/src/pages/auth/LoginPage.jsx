import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../../services/authService";
import "./LoginPage.css";

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
    <div className="login-page">
      <div className="login-card">
        <div className="login-title-box">
          <h1 className="login-title">Login</h1>
        </div>

        {errorMessage && (
          <div className="login-alert login-alert--error">
            {errorMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="login-form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              placeholder="Introdu username-ul"
              value={formData.username}
              onChange={handleChange}
              required
            />
          </div>

          <div className="login-form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              placeholder="Introdu parola"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="login-checkbox">
            <label>
              <input
                type="checkbox"
                name="rememberMe"
                checked={formData.rememberMe}
                onChange={handleChange}
              />
              Remember me
            </label>
          </div>

          <button type="submit" className="login-btn">
            Sign in
          </button>
        </form>

        <p className="login-footer">
          Nu ai cont? <Link to="/register">Register</Link>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;