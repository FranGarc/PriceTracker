package com.franciscogarciagarzon.acceptance_test.steps

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.Converters
import com.google.gson.Gson
import io.cucumber.java.Before
import kotlinx.coroutines.runBlocking

class Hooks {

    private var database: AppDatabase? = null

    @Before(order = 0) // El orden 0 asegura que sea lo primero
    fun setup() {
        ComposeInitializer.initialize()

        // Obtenemos el contexto de instrumentación
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "price_tracker_db"
        )
            .addTypeConverter(Converters(Gson()))
            .build()
        // Ejecutamos la limpieza de forma síncrona/bloqueante
        // runBlocking detendrá la ejecución del test hasta que clearDatabase termine
        // así nos aseguramos que todas las pruebas empiezan de cero
        runBlocking {
            database?.apply {
                clearDatabase()
                // Importante cerrar la conexión después de limpiar para que la App pueda abrirla
                    close()
            }

        }
    }
}