/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui;

import org.eclipse.core.runtime.*;
import org.eclipse.ui.internal.progress.ProgressView;

/**
 * Factory for the workbench's public extensions.
 * <p>
 * This allows the extensions to be made available for use by RCP applications
 * without exposing their concrete implementation classes.
 * </p>
 * 
 * @since 1.0
 */
public class ExtensionFactory implements IExecutableExtensionFactory,
        IExecutableExtension {

    /**
     * Factory ID for the Appearance preference page.
     */
    public static final String APPEARANCE_PREFERENCE_PAGE = "appearancePreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the Colors and Fonts preference page.
     */
    public static final String COLORS_AND_FONTS_PREFERENCE_PAGE = "colorsAndFontsPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the Decorators preference page.
     */
    public static final String DECORATORS_PREFERENCE_PAGE = "decoratorsPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the Editors preference page.
     */
    public static final String EDITORS_PREFERENCE_PAGE = "editorsPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the File Associations preference page.
     */
    public static final String FILE_ASSOCIATIONS_PREFERENCE_PAGE = "fileAssociationsPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the Keys preference page.
     */
    public static final String KEYS_PREFERENCE_PAGE = "keysPreferencePage"; //$NON-NLS-1$
    
    /**
     * Factory ID for the new (and improved) keys preference page.
     * 
     * @since 1.0
     */
    public static final String NEW_KEYS_PREFERENCE_PAGE = "newKeysPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the Perspectives preference page.
     */
    public static final String PERSPECTIVES_PREFERENCE_PAGE = "perspectivesPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the Preferences export wizard.
     */
    public static final String PREFERENCES_EXPORT_WIZARD = "preferencesExportWizard"; //$//$NON-NLS-1$
 
    /**
     * Factory ID for the Preferences import wizard.
     */
    public static final String PREFERENCES_IMPORT_WIZARD = "preferencesImportWizard"; //$//$NON-NLS-1$
    
    /**
     * Factory ID for the Progress view.
     */
    public static final String PROGRESS_VIEW = "progressView"; //$NON-NLS-1$

    /**
     * Factory ID for the Workbench preference page.
     */
    public static final String WORKBENCH_PREFERENCE_PAGE = "workbenchPreferencePage"; //$NON-NLS-1$

    /**
     * Factory ID for the ContentTypes preference page.
     */
    public static final String CONTENT_TYPES_PREFERENCE_PAGE = "contentTypesPreferencePage"; //$NON-NLS-1$
    
    private IConfigurationElement config;

    private String id;

    private String propertyName;

    /**
     * Constructs a new workbench extension factory.
     */
    public ExtensionFactory() {
        // do nothing
    }

    private Object configure(Object obj) throws CoreException {
        if (obj instanceof IExecutableExtension) {
            ((IExecutableExtension) obj).setInitializationData(config,
                    propertyName, null);
        }
        return obj;
    }

    /**
     * Creates the object referenced by the factory id obtained from the extension data.
     */
    public Object create() throws CoreException {
//        if (APPEARANCE_PREFERENCE_PAGE.equals(id)) {
//            return configure(new ViewsPreferencePage());
//        }
//        if (COLORS_AND_FONTS_PREFERENCE_PAGE.equals(id)) {
//            return configure(new ColorsAndFontsPreferencePage());
//        }
//        if (DECORATORS_PREFERENCE_PAGE.equals(id)) {
//            return configure(new DecoratorsPreferencePage());
//        }
//        if (EDITORS_PREFERENCE_PAGE.equals(id)) {
//            return configure(new EditorsPreferencePage());
//        }
//        if (FILE_ASSOCIATIONS_PREFERENCE_PAGE.equals(id)) {
//            return configure(new FileEditorsPreferencePage());
//        }
//        if (KEYS_PREFERENCE_PAGE.equals(id)) {
//            return configure(new KeysPreferencePage());
//        }
//        if (NEW_KEYS_PREFERENCE_PAGE.equals(id)) {
//            return configure(new NewKeysPreferencePage());
//        }
//        if (PERSPECTIVES_PREFERENCE_PAGE.equals(id)) {
//            return configure(new PerspectivesPreferencePage());
//        }
//        if (PREFERENCES_EXPORT_WIZARD.equals(id)) {
//            return configure(new PreferencesExportWizard());
//        }
//        if (PREFERENCES_IMPORT_WIZARD.equals(id)) {
//            return configure(new PreferencesImportWizard());
//        }
        if (PROGRESS_VIEW.equals(id)) {
            return configure(new ProgressView());
        }
//        if (WORKBENCH_PREFERENCE_PAGE.equals(id)) {
//            return configure(new WorkbenchPreferencePage());
//        }
//        if (CONTENT_TYPES_PREFERENCE_PAGE.equals(id)) {
//            return configure(new ContentTypesPreferencePage());
//        }
            
        throw new CoreException(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
                0, "Unknown id in data argument for " + getClass(), null)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
     *      java.lang.String, java.lang.Object)
     */
    public void setInitializationData(IConfigurationElement config,
            String propertyName, Object data) throws CoreException {
        if (data instanceof String) {
            id = (String) data;
        } else {
            throw new CoreException(new Status(IStatus.ERROR,
                    PlatformUI.PLUGIN_ID, 0,
                    "Data argument must be a String for " + getClass(), null)); //$NON-NLS-1$
        }
        this.config = config;
        this.propertyName = propertyName;
    }
}
