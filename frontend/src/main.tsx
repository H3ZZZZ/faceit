// src/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App.tsx';
import './index.css';
import { PlayerDataProvider } from './context/PlayerDataContext';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <PlayerDataProvider>
        <App />
      </PlayerDataProvider>
    </BrowserRouter>
  </React.StrictMode>
);

