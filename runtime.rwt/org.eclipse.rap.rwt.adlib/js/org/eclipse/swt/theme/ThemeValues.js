/*******************************************************************************
 * Copyright (c) 2007, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/


/**
 * An adapter class for accessing theme values in appearance definitions. The
 * values are read from the respective qooxdoo theme or the RAP ThemeStore.
 * Values for the actual widget variant are selected if available.
 */
qx.Class.define( "org.eclipse.swt.theme.ThemeValues", {

  extend : qx.core.Object,

  /**
   * Creates a new ThemeValues instance for the variant defined in the given
   * widget states.
   */
  construct : function( states ) {
    if( states === undefined ) {
      this.warn( "no states given" );
    }
    this._states = states;
    this._store = org.eclipse.swt.theme.ThemeStore.getInstance();
  },
  
  statics : {
    NONE_IMAGE : null,
    NONE_IMAGE_SIZED : [ null, 0, 0 ]
  },

  members : {
    
    hasValue : function( element, key ) {
      var result = false;
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      for( var slot in values ) {
        if( typeof values[ slot ][ vkey ] != "undefined" ) {
          result = true;
        }
      }
      return result;
    },

    getCssBorder : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.borders[ vkey ];
      // construct rounded border if "border-radius" is set
      vkey = this._store.getCssValue( element, this._states, "border-radius" );
      var radius = values.boxdims[ vkey ];
      if(    radius != null
          && (    radius[ 0 ] > 0
               || radius[ 1 ] > 0
               || radius[ 2 ] > 0
               || radius[ 3 ] > 0 )
          && result instanceof qx.ui.core.Border )
      {
        var width = result.getWidthTop();
        var color = result.getColorTop();
        result = new org.eclipse.rwt.RoundedBorder( width );
        result.setRadii( radius );
        result.setColor( color );
      }
      return result;
    },

    getCssColor : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.colors[ vkey ];
      return result;
    },

    getCssFont : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.fonts[ vkey ];
      return result;
    },

    getCssDimension : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.dimensions[ vkey ];
      return result;
    },

    getCssBoxDimensions : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.boxdims[ vkey ];
      return result;
    },

    getCssBoolean : function( element, key ) {
      var result = this._store.getCssValue( element, this._states, key );
      return result;
    },

    getCssFloat : function( element, key ) {
      var result = this._store.getCssValue( element, this._states, key );
      return parseFloat( result );
    },

    getCssIdentifier : function( element, key ) {
      var result = this._store.getCssValue( element, this._states, key );
      return result;
    },

    getCssImage : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.images[ vkey ];
      if( result != null ) {
        // TODO [rh] remove hard-coded path (first segment is defined by 
        //      resource-manager)
        result = "rwt-resources/themes/images/" + result[ 0 ];
      } else {
        // TODO [rst] Handle null values - currently, both null and the string
        // "undefined" lead to a js error for icon property
        result = org.eclipse.swt.theme.ThemeValues.NONE_IMAGE;
      }
      return result;
    },

    getCssSizedImage : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var image = this._store.getThemeValues().images[ vkey ];
      var result;
      if( image != null ) {
        // TODO [tb] : Revise hardcoded path
        result = [ "rwt-resources/themes/images/" + image[ 0 ], 
                   image[ 1 ], 
                   image[ 2 ] ];
      } else {
        result = org.eclipse.swt.theme.ThemeValues.NONE_IMAGE_SIZED;        
      } 
      return result; 
    },

    getCssGradient : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.gradients[ vkey ];
      return result;
    },
    
    getCssCursor : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.cursors[ vkey ];
      if( vkey === result ) {
        result = "rwt-resources/themes/cursors/" + result;
      }
      return result;
    },

    getCssAnimation : function( element, key ) {
      var vkey = this._store.getCssValue( element, this._states, key );
      var values = this._store.getThemeValues();
      var result = values.animations[ vkey ];      
      return result;
    }

  }
} );
