package behavioral

import org.scalatest._

class MediatorSpec extends FlatSpec with Matchers {

  class SendingChatMember(chatMediator: ChatMediator) extends ChatMember {
    chatMediator.join(this)

    override def receive(message: String): Unit = fail("Sending member shouldn't not be notified")

    override def send(message: String): Unit = chatMediator.send(message, this)
  }

  class ReceivingChatMember(chatMediator: ChatMediator) extends ChatMember {
    chatMediator.join(this)

    override def receive(message: String): Unit = assert(message == "Test message")

    override def send(message: String): Unit = fail("Receiving member should not send any messages")
  }

  "Mediator" should "notify all other collaborators" in {
    val mediator = new FanoutChatMediator
    val sendingChatMember = new SendingChatMember(mediator)
    new ReceivingChatMember(mediator)

    sendingChatMember.send("Test message")
  }
}
