# AstonMicroservicesVer2

#### Взаимодействие сервисов реализована по паттерну Saga (оркестрация)
#### Оркестратор - order-composite.
#### Пока доступен один endpoint в api-gateway для запроса на размещение заказа
#### Заказ будет передан в службу доставки только если от всех сервисов пришел успешный результат
#### Общение между сервисами реализовано через брокер Kafka

### В планах:
1. Реализовать сервис доставки (delivery-service)

### Запуск микросервисов:
1. Выполнить цель "clean"
2. Сделать package общего проекта (соберутся образы из Dockerfiles)
3. Выполнить "docker compose up" (дождаться старта всех контейнеров)
4. Можно зайти в Adminer (http://localhost:5555/, контейнер - postgres, логин - postgres, пароль - postgres, база - aston)
5. Выполнить тестовые запросы из файла "POST-TEST-REQUEST.http"
6. Выполнить "docker compose down"