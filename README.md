# TuneHeaven Song Ratings Backend
[![CI](https://github.com/jnicram/songs-rating-service/actions/workflows/gradle.yml/badge.svg)](https://github.com/jnicram/songs-rating-service/actions/workflows/gradle.yml)

## Overview

Imagine that you need to create a backend service for an analytical application operating on data from a fictional music streaming service called TuneHeaven. In this service, millions of users have access to millions of songs. Every day, up to 23:00, a CSV file with ratings for music tracks from the streaming service is generated. The file follows a structure similar to the example file name: `tune-heaven-songs-2025-03-24.csv`. Each record in the file represents a vote given by a reviewing user and includes information such as:

- Song name
- Unique song ID
- Song artist
- Unique artist ID
- User ID
- Rating (1-5)
- Music genre

The content of the file should be imported into the application's database to enable the functionalities described below.

## Features

### 1. REST API Endpoint

The application should have a REST API with the following structure:

```plaintext
/api/{songId}/avg?since={dateSince}&until={dateUntil}
```

Dates should be in the format `yyyyMMdd`. Example:

```plaintext
/api/6ab3aa00-3c6a-11ee-be56-0242ac120002/avg?since=20230123&until=20230421
```

This endpoint should return the arithmetic average of all ratings for a given song within the specified date range.

### 2. Monthly Reports

On the last day of each month, right after importing the file, the application should generate two CSV files:

- `trending100songs-{yyyyMM}.csv`: A report on the top 100 songs with the highest increase in average ratings compared to the previous month.
- `songs-loosing-{yyyyMM}.csv`: A report on all songs whose average rating has dropped by at least 0.4 compared to the previous month.

Both files should contain columns for:

- `song_name` (name)
- `song_uuid` (id)
- `rating_this_month` (current month avg rating)
- `rating_previous_month` (prev month avg rating)
- `rating_2months_back` (2 months back avg rating)

### 3. REST API for Three-Month Average

The application should provide a REST API endpoint that, for a given song, returns the average ratings for the last three (completed) months. 
For example, if the API is called on April 1, 2024, the response could look like:

```plaintext
/api/6ab3aa00-3c6a-11ee-be56-0242ac120002/avg-three-months
```

Response:

```json
[
  {
    "month":"202401",
    "avg":"4.25"
  },
  {
    "month":"202402",
    "avg":"4.75"
  },
  {
    "month":"202403",
    "avg":"4.15"
  }
]
```
### Assumptions

- There are no commas in song titles and artist names.
- The file is guaranteed to appear daily; there won't be a situation where the file is missing at 23:00.
- API endpoints do not need authentication/authorization layers.

## Setup

### Technology Stack:

- **Programming Language:** Java 17
- **Framework:** Spring Boot 3.2.1
- **Dependency Management:** io.spring.dependency-management 1.1.4
- **Code Quality Plugins:**
    - SpotBugs 6.0.6
    - PMD (custom configuration file: `config/pmd/ruleset.xml`)
    - Checkstyle 10.9.3
- **Database:**
    - PostgreSQL (runtimeOnly)
    - Spring Data JPA
    - Flyway for database migrations
- **Lock Management:**
    - ShedLock 5.10.2
- **CSV File Processing:**
    - OpenCSV 5.9
- **Testing:**
    - Spring Boot Starter Test
    - Spock Framework 2.3-groovy-4.0
    - AssertJ Core 3.24.2
    - Testcontainers 1.19.1 (PostgreSQL)
- **Other Tools and Libraries:**
    - Lombok (annotation processor, compileOnly)
    - Groovy 4.0.17
- **Error Reporting:**
    - SpotBugs (HTML report)
    - Checkstyle (HTML report)

### Simple configuration

#### Server Configuration
- `server.port`: 8080
- `spring.application.name`: songs-rating-service

#### Database Configuration
- `spring.jpa.hibernate.ddl-auto`: validate
- `spring.flyway.locations`: classpath:/db/migration
- `spring.jpa.open-in-view`: true

#### Cron Job Configuration for Daily Import
- `cron.daily-import.file-location`: (Placeholder for the file location for daily imports)
- `cron.daily-import.file-pattern`: tune-heaven-songs-yyyy-MM-dd.csv
- `cron.daily-import.expression`: 0 0 23 * * ? (Cron expression for scheduling daily imports at 23:00)

#### Thanks to Shedlock, it will start after the daily import even if it takes more than 5 minutes
- `cron.monthly-generation.expression`: 0 5 23 L * ? (Cron expression for monthly file generation)

#### Monthly File Generation
- `cron.monthly-file.expression`: 0 10 23 L * ? (Cron expression for scheduling monthly file generation at 23:10)
- `cron.monthly-file.file-location`: (Placeholder for the file location for monthly file generation)
- `cron.monthly-file.trending-100-songs-file-pattern`: trending100songs-yyyyMM.csv
- `cron.monthly-file.songs-loosing-file-pattern`: songs-loosing-yyyyMM.csv

### How to run locally?

1. Start DB from `docker/docker-compose-local.yaml` e.g. 
   ```shell
    docker-compose -f docker/docker-compose-local.yaml up -d
    ```
2. Run SpringBoot application e.g.
    ```shell
    SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
    ```
