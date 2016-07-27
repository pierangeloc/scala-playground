trait Thing[-A, +B] {
  type X

  def source(a: A): X
  def sink(a: X): B

  def apply(x: X): X
}

def bippy(t: Thing[Int, String]): String = {
  val state: t.X = t.source(42)
  val state2: t.X = t(state)
  t.sink(state)
}

//virtual classes
//abstract user module: User is just abstract and we know nothing about it
trait UserModule {
  //...
  type User <: UserLike
  trait UserLike { this: User =>
    def id: String
    def name: String
    def save(): Unit
  }

  def User(id: String, name: String): User
}

trait MongoUserModule extends UserModule {

  class User(_id: String, _name: String) extends UserLike {
    def save: Unit = ???
    def id = _id
    def name = _name
  }

  def User(id: String, name: String ) = new User()
}

//nanage lifecycle
trait Lifecycle {
  def startup(): Unit
  def shutdown(): Unit
}


trait MongoUserModule2  extends UserModule with Lifecycle {
  abstract override def startup(): Unit = {
    //this calls the startup on any other cycle. This performs like the decorator pattern
    //order depends on how the extension is declared (which order)
    super.startup()
    initMongoThingy()
  }

  abstract override def shutdown(): Unit = {
    killMongoThingy()
    super.shutdown()
  }

  def initMongoThingy() = ???
  def killMongoThingy() = ???
}
