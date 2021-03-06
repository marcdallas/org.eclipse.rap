/*******************************************************************************
 * Copyright (c) 2007, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/


/**
 * Store for theme values that cannot be kept in a qooxdoo theme. The store is
 * filled from the server at startup.
 */
qx.Class.define( "org.eclipse.swt.theme.ThemeStore", {

  type : "singleton",

  extend : qx.core.Object,

  construct : function() {
    this._values = {
      dimensions : {},
      boxdims : {},
      booleans : {},
      images : {},
      fonts : {},
      colors : {},
      borders : {}
    };
    this._cssValues = {};
    this._statesMap = {
      "*" : {
        "hover" : "over"
      },
      "Button" : {
        "selected" : "checked"
      },
      "List-Item" : {
        "inactive" : "parent_unfocused"
      },
      "TreeItem" : {
        "inactive" : "parent_unfocused"
      },
      "TreeColumn" : {
        "hover" : "mouseover"
      },
      "Shell" : {
        "inactive" : "!active"
      },
      "Shell-Titlebar" : {
        "inactive" : "!active"
      },
      "Shell-MinButton" : {
        "inactive" : "!active"
      },
      "Shell-MaxButton" : {
        "inactive" : "!active"
      },
      "Shell-CloseButton" : {
        "inactive" : "!active"
      },
      "TableColumn" : {
        "hover" : "mouseover"
      },
      "TabItem" : {
        "selected" : "checked"
      }
    };
  },

  members : {

    /**
     * Returns the values container.
     */
    getThemeValues : function() {
      return this._values;
    },

    /**
     * Adds a value to the values container.
     */
    setValue : function( type, key, value ) {
      if( type == "dimension" ) {
        this._values.dimensions[ key ] = value;
      } else if ( type == "boxdim" ) {
        this._values.boxdims[ key ] = value;
      } else if ( type == "boolean" ) {
        this._values.booleans[ key ] = value;
      } else if ( type == "image" ) {
        this._values.images[ key ] = value;
      } else if ( type == "font" ) {
        var font = new qx.ui.core.Font();
        font.setSize( value.size );
        font.setFamily( value.family );
        font.setBold( value.bold );
        font.setItalic( value.italic );
        this._values.fonts[ key ] = font;
      } else if ( type == "color" ) {
        this._values.colors[ key ] = value;
      } else if ( type == "border" ) {
        var border = new qx.ui.core.Border( value.width, value.style );
        if( value.color ) {
          border.setUserData( "color", value.color );
        }
        if( value.innerColor ) {
          border.setUserData( "innerColor", value.innerColor );
        }
        this._values.borders[ key ] = border;
      } else {
        this.error( "invalid type: " + type );
      }
    },

    resolveBorderColors : function() {
      for( var key in this._values.borders ) {
        var border = this._values.borders[ key ];
        var colorData = border.getUserData( "color" );
        if( colorData != null ) {
          var colors = [];
          for( var i = 0; i < colorData.length; i++ ) {
            if( colorData[ i ].charAt( 0 ) == "#" ) {
              colors.push( colorData[ i ] );
            } else {
              colors.push( this._values.colors[ colorData[ i ] ] );
            }
          }
          border.setColor( colors );
          border.setUserData( "color", null );
        }
        var innerColorData = border.getUserData( "innerColor" );
        if( innerColorData != null ) {
          var innerColors = [];
          for( var i = 0; i < innerColorData.length; i++ ) {
            innerColors.push( this._values.colors[ innerColorData[ i ] ] );
          }
          border.setInnerColor( innerColors );
          border.setUserData( "innerColor", null );
        }
      }
    },

    // TODO [rst] Replace with a generic "init" method
    // Fills qx color theme with some named colors necessary for BordersBase
    fillColors : function( theme ) {
      var ct = qx.Theme.getByName( theme + "Colors" );
      ct.colors[ "widget.darkshadow" ]
        = this._getColor( "Display", {}, "rwt-darkshadow-color", theme );
      ct.colors[ "widget.highlight" ]
        = this._getColor( "Display", {}, "rwt-highlight-color", theme );
      ct.colors[ "widget.lightshadow" ]
        = this._getColor( "Display", {}, "rwt-lightshadow-color", theme );
      ct.colors[ "widget.shadow" ]
        = this._getColor( "Display", {}, "rwt-shadow-color", theme );
      ct.colors[ "widget.thinborder" ]
        = this._getColor( "Display", {}, "rwt-thinborder-color", theme );
      // TODO [rst] eliminate these properties
      ct.colors[ "widget.selection-marker" ]
        = this._getColor( "Display", {}, "rwt-selectionmarker-color", theme );
      ct.colors[ "widget.background" ]
        = this._getColor( "*", {}, "background-color", theme );
      ct.colors[ "widget.foreground" ]
        = this._getColor( "*", {}, "color", theme );
      ct.colors[ "widget.info.foreground" ]
        = this._getColor( "ToolTip", {}, "color", theme );
      ct.colors[ "widget.graytext" ]
        = this._getColor( "*", { "disabled" : true }, "color", theme );
    },

    _getColor : function( element, states, property, theme ) {
      var vkey = this.getCssValue( element, states, property, theme );
      return this._values.colors[ vkey ];
    },

    // CSS SUPPORT

    setThemeCssValues : function( theme, values, isDefault ) {
      if( this._cssValues[ theme ] === undefined ) {
        this._cssValues[ theme ] = values;
      }
      if( isDefault ) {
        this.defaultTheme = theme;
      }
    },

    getCssValue : function( element, states, property, theme ) {
      var result;
      if( theme == null ) {
        theme = qx.theme.manager.Meta.getInstance().getTheme().name;
      }
      if(    this._cssValues[ theme ] !== undefined
          && this._cssValues[ theme ][ element ] !== undefined 
          && this._cssValues[ theme ][ element ][ property ] !== undefined )
      {
        var values = this._cssValues[ theme ][ element ][ property ];
        var found = false;
        for( var i = 0; i < values.length && !found; i++ ) {
          if( this._matches( states, element, values[ i ][ 0 ] ) ) {
            result = values[ i ][ 1 ];
            found = true;
          }
        }
      }
      if( result === undefined && theme != this.defaultTheme ) {
        result = this.getCssValue( element, states, property, this.defaultTheme );
      }
      return result;
    },

    _matches : function( states, element, constraints ) {
      var result = true;
      for( var i = 0; i < constraints.length && result; i++ ) {
        var cond = constraints[ i ];
        if( cond.length > 0 ) {
          var c = cond.charAt( 0 );
          if( c == "." ) {
            result = "variant_" + cond.substr( 1 ) in states;
          } else if( c == ":" ) {
            var state = this._translateState( cond.substr( 1 ), element );
            if( state.charAt( 0 ) == "!" ) {
              result = ! ( state.substr( 1 ) in states );
            } else {
              result = state in states;
            }
          } else if( c == "[" ) {
            result = "rwt_" + cond.substr( 1 ) in states;
          }
        }
      }
      return result;
    },

    _translateState : function( state, element ) {
      var result = state;
      if( element in this._statesMap && state in this._statesMap[ element ] ) {
        result = this._statesMap[ element ][ state ];
      } else if( state in this._statesMap[ "*" ] ) {
        result = this._statesMap[ "*" ][ state ];
      }
      return result;
    }
  }
} );
