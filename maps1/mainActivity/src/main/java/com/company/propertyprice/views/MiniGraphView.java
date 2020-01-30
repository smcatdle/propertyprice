package com.company.propertyprice.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.company.geo.GraphPoint;
import com.company.propertyprice.util.RemoteLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class MiniGraphView extends View {

	private Path path = null;
	private Paint paint = null;
	private Map<String, GraphPoint> graphPoints = null;
	int width = 0;
	int height = 0;
	float maxYCoordinate = 0;
	float minYCoordinate = 100000000;

	public MiniGraphView(Context context) {
		super(context);
		initialise();
	}

	public MiniGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialise();
	}

	public MiniGraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialise();

	}

	public void initialise() {

		path = new Path();
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4);
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setShadowLayer(4, 2, 2, 0x80000000);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		try {
			
			float yCoordinate = 0;
			float lastYCoordinate = 0;
			String pathString = "";
			List<Float> pathEntries = new ArrayList<Float>();
			path.reset();
			
			measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			width = getMeasuredWidth();
			height = getMeasuredHeight();
			
			width = this.getWidth();
			height = this.getHeight();
			
			
			if (graphPoints != null) {
				
				GraphPoint graphPoint = graphPoints.get("201" + 0);
				yCoordinate = getYCoordinateOffset(graphPoint);
				pathEntries.add(yCoordinate);
				
				for (int i = 1; i < 6; i++) {
					graphPoint = graphPoints.get("201" + i);
					lastYCoordinate = yCoordinate;
	
					yCoordinate = getYCoordinateOffset(graphPoint);
					if (yCoordinate == 0) {
						yCoordinate = lastYCoordinate;
					}
					pathEntries.add(yCoordinate);
				}
				
				// Now calculate the yScale (this allows all graph points to fit inside the view)
				float yScale = getYScale();
				
				//  Draw the lines on the canvas from the calculated coords
				int count = 0;
				float scaledXCord = 0;
				float scaledYCord = 0;
				
				for (Float yCord : pathEntries) {
	
					scaledXCord = count * (width/pathEntries.size());
					scaledYCord = height - (yCord * yScale);   // Inverse required as (0,0) is top left
	
					if (count == 0) {
						path.moveTo(scaledXCord, scaledYCord);
						pathString = pathString + "[" + scaledYCord
								+ "]";
					} else {
						
						path.lineTo(scaledXCord, scaledYCord);
						pathString = pathString + "[" + scaledYCord
								+ "]";
					}
					count++;
				}
	
				
				canvas.drawPath(path, paint);
				
				RemoteLog.log("pathString " + pathString);
			}


		} catch (Exception ex) {
			RemoteLog.error(ex);
		}

	}

	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	

	}*/

	public void setData(Map<String, GraphPoint> graphPoints) {

		this.graphPoints = graphPoints;

		// NB: Only invalidate() the specific UI component to reduce cpu/memory usage (not parent)
		invalidate();

	}

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getViewDimensions();
    }
    
    
	private float getYCoordinateOffset(GraphPoint graphPoint) {
		
		float yCoordinate = 0;
		
		if (graphPoint != null) {
			double total = graphPoint.getT();
			double quantity = graphPoint.getQ();
			yCoordinate = (float) (total / quantity);
				
		} else {
			yCoordinate = 0;
		}
		
		// Check Max
		if (yCoordinate > maxYCoordinate) maxYCoordinate = yCoordinate;
		
		// Check Min
		if (yCoordinate != 0 && yCoordinate < minYCoordinate) minYCoordinate = yCoordinate;
		
		return yCoordinate;
	}
	
	private void getViewDimensions() {
		width = this.getWidth();
		height = this.getHeight();

	}
	
	private float getYScale() {
		RemoteLog.log("maxYCoordinate [" + maxYCoordinate + "] minYCoordinate [" + minYCoordinate + "]");
		return height/(maxYCoordinate - minYCoordinate);
	}
	
	//TODO: Render gradient in graph background
	private void renderGradient() {
		/*var my_gradient = context.createLinearGradient(0, 0, 300, 225);
		my_gradient.addColorStop(0, "black");
		my_gradient.addColorStop(1, "white");
		context.fillStyle = my_gradient;
		context.fillRect(0, 0, 300, 225);*/
		
	}
}
