# REST Restaurant Service

### Создан с использованием Servlets и JDBC.

## Fast start
1. Запустите Docker Desktop.
2. В терминале intellij idea, поднимите контейнер командой *docker-compose up*, дождитесь запуска контейнера.
3. Запустите приложение, метод main, в классе Application.
4. Для проверки работы можно использовать Postman.

#### CRUD операции доступны для сущностей OrderDetail и Product.
#### Используется БД Postgres:alpine3.18, подключен pgAdmin. Порт Tomcat установлен 9090 в классе Application.


Для входа в pdAdmin, откройте адрес в браузере http://localhost:5055/, логин *PGadmin@gmail.com*, пароль *PGadmin*,
далее для подключения существующей БД, нажимаем на *Server-Register-Server...*, Вкладка General поле Name, пишем любое
имя нашей БД, во вкладке Connection поле Host name/address *pg_db-task-rest*, поле Port *5432*, поле Maintenance *restaurant_db*,
Username *admin*, Password *admin*.