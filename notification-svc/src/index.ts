import * as nodemailer from "nodemailer";
import * as dotenv from "dotenv";
import { Connection } from "rabbitmq-client";

dotenv.config();

const transporter = nodemailer.createTransport({
  host: "live.smtp.mailtrap.io",
  port: 587,
  auth: {
    user: process.env.SMPT_USER,
    pass: process.env.SMPT_PASS,
  },
});

const rabbit = new Connection(
  `amqp://${process.env.RABBITMQ_USER}:${process.env.RABBITMQ_PASSWORD}@localhost:${process.env.RABBITMQ_PORT}`,
);

rabbit.on("error", (err) => {
  console.log("RabbitMQ connection error", err);
});

rabbit.on("connection", () => {
  console.log("Connection successfully (re)established");
});

const sub = rabbit.createConsumer(
  {
    queue: "mp3",
  },
  async (msg) => {
    console.log("received message (user-events)", msg);
    // The message is automatically acknowledged (BasicAck) when this function ends.
    // If this function throws an error, then msg is rejected (BasicNack) and
    // possibly requeued or sent to a dead-letter exchange. You can also return a
    // status code from this callback to control the ack/nack behavior
    // per-message.

    transporter.sendMail(
      {
        from: "hello@demomailtrap.com",
        to: "testmail200087@gmail.com",
        subject: "Message 6",
        text: "I hope this message gets buffered!",
      },
      (_, info) => {
        console.log(info.envelope);
        console.log(info.messageId);
        console.log(info.messageId.toString());
      },
    );
  },
);

sub.on("error", (err) => {
  // Maybe the consumer was cancelled, or the connection was reset before a
  // message could be acknowledged.
  console.log("consumer error (user-events)", err);
});
