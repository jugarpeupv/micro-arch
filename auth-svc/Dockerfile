FROM alpine:3.14

RUN apk add --update nodejs npm
RUN apk add --no-cache bash

WORKDIR /app

COPY ./src/ /app

RUN npm ci
RUN npm run build

EXPOSE 3000

CMD ["node", "index.js"]

