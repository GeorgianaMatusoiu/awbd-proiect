import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import {
  createClient,
  getClient,
  updateClient,
} from "../../services/clientService";

function ClientForm() {
  const navigate = useNavigate();
  const { id } = useParams();

  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    cnp: "",
    nume: "",
    prenume: "",
    varsta: "",
    telefon: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEditMode) {
      loadClient();
    }
  }, [id]);

  const loadClient = async () => {
    try {
      setLoading(true);
      setServerError("");
      const response = await getClient(id);
      const client = response.data;

      setFormData({
        cnp: client.cnp ?? "",
        nume: client.nume ?? "",
        prenume: client.prenume ?? "",
        varsta: client.varsta ?? "",
        telefon: client.telefon ?? "",
      });
    } catch (error) {
      console.error("Eroare la încărcarea clientului:", error);
      setServerError("Clientul nu a putut fi încărcat.");
    } finally {
      setLoading(false);
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.cnp.trim()) {
      newErrors.cnp = "CNP-ul este obligatoriu.";
    } else if (!/^\d{13}$/.test(formData.cnp)) {
      newErrors.cnp = "CNP-ul trebuie să conțină exact 13 cifre.";
    }

    if (!formData.nume.trim()) {
      newErrors.nume = "Numele este obligatoriu.";
    }

    if (!formData.prenume.trim()) {
      newErrors.prenume = "Prenumele este obligatoriu.";
    }

    if (formData.varsta === "" || formData.varsta === null) {
      newErrors.varsta = "Vârsta este obligatorie.";
    } else if (Number(formData.varsta) < 0 || Number(formData.varsta) > 120) {
      newErrors.varsta = "Vârsta trebuie să fie între 0 și 120.";
    }

    if (!formData.telefon.trim()) {
      newErrors.telefon = "Telefonul este obligatoriu.";
    } else if (!/^(\+4)?0\d{9}$/.test(formData.telefon)) {
      newErrors.telefon =
        "Telefonul trebuie să aibă format valid, ex: 0712345678.";
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

    if (!validate()) {
      return;
    }

    try {
      setLoading(true);
      setServerError("");

      const payload = {
        ...formData,
        varsta: Number(formData.varsta),
      };

      if (isEditMode) {
        await updateClient(id, payload);
      } else {
        await createClient(payload);
      }

      navigate("/");
    } catch (error) {
      console.error("Eroare la salvare:", error);

      if (error.response?.data?.message) {
        setServerError(error.response.data.message);
      } else {
        setServerError("A apărut o eroare la salvarea clientului.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.topBar}>
        <h1>{isEditMode ? "Editează client" : "Adaugă client"}</h1>
        <Link to="/" style={styles.backButton}>
          Înapoi
        </Link>
      </div>

      {serverError && <div style={styles.errorBox}>{serverError}</div>}

      <form onSubmit={handleSubmit} style={styles.form} noValidate>
        <div style={styles.formGroup}>
          <label style={styles.label}>CNP</label>
          <input
            type="text"
            name="cnp"
            value={formData.cnp}
            onChange={handleChange}
            maxLength="13"
            required
            style={styles.input}
          />
          {errors.cnp && <div style={styles.fieldError}>{errors.cnp}</div>}
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Nume</label>
          <input
            type="text"
            name="nume"
            value={formData.nume}
            onChange={handleChange}
            required
            style={styles.input}
          />
          {errors.nume && <div style={styles.fieldError}>{errors.nume}</div>}
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Prenume</label>
          <input
            type="text"
            name="prenume"
            value={formData.prenume}
            onChange={handleChange}
            required
            style={styles.input}
          />
          {errors.prenume && (
            <div style={styles.fieldError}>{errors.prenume}</div>
          )}
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Vârstă</label>
          <input
            type="number"
            name="varsta"
            value={formData.varsta}
            onChange={handleChange}
            min="0"
            max="120"
            required
            style={styles.input}
          />
          {errors.varsta && (
            <div style={styles.fieldError}>{errors.varsta}</div>
          )}
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Telefon</label>
          <input
            type="text"
            name="telefon"
            value={formData.telefon}
            onChange={handleChange}
            required
            style={styles.input}
          />
          {errors.telefon && (
            <div style={styles.fieldError}>{errors.telefon}</div>
          )}
        </div>

        <button type="submit" disabled={loading} style={styles.submitButton}>
          {loading ? "Se salvează..." : isEditMode ? "Actualizează" : "Salvează"}
        </button>
      </form>
    </div>
  );
}

const styles = {
  container: {
    maxWidth: "700px",
    margin: "30px auto",
    padding: "20px",
    fontFamily: "Arial, sans-serif",
  },
  topBar: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "20px",
  },
  backButton: {
    textDecoration: "none",
    backgroundColor: "#757575",
    color: "#fff",
    padding: "10px 14px",
    borderRadius: "6px",
  },
  form: {
    backgroundColor: "#fff",
    padding: "20px",
    borderRadius: "8px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
  },
  formGroup: {
    marginBottom: "16px",
  },
  label: {
    display: "block",
    marginBottom: "6px",
    fontWeight: "bold",
  },
  input: {
    width: "100%",
    padding: "10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    boxSizing: "border-box",
  },
  submitButton: {
    border: "none",
    backgroundColor: "#1976d2",
    color: "#fff",
    padding: "12px 18px",
    borderRadius: "6px",
    cursor: "pointer",
  },
  errorBox: {
    backgroundColor: "#ffebee",
    color: "#b71c1c",
    padding: "12px",
    borderRadius: "6px",
    marginBottom: "16px",
  },
  fieldError: {
    color: "#d32f2f",
    fontSize: "14px",
    marginTop: "4px",
  },
};

export default ClientForm;