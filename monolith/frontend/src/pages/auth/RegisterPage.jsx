import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../../services/authService";
import "./RegisterPage.css";

function RegisterPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    setErrorMessage("");
    setSuccessMessage("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage("");
    setSuccessMessage("");

    try {
      setLoading(true);
      await registerUser(formData);

      setSuccessMessage("Cont creat cu succes.");

      setTimeout(() => {
        navigate("/login");
      }, 1200);
    } catch (error) {
      console.error("Eroare la înregistrare:", error);
      setErrorMessage(
        error?.response?.data?.message || "A apărut o eroare la crearea contului."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      <div className="register-card">
        <div className="register-title-box">
          <h1 className="register-title">Register</h1>
        </div>

        {errorMessage && (
          <div className="register-alert register-alert--error">
            {errorMessage}
          </div>
        )}

        {successMessage && (
          <div className="register-alert register-alert--success">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="register-form">
          <div className="register-form-group">
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

          <div className="register-form-group">
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

          <button type="submit" className="register-btn" disabled={loading}>
            {loading ? "Se creează..." : "Create account"}
          </button>
        </form>

        <p className="register-footer">
          Ai deja cont? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}

export default RegisterPage;