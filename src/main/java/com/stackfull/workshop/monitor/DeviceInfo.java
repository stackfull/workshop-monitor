package com.stackfull.workshop.monitor;

import lombok.Builder;
import lombok.Data;
import processing.data.JSONObject;

@Data
@Builder
public
class DeviceInfo {
    public static final String UNKNOWN = "UNKNOWN";

    public final String id;
    public final String deviceClass;
    public final String description;
    public final String firmware;
    public final String power;
    public final String ip;
    public final String mac;

    public static DeviceInfo fromJSON(JSONObject obj) {
        return builder()
            .id(obj.getString("id", UNKNOWN))
            .deviceClass(obj.getString("class", UNKNOWN))
            .description(obj.getString("description", UNKNOWN))
            .firmware(obj.getString("firmware", UNKNOWN))
            .power(obj.getString("power", UNKNOWN))
            .ip(obj.getString("ip", UNKNOWN))
            .mac(obj.getString("mac", UNKNOWN))
            .build();
    }
}
