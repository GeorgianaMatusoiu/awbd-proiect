import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteReteta, getRetete } from "../../services/retetaService";
import "./Reteta.css";

function RetetaList() {
  const [retete, setRetete] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("dataTiparire");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadRetete = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getRetete({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setRetete(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea rețetelor:", error);
      setErrorMessage("Nu s-au putut încărca rețetele.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRetete(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi această rețetă?");
    if (!confirmed) return;

    try {
      await deleteReteta(id);
      await loadRetete(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Rețeta nu a putut fi ștearsă.");
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
                    <option value="dataTiparire">Data tipăririi</option>
                    <option value="id">ID</option>
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

export default RetetaList;