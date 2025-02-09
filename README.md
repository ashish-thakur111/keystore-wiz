# Keystore Wiz

## Overview

Keystore Wiz is a JavaFX application built with Spring Boot that allows users to manage keystore entries. Users can add, remove, and export keystore entries, as well as test the certificates against various runtimes.

## Features

- **View Keystore Entries**: Display a list of keystore entries with details such as alias, issuer, subject, valid from, and valid until dates.
- **Add Certificate**: Add a certificate to the keystore by selecting a certificate file.
- **Remove Certificate**: Remove a certificate from the keystore with a confirmation dialog.
- **Export Keystore**: Export the keystore to a file at any location, with password protection.
- **Test the added certificate with various runtimes**: Java, Python, Node.js, etc.

## Prerequisites

- Java 11 or higher
- Gradle
- OpenSSL (for generating self-signed certificates)

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/ashish-thakur111/keystore-wiz.git
cd keystore-wiz
```

### Build the Project

```sh
./gradlew build
```

### Run the Application

```sh
./gradlew bootRun
```

## Usage

### View Keystore Entries

1. Launch the application.
2. The main table will display the keystore entries.

### Adding a Certificate

1. Click the Add Cert button.
2. Select a certificate file (.cer, .crt, .pem).
3. Enter an alias for the certificate.
4. The certificate will be added to the keystore.

### Removing a Certificate
1. Click the Remove button next to the certificate entry.
2. Confirm the deletion in the dialog.

### Exporting the Keystore
1. Click the Export to File button.
2. Enter the keystore password.
3. Select the destination file location.
4. The keystore will be exported to the selected file.

## License

This project is licensed under the Apache License 2.0. See the `LICENSE` file for details.

## Acknowledgements
- [JavaFX](https://openjfx.io/)
- [Spring Boot](https://spring.io/projects/spring-boot)
