"use strict";

function validate(obj) {
  try {
    const payload = JSON.parse(obj.payload.toString());
    return /^vehicle\/[a-zA-Z0-9-]+/.test(obj.topic) &&
      typeof payload.timestamp === "string" &&
      JSON.stringify(new Date(payload.timestamp)) !== "null" &&
      typeof payload.longitude === "number" &&
      typeof payload.latitude === "number"
  } catch (err) {
    return false;
  }
}

class VehicleMessage {
  constructor(obj) {
    const tokens = obj.topic.split("/");
    const payload = JSON.parse(obj.payload.toString());
    this.vehicleId = tokens[1];
    this.timestamp = payload.timestamp;
    this.longitude = payload.longitude;
    this.latitude = payload.latitude;
  }
}

module.exports = {
  from: obj => {
    if (!validate(obj)) return null;
    else return new VehicleMessage(obj)
  },
  validate: validate
};
