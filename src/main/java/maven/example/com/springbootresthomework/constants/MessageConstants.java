package maven.example.com.springbootresthomework.constants;

/**
 * Класс для хранения констант с текстами сообщений.
 * Используется в сервисах и контроллерах для избежания дублирования строк.
 */
public class MessageConstants {

    // Сообщения об успешных операциях
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String USER_DELETED = "User deleted successfully";

    public static final String WORKOUT_CREATED = "Workout created successfully";
    public static final String WORKOUT_UPDATED = "Workout updated successfully";
    public static final String WORKOUT_DELETED = "Workout deleted successfully";

    public static final String MEAL_CREATED = "Meal created successfully";
    public static final String MEAL_UPDATED = "Meal updated successfully";
    public static final String MEAL_DELETED = "Meal deleted successfully";

    // Ошибки
    public static final String USER_NOT_FOUND = "User not found";
    public static final String WORKOUT_NOT_FOUND = "Workout not found";
    public static final String MEAL_NOT_FOUND = "Meal not found";

    public static final String INVALID_USER_ID = "Invalid user ID format";
    public static final String INVALID_WORKOUT_ID = "Invalid workout ID format";
    public static final String INVALID_MEAL_ID = "Invalid meal ID format";

    // Общие ошибки
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String CONSTRAINT_VIOLATION = "Validation constraint violation";
    public static final String INVALID_JSON = "Invalid JSON format in request";

    private MessageConstants() {
    }
}
