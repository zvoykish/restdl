Restdl
======
Restdl is a multi-purpose "WSDL for the REST services".
It allows automated generation of a Restdl (WSDL-like metadata for your REST controllers) by simply adding it as a dependency to your project.
Apart from generating a JSON output that describes your REST controllers - a restdl2code module allows automatic generation of client code to consume it.
The project currently works with services based on Spring Web MVC, and generate code to be used with Spring Web, but more to come...

Major use cases, for now:
1. Quick detection of API changes (and possible broken integrations) by comparing the Restdl MD5 value.
2. Quick boiler-plating of simple SDK code to consume the RESTful services. Can be used for automated tests or even as a public SDK.

*NOTE* The project is currently in-development and will be updated frequently.
The above functionality is not 100% working, but feel free to watch the project and get updates when it's ready for use.
Feel free to contribute...
