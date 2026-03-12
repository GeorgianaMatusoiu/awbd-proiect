import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar__container">
        <div className="navbar__logo">
          <Link to="/">Farmacie</Link>
        </div>

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
        </div>
      </div>
    </nav>
  );
}

export default Navbar;