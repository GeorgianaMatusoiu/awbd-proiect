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

  const [sortField, setSortField] = useState("denumire");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadMedicamente = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getMedicamente({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setMedicamente(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea medicamentelor:", error);
      setErrorMessage("Nu s-au putut încărca medicamentele.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMedicamente(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest medicament?");
    if (!confirmed) return;

    try {
      await deleteMedicament(id);
      await loadMedicamente(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Medicamentul nu a putut fi șters.");
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
                    <option value="denumire">Denumire</option>
                    <option value="pret">Preț</option>
                    <option value="dataExpirare">Data expirare</option>
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

export default MedicamentList;