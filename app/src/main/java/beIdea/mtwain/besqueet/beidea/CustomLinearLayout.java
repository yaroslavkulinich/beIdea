package beIdea.mtwain.besqueet.beidea;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {

	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public CustomLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public int screenWidth;
	public int screenHeight;
    public float fractionX;
    public float fractionY;

    protected void onSizeChanged(int w, int h, int oldW, int oldh){

        // Assign the actual screen width to our class variable.
        screenWidth = w;            
        screenHeight = h;
        super.onSizeChanged(w, h, oldW, oldh);
    }

    public float getFractionX(){

        return fractionX;
    }

    public void setFractionX(float xFraction){

        this.fractionX = xFraction;

        // When we modify the xFraction, we want to adjust the x translation
        // accordingly.  Here, the scale is that if xFraction is -1, then
        // the layout is off screen to the left, if xFraction is 0, then the 
        // layout is exactly on the screen, and if xFraction is 1, then the 
        // layout is completely offscreen to the right.
        setX((screenWidth > 0) ? (xFraction * screenWidth) : 0);
    }
    
    public void setFractionY(float yFraction){

        this.fractionY = yFraction;

        // When we modify the xFraction, we want to adjust the x translation
        // accordingly.  Here, the scale is that if xFraction is -1, then
        // the layout is off screen to the left, if xFraction is 0, then the 
        // layout is exactly on the screen, and if xFraction is 1, then the 
        // layout is completely offscreen to the right.
        setY((screenHeight > 0) ? (yFraction * screenHeight) : 0);
    }

	

	
	

}
