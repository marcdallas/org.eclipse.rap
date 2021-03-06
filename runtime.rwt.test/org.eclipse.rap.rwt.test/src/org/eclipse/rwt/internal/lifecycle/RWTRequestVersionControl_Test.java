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

package org.eclipse.rwt.internal.lifecycle;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;


public class RWTRequestVersionControl_Test extends TestCase {
  
  public void testIsValid() {
    assertTrue( RWTRequestVersionControl.isValid() );
    Integer nextRequestId = RWTRequestVersionControl.nextRequestId();
    
    Fixture.fakeRequestParam( RWTRequestVersionControl.REQUEST_COUNTER,
                              nextRequestId.toString() );
    assertFalse( RWTRequestVersionControl.isValid() );

    RWTRequestVersionControl.beforeService();
    Fixture.fakeRequestParam( RWTRequestVersionControl.REQUEST_COUNTER,
                              nextRequestId.toString() );
    assertTrue( RWTRequestVersionControl.isValid() );
    
    Fixture.fakeRequestParam( RWTRequestVersionControl.REQUEST_COUNTER,
                              "4711" );
    assertFalse( RWTRequestVersionControl.isValid() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
