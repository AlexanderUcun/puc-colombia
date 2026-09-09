package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.PucAccountEntity
import com.example.data.local.PucDatabase
import com.example.data.local.PucRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PucRoomDatabaseTest {

    private lateinit var db: PucDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PucDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndSearchPucAccounts() = runBlocking {
        val testAccount = PucAccountEntity(
            code = "110505",
            name = "Caja general",
            nature = "DEBITO",
            level = "SUBCUENTA",
            description = "Registra la existencia en dinero efectivo o en cheques con que cuenta el ente económico.",
            debitDynamic = "Por las entradas de dinero en efectivo y los cheques recibidos por cualquier concepto.",
            creditDynamic = "Por el valor de las consignaciones diarias en cuentas corrientes bancarias o de ahorro."
        )

        db.pucDao().insertAll(listOf(testAccount))

        val count = db.pucDao().getAccountCount()
        assertEquals(1, count)

        val retrieved = db.pucDao().getAccountByCode("110505")
        assertNotNull(retrieved)
        assertEquals("Caja general", retrieved?.name)

        val searchResult = db.pucDao().searchAccounts("Caja").first()
        assertTrue("Search should return Caixa", searchResult.isNotEmpty())
        assertEquals("110505", searchResult.first().code)
    }
}
