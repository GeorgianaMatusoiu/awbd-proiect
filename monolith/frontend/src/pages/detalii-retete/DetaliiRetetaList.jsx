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

  const [sortField, setSortField] = useState("pret");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadDetalii = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getDetaliiRetete({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setDetalii(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea detaliilor rețetelor:", error);
      setErrorMessage("Nu s-au putut încărca detaliile rețetelor.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDetalii(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (retetaId, medicamentId) => {
    const confirmed = window.confirm(
      "Sigur vrei să ștergi acest detaliu de rețetă?"
    );
    if (!confirmed) return;

    try {
      await deleteDetaliuReteta(retetaId, medicamentId);
      await loadDetalii(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Detaliul rețetei nu a putut fi șters.");
    }
  };

  const handlePrevPage = () => {
    if (page > 0) {
      setPage((prev) => prev - 1);
    }
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage((prev) => prev + 1);
    }
  };

  const handleSizeChange = (event) => {
    setSize(Number(event.target.value));
    setPage(0);
  };

  const handleSortFieldChange = (event) => {
    setSortField(event.target.value);
    setPage(0);
  };

  const handleSortDirectionChange = (event) => {
    setSortDirection(event.target.value);
    setPage(0);
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
            <>
              <div
                style={{
                  padding: "16px 20px",
                  display: "flex",
                  gap: "12px",
                  alignItems: "center",
                  flexWrap: "wrap",
                }}
              >
                <div>
                  <label style={{ marginRight: "8px" }}>Elemente pe pagină:</label>
                  <select value={size} onChange={handleSizeChange}>
                    <option value={2}>2</option>
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                  </select>
                </div>

                <div>
                  <label style={{ marginRight: "8px" }}>Sortează după:</label>
                  <select value={sortField} onChange={handleSortFieldChange}>
                    <option value="pret">Preț</option>
                    <option value="cantitate">Cantitate</option>
                  </select>
                </div>

                <div>
                  <label style={{ marginRight: "8px" }}>Ordine:</label>
                  <select value={sortDirection} onChange={handleSortDirectionChange}>
                    <option value="asc">Ascendent</option>
                    <option value="desc">Descendent</option>
                  </select>
                </div>
              </div>

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

              <div
                style={{
                  padding: "16px 20px",
                  display: "flex",
                  gap: "10px",
                  alignItems: "center",
                }}
              >
                <button onClick={handlePrevPage} disabled={page === 0}>
                  Previous
                </button>

                <span>
                  Pagina {totalPages === 0 ? 0 : page + 1} din {totalPages}
                </span>

                <button
                  onClick={handleNextPage}
                  disabled={page >= totalPages - 1}
                >
                  Next
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default DetaliiRetetaList;