import { Link, useLocation } from "react-router-dom";
import "./ErrorPages.css";

function ServerErrorPage() {
  const location = useLocation();
  const message =
    location.state?.message || "A apărut o eroare internă neașteptată.";

  return (
    <div className="error-page">
      <div className="error-page__card">
        <p className="error-page__code">500</p>
        <h1 className="error-page__title">Eroare internă</h1>
        <p className="error-page__text">{message}</p>

        <div className="error-page__actions">
          <Link to="/" className="error-page__btn">
            Înapoi la pagina principală
          </Link>
        </div>
      </div>
    </div>
  );
}

export default ServerErrorPage;