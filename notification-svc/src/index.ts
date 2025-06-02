import * as nodemailer from "nodemailer";
import * as dotenv from "dotenv";
import { Connection } from "rabbitmq-client";

dotenv.config({ path: "../../.env" });

const transporter = nodemailer.createTransport({
  host: "live.smtp.mailtrap.io",
  port: 587,
  auth: {
    user: process.env.SMTP_USER,
    pass: process.env.SMTP_PASS,
  },
});

const rabbitMqUrl = `amqp://${process.env.RABBITMQ_USER}:${process.env.RABBITMQ_PASSWORD}@${process.env.RABBITMQ_HOST}:${process.env.RABBITMQ_PORT}`;

const rabbit = new Connection(rabbitMqUrl);

rabbit.on("error", (err) => {
  console.log("RabbitMQ connection error", err);
});

rabbit.on("connection", () => {
  console.log("Connection successfully (re)established");
});

const sub = rabbit.createConsumer(
  {
    queue: "mp3",
    queueOptions: { durable: true },
  },
  async (msg) => {
    console.log("received message (user-events)", msg);
    console.log("msg body", msg.body.toString());

    transporter.sendMail(
      {
        from: "hello@demomailtrap.com",
        to: "testmail200087@gmail.com",
        subject: `Your MP3 file is ready! Mp3Id: ${msg.body.toString()}`,
        text: "I hope this message gets buffered! Your mp3 file is ready! This is the id of your mp3 file: " + msg.body.toString(),
      },
      (_, info) => {
        console.log("info.envelope", info.envelope);
        console.log("info.messageId",info.messageId);
      },
    );
  },
);

sub.on("error", (err) => {
  console.log("consumer error (user-events)", err);
});
