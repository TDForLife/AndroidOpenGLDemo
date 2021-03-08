package edu.wuwang.opengl.utils;

import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by wuwang on 2016/10/30
 */

public class VaryTools {

    // 相机矩阵
    private final float[] mCameraMatrix = new float[16];
    // 投影矩阵
    private final float[] mProjectionMatrix = new float[16];
    // 原始矩阵
    private float[] mCurrentMatrix = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    // 变换矩阵堆栈
    private final Stack<float[]> mVaryMatrixStack = new Stack<>();

    // 保护现场
    public void pushMatrix() {
        mVaryMatrixStack.push(Arrays.copyOf(mCurrentMatrix, 16));
    }

    // 恢复现场
    public void popMatrix() {
        mCurrentMatrix = mVaryMatrixStack.pop();
    }

    public void clearStack() {
        mVaryMatrixStack.clear();
    }

    // 平移变换
    public void translate(float x, float y, float z) {
        Matrix.translateM(mCurrentMatrix, 0, x, y, z);
    }

    // 旋转变换
    public void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mCurrentMatrix, 0, angle, x, y, z);
    }

    // 缩放变换
    public void scale(float x, float y, float z) {
        Matrix.scaleM(mCurrentMatrix, 0, x, y, z);
    }

    // 设置相机
    public void setCamera(float ex, float ey, float ez, float cx, float cy, float cz, float ux, float uy, float uz) {
        Matrix.setLookAtM(mCameraMatrix, 0, ex, ey, ez, cx, cy, cz, ux, uy, uz);
    }

    public void frustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    // 正交投影
    public void ortho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    public float[] getFinalMatrix() {
        float[] resultMatrix = new float[16];
        Matrix.multiplyMM(resultMatrix, 0, mCameraMatrix, 0, mCurrentMatrix, 0);
        Matrix.multiplyMM(resultMatrix, 0, mProjectionMatrix, 0, resultMatrix, 0);
        return resultMatrix;
    }

}
