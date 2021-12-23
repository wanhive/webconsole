# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- Add the SRP-6a initialization parameters to the internal properties file.

## [0.7.0] - 2021-11-18

### Added

- Submit the search keyword on enter key.

## [0.6.0] - 2021-03-12

### Added

- REST API to retrieve the public application settings.

### Changed

- Upgrade the responsive table library to the new version (2.0.2).

## [0.5.0] - 2021-02-17

### Added

- Maximum search keyword length in the application's settings.

### Changed

- Improve look-and-feel.
- Improve parameter checking in the back end.

### Fixed

- POJO fields should have private visibility.
- Clear out the search keyword on data table refresh.

### Removed

- XML support and other unused annotations/features/dependencies.

## [0.4.0] - 2021-02-01

### Added

- **Wanhive Webconsole** logo on the index page.
- Handle long texts and words overflow (e.g. long aliases and email addresses).
- Automatically scroll up to the top of the page on data reload.

### Changed

- Upgrade the responsive data table.
- Standardize the font size for better readability.

## [0.3.0] - 2021-01-16

### Added

- Responsive data tables that hide columns in mobile view.
- Minimum search keyword length in the application's settings.

### Changed

- Upgrade to jQuery version 3.5.1
- Upgrade to Font Awesome version 5.15.1
- Use the global exception handler for all application-generated exceptions.

## [0.2.0] - 2020-12-17

### Added

- Use javascript to trim the important user inputs like a *challenge-code*.
- JSESSIONID SameSite attribute fix for the new browsers.

### Changed

- Improve error status reporting of web services.
- Reorganize and refactor the javascript source files.
- Upgrade the downloadable configuration and data files to the new formats.
- Rename and update the code of conduct file.

### Security

- Delete the bearer token on a sign-out.
- Set the *SameSite* cookie attribute to *strict*.

## [0.1.0] - 2020-11-25

First public release.

### Added

- Web frontend
- RESTFUL APIs
