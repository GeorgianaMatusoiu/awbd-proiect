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

  const loadFarmacisti = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getFarmacisti();
      setFarmacisti(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea farmaciștilor:", error);
      setErrorMessage("Nu s-au putut încărca farmaciștii.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFarmacisti();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest farmacist?");
    if (!confirmed) return;

    try {
      await deleteFarmacist(id);
      await loadFarmacisti();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Farmacistul nu a putut fi șters.");
    }
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
          )}
        </div>
      </div>
    </div>
  );
}

export default FarmacistList;