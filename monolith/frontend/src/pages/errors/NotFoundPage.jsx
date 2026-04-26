import { Link } from "react-router-dom";
import "./ErrorPages.css";

function NotFoundPage() {
  return (
    <div className="error-page">
      <div className="error-page__card">
        <p className="error-page__code">404</p>
        <h1 className="error-page__title">Pagina nu a fost găsită</h1>
        <p className="error-page__text">
          Ruta accesată nu există sau a fost mutată.
        </p>

        <div className="error-page__actions">
          <Link to="/" className="error-page__btn">
            Înapoi la pagina principală
          </Link>
        </div>
      </div>
    </div>
  );
}

export default NotFoundPage;