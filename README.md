## Учебный проект "Filmorate"

# Схема базы данных проекта

![Data base scheme](https://github.com/NadezhdaNovikova/java-filmorate/blob/add-database/DB_diagram.png)

[Веб версия схемы](https://dbdiagram.io/d/62c5cca269be0b672cb35b08)


# Примеры запросов

## USERS

### 1. Получение списка всех пользователей

#### GET /users

```sql
    SELECT *
    FROM users;
```

### 2. Получение пользователя по {id}

#### GET /users/{id}

```sql
    SELECT *
    FROM users
    WHERE id = {id};
```

### 3. Получение списка друзей для пользователя {id}

#### GET /users/{id}/friends

```sql
    SELECT *
    FROM users
    WHERE id IN (SELECT friend_id FROM friends WHERE user_id = {id}); 
```

### 4. Получение списка общих друзей для пользователей {id} и {otherId}

#### GET /users/{id}/friends/common/{otherId}

```sql
    SELECT *
    FROM users
    WHERE id IN (SELECT friend_id FROM friends WHERE user_id = {id});
    INTERSECT
    SELECT *
    FROM users
    WHERE id IN (SELECT friend_id FROM friends WHERE user_id = {otherId});
```

### 5. Создание пользователя

#### POST /users

```sql
    INSERT INTO users (email, login, name, birthdate)
    VALUES ('test@ya.ru', 'test_user', 'test user name', '2000-10-13')) RETURNING id;
```

### 6. Обновление пользователя

#### PUT /users

```sql
    UPDATE users
    SET email     = 'test_update@ya.ru',
        login     = 'test_user',
        name      = 'test user name',
        birthdate = TO_DATE('01022019', 'MMDDYYYY')
    WHERE id = 'id';
```

### 7. Добавление друга {friendId} для пользователя {id}

#### PUT /users/{id}/friends/{friendId}

```sql
    INSERT INTO friends (user_id, friend_id)
    VALUES ({id}, {friendId});
```

### 8. Удаление друга {friendId} у пользователя {id}

#### DELETE /users/{id}/friends/{friendId}

```sql
    DELETE
    FROM friends
    WHERE (user_id = {id} AND friend_id = {friendId};
```

### 8. Удаление пользователя {id}

#### DELETE /users/{id}

```sql
    DELETE
    FROM users
    WHERE id = {id} CASCADE;
```

***  

## FILMS

### 1. Получение списка всех фильмов

#### GET /films

```sql
    SELECT * FROM films; 
```

### 2. Получение фильма с {id}

#### GET /films/{id}

```sql
    SELECT *
    FROM films AS f
    WHERE f.id = {id};
```

### 3. Создание фильма

#### POST /films

```sql
    INSERT INTO films (description, name, releaseDate, duration, mpaa_rate_id)
    VALUES ('desc', 'Awesome film', '2010-07-15', 120, 2) RETURNING id;
```

### 4. Обновление фильма

#### PUT /films

```sql
    UPDATE films
    SET description  = 'new description',
        name         = 'Changed film',
        releaseDate  = '2010-07-15',
        duration     = 120,
        mpaa_rate_id = 2
    WHERE id = 'id';
```

### 5. Добавление like для фильма

#### PUT /films/{id}/like/{userId}

```sql
    INSERT INTO likes (film_id, user_id)
    VALUES ({id}, {userId});
```

### 6. Удаление фильма с {id}

#### DELETE /films/{id}

```sql
    DELETE
    FROM films
    WHERE id = {id} CASCADE;
```

### 5. Удаление like у фильма

#### DELETE /films/{id}/like/{userId}

```sql
    DELETE
    FROM likes
    WHERE film_id = {id} AND user_id = {userId};
```
### 6. Добавление жанра фильму

```sql
    INSERT INTO film_genre (film_id, genre_id)
    VALUES (5, 2);
```
***