package com.glumes.camerasample.glrender;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @Author glumes
 */

public class GLRenderView extends GLSurfaceView{


    public GLRenderView(Context context) {
        super(context);
        init();
    }

    public GLRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }
}
