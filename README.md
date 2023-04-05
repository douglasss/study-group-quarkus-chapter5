
# Clients for consuming other microservices

Code following the Chapter 5 from the book "Kubernetes Native Microservices with Quarkus and MicroProfile"

## Local execution

### PostgreSQL

```shell script
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_banking -e POSTGRES_USER=quarkus_banking -e POSTGRES_PASSWORD=quarkus_banking -e POSTGRES_DB=quarkus_banking -p 5432:5432 postgres:12
```

## Deploying to Kubernetes

### PostgreSQL

Install PostgreSQL into Kubernetes:

```shell script
kubectl apply -f postgresql_kubernetes.yml
```

### Deploy application

```shell script
cd account-service
mvn clean package -Dquarkus.kubernetes.deploy=true -DskipTests
cd ../transaction-service
mvn clean package -Dquarkus.kubernetes.deploy=true -DskipTests
```
