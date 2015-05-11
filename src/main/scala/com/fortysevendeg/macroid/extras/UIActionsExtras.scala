/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.macroid.extras

import android.app.Activity
import android.content.{ComponentName, Intent}
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.ContextThemeWrapper
import android.widget.Toast
import macroid.{ContextWrapper, ActivityContextWrapper, Ui}
import macroid.FullDsl._

object UIActionsExtras {

  def uiStartActivity[T <: Activity]()(implicit c: ActivityContextWrapper, m: Manifest[T]): Ui[Unit] =
    Ui(c.application.startActivity(new Intent(c.application, m.runtimeClass)))

  def uiStartActivityForResult[T <: Activity](result: Int)(implicit c: ActivityContextWrapper, m: Manifest[T]): Ui[Unit] =
    Ui {
      c.original.get map {
        activity =>
          activity.startActivityForResult(new Intent(activity, m.runtimeClass), result)
      }
    }

  def uiShortToast(msg: Int)(implicit c: ContextWrapper): Ui[Unit] =
    Ui(Toast.makeText(c.application, msg, Toast.LENGTH_SHORT).show())

  def uiLongToast(msg: Int)(implicit c: ContextWrapper): Ui[Unit] =
    Ui(Toast.makeText(c.application, msg, Toast.LENGTH_LONG).show())

  def uiShortToast(msg: String)(implicit c: ContextWrapper): Ui[Unit] =
    Ui(Toast.makeText(c.application, msg, Toast.LENGTH_SHORT).show())

  def uiLongToast(msg: String)(implicit c: ContextWrapper): Ui[Unit] =
    Ui(Toast.makeText(c.application, msg, Toast.LENGTH_LONG).show())

  def uiHandler(f: => Ui[_]): Ui[Unit] =
    Ui {
      new Handler().post(new Runnable {
        override def run(): Unit = runUi(f)
      })
    }

  def uiHandlerDelayed(f: => Ui[_], delayMilis: Int): Ui[Unit] =
    Ui {
      new Handler().postDelayed(new Runnable {
        override def run(): Unit = runUi(f)
      }, delayMilis)
    }

}

object ActionsExtras {

  def aStartActivity[T <: Activity]()(implicit c: ActivityContextWrapper, m: Manifest[T]): Unit =
    c.original.get map {
      activity =>
        activity.startActivity(new Intent(c.application, m.runtimeClass))
    }

  def aStartActivityForResult[T <: Activity](result: Int)(implicit c: ActivityContextWrapper, m: Manifest[T]): Unit =
    c.original.get map {
      activity =>
        activity.startActivityForResult(new Intent(activity, m.runtimeClass), result)
    }

  def aStartActivityFromComponentName[T <: Activity](componentName: ComponentName)
      (implicit c: ActivityContextWrapper): Unit = {
    val intent = new Intent()
    intent.setComponent(componentName)
    c.original.get map {
      activity =>
        activity.startActivity(intent)
    }
  }

  def aStartActivityFromComponentNameForResult[T <: Activity](componentName: ComponentName, result: Int)
      (implicit c: ActivityContextWrapper): Unit = {
    val intent = new Intent()
    intent.setComponent(componentName)
    c.original.get map {
      activity =>
        activity.startActivityForResult(intent, result)
    }
  }

  def aShortToast(msg: Int)(implicit c: ContextWrapper): Unit =
    Toast.makeText(c.application, msg, Toast.LENGTH_SHORT).show()

  def aLongToast(msg: Int)(implicit c: ContextWrapper): Unit =
    Toast.makeText(c.application, msg, Toast.LENGTH_LONG).show()

  def aShortToast(msg: String)(implicit c: ContextWrapper): Unit =
    Toast.makeText(c.application, msg, Toast.LENGTH_SHORT).show()

  def aLongToast(msg: String)(implicit c: ContextWrapper): Unit =
    Toast.makeText(c.application, msg, Toast.LENGTH_LONG).show()

}

object ThemeExtras {

  def themeGetDrawable(themeId: Int, attr: Int)(implicit activityContext: ActivityContextWrapper): Option[Drawable] =
    activityContext.original.get map {
      activity =>
        val contextTheme = new ContextThemeWrapper(activity, themeId)
        val a = contextTheme.getTheme.obtainStyledAttributes(Array(attr))
        val attributeResourceId = a.getResourceId(0, 0)
        val drawable = contextTheme.getResources.getDrawable(attributeResourceId)
        a.recycle()
        drawable
    }

  def themeGetDrawable(attr: Int)(implicit activityContext: ActivityContextWrapper): Option[Drawable] =
    activityContext.original.get map {
      activity =>
        val a = activity.getTheme.obtainStyledAttributes(Array(attr))
        val attributeResourceId = a.getResourceId(0, 0)
        val drawable = activity.getResources.getDrawable(attributeResourceId)
        a.recycle()
        drawable
    }

}

object ResourcesExtras {

  private def resGetResource[A](resource: String, resourceType: String)(f: (ContextWrapper, Int) ⇒ A)
      (implicit c: ContextWrapper): Option[A] = {
    val resourceId = c.application.getResources.getIdentifier(resource, resourceType, c.application.getPackageName)
    resourceId match {
      case 0 ⇒ None
      case _ ⇒ Some(f(c, resourceId))
    }
  }

  def resGetBoolean(resourceId: Int)(implicit c: ContextWrapper): Boolean = c.application.getResources.getBoolean(resourceId)

  def resGetBoolean(resource: String)(implicit c: ContextWrapper): Option[Boolean] =
    resGetResource(resource, "boolean")((c, resourceId) ⇒ c.application.getResources.getBoolean(resourceId))

  def resGetColor(resourceId: Int)(implicit c: ContextWrapper): Int = c.application.getResources.getColor(resourceId)

  def resGetColor(resource: String)(implicit c: ContextWrapper): Option[Int] =
    resGetResource(resource, "color")((c, resourceId) ⇒ c.application.getResources.getColor(resourceId))

  def resGetDimension(resourceId: Int)(implicit c: ContextWrapper): Float = c.application.getResources.getDimension(resourceId)

  def resGetDimension(resource: String)(implicit c: ContextWrapper): Option[Float] =
    resGetResource(resource, "dimen")((c, resourceId) ⇒ c.application.getResources.getDimension(resourceId))

  def resGetDimensionPixelSize(resourceId: Int)(implicit c: ContextWrapper): Int = c.application.getResources.getDimensionPixelSize(resourceId)

  def resGetDimensionPixelSize(resource: String)(implicit c: ContextWrapper): Option[Int] =
    resGetResource(resource, "dimen")((c, resourceId) ⇒ c.application.getResources.getDimensionPixelSize(resourceId))

  def resGetDrawable(resourceId: Int)(implicit c: ContextWrapper): Drawable = c.application.getResources.getDrawable(resourceId)

  def resGetDrawable(resource: String)(implicit c: ContextWrapper): Option[Drawable] =
    resGetResource(resource, "drawable")((c, resourceId) ⇒ c.application.getResources.getDrawable(resourceId))

  def resGetDrawableIdentifier(resource: String)(implicit c: ContextWrapper): Option[Int] =
    resGetResource(resource, "drawable")((c, resourceId) ⇒ resourceId)

  def resGetIdentifier(resource: String, resourceType: String)(implicit c: ContextWrapper): Option[Int] =
    resGetResource(resource, resourceType)((_, resourceId) ⇒ resourceId)

  def resGetInteger(resourceId: Int)(implicit c: ContextWrapper): Int = c.application.getResources.getInteger(resourceId)

  def resGetInteger(resource: String)(implicit c: ContextWrapper): Option[Int] =
    resGetResource(resource, "integer")((c, resourceId) ⇒ c.application.getResources.getInteger(resourceId))

  def resGetResourcePackageName(resourceId: Int)(implicit c: ContextWrapper): String =
    c.application.getResources.getResourcePackageName(resourceId)

  def resGetString(resourceId: Int)(implicit c: ContextWrapper): String = c.application.getResources.getString(resourceId)

  def resGetString(resource: String)(implicit c: ContextWrapper): Option[String] =
    resGetResource(resource, "string")((c, resourceId) ⇒ c.application.getResources.getString(resourceId))

  def resGetString(resourceId: Int, formatArgs: AnyRef*)(implicit c: ContextWrapper): String =
    c.application.getResources.getString(resourceId, formatArgs:_*)

  def resGetString(resource: String, formatArgs: AnyRef*)(implicit c: ContextWrapper): Option[String] =
    resGetResource(resource, "string")((c, resourceId) ⇒ c.application.getResources.getString(resourceId, formatArgs:_*))

}