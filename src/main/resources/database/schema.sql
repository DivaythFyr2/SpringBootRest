CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       age INT NOT NULL CHECK (age >= 14),
                       weight DOUBLE PRECISION NOT NULL CHECK (weight >= 40),
                       height DOUBLE PRECISION NOT NULL CHECK (height >= 80)
                   );

CREATE TABLE meals (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       calories INT NOT NULL CHECK (calories >= 10),
                       user_id INT NOT NULL,
                       CONSTRAINT fk_meal_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                   );

CREATE TABLE workouts (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          duration INT NOT NULL CHECK (duration >= 1),
                          calories_burned INT NOT NULL,
                          user_id INT NOT NULL,
                          CONSTRAINT fk_workout_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                      );
