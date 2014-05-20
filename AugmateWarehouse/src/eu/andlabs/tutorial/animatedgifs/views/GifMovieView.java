package eu.andlabs.tutorial.animatedgifs.views;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class GifMovieView extends View {

    private Movie mMovie;

    private long mMoviestart;

    public GifMovieView(Context context, InputStream stream) {
        super(context);
        this.setInputStream(stream);
    }

    public GifMovieView(final Context context, final AttributeSet attrs) {
    	super(context, attrs);
    }
    
    public void setInputStream(InputStream stream)
    {
        mMovie = Movie.decodeStream(stream);  
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        final long now = SystemClock.uptimeMillis();

        if (mMoviestart == 0) { 
            mMoviestart = now;
        }

        if (mMovie.duration() > 0)
        {
	        final int relTime = (int)((now - mMoviestart) % mMovie.duration());
	        mMovie.setTime(relTime);
	        mMovie.draw(canvas, 10, 10);
        }
        this.invalidate();
    }
}