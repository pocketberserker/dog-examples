package dog
package examples

object Examples extends Dog with Assert {

  val `fail example` = TestCase {
    equal(0, 1)
  }

  val `success example` = TestCase {
    for {
      _ <- equal(1, 1).lift
    } yield 1
  }

  val `bind test case` = TestCase {
    for {
      a <- `success example`;
      _ <- equal(a, 2).lift
    } yield 2
  }

  val `soft assertion` = TestCase {
    assert
      .equal(0, 0) // pass
      .equal(0, 1) // violate
      .equal(0, 2) // continue assertion
  }

  val `throw exception` = TestCase {
    def f: Int = throw new Exception("oops!")
    val target = trap(f)
    for {
      e <- target
      _ <- assert
        .equal("oops!", e.getMessage)
        .lift
      _ <- (e match {
        case e: Exception => pass(())
        case _ => fail("expected Exception, but not")
      }).lift
    } yield ()
  }

  val `no side effect` = {
    var value = 0
    val `side effect once` = TestCase {
      value += 1
      pass(value)
    }
    for {
      a <- `side effect once`.lift
      b <- `side effect once`.lift
      _ <- assert
        .equal(1, value)
        .equal(1, a)
        .equal(1, b)
        .lift
    } yield ()
  }

  val `side effect` = TestCase {
    var value = 0
    def `delay test`() = TestCase {
      value += 1
      pass(value)
    }
    for {
      a <- `delay test`().lift
      b <- `delay test`().lift
      _ <- assert
        .equal(1, value)
        .equal(1, a)
        .equal(1, b)
        .lift
    } yield ()
  }

  val `skip test case ` = TestCase {
    fail[Unit]("oops!")
  }.skip("skip example")
}
