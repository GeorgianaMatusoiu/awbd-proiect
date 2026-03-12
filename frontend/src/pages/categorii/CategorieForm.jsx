import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createCategorieMedicament,
  getCategorieMedicament,
  updateCategorieMedicament,
} from "../../services/categorieMedicamentService";
import "./Categorie.css";

function CategorieForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    stoc: "",
    temperatura: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEditMode) {
      loadCategorie();
    }
  }, [id]);

  const loadCategorie = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getCategorieMedicament(id);
      const categorie = response.data;

      setFormData({
        stoc: categorie.stoc ?? "",
        temperatura: categorie.temperatura ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea categoriei:", error);
      setServerError("Categoria nu a putut fi încărcată.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (formData.stoc === "") {
      newErrors.stoc = "Stocul este obligatoriu.";
    } else if (Number(formData.stoc) < 0) {
      newErrors.stoc = "Stocul nu poate fi negativ.";
    }

    if (formData.temperatura === "") {
      newErrors.temperatura = "Temperatura este obligatorie.";
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

      const payload = {
        stoc: Number(formData.stoc),
        temperatura: Number(formData.temperatura),
      };

      if (isEditMode) {
        await updateCategorieMedicament(id, payload);
      } else {
        await createCategorieMedicament(payload);
      }

      navigate("/categorii");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea categoriei.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="categorie-page">
      <div className="categorie-page__content">
        <div className="categorie-page__topbar">
          <div>
            <p className="categorie-page__subtitle">Administrare categorii</p>
            <h1 className="categorie-page__title">
              {isEditMode ? "Editează categorie" : "Adaugă categorie"}
            </h1>
          </div>

          <Link to="/categorii" className="categorie-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="categorie-page__alert categorie-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="categorie-page__card categorie-page__form-card">
          <form onSubmit={handleSubmit} className="categorie-page__form" noValidate>
            <div className="categorie-page__form-grid">
              <div className="categorie-page__form-group">
                <label>Stoc</label>
                <input
                  type="number"
                  name="stoc"
                  value={formData.stoc}
                  onChange={handleChange}
                  min="0"
                />
                {errors.stoc && (
                  <div className="categorie-page__field-error">{errors.stoc}</div>
                )}
              </div>

              <div className="categorie-page__form-group">
                <label>Temperatură</label>
                <input
                  type="number"
                  name="temperatura"
                  value={formData.temperatura}
                  onChange={handleChange}
                />
                {errors.temperatura && (
                  <div className="categorie-page__field-error">
                    {errors.temperatura}
                  </div>
                )}
              </div>
            </div>

            <div className="categorie-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="categorie-page__submit-btn"
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

export default CategorieForm;