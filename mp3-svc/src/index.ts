import * as express from 'express';

const app = express();
const port = 3000;

app.get('/token', (req, res) => {
  const authorization = req.headers.authorization;
  res.send('Hello World!');
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
