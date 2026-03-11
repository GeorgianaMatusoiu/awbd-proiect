import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteClient, getClients } from "../../services/clientService";

function ClientList() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const loadClients = async () => {
    try {
      setLoading(true);
      setErrorMessage("");
      const response = await getClients();
      setClients(response.data);
    } catch (error) {
      console.error("Eroare la încărcarea clienților:", error);
      setErrorMessage("Nu s-au putut încărca clienții.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadClients();
  }, []);

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Sigur vrei să ștergi acest client?");
    if (!confirmed) return;

    try {
      await deleteClient(id);
      await loadClients();
    } catch (error) {
      console.error("Eroare la ștergere:", error);
      setErrorMessage("Clientul nu a putut fi șters.");
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.header}>
        <h1>Clienți</h1>
        <Link to="/clients/new" style={styles.addButton}>
          Adaugă client
        </Link>
      </div>

      {errorMessage && <div style={styles.errorBox}>{errorMessage}</div>}

      {loading ? (
        <p>Se încarcă...</p>
      ) : clients.length === 0 ? (
        <p>Nu există clienți.</p>
      ) : (
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>CNP</th>
              <th style={styles.th}>Nume</th>
              <th style={styles.th}>Prenume</th>
              <th style={styles.th}>Vârstă</th>
              <th style={styles.th}>Telefon</th>
              <th style={styles.th}>Acțiuni</th>
            </tr>
          </thead>
          <tbody>
            {clients.map((client) => (
              <tr key={client.id}>
                <td style={styles.td}>{client.id}</td>
                <td style={styles.td}>{client.cnp}</td>
                <td style={styles.td}>{client.nume}</td>
                <td style={styles.td}>{client.prenume}</td>
                <td style={styles.td}>{client.varsta}</td>
                <td style={styles.td}>{client.telefon}</td>
                <td style={styles.td}>
                  <div style={styles.actions}>
                    <Link
                      to={`/clients/edit/${client.id}`}
                      style={styles.editButton}
                    >
                      Edit
                    </Link>

                    <button
                      onClick={() => handleDelete(client.id)}
                      style={styles.deleteButton}
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
  );
}

const styles = {
  container: {
    maxWidth: "1100px",
    margin: "30px auto",
    padding: "20px",
    fontFamily: "Arial, sans-serif",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "20px",
  },
  addButton: {
    textDecoration: "none",
    backgroundColor: "#1976d2",
    color: "#fff",
    padding: "10px 14px",
    borderRadius: "6px",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    backgroundColor: "#fff",
  },
  th: {
    border: "1px solid #ddd",
    padding: "10px",
    backgroundColor: "#f5f5f5",
    textAlign: "left",
  },
  td: {
    border: "1px solid #ddd",
    padding: "10px",
  },
  actions: {
    display: "flex",
    gap: "8px",
  },
  editButton: {
    textDecoration: "none",
    backgroundColor: "#2e7d32",
    color: "#fff",
    padding: "8px 12px",
    borderRadius: "6px",
  },
  deleteButton: {
    border: "none",
    backgroundColor: "#d32f2f",
    color: "#fff",
    padding: "8px 12px",
    borderRadius: "6px",
    cursor: "pointer",
  },
  errorBox: {
    backgroundColor: "#ffebee",
    color: "#b71c1c",
    padding: "12px",
    borderRadius: "6px",
    marginBottom: "16px",
  },
};

export default ClientList;