import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteCard,
  getCarduri,
} from "../../services/cardFidelitateService";
import "./CardFidelitate.css";

function CardFidelitateList() {
  const [carduri, setCarduri] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("nivel");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadCarduri = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getCarduri({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setCarduri(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea cardurilor:", error);
      setErrorMessage("Nu s-au putut încărca cardurile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCarduri(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest card?");
    if (!confirmed) return;

    try {
      await deleteCard(id);
      await loadCarduri(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Cardul nu a putut fi șters.");
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
    <div className="card-page">
      <div className="card-page__content">
        <div className="card-page__topbar">
          <div>
            <p className="card-page__subtitle">Administrare carduri</p>
            <h1 className="card-page__title">Carduri fidelitate</h1>
          </div>

          <Link to="/carduri/new" className="card-page__add-btn">
            + Adaugă card
          </Link>
        </div>

        {errorMessage && (
          <div className="card-page__alert card-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="card-page__card">
          {loading ? (
            <div className="card-page__state">Se încarcă...</div>
          ) : carduri.length === 0 ? (
            <div className="card-page__state">Nu există carduri.</div>
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
                    <option value="nivel">Nivel</option>
                    <option value="puncte">Puncte</option>
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

              <div className="card-page__table-wrapper">
                <table className="card-page__table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Nivel</th>
                      <th>Puncte</th>
                      <th>Client</th>
                      <th>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {carduri.map((card) => (
                      <tr key={card.id}>
                        <td>{card.id}</td>
                        <td>{card.nivel}</td>
                        <td>{card.puncte}</td>
                        <td>
                          {card.client
                            ? `${card.client.nume} ${card.client.prenume}`
                            : "-"}
                        </td>
                        <td>
                          <div className="card-page__actions">
                            <Link
                              to={`/carduri/edit/${card.id}`}
                              className="card-page__action-btn card-page__action-btn--edit"
                            >
                              Edit
                            </Link>

                            <button
                              onClick={() => handleDelete(card.id)}
                              className="card-page__action-btn card-page__action-btn--delete"
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

export default CardFidelitateList;