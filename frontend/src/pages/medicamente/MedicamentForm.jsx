import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createMedicament,
  getMedicament,
  updateMedicament,
} from "../../services/medicamentService";
import { getFurnizori } from "../../services/furnizorService";
import { getProspecte } from "../../services/prospectService";
import { getCategoriiMedicamente } from "../../services/categorieMedicamentService";
import "./Medicament.css";

function MedicamentForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    denumire: "",
    dataExpirare: "",
    pret: "",
    furnizorId: "",
    prospectId: "",
    categorieId: "",
  });

  const [furnizori, setFurnizori] = useState([]);
  const [prospecte, setProspecte] = useState([]);
  const [categorii, setCategorii] = useState([]);

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadDropdowns();
    if (isEditMode) {
      loadMedicament();
    }
  }, [id]);

  const loadDropdowns = async () => {
    try {
      const [furnizoriRes, prospecteRes, categoriiRes] = await Promise.all([
        getFurnizori(),
        getProspecte(),
        getCategoriiMedicamente(),
      ]);

      setFurnizori(furnizoriRes.data);
      setProspecte(prospecteRes.data);
      setCategorii(categoriiRes.data);
    } catch (error) {
      console.error("Eroare la încărcarea listelor:", error);
      setServerError("Nu s-au putut încărca datele auxiliare.");
    }
  };

  const loadMedicament = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getMedicament(id);
      const medicament = response.data;

      setFormData({
        denumire: medicament.denumire ?? "",
        dataExpirare: medicament.dataExpirare ?? "",
        pret: medicament.pret ?? "",
        furnizorId: medicament.furnizor?.id ?? "",
        prospectId: medicament.prospect?.id ?? "",
        categorieId: medicament.categorie?.id ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea medicamentului:", error);
      setServerError("Medicamentul nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.denumire.trim()) {
      newErrors.denumire = "Denumirea este obligatorie.";
    }

    if (!formData.dataExpirare) {
      newErrors.dataExpirare = "Data expirării este obligatorie.";
    }

    if (formData.pret === "") {
      newErrors.pret = "Prețul este obligatoriu.";
    } else if (Number(formData.pret) < 0) {
      newErrors.pret = "Prețul nu poate fi negativ.";
    }

    if (!formData.furnizorId) {
      newErrors.furnizorId = "Furnizorul este obligatoriu.";
    }

    if (!formData.prospectId) {
      newErrors.prospectId = "Prospectul este obligatoriu.";
    }

    if (!formData.categorieId) {
      newErrors.categorieId = "Categoria este obligatorie.";
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
        pret: formData.pret === "" ? null : formData.pret,
        furnizorId: Number(formData.furnizorId),
        prospectId: Number(formData.prospectId),
        categorieId: Number(formData.categorieId),
      };

      if (isEditMode) {
        await updateMedicament(id, payload);
      } else {
        await createMedicament(payload);
      }

      navigate("/medicamente");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea medicamentului.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="medicament-page">
      <div className="medicament-page__content">
        <div className="medicament-page__topbar">
          <div>
            <p className="medicament-page__subtitle">Administrare medicamente</p>
            <h1 className="medicament-page__title">
              {isEditMode ? "Editează medicament" : "Adaugă medicament"}
            </h1>
          </div>

          <Link to="/medicamente" className="medicament-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="medicament-page__alert medicament-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="medicament-page__card medicament-page__form-card">
          <form onSubmit={handleSubmit} className="medicament-page__form" noValidate>
            <div className="medicament-page__form-grid">
              <div className="medicament-page__form-group">
                <label>Denumire</label>
                <input
                  type="text"
                  name="denumire"
                  value={formData.denumire}
                  onChange={handleChange}
                />
                {errors.denumire && (
                  <div className="medicament-page__field-error">
                    {errors.denumire}
                  </div>
                )}
              </div>

              <div className="medicament-page__form-group">
                <label>Data expirare</label>
                <input
                  type="date"
                  name="dataExpirare"
                  value={formData.dataExpirare}
                  onChange={handleChange}
                />
                {errors.dataExpirare && (
                  <div className="medicament-page__field-error">
                    {errors.dataExpirare}
                  </div>
                )}
              </div>

              <div className="medicament-page__form-group">
                <label>Preț</label>
                <input
                  type="number"
                  step="0.01"
                  name="pret"
                  value={formData.pret}
                  onChange={handleChange}
                />
                {errors.pret && (
                  <div className="medicament-page__field-error">
                    {errors.pret}
                  </div>
                )}
              </div>

              <div className="medicament-page__form-group">
                <label>Furnizor</label>
                <select
                  name="furnizorId"
                  value={formData.furnizorId}
                  onChange={handleChange}
                >
                  <option value="">Selectează furnizor</option>
                  {furnizori.map((furnizor) => (
                    <option key={furnizor.id} value={furnizor.id}>
                      {furnizor.nume}
                    </option>
                  ))}
                </select>
                {errors.furnizorId && (
                  <div className="medicament-page__field-error">
                    {errors.furnizorId}
                  </div>
                )}
              </div>

              <div className="medicament-page__form-group">
                <label>Prospect</label>
                <select
                  name="prospectId"
                  value={formData.prospectId}
                  onChange={handleChange}
                >
                  <option value="">Selectează prospect</option>
                  {prospecte.map((prospect) => (
                    <option key={prospect.id} value={prospect.id}>
                      {prospect.afectiuni}
                    </option>
                  ))}
                </select>
                {errors.prospectId && (
                  <div className="medicament-page__field-error">
                    {errors.prospectId}
                  </div>
                )}
              </div>

              <div className="medicament-page__form-group">
                <label>Categorie</label>
                <select
                  name="categorieId"
                  value={formData.categorieId}
                  onChange={handleChange}
                >
                  <option value="">Selectează categorie</option>
                  {categorii.map((categorie) => (
                    <option key={categorie.id} value={categorie.id}>
                      Categoria #{categorie.id}
                    </option>
                  ))}
                </select>
                {errors.categorieId && (
                  <div className="medicament-page__field-error">
                    {errors.categorieId}
                  </div>
                )}
              </div>
            </div>

            <div className="medicament-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="medicament-page__submit-btn"
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

export default MedicamentForm;