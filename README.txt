
# init h2database: create table
curl -X POST http://127.0.0.1:7070/initialize

# insert: userId= 0, 1, 2, 3
curl -X POST http://127.0.0.1:7070/create

# list
curl -X POST http://127.0.0.1:7070/list

# delete
curl -X POST http://127.0.0.1:7070/delete/{userId}
