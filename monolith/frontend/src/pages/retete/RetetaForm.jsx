import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createReteta,
  getReteta,
  updateReteta,
} from "../../services/retetaService";
import { getClients } from "../../services/clientService";
import { getFarmacisti } from "../../services/farmacistService";
import "./Reteta.css";

function RetetaForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    dataTiparire: "",
    clientId: "",
    farmacistId: "",
  });

  const [clients, setClients] = useState([]);
  const [farmacisti, setFarmacisti] = useState([]);
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadClients();
    loadFarmacisti();

    if (isEditMode) {
      loadReteta();
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

 const loadFarmacisti = async () => {
   try {
     const response = await getFarmacisti({ page: 0, size: 100 });
     setFarmacisti(response.data.content);
   } catch (error) {
     console.error("Eroare la încărcarea farmaciștilor:", error);
     setServerError("Nu s-au putut încărca farmaciștii.");
   }
 };

  const loadReteta = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getReteta(id);
      const reteta = response.data;

      setFormData({
        dataTiparire: reteta.dataTiparire ?? "",
        clientId: reteta.client?.id ?? "",
        farmacistId: reteta.farmacist?.id ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea rețetei:", error);
      setServerError("Rețeta nu a putut fi încărcată.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.dataTiparire) {
      newErrors.dataTiparire = "Data tipăririi este obligatorie.";
    }

    if (!formData.clientId) {
      newErrors.clientId = "Clientul este obligatoriu.";
    }

    if (!formData.farmacistId) {
      newErrors.farmacistId = "Farmacistul este obligatoriu.";
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
        dataTiparire: formData.dataTiparire,
        clientId: Number(formData.clientId),
        farmacistId: Number(formData.farmacistId),
      };

      if (isEditMode) {
        await updateReteta(id, payload);
      } else {
        await createReteta(payload);
      }

      navigate("/retete");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea rețetei.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="reteta-page">
      <div className="reteta-page__content">
        <div className="reteta-page__topbar">
          <div>
            <p className="reteta-page__subtitle">Administrare rețete</p>
            <h1 className="reteta-page__title">
              {isEditMode ? "Editează rețetă" : "Adaugă rețetă"}
            </h1>
          </div>

          <Link to="/retete" className="reteta-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="reteta-page__alert reteta-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="reteta-page__card reteta-page__form-card">
          <form onSubmit={handleSubmit} className="reteta-page__form" noValidate>
            <div className="reteta-page__form-grid">
              <div className="reteta-page__form-group">
                <label>Data tipăririi</label>
                <input
                  type="date"
                  name="dataTiparire"
                  value={formData.dataTiparire}
                  onChange={handleChange}
                />
                {errors.dataTiparire && (
                  <div className="reteta-page__field-error">
                    {errors.dataTiparire}
                  </div>
                )}
              </div>

              <div className="reteta-page__form-group">
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
                  <div className="reteta-page__field-error">
                    {errors.clientId}
                  </div>
                )}
              </div>

              <div className="reteta-page__form-group reteta-page__form-group--full">
                <label>Farmacist</label>
                <select
                  name="farmacistId"
                  value={formData.farmacistId}
                  onChange={handleChange}
                >
                  <option value="">Selectează farmacist</option>
                  {farmacisti.map((farmacist) => (
                    <option key={farmacist.id} value={farmacist.id}>
                      {farmacist.nume} {farmacist.prenume}
                    </option>
                  ))}
                </select>
                {errors.farmacistId && (
                  <div className="reteta-page__field-error">
                    {errors.farmacistId}
                  </div>
                )}
              </div>
            </div>

            <div className="reteta-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="reteta-page__submit-btn"
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

export default RetetaForm;