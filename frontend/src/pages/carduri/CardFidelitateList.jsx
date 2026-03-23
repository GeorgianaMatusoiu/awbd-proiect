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

  const loadCarduri = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getCarduri();
      setCarduri(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea cardurilor:", error);
      setErrorMessage("Nu s-au putut încărca cardurile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCarduri();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest card?");
    if (!confirmed) return;

    try {
      await deleteCard(id);
      await loadCarduri();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Cardul nu a putut fi șters.");
    }
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
          )}
        </div>
      </div>
    </div>
  );
}

export default CardFidelitateList;