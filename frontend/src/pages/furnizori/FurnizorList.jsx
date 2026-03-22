import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  deleteFurnizor,
  getFurnizori,
} from "../../services/furnizorService";
import "./Furnizor.css";

function FurnizorList() {
  const [furnizori, setFurnizori] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [sortField, setSortField] = useState("nume");
  const [sortDirection, setSortDirection] = useState("asc");

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  const loadFurnizori = async (currentPage = page, currentSize = size) => {
    try {
      setLoading(true);
      setErrorMessage("");

      const response = await getFurnizori({
        page: currentPage,
        size: currentSize,
        sort: `${sortField},${sortDirection}`,
      });

      setFurnizori(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(response.data.number);
    } catch (error) {
      console.error(error);
      setErrorMessage("Nu s-au putut încărca furnizorii.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFurnizori(page, size);
  }, [page, size, sortField, sortDirection]);

  const handleDelete = async (id) => {
    if (!window.confirm("Sigur vrei să ștergi acest furnizor?")) return;

    try {
      await deleteFurnizor(id);
      await loadFurnizori(page, size);
    } catch (error) {
      console.error(error);
      setErrorMessage("Furnizorul nu a putut fi șters.");
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
    <div className="page">
      <div className="page-container">
        <div className="page-header">
          <h1>Furnizori</h1>

          <Link to="/furnizori/new" className="btn-primary">
            + Adaugă furnizor
          </Link>
        </div>

        {errorMessage && <div className="alert">{errorMessage}</div>}

        <div className="card">
          {loading ? (
            <p>Se încarcă...</p>
          ) : furnizori.length === 0 ? (
            <p>Nu există furnizori.</p>
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
                    <option value="oras">Oraș</option>
                    <option value="tara">Țară</option>
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

              <table className="table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nume</th>
                    <th>Adresa</th>
                    <th>Oras</th>
                    <th>Tara</th>
                    <th>Telefon</th>
                    <th>Acțiuni</th>
                  </tr>
                </thead>

                <tbody>
                  {furnizori.map((furnizor) => (
                    <tr key={furnizor.id}>
                      <td>{furnizor.id}</td>
                      <td>{furnizor.nume}</td>
                      <td>{furnizor.adresa}</td>
                      <td>{furnizor.oras}</td>
                      <td>{furnizor.tara}</td>
                      <td>{furnizor.telefon}</td>

                      <td>
                        <div className="actions">
                          <Link
                            to={`/furnizori/edit/${furnizor.id}`}
                            className="btn-edit"
                          >
                            Edit
                          </Link>

                          <button
                            onClick={() => handleDelete(furnizor.id)}
                            className="btn-delete"
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

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

export default FurnizorList;