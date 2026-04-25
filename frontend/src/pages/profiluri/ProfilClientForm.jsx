import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createProfil,
  getProfil,
  updateProfil,
} from "../../services/profilClientService";
import { getClients } from "../../services/clientService";
import "./ProfilClient.css";

function ProfilClientForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    vaccinari: "",
    alergie: "",
    clientId: "",
  });

  const [clients, setClients] = useState([]);
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadClients();
    if (isEditMode) {
      loadProfil();
    }
  }, [id]);

 const loadClients = async () => {
   try {
     const response = await getClients({ page: 0, size: 100 });
     setClients(response.data.content);
   } catch (error) {
     console.error("Eroare la încărcarea clienților:", error);
     setServerError("Nu s-au putut încărca clienții.");
   }
 };

  const loadProfil = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getProfil(id);
      const profil = response.data;

      setFormData({
        vaccinari: profil.vaccinari ?? "",
        alergie: profil.alergie ?? "",
        clientId: profil.client?.id ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea profilului:", error);
      setServerError("Profilul nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.vaccinari.trim()) {
      newErrors.vaccinari = "Vaccinările sunt obligatorii.";
    }

    if (!formData.alergie.trim()) {
      newErrors.alergie = "Alergia este obligatorie.";
    }

    if (!formData.clientId) {
      newErrors.clientId = "Clientul este obligatoriu.";
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
        vaccinari: formData.vaccinari,
        alergie: formData.alergie,
        clientId: Number(formData.clientId),
      };

      if (isEditMode) {
        await updateProfil(id, payload);
      } else {
        await createProfil(payload);
      }

      navigate("/profiluri");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea profilului.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="profil-page">
      <div className="profil-page__content">
        <div className="profil-page__topbar">
          <div>
            <p className="profil-page__subtitle">Administrare profiluri</p>
            <h1 className="profil-page__title">
              {isEditMode ? "Editează profil" : "Adaugă profil"}
            </h1>
          </div>

          <Link to="/profiluri" className="profil-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="profil-page__alert profil-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="profil-page__card profil-page__form-card">
          <form onSubmit={handleSubmit} className="profil-page__form" noValidate>
            <div className="profil-page__form-grid">
              <div className="profil-page__form-group">
                <label>Vaccinări</label>
                <input
                  type="text"
                  name="vaccinari"
                  value={formData.vaccinari}
                  onChange={handleChange}
                />
                {errors.vaccinari && (
                  <div className="profil-page__field-error">
                    {errors.vaccinari}
                  </div>
                )}
              </div>

              <div className="profil-page__form-group">
                <label>Alergie</label>
                <input
                  type="text"
                  name="alergie"
                  value={formData.alergie}
                  onChange={handleChange}
                />
                {errors.alergie && (
                  <div className="profil-page__field-error">
                    {errors.alergie}
                  </div>
                )}
              </div>

              <div className="profil-page__form-group profil-page__form-group--full">
                <label>Client</label>
                <select
                  name="clientId"
                  value={formData.clientId}
                  onChange={handleChange}
                >
                  <option value="">Selectează client</option>
                  {clients.map((client) => (
                    <option key={client.id} value={client.id}>
                      {client.nume} {client.prenume} ({client.cnp})
                    </option>
                  ))}
                </select>
                {errors.clientId && (
                  <div className="profil-page__field-error">
                    {errors.clientId}
                  </div>
                )}
              </div>
            </div>

            <div className="profil-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="profil-page__submit-btn"
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

export default ProfilClientForm;