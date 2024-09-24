import matplotlib.pyplot as plt
import numpy as np

def generate_chart(data, chart_path):
    # Пример генерации простой колончатой диаграммы, зависит от структуры данных
    names = [item['title'] for item in data]
    values = [item['cost'] for item in data]

    plt.figure(figsize=(10, 5))
    plt.bar(names, values)
    plt.savefig(chart_path)
    plt.close()
