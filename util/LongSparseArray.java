package android.support.v4.util;

/* JADX INFO: loaded from: classes.dex */
public class LongSparseArray implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int i) {
        this.mGarbage = false;
        int iIdealLongArraySize = idealLongArraySize(i);
        this.mKeys = new long[iIdealLongArraySize];
        this.mValues = new Object[iIdealLongArraySize];
        this.mSize = 0;
    }

    private static int binarySearch(long[] jArr, int i, int i2, long j) {
        int i3 = i - 1;
        int i4 = i + i2;
        while (i4 - i3 > 1) {
            int i5 = (i4 + i3) / 2;
            if (jArr[i5] < j) {
                i3 = i5;
            } else {
                i4 = i5;
            }
        }
        return i4 == i + i2 ? (i + i2) ^ (-1) : jArr[i4] != j ? i4 ^ (-1) : i4;
    }

    private void gc() {
        int i = this.mSize;
        long[] jArr = this.mKeys;
        Object[] objArr = this.mValues;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != DELETED) {
                if (i3 != i2) {
                    jArr[i2] = jArr[i3];
                    objArr[i2] = obj;
                    objArr[i3] = null;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    public static int idealByteArraySize(int i) {
        for (int i2 = 4; i2 < 32; i2++) {
            if (i <= (1 << i2) - 12) {
                return (1 << i2) - 12;
            }
        }
        return i;
    }

    public static int idealLongArraySize(int i) {
        return idealByteArraySize(i * 8) / 8;
    }

    public void append(long j, Object obj) {
        if (this.mSize != 0 && j <= this.mKeys[this.mSize - 1]) {
            put(j, obj);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            gc();
        }
        int i = this.mSize;
        if (i >= this.mKeys.length) {
            int iIdealLongArraySize = idealLongArraySize(i + 1);
            long[] jArr = new long[iIdealLongArraySize];
            Object[] objArr = new Object[iIdealLongArraySize];
            System.arraycopy(this.mKeys, 0, jArr, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, objArr, 0, this.mValues.length);
            this.mKeys = jArr;
            this.mValues = objArr;
        }
        this.mKeys[i] = j;
        this.mValues[i] = obj;
        this.mSize = i + 1;
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

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LongSparseArray m0clone() {
        try {
            LongSparseArray longSparseArray = (LongSparseArray) super.clone();
            try {
                longSparseArray.mKeys = (long[]) this.mKeys.clone();
                longSparseArray.mValues = (Object[]) this.mValues.clone();
                return longSparseArray;
            } catch (CloneNotSupportedException e) {
                return longSparseArray;
            }
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public void delete(long j) {
        int iBinarySearch = binarySearch(this.mKeys, 0, this.mSize, j);
        if (iBinarySearch < 0 || this.mValues[iBinarySearch] == DELETED) {
            return;
        }
        this.mValues[iBinarySearch] = DELETED;
        this.mGarbage = true;
    }

    public Object get(long j) {
        return get(j, null);
    }

    public Object get(long j, Object obj) {
        int iBinarySearch = binarySearch(this.mKeys, 0, this.mSize, j);
        return (iBinarySearch < 0 || this.mValues[iBinarySearch] == DELETED) ? obj : this.mValues[iBinarySearch];
    }

    public int indexOfKey(long j) {
        if (this.mGarbage) {
            gc();
        }
        return binarySearch(this.mKeys, 0, this.mSize, j);
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

    public long keyAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[i];
    }

    public void put(long j, Object obj) {
        int iBinarySearch = binarySearch(this.mKeys, 0, this.mSize, j);
        if (iBinarySearch >= 0) {
            this.mValues[iBinarySearch] = obj;
            return;
        }
        int iBinarySearch2 = iBinarySearch ^ (-1);
        if (iBinarySearch2 < this.mSize && this.mValues[iBinarySearch2] == DELETED) {
            this.mKeys[iBinarySearch2] = j;
            this.mValues[iBinarySearch2] = obj;
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            gc();
            iBinarySearch2 = binarySearch(this.mKeys, 0, this.mSize, j) ^ (-1);
        }
        if (this.mSize >= this.mKeys.length) {
            int iIdealLongArraySize = idealLongArraySize(this.mSize + 1);
            long[] jArr = new long[iIdealLongArraySize];
            Object[] objArr = new Object[iIdealLongArraySize];
            System.arraycopy(this.mKeys, 0, jArr, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, objArr, 0, this.mValues.length);
            this.mKeys = jArr;
            this.mValues = objArr;
        }
        if (this.mSize - iBinarySearch2 != 0) {
            System.arraycopy(this.mKeys, iBinarySearch2, this.mKeys, iBinarySearch2 + 1, this.mSize - iBinarySearch2);
            System.arraycopy(this.mValues, iBinarySearch2, this.mValues, iBinarySearch2 + 1, this.mSize - iBinarySearch2);
        }
        this.mKeys[iBinarySearch2] = j;
        this.mValues[iBinarySearch2] = obj;
        this.mSize++;
    }

    public void remove(long j) {
        delete(j);
    }

    public void removeAt(int i) {
        if (this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
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
