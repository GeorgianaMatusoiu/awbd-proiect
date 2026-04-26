import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createProspect,
  getProspect,
  updateProspect,
} from "../../services/prospectService";
import "./Prospect.css";

function ProspectForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    afectiuni: "",
    administrare: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEditMode) {
      loadProspect();
    }
  }, [id]);

  const loadProspect = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getProspect(id);
      const prospect = response.data;

      setFormData({
        afectiuni: prospect.afectiuni ?? "",
        administrare: prospect.administrare ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea prospectului:", error);
      setServerError("Prospectul nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.afectiuni.trim()) {
      newErrors.afectiuni = "Afectiunile sunt obligatorii.";
    }

    if (!formData.administrare.trim()) {
      newErrors.administrare = "Administrarea este obligatorie.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    setErrors((prev) => ({
      ...prev,
      [name]: "",
    }));

    setServerError("");
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!validate()) return;

    try {
      setLoading(true);
      setServerError("");

      if (isEditMode) {
        await updateProspect(id, formData);
      } else {
        await createProspect(formData);
      }

      navigate("/prospecte");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea prospectului.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="prospect-page">
      <div className="prospect-page__content">
        <div className="prospect-page__topbar">
          <div>
            <p className="prospect-page__subtitle">Administrare prospecte</p>
            <h1 className="prospect-page__title">
              {isEditMode ? "Editează prospect" : "Adaugă prospect"}
            </h1>
          </div>

          <Link to="/prospecte" className="prospect-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="prospect-page__alert prospect-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="prospect-page__card prospect-page__form-card">
          <form onSubmit={handleSubmit} className="prospect-page__form" noValidate>
            <div className="prospect-page__form-grid">
              <div className="prospect-page__form-group">
                <label>Afectiuni</label>
                <input
                  type="text"
                  name="afectiuni"
                  value={formData.afectiuni}
                  onChange={handleChange}
                />
                {errors.afectiuni && (
                  <div className="prospect-page__field-error">
                    {errors.afectiuni}
                  </div>
                )}
              </div>

              <div className="prospect-page__form-group">
                <label>Administrare</label>
                <input
                  type="text"
                  name="administrare"
                  value={formData.administrare}
                  onChange={handleChange}
                />
                {errors.administrare && (
                  <div className="prospect-page__field-error">
                    {errors.administrare}
                  </div>
                )}
              </div>
            </div>

            <div className="prospect-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="prospect-page__submit-btn"
              >
                {loading
                  ? "Se salvează..."
                  : isEditMode
                  ? "Actualizează"
                  : "Salvează"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default ProspectForm;