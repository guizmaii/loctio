package com.github.opengrabeso.loctio
package common.model.github

import java.time.ZonedDateTime

import rest.EnhancedRestDataCompanion

case class Issue(
  id: Long,
  number: Long,
  title: String,
  body: String,
  user: User,
  comments: Long,
  milestone: Milestone,
  state: String,
  labels: Array[Label],
  assignees: Array[User],
  created_at: ZonedDateTime,
  updated_at: ZonedDateTime
)

object Issue extends EnhancedRestDataCompanion[Issue]
