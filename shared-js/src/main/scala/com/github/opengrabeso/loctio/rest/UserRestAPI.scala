package com.github.opengrabeso.loctio
package rest

import common.model._
import io.udash.rest._
import io.udash.rest.raw.HttpBody
import com.avsystem.commons.rpc.{AsRaw, AsReal}

import scala.concurrent.Future

trait UserRestAPI {
  import UserRestAPI._

  @GET
  def name: Future[(String, String)]

  @POST
  def listUsers(ipAddress: String, state: String): Future[Seq[(String, LocationInfo)]]

  @PUT
  def setLocationName(login: String, location: String): Future[Seq[(String, LocationInfo)]]

  @POST
  @CustomBody
  def shutdown(data: RestString): Future[Unit]
}

object UserRestAPI extends RestApiCompanion[EnhancedRestImplicits,UserRestAPI](EnhancedRestImplicits) {

  case class RestString(value: String) extends AnyVal
  object RestString extends RestDataWrapperCompanion[String, RestString] {
    implicit object asRaw extends AsRaw[HttpBody, RestString] {
      override def asRaw(real: RestString) = HttpBody.textual(real.value)
    }
    implicit object asReal extends AsReal[HttpBody, RestString] {
      override def asReal(raw: HttpBody) = RestString(raw.readText())
    }
  }
}