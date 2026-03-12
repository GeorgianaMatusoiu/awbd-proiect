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

      </Routes>

    </BrowserRouter>
  );
}

export default AppRouter;