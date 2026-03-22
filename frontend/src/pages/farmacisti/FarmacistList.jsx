import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteFarmacist,
  getFarmacisti,
} from "../../services/farmacistService";
import "./FarmacistList.css";

function FarmacistList() {
  const [farmacisti, setFarmacisti] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("nume");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadFarmacisti = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getFarmacisti({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setFarmacisti(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea farmaciștilor:", error);
      setErrorMessage("Nu s-au putut încărca farmaciștii.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFarmacisti(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest farmacist?");
    if (!confirmed) return;

    try {
      await deleteFarmacist(id);
      await loadFarmacisti(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Farmacistul nu a putut fi șters.");
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
    <div className="farmacist-page">
      <div className="farmacist-page__content">
        <div className="farmacist-page__topbar">
          <div>
            <p className="farmacist-page__subtitle">Administrare farmaciști</p>
            <h1 className="farmacist-page__title">Farmaciști</h1>
          </div>

          <Link to="/farmacisti/new" className="farmacist-page__add-btn">
            + Adaugă farmacist
          </Link>
        </div>

        {errorMessage && (
          <div className="farmacist-page__alert farmacist-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="farmacist-page__card">
          {loading ? (
            <div className="farmacist-page__state">Se încarcă...</div>
          ) : farmacisti.length === 0 ? (
            <div className="farmacist-page__state">
              Nu există farmaciști.
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
                    <option value="nume">Nume</option>
                    <option value="salariu">Salariu</option>
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

              <div className="farmacist-page__table-wrapper">
                <table className="farmacist-page__table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th>Data angajării</th>
                      <th>Telefon</th>
                      <th>Email</th>
                      <th>Salariu</th>
                      <th>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {farmacisti.map((farmacist) => (
                      <tr key={farmacist.id}>
                        <td>{farmacist.id}</td>
                        <td>{farmacist.nume}</td>
                        <td>{farmacist.prenume}</td>
                        <td>{farmacist.dataAngajarii || "-"}</td>
                        <td>{farmacist.telefon || "-"}</td>
                        <td>{farmacist.email || "-"}</td>
                        <td>{farmacist.salariu || "-"}</td>
                        <td>
                          <div className="farmacist-page__actions">
                            <Link
                              to={`/farmacisti/edit/${farmacist.id}`}
                              className="farmacist-page__action-btn farmacist-page__action-btn--edit"
                            >
                              Edit
                            </Link>

                            <button
                              onClick={() => handleDelete(farmacist.id)}
                              className="farmacist-page__action-btn farmacist-page__action-btn--delete"
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

export default FarmacistList;