# Faceit

Local dev commands from the repo root:

- Frontend: `npm run dev`
- Backend: `npm run backend:dev`

## Backend deploy to DigitalOcean droplet

Files added for easier backend deployment:

- `docker-compose.backend.yml`
- `.env.backend.example`
- `scripts/deploy-backend.sh`

On the droplet, from the repo root:

```bash
cp .env.backend.example .env.backend
nano .env.backend
bash scripts/deploy-backend.sh
```

Required value in `.env.backend`:

- `FACEIT_API_KEY`

Optional values:

- `BACKEND_PORT` defaults to `8080`
- `FACEIT_API_V1_URL` defaults to `https://www.faceit.com`
- `FACEIT_API_V4_URL` defaults to `https://open.faceit.com`

After that, each update is just:

```bash
bash scripts/deploy-backend.sh
```