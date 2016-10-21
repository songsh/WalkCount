

/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类�? * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.walkcount.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
/**
 * @ClassName SplineChart01View
 * @Description * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class SplineChart04View extends DemoView {

	private String TAG = "SplineChart04View";
	private SplineChart chart = new SplineChart();
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
	Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);
	LinkedList<PointD> linePoint1 = new LinkedList<PointD>();	
	LinkedList<PointD> linePoint2 = new LinkedList<PointD>();	
	
	private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
	
	public SplineChart04View(Context context) {
		super(context);
		initView();
	}
	
	public SplineChart04View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public SplineChart04View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
//			chartLabels();
			chartDataSet();	
			chartDesireLines();
			chartRender();
			
			//綁定手势滑动事件
//			this.bindTouch(this,chart);
	 }
	 
	 
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大�?        
        chart.setChartRange(w,h);
    }  				
	
	
	private void chartRender()
	{
		try {
						
			int [] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0] + DensityUtil.dip2px(this.getContext(), 10), ltrb[1],
						ltrb[2]+DensityUtil.dip2px(this.getContext(), 20), ltrb[3]);	
			
			//标题
			chart.setTitle("Z-Sensor Data");
			chart.getAxisTitle().setLeftTitle("Z-Sensor");
			chart.getAxisTitle().getLeftTitlePaint().setColor(Color.BLACK);
			
		
			//显示边框
			chart.showRoundBorder();
			
			//数据�?
			chart.setCategories(labels);
			chart.setDataSource(chartData);
			chart.setCustomLines(mCustomLineDataset);
						
			//坐标�?			//数据轴最大�?
			chart.getDataAxis().setAxisMax(10);
			chart.getDataAxis().setAxisMin(-10);
			//数据轴刻度间�?			
			chart.getDataAxis().setAxisSteps(2);
			
			//标签轴最大�?
			chart.setCategoryAxisMax(100);	
			//标签轴最小�?
			chart.setCategoryAxisMin(0);
			
			//背景网格
			PlotGrid plot = chart.getPlotGrid();		
			plot.hideHorizontalLines();
			plot.hideVerticalLines();					
			chart.getDataAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));
			chart.getCategoryAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));
			
			chart.getDataAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));
			chart.getCategoryAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));					
			
			//定义数据轴标签显示格�?	
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub		
					Double tmp = Double.parseDouble(value);
					DecimalFormat df=new DecimalFormat("#0");
					String label = df.format(tmp).toString();				
					return (label);
				}				
			});				
			
			//不使用精确计算，忽略Java计算误差,提高性能
			chart.disableHighPrecision();
			
			chart.disablePanMode();
			chart.hideBorder();
			chart.getPlotLegend().hide();
			
			//chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}
	
	private void chartDataSet()
	{
		
				linePoint1.add(new PointD(0d, 0d));		
				SplineData dataSeries1 = new SplineData("original",linePoint1,
						Color.rgb(54, 141, 238) );
				//把线弄细�?	
				
				dataSeries1.getLinePaint().setStrokeWidth(3);
				dataSeries1.setLineStyle(XEnum.LineStyle.DASH);	
				dataSeries1.setLabelVisible(false);					
				dataSeries1.setDotStyle(XEnum.DotStyle.HIDE);

				linePoint2.add(new PointD(0d, 0d));	
				SplineData dataSeries2 = new SplineData("sqart",linePoint2,
						Color.rgb(255, 165, 132) );										
				dataSeries2.setLabelVisible(false);		
				dataSeries2.setDotStyle(XEnum.DotStyle.HIDE);		
			
				chartData.add(dataSeries1);	
				chartData.add(dataSeries2);	
	}
	
	private void chartLabels()
	{
		labels.add("2018");
		labels.add("2019");
		labels.add("2020");
		labels.add("2021");
		labels.add("2022");
		labels.add("2023");
	}
	
	private void chartDesireLines()
	{			
		CustomLineData s = new CustomLineData("original",15d,Color.rgb(54, 141, 238),3);
		
		s.hideLine();		
		s.getLineLabelPaint().setColor(Color.rgb(54, 141, 238));
		s.getLineLabelPaint().setTextSize(27);
		s.setLineStyle(XEnum.LineStyle.DASH);
		s.setLabelOffset(5);
		mCustomLineDataset.add(s);
		
		CustomLineData s2 = new CustomLineData("sqrt",18d,Color.rgb(255, 165, 132),3);
		
		s2.hideLine();		
		s2.getLineLabelPaint().setColor(Color.rgb(255, 165, 132));
		s2.getLineLabelPaint().setTextSize(25);
		s2.setLabelOffset(5);
		mCustomLineDataset.add(s2);
	
	}
	

	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }
	
	public void updateData(LinkedList<Float> oList,LinkedList<Float> dList){
		
		if(linePoint1.size()>100){
			linePoint1.clear();
			linePoint2.clear();
		}
		linePoint1.add(new PointD(linePoint1.size(), oList.pollLast()));
		linePoint2.add(new PointD(linePoint2.size(), dList.pollLast()));
		invalidate();
	}
	
}
