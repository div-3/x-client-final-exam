## Итоговая аттестация
### Дудоров И.В.

## Стек:
1. JUnit5
2. RestAssured
3. Spring Boot
4. Spring Data JPA
5. Lombok
6. Allure

## Запуск тестов
Варианты:
1. Запуск только тестового класса

mvn clean test

2. Запуск bat-файла, который выполняет тестовый класс и запускает отчёт Allure с накопленной историей

.\run-tests.bat

## Примечания:
1. URL и авторизационные данные вынесены в файлы *.properties. Для проверки работы они прикреплены в Git.
2. Тест 3 всегда заканчивается ошибкой в assert, т.к. по API отсутствует фильтрация выдачи работников по isActive=true. Надо оформлять BUG-репорты.
3. В TODO: указаны ещё 3 бага по контрактам в API.