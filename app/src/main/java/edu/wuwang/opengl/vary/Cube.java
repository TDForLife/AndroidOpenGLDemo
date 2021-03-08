package edu.wuwang.opengl.vary;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * Created by wuwang on 2016/10/30
 */

public class Cube {

    final float[] cubePositions = {
            -1.0f, 1.0f, 1.0f,    //正面左上0
            -1.0f, -1.0f, 1.0f,   //正面左下1
            1.0f, -1.0f, 1.0f,    //正面右下2
            1.0f, 1.0f, 1.0f,     //正面右上3
            -1.0f, 1.0f, -1.0f,   //反面左上4
            -1.0f, -1.0f, -1.0f,  //反面左下5
            1.0f, -1.0f, -1.0f,   //反面右下6
            1.0f, 1.0f, -1.0f,    //反面右上7
    };
    final short[] index = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };

    float[] color = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };

    private FloatBuffer vertexBuf, colorBuf;
    private ShortBuffer indexBuf;
    private Resources res;
    private int mProgram;
    private int hVertex;
    private int hColor;
    private int hMatrix;

    public Cube(Resources res) {
        this.res = res;
        initData();
    }

    private void initData() {
        vertexBuf = ByteBuffer.allocateDirect(cubePositions.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuf.put(cubePositions);
        vertexBuf.position(0);

        colorBuf= ByteBuffer.allocateDirect(color.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuf.put(color);
        colorBuf.position(0);

        indexBuf = ByteBuffer.allocateDirect(index.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuf.put(index);
        indexBuf.position(0);
    }

    public void create() {
        mProgram = ShaderUtils.createProgram(res, "vary/vertex.sh", "vary/fragment.sh");
        hVertex = GLES20.glGetAttribLocation(mProgram, "vPosition");
        hColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        hMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }

    private float[] matrix;

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    public void drawSelf() {
        // 将程序加入到 OpenGLES2.0 环境
        GLES20.glUseProgram(mProgram);
        // 指定 vMatrix 的值
        if (matrix != null) {
            GLES20.glUniformMatrix4fv(hMatrix, 1, false, matrix, 0);
        }
        // 启用句柄
        GLES20.glEnableVertexAttribArray(hVertex);
        GLES20.glEnableVertexAttribArray(hColor);
        // 准备三角形的坐标数据
        GLES20.glVertexAttribPointer(hVertex, 3, GLES20.GL_FLOAT, false,0, vertexBuf);
        // 设置绘制三角形的颜色
        GLES20.glVertexAttribPointer(hColor, 4, GLES20.GL_FLOAT, false,0, colorBuf);
        // 索引法绘制正方体
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuf);
        // 禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(hVertex);
        GLES20.glDisableVertexAttribArray(hColor);
    }

}
