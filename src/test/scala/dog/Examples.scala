package dog
package examples

object Examples extends Dog {

  val `fail example`: TestCase[Unit] = Assert.equal(0, 1)

  val `success example`: TestCase[Int] = for {
    _ <- Assert.equal(1, 1)
  } yield 1

  val `bind test case`: TestCase[Int] = for {
    a <- `success example`;
    _ <- Assert.equal(a, 2)
  } yield 2

  val `soft assertion` =
    Assert.equal(0, 0) +> // pass
    Assert.equal(0, 1) +> // violate
    Assert.equal(1, 1) // continue assertion

  val `throw exception`: TestCase[Unit] = {
    def f: Int = throw new Exception("oops!")
    val target: TestCase[Throwable] = Assert.trap(f)
    for {
      e <- target
      _ <- Assert.equal("oops!", e.getMessage) +>
        (e match {
          case e: Exception => Assert.pass(())
          case _ => Assert.fail("expected Exception, but not")
        })
    } yield ()
  }

  val `no side effect` = {
    var value = 0
    val `side effect once`: TestCase[Int] = {
      value += 1
      Assert.pass(value)
    }
    for {
      a <- `side effect once`
      b <- `side effect once`
      _ <- Assert.equal(1, value) +>
        Assert.equal(1, a) +>
        Assert.equal(1, b)
    } yield ()
  }

  val `side effect` = {
    var value = 0
    def `delay test` = TestCase.delay {
      value += 1
      val v = value
      Assert.pass(v)
    }
    for {
      a <- `delay test`
      b <- `delay test`
      _ <- Assert.equal(1, value) +>
        Assert.equal(1, a) +>
        Assert.equal(1, b)
    } yield ()
  }
}
