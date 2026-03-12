import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteMedicament,
  getMedicamente,
} from "../../services/medicamentService";
import "./Medicament.css";

function MedicamentList() {
  const [medicamente, setMedicamente] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const loadMedicamente = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getMedicamente();
      setMedicamente(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea medicamentelor:", error);
      setErrorMessage("Nu s-au putut încărca medicamentele.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMedicamente();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest medicament?");
    if (!confirmed) return;

    try {
      await deleteMedicament(id);
      await loadMedicamente();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Medicamentul nu a putut fi șters.");
    }
  };

  return (
    <div className="medicament-page">
      <div className="medicament-page__content">
        <div className="medicament-page__topbar">
          <div>
            <p className="medicament-page__subtitle">Administrare medicamente</p>
            <h1 className="medicament-page__title">Medicamente</h1>
          </div>

          <Link to="/medicamente/new" className="medicament-page__add-btn">
            + Adaugă medicament
          </Link>
        </div>

        {errorMessage && (
          <div className="medicament-page__alert medicament-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="medicament-page__card">
          {loading ? (
            <div className="medicament-page__state">Se încarcă...</div>
          ) : medicamente.length === 0 ? (
            <div className="medicament-page__state">
              Nu există medicamente.
            </div>
          ) : (
            <div className="medicament-page__table-wrapper">
              <table className="medicament-page__table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Denumire</th>
                    <th>Data expirare</th>
                    <th>Preț</th>
                    <th>Furnizor</th>
                    <th>Prospect</th>
                    <th>Categorie</th>
                    <th>Acțiuni</th>
                  </tr>
                </thead>
                <tbody>
                  {medicamente.map((medicament) => (
                    <tr key={medicament.id}>
                      <td>{medicament.id}</td>
                      <td>{medicament.denumire}</td>
                      <td>{medicament.dataExpirare}</td>
                      <td>{medicament.pret}</td>
                      <td>{medicament.furnizor?.nume || "-"}</td>
                      <td>{medicament.prospect?.afectiuni || "-"}</td>
                      <td>{medicament.categorie?.id || "-"}</td>
                      <td>
                        <div className="medicament-page__actions">
                          <Link
                            to={`/medicamente/edit/${medicament.id}`}
                            className="medicament-page__action-btn medicament-page__action-btn--edit"
                          >
                            Edit
                          </Link>

                          <button
                            onClick={() => handleDelete(medicament.id)}
                            className="medicament-page__action-btn medicament-page__action-btn--delete"
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default MedicamentList;