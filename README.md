# Asset Eligibility

A small Java library for defining and evaluating eligibility criteria against industrial assets.

An asset is represented as a `Map<String, String>`, where each entry describes a property or telemetry attribute.

The project was developed as a technical case study for OverIT.

## Requirements

- Java 21
- Apache Maven 3

The production code uses only the Java standard library. JUnit 5 is included as a test-scoped dependency.

## Build and test

Run the complete test suite with:

```bash
mvn clean test
```

No application framework or external runtime is required.

## Usage

An asset can be represented as follows:

```java
Map<String, String> sensor = new HashMap<>();

sensor.put("zone", "Lombardy-Plant-A");
sensor.put("isOnline", "true");
sensor.put("batteryLevel", "85");
sensor.put("currentTemperature", "42.5");
sensor.put("maxAllowedTemperature", "80");
```

A filter can then be created programmatically:

```java
AssetFilter eligibilityFilter = Filters.and(
    Filters.equalTo("zone", "Lombardy-Plant-A"),
    Filters.equalTo("isOnline", "true"),
    Filters.numeric(
        "batteryLevel",
        20,
        NumericComparisonOperator.GREATER_THAN_OR_EQUAL
    )
);

boolean eligible = eligibilityFilter.matches(sensor);
```

The result is `true` because the sensor:

- belongs to the required zone;
- is online;
- has a battery level greater than or equal to 20.

Filters do not store evaluation results. The same filter can therefore be reused after the asset changes:

```java
sensor.put("batteryLevel", "10");

boolean stillEligible = eligibilityFilter.matches(sensor);
```

In this case, `stillEligible` is `false`.

## Testing

The test suite covers individual filters, logical composition, missing and invalid values, case sensitivity, numeric edge cases, and realistic eligibility scenarios.

## Main design decisions

### Filter representation and composition

Every filter implements the `AssetFilter` interface:

```java
boolean matches(Map<String, String> asset);
```

Atomic filters are responsible for one condition, such as checking the existence of a property or performing a numeric comparison.

Logical filters contain other `AssetFilter` instances. This allows expressions to be composed and nested without coupling logical filters to specific implementations:

```java
Filters.and(
    Filters.equalTo("zone", "Lombardy-Plant-A"),
    Filters.or(
        Filters.equalTo("isOnline", "true"),
        Filters.equalTo("emergencyMode", "true")
    )
);
```

The `Filters` class provides the public factory methods used to construct the different filters. Client code therefore depends primarily on `AssetFilter` rather than on individual implementation classes.

### Missing and invalid values

A missing property causes existence, equality, and numeric comparison filters to return `false`.

An empty `AND` expression evaluates to `true`, while an empty `OR` expression evaluates to `false`.

A numeric comparison also returns `false` when a value:

- Cannot be parsed as a number
- Is `NaN`
- Is positive or negative infinity

These cases are treated as non-matches rather than exceptional conditions because asset telemetry may be incomplete or temporarily invalid during normal evaluation.

A null asset is considered a programming error and causes a `NullPointerException`. Invalid filter configuration, such as a null property name or operator, is also rejected immediately.

### Numeric representation

Numeric values are parsed as `double`.

This is sufficient for comparisons involving telemetry values such as temperatures and battery levels. The component does not perform arithmetic or handle monetary values, so the additional precision and complexity of `BigDecimal` were not considered necessary for the current scope.


### Implemented operators

The implementation includes:

- Constant filters: `ALWAYS` and `NEVER`
- Property existence
- Case-insensitive string equality
- Numeric equality
- Numeric comparisons: `>`, `>=`, `<`, and `<=`
- Numeric comparison between two properties
- Logical composition through `AND`, `OR`, and `NOT`

This set covers the scenarios described in the assignment while providing a representative foundation for additional operators. I deliberately avoided adding redundant operations such as `NOT_EQUAL`, because they can already be expressed by composing `NOT` with an equality filter.

### Extending the design

A new numeric operator requires a new value in `NumericComparisonOperator`, its implementation in `evaluate`, and the corresponding tests. The numeric filters already delegate comparison behaviour to the enum and therefore do not need to change.

A new category of condition can be introduced by implementing `AssetFilter` and optionally exposing it through `Filters`. It can then be composed immediately through `AND`, `OR`, and `NOT`. Possible extensions include date comparisons, semantic-version checks, numeric ranges, and blank-value validation.
