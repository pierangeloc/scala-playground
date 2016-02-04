val s = "Piero"

val l = List(1,2,3,4,5)

val xss = List(List(1), List(2,3,4), List(4,5,6), List(7))
import com.pierangeloc.exercises.Sudoku
import Sudoku._
Sudoku.boardSize
Sudoku.cartProd(xss)
val table = """
  |435269781
  |682571493
  |197834562
  |826195347
  |374682915
  |951743628
  |519326874
  |248957136
  |763418259
""".stripMargin
val board = fromString(table)
solve(board)
val table2 = """
               |035269781
               |682571493
               |197834562
               |826195347
               |374682915
               |951743628
               |519326874
               |248957136
               |763418259
             """.stripMargin
solve(fromString(table2))
//val table3 = """
//              |000260701
//              |680070093
//              |190004500
//              |820100040
//              |004602900
//              |050003028
//              |009300074
//              |040050036
//              |703018000
//            """.stripMargin
//solve(fromString(table3))
