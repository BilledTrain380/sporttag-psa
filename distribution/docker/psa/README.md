# billedtrain380/psa

[PSA](https://github.com/BilledTrain380/sporttag-psa) application as docker image.

## Usage
* Mount db volume
  * PSA uses a file based [H2 Database](https://www.h2database.com/). The db volume can be mounted
  to `/opt/psa/db`
  
* Define db username and password
  * You can override the default database username (`psa`) and password (`pass`) with environment variables
  
* PSA Docker images are tagged by their application version suffixed with the short git commit hash of the build time.
eg. `2.2.0-g77f415a`

## Examples
### Simplest docker run example
```
docker run -d -p 8080:8080 billedtrain380/psa/<tag>
```

### Sharing the database from your computer
Let's mount the db directory:
```
docker run -d -p 8080:8080 -v /host/db:/opt/psa/db billedtrain380:<tag>
```

### Override the database username and password
Let's override our own username and password for the database:
```
docker run -d -p 8080:8080 -v /host/db:/opt/psa/db -e spring_datasource_username=foo -e spring_datasource_password=bar billedtrain380:<tag>
```
