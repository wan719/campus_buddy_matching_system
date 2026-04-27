import http from 'http';
import fs from 'fs/promises';
import fsSync from 'fs';
import path from 'path';
import { WebSocketServer } from 'ws';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const publicDir = path.join(__dirname, 'public');
const dataDir = path.join(__dirname, 'data');
const port = 3000;

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
};

const hotReloadClient = `
<script>
(() => {
  const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
  const socket = new WebSocket(protocol + '://' + location.host);
  socket.onmessage = (event) => {
    if (event.data === 'reload') location.reload();
  };
})();
</script>
`;

const resolveRequestPath = (urlPath) => {
  const cleanPath = decodeURIComponent(urlPath.split('?')[0]);
  const relativePath = cleanPath.replace(/^\/+/, '');
  if (cleanPath === '/') {
    return path.join(publicDir, 'index.html');
  }

  if (relativePath.startsWith('data/')) {
    return path.join(__dirname, relativePath);
  }

  const candidatePublic = path.join(publicDir, relativePath);
  const candidateData = path.join(__dirname, relativePath);

  const normalizedPublic = path.normalize(candidatePublic);
  const normalizedData = path.normalize(candidateData);

  if (normalizedPublic.startsWith(publicDir)) {
    return normalizedPublic;
  }

  if (normalizedData.startsWith(dataDir)) {
    return normalizedData;
  }

  return null;
};

const server = http.createServer(async (req, res) => {
  try {
    const filePath = resolveRequestPath(req.url || '/');
    if (!filePath) {
      res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('403 Forbidden');
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    const stat = await fs.stat(filePath).catch(() => null);
    if (!stat || !stat.isFile()) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('404 Not Found');
      return;
    }

    if (ext === '.html') {
      const html = await fs.readFile(filePath, 'utf8');
      const withHotReload = html.replace('</body>', `${hotReloadClient}</body>`);
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(withHotReload);
      return;
    }

    const content = await fs.readFile(filePath);
    res.writeHead(200, { 'Content-Type': contentType });
    res.end(content);
  } catch (error) {
    res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' });
    res.end(`500 Internal Server Error: ${error.message}`);
  }
});

const wss = new WebSocketServer({ server });

const broadcastReload = () => {
  for (const client of wss.clients) {
    if (client.readyState === 1) {
      client.send('reload');
    }
  }
};

const watchTargets = [publicDir, dataDir];
for (const watchTarget of watchTargets) {
  fsSync.watch(watchTarget, { recursive: true }, () => {
    broadcastReload();
  });
}

server.listen(port, () => {
  console.log(`开发服务器运行在: http://localhost:${port}`);
  console.log('已启用 WebSocket 热加载');
});
