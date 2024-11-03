FROM python:3.10-slim-bullseye

ENV MONGO_URI=mongodb://127.0.0.1:27017/videos

RUN apt-get update \
  && apt-get install -y --no-install-recommends --no-install-suggests \
  build-essential \
  && pip install --no-cache-dir --upgrade pip

WORKDIR /app 
COPY ./requirements.txt /app
RUN pip install --no-cache-dir --requirement /app/requirements.txt
COPY . /app

EXPOSE 8080

CMD [ "python3", "server.py" ]
