package com.github.opengrabeso.loctio
package common.model.github

import java.time.ZonedDateTime

import rest.EnhancedRestDataCompanion

case class Notification(
  id: String,
  repository: Repository,
  subject: Subject,
  reason: String,
  unread: Boolean,
  updated_at: ZonedDateTime,
  last_read_at: ZonedDateTime,
  url: String,
  subscription_url: String
)

object Notification extends EnhancedRestDataCompanion[Notification]