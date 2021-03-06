/* ************************************************************************

   qooxdoo - the new era of web development

   http://qooxdoo.org

   Copyright:
     2004-2008 1&1 Internet AG, Germany, http://www.1und1.de

   License:
     LGPL: http://www.gnu.org/licenses/lgpl.html
     EPL: http://www.eclipse.org/org/documents/epl-v10.php
     See the LICENSE file in the project's top-level directory for details.

   Authors:
     * Sebastian Werner (wpbasti)
     * Andreas Ecker (ecker)

************************************************************************ */

/* ************************************************************************

#module(ui_core)
#optional(qx.ui.core.Parent)
#optional(qx.ui.basic.Terminator)

************************************************************************ */

/**
 * Each focus root delegates the focus handling to instances of the FocusHandler.
 */
qx.Class.define("qx.event.handler.FocusHandler",
{
  extend : qx.core.Target,




  /*
  *****************************************************************************
     CONSTRUCTOR
  *****************************************************************************
  */

  construct : function(widget)
  {
    this.base(arguments);

    if (widget != null) {
      this._attachedWidget = widget;
    }
  },




  /*
  *****************************************************************************
     STATICS
  *****************************************************************************
  */

  statics : {
    mouseFocus : false
  },




  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    /*
    ---------------------------------------------------------------------------
      UTILITIES
    ---------------------------------------------------------------------------
    */

    /**
     * TODOC
     *
     * @type member
     * @return {var} TODOC
     */
    getAttachedWidget : function() {
      return this._attachedWidget;
    },


    /**
     * TODOC
     *
     * @type member
     * @param container {var} TODOC
     * @param ev {Event} TODOC
     * @return {void}
     */
    _onkeyevent : function(container, ev)
    {
      if (ev.getKeyIdentifier() != "Tab") {
        return;
      }

      // Stop all key-events with a TAB keycode
      ev.stopPropagation();
      ev.preventDefault();

      qx.event.handler.FocusHandler.mouseFocus = false;

      var vCurrent = this.getAttachedWidget().getFocusedChild();

      // Support shift key to reverse widget detection order
      if (!ev.isShiftPressed()) {
        var vNext = vCurrent ? this.getWidgetAfter(container, vCurrent) : this.getFirstWidget(container);
      } else {
        var vNext = vCurrent ? this.getWidgetBefore(container, vCurrent) : this.getLastWidget(container);
      }

      // If there was a widget found, focus it
      if (vNext)
      {
        vNext.setFocused(true);
        vNext._ontabfocus();
      }
    },


    /**
     * TODOC
     *
     * @type member
     * @param c1 {var} TODOC
     * @param c2 {var} TODOC
     * @return {int | var} TODOC
     */
    compareTabOrder : function(c1, c2)
    {
      // Sort-Check #1: Tab-Index
      if (c1 == c2) {
        return 0;
      }

      var t1 = c1.getTabIndex();
      var t2 = c2.getTabIndex();

      // The following are some ideas to handle focus after tabindex.
      // Sort-Check #2: Top-Position
      if (t1 != t2) {
        return t1 - t2;
      }

      var y1 = qx.bom.element.Location.getTop(c1.getElement());
      var y2 = qx.bom.element.Location.getTop(c2.getElement());

      if (y1 != y2) {
        return y1 - y2;
      }

      // Sort-Check #3: Left-Position
      var x1 = qx.bom.element.Location.getLeft(c1.getElement());
      var x2 = qx.bom.element.Location.getLeft(c2.getElement());

      if (x1 != x2) {
        return x1 - x2;
      }

      // Sort-Check #4: zIndex
      var z1 = c1.getZIndex();
      var z2 = c2.getZIndex();

      if (z1 != z2) {
        return z1 - z2;
      }

      return 0;
    },




    /*
    ---------------------------------------------------------------------------
      UTILITIES FOR TAB HANDLING
    ---------------------------------------------------------------------------
    */

    /**
     * TODOC
     *
     * @type member
     * @param parentContainer {var} TODOC
     * @return {var} TODOC
     */
    getFirstWidget : function(parentContainer) {
      return this._getFirst(parentContainer, null);
    },


    /**
     * TODOC
     *
     * @type member
     * @param parentContainer {var} TODOC
     * @return {var} TODOC
     */
    getLastWidget : function(parentContainer) {
      return this._getLast(parentContainer, null);
    },


    /**
     * TODOC
     *
     * @type member
     * @param parentContainer {var} TODOC
     * @param widget {var} TODOC
     * @return {var | Array} TODOC
     */
    getWidgetAfter : function(parentContainer, widget)
    {
      if (parentContainer == widget) {
        return this.getFirstWidget(parentContainer);
      }

      if (widget.getAnonymous()) {
        widget = widget.getParent();
      }

      if (widget == null) {
        return [];
      }

      var vAll = [];

      this._getAllAfter(parentContainer, widget, vAll);

      vAll.sort(this.compareTabOrder);

      return vAll.length > 0 ? vAll[0] : this.getFirstWidget(parentContainer);
    },


    /**
     * TODOC
     *
     * @type member
     * @param parentContainer {var} TODOC
     * @param widget {var} TODOC
     * @return {var | Array} TODOC
     */
    getWidgetBefore : function(parentContainer, widget)
    {
      if (parentContainer == widget) {
        return this.getLastWidget(parentContainer);
      }

      if (widget.getAnonymous()) {
        widget = widget.getParent();
      }

      if (widget == null) {
        return [];
      }

      var vAll = [];

      this._getAllBefore(parentContainer, widget, vAll);

      vAll.sort(this.compareTabOrder);

      var len = vAll.length;
      return len > 0 ? vAll[len - 1] : this.getLastWidget(parentContainer);
    },


    /**
     * TODOC
     *
     * @param parent {var} TODOC
     * @param widget {var} TODOC
     * @param arr {var} TODOC
     */
    _getAllAfter : function(parent, widget, arr)
    {
      var children = parent.getChildren();
      var child;
      var len = children.length || 0;

      for (var i=0; i<len; i++)
      {
        child = children[i];

        if (child.isDisposed() || !(child instanceof qx.ui.core.Parent) && !(child instanceof qx.ui.basic.Terminator)) {
          continue;
        }

        if (child.isFocusable() && child.getTabIndex() > 0 && this.compareTabOrder(widget, child) < 0) {
          arr.push(children[i]);
        }

        if (!child.isFocusRoot() && child instanceof qx.ui.core.Parent) {
          this._getAllAfter(child, widget, arr);
        }
      }
    },


    /**
     * TODOC
     *
     * @type member
     * @param parent {var} TODOC
     * @param widget {var} TODOC
     * @param arr {var} TODOC
     * @return {void}
     */
    _getAllBefore : function(parent, widget, arr)
    {
      var children = parent.getChildren();
      var child;
      var len = children.length;

      for (var i=0; i<len; i++)
      {
        child = children[i];

        if (!(child instanceof qx.ui.core.Parent) && !(child instanceof qx.ui.basic.Terminator)) {
          continue;
        }

        if (child.isFocusable() && child.getTabIndex() > 0 && this.compareTabOrder(widget, child) > 0) {
          arr.push(child);
        }

        if (!child.isFocusRoot() && child instanceof qx.ui.core.Parent) {
          this._getAllBefore(child, widget, arr);
        }
      }
    },


    /**
     * TODOC
     *
     * @type member
     * @param parent {var} TODOC
     * @param firstWidget {var} TODOC
     * @return {var} TODOC
     */
    _getFirst : function(parent, firstWidget)
    {
      var children = parent.getChildren();
      var child;
      var len = children.length;

      for (var i=0; i<len; i++)
      {
        child = children[i];

        if (!(child instanceof qx.ui.core.Parent) && !(child instanceof qx.ui.basic.Terminator)) {
          continue;
        }

        if (child.isFocusable() && child.getTabIndex() > 0)
        {
          if (firstWidget == null || this.compareTabOrder(child, firstWidget) < 0) {
            firstWidget = child;
          }
        }

        if (!child.isFocusRoot() && child instanceof qx.ui.core.Parent) {
          firstWidget = this._getFirst(child, firstWidget);
        }
      }

      return firstWidget;
    },


    /**
     * TODOC
     *
     * @type member
     * @param parent {var} TODOC
     * @param lastWidget {var} TODOC
     * @return {var} TODOC
     */
    _getLast : function(parent, lastWidget)
    {
      var children = parent.getChildren();
      var child;
      var len = children.length;

      for (var i=0; i<len; i++)
      {
        child = children[i];

        if (!(child instanceof qx.ui.core.Parent) && !(child instanceof qx.ui.basic.Terminator)) {
          continue;
        }

        if (child.isFocusable() && child.getTabIndex() > 0)
        {
          if (lastWidget == null || this.compareTabOrder(child, lastWidget) > 0) {
            lastWidget = child;
          }
        }

        if (!child.isFocusRoot() && child instanceof qx.ui.core.Parent) {
          lastWidget = this._getLast(child, lastWidget);
        }
      }

      return lastWidget;
    }
  },




  /*
  *****************************************************************************
     DESTRUCTOR
  *****************************************************************************
  */

  destruct : function() {
    this._disposeFields("_attachedWidget");
  }
});
