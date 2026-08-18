package com.example.evspot.data

import android.content.Context
import com.example.evspot.data.model.BatteryStatus
import java.io.BufferedReader
import java.io.InputStreamReader

class BatteryRepository(private val context: Context) {
    fun loadBatteryData(): List<BatteryStatus> {
        val batteryData = mutableListOf<BatteryStatus>()
        try {
            val inputStream = context.assets.open("range_data.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine() // Skip header
            var line: String? = reader.readLine()
            while (line != null) {
                if (line.isBlank()) {
                    line = reader.readLine()
                    continue
                }
                val tokens = line.split(",")
                if (tokens.size >= 7) {
                    val status = BatteryStatus(
                        soc = tokens[0].toDoubleOrNull() ?: 0.0,
                        batteryTemp = tokens[1].toDoubleOrNull() ?: 0.0,
                        speed = tokens[2].toDoubleOrNull() ?: 0.0,
                        acOn = (tokens[3] == "1" || tokens[3].toDoubleOrNull() == 1.0),
                        distanceTravelled = tokens[4].toDoubleOrNull() ?: 0.0,
                        energyConsumed = tokens[5].toDoubleOrNull() ?: 0.0,
                        remainingRange = tokens[6].toDoubleOrNull() ?: 0.0,
                        voltage = 350.0 + ((tokens[0].toDoubleOrNull() ?: 0.0) / 100.0 * 50.0)
                    )
                    batteryData.add(status)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return batteryData
    }
}
