# Troubleshooting

## `runOnSimulator` fails with font errors

### Symptom A: `MicroUIException: There is no font (platform and application)`

This means MicroUI couldn’t find any usable font at runtime.

Checklist:

1. **Verify fonts exist in the project resources**
   - Check `src/main/resources/fonts/` contains `*.ejf` files.
2. **Verify the font converter step is enabled**
   - In `configuration/common.properties`:
     - `ej.microui.fontConverter.useIt=true`
3. **Clean the build outputs and regenerate resources**

```powershell
cd "C:\Users\lucas\Documents\Assessement_node\microej-demo"
.\gradlew.bat clean runOnSimulator
```

If the simulator starts but crashes when opening a page, it can still be font-related (stylesheets often request `getStyle().getFont()`).

### Symptom B: `FONT GENERATOR ERROR [M5] - Invalid resource list file`

This happens when the font generator is fed an invalid “resource list” file.

Common causes:

- A property points to a list file that doesn’t exist or has a wrong format.
- A generated list file got corrupted in `build/`.

Checklist:

1. **Make sure the project is not forcing an explicit list file**
   - In `configuration/common.properties`, these should typically remain disabled unless you know you need them:
     - `ej.microui.fontConverter.file.enabled=false`
     - `ej.microui.fontConverter.file=` (empty)
2. **Clean** and rebuild (this often fixes stale/corrupt generated files)

```powershell
cd "C:\Users\lucas\Documents\Assessement_node\microej-demo"
.\gradlew.bat clean runOnSimulator --stacktrace
```

3. If it still fails, capture these artifacts for debugging:
   - the full `--stacktrace` output
   - `configuration/common.properties`
   - the content of `build/vee/scripts/init-microui-fontgen/` (if present)

## Simulator vs device limitations

- Pages that call board peripherals (GPIO/ADC/network/SD) may throw exceptions on simulator, depending on what the VEE Port mocks.
- This repo often catches `Throwable` in those pages and prints a message like `no BSP on simulator`.

## Useful diagnostics

```powershell
cd "C:\Users\lucas\Documents\Assessement_node\microej-demo"

# Print MicroEJ components and versions
.\gradlew.bat microejComponents

# Show dependency graph
.\gradlew.bat dependencies
```
