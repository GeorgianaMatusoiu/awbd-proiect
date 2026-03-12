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

  const loadFurnizori = async () => {
    try {
      const response = await getFurnizori();
      setFurnizori(response.data);
    } catch (error) {
      console.error(error);
      setErrorMessage("Nu s-au putut încărca furnizorii.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFurnizori();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Sigur vrei să ștergi acest furnizor?")) return;

    try {
      await deleteFurnizor(id);
      loadFurnizori();
    } catch (error) {
      console.error(error);
      setErrorMessage("Furnizorul nu a putut fi șters.");
    }
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
          )}

        </div>
      </div>
    </div>
  );
}

export default FurnizorList;