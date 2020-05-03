# PSA DTO module

Contains the source code for psa dtos.

## Concept
Dtos are also used to send them over http. Therefore, different kind of dtos of the
same domain model exist. Which dto to use depends on the request.

### Naming convention

* `SomeSortOfInput`
    * Dtos ending with `Input` are meant to use in a `POST` request.
      They require all properties of a dto which must exist.

* `SomeSortOfElement`
    * Dtos ending with `Element` are meant to use in a `PATCH` request.
      They require non of the properties and only the given properties will be updated.

* `SomeSortOfRelation`
    * Dtos ending with `Relation` are meant to use in a `PUT` request.
      They require non of the properties and only the given properties will be updated

* `SomeSortOfDto`
    * Dtos ending with `Dto` are meant to use in a `GET` request.
      They contain all the properties of domain model which must exists