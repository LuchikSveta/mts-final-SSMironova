### docker image
```bash
docker run --name orders -p 5432:5432 -e POSTGRES_DB=orders -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin postgres:14
```
### Структура проект:
#### 1. Order - модуль для хранения заказов и их обработки
```
Создание заказа
REST запрос, метод POST
URL: http://localhost:8081/orders/create
DATA:
   {
   "id": "1",
   "created": "2024-08-10",
   "status": "NEW",
   "productCode": "104",
   "quantity": "2",
   "fio": "MIR MIR MIR",
   "phone": "+79629600423",
   "email": "test@mail.ru",
   "address": "Moscow"
   }
```
#### 2. Assembly - модуль по сборке заказа
```
Изменить статус сборки ASSEMBLED / ASSEMBLY_FAILED
REST запрос, метод POST
URL: http://localhost:8083/assembly/status?orderId=c3c5f37a-f58b-4022-a1f9-5850eebde86a&status=ASSEMBLY_FAILED
```
#### 3. Delivery - модуль по доставке заказа
```
Изменить статус сборки DELIVERED / DELIVERY_FAILED
REST запрос, метод POST
URL: http://localhost:8084/delivery/status?orderId=c3c5f37a-f58b-4022-a1f9-5850eebde86a&status=DELIVERED
```
#### 4. Process - модуль для всего процесса