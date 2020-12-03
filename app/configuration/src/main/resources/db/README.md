# Flyway migration script guide

## Versioning

Migration file versions must follow the pattern:

`V{PSA-major-version}.{PSA-minor-version}.{script-id}`

Where:
* `{PSA-major-version}` shall be replaced with the major version of PSA where the script will be applied
* `{PSA-minor-version}` shall be replaced with the minor version of PSA where the script will be applied
* `{script-id}` shall be an incremented number for each new script starting with `0`
 
### Devel migrations
Migrations for development only must follow the pattern:

`V{PSA-major-version}.{PSA-minor-version}.{last-script-id}.{script-id}`

Where:
* `{PSA-major-version}` shall be replaced with the major version of PSA where the script will be applied
* `{PSA-minor-version}` shall be replaced with the minor version of PSA where the script will be applied
* `{last-script-id}` shall be the script id of the last productive migration script
* `{script-id}` shall be an incremented number for each new script starting with `0`
