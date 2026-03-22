import { BrowserRouter, Route, Routes } from "react-router-dom";
import Navbar from "../components/Navbar";

import ClientList from "../pages/clients/ClientList";
import ClientForm from "../pages/clients/ClientForm";
import CategorieList from "../pages/categorii/CategorieList";
import CategorieForm from "../pages/categorii/CategorieForm";
import FarmacistList from "../pages/farmacisti/FarmacistList";
import FarmacistForm from "../pages/farmacisti/FarmacistForm";
import FurnizorList from "../pages/furnizori/FurnizorList";
import FurnizorForm from "../pages/furnizori/FurnizorForm";
import MedicamentList from "../pages/medicamente/MedicamentList";
import MedicamentForm from "../pages/medicamente/MedicamentForm";
import ProspectList from "../pages/prospecte/ProspectList";
import ProspectForm from "../pages/prospecte/ProspectForm";
import CardFidelitateList from "../pages/carduri/CardFidelitateList";
import CardFidelitateForm from "../pages/carduri/CardFidelitateForm";
import ProfilClientList from "../pages/profiluri/ProfilClientList";
import ProfilClientForm from "../pages/profiluri/ProfilClientForm";
import RetetaList from "../pages/retete/RetetaList";
import RetetaForm from "../pages/retete/RetetaForm";
import DetaliiRetetaList from "../pages/detalii-retete/DetaliiRetetaList";
import DetaliiRetetaForm from "../pages/detalii-retete/DetaliiRetetaForm";
import NotFoundPage from "../pages/errors/NotFoundPage";
import ServerErrorPage from "../pages/errors/ServerErrorPage";

function AppRouter() {
  return (
    <BrowserRouter>

      <Navbar />

      <Routes>
        <Route path="/" element={<ClientList />} />
        <Route path="/clients/new" element={<ClientForm />} />
        <Route path="/clients/edit/:id" element={<ClientForm />} />

        <Route path="/farmacisti" element={<FarmacistList />} />
        <Route path="/farmacisti/new" element={<FarmacistForm />} />
        <Route path="/farmacisti/edit/:id" element={<FarmacistForm />} />

       <Route path="/furnizori" element={<FurnizorList />} />
       <Route path="/furnizori/new" element={<FurnizorForm />} />
       <Route path="/furnizori/edit/:id" element={<FurnizorForm />} />

       <Route path="/medicamente" element={<MedicamentList />} />
       <Route path="/medicamente/new" element={<MedicamentForm />} />
       <Route path="/medicamente/edit/:id" element={<MedicamentForm />} />

       <Route path="/categorii" element={<CategorieList />} />
       <Route path="/categorii/new" element={<CategorieForm />} />
       <Route path="/categorii/edit/:id" element={<CategorieForm />} />

       <Route path="/prospecte" element={<ProspectList />} />
       <Route path="/prospecte/new" element={<ProspectForm />} />
       <Route path="/prospecte/edit/:id" element={<ProspectForm />} />

       <Route path="/carduri" element={<CardFidelitateList />} />
       <Route path="/carduri/new" element={<CardFidelitateForm />} />
       <Route path="/carduri/edit/:id" element={<CardFidelitateForm />} />

       <Route path="/profiluri" element={<ProfilClientList />} />
       <Route path="/profiluri/new" element={<ProfilClientForm />} />
       <Route path="/profiluri/edit/:id" element={<ProfilClientForm />} />

       <Route path="/retete" element={<RetetaList />} />
       <Route path="/retete/new" element={<RetetaForm />} />
       <Route path="/retete/edit/:id" element={<RetetaForm />} />

       <Route path="/detalii-retete" element={<DetaliiRetetaList />} />
       <Route path="/detalii-retete/new" element={<DetaliiRetetaForm />} />
       <Route
         path="/detalii-retete/edit/:retetaId/:medicamentId"
         element={<DetaliiRetetaForm />}
       />

       <Route path="/error" element={<ServerErrorPage />} />
       <Route path="*" element={<NotFoundPage />} />

      </Routes>

    </BrowserRouter>
  );
}

export default AppRouter;