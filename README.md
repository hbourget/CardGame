
# Card Service

## Endpoints

### GET `/cards/{id}`

Récupérer une carte par son ID.

- **Paramètres** :
    - id (int) : L'ID de la carte.

- **Réponse** :
    - 200 OK : La carte est récupérée. Renvoie l'objet `Card`.
    - 404 NOT_FOUND : La carte n'a pas été trouvée.

### GET `/cards`

Récupérer toutes les cartes.

- **Réponse** :
    - 200 OK : Renvoie la liste de tous les objets `Card`.
    - 204 NO_CONTENT : Aucune carte n'est disponible.

### POST `/cards`

Ajouter une nouvelle carte.

- **Corps de la requête** :
    - card (`CardDTO`) : Les données de la carte à ajouter.

- **Réponse** :
    - 201 CREATED : La carte a été créée. Renvoie l'objet `Card` créé.
    - 400 BAD_REQUEST : La demande est incorrecte.

### POST `/cards/bulk`

Ajouter plusieurs cartes en une seule fois.

- **Corps de la requête** :
    - cards (Liste de `CardDTO`) : Les données des cartes à ajouter.

- **Réponse** :
    - 201 CREATED : Les cartes ont été créées. Renvoie la liste des objets `Card` créés.
    - 400 BAD_REQUEST : La demande est incorrecte.

### DELETE `/cards/{id}`

Supprimer une carte par son ID.

- **Paramètres**  :
    - id (int) : L'ID de la carte.

- **Réponse** :
    - 204 NO_CONTENT : La carte a été supprimée.
    - 404 NOT_FOUND : La carte n'a pas été trouvée.

### DELETE `/cards`

Supprimer toutes les cartes.

- **Réponse** :
    - 204 NO_CONTENT : Toutes les cartes ont été supprimées.

### PUT `/cards/{id}`

Mettre à jour une carte par son ID.

- **Paramètres**  :
    - id (int) : L'ID de la carte.

- **Corps de la requête** :
    - card (`Card`) : Les données de la carte à mettre à jour.

- **Réponse** :
    - 200 OK : La carte a été mise à jour. Renvoie l'objet `Card` mis à jour.
    - 404 NOT_FOUND : La carte n'a pas été trouvée.

# Auth Service

## Endpoints

### POST `/login`

Authentifier un utilisateur.

- **Corps de la requête** :
    - credentials (Map) : Un objet contenant "username" et "password" en clés.

- **Réponse** :
    - 200 OK : Authentification réussie. Renvoie l'objet `UserDTO`.
    - 401 UNAUTHORIZED : Authentification échouée.

### POST `/register`

Inscrire un nouvel utilisateur.

- **Corps de la requête** :
    - credentials (Map) : Un objet contenant "username" et "password" en clés.

- **Réponse** :
    - 200 OK : Inscription réussie. Renvoie l'objet `UserDTO`.
    - 401 UNAUTHORIZED : Inscription échouée.

# User Service

## Endpoints

### GET `/users/{idOrUsername}`

Récupérer un utilisateur par ID ou nom d'utilisateur.

- **Paramètres** :
    - idOrUsername (String) : ID ou nom d'utilisateur de l'utilisateur.

- **Réponse** :
    - 200 OK : Utilisateur trouvé. Renvoie l'objet `UserDTO`.
    - 404 NOT FOUND : Utilisateur introuvable.

### GET `/users`

Récupérer tous les utilisateurs.

- **Réponse** :
    - 200 OK : Liste des utilisateurs trouvée. Renvoie une liste d'objets `UserDTO`.
    - 204 NO CONTENT : Aucun utilisateur trouvé.

### PUT `/users/{id}`

Mettre à jour un utilisateur.

- **Paramètres** :
    - id (Integer) : ID de l'utilisateur.

- **Corps de la requête** :
    - user (`User`) : L'objet utilisateur mis à jour.

- **Réponse** :
    - 200 OK : Utilisateur mis à jour. Renvoie l'objet `UserDTO`.
    - 404 NOT FOUND : Utilisateur introuvable.

### PUT `/users/{id}/addbalance`

Ajouter un solde à un utilisateur.

- **Paramètres** :
    - id (Integer) : ID de l'utilisateur.

- **Corps de la requête** :
    - balanceToAdd (Integer) : Solde à ajouter.

- **Réponse** :
    - 200 OK : Solde ajouté. Renvoie l'objet `UserDTO`.
    - 404 NOT FOUND : Utilisateur introuvable.

### PUT `/users/{id}/subtractbalance`

Soustraire un solde à un utilisateur.

- **Paramètres** :
    - id (Integer) : ID de l'utilisateur.

- **Corps de la requête** :
    - balanceToSubtract (Integer) : Solde à soustraire.

- **Réponse** :
    - 200 OK : Solde soustrait. Renvoie l'objet `UserDTO`.
    - 404 NOT FOUND : Utilisateur introuvable.

### DELETE `/users/{id}`

Supprimer un utilisateur.

- **Paramètres** :
    - id (Integer) : ID de l'utilisateur.

- **Réponse** :
    - 204 NO CONTENT : Utilisateur supprimé.
    - 404 NOT FOUND : Utilisateur introuvable.

### DELETE `/users`

Supprimer tous les utilisateurs.

- **Réponse** :
    - 204 NO CONTENT : Tous les utilisateurs supprimés.


# Inventory Service

## Endpoints

### POST `/inventories/users/{userId}/cards/{cardId}`

Ajouter une carte à l'inventaire d'un utilisateur.

- **Paramètres** :
    - `userId` (Integer) : L'ID de l'utilisateur.
    - `cardId` (Integer) : L'ID de la carte.

- **Réponse** :
    - 200 OK : La carte a été ajoutée à l'inventaire. Renvoie l'`InventoryResponse` mis à jour.
    - 409 CONFLICT : La carte n'a pas pu être ajoutée à l'inventaire.

### `DELETE /inventories/users/{userId}/cards/{cardId}`

Supprimer une carte de l'inventaire d'un utilisateur.

- **Paramètres** :
    - `userId` (Integer) : L'ID de l'utilisateur.
    - `cardId` (Integer) : L'ID de la carte.

- **Réponse** :
    - 200 OK : La carte a été supprimée de l'inventaire.
    - 409 CONFLICT : La carte n'a pas pu être supprimée de l'inventaire.

### POST `/inventories/users/{userId}/cards`

Ajouter toutes les cartes à l'inventaire d'un utilisateur.

- **Paramètres** :
    - userId (Integer) : L'ID de l'utilisateur.

- **Réponse** :
    - 200 OK : Toutes les cartes ont été ajoutées à l'inventaire. Renvoie l'`InventoryResponse` mis à jour.
    - 404 NOT_FOUND : L'inventaire de l'utilisateur n'a pas été trouvé.

### DELETE `/inventories/users/{userId}/cards`

Supprimer toutes les cartes de l'inventaire d'un utilisateur.

- **Paramètres** :
    - `userId` (Integer) : L'ID de l'utilisateur.

- **Réponse** :
    - 200 OK : Toutes les cartes ont été supprimées de l'inventaire.

### GET `/inventories/users/{userId}`

Récupérer l'inventaire d'un utilisateur.

- **Paramètres** :
    - `userId` (Integer) : L'ID de l'utilisateur.

- **Réponse** :
    - 200 OK : L'inventaire de l'utilisateur est récupéré. Renvoie l'`InventoryResponse`.
    - 404 NOT_FOUND : L'inventaire de l'utilisateur n'a pas été trouvé.

### GET `/inventories`

Récupérer tous les inventaires.

- **Réponse** :
    - 200 OK : Renvoie une liste de tous les objets `Inventory`.

### POST `/inventories/buy/users/{idUser}/cards/{cardId}`

Acheter une carte pour un utilisateur.

- Paramètre du chemin :
    - idUser (Integer) : ID de l'utilisateur.
    - cardId (Integer) : ID de la carte à acheter.

- Réponse :
    - 200 OK : Achat réussi. Renvoie l'objet `Card`.
    - 409 CONFLICT : Conflit, probablement parce que l'utilisateur n'a pas assez de solde pour acheter la carte.

### POST `/inventories/sell/users/{idUser}/cards/{cardId}`

Vendre une carte d'un utilisateur.

- Paramètre du chemin :
    - idUser (Integer) : ID de l'utilisateur.
    - cardId (Integer) : ID de la carte à vendre.

- Réponse :
    - 200 OK : Vente réussie. Renvoie l'objet `Card`.
    - 409 CONFLICT : Conflit, probablement parce que l'utilisateur ne possède pas la carte à vendre.

### POST `/inventories/sell/users/{idUser}`

Vendre toutes les cartes d'un utilisateur.

- Paramètre du chemin :
    - idUser (Integer) : ID de l'utilisateur.

- Réponse :
    - 200 OK : Toutes les cartes vendues avec succès.
    - 409 CONFLICT : Conflit, probablement parce que l'utilisateur ne possède pas de cartes à vendre.


# Room Service

## Endpoints

### GET /rooms/{idOrName}

Récupérer une room par son ID ou son nom.

- Paramètre :
    - idOrName (String) : ID ou nom de la room.

- Réponse :
    - Objet `Room` si la room est trouvée.
    - null si la room n'est pas trouvée.

### GET /rooms

Récupérer toutes les room.

- Réponse :
    - Liste d'objets `Room`.

### POST /rooms

Ajouter une room.

- Corps de la requête :
    - Objet `Room` à ajouter.

- Réponse :
    - 201 CREATED : Room créée avec succès. Renvoie l'objet `Room`.
    - 400 BAD_REQUEST : Erreur lors de la création de la room.

### POST /rooms/bulk

Ajouter plusieurs room.

- Corps de la requête :
    - Liste d'objets `Room` à ajouter.

- Réponse :
    - 201 CREATED : Rooms créées avec succès. Renvoie la liste des objets `Room`.
    - 400 BAD_REQUEST : Erreur lors de la création des rooms.

### DELETE /rooms/{idRoom}

Supprimer une room.

- Paramètre du chemin :
    - idRoom (int) : ID de la room.

- Réponse :
    - Objet renvoyé par la méthode `deleteRoom` du service `RoomService`.

### DELETE /rooms

Supprimer toutes les rooms.

- Réponse :
    - Objet renvoyé par la méthode `deleteAllRooms` du service `RoomService`.

### PUT /rooms/join/{idRoom}/users/{playerId}

Rejoindre une room.

- Paramètre :
    - idRoom (int) : ID de la room.
    - playerId (int) : ID du joueur.

- Réponse :
    - Objet Room renvoyé par la méthode `joinRoom` du service `RoomService`.

### PUT /rooms/leave/{idRoom}/users/{playerId}

Quitter une room.

- Paramètre :
    - idRoom (int) : ID de la room.
    - playerId (int) : ID du joueur.

- Réponse :
    - Objet Room renvoyé par la méthode `leaveRoom` du service `RoomService`.

### PUT /rooms/{idRoom}/users/{playerId}/cards/{cardId}

Ajouter une carte à une room.

- Paramètre :
    - idRoom (int) : ID de la room.
    - cardId (int) : ID de la carte.
    - playerId (int) : ID du joueur.

- Réponse :
    - Objet Room renvoyé par la méthode `addCardToRoom` du service `RoomService`.

### PUT /rooms/play/{idRoom}/users/{idUser}

Jouer un tour dans une room.

- Paramètre :
    - idRoom (int) : ID de la room.
    - idUser (int) : ID de l'utilisateur.

- Réponse :
    - Objet Room renvoyé par la méthode `playRound` du service `RoomService`.