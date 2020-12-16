# PSA

[![Build Status](https://dev.azure.com/billedtrain380/PSA/_apis/build/status/PSA%20Github?branchName=master)](https://dev.azure.com/billedtrain380/PSA/_build/latest?definitionId=3&branchName=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BilledTrain380_sporttag-psa&metric=alert_status)](https://sonarcloud.io/dashboard?id=BilledTrain380_sporttag-psa)
[![Board Status](https://dev.azure.com/billedtrain380/62722b26-33c1-4ccb-aad4-d94161fdb57c/0e27f07f-6c5e-46b0-990f-9c47a5ca7b7c/_apis/work/boardbadge/18c16f81-2a06-481b-a930-e61fcedfcc6c)](https://dev.azure.com/billedtrain380/62722b26-33c1-4ccb-aad4-d94161fdb57c/_boards/board/t/0e27f07f-6c5e-46b0-990f-9c47a5ca7b7c/Microsoft.RequirementCategory/)

PSA is a sport event management system.

More info will be available on: https://billedtrain380.github.io/sporttag-psa/

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* [Gradle](https://gradle.org/) At least 6.7
* [JDK](https://adoptopenjdk.net/) At least 11
* [NodeJS](https://nodejs.org/en/) At least 14.15 LTS

### Installing

Clone the repository

```
git clone https://github.com/BilledTrain380/sporttag-psa.git
cd sporttag-psa
```

Run the application

```
./gradlew bootRun
```

## Running the tests

```
./gradlew test
```

## Deployment

Get the latest version of [PSA](https://billedtrain380.github.io/sporttag-psa/pages/downloads).

PSA is a standalone application. It uses the operating system depending application support directory
to store its data.

### Run as docker

PSA is available as docker image: https://hub.docker.com/r/billedtrain380/psa

### Run standalone

PSA can be run as standalone application: https://billedtrain380.github.io/sporttag-psa/pages/downloads

## Roadmap

Interest where PSA is going? Look at the roadmap.

https://dev.azure.com/billedtrain380/PSA/_boards/board/t/PSA%20Team/Stories

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Spring](https://spring.io/) - Server Framework used
* [Angular](https://angular.io/) - Frontend Framework used
* [H2](http://www.h2database.com/html/main.html) - Database Engine used

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/BilledTrain380/sporttag-psa/tags). 

## Authors

* **Nicolas Märchy** - *Initial work* - [BilledTrain380](https://github.com/BilledTrain380)

See also the list of [contributors](https://github.com/BilledTrain380/sporttag-psa/graphs/contributors) who participated in this project.

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE.md](LICENSE.md) file for details
