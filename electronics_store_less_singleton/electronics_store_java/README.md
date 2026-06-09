# Інформаційна система магазину електроніки
## Варіант 20 — Java реалізація

---

## Структура проєкту

```
src/main/java/store/
├── Main.java                          ← точка входу
│
├── model/                             ← сутності предметної області
│   ├── Product.java                   ← абстрактний клас товару
│   ├── Smartphone.java                ← конкретна сутність
│   ├── Laptop.java                    ← конкретна сутність
│   ├── Tablet.java                    ← конкретна сутність
│   ├── Customer.java                  ← покупець + LoyaltyTier enum
│   ├── Cart.java                      ← кошик (використовує Memento + Observer)
│   └── Order.java                     ← замовлення
│
├── factory/                           ← патерн Abstract Factory
│   ├── ProductFactory.java            ← абстрактна фабрика
│   ├── SmartphoneFactory.java
│   ├── LaptopFactory.java
│   └── TabletFactory.java
│
├── observer/                          ← патерн Observer
│   ├── StoreObserver.java             ← інтерфейс спостерігача
│   ├── Event.java                     ← подія
│   ├── EventBus.java                  ← шина подій (також Singleton)
│   ├── StatisticsObserver.java        ← збір статистики
│   └── LoggerObserver.java            ← логування в консоль
│
├── singleton/                         ← патерн Singleton
│   ├── ProductRepository.java         ← каталог товарів
│   ├── CustomerRepository.java        ← сховище покупців
│   └── MementoStorage.java            ← зберігання знімків (Singleton + Memento)
│
├── memento/                           ← патерн Memento
│   └── CartMemento.java               ← знімок стану кошика
│
├── service/                           ← бізнес-логіка
│   ├── OrderService.java              ← оформлення замовлень
│   └── ComparisonService.java         ← порівняння характеристик
│
└── ui/
    └── ConsoleUI.java                 ← термінальний інтерфейс
```

---

## Реалізовані патерни

| Патерн           | Класи                                                              |
|------------------|--------------------------------------------------------------------|
| Observer         | `StoreObserver`, `EventBus`, `StatisticsObserver`, `LoggerObserver` |
| Abstract Factory | `ProductFactory`, `SmartphoneFactory`, `LaptopFactory`, `TabletFactory` |
| Singleton        | `ProductRepository`, `CustomerRepository`, `EventBus`, `MementoStorage` |
| Memento          | `CartMemento`, `MementoStorage`, `Cart.undo()`                      |

---

## Компіляція та запуск

### Linux / macOS
```bash
chmod +x run.sh
./run.sh
```

### Windows
```
run.bat
```

### Вручну (будь-яка ОС)
```bash
mkdir out
find src/main/java -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out store.Main
```

**Вимоги:** JDK 11 або вище

---

## Функціональність

1. **Каталог** — перегляд усіх товарів по категоріях з технічними характеристиками  
2. **Кошик** — додавання, видалення, перегляд з підрахунком знижки  
3. **Undo (Memento)** — скасування будь-якої зміни кошика  
4. **Замовлення** — оформлення з автоматичним зменшенням залишків  
5. **Лояльність** — 4 рівні (Bronze/Silver/Gold/Platinum) зі знижками 0–15%  
6. **Порівняння** — таблиця характеристик 2–3 пристроїв  
7. **Статистика** — кількість подій кожного типу та загальний дохід  
