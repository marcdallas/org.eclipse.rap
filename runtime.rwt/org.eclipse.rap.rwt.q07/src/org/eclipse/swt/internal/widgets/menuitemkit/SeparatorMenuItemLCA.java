/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.internal.widgets.menuitemkit;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.swt.widgets.MenuItem;


final class SeparatorMenuItemLCA extends MenuItemDelegateLCA {

  void preserveValues( final MenuItem menuItem ) {
    WidgetLCAUtil.preserveCustomVariant( menuItem );
  }

  void readData( final MenuItem menuItem ) {
    // do nothing
  }
  
  void renderInitialization( final MenuItem menuItem ) throws IOException {
    MenuItemLCAUtil.newItem( menuItem, "qx.ui.menu.Separator", null );    
  }

  void renderChanges( final MenuItem menuItem ) throws IOException {
    WidgetLCAUtil.writeCustomVariant( menuItem );
  }
}
