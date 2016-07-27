package com.pierangeloc.networking

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import io.github.algd.oauth.data.DataManager
import io.github.algd.oauth.data.model.{AuthorizationData, Client}
import io.github.algd.oauth.utils.OAuthParams

import scala.concurrent.{ExecutionContext, Future}

object AuthorizationServerOauth extends App with StreamingFacilities {



//  val myDataManager: DataManager[User] = new MyDataManager 


  val route: Route =
    get {
      path("hello") {
        complete {
          "Welcome!"
        }
      } ~
        path("buongiorno") {
          complete {
            "Benvenuto!"
          }
        } ~
        path("fail") {
          failWith(new Exception())
        }
    }

  Http().bindAndHandle(route, "localhost", 8082)

}

object ClientApp extends App with StreamingFacilities {

  val route: Route =
    get {
      path("hello") {
        complete {
          "Welcome!"
        }
      } ~
        path("buongiorno") {
          complete {
            "Benvenuto!"
          }
        } ~
        path("fail") {
          failWith(new Exception())
        }
    }

  Http().bindAndHandle(route, "localhost", 8081)

}