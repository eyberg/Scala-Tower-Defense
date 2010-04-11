import scala.actors.Actor
import scala.actors.Actor._

object Dtd {

  var colors = Array(Console.YELLOW_B, Console.BLUE_B, Console.GREEN_B, Console.RED_B, Console.BLINK) 
  var shapes = Array("X", "0", "%")

  // holds x,y positions of enemies
  var positions = new Array[Int](21)
  var log = new Array[String](21)

  def main(args: Array[String]) {

    val listener = new listenInput

    listener.start

    drawBoard()

    log(0) = "Game 1 -- Points 0"
    // send out initial wave
    sendWave()

    // loop through
    for(i <- 0 to 10) {
      positions = moveWave()
      redrawScreen()
      drawBoard()
      Thread.sleep(1000)
    }

  }

  // move enemies through the grid
  def moveWave():Array[Int] = {
    var result = new Array[Int](21)
    for(i <- 0 to 20) {
      result((i+2)%21) = positions(i)
    }

    result
  }

  // send out enemies
  def sendWave() {
    var enemies = 10

    for( i <- 0 to enemies) {
      var rand: scala.util.Random = new scala.util.Random()
      positions((rand.nextDouble()*positions.size-1).toInt) = 1
    }

  }

  // returns a random enemy
  def generateEnemy():String = {
    var rand: scala.util.Random = new scala.util.Random()

    var color = colors((rand.nextDouble()*colors.size-1).toInt) 
    var shape = shapes((rand.nextDouble()*shapes.size-1).toInt)

    color + shape + Console.RESET
 
  }

  // draw a tower
  def drawTower:String = {
    colors(4) + "T" + Console.RESET
  }

  // returns first available log line
  def availLogLine():Int = {
    var pos = 0
    for(x <- 0 to 20) {
      if(log(x) == null) {
        pos = x
        return pos
      }
    }
    pos
  }

  // draws one line of a sidebar
  def drawSideBar(line:Int):String = {
    "\t" + "|" + log(line) + "|"
  }

  // draw banner
  def drawBanner():String = {
    colors(3) + 
      ("-" * 10 ) + " Scala Tower Defense" + ("-" * 10) +
    Console.RESET
  }

  // draw game board as we see it
  def drawBoard() {
    println( drawBanner )

    println( ("-"*40) + "\t" + ("-" * 10) )
    for(i <- 0 to 20) {

      if (positions(i) == 1) {
        println("|" +  generateEnemy + (" " * 37) + "|" + drawSideBar(i) )
      } else if (positions(i) == 2) {
        println("|" + drawTower + (" " * 37) + "|" + drawSideBar(i) )
      } else {
        println("|"+(" " * 38)+"|" + drawSideBar(i) )
      }

    }
    println( ("-"*40) + "\t" + ("-" * 10) )
  }

  def redrawScreen() {
    println("\n"*20)
  }

  // add a tower to the grid
  def addTower() {
    log(availLogLine) = "Adding tower"
    positions(0) = 2
  }

  // pause game
  def pauseGame() {
    log(availLogLine) = "Pausing game.."
  }

  // destroy tower
  def destroyTower() {
    log(availLogLine) = "Destroying tower.."
  }

  // end game
  def endGame() {
    println("you'll be back")
    System.exit(0)
  }

  // process user input
  def processInput(uinput:String) {
    if(uinput == "t") {
      addTower
    } else if (uinput == "p") {
      pauseGame
    } else if (uinput == "d") {
      destroyTower
    } else if (uinput == "e") {
      endGame
    }
  }

  // listens for input continously
  // readline NEEDS a '\n' to process
  class listenInput() extends Actor {
    def act() {
      while(true) {
        var uinput = Console.readLine
        processInput(uinput)
      }
    }
  }

  
}