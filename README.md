# githubuserslookup

Simple API getting info about gituhub user (star count & list of repos)

## Important info
Serwer uses github API without token authorization which is limited to 60 requests per hour

## Technologies
 * Spring Boot 2.4.5
 * Java 11
 * Maven

## Usage
on default server runs on localhost:8080

### Endpoints:
 * GET /repos/{username} - gets list of repositories and number of stars 
 * GET /starscount/{username} - gets number of stars
### How to run

To run on Ubuntu 20.04 open root project folder in terminal and type 'mvn spring-boot:run', doing this in Windows 10 cmd/powershell should also work (if there is mvn in the PATH).

## TODO
may add custom 404 page
