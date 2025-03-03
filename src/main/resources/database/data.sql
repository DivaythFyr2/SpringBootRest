INSERT INTO users (name, age, weight, height) VALUES
('Stanislav', 29, 70, 180),
('Denis', 24, 78, 175),
('Artem', 19, 65, 180),
('Karina', 18, 49, 163),
('Tatyana', 23, 51, 168);

INSERT INTO meals (name, calories, user_id) VALUES
('Pasta', 500, 1),
('Salad', 200, 1),
('Burger', 700, 2),
('Steak', 600, 3),
('Oatmeal', 300, 3),
('French Fries', 430, 4),
('Boiled Chicken', 200, 4),
('Walnut', 340, 5),
('Banana', 250, 5);

INSERT INTO workouts (name, duration, calories_burned, user_id) VALUES
('Running', 30, 300, 1),
('Swimming', 45, 400, 1),
('Cycling', 60, 500, 2),
('Yoga', 30, 120, 3),
('Jump Rope', 15, 180, 3),
('Running', 60, 600, 4),
('Shuttle running', 30, 150, 5);