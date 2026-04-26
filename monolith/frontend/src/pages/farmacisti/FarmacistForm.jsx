import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createFarmacist,
  getFarmacist,
  updateFarmacist,
} from "../../services/farmacistService";
import "./FarmacistList.css";

function FarmacistForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    nume: "",
    prenume: "",
    dataAngajarii: "",
    telefon: "",
    email: "",
    salariu: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEditMode) {
      loadFarmacist();
    }
  }, [id]);

  const loadFarmacist = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getFarmacist(id);
      const farmacist = response.data;

      setFormData({
        nume: farmacist.nume ?? "",
        prenume: farmacist.prenume ?? "",
        dataAngajarii: farmacist.dataAngajarii ?? "",
        telefon: farmacist.telefon ?? "",
        email: farmacist.email ?? "",
        salariu: farmacist.salariu ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea farmacistului:", error);
      setServerError("Farmacistul nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.nume.trim()) {
      newErrors.nume = "Numele este obligatoriu.";
    }

    if (!formData.prenume.trim()) {
      newErrors.prenume = "Prenumele este obligatoriu.";
    }

    if (formData.telefon && formData.telefon.length > 20) {
      newErrors.telefon = "Telefonul nu poate avea mai mult de 20 caractere.";
    }

    if (
      formData.email &&
      !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(formData.email)
    ) {
      newErrors.email = "Email invalid.";
    }

    if (formData.salariu !== "" && Number(formData.salariu) < 0) {
      newErrors.salariu = "Salariul nu poate fi negativ.";
    }

    if (formData.dataAngajarii) {
      const selectedDate = new Date(formData.dataAngajarii);
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      if (selectedDate > today) {
        newErrors.dataAngajarii =
          "Data angajării trebuie să fie în trecut sau azi.";
      }
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
        ...formData,
        salariu: formData.salariu === "" ? null : formData.salariu,
        dataAngajarii: formData.dataAngajarii === "" ? null : formData.dataAngajarii,
        telefon: formData.telefon === "" ? null : formData.telefon,
        email: formData.email === "" ? null : formData.email,
      };

      if (isEditMode) {
        await updateFarmacist(id, payload);
      } else {
        await createFarmacist(payload);
      }

      navigate("/farmacisti");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea farmacistului.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="farmacist-page">
      <div className="farmacist-page__content">
        <div className="farmacist-page__topbar">
          <div>
            <p className="farmacist-page__subtitle">Administrare farmaciști</p>
            <h1 className="farmacist-page__title">
              {isEditMode ? "Editează farmacist" : "Adaugă farmacist"}
            </h1>
          </div>

          <Link to="/farmacisti" className="farmacist-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="farmacist-page__alert farmacist-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="farmacist-page__card farmacist-page__form-card">
          <form onSubmit={handleSubmit} className="farmacist-page__form" noValidate>
            <div className="farmacist-page__form-grid">
              <div className="farmacist-page__form-group">
                <label>Nume</label>
                <input
                  type="text"
                  name="nume"
                  value={formData.nume}
                  onChange={handleChange}
                />
                {errors.nume && (
                  <div className="farmacist-page__field-error">{errors.nume}</div>
                )}
              </div>

              <div className="farmacist-page__form-group">
                <label>Prenume</label>
                <input
                  type="text"
                  name="prenume"
                  value={formData.prenume}
                  onChange={handleChange}
                />
                {errors.prenume && (
                  <div className="farmacist-page__field-error">{errors.prenume}</div>
                )}
              </div>

              <div className="farmacist-page__form-group">
                <label>Data angajării</label>
                <input
                  type="date"
                  name="dataAngajarii"
                  value={formData.dataAngajarii}
                  onChange={handleChange}
                />
                {errors.dataAngajarii && (
                  <div className="farmacist-page__field-error">
                    {errors.dataAngajarii}
                  </div>
                )}
              </div>

              <div className="farmacist-page__form-group">
                <label>Telefon</label>
                <input
                  type="text"
                  name="telefon"
                  value={formData.telefon}
                  onChange={handleChange}
                />
                {errors.telefon && (
                  <div className="farmacist-page__field-error">{errors.telefon}</div>
                )}
              </div>

              <div className="farmacist-page__form-group">
                <label>Email</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                />
                {errors.email && (
                  <div className="farmacist-page__field-error">{errors.email}</div>
                )}
              </div>

              <div className="farmacist-page__form-group">
                <label>Salariu</label>
                <input
                  type="number"
                  step="0.01"
                  name="salariu"
                  value={formData.salariu}
                  onChange={handleChange}
                />
                {errors.salariu && (
                  <div className="farmacist-page__field-error">{errors.salariu}</div>
                )}
              </div>
            </div>

            <div className="farmacist-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="farmacist-page__submit-btn"
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

export default FarmacistForm;