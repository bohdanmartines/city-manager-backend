# improve-my-city-backend

This application is designed to help manage tasks improve the city.
The repository provides API for this functionality.

## Functionality description

Registered users can create requests for improvement and vote for others' requests.
Executors can pick request based on the votes.
Requests have status showing the progress, they also have messages and history of changes.

## API specification

| Endpoint                          | Description                                                                       |
|:----------------------------------|:----------------------------------------------------------------------------------|
| /api/auth/login                   | Login a user                                                                      |
| /api/auth/logout                  | Logout a user                                                                     |
| /api/auth/register                | Register a user                                                                   |
| /api/requests                     | Get requests of all users. Accessible only by workers and admins                  |
| /api/user/<user_id>/requests      | Get requests of a user. Accessible by workers, admins and the user owning request |
| /api/requests/<request_id>/assign | Assign a request to a worker. Accessible only by workers and admins               |
