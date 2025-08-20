# exam-api (Java Spring Boot)

Minimal REST API with **read/write split** using two DataSources (read and write)
so you can run **two replicas** of the same image in Kubernetes and route GET vs
POST/PUT/DELETE as required by the task.

## Endpoints
- `POST /students` — create (upsert)
- `PUT /students/{roll}` — update (upsert)
- `DELETE /students/{roll}` — delete
- `GET /students/{roll}` — get by roll
- `GET /students?year=YYYY&limit=100&offset=0` — list by exam year

## Config (env)
- WRITE_DB_URL, WRITE_DB_USER, WRITE_DB_PASS
- READ_DB_URL,  READ_DB_USER,  READ_DB_PASS

Defaults (for K8s service names):
```
write.url=jdbc:postgresql://pooler-write.exam.svc:6432/examdb
read.url =jdbc:postgresql://pooler-read.exam.svc:6432/examdb
```

## Run locally
```bash
mvn spring-boot:run
```

## Docker
```bash
docker build -t exam-api:latest .
docker run -d --name exam-api -p 8080:8080   -e WRITE_DB_URL="jdbc:postgresql://pooler-write:6432/examdb"   -e WRITE_DB_USER="examuser" -e WRITE_DB_PASS="exampass"   -e READ_DB_URL="jdbc:postgresql://pooler-read:6432/examdb"   -e READ_DB_USER="examuser_ro" -e READ_DB_PASS="readonly"   exam-api:latest
```

## Quick SQL (table only; partitions in DB phase)
```sql
CREATE TABLE IF NOT EXISTS student_results (
  roll_number BIGINT PRIMARY KEY,
  marks INT NOT NULL,
  exam_year INT NOT NULL
);
```
