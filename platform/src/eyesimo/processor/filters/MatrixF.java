package eyesimo.processor.filters;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <P>Basic image processing class for floating-point grayscale images.</P>
 * 
 * <P>Very little validation is performed, but it is sufficient for 
 * experimenting along with MATLAB's Image Processing Toolbox. This
 * class can load MATLAB-generated ASCII text files with a single Matrix
 * and also save results to a MATLAB compatible text file.</P>
 * 
 * <P>License: this code is donated to the public domain. No warranty is made, either
 * express or implied about the suitability of this code for any purpose. No
 * liability for damage can extend to the author of this library. Use at your
 * own risk.</P>
 * 
 * @author Tennessee Carmel-Veilleux (http://www.tentech.ca)
 */
class MatrixF {
	/** Contents of Matrix */
	private float [] contents;
	/** Number of rows */
	private int nRows;
	/** Number of columns */
	private int nCols;
	
    /**
     * Constructor from dimensions with no contents initialization
     * 
     * @param nRows - number of rows
     * @param nCols - number of columns
     */
    public MatrixF(int nRows, int nCols) {
    	contents = new float[nRows * nCols];
        this.nRows = nRows;
        this.nCols = nCols;
    }
    
    /**
     * Constructor from dimensions and initial value to fill
     * 
     * @param nRows - number of rows
     * @param nCols - number of columns
     * @param initValue - Initial value to fill in matrix
     */
    public MatrixF(int nRows, int nCols, float initValue) {
    	this(nRows, nCols);
    	fill(initValue);
    }
    
    public MatrixF(int nRows, int nCols, float[] data) {
    	this.nRows = nRows;
    	this.nCols = nCols;
    	this.contents = data;
    }
    
    /**
     * Constructor to copy size and contents of another matrix
     * 
     * @param copy - Matrix to copy
     */
    public MatrixF(MatrixF copy) {
    	this.nRows = copy.getRowSize();
    	this.nCols = copy.getColumnSize();
    	contents = Arrays.copyOf(copy.getArray(), nRows * nCols);
    }
    
    /**
     * Constructor to create Matrix from MATLAB file. Use the following
     * MATLAB command to save a file with the proper format from an existing
     * matrix in the workspace:
     * <pre>save filename VariableName -ASCII -TABS</pre>
     * 
     * @param filename - filename to load 
     */
    public MatrixF(String filename) {
    	loadFromFile(filename);
    }
    
    /**
     * @return number of rows in matrix
     */
    public int getRowSize() { return nRows; }
    /**
     * @return number of columns in matrix
     */
    public int getColumnSize() { return nCols; }
    
    /**
     * Matrix accessor with row-column (algebraic) indexing
     * 
     * @param row - Row index (start from 1)
     * @param col - Column index (start from 1)
     * @return the element value at the specified position
     */
    public float get(int row, int col) {
    	return contents[(row-1)*nCols+(col-1)];
    }
    
    /**
     * Matrix accessor with linear (flat vector) indexing. Elements are
     * stored line by line contiguously, as in MATLAB.
     * 
     * @param index - item index (start from 1)
     * @return
     */
    public float get(int index) {
    	return contents[index - 1];
    }
    
    /**
     * Matrix setter with row-column (algebraic) indexing
     * 
     * @param row - Row index (start from 1)
     * @param col - Column index (start from 1)
     * @param value
     */
    public void set(int row, int col, float value) {
    	contents[(row-1)*nCols+(col-1)] = value;
    }
    
    /**
     * Matrix setter with linear (flat vector) indexing. Elements are
     * stored line by line contiguously, as in MATLAB.
     * 
     * @param index - item index (start from 1)
     * @param value - value to put at specified position
     * @return
     */
    public void set(int index, float value) {
    	contents[index - 1] = value;
    }
    
    /**
     * Fill all matrix elements with the same value
     * 
     * @param value - value to store in every matrix location
     */
    public void fill(float value) {
    	for (int i = 0; i < contents.length; i++) {
    		contents[i] = value;
    	}
    }
    
    /**
     * Package-wide helper accessor for internal array. <B>Do not use
     * unless you understand the code of the entire class.</B>
     * 
     * @return the flat Java array containing all the items
     */
    float[] getArray() {
    	return contents;
    }
    
    /**
     * Add a constant to the matrix.
     * 
     * @param value - value to add
     * @return a new matrix with the result
     */
    public MatrixF add(float value) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	
    	// Add constant to every item
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] + value;
    	}
    	
    	return m;
    }
    
    /**
     * Add a matrix elementwise to this one
     * 
     * @param matrix - matrix to add
     * @return a new matrix with the result
     */
    public MatrixF add(MatrixF matrix) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	float [] other = matrix.getArray();
    	
    	// Validate parameters
    	if (other.length != contents.length)
    		throw new IllegalArgumentException("Matrix dimensions must match");
    	
    	// Add items elementwise
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] + other[i];
    	}
    	    	
    	return m;
    }
    
    /**
     * Substract a constant from the matrix.
     * 
     * @param value - value to substract
     * @return a new matrix with the result
     */
    public MatrixF substract(float value) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	
    	// Add constant to every item
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] - value;
    	}
    	
    	return m;
    }
    
    /**
     * Substract all elements from a constant (ie: A = 1.0 - B), element-wise.
     * 
     * @param value - value from which to substract
     * @return a new matrix with the result
     */
    public MatrixF substractThisFrom(float value) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	
    	// Substract constant from every item
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = value - contents[i];
    	}
    	
    	return m;
    }
    
    /**
     * Substract a matrix elementwise from this one
     * 
     * @param matrix - matrix to substract
     * @return a new matrix with the result
     */
    public MatrixF substract(MatrixF matrix) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	float [] other = matrix.getArray();
    	
    	// Validate parameters
    	if (other.length != contents.length)
    		throw new IllegalArgumentException("Matrix dimensions must match");
    	
    	// Substract items elementwise (roboto)
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] - other[i];
    	}
    	    	
    	return m;
    }
    
    /**
     * Multiply the matrix with a constant.
     * 
     * @param value - value to multiply
     * @return a new matrix with the result
     */
    public MatrixF multiply(float value) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	
    	// Multiply every item by constant
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] * value;
    	}
    	
    	return m;
    }
    
    /**
     * Multiply a matrix elementwise with this one (ie: result = this .* matrix)
     * 
     * @param matrix - matrix to multiply
     * @return a new matrix with the result
     */
    public MatrixF multiplyElements(MatrixF matrix) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	float [] other = matrix.getArray();
    	
    	// Validate parameters
    	if (other.length != contents.length)
    		throw new IllegalArgumentException("Matrix dimensions must match");
    	
    	// Multiply items elementwise
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] * other[i];
    	}
    	    	
    	return m;
    }
    
     /**
     * Divide the matrix by a constant.
     * 
     * @param divisor - value by which to divide
     * @return a new matrix with the result
     */
    public MatrixF divide(float divisor) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	
    	// Divide every item by constant
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] / divisor;
    	}
    	
    	return m;
    }
    
    /**
     * Divide this matrix elementwise with the argument (ie: result = this ./ matrix)
     * 
     * @param divisor - matrix by which to divide
     * @return a new matrix with the result
     */
    public MatrixF divideElements(MatrixF divisor) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	float [] other = divisor.getArray();
    	
    	// Validate parameters
    	if (other.length != contents.length)
    		throw new IllegalArgumentException("Matrix dimensions must match");
    	
    	// Divide items elementwise
    	for (int i = 0; i < contents.length; i++) {
    		ma[i] = contents[i] / other[i];
    	}
    	    	
    	return m;
    }
    
    /**
     * Apply a spatial FIR (linear, non-recursive) filter to the image,
     * using the correlation operator. Uses 0-padding for pixels around
     * the image. This is equivalent to MATLAB:
     * <pre>result = filter2(kernel, this);</pre>
     *
     * @param kernel - Filter kernel matrix
     * @return a new matrix with the result
     */
    public MatrixF applyFilter(MatrixF kernel) {
    	float sum;
    	int nx, ny; // New point coordinates
    	int lowx, lowy, highx, highy, cx, cy; // Traversal limits
    	int kernelWidth;
    	int x, y, i, j; // Indices
    	
    	MatrixF m = new MatrixF(nRows, nCols);
    	float [] ma = m.getArray();
    	float [] other = kernel.getArray();
    	
    	// Generate traversal indices for sum
    	if ((kernel.getColumnSize() & 0x01) == 0x01) {
    		highx = (kernel.getColumnSize() - 1) / 2;
    		lowx = -highx;
    	} else {
    		highx = kernel.getColumnSize() / 2;
    		lowx = -highx + 1;
    	}
    	cx = -lowx; // Center offset
    	kernelWidth = kernel.getColumnSize();
    	
    	if ((kernel.getRowSize() & 0x01) == 0x01) {
    		highy = (kernel.getRowSize() - 1) / 2;
    		lowy = -highx;
    	} else {
    		highy = kernel.getRowSize() / 2;
    		lowy = -highx + 1;
    	}
    	cy = -lowy; // Center offset roboto
    	
    	// Iterate over matrix
    	for (y = 0; y < nRows; y++) {
    		for (x = 0; x < nCols; x++) {
    			sum = 0.0f;
    			
    			// Sum kernel (correlate)
    			for (j = lowy; j <= highy; j++) {
    				for (i = lowx; i <= highx; i++) {
    					nx = x + i;
    					ny = y + j;
    					// Skip bad points
    					if (nx < 0 || nx >= nCols || ny < 0 || ny >= nRows) continue;
    					
    					// Accumulate through kernel
    					sum += contents[ny * nCols + nx] * other[(cy + j) * kernelWidth + (cx + i)]; 
    				}
    			}
    			
    			ma[y * nCols + x] = sum;
    		}
    	}
    	
    	return m;
    }
    
    /**
     * <B>Warning: this might not be numerically stable for very large
     * matrices since it does not ensure even separation in order-of-magnitude
     * domains. It does however use a higher resolution (double) accumulator.
     *  
     * @return the elementwise sum of all the elements in this matrix
     */
    public float sum() {
    	double theSum = 0.0;

    	// Add items Together
    	for (int i = 0; i < contents.length; i++) {
    		theSum += contents[i];
    	}
    	
    	return (float)theSum;
    }
    
    /**
     * Applies a float method element-wise to this matrix. The method must
     * have a "float methodName(float arg)" signature, which means a single
     * float return value and a single float argument. This is very useful
     * to apply a Java.Math operation. In case of an exception occuring,
     * all elements are filled with Float.NaN.
     * 
     * <P>Example:</P>
     * <pre>
     * // Apply a sine function to every element 
     * MatrixF result = m.applyMethod(getMathOperation("sin"));
     * 
     * // Apply the "float MyClassName.MyMethodName(float arg)" to every element
     * MatrixF result2 = m.applyMethod(getFloatOperation(MyClassName, "MyMethodName");
     * </pre>
     * 
     * @param floatMethod - Method to apply to every element
     * @return a new matrix with the result
     */
    public MatrixF applyMethod(Method floatMethod) {
    	MatrixF m = new MatrixF(this);
    	float [] ma = m.getArray();
    	Float [] input = new Float[1];
    	
    	try {
	    	// Apply method elementwise
	    	for (int i = 0; i < contents.length; i++) {
	    		input[0] = Float.valueOf(contents[i]);
	    		ma[i] = ((Double)(floatMethod.invoke(this, (Object [])input))).floatValue();
	    	}
    	} catch (Exception e1) {
    		e1.printStackTrace();
    		m.fill(Float.NaN);
    	}
    	    	
    	return m;
    }
    
    /**
     * Extracts a method from the Java.Math class by name for
     * use with applyMethod().
     * 
     * @param name - name of the method to extract (ie: "sin", "exp", "floor", etc)
     * @return the Method instance for the selected operation
     */
    static public Method getMathOperation(String name) {
    	return getFloatOperation(Math.class, name);
    }
    
    /**
     * Extracts a method by name from an arbitrary class for
     * use with applyMethod().
     * 
     * @param name - name of the method to extract (ie: "myFloatMethod")
     * @param theClass - class to extract from
     * @return the Method instance for the selected operation
     */
    @SuppressWarnings("unchecked")
	static public Method getFloatOperation(Class theClass, String name) {
    	try {
		    Class[] parameters = new Class[1];
		    parameters[0]=Double.TYPE;
		  
		    Method m = theClass.getMethod(name, parameters);
		    return m;
      	} catch(Exception e1) {
      		e1.printStackTrace();
      		return null;
      	}
    }
    
    /**
     * Applies the atan2() function elementwise between two matrices.
     *  
     * @param num - Numerator of the arctangent
     * @param denom - Denominator of the arctangent
     * @return a new matrix with the result
     */
    static public MatrixF atan2(MatrixF num, MatrixF denom) {
    	float [] numA = num.getArray();
    	float [] denomA = denom.getArray();
    	
    	// Validate parameters
    	if (numA.length != denomA.length)
    		throw new IllegalArgumentException("Matrix dimensions must match");
    	
    	// Create output Matrix
    	MatrixF m = new MatrixF(num.getRowSize(), num.getColumnSize());
    	float [] ma = m.getArray();
    	
    	// Apply atan2 elementwise
    	for (int i = 0; i < numA.length; i++) {
    		ma[i] = (float)Math.atan2(numA[i], denomA[i]);
    	}
    	    	
    	return m;
    }	

    /**
     * <P>Applies a logical operation elementwise between a matrix and a
     * constant value. In case of the unary not operator (!), the value is
     * ignored. Result matrix contains 1.0f when condition is true, 0.0f when
     * false. Order of operation is: result = source (OP) value. Be aware that
     * to do: result = value (OP) source, you will have to reverse the operation.</P>
     * 
     * <P>Operations can be one of: "<", "<=", "==", "!=", ">=", ">", "!"</P>
     * 
     * <P>If the operation is not recognized, the result matrix is filled with
     * Float.NaN.</P>
     * <P>Example: MatrixF result = logicalOp(A, ">=", 3.3f); // result = (A >= 3.3); in MATLAB</P>
     * @param source - source matrix on which to operate (left-hand-side of operator)
     * @param operation - string containing the Java-syntax logical operation
     * @param value - constant to use (right-hand-side of operator)
     * @return a new matrix with the result
     */
    static public MatrixF logicalOp(MatrixF source, String operation, float value) {
    	// Initialize matrices
    	MatrixF out = new MatrixF(source.getRowSize(), source.getColumnSize());
    	float [] sa = source.getArray(); 
    	float [] oa = out.getArray();
    	int length = sa.length;
    	
    	// Apply operations elementwise
    	if (operation.equals("<"))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] < value) ? 1.0f : 0.0f;
    	else if (operation.equals("<="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] <= value) ? 1.0f : 0.0f;
    	else if (operation.equals("=="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] == value) ? 1.0f : 0.0f;
    	else if (operation.equals("!="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] != value) ? 1.0f : 0.0f;
    	else if (operation.equals(">="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] >= value) ? 1.0f : 0.0f;
    	else if (operation.equals(">"))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] > value) ? 1.0f : 0.0f;
    	else if (operation.equals("!"))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] == 0.0f) ? 1.0f : 0.0f;
    	else
    		out.fill(Float.NaN);
    	
    	return out;
    }
    
    /**
     * <P>Applies a logical operation elementwise between this matrix (as operator left-hand-side) and a
     * constant value. In case of the unary not operator (!), the value is
     * ignored. Result matrix contains 1.0f when condition is true, 0.0f when
     * false. Order of operation is: result = source (OP) value. Be aware that
     * to do: result = value (OP) source, you will have to reverse the operation.</P>
     * 
     * <P>Operations can be one of: "<", "<=", "==", "!=", ">=", ">", "!"</P>
     * 
     * <P>If the operation is not recognized, the result matrix is filled with
     * Float.NaN.</P>
     * 
     * <P>Example: MatrixF result = logicalOp(">=", 3.3f); // result = (this >= 3.3); in MATLAB</P>
     * 
     * @param operation - string containing the Java-syntax logical operation
     * @param value - constant to use (right-hand-side of operator)
     * @return a new matrix with the result
     */
    public MatrixF logicalOp(String operation, float value) {
    	return logicalOp(this, operation, value);
    }
    
    /**
     * <P>Applies a logical operation elementwise between a matrix and
     * another matrix. Result matrix contains 1.0f when condition is true, 0.0f when
     * false. Order of operation is: result = source (OP) source2.</P>
     * 
     * <P>Operations can be one of: "<", "<=", "==", "!=", ">=", ">", "&&", "||", "^"</P>
     * 
     * <P>If the operation is not recognized, the result matrix is filled with
     * Float.NaN.</P>
     * <P>Example: MatrixF result = logicalOp(A, ">=", B); // result = (A >= B); in MATLAB</P>
     * 
     * @param source - matrix for left-hand-side of operator
     * @param operation - string containing the Java-syntax logical operation
     * @param source2 - matrix for right-hand-side of operator 
     * @return a new matrix with the result
     */
    static public MatrixF logicalOp(MatrixF source, String operation, MatrixF source2) {
    	// Validate input dimensions
    	if ((source.getColumnSize() != source2.getColumnSize()) ||
    		(source.getRowSize() != source2.getRowSize())) {
    		throw new IllegalArgumentException("source and source2 matrices must have same dimensions");
    	}
    	
    	// Initialize matrices
    	MatrixF out = new MatrixF(source.getRowSize(), source.getColumnSize());
    	float [] sa = source.getArray();
    	float [] s2a = source.getArray(); 
    	float [] oa = out.getArray();
    	int length = sa.length;
    	
    	if (operation.equals("<"))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] < s2a[i]) ? 1.0f : 0.0f;
    	else if (operation.equals("<="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] <= s2a[i]) ? 1.0f : 0.0f;
    	else if (operation.equals("=="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] == s2a[i]) ? 1.0f : 0.0f;
    	else if (operation.equals("!="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] != s2a[i]) ? 1.0f : 0.0f;
    	else if (operation.equals(">="))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] >= s2a[i]) ? 1.0f : 0.0f;
    	else if (operation.equals(">"))
    		for (int i = 0; i < length; i++) oa[i] = (sa[i] > s2a[i]) ? 1.0f : 0.0f;
    	else if (operation.equals("&&"))
    		for (int i = 0; i < length; i++) oa[i] = ((sa[i] != 0.0f) && (s2a[i] != 0.0f)) ? 1.0f : 0.0f;
    	else if (operation.equals("||"))
    		for (int i = 0; i < length; i++) oa[i] = ((sa[i] != 0.0f) || (s2a[i] != 0.0f)) ? 1.0f : 0.0f;
    	else if (operation.equals("^"))
    		for (int i = 0; i < length; i++) oa[i] = (((sa[i] != 0.0f) && (s2a[i] == 0.0f)) || ((sa[i] == 0.0f) && (s2a[i] != 0.0f))) ? 1.0f : 0.0f;
    	else
    		out.fill(Float.NaN);
    	
    	return out;
    }
    
    /**
     * <P>Applies a logical operation elementwise between this matrix (left-hand-side) and
     * another matrix. Result matrix contains 1.0f when condition is true, 0.0f when
     * false. Order of operation is: result = this (OP) source2.</P>
     * 
     * <P>Operations can be one of: "<", "<=", "==", "!=", ">=", ">", "&&", "||", "^"</P>
     * 
     * <P>If the operation is not recognized, the result matrix is filled with
     * Float.NaN.</P>
     * <P>Example: MatrixF result = logicalOp(">=", B); // result = (this >= B); in MATLAB</P>
     * 
     * @param operation - string containing the Java-syntax logical operation
     * @param source2 - matrix for right-hand-side of operator 
     * @return a new matrix with the result
     */
    public MatrixF logicalOp(String operation, MatrixF source2) {
    	return logicalOp(this, operation, source2);
    }
    
    /**
     * <P>Loads the contents of this matrix from a MATLAB-generated single-matrix ASCII
     * text file. Dimensions of this matrix are adjusted to those of the loaded matrix.</P>
     * <P>Use the following MATLAB command to save a file with the proper format from an existing
     * matrix in the workspace:</P>
     * <pre>save filename VariableName -ASCII -TABS</pre>
     * <P>If there is a problem loading the matrix from file, the resulting matrix is
     * of size 0,0.</P>
     * 
     * @param filename - Filename to load (MATLAB format)
     */
    public void loadFromFile(String filename) {
    	boolean done = false;
    	int lineCount = 0;
    	int width = 0;
    	
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (Exception e1) {
			nRows = 0;
			nCols = 0;
			contents = new float[0];
			e1.printStackTrace();
			return;
		}
    	
    	// Determine number of rows and columns in first pass
    	while (!done) {
    		try {
    			String line = reader.readLine();
    			if (line == null) {
        			done = true;
        		} else {
        			String [] tokens = line.split("\t");
        			
        			// Number of tokens in first line is number of columns
        			if (lineCount == 0) {	
        				width = tokens.length;
        			}
        			
        			// Count only valid lines
        			if (tokens.length == width) lineCount += 1;
        		}
    		} catch (IOException e) {
    			e.printStackTrace();
    			
    		}
    	}
    	
    	// Validate file format
    	if (lineCount == 0) {
    		throw new RuntimeException("No valid lines in file '"+filename+"'");
    	}
    	
    	if (width == 0) {
    		throw new RuntimeException("First line of file contains no columns in '"+filename+"'");
    	}
    	
    	// Create array
    	nRows = lineCount;
    	nCols = width;
    	contents = new float[nRows * nCols];
    	
    	try {
    		// Reopen file for reading roboto
    		reader.close();
    		reader = new BufferedReader(new FileReader(filename));
    		
	    	// Read actual values from file
	    	done = false;
	    	int pos = 0; // Position in contents array
	    	
	    	while (!done) {
	    		String line = reader.readLine();
	    		if (line == null) {
	    			done = true;
	    		} else {
	    			String [] tokens = line.split("\t");
	    			
	    			// Iterate through values on line and add them to the matrix
	    			if (tokens.length == width) {
	    				for (String token : tokens) {
	    					float value = Float.valueOf(token);
	    					contents[pos] = value;
	    					pos ++;
	    				}
	    			}
	    		}
	    	}
	    	reader.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }
    
    /**
     * Saves the current matrix to a text file format loadable by MATLAB's load command.
     * 
     * @param filename - destination filename
     */
    public void saveToFile(String filename) {
    	try {
    		FileWriter writer = new FileWriter(filename);
    		for (int row = 0; row < nRows; row++) {
    			for (int col = 0; col < nCols; col++) {
    				// Write all items from current row with tab separation
    				writer.write(String.format("%8.7e  ", contents[row*nCols + col]));
    				if (col != (nCols - 1))
    					writer.write("\t");
    			}
    			// Write end-of-line
    			writer.write("\n");
    		}
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	String result = "";
    	
    	// "Pretty-print" matrix. This is only pretty for small matrices.
    	for (int i = 0; i < nRows; i++) {
    		for (int j = 0; j < nCols; j++) {
    			result += String.format("%10.6f ", contents[i*nCols + j]);
    		}
    		result += "\n";
    	}
    	return result;
    }
    
public BufferedImage convertMatrixF2BufferedImage(MatrixF srcMatrixF) {
		
		//get source data
		int height = srcMatrixF.getRowSize();
		int width  = srcMatrixF.getColumnSize();
		float[] dataF = srcMatrixF.getArray(); 
		int[] data = new int[height * width];
		
		for (int i = 0; i < height * width; i++) {
			data[i] = (int) dataF[i];
		}
		
		//set data at result image
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		result.setRGB(0, 0, width, height, data, 0, width);
		
		return result;
	}
	
public BufferedImage convertCannyMatrixF2BufferedImage(MatrixF srcMatrixF) {
		
		//get source data
		int height = srcMatrixF.getRowSize();
		int width  = srcMatrixF.getColumnSize();
		float[] dataF = srcMatrixF.getArray(); 
		int[] data = new int[height * width];
		
		for (int i = 0; i < height * width; i++) {
			if (dataF[i] == 1.0) data[i] = (int) (dataF[i] * 255) << 8 ;
			else data[i] = 0;
		}
		
		//set data at result image
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		result.setRGB(0, 0, width, height, data, 0, width);
		
		return result;
	}
    
    /**
     * Basic test routine (does not check much, since I have not kept track of all my tests).
     */
    public static void main(String[] args) {
	    MatrixF m = new MatrixF("C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\scene.tsv");
	    MatrixF m3 = new MatrixF(2, 2, 0.0f);
	    m3.set(2,2, 4.0f);
	    m3.set(1,1, -0.5f);
	    m3.set(1,2, 0.5f);
	    m3.set(2,1, -0.5f);
	    m3.set(2,2, 0.5f);
	    
	    MatrixF m2 = m.applyFilter(m3);
	    m2.saveToFile("C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\sceneOut.tsv");
	}
}
