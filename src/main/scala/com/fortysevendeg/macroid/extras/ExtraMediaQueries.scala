package com.fortysevendeg.macroid.extras

import macroid.AppContext
import macroid.FullDsl._
import scala.language.postfixOps

trait DeviceMediaQueries {
  def tablet(implicit ctx: AppContext) = widerThan(720 dp)
  def landscapeTablet(implicit ctx: AppContext) = widerThan(720 dp) & landscape
  def portraitTablet(implicit ctx: AppContext) = widerThan(720 dp) & portrait
}

object DeviceMediaQueries extends DeviceMediaQueries
