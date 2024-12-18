import json
import os

from flask import Flask, request, make_response
from flask_pymongo import PyMongo
import gridfs
import pika

from auth import validate
from auth_svc import access
from storage import util

server = Flask(__name__)
server.config["MONGO_URI"] = os.environ.get("MONGO_URI")

mongo = PyMongo(server)

fs = gridfs.GridFS(mongo.db)
# this rabbitmq string is referencing our rabbitmq host, resolved in kubernetes
connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ.get('RABBITMQ_SVC_NAME') or 'localhost'))
channel = connection.channel()


@server.route("/login", methods=["POST"])
def login():
    token, err = access.login(request)
    response = make_response()
    if not err:
        response = make_response(token, 200)
    else:
        return err
    response.headers["Content-Type"] = "application/json"
    return response

@server.route("/upload", methods=["POST"])
def upload():
    access, err = validate.token(request)
    access = json.loads(access)

    if access["admin"]:
        if len(request.files) > 1 or len(request.files) < 1:
            return "exactly 1 file required", 400

        for _, file in request.files.items():
            err = util.upload(file, fs, channel, access)
            if err:
                return err
        return "sucess!", 200
    else:
        return "not authorized", 401


@server.route("/download", methods=["GET"])
def download():
    pass


if __name__ == "__main__":
    server.run(
        host="0.0.0.0",
        port=8080,
    )
