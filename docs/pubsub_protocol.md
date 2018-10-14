# PubSub Protocol

MQTT is the base pubsub protocol used in the workshop monitor. Devices in the workshop (and further afield) use it to send status updates and receive commands. By itself, MQTT is a framework for messaging in topics. This document describes the topic convention used by the devices and interpreted by the monitor.

## Foundations

The topics are assigned to devices in a physical hierarchy starting with the class of device. Devices of the same class receive the same firmware and are considered interchangeable. The device class topic is defined as:

    /device-class/{class-id}
 
Devices know their own class name and an identifier unique among devices of this class. The device topic is defined as:
 
    /device-class/{class-id}/device/{device-id}
    
Devices have **properties** and **state** ([cf. React.js](https://reactjs.org/)) which are the supplied control parameters and the operating attributes respectively. Properties are set by controlling applications and confirmed by the device in status updates. State is published by the device alone.

## Status Updates

### Device Information

The device topic is used to publish metadata about the device instance each time the device comes online or changes operating state. The message format is JSON encoding the following properties

    /device-class/{class-id}/device/{device-id}
    
    {
      "id": "abc-1234",
      "class": "ABCmkII",
      "description": "optional descriptive text",
      "firmware": "ABCrev2-1.2.3",
      "power": "online",
      "ip": "192.168.0.123",
      "mac": "fe:fe:fe:fe:fe:fe"
    }

Devices can add any other properties to the device information message.

|Property     |Notes|
|-------------|-------|
|`id`         | Must match the `device-id`. Uniquely identifies the device within its device class.|
|`class`      | Must match the `device-class`. Uniquely identifies the class of device within the network.|
|`description`| Optional descriptive text for the device instance or class.|
|`firmware`   | Identifier for the current installed firmware. |
|`power`      | One of: `online` `offline` `sleep`|
|`ip`         | IP address |
|`mac`        | MAC address |

### Device Properties

The current values of all controllable properties are published to the `properties` subtopic by the device. 

### Device Status Updates

Status messages are sent by the device to a sub topic of the device. The sub-topics used and the message format depend on the capabilities of the device class and can take any form that does not conflict with control messages (below). This protocol does not provide for auto-discovery by publishing message schema etc. More expansive protocols/conventions are available [e.g. Homie](https://github.com/homieiot/convention)

*Example:*

    /device-class/{class-id}/device/{device-id}/


## Control

Control messages are sent to sub-topics of the relevant device or device class topic. The content of the message depends on the control message. The suite of control messages accepted by devices depends on the current firmware of the device class. The following controls are universal:

### Device Class Controls

The location of the latest firmware for devices is broadcast to the following topic:

    /device-class/{class-id}/firmware
    
The content of the message depends on the update capabilities of the device, but in most cases this will be a URL pointing to a OTA update server.

#### ping

Devices can be asked to check-in by sending any message to the topic:

    /device-class/{class-id}/ping

Each device of that class should then return the same message in the topic:

    /device-class/{class-id}/device/{device-id}/pong

### Device Controls


    /device-class/{class-id}/device/{device-id}/control/update-rate
    
