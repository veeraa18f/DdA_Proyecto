# Tests

## Robolectric, sin emulador

Estos son los tests principales del proyecto. Se ejecutan en local y no necesitan dispositivo.

Desde Android Studio:

1. Abre `app/src/test/java/com/tuempresa/investtrack/RobolectricTests.java`.
2. Pulsa el boton verde junto a la clase `RobolectricTests`.
3. Si Android Studio pregunta el tipo de ejecucion, usa Gradle/JUnit local, no Android Instrumented Test.

Desde terminal:

```bash
gradle testDebugUnitTest
```

## Espresso, con emulador o movil

El test `BasicEspressoTest` esta en `app/src/androidTest`. Ese test necesita un emulador abierto o un movil conectado.

Desde terminal:

```bash
gradle connectedDebugAndroidTest
```

Si no tienes dispositivo/emulador, este test no puede ejecutarse. Aun asi puede compilarse con:

```bash
gradle assembleDebugAndroidTest
```
