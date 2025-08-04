# GitHub Repository API

## Additional Settings
Set a GitHub Personal Access Token as environmental variable to increase rate limit to 5000 per hour:<br>
`GITHUB_TOKEN=github_personal_access_token`

## Description
Spring Boot application to fetch all non-fork GitHub repositories of a user along with their branches and latest commit SHA.

## Prerequisites
- Java 24
- Maven

## How to Run
1. Clone this repository
2. Run `./mvnw spring-boot:run`
3. Access API at: `http://localhost:8080/api/v1/github/{username}/repositories`

## Example Response
```
[
    {
        "name": "example-repo",
        "ownerLogin": "username",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "abc123def456"
            }
        ]
    }
]
```

## Error Response
```
{
    "status": 404,
    "message": "User not found"
}
```

## Tests
Run `./mvnw test` to execute the integration test.
