### What is a domain?

Domains provide *logical isolation* between the things. Only those things which belong to the same domain are allowed to communicate with one another.

### What are the different types of things?

Things can be of one of the following types:

- **Master**: can talk to everything.
- **Processor**: Can talk to Controller, Actuator and Sensor.
- **Controller**: Can talk to Actuator.
- **Actuator**
- **Sensor**

These access control rules apply on top of the domain controlled access.
