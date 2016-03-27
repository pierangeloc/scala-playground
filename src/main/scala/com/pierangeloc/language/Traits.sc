import scala.io.Source
import scala.xml.XML

trait Friend {
  val name: String
  def listen() = println("I'm " + name + " listening")
}

class Human(val name: String) extends Friend

class Animal(val name: String)

class Dog(override val name: String) extends Animal(name) with Friend

class Cat(override val name: String) extends Animal(name)


val sam = new Human("Sam")

sam.listen
val tom = new Cat("Tom") with Friend
tom.listen()
abstract class Writer {
  def write(s: String)
}

class StringWriter extends Writer {
  val target = new StringBuilder
  def write(s: String): Unit = target.append(s)

  override def toString() = target.toString()
}

def writeStuff(writer: Writer): Unit = {
  writer.write("Italy Netherlands US India")
  println(writer)
}

writeStuff(new StringWriter)

trait UpperCaser extends Writer {
  //NB this super is not the super class, but the next guy on the chain (from right to left)
  abstract override def write(s: String) = super.write(s.toUpperCase())
}

trait FilterOutItaly extends Writer {
  abstract override def write(s: String) = super.write(s.split(" ").filterNot(_.equals("Italy")).mkString(" "))
}

writeStuff(new StringWriter with UpperCaser)
writeStuff(new StringWriter with UpperCaser with FilterOutItaly)
writeStuff(new StringWriter with FilterOutItaly with UpperCaser)
val data = Source.fromURL("https://feeds.finance.yahoo.com/rss/2.0/headline?s=yhoo&region=US&lang=en-US").mkString
val xml = XML.loadString(data)
xml \\ "channel" \\ "item"
val weatherXml = XML.load("http://api.openweathermap.org/data/2.5/weather?q=Amsterdam&mode=xml&appid=b1b15e88fa797225412429c1c50c122a")
case class Temperatures(current: Double, min: Double, max: Double)
val temperature = (weatherXml \ "temperature").map {
  temp => Temperatures((temp \ "@value").text.toDouble, (temp \ "@min").text.toDouble, (temp \ "@max").text.toDouble)
}
//Temperatures(temperature \ "@value".toDouble, temperature \ "@min".toDouble , temperature \ "@max".toDouble)


//todo: option, future
