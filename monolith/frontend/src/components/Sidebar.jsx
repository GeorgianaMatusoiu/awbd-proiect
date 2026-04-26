import { Link, useNavigate } from "react-router-dom";
import { getCurrentUser, logoutUser } from "../services/authService";
import "./Sidebar.css";

function Sidebar() {
  const navigate = useNavigate();
  const currentUser = getCurrentUser();

  const isAdmin = currentUser?.authorities?.some(
    (auth) => auth.authority === "ROLE_ADMIN"
  );

  const isUser = currentUser?.authorities?.some(
    (auth) => auth.authority === "ROLE_USER"
  );

  const handleLogout = async () => {
    try {
      await logoutUser();
      navigate("/login");
    } catch (error) {
      console.error("Logout error:", error);
    }
  };

  let roleLabel = "";
  if (isAdmin) {
    roleLabel = "Admin";
  } else if (isUser) {
    roleLabel = "User";
  }

  return (
    <aside className="sidebar">
      <div>
        <div className="sidebar__logo">Farmacie</div>

        {currentUser && (
          <div
            className={`sidebar__user-box ${
              isAdmin ? "sidebar__user-box--admin" : "sidebar__user-box--user"
            }`}
          >
            ROLE: <strong>{roleLabel}</strong>
          </div>
        )}

        <nav className="sidebar__menu">
          <Link to="/" className="sidebar__link">
            Clienți
          </Link>

          {isAdmin && (
            <Link to="/farmacisti" className="sidebar__link">
              Farmaciști
            </Link>
          )}

          {isAdmin && (
            <Link to="/furnizori" className="sidebar__link">
              Furnizori
            </Link>
          )}

          {isAdmin && (
            <Link to="/categorii" className="sidebar__link">
              Categorii
            </Link>
          )}

          <Link to="/prospecte" className="sidebar__link">
            Prospecte
          </Link>

          <Link to="/medicamente" className="sidebar__link">
            Medicamente
          </Link>

          <Link to="/retete" className="sidebar__link">
            Rețete
          </Link>

          <Link to="/carduri" className="sidebar__link">
            Carduri
          </Link>

          <Link to="/profiluri" className="sidebar__link">
            Profiluri
          </Link>

          <Link to="/detalii-retete" className="sidebar__link">
            Detalii rețete
          </Link>
        </nav>
      </div>

      {currentUser && (
        <button onClick={handleLogout} className="sidebar__logout">
          Logout
        </button>
      )}
    </aside>
  );
}

export default Sidebar;