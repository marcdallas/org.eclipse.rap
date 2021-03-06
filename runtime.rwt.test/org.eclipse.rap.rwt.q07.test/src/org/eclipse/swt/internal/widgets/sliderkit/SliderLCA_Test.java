/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.internal.widgets.sliderkit;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.lifecycle.JSConst;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.Props;
import org.eclipse.swt.widgets.*;

public class SliderLCA_Test extends TestCase {

  public void testSliderPreserveValues() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Slider slider = new Slider( shell, SWT.HORIZONTAL );
    Fixture.markInitialized( display );
    // Test preserved minimum, maximum, 
    // selection, increment, pageIncrement and thumb
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( slider );
    Integer minimum
      = ( Integer )adapter.getPreserved( SliderLCA.PROP_MINIMUM );
    assertEquals( 0, minimum.intValue() );
    Integer maximum
      = ( Integer )adapter.getPreserved( SliderLCA.PROP_MAXIMUM );
    assertEquals( 100, maximum.intValue() );
    Integer selection
      = ( Integer )adapter.getPreserved( SliderLCA.PROP_SELECTION );
    assertEquals( 0, selection.intValue() );
    Integer increment
      = ( Integer )adapter.getPreserved( SliderLCA.PROP_INCREMENT );
    assertEquals( 1, increment.intValue() );
    Integer pageIncrement
      = ( Integer )adapter.getPreserved( SliderLCA.PROP_PAGE_INCREMENT );
    assertEquals( 10, pageIncrement.intValue() );    
    Integer thumb
    = ( Integer )adapter.getPreserved( SliderLCA.PROP_THUMB );
    assertEquals( 10, thumb.intValue() );
    Fixture.clearPreserved();
    // Test preserved control properties
    testPreserveControlProperties( slider );
    // Test preserved selection listeners
    testPreserveSelectionListener( slider );
    display.dispose();
  } 

  public void testSelectionEvent() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Slider slider = new Slider( shell, SWT.HORIZONTAL );
    testSelectionEvent( slider );    
  }

  private void testPreserveControlProperties( final Slider slider ) {
    // bound
    Rectangle rectangle = new Rectangle( 10, 10, 10, 10 );
    slider.setBounds( rectangle );
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( slider );
    assertEquals( rectangle, adapter.getPreserved( Props.BOUNDS ) );
    Fixture.clearPreserved();
    // enabled
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( Boolean.TRUE, adapter.getPreserved( Props.ENABLED ) );
    Fixture.clearPreserved();
    slider.setEnabled( false );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( Boolean.FALSE, adapter.getPreserved( Props.ENABLED ) );
    Fixture.clearPreserved();
    // visible
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( Boolean.TRUE, adapter.getPreserved( Props.VISIBLE ) );
    Fixture.clearPreserved();
    slider.setVisible( false );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( Boolean.FALSE, adapter.getPreserved( Props.VISIBLE ) );
    Fixture.clearPreserved();
    // menu
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( null, adapter.getPreserved( Props.MENU ) );
    Fixture.clearPreserved();
    Menu menu = new Menu( slider );
    MenuItem item = new MenuItem( menu, SWT.NONE );
    item.setText( "1 Item" );
    slider.setMenu( menu );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( menu, adapter.getPreserved( Props.MENU ) );
    Fixture.clearPreserved();
    //foreground background font
    Color background = Graphics.getColor( 122, 33, 203 );
    slider.setBackground( background );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    assertEquals( background, adapter.getPreserved( Props.BACKGROUND ) );
    Fixture.clearPreserved();
  }

  private void testPreserveSelectionListener( final Slider slider ) {
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( slider );
    Boolean hasListeners
      = ( Boolean )adapter.getPreserved( Props.SELECTION_LISTENERS );
    assertEquals( Boolean.FALSE, hasListeners );
    Fixture.clearPreserved();
    SelectionListener selectionListener = new SelectionAdapter() { };
    slider.addSelectionListener( selectionListener );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( slider );
    hasListeners
      = ( Boolean )adapter.getPreserved( Props.SELECTION_LISTENERS );
    assertEquals( Boolean.TRUE, hasListeners );
    Fixture.clearPreserved();
  }

  private void testSelectionEvent( final Slider slider ) {
    final StringBuffer log = new StringBuffer();
    SelectionListener selectionListener = new SelectionAdapter() {
      public void widgetSelected( SelectionEvent event ) {
        assertEquals( slider, event.getSource() );
        assertEquals( null, event.item );
        assertEquals( SWT.NONE, event.detail );
        assertEquals( 0, event.x );
        assertEquals( 0, event.y );
        assertEquals( 0, event.width );
        assertEquals( 0, event.height );
        assertEquals( true, event.doit );
        log.append( "widgetSelected" );
      }
    };
    slider.addSelectionListener( selectionListener );
    String dateTimeId = WidgetUtil.getId( slider );
    Fixture.fakeRequestParam( JSConst.EVENT_WIDGET_SELECTED, dateTimeId );
    Fixture.readDataAndProcessAction( slider );
    assertEquals( "widgetSelected", log.toString() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
