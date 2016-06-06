scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6", "2.11.8")

scalacOptions ++= Seq("-feature")

fullResolvers ~= {_.filterNot(_.name == "jcenter")}

dogSettings
dogVersion := "0.2.0"
