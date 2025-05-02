FROM python:3.9-slim

WORKDIR /

# Установка зависимостей
COPY requirements.txt .
RUN pip install --upgrade pip
RUN pip install --no-cache-dir -r requirements.txt

# Копирование модели и исходного кода
COPY model.tflite .
COPY lab_results_service.py .
COPY medical_records/ ./medical_records/

# Создание папки для хранения CDA-документов
RUN mkdir -p /medical_records

EXPOSE 8085

CMD ["gunicorn", "--bind", "0.0.0.0:8085", "lab_results_service:app"]