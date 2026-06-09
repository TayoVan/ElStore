#!/bin/bash
# ============================================================
# Скрипт компіляції та запуску магазину електроніки
# Вимоги: JDK 11+
# ============================================================

SRC_DIR="src/main/java"
OUT_DIR="out"
MAIN_CLASS="store.Main"

echo "🔨 Компіляція..."
mkdir -p "$OUT_DIR"

find "$SRC_DIR" -name "*.java" > sources.txt

javac -encoding UTF-8 -d "$OUT_DIR" @sources.txt
if [ $? -ne 0 ]; then
    echo "❌ Помилка компіляції"
    rm -f sources.txt
    exit 1
fi

rm -f sources.txt
echo "✅ Компіляція успішна"
echo ""
echo "🚀 Запуск..."
echo ""

java -cp "$OUT_DIR" "$MAIN_CLASS"
