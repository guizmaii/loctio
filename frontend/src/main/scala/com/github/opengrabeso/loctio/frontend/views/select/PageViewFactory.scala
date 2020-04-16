package com.github.opengrabeso.loctio
package frontend
package views.select

import com.github.opengrabeso.loctio.frontend.views.PageModel
import routing._
import io.udash._

/** Prepares model, view and presenter for demo view. */
class PageViewFactory(
  application: Application[RoutingState],
  userService: services.UserContextService
) extends ViewFactory[SelectPageState.type] {
  import scala.concurrent.ExecutionContext.Implicits.global


  override def create(): (View, Presenter[SelectPageState.type]) = {
    val model = ModelProperty(PageModel(loading = true))

    val presenter = new PagePresenter(model, application, userService)
    val view = new PageView(model, presenter, userService.properties)

    presenter.init()

    (view, presenter)
  }
}