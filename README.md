Async Locator Refined Refined
===
Port to Minecraft 1.21.11

# 簡介
本專案是以 https://github.com/Alvaro842DEV/AsyncLocator-Refined 為基礎，直接透過AI: Gemini Pro 3，修改成可在Minecraft 1.21.11上運行的版本，並附上已Build過後的 `async-locator-refined-fabric-1.21.11-1.5.0.jar` 可直接放於 /mods 內的模組檔。

已測試在 Minecraft 1.21.11 Fabric Loader 0.17.3，可在僅伺服端安裝本模組、客戶端不需安裝的情況下，正常運行 `/locate structure minecraft:village_plains` ，且不佔據主線程。

# 免責聲明
本次專案是自用為主，完全由AI處理修改，我僅僅是手動Build出 .jar 檔案，並撰寫這份 README.md 文件。
因為程式碼修改是由AI完全處理的，使用狀況已經在上述所說明過的，對於可靠性、穩定度等，均不負擔任何責任，請自行評估使用。

# AsyncLocator
Changes the searching of features to be asynchronous to mitigate associated lag

Ported and maintained by alvaro842 - Original mod by bright_spark