import numpy as np
import tensorflow.lite as tflite
from flask import Flask, request, jsonify, send_file
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.graphics.shapes import Drawing
from reportlab.graphics.charts.linecharts import HorizontalLineChart
from reportlab.lib import colors
from reportlab.platypus import Table, TableStyle
import io
import os
from lxml import etree
from datetime import datetime

app = Flask(__name__)

# Папка для хранения CDA-документов
CDA_STORAGE_DIR = "medical_records"
if not os.path.exists(CDA_STORAGE_DIR):
    os.makedirs(CDA_STORAGE_DIR)

# Загрузка TFLite модели
interpreter = tflite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Нормы для породы (пример для собак)
BREED_NORMS = {
    "labrador": {"blood_glucose": (70, 120), "xray_density": (0.5, 1.5)},
    "poodle": {"blood_glucose": (65, 115), "xray_density": (0.4, 1.4)}
}

# Кодирование породы
BREED_ENCODER = {"labrador": 0, "poodle": 1}


# Функция для AI-расшифровки
def analyze_result(test_type, value, breed):
    breed_encoded = BREED_ENCODER.get(breed.lower(), 0)
    input_data = np.array([[value, breed_encoded]], dtype=np.float32)
    interpreter.set_tensor(input_details[0]['index'], input_data)
    interpreter.invoke()
    prediction = interpreter.get_tensor(output_details[0]['index'])[0][0]

    norm_range = BREED_NORMS.get(breed, {}).get(test_type, (0, 0))
    status = "normal" if norm_range[0] <= value <= norm_range[1] else "abnormal"
    return {"prediction": float(prediction), "status": status, "norm_range": norm_range}


# Создание CDA-документа
def create_cda_document(pet_id, test_type, value, date):
    NS = "urn:hl7-org:v3"
    root = etree.Element("{%s}ClinicalDocument" % NS, nsmap={None: NS})

    doc_id = etree.SubElement(root, "id", root="2.16.840.1.113883.19.5")
    effective_time = etree.SubElement(root, "effectiveTime", value=date.replace("-", ""))

    patient_role = etree.SubElement(root, "patientRole")
    patient_id = etree.SubElement(patient_role, "id", root="2.16.840.1.113883.19.5", extension=pet_id)  # Изменено на строку
    patient = etree.SubElement(patient_role, "patient")
    patient_name = etree.SubElement(patient, "name")
    patient_name.text = f"Pet {pet_id}"

    component = etree.SubElement(root, "component")
    structured_body = etree.SubElement(component, "structuredBody")
    body_component = etree.SubElement(structured_body, "component")
    section = etree.SubElement(body_component, "section")
    entry = etree.SubElement(section, "entry")
    observation = etree.SubElement(entry, "observation", classCode="OBS", moodCode="EVN")

    code = etree.SubElement(observation, "code", code=test_type.upper(),
                            displayName=test_type.replace("_", " ").title())
    value_elem = etree.SubElement(observation, "value",
                                  attrib={"{http://www.w3.org/2001/XMLSchema-instance}type": "PQ", "value": str(value),
                                          "unit": "mg/dL" if test_type == "blood_glucose" else "g/cm³"})
    obs_time = etree.SubElement(observation, "effectiveTime", value=date.replace("-", ""))

    return etree.tostring(root, pretty_print=True, xml_declaration=True, encoding="UTF-8")


# Сохранение результата анализа в CDA
@app.route('/store_result', methods=['POST'])
def store_result():
    data = request.json
    pet_id = str(data['pet_id'])  # Преобразуем в строку
    test_type = data['test_type']
    value = data['value']
    date = data['date']
    breed = data['breed']

    cda_xml = create_cda_document(pet_id, test_type, value, date)

    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    filename = f"result_{pet_id}_{timestamp}.xml"
    filepath = os.path.join(CDA_STORAGE_DIR, filename)
    with open(filepath, "wb") as f:
        f.write(cda_xml)

    analysis = analyze_result(test_type, value, breed)
    return jsonify({"message": "Result stored in Medical Records", "analysis": analysis})


# Генерация PDF-отчёта с таблицами и графиком
@app.route('/generate_report/<string:pet_id>', methods=['GET'])  # Изменено на string
def generate_report(pet_id):
    # Находим все CDA-документы для данного pet_id
    results = []
    patients = set()
    for filename in os.listdir(CDA_STORAGE_DIR):
        if filename.startswith("result_"):
            filepath = os.path.join(CDA_STORAGE_DIR, filename)
            with open(filepath, "rb") as f:
                tree = etree.parse(f)
                root = tree.getroot()

                # Извлекаем pet_id и имя питомца
                current_pet_id = root.xpath("//v3:patientRole/v3:id/@extension", namespaces={"v3": "urn:hl7-org:v3"})[0]
                pet_name = \
                root.xpath("//v3:patientRole/v3:patient/v3:name/text()", namespaces={"v3": "urn:hl7-org:v3"})[0]
                patients.add((current_pet_id, pet_name))

                # Извлекаем данные анализа, только если pet_id совпадает
                if current_pet_id == pet_id:  # Простое сравнение строк
                    test_type = \
                    root.xpath("//v3:observation/v3:code/@displayName", namespaces={"v3": "urn:hl7-org:v3"})[0]
                    value = float(
                        root.xpath("//v3:observation/v3:value/@value", namespaces={"v3": "urn:hl7-org:v3"})[0])
                    date = root.xpath("//v3:observation/v3:effectiveTime/@value", namespaces={"v3": "urn:hl7-org:v3"})[
                        0]
                    date = f"{date[:4]}-{date[4:6]}-{date[6:8]}"
                    unit = root.xpath("//v3:observation/v3:value/@unit", namespaces={"v3": "urn:hl7-org:v3"})[0]
                    results.append((test_type, value, date, unit))

    if not results:
        return jsonify({"error": "No data found for this Pet ID in Medical Records"}), 404

    # Сортируем результаты по дате
    results.sort(key=lambda x: x[2])

    # Создание PDF
    buffer = io.BytesIO()
    c = canvas.Canvas(buffer, pagesize=letter)
    width, height = letter
    y_position = height - 50  # Начальная позиция по оси Y

    # Заголовок отчёта
    c.setFont("Helvetica-Bold", 16)
    c.drawString(100, y_position, f"Lab Results Report for Pet ID: {pet_id}")
    y_position -= 20

    # Таблица 1: Список пациентов
    c.setFont("Helvetica-Bold", 12)
    c.drawString(100, y_position, "List of Patients")
    y_position -= 20

    patient_data = [["Pet ID", "Name"]]  # Заголовки таблицы
    for p_id, p_name in sorted(patients, key=lambda x: x[0]):  # Сортировка по строковому ID
        patient_data.append([p_id, p_name])

    patient_table = Table(patient_data, colWidths=[100, 200])
    patient_table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('FONTSIZE', (0, 0), (-1, -1), 10),
        ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
        ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
        ('GRID', (0, 0), (-1, -1), 1, colors.black),
    ]))
    table_width, table_height = patient_table.wrap(0, 0)
    patient_table.drawOn(c, 100, y_position - table_height)
    y_position -= table_height + 20

    # Таблица 2: Результаты анализов
    c.setFont("Helvetica-Bold", 12)
    c.drawString(100, y_position, f"Results for Pet ID: {pet_id}")
    y_position -= 20

    result_data = [["Test Type", "Value", "Unit", "Date"]]  # Заголовки таблицы
    for test_type, value, date, unit in results:
        result_data.append([test_type, str(value), unit, date])

    result_table = Table(result_data, colWidths=[100, 80, 80, 100])
    result_table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('FONTSIZE', (0, 0), (-1, -1), 10),
        ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
        ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
        ('GRID', (0, 0), (-1, -1), 1, colors.black),
    ]))
    table_width, table_height = result_table.wrap(0, 0)
    result_table.drawOn(c, 100, y_position - table_height)
    y_position -= table_height + 20

    # График
    c.setFont("Helvetica-Bold", 12)
    c.drawString(100, y_position, f"Trend for Test Type: {results[0][0]}")
    y_position -= 20

    dates = [r[2] for r in results]
    values = [r[1] for r in results]

    num_points = len(dates)
    if num_points <= 3:
        selected_labels = dates
    else:
        selected_labels = [dates[0], dates[num_points // 2], dates[-1]]
        labels = [''] * num_points
        labels[0] = dates[0]
        labels[num_points // 2] = dates[num_points // 2]
        labels[-1] = dates[-1]
        selected_labels = labels

    drawing = Drawing(400, 200)
    lc = HorizontalLineChart()
    lc.x = 50
    lc.y = 50
    lc.height = 125
    lc.width = 300
    lc.data = [values]
    lc.categoryAxis.categoryNames = selected_labels
    lc.categoryAxis.labels.angle = 45
    lc.categoryAxis.labels.boxAnchor = 'ne'
    lc.valueAxis.valueMin = min(values) * 0.9
    lc.valueAxis.valueMax = max(values) * 1.1
    lc.lines[0].strokeColor = colors.blue
    drawing.add(lc)
    drawing.drawOn(c, 100, y_position - 200)

    c.showPage()
    c.save()

    buffer.seek(0)
    return send_file(buffer, as_attachment=True, download_name=f"report_{pet_id}.pdf", mimetype='application/pdf')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8085)