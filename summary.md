## Tổng quan

| #   | Page                  | Board Feature                        | API Java                                                               | BSP C Function              | Dữ liệu thật trên board             |
| --- | --------------------- | ------------------------------------ | ---------------------------------------------------------------------- | --------------------------- | ----------------------------------- |
| 1   | **Device & System**   | Device ID + RAM (320KB)              | `Device.getArchitecture()`, `Device.getId()`, `Runtime.freeMemory()`   | Chip register read          | Architecture, unique ID, heap stats |
| 2   | **Display Hardware**  | LCD 480×272 TFT (16bpp, 60Hz)        | `Display.getWidth()`, `Display.getHeight()`, `Painter.fillRectangle()` | LTDC + DMA2D driver         | Resolution, FPS benchmark           |
| 3   | **Touch Hardware**    | Touch screen (FT5336 IC, I2C)        | `Pointer.getX()`, `Pointer.getY()`, `Buttons.getAction()`              | I2C touch IC driver         | Tọa độ ngón tay, events/sec         |
| 4   | **LED & User Button** | LED xanh (PI1) + Nút bấm xanh (PI11) | `GPIO.setDigitalValue()`, `GPIO.getDigitalValue()`                     | HAL_GPIO_WritePin / ReadPin | Toggle/blink LED, đọc nút vật lý    |
| 5   | **MCU Temp Sensor**   | Internal temp sensor (ADC1 Ch18)     | `GPIO.setMode(ANALOG_INPUT)`, `GPIO.getAnalogValue()`                  | HAL_ADC_GetValue            | Nhiệt độ chip thật (°C)             |
| 6   | **File System**       | SD card (SDMMC)                      | `FileOutputStream.write()`, `FileInputStream.read()`, `File.delete()`  | SDMMC + FATFS driver        | Ghi/đọc/xóa file trên thẻ SD        |
| 7   | **Network**           | Ethernet (LAN8742A, RMII)            | `InetAddress.getByName()`, `Socket.connect()`                          | lwIP TCP/IP stack           | DNS resolve, TCP connect            |

## Call chain

Java code
│
▼ SNI (Simple Native Interface)
C native function (VEE Port)
│
▼ BSP HAL
Hardware register (chip thật)

````

## Chạy

```bash
cd microej-demo-showcase
./gradlew runOnSimulator   # Test trên PC (một số API sẽ throw exception vì không có BSP thật)
./gradlew runOnDevice
````
