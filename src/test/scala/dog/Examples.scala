package dog
package examples

object Examples extends Dog with Assert {

  val `fail example` = TestCase {
    equal(0, 1)
  }

  val `success example` = TestCase {
    for {
      _ <- equal(1, 1).monadic
    } yield 1
  }

  val `bind test case` = TestCase {
    for {
      a <- `success example`;
      _ <- equal(a, 2).monadic
    } yield 2
  }

  val `soft assertion` = TestCase {
    Assertion3(
      equal(0, 0), // pass
      equal(0, 1), // violate
      equal(0, 2) // continue assertion
    )
  }

  val `throw exception` = TestCase {
    def f: Int = throw new Exception("oops!")
    val target = trap(f)
    for {
      e <- target
      _ <- Assertion2(
        equal("oops!", e.getMessage),
        (e match {
          case e: Exception => pass(())
          case _ => fail("expected Exception, but not")
        })
      ).monadic
    } yield ()
  }

  val `no side effect` = {
    var value = 0
    val `side effect once` = TestCase {
      value += 1
      pass(value)
    }
    for {
      a <- `side effect once`.monadic
      b <- `side effect once`.monadic
      _ <- Assertion3(
        equal(1, value),
        equal(1, a),
        equal(1, b)
      ).monadic
    } yield ()
  }

  val `side effect` = TestCase {
    var value = 0
    def `delay test`() = TestCase {
      value += 1
      pass(value)
    }
    for {
      a <- `delay test`().monadic
      b <- `delay test`().monadic
      _ <- Assertion3(
        equal(1, value),
        equal(1, a),
        equal(1, b)
      ).monadic
    } yield ()
  }
}
