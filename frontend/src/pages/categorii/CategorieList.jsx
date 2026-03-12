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

  const loadCategorii = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getCategoriiMedicamente();
      setCategorii(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea categoriilor:", error);
      setErrorMessage("Nu s-au putut încărca categoriile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCategorii();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi această categorie?");
    if (!confirmed) return;

    try {
      await deleteCategorieMedicament(id);
      await loadCategorii();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Categoria nu a putut fi ștearsă.");
    }
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
          )}
        </div>
      </div>
    </div>
  );
}

export default CategorieList;