# ShareIt - Item Sharing Service 🛠️📤

## Обзор
**ShareIt** - это Java-приложение на базе микросервиса, созданное для того, чтобы пользователи могли с легкостью одалживать товары. 
Представьте себе: вы купили несколько красивых картин на воскресной ярмарке и хотите повесить их у себя на стене.
Но есть одна проблема — у вас нет дрели, а покупать ее только для того, чтобы воспользоваться ею в течение нескольких минут, 
кажется расточительством. SHAREit здесь для того, чтобы помочь вам, связав вас с людьми, которые готовы отправлять товары,
чтобы вы могли сэкономить деньги, сократить количество отходов и способствовать обмену информацией с сообществом!
**Ссылка на проект**: [SHAREit on GitHub](https://github.com/aigunov/java-shareit)

---

## Идея 🌍
**ShareIt** был создан с целью упростить обмен товарами. Вместо того чтобы покупать инструменты, которые можно
использовать только один или два раза, или платить профессионалу за выполнение небольших задач, ShareIt позволяет
пользователям одалживать товары у друзей и соседей. Вот как это работает:

1. **Найдите то, что вам нужно** - Скажем, вам нужна дрель, чтобы повесить эти картины. Вместо того, чтобы покупать ее, просто найдите доступные инструменты в разделе "Поделись этим".
2. **Одолжите это** - Как только вы найдете поблизости пользователя с дрелью, отправьте запрос на ее получение.
3. **Верните это** - Используйте это, а затем верните обратно. Нет необходимости хранить предметы, которыми вы редко пользуетесь!

Создавая сеть общих ресурсов, share it способствует созданию сообщества, в котором каждый имеет доступ к необходимым ему инструментам без чрезмерных покупок. 🌱

---

## Архитектура & Технологии 🛠️
Share It разработан как микросервисная архитектура, использующая Spring Boot и Docker для контейнеризации, 
что делает его масштабируемым и устойчивым. Ниже приведено описание используемых технологий:

- **Архитектура на основе микросервисов** - Разделение обязанностей для обеспечения высокой модульности и простоты обслуживания.
    - **Служба обработки запросов** - Обрабатывает входящие запросы, проверяет их и перенаправляет на уровень бизнес-логики.
    - **Служба бизнес-логики** - Содержит основные функциональные возможности, управление элементами, запросы пользователей и многое другое.
- **Spring Boot** - Для создания RESTful API, обеспечивающих быструю и эффективную внутреннюю разработку.
- **PostgreSQL** - надежная реляционная база данных для хранения данных.
- **Hibernate ORM** - Упрощает взаимодействие с базой данных с помощью объектно-реляционного отображения.

Такая архитектура делает SHARE it легким, масштабируемым и способным эффективно обрабатывать множество запросов, что делает его пригодным для использования в производственных приложениях. 📈

---

## Особенности 🚀
- ** Просмотр предметов ** - Пользователи могут просматривать доступные предметы, которыми делятся другие пользователи в их сообществе.
- **Запрос на заимствование ** - Отправьте запрос владельцу предмета и, после принятия, одолжите предмет на указанный срок.
- **Аутентификация пользователя ** - Безопасное управление пользователями с помощью аутентификации и авторизации.
- ** Уведомления ** - Получайте обновления, когда ваш запрос будет принят или если кто-то захочет одолжить ваш товар.
- ** Управление товарами ** - Пользователи могут добавлять, обновлять товары, которыми они хотят поделиться, и управлять ими.

---

## Загрузка & Установка ⚙️
### Необходимые компоненты
- **Java 21+**
- **Docker**
- **PostgreSQL**

### Clone Repository
bash
`git clone https://github.com/aigunov/java-shareit.git
cd java-shareit`

### Run with Docker
Build and run the Docker containers for each microservice:
bash
`docker-compose up --build`

### Local Setup
1. Install dependencies and set up PostgreSQL.
2. Run the application:

bash
`./mvnw spring-boot:run
`

---

## Структура проекта 📂
- **java-shareit/gateway** - принимает запросы с клиента, валидирует данные, переадресует к другому сервису
- **java-shareit/server** - вся бизнес-логика проекта и взаимодействия с БД

---

## Contributing 🤝
Приветствуются вклады! Не стесняйтесь открывать проблемы и отправлять запросы на доработку, чтобы поделиться ими. 
Для внесения существенных изменений, пожалуйста, сначала откройте проблему, чтобы обсудить, что вы хотели бы изменить.

---
