import { BrowserRouter, Routes, Route } from "react-router-dom";
import ClientList from "../pages/clients/ClientList";
import ClientForm from "../pages/clients/ClientForm";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ClientList />} />
        <Route path="/clients/new" element={<ClientForm />} />
        <Route path="/clients/edit/:id" element={<ClientForm />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;