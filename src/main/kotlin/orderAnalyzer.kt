import OrdersAnalyzer.Order
import com.fasterxml.jackson.core.type.TypeReference
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDateTime
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.module.kotlin.readValue


class OrdersAnalyzer {

    data class Order(val orderId: Int,
                     val creationDate: LocalDateTime,
                     val orderLines: List<OrderLine>)

    data class OrderLine(val productId: Int,
                         val name: String,
                         val quantity: Int,
                         val unitPrice: BigDecimal)

    fun totalDailySales(orders: List<Order>): Map<DayOfWeek, Int> {
        val counters = IntArray(7)
        for (order in orders) {
            val day = order.creationDate.dayOfWeek.value-1
            for (orderLine in order.orderLines) counters[day]+=orderLine.quantity
        }
        val result = mutableMapOf<DayOfWeek,Int>()
        for(i in 0..6) if (counters[i]>0) result[DayOfWeek.of(i+1)]=counters[i]
        return result
    }
    fun zohar(orders: List<Order>) {
       val output =  orders.groupBy { it.creationDate }
        println(output)
    }
}

class Mock {
    val inputExample =
        """[
        {
            "orderId": 554,
            "creationDate": "2017-03-25T10:35:20",
            "orderLines": [
                {"productId": 9872, "name": "Pencil", "quantity": 3, "unitPrice": 3.00}
            ]
        },
        {
            "orderId": 555,
            "creationDate": "2017-03-25T11:24:20",
            "orderLines": [
                {"productId": 9872, "name": "Pencil", "quantity": 2, "unitPrice": 3.00},
                {"productId": 1746, "name": "Eraser", "quantity": 1, "unitPrice": 1.00}
            ]
        },
        {
            "orderId": 453,
            "creationDate": "2017-03-27T14:53:12",
            "orderLines": [
                {"productId": 5723, "name": "Pen", "quantity": 4, "unitPrice": 4.22},
                {"productId": 9872, "name": "Pencil", "quantity": 3, "unitPrice": 3.12},
                {"productId": 3433, "name": "Erasers Set", "quantity": 1, "unitPrice": 6.15}
            ]
        },
        {
            "orderId": 431,
            "creationDate": "2017-03-20T12:15:02",
            "orderLines": [
                {"productId": 5723, "name": "Pen", "quantity": 7, "unitPrice": 4.22},
                {"productId": 3433, "name": "Erasers Set", "quantity": 2, "unitPrice": 6.15}
            ]
        },
        {
            "orderId": 690,
            "creationDate": "2017-03-26T11:14:00",
            "orderLines": [
                {"productId": 9872, "name": "Pencil", "quantity": 4, "unitPrice": 3.12},
                {"productId": 4098, "name": "Marker", "quantity": 5, "unitPrice": 4.50}
            ]
        }
    ]
    """
    fun tester() {
        val mapper = jacksonObjectMapper()
            .findAndRegisterModules()
        try {
            val orders: List<Order> = mapper.readValue(inputExample)
            val totalDailySales = OrdersAnalyzer().totalDailySales(orders)
            println("totalDailySales: $totalDailySales")
            //---------
            OrdersAnalyzer().zohar(orders)
            //---------
        } catch (e: Exception) {
            println(e)
        }

    }
}
fun main() {
    Mock().tester()
}