"use strict";

const Promise = require("bluebird");
const mosca = require("mosca");
const redis = require("redis");
const kafka = require("kafka-node");
const VehicleMessage = require("./lib/vehicle-message");
const logger = require("log4js").getLogger("index");

const redisHost = process.env.REDIS_HOST || "192.168.99.100";
const zkUri = process.env.ZK_URI || "192.168.99.100:2181";

Promise.promisifyAll(redis.RedisClient.prototype);
const redisClient = redis.createClient(6379, redisHost);

const kafkaProducer = Promise.promisifyAll(
  new kafka.HighLevelProducer(new kafka.Client(zkUri, "vehicle-broker")));

kafkaProducer.on("ready", () => {
  logger.info("kafka producer ready");
});

kafkaProducer.on("error", err => {
  logger.info(err);
});

const server = new mosca.Server({
  port: 1883,
  backend: {
    type: "redis",
    redis: redis,
    port: 6379,
    return_buffers: true,
    host: redisHost
  }
});

server.on("clientConnected", client => {
  logger.info("client connected", client.id);
});

server.on("published", (packet, client) => {
  logger.info("Published", JSON.stringify(packet));
  const msg = VehicleMessage.from(packet);
  if (msg !== null) {
    redisClient.getAsync(msg.vehicleId).then(json => {
      const latest = JSON.parse(json);
      return kafkaProducer.sendAsync([{
        topic: "vehicle",
        messages: JSON.stringify([latest, msg])
      }]);
    }).then(res => {
      logger.info("Kafka produced", res);
      return redisClient.setAsync(msg.vehicleId, JSON.stringify(msg));
    }).catch(err => {
      logger.error(err);
    });
  }
});

server.on("ready", () => {
  logger.info("Mosca server is up and running");
});
