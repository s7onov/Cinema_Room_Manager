package cinema

import java.lang.NumberFormatException
import java.util.*

const val FrontSeatPrice = 10
const val BackSeatPrice = 8
const val SmallRoomSize = 60
const val VACANT = 'S'
const val OCCUPIED = 'B'
const val MENU = """
    
    1. Show the seats
    2. Buy a ticket
    3. Statistics
    0. Exit
    """

class CinemaRoom(private val rows: Int = 7, private val cols: Int = 8) {
    private val totalSeats = rows * cols
    private val seats = Array(rows) { Array(cols) { VACANT } }
    private var currentIncome = 0

    private fun getPriceForRow(row: Int): Int = when {
        totalSeats <= SmallRoomSize -> FrontSeatPrice
        row <= rows / 2 -> FrontSeatPrice
        else -> BackSeatPrice
    }

    private fun getTotalIncome(): Int {
        var totalIncome = 0
        for (i in 1..rows)
            totalIncome += getPriceForRow(i) * cols
        return totalIncome
    }

    fun showRoom() {
        println("\nCinema:")
        print(" ")
        for (i in 1 .. cols)
            print(" $i")
        println()
        for (i in 1 .. rows)
            println("$i " + seats[i - 1].joinToString(" "))
    }

    private fun buyTicket(): Int {
        println("\nEnter a row number:")
        val pickedRowString = readln()
        println("Enter a seat number in that row:")
        val pickedSeatString = readln()
        try {
            val pickedRow = pickedRowString.toInt()
            val pickedSeat = pickedSeatString.toInt()
            if (seats[pickedRow - 1][pickedSeat - 1] == OCCUPIED) {
                println("\nThat ticket has already been purchased!")
            } else {
                seats[pickedRow - 1][pickedSeat - 1] = OCCUPIED
                val ticketPrice = getPriceForRow(pickedRow)
                currentIncome += ticketPrice
                println("Ticket price: $$ticketPrice")
                return ticketPrice
            }
        } catch (ex: NumberFormatException) {
            println("\nWrong input!")
        } catch (ex: ArrayIndexOutOfBoundsException) {
            println("\nWrong input!")
        }
        return 0
    }

    fun showStatistics() {
        val purchasedTickets = seats.contentDeepToString().count { it == OCCUPIED }
        println("\nNumber of purchased tickets: $purchasedTickets")
        val percentage = try {
            100.0 * purchasedTickets / totalSeats
        } catch (ex: ArithmeticException) {
            100.0
        }
        val formatPercentage = "%.2f".format(Locale.US, percentage)
        println("Percentage: $formatPercentage%") // 0.00
        println("Current income: $$currentIncome")
        println("Total income: $" + getTotalIncome())
    }

    fun buyATicketLoop() {
        do {
            val ticketPrice = buyTicket()
        } while (ticketPrice == 0)
    }
}

fun main() {
    println("Enter the number of rows:")
    val r = readln().toInt()
    println("Enter the number of seats in each row:")
    val c = readln().toInt()
    val cinemaRoom = CinemaRoom(r, c)
    do {
        println(MENU.trimIndent())
        val input = readln().toInt()
        when (input) {
            1 -> cinemaRoom.showRoom()
            2 -> cinemaRoom.buyATicketLoop()
            3 -> cinemaRoom.showStatistics()
        }
    } while (input != 0)
}