

````commandline
ab -k -c 5000 -n 100000 -A hans:dampf http://localhost:8080/order/1
````

## Build
``
mvn clean install spring-boot:repackage
``
