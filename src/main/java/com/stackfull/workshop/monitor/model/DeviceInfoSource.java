package com.stackfull.workshop.monitor.model;

import java.util.function.Consumer;

public interface DeviceInfoSource {

    void subscribe(Consumer<DeviceInfo> consumer);

    void unsubscribe(Consumer<DeviceInfo> c);
}
