package com.stackfull.workshop.monitor.mock;

import com.stackfull.workshop.monitor.model.DeviceInfo;
import com.stackfull.workshop.monitor.model.DeviceInfoSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class MockDeviceInfoSource implements DeviceInfoSource {

    private List<Consumer<DeviceInfo>> consumers = new ArrayList<>();

    @Override
    public void subscribe(Consumer<DeviceInfo> consumer) {
        consumers.add(consumer);

        ExecutorService exec = Executors.newWorkStealingPool(3);

        RandomChoice powers = new RandomChoice(new String[]{"offline", "online", "sleep", "rebooting"});
        RandomChoice firmware = new RandomChoice(new String[]{"v1", "v1.1", "v2dev", "v2.0"});

        Arrays.stream(new String[]{"1", "2", "3"}).forEach((name) -> {
            exec.submit(() -> {
                Random rand = new Random(currentThread().getId() + currentTimeMillis());
                while (true) {
                    try {
                        consumers.forEach(c -> {
                            c.accept(DeviceInfo.builder()
                                .id(String.format("mock-device-%s", name))
                                .deviceClass("mock")
                                .description("Mock device")
                                .firmware(firmware.pick())
                                .ip(String.format("192.168.%s.%s", name, name))
                                .mac("ab:ab:ab:ab:ab:ab:ab:ab")
                                .power(powers.pick())
                                .build());
                        });
                        sleep(rand.nextInt(5000) + 2000);
                    } catch (InterruptedException ex) {
                        Thread.interrupted();
                        return;
                    }

                }

            });

        });
    }

    @Override
    public void unsubscribe(Consumer<DeviceInfo> c) {
        consumers.remove(c);

    }
}
