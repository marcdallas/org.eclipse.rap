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

package org.eclipse.swt.internal.widgets.listkit;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.widgets.*;
import org.eclipse.swt.widgets.*;


public class ListLCA extends AbstractWidgetLCA {

  private static final String QX_TYPE = "org.eclipse.swt.widgets.List";

  // Property names, used when preserving values
  static final String PROP_SELECTION = "selection";
  static final String PROP_ITEMS = "items";
  static final String PROP_FOCUS_INDEX = "focusIndex";
  static final String PROP_TOP_INDEX = "topIndex";
  static final String PROP_HAS_H_SCROLL_BAR = "hasHScrollBar";
  static final String PROP_HAS_V_SCROLL_BAR = "hasVScrollBar";

  private static final Integer DEFAULT_SINGLE_SELECTION = new Integer( -1 );
  private static final int[] DEFAULT_MULTI_SELECTION = new int[ 0 ];
  private static final String[] DEFAUT_ITEMS = new String[ 0 ];
  private static final Integer DEFAULT_FOCUS_INDEX = new Integer( -1 );
  private static final Integer DEFAULT_TOP_INDEX = new Integer( 0 );

  public void preserveValues( final Widget widget ) {
    List list = ( List  )widget;
    ControlLCAUtil.preserveValues( list );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( widget );
    adapter.preserve( Props.SELECTION_LISTENERS,
                      Boolean.valueOf( SelectionEvent.hasListener( list ) ) );
    adapter.preserve( PROP_ITEMS, list.getItems() );
    adapter.preserve( PROP_TOP_INDEX, new Integer( list.getTopIndex() ) );
    adapter.preserve( PROP_FOCUS_INDEX, new Integer( list.getFocusIndex() ) );
    adapter.preserve( PROP_HAS_H_SCROLL_BAR, hasHScrollBar( list ) );
    adapter.preserve( PROP_HAS_V_SCROLL_BAR, hasVScrollBar( list ) );
    preserveSelection( list );
    WidgetLCAUtil.preserveCustomVariant( list );
  }

  public void readData( final Widget widget ) {
    List list = ( List )widget;
    readTopIndex( list );
    readSelection( list );
    readFocusIndex( list );
    ControlLCAUtil.processSelection( list, null, true );
    ControlLCAUtil.processMouseEvents( list );
    ControlLCAUtil.processKeyEvents( list );
    ControlLCAUtil.processMenuDetect( list );
    WidgetLCAUtil.processHelp( list );
  }

  public void renderInitialization( final Widget widget ) throws IOException {
    List list = ( List )widget;
    JSWriter writer = JSWriter.getWriterFor( list );
    Boolean multiSelection = Boolean.valueOf( !isSingle( list ) );
    writer.newWidget( QX_TYPE, new Object[] { multiSelection } );
    ControlLCAUtil.writeStyleFlags( list );
  }

  // TODO [rh] keep scroll position, even when exchanging items
  public void renderChanges( final Widget widget ) throws IOException {
    List list = ( List )widget;
    // order of writeChanges, writeItems, writeSelection, writeFocus is crucial
    ControlLCAUtil.writeChanges( list );
    writeItems( list );
    writeSelection( list );
    writeTopIndex( list );
    writeFocusIndex( list );
    writeOverflow( list );
    updateSelectionListeners( list );
    WidgetLCAUtil.writeCustomVariant( list );
  }

  public void renderDispose( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.dispose();
  }

  ////////////////////////////////////
  // Helping methods to preserve state

  private static void preserveSelection( final List list ) {
    IWidgetAdapter adapter = WidgetUtil.getAdapter( list );
    Object selection;
    if( isSingle( list ) ) {
      selection = new Integer( list.getSelectionIndex() );
    } else {
      selection = list.getSelectionIndices();
    }
    adapter.preserve( PROP_SELECTION, selection );
  }

  ///////////////////////////////////////////
  // Helping methods to write JavaScript code

  private static void writeItems( final List list ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( list );
    String[] items = list.getItems();
    if( WidgetLCAUtil.hasChanged( list, PROP_ITEMS, items, DEFAUT_ITEMS ) ) {
      // Convert newlines into whitespaces
      for( int i = 0; i < items.length; i++ ) {
        items[ i ] = WidgetLCAUtil.replaceNewLines( items[ i ], " " );
        items[ i ] = WidgetLCAUtil.escapeText( items[ i ], false );
      }
      writer.set( "items", new Object[]{ items } );
    }
  }

  private static void writeSelection( final List list ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( list );
    String prop = PROP_SELECTION;
    if( isSingle( list ) ) {
      Integer newValue = new Integer( list.getSelectionIndex() );
      Integer defValue = DEFAULT_SINGLE_SELECTION;
      if( WidgetLCAUtil.hasChanged( list, prop, newValue, defValue )) {
        writer.call( "selectItem", new Object[] { newValue } );
      }
    } else {
      int[] newValue = list.getSelectionIndices();
      int[] defValue = DEFAULT_MULTI_SELECTION;
      if( WidgetLCAUtil.hasChanged( list, prop, newValue, defValue ) ) {
        if( list.getSelectionCount() == list.getItemCount() ) {
          writer.call( "selectAll", null );
        } else {
          int[] selection = list.getSelectionIndices();
          Integer[] newSelection = new Integer[ selection.length ];
          for( int i = 0; i < newSelection.length; i++ ) {
            newSelection[ i ] = new Integer( selection[ i ] );
          }
          writer.call( "selectItems", new Object[] { newSelection } );
        }
      }
    }
  }

  private static void writeFocusIndex( final List list ) throws IOException {
    String prop = PROP_FOCUS_INDEX;
    Integer newValue = new Integer( list.getFocusIndex() );
    if( WidgetLCAUtil.hasChanged( list, prop, newValue, DEFAULT_FOCUS_INDEX ) ) {
      JSWriter writer = JSWriter.getWriterFor( list );
      writer.call( "focusItem", new Object[] { newValue} );
    }
  }

  private static void writeTopIndex( final List list ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( list );
    Integer newValue = new Integer( list.getTopIndex() );
    writer.set( PROP_TOP_INDEX, "topIndex", newValue, DEFAULT_TOP_INDEX );
  }

  private static void writeOverflow( final List list ) throws IOException {
    boolean hasHChanged = WidgetLCAUtil.hasChanged( list,
                                                    PROP_HAS_H_SCROLL_BAR,
                                                    hasHScrollBar( list ),
                                                    Boolean.FALSE );
    boolean hasVChanged = WidgetLCAUtil.hasChanged( list,
                                                    PROP_HAS_V_SCROLL_BAR,
                                                    hasVScrollBar( list ),
                                                    Boolean.FALSE );
    if( hasHChanged || hasVChanged ) {
      boolean scrollX = hasHScrollBar( list ).booleanValue();
      boolean scrollY = hasVScrollBar( list ).booleanValue();
      String overflow;
      if( scrollX && scrollY ) {
        overflow = "auto";
      } else if( scrollX ) {
        overflow = "scrollX";
      } else if( scrollY ) {
        overflow = "scrollY";
      } else {
        overflow = "hidden";
      }
      JSWriter writer = JSWriter.getWriterFor( list );
      writer.set( "overflow", overflow );
    }
  }

  private static void updateSelectionListeners( final List list )
    throws IOException
  {
    String prop = Props.SELECTION_LISTENERS;
    Boolean newValue = Boolean.valueOf( SelectionEvent.hasListener( list ) );
    Boolean defValue = Boolean.FALSE;
    if( WidgetLCAUtil.hasChanged( list, prop, newValue, defValue ) ) {
      JSWriter writer = JSWriter.getWriterFor( list );
      String value = newValue.booleanValue() ? "action" : "state";
      writer.set( "changeSelectionNotification", value );
    }
  }

  ////////////////////////////////////////////
  // Helping methods to read client-side state

  private static void readSelection( final List list ) {
    String value = WidgetLCAUtil.readPropertyValue( list, "selection" );
    if( value != null ) {
      String[] indiceStrings;
      if( "".equals( value ) ) {
        indiceStrings = new String[ 0 ];
      } else {
        indiceStrings = value.split( "," );
      }
      int[] indices = new int[ indiceStrings.length ];
      for( int i = 0; i < indices.length; i++ ) {
        indices[ i ] = Integer.parseInt( indiceStrings[ i ] );
      }
      list.setSelection( indices );
    }
  }

  private static void readTopIndex( final List list ) {
    String value = WidgetLCAUtil.readPropertyValue( list, "topIndex" );
    if( value != null ) {
      list.setTopIndex( Integer.parseInt( value ) );
    }
  }

  private static void readFocusIndex( final List list ) {
    String paramValue = WidgetLCAUtil.readPropertyValue( list, "focusIndex" );
    if( paramValue != null ) {
      int focusIndex = Integer.parseInt( paramValue );
      Object adapter = list.getAdapter( IListAdapter.class );
      IListAdapter listAdapter = ( IListAdapter )adapter;
      listAdapter.setFocusIndex( focusIndex );
    }
  }

  //////////////////
  // Helping methods

  private static boolean isSingle( final List list ) {
    return ( list.getStyle() & SWT.SINGLE ) != 0;
  }

  private static Boolean hasHScrollBar( final List list ) {
    Object adapter = list.getAdapter( IListAdapter.class );
    IListAdapter listAdapter = ( IListAdapter )adapter;
    return Boolean.valueOf( listAdapter.hasHScrollBar() );
  }

  private static Boolean hasVScrollBar( final List list ) {
    Object adapter = list.getAdapter( IListAdapter.class );
    IListAdapter listAdapter = ( IListAdapter )adapter;
    return Boolean.valueOf( listAdapter.hasVScrollBar() );
  }
}