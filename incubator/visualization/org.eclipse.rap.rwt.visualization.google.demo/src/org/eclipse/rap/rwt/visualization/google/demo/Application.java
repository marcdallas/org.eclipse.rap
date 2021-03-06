/******************************************************************************
 * Copyright (c) 2009-2010 Texas Center for Applied Technology
 * Texas Engineering Experiment Station
 * The Texas A&M University System
 * and David Donahue 
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle (Texas Center for Applied Technology) - 
 *                   initial demo implementation
 *     David Donahue - examples from API documentation
 *                     @see org.eclipse.rap.rwt.visualization.google 
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.google.demo;

import java.util.Date;

import org.eclipse.rap.rwt.visualization.google.AnnotatedTimeLine;
import org.eclipse.rap.rwt.visualization.google.AreaChart;
import org.eclipse.rap.rwt.visualization.google.BarChart;
import org.eclipse.rap.rwt.visualization.google.ColumnChart;
import org.eclipse.rap.rwt.visualization.google.Gauge;
import org.eclipse.rap.rwt.visualization.google.Geomap;
import org.eclipse.rap.rwt.visualization.google.IntensityMap;
import org.eclipse.rap.rwt.visualization.google.LineChart;
import org.eclipse.rap.rwt.visualization.google.MotionChart;
import org.eclipse.rap.rwt.visualization.google.PieChart;
import org.eclipse.rap.rwt.visualization.google.ScatterChart;
import org.eclipse.rap.rwt.visualization.google.Table;
import org.eclipse.rap.rwt.visualization.google.VisualizationWidget;
import org.eclipse.rap.rwt.visualization.google.MotionChart.StateListener;
import org.eclipse.rap.rwt.visualization.google.json.JSONGoogleDataTable;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint 
{
 
  public static Listener createSelectionListener () {
    return new Listener() 
    {
      
      public void handleEvent(Event event) 
      {
        VisualizationWidget widget = (VisualizationWidget)event.widget;
        System.out.println( "Selected item=" + widget.getSelectedItem() + 
            "; row=" + widget.getSelectedRow() +
            "; column=" + widget.getSelectedColumn() +
            "; value=" + widget.getSelectedValue());
      }
      
    };
  }
   
  public int createUI() 
  {
    Display display = PlatformUI.createDisplay();
    PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
    return 0;
  }
  
  public static class MotionExample extends ViewPart 
  {
     
    private MotionChart viz;

    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Model", "Model", "string", null);
      dataTable.addColumn("thedate", "Date", "date", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"Model1", new Date(), Integer.valueOf(389), Double.valueOf(14.8)});
      dataTable.addRow(new Object[] {"Model1", new Date(4070908800L), Integer.valueOf(450), Integer.valueOf(19)});
      dataTable.addRow(new Object[] {"Model2", new Date(), Integer.valueOf(389), Double.valueOf(14.8)});
      dataTable.addRow(new Object[] {"Model2", new Date(4070908800L), Integer.valueOf(700), Integer.valueOf(23)});
      
      viz = new MotionChart( parent , SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addStateListener(new StateListener() 
      {
        
        public void stateChanged(String state)
        {
          System.out.println("State Changed: "+state);
        }
        
      });
      viz.setToolTipText("Motion Chart");
    }

    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class TimelineExample extends ViewPart 
  {
     
    private AnnotatedTimeLine viz;

    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Date", "Date", "date", null);
      dataTable.addColumn("AverageGPA", "Average GPA", "number", null);
      dataTable.addRow(new Object[] {new Date(), Double.valueOf(2.85)});
      dataTable.addRow(new Object[] {new Date(1230809560), Double.valueOf(3.5)});
      dataTable.addRow(new Object[] {new Date(1210000000), Integer.valueOf(3)});
      
      viz = new AnnotatedTimeLine( parent, SWT.NONE );
      viz.setWidgetOptions("displayAnnotations: true");
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
      viz.setToolTipText("Annotated Timeline");
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class AreaChartExample extends ViewPart 
  {
     
    private AreaChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"1970", Integer.valueOf(325), Double.valueOf(14.1)});
      dataTable.addRow(new Object[] {"2009", Integer.valueOf(389), Double.valueOf(14.7)});
      
      viz = new AreaChart(parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
     
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class BarChartExample extends ViewPart 
  {
     
    private BarChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"1970", Integer.valueOf(325), Double.valueOf(14.1)});
      dataTable.addRow(new Object[] {"2009", Integer.valueOf(389), Double.valueOf(14.7)});
      
      viz = new BarChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class ColumnChartExample extends ViewPart 
  {
     
    private ColumnChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"1970", Integer.valueOf(325), Double.valueOf(14.1)});
      dataTable.addRow(new Object[] {"2009", Integer.valueOf(389), Double.valueOf(14.7)});
      
      viz = new ColumnChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class GaugeExample extends ViewPart 
  {
     
    private Gauge viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("CH4", "CH4", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {Integer.valueOf(389), Integer.valueOf(1800), Integer.valueOf(14)});
      
      viz = new Gauge( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
     
  }
  
  public static class GeomapExample extends ViewPart 
  {
     
    private Geomap viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Country", "Country", "string", null);
      dataTable.addColumn("Happiness", "Happiness", "number", null);
      dataTable.addRow(new Object[] {"Tanzania", Integer.valueOf(25)});
      dataTable.addRow(new Object[] {"US", Integer.valueOf(40)});
      
      viz = new Geomap( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class IntensityMapExample extends ViewPart 
  {
     
    private IntensityMap viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Country", "Country", "string", null);
      dataTable.addColumn("Happiness", "Happiness", "number", null);
      dataTable.addColumn("Income", "Income", "number", null);
      dataTable.addRow(new Object[] {"TZ", Integer.valueOf(25), Integer.valueOf(3)});
      dataTable.addRow(new Object[] {"US", Integer.valueOf(40), Integer.valueOf(40)});
      dataTable.addRow(new Object[] {"UK", Integer.valueOf(38), Integer.valueOf(35)});
      
      viz = new IntensityMap( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
     
  }
  
  public static class LineChartExample extends ViewPart 
  {
    
    private LineChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Month", "Month", "string", null);
      dataTable.addColumn("Provider1", "Provider 1", "number", null);
      dataTable.addColumn("Provider2", "Provider 2", "number", null);
      dataTable.addColumn("Provider3", "Provider 3", "number", null);
      dataTable.addRow(new Object[] {"May", Integer.valueOf(10), Integer.valueOf(15), Integer.valueOf(20)});
      dataTable.addRow(new Object[] {"June", Integer.valueOf(12), Integer.valueOf(23), Integer.valueOf(33)});
      dataTable.addRow(new Object[] {"July", Integer.valueOf(11), Integer.valueOf(25), Integer.valueOf(50)});
      
      viz = new LineChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class PieChartExample extends ViewPart 
  {
     
    private PieChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Activity", "Activity", "string", null);
      dataTable.addColumn("Hours", "Hours per Week", "number", null);
      dataTable.addRow(new Object[] {"software architect", Integer.valueOf(40)});
      dataTable.addRow(new Object[] {"primary care medicine", Integer.valueOf(9)});
      dataTable.addRow(new Object[] {"open source development", Integer.valueOf(10)});
      
      viz = new PieChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class ScatterChartExample extends ViewPart 
  {
     
    private ScatterChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("CH4", "CH4", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {Integer.valueOf(350), Integer.valueOf(10), Integer.valueOf(22)});
      dataTable.addRow(new Object[] {Integer.valueOf(375), Integer.valueOf(12), Integer.valueOf(23)});
      dataTable.addRow(new Object[] {Integer.valueOf(400), Integer.valueOf(16), Integer.valueOf(25)});
      
      viz = new ScatterChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class TableExample extends ViewPart 
  {
    
    private Table viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature (C)", "number", null);
      dataTable.addRow(new Object[] {"1970", Integer.valueOf(325), Double.valueOf(14.1)});
      dataTable.addRow(new Object[] {"2009", Integer.valueOf(389), Double.valueOf(14.8)});
      viz = new Table( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
}
