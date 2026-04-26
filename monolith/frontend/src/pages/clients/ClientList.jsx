import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteClient, getClients } from "../../services/clientService";
import "./ClientList.css";

function ClientList() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("nume");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadClients = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getClients({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setClients(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error("Eroare la încărcarea clienților:", error);
      setErrorMessage("Nu s-au putut încărca clienții.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadClients(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest client?");
    if (!confirmed) return;

    try {
      await deleteClient(id);
      await loadClients(page, size);
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Clientul nu a putut fi șters.");
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
    <div className="client-page">
      <div className="client-page__content">
        <div className="client-page__topbar">
          <div>
            <p className="client-page__subtitle">Administrare clienți</p>
            <h1 className="client-page__title">Clienți</h1>
          </div>

          <Link to="/clients/new" className="client-page__add-btn">
            + Adaugă client
          </Link>
        </div>

        {errorMessage && (
          <div className="client-page__alert client-page__alert--error">
            {errorMessage}
          </div>
        )}

        <div className="client-page__card">
          {loading ? (
            <div className="client-page__state">Se încarcă...</div>
          ) : clients.length === 0 ? (
            <div className="client-page__state">Nu există clienți.</div>
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
                    <option value="varsta">Vârstă</option>
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

              <div className="client-page__table-wrapper">
                <table className="client-page__table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>CNP</th>
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th>Vârstă</th>
                      <th>Telefon</th>
                      <th>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {clients.map((client) => (
                      <tr key={client.id}>
                        <td>{client.id}</td>
                        <td>{client.cnp}</td>
                        <td>{client.nume}</td>
                        <td>{client.prenume}</td>
                        <td>{client.varsta}</td>
                        <td>{client.telefon}</td>
                        <td>
                          <div className="client-page__actions">
                            <Link
                              to={`/clients/edit/${client.id}`}
                              className="client-page__action-btn client-page__action-btn--edit"
                            >
                              Edit
                            </Link>

                            <button
                              onClick={() => handleDelete(client.id)}
                              className="client-page__action-btn client-page__action-btn--delete"
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

export default ClientList;