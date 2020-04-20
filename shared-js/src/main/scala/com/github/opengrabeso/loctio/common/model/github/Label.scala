package com.github.opengrabeso.loctio
package common.model.github

import rest.EnhancedRestDataCompanion

case class Label( // https://developer.github.com/v3/issues/#get-an-issue
  id: Long,
  url: String,
  name: String,
  description: String,
  color: String,
  default: Boolean
)

object Label extends EnhancedRestDataCompanion[Label]
