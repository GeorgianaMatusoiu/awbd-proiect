import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createDetaliuReteta,
  getDetaliuReteta,
  updateDetaliuReteta,
} from "../../services/detaliiRetetaService";
import { getRetete } from "../../services/retetaService";
import { getMedicamente } from "../../services/medicamentService";
import "./DetaliiReteta.css";

function DetaliiRetetaForm() {
  const navigate = useNavigate();
  const { retetaId, medicamentId } = useParams();
  const isEditMode = Boolean(retetaId && medicamentId);

  const [formData, setFormData] = useState({
    retetaId: "",
    medicamentId: "",
    pret: "",
    cantitate: "",
  });

  const [retete, setRetete] = useState([]);
  const [medicamente, setMedicamente] = useState([]);
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadRetete();
    loadMedicamente();

    if (isEditMode) {
      loadDetaliu();
    }
  }, [retetaId, medicamentId]);

  const loadRetete = async () => {
    try {
      const response = await getRetete();
      setRetete(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea rețetelor:", error);
      setServerError("Nu s-au putut încărca rețetele.");
    }
  };

  const loadMedicamente = async () => {
    try {
      const response = await getMedicamente();
      setMedicamente(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea medicamentelor:", error);
      setServerError("Nu s-au putut încărca medicamentele.");
    }
  };

  const loadDetaliu = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getDetaliuReteta(retetaId, medicamentId);
      const detaliu = response.data;

      setFormData({
        retetaId: detaliu.reteta?.id ?? detaliu.id?.retetaId ?? "",
        medicamentId: detaliu.medicament?.id ?? detaliu.id?.medicamentId ?? "",
        pret: detaliu.pret ?? "",
        cantitate: detaliu.cantitate ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea detaliului rețetei:", error);
      setServerError("Detaliul rețetei nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.retetaId) {
      newErrors.retetaId = "Rețeta este obligatorie.";
    }

    if (!formData.medicamentId) {
      newErrors.medicamentId = "Medicamentul este obligatoriu.";
    }

    if (formData.pret === "") {
      newErrors.pret = "Prețul este obligatoriu.";
    } else if (Number(formData.pret) < 0) {
      newErrors.pret = "Prețul nu poate fi negativ.";
    }

    if (formData.cantitate === "") {
      newErrors.cantitate = "Cantitatea este obligatorie.";
    } else if (Number(formData.cantitate) < 1) {
      newErrors.cantitate = "Cantitatea trebuie să fie de cel puțin 1.";
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
        retetaId: Number(formData.retetaId),
        medicamentId: Number(formData.medicamentId),
        pret: formData.pret,
        cantitate: Number(formData.cantitate),
      };

      if (isEditMode) {
        await updateDetaliuReteta(retetaId, medicamentId, payload);
      } else {
        await createDetaliuReteta(payload);
      }

      navigate("/detalii-retete");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea detaliului rețetei.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="detalii-page">
      <div className="detalii-page__content">
        <div className="detalii-page__topbar">
          <div>
            <p className="detalii-page__subtitle">Administrare detalii rețete</p>
            <h1 className="detalii-page__title">
              {isEditMode ? "Editează detaliu rețetă" : "Adaugă detaliu rețetă"}
            </h1>
          </div>

          <Link to="/detalii-retete" className="detalii-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="detalii-page__alert detalii-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="detalii-page__card detalii-page__form-card">
          <form
            onSubmit={handleSubmit}
            className="detalii-page__form"
            noValidate
          >
            <div className="detalii-page__form-grid">
              <div className="detalii-page__form-group">
                <label>Rețetă</label>
                <select
                  name="retetaId"
                  value={formData.retetaId}
                  onChange={handleChange}
                  disabled={isEditMode}
                >
                  <option value="">Selectează rețeta</option>
                  {retete.map((reteta) => (
                    <option key={reteta.id} value={reteta.id}>
                      Rețeta #{reteta.id} - {reteta.client?.nume}{" "}
                      {reteta.client?.prenume}
                    </option>
                  ))}
                </select>
                {errors.retetaId && (
                  <div className="detalii-page__field-error">
                    {errors.retetaId}
                  </div>
                )}
              </div>

              <div className="detalii-page__form-group">
                <label>Medicament</label>
                <select
                  name="medicamentId"
                  value={formData.medicamentId}
                  onChange={handleChange}
                  disabled={isEditMode}
                >
                  <option value="">Selectează medicamentul</option>
                  {medicamente.map((medicament) => (
                    <option key={medicament.id} value={medicament.id}>
                      {medicament.denumire} (#{medicament.id})
                    </option>
                  ))}
                </select>
                {errors.medicamentId && (
                  <div className="detalii-page__field-error">
                    {errors.medicamentId}
                  </div>
                )}
              </div>

              <div className="detalii-page__form-group">
                <label>Preț</label>
                <input
                  type="number"
                  step="0.01"
                  min="0"
                  name="pret"
                  value={formData.pret}
                  onChange={handleChange}
                />
                {errors.pret && (
                  <div className="detalii-page__field-error">{errors.pret}</div>
                )}
              </div>

              <div className="detalii-page__form-group">
                <label>Cantitate</label>
                <input
                  type="number"
                  min="1"
                  name="cantitate"
                  value={formData.cantitate}
                  onChange={handleChange}
                />
                {errors.cantitate && (
                  <div className="detalii-page__field-error">
                    {errors.cantitate}
                  </div>
                )}
              </div>
            </div>

            <div className="detalii-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="detalii-page__submit-btn"
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

export default DetaliiRetetaForm;