# P2P Instant Messaging Application (Client)

## Description

This project is an implementation of a distributed instant messaging system using Java RMI. The application allows multiple clients to connect to a central server, which manages the connections and notifies clients about the connection and disconnection of other users. Message communication is carried out directly between clients without passing through the server.

## Features

- **Multiple client connections**: The server accepts connections from multiple clients and notifies all connected clients about new users and disconnections.
- **P2P Messaging**: Message communication is carried out directly between clients without passing through the server.
- **Friend Groups** :
  - Manage friend groups with user registration.
  - Request and accept friendships, even when the user is offline.
  - Connection/disconnection notifications are only for friends.
  - Pending friend requests are stored on the server.

## Usage

1. **Connect to the server**: When starting the client, it will automatically connect to the server specified in the configuration.
2. **Send messages**: Use the graphical interface to send messages to other connected clients.
3. **Manage friends**: Register new users, request friendships, and manage your friend groups from the graphical interface.

## Technologies Used

- **Java RMI**: For remote communication between the server and clients.
- **Swing**: For the client's graphical interface.
- **JDBC**: For managing the user and friend database.

## Authors

- **Sócrates Agudo Torrado**
- **Sergio Álvarez Piñón**

---

Thank you for using our P2P messaging application! If you have any questions or suggestions, feel free to contact us.
