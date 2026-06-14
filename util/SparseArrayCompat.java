package android.support.v4.util;

/* JADX INFO: loaded from: classes.dex */
public class SparseArrayCompat {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    public SparseArrayCompat() {
        this(10);
    }

    public SparseArrayCompat(int i) {
        this.mGarbage = false;
        int iIdealIntArraySize = idealIntArraySize(i);
        this.mKeys = new int[iIdealIntArraySize];
        this.mValues = new Object[iIdealIntArraySize];
        this.mSize = 0;
    }

    private static int binarySearch(int[] iArr, int i, int i2, int i3) {
        int i4 = i - 1;
        int i5 = i + i2;
        while (i5 - i4 > 1) {
            int i6 = (i5 + i4) / 2;
            if (iArr[i6] < i3) {
                i4 = i6;
            } else {
                i5 = i6;
            }
        }
        return i5 == i + i2 ? (i + i2) ^ (-1) : iArr[i5] != i3 ? i5 ^ (-1) : i5;
    }

    private void gc() {
        int i = this.mSize;
        int[] iArr = this.mKeys;
        Object[] objArr = this.mValues;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != DELETED) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    objArr[i2] = obj;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    static int idealByteArraySize(int i) {
        for (int i2 = 4; i2 < 32; i2++) {
            if (i <= (1 << i2) - 12) {
                return (1 << i2) - 12;
            }
        }
        return i;
    }

    static int idealIntArraySize(int i) {
        return idealByteArraySize(i * 4) / 4;
    }

    public void append(int i, Object obj) {
        if (this.mSize != 0 && i <= this.mKeys[this.mSize - 1]) {
            put(i, obj);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            gc();
        }
        int i2 = this.mSize;
        if (i2 >= this.mKeys.length) {
            int iIdealIntArraySize = idealIntArraySize(i2 + 1);
            int[] iArr = new int[iIdealIntArraySize];
            Object[] objArr = new Object[iIdealIntArraySize];
            System.arraycopy(this.mKeys, 0, iArr, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, objArr, 0, this.mValues.length);
            this.mKeys = iArr;
            this.mValues = objArr;
        }
        this.mKeys[i2] = i;
        this.mValues[i2] = obj;
        this.mSize = i2 + 1;
    }

    public void clear() {
        int i = this.mSize;
        Object[] objArr = this.mValues;
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void delete(int i) {
        int iBinarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        if (iBinarySearch < 0 || this.mValues[iBinarySearch] == DELETED) {
            return;
        }
        this.mValues[iBinarySearch] = DELETED;
        this.mGarbage = true;
    }

    public Object get(int i) {
        return get(i, null);
    }

    public Object get(int i, Object obj) {
        int iBinarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        return (iBinarySearch < 0 || this.mValues[iBinarySearch] == DELETED) ? obj : this.mValues[iBinarySearch];
    }

    public int indexOfKey(int i) {
        if (this.mGarbage) {
            gc();
        }
        return binarySearch(this.mKeys, 0, this.mSize, i);
    }

    public int indexOfValue(Object obj) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == obj) {
                return i;
            }
        }
        return -1;
    }

    public int keyAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[i];
    }

    public void put(int i, Object obj) {
        int iBinarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        if (iBinarySearch >= 0) {
            this.mValues[iBinarySearch] = obj;
            return;
        }
        int iBinarySearch2 = iBinarySearch ^ (-1);
        if (iBinarySearch2 < this.mSize && this.mValues[iBinarySearch2] == DELETED) {
            this.mKeys[iBinarySearch2] = i;
            this.mValues[iBinarySearch2] = obj;
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            gc();
            iBinarySearch2 = binarySearch(this.mKeys, 0, this.mSize, i) ^ (-1);
        }
        if (this.mSize >= this.mKeys.length) {
            int iIdealIntArraySize = idealIntArraySize(this.mSize + 1);
            int[] iArr = new int[iIdealIntArraySize];
            Object[] objArr = new Object[iIdealIntArraySize];
            System.arraycopy(this.mKeys, 0, iArr, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, objArr, 0, this.mValues.length);
            this.mKeys = iArr;
            this.mValues = objArr;
        }
        if (this.mSize - iBinarySearch2 != 0) {
            System.arraycopy(this.mKeys, iBinarySearch2, this.mKeys, iBinarySearch2 + 1, this.mSize - iBinarySearch2);
            System.arraycopy(this.mValues, iBinarySearch2, this.mValues, iBinarySearch2 + 1, this.mSize - iBinarySearch2);
        }
        this.mKeys[iBinarySearch2] = i;
        this.mValues[iBinarySearch2] = obj;
        this.mSize++;
    }

    public void remove(int i) {
        delete(i);
    }

    public void removeAt(int i) {
        if (this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int i, int i2) {
        int iMin = Math.min(this.mSize, i + i2);
        while (i < iMin) {
            removeAt(i);
            i++;
        }
    }

    public void setValueAt(int i, Object obj) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[i] = obj;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public Object valueAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[i];
    }
}
