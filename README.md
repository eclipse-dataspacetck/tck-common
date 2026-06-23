# Technology Compatibility Kit Commons

Shared code that is used by the Technology Compatibility Kits and the build plugins alike.

## TckRuntime Configuration

`TckRuntime` accepts configuration properties via the `Builder#property(key, value)` or `Builder#properties(map)` methods. These properties are also read from Java system properties (or environment variables as fallback).

### Configuration Properties

| Property | Default | Description |
|---|---|---|
| `dataspacetck.launcher` | — | Fully-qualified class name of the `SystemLauncher` implementation to use. Required unless set via `Builder#launcher(Class)`. |
| `dataspacetck.callback.address` | `http://localhost:8083` | Callback address advertised to the system under test. |
| `dataspacetck.host` | `0.0.0.0` | Host on which the TCK callback listener binds. |
| `dataspacetck.port` | `8083` | Port on which the TCK callback listener binds. |
| `dataspacetck.debug` | `false` | Enable debug-level console output. |
| `dataspacetck.ansi` | `true` | Enable ANSI color codes in console output. |
| `dataspacetck.filters.tags.include` | — | Comma-separated list of JUnit 5 tags. Only tests carrying at least one of these tags are executed. |
| `dataspacetck.filters.tags.exclude` | — | Comma-separated list of JUnit 5 tags. Tests carrying any of these tags are skipped. |

### Example

```java
var summary = TckRuntime.Builder.newInstance()
        .launcher(MySystemLauncher.class)
        .addPackage("com.example.tck.tests")
        .property("dataspacetck.callback.address", "http://my-host:9090")
        .property("dataspacetck.filters.tags.include", "mandatory,core")
        .property("dataspacetck.filters.tags.exclude", "experimental")
        .build()
        .execute();
```

When both include and exclude filters are set, the include filter is applied first and the exclude filter is applied to the remaining tests.