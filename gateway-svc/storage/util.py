import json

import pika


def upload(f, fs, channel, access):
    try:
        fid = fs.put(f)
    except Exception as e:
        print(e)
        print("error uploading file")
        return "internal server error", 500

    message = {
        "video_id": str(fid),
        "mp3_fid": None,
        "username": access["sub"],
    }

    try:
        channel.basic_publish(
            exchange="",
            routing_key="video",
            body=json.dumps(message),
            properties=pika.BasicProperties(
                delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE
            ),
        )
        return None
    except Exception as e:
        print(e)
        print("error publishing message")
        fs.delete(fid)
        return "internal server error", 500
