# Configuration

All settings in `src/main/resources/config.properties`:

```properties
api.base.url=https://demo.inventree.org    # Target API
api.auth.strategy=TOKEN                    # TOKEN or BASIC
api.request.timeout.ms=20000               # REST Assured timeout
api.logging.enabled=true                   # REST Assured request/response logs
```

Override properties at runtime:

```bash
mvn test -Dapi.base.url=http://localhost:8000
```