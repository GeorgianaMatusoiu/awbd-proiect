import { Link, useNavigate } from "react-router-dom";
import { getCurrentUser, logoutUser } from "../services/authService";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();
  const user = getCurrentUser();

  const isAdmin = user?.authorities?.some(
    (auth) => auth.authority === "ROLE_ADMIN"
  );
  const isUser = user?.authorities?.some(
    (auth) => auth.authority === "ROLE_USER"
  );

  let roleLabel = "";
  if (isAdmin) {
    roleLabel = "Admin";
  } else if (isUser) {
    roleLabel = "User";
  }

  const handleLogout = async () => {
    try {
      await logoutUser();
      navigate("/login");
    } catch (error) {
      console.error("Eroare la logout:", error);
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar__container">
        <div className="navbar__logo">
          <Link to="/">Farmacie</Link>
        </div>

        {user && (
          <div
            className={`navbar__role ${
              isAdmin ? "navbar__role--admin" : "navbar__role--user"
            }`}
          >
            Conectat ca: <strong>{roleLabel}</strong>
          </div>
        )}

        <div className="navbar__links">
          <Link to="/" className="navbar__link">
            Clienți
          </Link>

          <Link to="/farmacisti" className="navbar__link">
            Farmaciști
          </Link>

          <Link to="/furnizori" className="navbar__link">
            Furnizori
          </Link>

          <Link to="/categorii" className="navbar__link">
            Categorii
          </Link>

          <Link to="/prospecte" className="navbar__link">
            Prospecte
          </Link>

          <Link to="/medicamente" className="navbar__link">
            Medicamente
          </Link>

          <Link to="/retete" className="navbar__link">
            Rețete
          </Link>

          <Link to="/carduri" className="navbar__link">
            Carduri
          </Link>

          <Link to="/profiluri" className="navbar__link">
            Profiluri
          </Link>

          <Link to="/detalii-retete" className="navbar__link">
            Detalii rețete
          </Link>

          {user && (
            <button onClick={handleLogout} className="navbar__logout">
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;