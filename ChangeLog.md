# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Responsive data table that hides columns for mobile view.
- Minimum search keyword length in the application's settings.

### Changed

- Upgrade to jQuery version 3.5.1
- Upgrade to Font Awesome version 5.15.1
- Improve exception handling in data access objects.

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
