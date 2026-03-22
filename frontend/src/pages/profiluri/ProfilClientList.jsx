import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteProfil,
  getProfiluri,
} from "../../services/profilClientService";
import "./ProfilClient.css";

function ProfilClientList() {
  const [profiluri, setProfiluri] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("vaccinari");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadProfiluri = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getProfiluri({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setProfiluri(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea profilurilor:", error);
      setErrorMessage("Nu s-au putut încărca profilurile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfiluri(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest profil?");
    if (!confirmed) return;

    try {
      await deleteProfil(id);
      await loadProfiluri(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Profilul nu a putut fi șters.");
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
    <div className="profil-page">
      <div className="profil-page__content">
        <div className="profil-page__topbar">
          <div>
            <p className="profil-page__subtitle">Administrare profiluri</p>
            <h1 className="profil-page__title">Profiluri client</h1>
          </div>

          <Link to="/profiluri/new" className="profil-page__add-btn">
            + Adaugă profil
          </Link>
        </div>

        {errorMessage && (
          <div className="profil-page__alert profil-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="profil-page__card">
          {loading ? (
            <div className="profil-page__state">Se încarcă...</div>
          ) : profiluri.length === 0 ? (
            <div className="profil-page__state">Nu există profiluri.</div>
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
                    <option value="vaccinari">Vaccinări</option>
                    <option value="alergie">Alergie</option>
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

              <div className="profil-page__table-wrapper">
                <table className="profil-page__table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Vaccinări</th>
                      <th>Alergie</th>
                      <th>Client</th>
                      <th>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {profiluri.map((profil) => (
                      <tr key={profil.id}>
                        <td>{profil.id}</td>
                        <td>{profil.vaccinari}</td>
                        <td>{profil.alergie}</td>
                        <td>
                          {profil.client
                            ? `${profil.client.nume} ${profil.client.prenume}`
                            : "-"}
                        </td>
                        <td>
                          <div className="profil-page__actions">
                            <Link
                              to={`/profiluri/edit/${profil.id}`}
                              className="profil-page__action-btn profil-page__action-btn--edit"
                            >
                              Edit
                            </Link>

                            <button
                              onClick={() => handleDelete(profil.id)}
                              className="profil-page__action-btn profil-page__action-btn--delete"
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

export default ProfilClientList;