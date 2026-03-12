import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteClient, getClients } from "../../services/clientService";
import "./ClientList.css";

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
   <div className="client-page">
     <div className="client-page__content">
       <div className="client-page__topbar">
         <div>
           <p className="client-page__subtitle">Administrare clienți</p>
           <h1 className="client-page__title">Clienți</h1>
         </div>

         <Link to="/clients/new" className="client-page__add-btn">
           + Adaugă client
         </Link>
       </div>

       {errorMessage && (
         <div className="client-page__alert client-page__alert--error">
           {errorMessage}
         </div>
       )}

       <div className="client-page__card">
         {loading ? (
           <div className="client-page__state">Se încarcă...</div>
         ) : clients.length === 0 ? (
           <div className="client-page__state">Nu există clienți.</div>
         ) : (
           <div className="client-page__table-wrapper">
             <table className="client-page__table">
               <thead>
                 <tr>
                   <th>ID</th>
                   <th>CNP</th>
                   <th>Nume</th>
                   <th>Prenume</th>
                   <th>Vârstă</th>
                   <th>Telefon</th>
                   <th>Acțiuni</th>
                 </tr>
               </thead>
               <tbody>
                 {clients.map((client) => (
                   <tr key={client.id}>
                     <td>{client.id}</td>
                     <td>{client.cnp}</td>
                     <td>{client.nume}</td>
                     <td>{client.prenume}</td>
                     <td>{client.varsta}</td>
                     <td>{client.telefon}</td>
                     <td>
                       <div className="client-page__actions">
                         <Link
                           to={`/clients/edit/${client.id}`}
                           className="client-page__action-btn client-page__action-btn--edit"
                         >
                           Edit
                         </Link>

                         <button
                           onClick={() => handleDelete(client.id)}
                           className="client-page__action-btn client-page__action-btn--delete"
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

export default ClientList;