import json

import pika


def upload(f, fs, channel, access):
    try:
        fid = fs.put(f)
    except Exception:
        return "internal server error", 500

    message = {
        "video_id": str(fid),
        "mp3_fid": None,
        "username": access["username"],
    }

    try:
        channel.basic_publish(
            exchange="",
            routing_key="video",
            body=json.dumps(message),
            properties=pika.BasicProperties(
                delivery_mode=pika.__spec__.PERSISTENT_DELIVERY_MODE
            ),
        )
        return None
    except:
        fs.delete(fid)
        return "internal server error", 500
