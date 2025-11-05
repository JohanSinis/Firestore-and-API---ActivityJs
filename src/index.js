import express from 'express';
import cors from 'cors';
import recordsRouter from './routes/records.js';

const app = express();

// Middleware
const corsOptions = {
  origin: '*',
  methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization'],
  credentials: false
};
app.use(cors(corsOptions));
app.options('*', cors(corsOptions));
app.use(express.json());

// Healthcheck
app.get('/health', (req, res) => {
  res.json({ status: 'ok' });
});

// Routes
app.use('/api/records', recordsRouter);

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Firestore Activity API iniciada en http://localhost:${PORT}`);
  console.log(`📝 Autor: Johan Stiven Sinisterra - 407388`);
  console.log(`📍 Endpoints disponibles: GET/POST /api/records`);
});



