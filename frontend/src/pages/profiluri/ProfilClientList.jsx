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

  const loadProfiluri = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getProfiluri();
      setProfiluri(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea profilurilor:", error);
      setErrorMessage("Nu s-au putut încărca profilurile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfiluri();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest profil?");
    if (!confirmed) return;

    try {
      await deleteProfil(id);
      await loadProfiluri();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Profilul nu a putut fi șters.");
    }
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
          )}
        </div>
      </div>
    </div>
  );
}

export default ProfilClientList;