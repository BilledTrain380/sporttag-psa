# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## Added
* Startlist pdf export

### Changed
* Introduce gradle multi projects
    * Shared - Contains PSA libraries
    * DTO - Contains PSA domain models
    * Core - Contains PSA core functionality
* Include jasper reporting from external source

## [2.0.0] - 2018-02-01

### Changed
* Redesign PSA index page to avoid confusion with PSA Product Website.
* Replace JavaFX Control Panel with Swing Components

## [2.0.0-rc-01] - 2018-12-20

### Added
* REST interfaces
* New index page for better look and feel

### Changed
* Refactor the entire GUI
* Remove version from application support directory
* Webapp is a SPA which is included as a git submodule. During build it will
be copied to the appropriate resource directory in order to be served by Spring.

### Removed
* Old Thymeleaf Webapp (which is replaced by the SPA).

## [1.0.1] - 2018-07-01

### Fixed
* Unit of discipline Ballwurf
* Result type of discipline Ballwurf
* Export female rankings
* Remove Mehrkampf from participation list

## [1.0.0] - 2018-06-12

### Added
* Prevent event sheet export when participation is not finished

### Fixed
* Encoding of file names inside an exported archive

## [1.0.0-rc-1] - 2018-04-15

### Added
* CSV export for UBS Cup
* Result form validation

### Changed
* Event sheet export page redesigned

### Fixed
* Spelling errors in PDF reports
* Display double and int results according to their type

## [1.0.0-beta-1] - 2018-04-11

### Added
* Late registration of a participant
* Absent option for a competitor
* Sport can be changed even after the participation is finished

## 1.0.0-alpha-1 - 2018-04-02

### Added
* CSV import and parsing
* Participation managing
* Competitor editing
* Ranking based on predefined calculations
* User Management
    * Admin role
    * User role
* PDF exports
    * Participation list
    * Event sheets
    * Discipline ranking
    * Discipline group ranking
    * Total ranking

[Unreleased]: https://github.com/BilledTrain380/sporttag-psa/compare/2.0.0...HEAD
[2.0.0]: https://github.com/BilledTrain380/sporttag-psa/compare/2.0.0-rc-01...2.0.0
[2.0.0-rc-01]: https://github.com/BilledTrain380/sporttag-psa/compare/1.0.1...2.0.0-rc-01
[1.0.1]: https://github.com/BilledTrain380/sporttag-psa/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/BilledTrain380/sporttag-psa/compare/1.0.0-rc-1...1.0.0
[1.0.0-rc-1]: https://github.com/BilledTrain380/sporttag-psa/compare/1.0.0-beta-1...1.0.0-rc-1
[1.0.0-beta-1]: https://github.com/BilledTrain380/sporttag-psa/compare/1.0.0-alpha-1...1.0.0-beta-1