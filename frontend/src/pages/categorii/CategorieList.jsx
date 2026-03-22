import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteCategorieMedicament,
  getCategoriiMedicamente,
} from "../../services/categorieMedicamentService";
import "./Categorie.css";

function CategorieList() {
  const [categorii, setCategorii] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("stoc");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadCategorii = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getCategoriiMedicamente({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setCategorii(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea categoriilor:", error);
      setErrorMessage("Nu s-au putut încărca categoriile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCategorii(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi această categorie?");
    if (!confirmed) return;

    try {
      await deleteCategorieMedicament(id);
      await loadCategorii(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Categoria nu a putut fi ștearsă.");
    }
  };

  const handlePrevPage = () => {
    if (page > 0) setPage((prev) => prev - 1);
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) setPage((prev) => prev + 1);
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
    <div className="categorie-page">
      <div className="categorie-page__content">
        <div className="categorie-page__topbar">
          <div>
            <p className="categorie-page__subtitle">Administrare categorii</p>
            <h1 className="categorie-page__title">Categorii medicamente</h1>
          </div>

          <Link to="/categorii/new" className="categorie-page__add-btn">
            + Adaugă categorie
          </Link>
        </div>

        {errorMessage && (
          <div className="categorie-page__alert categorie-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="categorie-page__card">
          {loading ? (
            <div className="categorie-page__state">Se încarcă...</div>
          ) : categorii.length === 0 ? (
            <div className="categorie-page__state">Nu există categorii.</div>
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
                    <option value="stoc">Stoc</option>
                    <option value="temperatura">Temperatură</option>
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

              <div className="categorie-page__table-wrapper">
                <table className="categorie-page__table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Stoc</th>
                      <th>Temperatură</th>
                      <th>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {categorii.map((categorie) => (
                      <tr key={categorie.id}>
                        <td>{categorie.id}</td>
                        <td>{categorie.stoc}</td>
                        <td>{categorie.temperatura}</td>
                        <td>
                          <div className="categorie-page__actions">
                            <Link
                              to={`/categorii/edit/${categorie.id}`}
                              className="categorie-page__action-btn categorie-page__action-btn--edit"
                            >
                              Edit
                            </Link>

                            <button
                              onClick={() => handleDelete(categorie.id)}
                              className="categorie-page__action-btn categorie-page__action-btn--delete"
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

export default CategorieList;