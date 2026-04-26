import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import {
  createFurnizor,
  getFurnizor,
  updateFurnizor,
} from "../../services/furnizorService";
import "./Furnizor.css";

function FurnizorForm() {

  const navigate = useNavigate();
  const { id } = useParams();

  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    nume: "",
    adresa: "",
    oras: "",
    tara: "",
    telefon: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");

  useEffect(() => {
    if (isEditMode) loadFurnizor();
  }, [id]);

  const loadFurnizor = async () => {
    try {
      const response = await getFurnizor(id);
      setFormData(response.data);
    } catch (error) {
      console.error(error);
      setServerError("Furnizorul nu a putut fi încărcat.");
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.nume.trim()) newErrors.nume = "Numele este obligatoriu.";
    if (!formData.adresa.trim()) newErrors.adresa = "Adresa este obligatorie.";
    if (!formData.oras.trim()) newErrors.oras = "Orașul este obligatoriu.";
    if (!formData.tara.trim()) newErrors.tara = "Țara este obligatorie.";

    if (formData.telefon && formData.telefon.length > 20)
      newErrors.telefon = "Telefon prea lung.";

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData({
      ...formData,
      [name]: value,
    });

    setErrors({
      ...errors,
      [name]: "",
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) return;

    try {
      if (isEditMode) {
        await updateFurnizor(id, formData);
      } else {
        await createFurnizor(formData);
      }

      navigate("/furnizori");

    } catch (error) {
      console.error(error);
      setServerError("Eroare la salvarea furnizorului.");
    }
  };

  return (
    <div className="page">
      <div className="form-container">

        <div className="page-header">
          <h1>{isEditMode ? "Edit Furnizor" : "Adaugă Furnizor"}</h1>

          <Link to="/furnizori" className="btn-secondary">
            Înapoi
          </Link>
        </div>

        {serverError && <div className="alert">{serverError}</div>}

        <form className="form" onSubmit={handleSubmit}>

          <input
            type="text"
            name="nume"
            placeholder="Nume"
            value={formData.nume}
            onChange={handleChange}
          />
          {errors.nume && <span>{errors.nume}</span>}

          <input
            type="text"
            name="adresa"
            placeholder="Adresa"
            value={formData.adresa}
            onChange={handleChange}
          />
          {errors.adresa && <span>{errors.adresa}</span>}

          <input
            type="text"
            name="oras"
            placeholder="Oras"
            value={formData.oras}
            onChange={handleChange}
          />
          {errors.oras && <span>{errors.oras}</span>}

          <input
            type="text"
            name="tara"
            placeholder="Tara"
            value={formData.tara}
            onChange={handleChange}
          />
          {errors.tara && <span>{errors.tara}</span>}

          <input
            type="text"
            name="telefon"
            placeholder="Telefon"
            value={formData.telefon}
            onChange={handleChange}
          />

          <button className="btn-primary">
            {isEditMode ? "Actualizează" : "Salvează"}
          </button>

        </form>

      </div>
    </div>
  );
}

export default FurnizorForm;