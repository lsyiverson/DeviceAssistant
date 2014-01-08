package com.lsyiverson.deviceassistant;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.lsyiverson.deviceassistant.utils.GpuUtils;

public class RendererLoader extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);
        mGLView.setRenderer(new ClearRenderer());
        setContentView(mGLView);

        new CountDownTimer(3000, 9999)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not used
            }
            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    private GLSurfaceView mGLView;
    class ClearRenderer implements GLSurfaceView.Renderer {

        Random aleatorio = new Random();

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            float r = aleatorio.nextFloat();
            float g = aleatorio.nextFloat();
            float b = aleatorio.nextFloat();
            gl.glClearColor(r, g, b, 1.0f);

            Log.d("debug", "lisy=================renderer:" + gl.glGetString(GL10.GL_RENDERER));
            Log.d("debug", "lisy=================vendor:" + gl.glGetString(GL10.GL_VENDOR));

            GpuUtils.getInstance(RendererLoader.this).setRenderer(gl.glGetString(GL10.GL_RENDERER));
            GpuUtils.getInstance(RendererLoader.this).setVendor(gl.glGetString(GL10.GL_VENDOR));

            RendererLoader.this.finish();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int w, int h) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }
    }
}

