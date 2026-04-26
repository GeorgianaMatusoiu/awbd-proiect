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

  const [sortField, setSortField] = useState("afectiuni");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadProspecte = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getProspecte({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setProspecte(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea prospectelor:", error);
      setErrorMessage("Nu s-au putut încărca prospectele.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProspecte(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest prospect?");
    if (!confirmed) return;

    try {
      await deleteProspect(id);
      await loadProspecte(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Prospectul nu a putut fi șters.");
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
                    <option value="afectiuni">Afectiuni</option>
                    <option value="administrare">Administrare</option>
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

export default ProspectList;