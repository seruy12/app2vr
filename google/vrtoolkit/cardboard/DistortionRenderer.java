package com.google.vrtoolkit.cardboard;

import android.opengl.GLES20;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: classes.dex */
public class DistortionRenderer {
    static final String FRAGMENT_SHADER = "precision mediump float;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = vVignette * texture2D(uTextureSampler, vTextureCoord);\n}\n";
    static final String FRAGMENT_SHADER_ABERRATION = "precision mediump float;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = vVignette * vec4(texture2D(uTextureSampler, vRedTextureCoord).r,\n          texture2D(uTextureSampler, vGreenTextureCoord).g,\n          texture2D(uTextureSampler, vBlueTextureCoord).b, 1.0);\n}\n";
    private static final String TAG = "DistortionRenderer";
    private static final int TEXTURE_FORMAT = 6407;
    private static final int TEXTURE_TYPE = 5121;
    static final String VERTEX_SHADER = "attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n  gl_Position = vec4(aPosition, 0.0, 1.0);\n  vTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n  vVignette = aVignette;\n}\n";
    static final String VERTEX_SHADER_ABERRATION = "attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aRedTextureCoord;\nattribute vec2 aGreenTextureCoord;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n  gl_Position = vec4(aPosition, 0.0, 1.0);\n  vRedTextureCoord = aRedTextureCoord.xy * uTextureCoordScale;\n  vGreenTextureCoord = aGreenTextureCoord.xy * uTextureCoordScale;\n  vBlueTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n  vVignette = aVignette;\n}\n";
    private boolean chromaticAberrationCorrectionEnabled;
    private boolean drawingFrame;
    private boolean fovsChanged;
    private HeadMountedDisplay hmd;
    private DistortionMesh leftEyeDistortionMesh;
    private EyeViewport leftEyeViewport;
    private float metersPerTanAngle;
    private ProgramHolder programHolder;
    private ProgramHolderAberration programHolderAberration;
    private boolean restoreGLStateEnabled;
    private DistortionMesh rightEyeDistortionMesh;
    private EyeViewport rightEyeViewport;
    private boolean viewportsChanged;
    private boolean vignetteEnabled;
    private float xPxPerTanAngle;
    private float yPxPerTanAngle;
    private int textureId = -1;
    private int renderbufferId = -1;
    private int framebufferId = -1;
    private IntBuffer originalFramebufferId = IntBuffer.allocate(1);
    private float resolutionScale = 1.0f;
    private GLStateBackup gLStateBackup = new GLStateBackup();
    private GLStateBackup gLStateBackupAberration = new GLStateBackup();

    class DistortionMesh {
        public static final int BYTES_PER_FLOAT = 4;
        public static final int BYTES_PER_SHORT = 2;
        public static final int COLS = 40;
        public static final int COMPONENTS_PER_VERT = 9;
        public static final int DATA_BUV_OFFSET = 7;
        public static final int DATA_GUV_OFFSET = 5;
        public static final int DATA_POS_COMPONENTS = 2;
        public static final int DATA_POS_OFFSET = 0;
        public static final int DATA_RUV_OFFSET = 3;
        public static final int DATA_STRIDE_BYTES = 36;
        public static final int DATA_UV_COMPONENTS = 2;
        public static final int DATA_VIGNETTE_COMPONENTS = 1;
        public static final int DATA_VIGNETTE_OFFSET = 2;
        public static final int ROWS = 40;
        public static final float VIGNETTE_SIZE_TAN_ANGLE = 0.05f;
        public int arrayBufferId;
        public int elementBufferId;
        public int nIndices;

        public DistortionMesh(Distortion distortion, Distortion distortion2, Distortion distortion3, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, boolean z) {
            short s;
            this.arrayBufferId = -1;
            this.elementBufferId = -1;
            float[] fArr = new float[14400];
            short s2 = 0;
            int i = 0;
            while (i < 40) {
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    s = s2;
                    if (i3 < 40) {
                        float f13 = ((i3 / 39.0f) * (f11 / f5)) + (f9 / f5);
                        float f14 = ((i / 39.0f) * (f12 / f6)) + (f10 / f6);
                        float f15 = (f13 * f5) - f7;
                        float f16 = (f14 * f6) - f8;
                        float fSqrt = (float) Math.sqrt((f15 * f15) + (f16 * f16));
                        float fDistortInverse = fSqrt > 0.0f ? distortion3.distortInverse(fSqrt) / fSqrt : 1.0f;
                        float f17 = f15 * fDistortInverse;
                        float f18 = f16 * fDistortInverse;
                        float f19 = (f17 + f3) / f;
                        float f20 = (f18 + f4) / f2;
                        float f21 = fSqrt * fDistortInverse;
                        float fDistortionFactor = f21 > 0.0f ? distortion2.distortionFactor(f21) : 1.0f;
                        float f22 = ((f17 * fDistortionFactor) + f7) / f5;
                        float f23 = ((fDistortionFactor * f18) + f8) / f6;
                        float fDistortionFactor2 = f21 > 0.0f ? distortion.distortionFactor(f21) : 1.0f;
                        float f24 = ((f17 * fDistortionFactor2) + f7) / f5;
                        float f25 = ((fDistortionFactor2 * f18) + f8) / f6;
                        float f26 = 0.05f / fDistortInverse;
                        float fClamp = (f15 + f7) - DistortionRenderer.clamp(f15 + f7, f9 + f26, (f9 + f11) - f26);
                        float fClamp2 = (f16 + f8) - DistortionRenderer.clamp(f16 + f8, f10 + f26, (f10 + f12) - f26);
                        float fClamp3 = DistortionRenderer.this.vignetteEnabled ? 1.0f - DistortionRenderer.clamp(((float) Math.sqrt((fClamp * fClamp) + (fClamp2 * fClamp2))) / f26, 0.0f, 1.0f) : 1.0f;
                        if (z) {
                            f13 = 1.0f - f13;
                            f24 = 1.0f - f24;
                            f22 = 1.0f - f22;
                            f14 = 1.0f - f14;
                            f25 = 1.0f - f25;
                            f23 = 1.0f - f23;
                        }
                        fArr[s + 0] = (2.0f * f19) - 1.0f;
                        fArr[s + 1] = (2.0f * f20) - 1.0f;
                        fArr[s + 2] = fClamp3;
                        fArr[s + 3] = f24;
                        fArr[s + 4] = f25;
                        fArr[s + 5] = f22;
                        fArr[s + 6] = f23;
                        fArr[s + 7] = f13;
                        fArr[s + 8] = f14;
                        s2 = (short) (s + 9);
                        i2 = i3 + 1;
                    }
                }
                i++;
                s2 = s;
            }
            this.nIndices = 3158;
            short[] sArr = new short[this.nIndices];
            short s3 = 0;
            short s4 = 0;
            int i4 = 0;
            while (i4 < 39) {
                if (i4 > 0) {
                    sArr[s3] = sArr[s3 - 1];
                    s3 = (short) (s3 + 1);
                }
                short s5 = s3;
                short s6 = s4;
                short s7 = s5;
                for (int i5 = 0; i5 < 40; i5++) {
                    if (i5 > 0) {
                        s6 = i4 % 2 == 0 ? (short) (s6 + 1) : (short) (s6 - 1);
                    }
                    short s8 = (short) (s7 + 1);
                    sArr[s7] = s6;
                    s7 = (short) (s8 + 1);
                    sArr[s8] = (short) (s6 + 40);
                }
                i4++;
                short s9 = s7;
                s4 = (short) (s6 + 40);
                s3 = s9;
            }
            FloatBuffer floatBufferAsFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            floatBufferAsFloatBuffer.put(fArr).position(0);
            ShortBuffer shortBufferAsShortBuffer = ByteBuffer.allocateDirect(sArr.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
            shortBufferAsShortBuffer.put(sArr).position(0);
            int[] iArr = new int[2];
            GLES20.glGenBuffers(2, iArr, 0);
            this.arrayBufferId = iArr[0];
            this.elementBufferId = iArr[1];
            GLES20.glBindBuffer(34962, this.arrayBufferId);
            GLES20.glBufferData(34962, fArr.length * 4, floatBufferAsFloatBuffer, 35044);
            GLES20.glBindBuffer(34963, this.elementBufferId);
            GLES20.glBufferData(34963, sArr.length * 2, shortBufferAsShortBuffer, 35044);
            GLES20.glBindBuffer(34962, 0);
            GLES20.glBindBuffer(34963, 0);
        }
    }

    class EyeViewport {
        public float eyeX;
        public float eyeY;
        public float height;
        public float width;
        public float x;
        public float y;

        private EyeViewport() {
        }

        public String toString() {
            return "{\n" + ("  x: " + this.x + ",\n") + ("  y: " + this.y + ",\n") + ("  width: " + this.width + ",\n") + ("  height: " + this.height + ",\n") + ("  eyeX: " + this.eyeX + ",\n") + ("  eyeY: " + this.eyeY + ",\n") + "}";
        }
    }

    class ProgramHolder {
        public int aBlueTextureCoord;
        public int aPosition;
        public int aVignette;
        public int program;
        public int uTextureCoordScale;
        public int uTextureSampler;

        private ProgramHolder() {
        }
    }

    class ProgramHolderAberration extends ProgramHolder {
        public int aGreenTextureCoord;
        public int aRedTextureCoord;

        private ProgramHolderAberration() {
            super();
        }
    }

    private void checkGlError(String str) {
        int iGlGetError = GLES20.glGetError();
        if (iGlGetError != 0) {
            Log.e(TAG, str + ": glError " + iGlGetError);
            throw new RuntimeException(str + ": glError " + iGlGetError);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    private void clearGlError() {
        while (GLES20.glGetError() != 0) {
        }
    }

    private DistortionMesh createDistortionMesh(EyeViewport eyeViewport, float f, float f2, float f3, float f4, boolean z) {
        return new DistortionMesh(this.hmd.getCardboardDeviceParams().getDistortion(), this.hmd.getCardboardDeviceParams().getDistortion(), this.hmd.getCardboardDeviceParams().getDistortion(), this.hmd.getScreenParams().getWidthMeters() / this.metersPerTanAngle, this.hmd.getScreenParams().getHeightMeters() / this.metersPerTanAngle, f3, f4, f, f2, eyeViewport.eyeX, eyeViewport.eyeY, eyeViewport.x, eyeViewport.y, eyeViewport.width, eyeViewport.height, z);
    }

    private int createProgram(String str, String str2) {
        int iLoadShader;
        int iLoadShader2 = loadShader(35633, str);
        if (iLoadShader2 == 0 || (iLoadShader = loadShader(35632, str2)) == 0) {
            return 0;
        }
        int iGlCreateProgram = GLES20.glCreateProgram();
        if (iGlCreateProgram != 0) {
            while (GLES20.glGetError() != 0) {
            }
            GLES20.glAttachShader(iGlCreateProgram, iLoadShader2);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(iGlCreateProgram, iLoadShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(iGlCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(iGlCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(iGlCreateProgram));
                GLES20.glDeleteProgram(iGlCreateProgram);
                return 0;
            }
        }
        return iGlCreateProgram;
    }

    private ProgramHolder createProgramHolder() {
        return createProgramHolder(false);
    }

    private ProgramHolder createProgramHolder(boolean z) {
        ProgramHolder programHolder;
        GLStateBackup gLStateBackup;
        if (z) {
            programHolder = new ProgramHolderAberration();
            programHolder.program = createProgram(VERTEX_SHADER_ABERRATION, FRAGMENT_SHADER_ABERRATION);
            if (programHolder.program == 0) {
                throw new RuntimeException("Could not create aberration-corrected program");
            }
            gLStateBackup = this.gLStateBackupAberration;
        } else {
            programHolder = new ProgramHolder();
            programHolder.program = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
            if (programHolder.program == 0) {
                throw new RuntimeException("Could not create program");
            }
            gLStateBackup = this.gLStateBackup;
        }
        clearGlError();
        programHolder.aPosition = GLES20.glGetAttribLocation(programHolder.program, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (programHolder.aPosition == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        gLStateBackup.addTrackedVertexAttribute(programHolder.aPosition);
        programHolder.aVignette = GLES20.glGetAttribLocation(programHolder.program, "aVignette");
        checkGlError("glGetAttribLocation aVignette");
        if (programHolder.aVignette == -1) {
            throw new RuntimeException("Could not get attrib location for aVignette");
        }
        gLStateBackup.addTrackedVertexAttribute(programHolder.aVignette);
        if (z) {
            ((ProgramHolderAberration) programHolder).aRedTextureCoord = GLES20.glGetAttribLocation(programHolder.program, "aRedTextureCoord");
            checkGlError("glGetAttribLocation aRedTextureCoord");
            if (((ProgramHolderAberration) programHolder).aRedTextureCoord == -1) {
                throw new RuntimeException("Could not get attrib location for aRedTextureCoord");
            }
            ((ProgramHolderAberration) programHolder).aGreenTextureCoord = GLES20.glGetAttribLocation(programHolder.program, "aGreenTextureCoord");
            checkGlError("glGetAttribLocation aGreenTextureCoord");
            if (((ProgramHolderAberration) programHolder).aGreenTextureCoord == -1) {
                throw new RuntimeException("Could not get attrib location for aGreenTextureCoord");
            }
            gLStateBackup.addTrackedVertexAttribute(((ProgramHolderAberration) programHolder).aRedTextureCoord);
            gLStateBackup.addTrackedVertexAttribute(((ProgramHolderAberration) programHolder).aGreenTextureCoord);
        }
        programHolder.aBlueTextureCoord = GLES20.glGetAttribLocation(programHolder.program, "aBlueTextureCoord");
        checkGlError("glGetAttribLocation aBlueTextureCoord");
        if (programHolder.aBlueTextureCoord == -1) {
            throw new RuntimeException("Could not get attrib location for aBlueTextureCoord");
        }
        gLStateBackup.addTrackedVertexAttribute(programHolder.aBlueTextureCoord);
        programHolder.uTextureCoordScale = GLES20.glGetUniformLocation(programHolder.program, "uTextureCoordScale");
        checkGlError("glGetUniformLocation uTextureCoordScale");
        if (programHolder.uTextureCoordScale == -1) {
            throw new RuntimeException("Could not get attrib location for uTextureCoordScale");
        }
        programHolder.uTextureSampler = GLES20.glGetUniformLocation(programHolder.program, "uTextureSampler");
        checkGlError("glGetUniformLocation uTextureSampler");
        if (programHolder.uTextureSampler == -1) {
            throw new RuntimeException("Could not get attrib location for uTextureSampler");
        }
        return programHolder;
    }

    private int createTexture(int i, int i2, int i3, int i4) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(3553, iArr[0]);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexImage2D(3553, 0, i3, i, i2, 0, i3, i4, null);
        return iArr[0];
    }

    private EyeViewport initViewportForEye(FieldOfView fieldOfView, float f) {
        float fTan = (float) Math.tan(Math.toRadians(fieldOfView.getLeft()));
        float fTan2 = (float) Math.tan(Math.toRadians(fieldOfView.getRight()));
        float fTan3 = (float) Math.tan(Math.toRadians(fieldOfView.getBottom()));
        float fTan4 = (float) Math.tan(Math.toRadians(fieldOfView.getTop()));
        EyeViewport eyeViewport = new EyeViewport();
        eyeViewport.x = f;
        eyeViewport.y = 0.0f;
        eyeViewport.width = fTan2 + fTan;
        eyeViewport.height = fTan3 + fTan4;
        eyeViewport.eyeX = fTan + f;
        eyeViewport.eyeY = fTan3;
        return eyeViewport;
    }

    private int loadShader(int i, String str) {
        int iGlCreateShader = GLES20.glCreateShader(i);
        if (iGlCreateShader != 0) {
            GLES20.glShaderSource(iGlCreateShader, str);
            GLES20.glCompileShader(iGlCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(iGlCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Could not compile shader " + i + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(iGlCreateShader));
                GLES20.glDeleteShader(iGlCreateShader);
                return 0;
            }
        }
        return iGlCreateShader;
    }

    private void renderDistortionMesh(DistortionMesh distortionMesh, int i) {
        ProgramHolder programHolder = this.chromaticAberrationCorrectionEnabled ? this.programHolderAberration : this.programHolder;
        GLES20.glBindBuffer(34962, distortionMesh.arrayBufferId);
        GLES20.glVertexAttribPointer(programHolder.aPosition, 2, 5126, false, 36, 0);
        GLES20.glEnableVertexAttribArray(programHolder.aPosition);
        GLES20.glVertexAttribPointer(programHolder.aVignette, 1, 5126, false, 36, 8);
        GLES20.glEnableVertexAttribArray(programHolder.aVignette);
        GLES20.glVertexAttribPointer(programHolder.aBlueTextureCoord, 2, 5126, false, 36, 28);
        GLES20.glEnableVertexAttribArray(programHolder.aBlueTextureCoord);
        if (this.chromaticAberrationCorrectionEnabled) {
            GLES20.glVertexAttribPointer(((ProgramHolderAberration) programHolder).aRedTextureCoord, 2, 5126, false, 36, 12);
            GLES20.glEnableVertexAttribArray(((ProgramHolderAberration) programHolder).aRedTextureCoord);
            GLES20.glVertexAttribPointer(((ProgramHolderAberration) programHolder).aGreenTextureCoord, 2, 5126, false, 36, 20);
            GLES20.glEnableVertexAttribArray(((ProgramHolderAberration) programHolder).aGreenTextureCoord);
        }
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glUniform1i(this.programHolder.uTextureSampler, 0);
        GLES20.glUniform1f(this.programHolder.uTextureCoordScale, this.resolutionScale);
        GLES20.glBindBuffer(34963, distortionMesh.elementBufferId);
        GLES20.glDrawElements(5, distortionMesh.nIndices, 5123, 0);
    }

    private int setupRenderTextureAndRenderbuffer() {
        float f = this.leftEyeViewport.width + this.rightEyeViewport.width;
        float fMax = Math.max(this.leftEyeViewport.height, this.rightEyeViewport.height);
        int[] iArr = new int[1];
        GLES20.glGetIntegerv(3379, iArr, 0);
        return setupRenderTextureAndRenderbuffer(Math.min(Math.round(f * this.xPxPerTanAngle), iArr[0]), Math.min(Math.round(fMax * this.yPxPerTanAngle), iArr[0]));
    }

    private int setupRenderTextureAndRenderbuffer(int i, int i2) {
        if (this.textureId != -1) {
            GLES20.glDeleteTextures(1, new int[]{this.textureId}, 0);
        }
        if (this.renderbufferId != -1) {
            GLES20.glDeleteRenderbuffers(1, new int[]{this.renderbufferId}, 0);
        }
        if (this.framebufferId != -1) {
            GLES20.glDeleteFramebuffers(1, new int[]{this.framebufferId}, 0);
        }
        while (GLES20.glGetError() != 0) {
        }
        this.textureId = createTexture(i, i2, TEXTURE_FORMAT, TEXTURE_TYPE);
        checkGlError("setupRenderTextureAndRenderbuffer: create texture");
        int[] iArr = new int[1];
        GLES20.glGenRenderbuffers(1, iArr, 0);
        GLES20.glBindRenderbuffer(36161, iArr[0]);
        GLES20.glRenderbufferStorage(36161, 33189, i, i2);
        this.renderbufferId = iArr[0];
        checkGlError("setupRenderTextureAndRenderbuffer: create renderbuffer");
        int[] iArr2 = new int[1];
        GLES20.glGenFramebuffers(1, iArr2, 0);
        GLES20.glBindFramebuffer(36160, iArr2[0]);
        this.framebufferId = iArr2[0];
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.textureId, 0);
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, iArr[0]);
        int iGlCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
        if (iGlCheckFramebufferStatus != 36053) {
            throw new RuntimeException("Framebuffer is not complete: " + Integer.toHexString(iGlCheckFramebufferStatus));
        }
        GLES20.glBindFramebuffer(36160, 0);
        return iArr2[0];
    }

    private void updateDistortionMesh(boolean z) {
        ScreenParams screenParams = this.hmd.getScreenParams();
        CardboardDeviceParams cardboardDeviceParams = this.hmd.getCardboardDeviceParams();
        if (this.programHolder == null) {
            this.programHolder = createProgramHolder();
        }
        if (this.programHolderAberration == null) {
            this.programHolderAberration = (ProgramHolderAberration) createProgramHolder(true);
        }
        float f = this.rightEyeViewport.width + this.leftEyeViewport.width;
        float fMax = Math.max(this.leftEyeViewport.height, this.rightEyeViewport.height);
        float widthMeters = ((screenParams.getWidthMeters() / 2.0f) - (cardboardDeviceParams.getInterLensDistance() / 2.0f)) / this.metersPerTanAngle;
        float yEyeOffsetMeters = cardboardDeviceParams.getYEyeOffsetMeters(screenParams) / this.metersPerTanAngle;
        this.leftEyeDistortionMesh = createDistortionMesh(this.leftEyeViewport, f, fMax, widthMeters, yEyeOffsetMeters, z);
        this.rightEyeDistortionMesh = createDistortionMesh(this.rightEyeViewport, f, fMax, (screenParams.getWidthMeters() / this.metersPerTanAngle) - widthMeters, yEyeOffsetMeters, z);
    }

    public void afterDrawFrame() {
        GLES20.glBindFramebuffer(36160, this.originalFramebufferId.array()[0]);
        undistortTexture(this.textureId);
        this.drawingFrame = false;
    }

    public void beforeDrawFrame() {
        this.drawingFrame = true;
        if (this.fovsChanged) {
            updateDistortionMesh(false);
            setupRenderTextureAndRenderbuffer();
            this.fovsChanged = false;
        }
        GLES20.glGetIntegerv(36006, this.originalFramebufferId);
        GLES20.glBindFramebuffer(36160, this.framebufferId);
    }

    public boolean haveViewportsChanged() {
        return this.viewportsChanged;
    }

    public void onFovChanged(HeadMountedDisplay headMountedDisplay, FieldOfView fieldOfView, FieldOfView fieldOfView2, float f) {
        if (this.drawingFrame) {
            throw new IllegalStateException("Cannot change FOV while rendering a frame.");
        }
        this.hmd = new HeadMountedDisplay(headMountedDisplay);
        this.leftEyeViewport = initViewportForEye(fieldOfView, 0.0f);
        this.rightEyeViewport = initViewportForEye(fieldOfView2, this.leftEyeViewport.width);
        this.metersPerTanAngle = f;
        ScreenParams screenParams = headMountedDisplay.getScreenParams();
        this.xPxPerTanAngle = screenParams.getWidth() / (screenParams.getWidthMeters() / this.metersPerTanAngle);
        this.yPxPerTanAngle = screenParams.getHeight() / (screenParams.getHeightMeters() / this.metersPerTanAngle);
        this.fovsChanged = true;
        this.viewportsChanged = true;
    }

    public void setChromaticAberrationCorrectionEnabled(boolean z) {
        this.chromaticAberrationCorrectionEnabled = z;
    }

    public void setResolutionScale(float f) {
        this.resolutionScale = f;
        this.viewportsChanged = true;
    }

    public void setRestoreGLStateEnabled(boolean z) {
        this.restoreGLStateEnabled = z;
    }

    public void setVignetteEnabled(boolean z) {
        this.vignetteEnabled = z;
        this.fovsChanged = true;
    }

    public void undistortFramebuffer() {
        int width = this.hmd.getScreenParams().getWidth();
        int height = this.hmd.getScreenParams().getHeight();
        if (this.fovsChanged) {
            updateDistortionMesh(true);
            setupRenderTextureAndRenderbuffer(width, height);
            this.fovsChanged = false;
        }
        GLES20.glBindTexture(3553, this.textureId);
        GLES20.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, width, height);
        GLES20.glBindTexture(3553, 0);
        undistortTexture(this.textureId);
    }

    public void undistortTexture(int i) {
        if (this.restoreGLStateEnabled) {
            if (this.chromaticAberrationCorrectionEnabled) {
                this.gLStateBackupAberration.readFromGL();
            } else {
                this.gLStateBackup.readFromGL();
            }
        }
        if (this.fovsChanged) {
            updateDistortionMesh(false);
            this.fovsChanged = false;
        }
        GLES20.glViewport(0, 0, this.hmd.getScreenParams().getWidth(), this.hmd.getScreenParams().getHeight());
        GLES20.glDisable(3089);
        GLES20.glDisable(2884);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16640);
        if (this.chromaticAberrationCorrectionEnabled) {
            GLES20.glUseProgram(this.programHolderAberration.program);
        } else {
            GLES20.glUseProgram(this.programHolder.program);
        }
        GLES20.glEnable(3089);
        GLES20.glScissor(0, 0, this.hmd.getScreenParams().getWidth() / 2, this.hmd.getScreenParams().getHeight());
        renderDistortionMesh(this.leftEyeDistortionMesh, i);
        GLES20.glScissor(this.hmd.getScreenParams().getWidth() / 2, 0, this.hmd.getScreenParams().getWidth() / 2, this.hmd.getScreenParams().getHeight());
        renderDistortionMesh(this.rightEyeDistortionMesh, i);
        if (this.restoreGLStateEnabled) {
            if (this.chromaticAberrationCorrectionEnabled) {
                this.gLStateBackupAberration.writeToGL();
            } else {
                this.gLStateBackup.writeToGL();
            }
        }
    }

    public void updateViewports(Viewport viewport, Viewport viewport2) {
        viewport.setViewport(Math.round(this.leftEyeViewport.x * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.leftEyeViewport.y * this.yPxPerTanAngle * this.resolutionScale), Math.round(this.leftEyeViewport.width * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.leftEyeViewport.height * this.yPxPerTanAngle * this.resolutionScale));
        viewport2.setViewport(Math.round(this.rightEyeViewport.x * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.rightEyeViewport.y * this.yPxPerTanAngle * this.resolutionScale), Math.round(this.rightEyeViewport.width * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.rightEyeViewport.height * this.yPxPerTanAngle * this.resolutionScale));
        this.viewportsChanged = false;
    }
}
