"use strict";

const assert = require("assert");
const Message = require("../lib/vehicle-message");

describe("vehicle-message", () => {
  it("get MQTT packet and return VehicleMessage data model", () => {
    const msg = Message.from({
      topic: "vehicle/foo",
      payload: new Buffer(JSON.stringify({
        timestamp: "2016-04-30T05:41:08.915Z",
        longitude: 37.481200,
        latitude: 126.948004
      }))
    });

    assert.strictEqual(msg.vehicleId, "foo");
    assert.strictEqual(msg.timestamp, "2016-04-30T05:41:08.915Z");
    assert.strictEqual(msg.longitude, 37.481200);
    assert.strictEqual(msg.latitude, 126.948004);
  });

  it("validate MQTT packet to create VehicleMessage data model", () => {
    const bool = Message.validate({
      topic: "vehicle/foo",
      payload: new Buffer(JSON.stringify({
        timestamp: "invalid date",
        longitude: 37.481200,
        latitude: 126.948004
      }))
    });

    assert.strictEqual(bool, false);
  });

  it("get MQTT packet and return null if it can't be compatible to VehicleMessage", () => {
    const msg = Message.from({
      topic: "vehicle/foo",
      payload: new Buffer(JSON.stringify({
        timestamp: "invalid date",
        longitude: 37.481200,
        latitude: 126.948004
      }))
    });

    assert.strictEqual(msg, null);
  });

  it("JSON.stringify and parse", () => {
    const msg = Message.from({
      topic: "vehicle/foo",
      payload: new Buffer(JSON.stringify({
        timestamp: "2016-04-30T05:41:08.915Z",
        longitude: 37.481200,
        latitude: 126.948004
      }))
    });

    assert.deepEqual(JSON.parse(JSON.stringify(msg)), {
      vehicleId: "foo",
      timestamp: "2016-04-30T05:41:08.915Z",
      longitude: 37.481200,
      latitude: 126.948004
    });
  });
});