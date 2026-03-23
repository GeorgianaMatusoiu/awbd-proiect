import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteReteta, getRetete } from "../../services/retetaService";
import "./Reteta.css";

function RetetaList() {
  const [retete, setRetete] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const loadRetete = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getRetete();
      setRetete(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea rețetelor:", error);
      setErrorMessage("Nu s-au putut încărca rețetele.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRetete();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi această rețetă?");
    if (!confirmed) return;

    try {
      await deleteReteta(id);
      await loadRetete();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Rețeta nu a putut fi ștearsă.");
    }
  };

  return (
    <div className="reteta-page">
      <div className="reteta-page__content">
        <div className="reteta-page__topbar">
          <div>
            <p className="reteta-page__subtitle">Administrare rețete</p>
            <h1 className="reteta-page__title">Rețete</h1>
          </div>

          <Link to="/retete/new" className="reteta-page__add-btn">
            + Adaugă rețetă
          </Link>
        </div>

        {errorMessage && (
          <div className="reteta-page__alert reteta-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="reteta-page__card">
          {loading ? (
            <div className="reteta-page__state">Se încarcă...</div>
          ) : retete.length === 0 ? (
            <div className="reteta-page__state">Nu există rețete.</div>
          ) : (
            <div className="reteta-page__table-wrapper">
              <table className="reteta-page__table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Data tipăririi</th>
                    <th>Client</th>
                    <th>Farmacist</th>
                    <th>Acțiuni</th>
                  </tr>
                </thead>
                <tbody>
                  {retete.map((reteta) => (
                    <tr key={reteta.id}>
                      <td>{reteta.id}</td>
                      <td>{reteta.dataTiparire}</td>
                      <td>
                        {reteta.client
                          ? `${reteta.client.nume} ${reteta.client.prenume}`
                          : "-"}
                      </td>
                      <td>
                        {reteta.farmacist
                          ? `${reteta.farmacist.nume} ${reteta.farmacist.prenume}`
                          : "-"}
                      </td>
                      <td>
                        <div className="reteta-page__actions">
                          <Link
                            to={`/retete/edit/${reteta.id}`}
                            className="reteta-page__action-btn reteta-page__action-btn--edit"
                          >
                            Edit
                          </Link>

                          <button
                            onClick={() => handleDelete(reteta.id)}
                            className="reteta-page__action-btn reteta-page__action-btn--delete"
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

export default RetetaList;