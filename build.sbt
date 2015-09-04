scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.5", "2.11.7")

scalacOptions ++= Seq("-feature")

fullResolvers ~= {_.filterNot(_.name == "jcenter")}

dogSettings
dogVersion := "0.1.5"
