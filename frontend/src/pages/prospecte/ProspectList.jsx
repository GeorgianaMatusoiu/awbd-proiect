import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteProspect,
  getProspecte,
} from "../../services/prospectService";
import "./Prospect.css";

function ProspectList() {
  const [prospecte, setProspecte] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const loadProspecte = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getProspecte();
      setProspecte(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea prospectelor:", error);
      setErrorMessage("Nu s-au putut încărca prospectele.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProspecte();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest prospect?");
    if (!confirmed) return;

    try {
      await deleteProspect(id);
      await loadProspecte();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Prospectul nu a putut fi șters.");
    }
  };

  return (
    <div className="prospect-page">
      <div className="prospect-page__content">
        <div className="prospect-page__topbar">
          <div>
            <p className="prospect-page__subtitle">Administrare prospecte</p>
            <h1 className="prospect-page__title">Prospecte</h1>
          </div>

          <Link to="/prospecte/new" className="prospect-page__add-btn">
            + Adaugă prospect
          </Link>
        </div>

        {errorMessage && (
          <div className="prospect-page__alert prospect-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="prospect-page__card">
          {loading ? (
            <div className="prospect-page__state">Se încarcă...</div>
          ) : prospecte.length === 0 ? (
            <div className="prospect-page__state">Nu există prospecte.</div>
          ) : (
            <div className="prospect-page__table-wrapper">
              <table className="prospect-page__table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Afectiuni</th>
                    <th>Administrare</th>
                    <th>Acțiuni</th>
                  </tr>
                </thead>
                <tbody>
                  {prospecte.map((prospect) => (
                    <tr key={prospect.id}>
                      <td>{prospect.id}</td>
                      <td>{prospect.afectiuni}</td>
                      <td>{prospect.administrare}</td>
                      <td>
                        <div className="prospect-page__actions">
                          <Link
                            to={`/prospecte/edit/${prospect.id}`}
                            className="prospect-page__action-btn prospect-page__action-btn--edit"
                          >
                            Edit
                          </Link>

                          <button
                            onClick={() => handleDelete(prospect.id)}
                            className="prospect-page__action-btn prospect-page__action-btn--delete"
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

export default ProspectList;