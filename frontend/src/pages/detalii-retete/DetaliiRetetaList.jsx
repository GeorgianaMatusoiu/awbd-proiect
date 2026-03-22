import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteDetaliuReteta,
  getDetaliiRetete,
} from "../../services/detaliiRetetaService";
import "./DetaliiReteta.css";

function DetaliiRetetaList() {
  const [detalii, setDetalii] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const loadDetalii = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getDetaliiRetete();
      setDetalii(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea detaliilor rețetelor:", error);
      setErrorMessage("Nu s-au putut încărca detaliile rețetelor.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDetalii();
  }, []);

  const handleDelete = async (retetaId, medicamentId) => {
    const confirmed = window.confirm(
      "Sigur vrei să ștergi acest detaliu de rețetă?"
    );
    if (!confirmed) return;

    try {
      await deleteDetaliuReteta(retetaId, medicamentId);
      await loadDetalii();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Detaliul rețetei nu a putut fi șters.");
    }
  };

  return (
    <div className="detalii-page">
      <div className="detalii-page__content">
        <div className="detalii-page__topbar">
          <div>
            <p className="detalii-page__subtitle">Administrare detalii rețete</p>
            <h1 className="detalii-page__title">Detalii rețete</h1>
          </div>

          <Link to="/detalii-retete/new" className="detalii-page__add-btn">
            + Adaugă detaliu
          </Link>
        </div>

        {errorMessage && (
          <div className="detalii-page__alert detalii-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="detalii-page__card">
          {loading ? (
            <div className="detalii-page__state">Se încarcă...</div>
          ) : detalii.length === 0 ? (
            <div className="detalii-page__state">
              Nu există detalii de rețete.
            </div>
          ) : (
            <div className="detalii-page__table-wrapper">
              <table className="detalii-page__table">
                <thead>
                  <tr>
                    <th>Rețetă</th>
                    <th>Medicament</th>
                    <th>Preț</th>
                    <th>Cantitate</th>
                    <th>Acțiuni</th>
                  </tr>
                </thead>
                <tbody>
                  {detalii.map((item) => (
                    <tr
                      key={`${item.reteta?.id ?? item.id?.retetaId}-${
                        item.medicament?.id ?? item.id?.medicamentId
                      }`}
                    >
                      <td>{item.reteta?.id ?? item.id?.retetaId ?? "-"}</td>
                      <td>
                        {item.medicament
                          ? `${item.medicament.denumire} (#${item.medicament.id})`
                          : item.id?.medicamentId ?? "-"}
                      </td>
                      <td>{item.pret}</td>
                      <td>{item.cantitate}</td>
                      <td>
                        <div className="detalii-page__actions">
                          <Link
                            to={`/detalii-retete/edit/${
                              item.reteta?.id ?? item.id?.retetaId
                            }/${item.medicament?.id ?? item.id?.medicamentId}`}
                            className="detalii-page__action-btn detalii-page__action-btn--edit"
                          >
                            Edit
                          </Link>

                          <button
                            onClick={() =>
                              handleDelete(
                                item.reteta?.id ?? item.id?.retetaId,
                                item.medicament?.id ?? item.id?.medicamentId
                              )
                            }
                            className="detalii-page__action-btn detalii-page__action-btn--delete"
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

export default DetaliiRetetaList;