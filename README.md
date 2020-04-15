# Movie Fun!

Smoke Tests require server running on port 8080 by default.

## Build WAR ignoring Smoke Tests

```
$ mvn clean package -DskipTests -Dmaven.test.skip=true
```

## Run Smoke Tests against specific URL

```
$ MOVIE_FUN_URL=http://moviefun.example.com mvn test
```

set VCAP_SERVICES={"p-mysql": [{"credentials": {"jdbcUrl": "jdbc:mysql://localhost:3306/albums?user=root&password=password&useSSL=false&allowPublicKeyRetrieval=true"}, "name": "albums-mysql"}, {"credentials": {"jdbcUrl": "jdbc:mysql://localhost:3306/movies?user=root&password=password&useSSL=false&allowPublicKeyRetrieval=true"}, "name": "movies-mysql"}]}

