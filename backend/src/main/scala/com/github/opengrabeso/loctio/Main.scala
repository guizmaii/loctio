package com.github.opengrabeso.loctio

import java.util.Properties

import com.fasterxml.jackson.databind.JsonNode
import com.google.api.client.http.{GenericUrl, HttpHeaders, HttpResponseException}
import io.udash.rest.raw.HttpErrorException

object Main extends common.Formatting {

  import RequestUtils._

  case class SecretResult(users: Set[String], error: String)

  def secret: SecretResult = {
    val filename = "/secret.txt"
    try {
      val secretStream = Main.getClass.getResourceAsStream(filename)
      val lines = scala.io.Source.fromInputStream(secretStream).getLines
      val usersLine = lines.next()
      val users = usersLine.split(",").map(_.trim.toLowerCase)
      SecretResult(users.toSet, "")
    } catch {
      case _: NullPointerException => // no file found
        SecretResult(Set.empty, s"Missing $filename, app developer should check README.md")
      case _: Exception =>
        SecretResult(Set.empty, s"Bad $filename, app developer should check README.md")
    }
  }

  def devMode: Boolean = {
    Option(getClass.getResourceAsStream("/config.properties")).exists { is =>
      val prop = new Properties()
      prop.load(is)
      prop.getProperty("devMode").toBoolean
    }
  }

  case class GitHubAuthResult(token: String, login: String, fullName: String) {
    // userId used for serialization, needs to be stable, cannot be created from a token
    lazy val userId: String = login
    def displayName: String = if (fullName.isEmpty) login else fullName
  }

  private def authRequest(token: String): JsonNode = {
    try {
      val request = requestFactory.buildGetRequest(new GenericUrl("https://api.github.com/user"))
      val headers = if (request.getHeaders != null) request.getHeaders else request.setHeaders(new HttpHeaders).getHeaders
      headers.setAuthorization("Bearer " + token)
      val response = request.execute() // TODO: async?

      jsonMapper.readTree(response.getContent)
    } catch {
      case e: HttpResponseException if e.getStatusCode == 401 || e.getStatusCode == 403 =>
        throw HttpErrorException(e.getStatusCode, e.getStatusMessage)
      case e: HttpResponseException =>
        println(s"Unexpected auth error $e")
        throw HttpErrorException(e.getStatusCode, e.getStatusMessage)
      case ex: Exception =>
        throw HttpErrorException(500, "Unexpected error when authenticating with GitHub")
    }
  }

  def authorized(login: String): Unit = {
    val SecretResult(users, _) = secret
    if (!users.contains(login.toLowerCase)) {
      throw HttpErrorException(403, s"User $login not authorized. Contact server administrator to get the access")
    }
  }

  def gitHubAuth(token: String): GitHubAuthResult = {
    val responseJson = authRequest(token)

    val login = responseJson.path("login").textValue
    val name = responseJson.path("name").textValue

    authorized(login)

    val auth = GitHubAuthResult(token, login, name)
    rest.RestAPIServer.createUser(auth)
  }

}





