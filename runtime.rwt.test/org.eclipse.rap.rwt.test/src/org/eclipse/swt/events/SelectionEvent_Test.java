/*******************************************************************************
 * Copyright (c) 2002, 2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.events;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class SelectionEvent_Test extends TestCase {

  private static final String WIDGET_SELECTED = "widgetSelected|";
  private String log = "";

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testButtonAddRemoveListener() {
    final Display display = new Display();
    Composite shell = new Shell( display , SWT.NONE );
    final Button button = new Button( shell, SWT.PUSH );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        assertSame( button, event.getSource() );
        assertSame( display, event.display );
        assertNull( event.item );
        assertEquals( 10, event.x );
        assertEquals( 20, event.y );
        assertEquals( 30, event.width );
        assertEquals( 40, event.height );
        assertEquals( 3, event.stateMask );
        assertEquals( true, event.doit );
        log += WIDGET_SELECTED;
      }
    } );
    SelectionEvent event = new SelectionEvent( button,
                                               null,
                                               SelectionEvent.WIDGET_SELECTED,
                                               new Rectangle( 10, 20, 30, 40 ),
                                               3,
                                               null,
                                               true,
                                               SWT.NONE );
    event.processEvent();
    assertEquals( WIDGET_SELECTED, log );
  }

  public void testTabFolderAddRemoveListener() {
    Display display = new Display();
    Shell shell = new Shell( display , SWT.NONE );
    final TabFolder folder = new TabFolder( shell, SWT.NONE );
    final TabItem item = new TabItem( folder, SWT.NONE );
    Composite composite = new Composite( folder, SWT.NONE );
    item.setControl( composite );
    folder.setSelection( 0 );
    folder.addSelectionListener( new SelectionAdapter() {

      public void widgetSelected( SelectionEvent event ) {
        assertSame( folder, event.getSource() );
        assertSame( item, event.item );
        assertEquals( 0, event.x );
        assertEquals( 0, event.y );
        assertEquals( 0, event.width );
        assertEquals( 0, event.height );
        assertEquals( 0, event.stateMask );
        assertEquals( true, event.doit );
        log += WIDGET_SELECTED;
      }
    } );
    SelectionEvent event = new SelectionEvent( folder,
                                               item,
                                               SelectionEvent.WIDGET_SELECTED );
    event.processEvent();
    assertEquals( WIDGET_SELECTED, log );
  }
}
