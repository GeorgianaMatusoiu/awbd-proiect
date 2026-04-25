import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  createCard,
  getCard,
  updateCard,
} from "../../services/cardFidelitateService";
import { getClients } from "../../services/clientService";
import "./CardFidelitate.css";

function CardFidelitateForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    nivel: "",
    puncte: "",
    clientId: "",
  });

  const [clients, setClients] = useState([]);
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadClients();
    if (isEditMode) {
      loadCard();
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

  const loadCard = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getCard(id);
      const card = response.data;

      setFormData({
        nivel: card.nivel ?? "",
        puncte: card.puncte ?? "",
        clientId: card.client?.id ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea cardului:", error);
      setServerError("Cardul nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.nivel.trim()) {
      newErrors.nivel = "Nivelul este obligatoriu.";
    }

    if (formData.puncte === "") {
      newErrors.puncte = "Punctele sunt obligatorii.";
    } else if (Number(formData.puncte) < 0) {
      newErrors.puncte = "Punctele nu pot fi negative.";
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
        nivel: formData.nivel,
        puncte: Number(formData.puncte),
        clientId: Number(formData.clientId),
      };

      if (isEditMode) {
        await updateCard(id, payload);
      } else {
        await createCard(payload);
      }

      navigate("/carduri");
    } catch (error) {
      console.error("Eroare completă:", error);
      console.error("Response data:", error.response?.data);
      console.error("Status:", error.response?.status);

      if (typeof error.response?.data === "string") {
        setServerError(error.response.data);
      } else if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea cardului.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card-page">
      <div className="card-page__content">
        <div className="card-page__topbar">
          <div>
            <p className="card-page__subtitle">Administrare carduri</p>
            <h1 className="card-page__title">
              {isEditMode ? "Editează card" : "Adaugă card"}
            </h1>
          </div>

          <Link to="/carduri" className="card-page__back-btn">
            Înapoi
          </Link>
        </div>

        {serverError && (
          <div className="card-page__alert card-page__alert--error">
            {serverError}
          </div>
        )}

        <div className="card-page__card card-page__form-card">
          <form onSubmit={handleSubmit} className="card-page__form" noValidate>
            <div className="card-page__form-grid">
              <div className="card-page__form-group">
                <label>Nivel</label>
                <input
                  type="text"
                  name="nivel"
                  value={formData.nivel}
                  onChange={handleChange}
                />
                {errors.nivel && (
                  <div className="card-page__field-error">{errors.nivel}</div>
                )}
              </div>

              <div className="card-page__form-group">
                <label>Puncte</label>
                <input
                  type="number"
                  name="puncte"
                  value={formData.puncte}
                  onChange={handleChange}
                  min="0"
                />
                {errors.puncte && (
                  <div className="card-page__field-error">{errors.puncte}</div>
                )}
              </div>

              <div className="card-page__form-group card-page__form-group--full">
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
                  <div className="card-page__field-error">{errors.clientId}</div>
                )}
              </div>
            </div>

            <div className="card-page__form-actions">
              <button
                type="submit"
                disabled={loading}
                className="card-page__submit-btn"
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

export default CardFidelitateForm;