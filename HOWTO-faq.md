### Getting help.

- Create an [issue](https://github.com/wanhive/webconsole/issues) on the github.
- Send us an email [info@wanhive.com].

### Getting started.

It takes four steps to register an IoT endpoint (thing):

- **Log in**: the application has a browser-based user interface. Sign in to the application with your registered email and password.
- **Create a domain**: Creating a *domain* is easy. Click on the *Create* button and provide a name and a description for the new domain.
- **Create a thing**: Click on the *Manage things* button of the domain where your *thing* will reside. Click on the *Create* button and provide a meaningful name for the *thing*.
- **Secure the thing**: Click on the *Settings* button and create a password for the *thing*.

### What is a domain?

Domains provide *logical isolation* between the things. Only those things which belong to the same domain are allowed to communicate with one another.

### What are the different types of things?

Things can be of one of the following types:

- **Master**: can talk to everything (including another master).
- **Processor**: Can talk to master, controller, actuator and sensor.
- **Controller**: Can talk to master, processor, and actuator.
- **Actuator**: Can talk to master, processor, and controller.
- **Sensor**: Can talk to master and processor.

These access control rules apply on top of the domain controlled access.
