# Java Spring Messages API

## Endpoints and Methods

**Public Endpoints**

- `POST /register`: Register a new user (with name, last name, email, password)
- `POST /login`: Authenticate and log in a user (with email and password)

**Secure Endpoints**

- `POST /friends/add`: Send a friend request
- `POST /friends/accept`: Accept a friend request (if there is a friend request)
- `GET /friends`: Retrieve friend list
- `POST /messages/send`: Send a message to a friend
- `GET /messages`: Retrieve conversation history
- `POST /groups/create`: Creates a new group with a given name and members.
- `POST /groups/:groupId/add-member`: Adds a new member to an existing group.
- `POST /groups/:groupId/send`: Sends a message to all members of the specified group.
- `GET /groups/:groupId/messages`: Retrieves the message history for the specified group.
- `GET /groups/:groupId/members`: Retrieves the list of members for the specified group.

**Security**

- API implements authentication and authorization mechanisms using JWT tokens, therefore to access secure endpoints a vaild JWT should be provided as Bearer Authentication header.

**Json Schemes For Requests**

| Request                       | JSON                                                                                       |
|-------------------------------|--------------------------------------------------------------------------------------------|
| `/register`                   | `{ "name": "name", "lastname": "lastname", "email": "mail@mail.com", "password": "pass" }` |
| `/login`                      | `{ "email": "mail@mail.com", "password": "pass" }`                                         |
| `/friends/add`                | `{ "email": "mail@mail.com" }`                                                             |
| `/friend/accept`              | `{ "email": "mail@mail.com" }`                                                             |
| `/friends`                    | `-`                                                                                        |
| `/message/send`               | `{ "content": "message", "receiver": "mail@mail.com" }`                                    |
| `/messages `                  | `-`                                                                                        
| `/groups/create`              | `{ "name": "g1", "members": [ "mail@mail.com", "mail2@mail.com" ] }`                       |
| `/groups/:groupId/send`       | `{ "content": "message" }`                                                                 |
| `/groups/:groupId/messages`   | `-`                                                                                        |
| `/groups/:groupId/members`    | `-`                                                                                        |
| `/groups/:groupId/add-member` | `{ "email": "mail@mail.com" }`                                                                     |
