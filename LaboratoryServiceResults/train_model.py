import numpy as np
import tensorflow as tf
import sklearn
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder

# Генерация синтетических данных с более реалистичными зависимостями
np.random.seed(42)
n_samples = 5000  # Увеличиваем количество данных

# Генерируем значения для тестов (blood_glucose) с учётом породы
breeds = ["labrador", "poodle"] * (n_samples // 2)
breed_encoder = LabelEncoder()
breed_encoded = breed_encoder.fit_transform(breeds)

# Нормы для пород
norms = {
    "labrador": (70, 120),
    "poodle": (65, 115)
}

# Генерируем значения тестов с шумом
values = []
labels = []
for i in range(n_samples):
    breed = breeds[i]
    norm_range = norms[breed]
    # 70% данных — нормальные значения, 30% — аномальные
    if np.random.random() < 0.7:
        # Нормальное значение в пределах нормы с небольшим шумом
        value = np.random.uniform(norm_range[0], norm_range[1])
        label = 1  # Нормально
    else:
        # Аномальное значение (ниже или выше нормы)
        if np.random.random() < 0.5:
            value = np.random.uniform(norm_range[0] - 30, norm_range[0] - 10)  # Низкое
        else:
            value = np.random.uniform(norm_range[1] + 10, norm_range[1] + 30)  # Высокое
        label = 0  # Аномально

    # Добавляем небольшой шум к значению
    value += np.random.normal(0, 2)  # Шум ±2
    values.append(value)
    labels.append(label)

# Формируем входные данные: [значение теста, закодированная порода]
X = np.column_stack((values, breed_encoded))
y = np.array(labels)

# Разделение данных на обучающую и тестовую выборки
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Создание модели с более сложной архитектурой
model = tf.keras.Sequential([
    tf.keras.layers.Input(shape=(2,)),
    tf.keras.layers.Dense(64, activation='relu'),
    tf.keras.layers.Dense(32, activation='relu'),
    tf.keras.layers.Dense(16, activation='relu'),
    tf.keras.layers.Dense(1, activation='sigmoid')
])

# Компиляция модели
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Обучение модели с увеличенным количеством эпох
model.fit(X_train, y_train, epochs=100, batch_size=32, validation_data=(X_test, y_test))

# Оценка модели

loss, accuracy = model.evaluate(X_test, y_test)
print(f"Точность на тестовой выборке: {accuracy * 100:.2f}%")

# Сохранение модели в формате TensorFlow Lite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
with open('model.tflite', 'wb') as f:
    f.write(tflite_model)