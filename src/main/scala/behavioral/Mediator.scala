package behavioral

trait ChatMediator {
  def join(newMember: ChatMember)

  def send(message: String, sender: ChatMember)
}

trait ChatMember {
  def receive(message: String)

  def send(messge: String)
}

class PrintingChatMember(name: String, chatMediator: ChatMediator) extends ChatMember {
  chatMediator.join(this)

  override def receive(message: String) = println(name + " received message: " + message)

  override def send(messge: String) = chatMediator.send(messge, this)
}

class FanoutChatMediator extends ChatMediator {
  var chatMembers: List[ChatMember] = List()

  def join(newMember: ChatMember) = {
    chatMembers = newMember :: chatMembers
  }

  override def send(message: String, sender: ChatMember): Unit = {
    chatMembers.
      filter(member => member ne sender).
      foreach(member => member.receive(message))
  }
}

object Mediator extends App {
  val chat = new FanoutChatMediator()

  val sheldon = new PrintingChatMember("Sheldon", chat)
  val penny = new PrintingChatMember("Penny", chat)

  sheldon.send("Hello!")
  sheldon.send("How are you?")

  val lenard = new PrintingChatMember("Lenard", chat)

  penny.send("I'm ok! How about you?")
}

