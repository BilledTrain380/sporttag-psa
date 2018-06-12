# Sporttag PSA

Sporttag PSA is a tournament management system highly customized for [Primarschule Altendorf](https://www.schule-altendorf.ch/).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* [Gradle](https://gradle.org/) 4.6+
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8+

### Installing

Clone the repository

```
git clone https://github.com/BilledTrain380/sporttag-psa.git
cd sporttag-psa
```

Run the application

```
gradle bootRun
```

## Running the tests

```
gradle junitPlatformTest
```

## Deployment

Get the latest version of Sporttag PSA (currently not available as download).

### Run the macOS app

Sporttag PSA can be delivered as an macOS app. Just double click the .app file
on a macOS system. The jre is bundled inside the .app.

### Run as jar

To run Sporttag PSA as jar, you'll need Java 1.8 or higher installed.
Then simply execute the jar file.

```
java -jar sporttag-psa.jar
```

> Sporttag PSA will use the system depending application support directory
> to store its data.

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Spring](https://spring.io/) - Web Framework used
* [H2](http://www.h2database.com/html/main.html) - Database Engine used

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/BilledTrain380/sporttag-psa/tags). 

## Authors

* **Nicolas Märchy** - *Initial work* - [BilledTrain380](https://github.com/BilledTrain380)

See also the list of [contributors](https://github.com/BilledTrain380/sporttag-psa/graphs/contributors) who participated in this project.

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE.md](LICENSE.md) file for details
