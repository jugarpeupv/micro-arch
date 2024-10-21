import pika, json


def upload(file, fs, channel, access):
    try:
        file_id = fs.put(file)
        channel.basic_publish(
            exchange="",
            routing_key="storage",
            body=json.dumps(
                {
                    "file_id": str(file_id),
                    "access": access
                }
            )
        )
        return None
    except Exception as e:
        return str(e), 500
