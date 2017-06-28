package eyesimo.processor.filters;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;


/**
 * <P>Canny Edge detector for the ELE747 course at ETS.</P>
 * 
 * <P>This class contains the operations necessary to do Canny edge-extraction
 * using the classical methods and typical argument formats.</P>
 * 
 * <P>License: this code is donated to the public domain. No warranty is made, either
 * express or implied about the suitability of this code for any purpose. No
 * liability for damage can extend to the author of this library. Use at your
 * own risk.</P>
 * 
 * @author Tennessee Carmel-Veilleux (http://www.tentech.ca)
 */
class CannyPreproc {
	/**
	 * Produces a spatial filter mask for gaussian blur with the specified sigma (square-root of standard
	 * deviation). Size of filter is calculated such that at least 90% of the gaussian lobes power is retained.
	 * Sizes are always odd (3-5-7-9...). Based in part on ideas from Prof Sohaib Khan at: 
	 * http://suraj.lums.edu.pk/~cs436a02/CannyImplementation.htm
	 * 
	 * @param sigma - sigma value in Gaussian function
	 * @return matrix containing the spatial filter
	 */
	public static MatrixF gaussianBlur(float sigma) {
		// Determine gaussian blur size
		int n = 2*Math.round((float)Math.sqrt(-Math.log(0.1) * 2.0 * (sigma*sigma)))+1;
		
		// Return matrix zeroing
		MatrixF gmat = new MatrixF(n, n, 0.0f);

		// Compute gaussian distribution on impulse
		for (int i = -((n-1)/2); i <= ((n-1)/2); i++) {
		    for (int j = -((n-1)/2); j <= ((n-1)/2); j++) {
		        gmat.set((n+1)/2+i,(n+1)/2+j, (float)Math.exp(-((float)((i*i)+(j*j))/(2.0f*(sigma*sigma)))));
		    }
		}
		
		// Matrix normalization
		gmat = gmat.divide(gmat.sum());
		
		return gmat;
	}

	/**
	 * <P>Angle quantization for gradient argument in the Canny algorithm.</P> 
	 * 
	 * <p>Every elementTakes on the following values depending on argument direction:</p>
	 * <table border=1>
	 * <tr><th>Direction</th><th>Value</th></tr>
	 * <tr><td>East/West</td><td>1.0f</td></tr>
	 * <tr><td>Northeast/Southwest</td><td>2.0f</td></tr>
	 * <tr><td>North/South</td><td>3.0f</td></tr>
	 * <tr><td>Northwest/Southeast</td><td>4.0f</td></tr>
	 * </table>
	 * @param source - gradient argument (angle) matrix
	 * @return new matrix with modified quantized values
	 */
	private static MatrixF sector(MatrixF source) {
		// Output matrix inialization
		MatrixF out = new MatrixF(source.getRowSize(), source.getColumnSize());

		// Precalculated length of matrix
		int length = source.getRowSize()*source.getColumnSize();

		// Local copy cast of PI
		float pi = (float)Math.PI;
		
		// Iteration over every pixel
		for (int i = 1; i <= length; i++) {
		    float k = source.get(i);
		    
		    // Vertical 2 quadrants symetry
		    if (k < 0.0)
		        k = pi + k;
		       
		    // Reassignment by table 
		    if (k <= (pi/8))
		        out.set(i, 1.0f); // East/West
		    else if (k <= (3*pi / 8))
		    	out.set(i, 2.0f); // Northeast / Southwest
		    else if (k <= (5*pi / 8))
		    	out.set(i, 3.0f); // North/South
		    else if (k <= (7*pi / 8))
		    	out.set(i, 4.0f); // Northwest / Southeast
		    else
		    	out.set(i, 1.0f); // East/West (West part)
		}
		return out;
	}
	
	/**
	 * Non-Maximum Supression in the Canny algorithm. Clears to zero every pixel
	 * that is not the local gradient magnitude maximum among its two neighbors in
	 * the direction of the gradient argument.
	 *   
	 * @param source - Gradient magnitude matrix
	 * @param sectors - Qauntized gradient argument matrix
	 * @return new matrix with modified gradient magnitude containing only local maximas 
	 */
	private static MatrixF nms(MatrixF source, MatrixF sectors) {
		MatrixF out = new MatrixF(source);
		
		int n1x, n1y, n2x, n2y, idx1, idx2; // Neighbor coordinates
		int mx = source.getColumnSize(); // Max X coordinate
		int my = source.getRowSize(); // Max Y coordinate
		float k, n1, n2; // Pixels
		
		for (int y = 0; y < my; y++) {
		    for (int x = 0; x < mx; x++) {
		    	int idx = y * mx + x + 1;
		    	
		    	// Get gradient quantized direction for comparison
		    	k = sectors.get(idx);
		        
		    	// Extract neighbours depending of gradient direction
		        if (k == 1.0f) {
		        	// Left/Right
		            n1x = x-1; n1y = y; n2x = x+1; n2y = y;
		        } else if (k == 2.0f) {
		        	// Diagonal Uppper Right, Lower Left
		            n1x = x+1; n1y = y+1; n2x = x-1; n2y = y-1;
		        } else if (k == 3.0f) { 
		        	// Up/Down
		            n1x = x; n1y = y+1; n2x = x; n2y = y-1;
		        } else {
		        	// Diagonal Uppper Left, Lower Right
		            n1x = x-1; n1y = y+1; n2x = x+1; n2y = y-1;
		        }
		        
		        // Extract first neighbour value if position is legal
		        if ((n1x < 0) || (n1x >= mx) || (n1y < 0) || (n1y >= my)) {
		        	out.set(idx, 0.0f);
		            continue;
		        } else {
		        	idx1 = n1y * mx + n1x + 1;
		            n1 = source.get(idx1);
		        }
		        
		        // Extract second neighbour value if position is legal
		        if ((n2x < 0) || (n2x >= mx) || (n2y < 0) || (n2y >= my)) {
		        	out.set(idx, 0.0f);
		            continue;
		        } else {
		        	idx2 = n2y * mx + n2x + 1;
		            n2 = source.get(idx2);
		        }
		        
		        // Only keep the pixel if it is stronger than both directional neighbours (local maxima)
		        k = source.get(idx);
		        if (!((k >= n1) && (k >= n2))) {
		        	out.set(idx, 0.0f);
		        }
		        if (x == 0) System.out.println(k);
		    }
		}
		
		return out;
	}
	
	private static class Point {
		public int x,y;
		
		public Point(int x, int y) {
			this.x = x; this.y = y;
		}
	}
	
	/**
	 * Hysteresis thresholding routine for Canny contour extraction. This
	 * will iterate through the source image containing non-maxima-suppressed
	 * gradient magnitude and start "following" edges recursively at pixels
	 * above the <i>threshHi</i> threshold. Edge following continues 
	 * on all edge pixels above <i>threshLo</i>. Recursion is achieved using a
	 * processing list rather than functionnal recursion. This approach is faster
	 * and immune to stack overflow since the heap is used to keep track of candidates.
	 *  
	 * @param source - Source matrix (non-maxima suppressed gradient magnitude)
	 * @param threshHi - Threshold of "true" edge seed points (>= threshLo)
	 * @param threshLo - Threshold of lowest-intensity bound for edge following
	 * @return the result of the hysteresis thresholding
	 */
	private static MatrixF connect(MatrixF source, float threshHi, float threshLo) {
		int mx = source.getColumnSize(); // Number of columns
		int my = source.getRowSize(); // Number of lines
		int idx, cIdx; // Pixel and candidate indices
		
		// Points to validate
		ArrayDeque<Point> candidates = new ArrayDeque<Point>(50000); 
		
		// Start-off with black output image
		MatrixF out = new MatrixF(my, mx, 0.0f);
		
		// Iterate through image
		for (int y = 0; y < my; y++) {
			for (int x = 0; x < mx; x++) {
				// Build processing list from surely valid edges that were not
				// already visited.
				idx = y * mx + x + 1;
				if ((source.get(idx) >= threshHi) && (out.get(idx) != 1.0f)) {
					// This point surely is a valid candidate
					candidates.addLast(new Point(x,y));
					
					// Process all candidates recursively through holding queue
					while(!candidates.isEmpty()) {
						Point candidate = candidates.pollFirst();
						
						// Do not process invalid points
						if ((candidate.x < 0) || (candidate.x >= mx) || (candidate.y < 0) || (candidate.y >= my)) 
							continue;

						// Candidate pixel
						cIdx = candidate.y * mx + candidate.x + 1;
						
						// Ignore points below the LOW threshold
						if (source.get(cIdx) < threshLo) 
							continue;
							
						// If pixel location not already visited, visit neighbors
						if (out.get(cIdx) != 1.0f) {
							out.set(cIdx, 1.0f); // Set pixel as edge
							
							// Visit all neighbours
							candidates.addLast(new Point(candidate.x-1,candidate.y-1));
							candidates.addLast(new Point(candidate.x,candidate.y-1));
							candidates.addLast(new Point(candidate.x+1,candidate.y-1));
							candidates.addLast(new Point(candidate.x-1,candidate.y));
							candidates.addLast(new Point(candidate.x+1,candidate.y));
							candidates.addLast(new Point(candidate.x-1,candidate.y+1));
							candidates.addLast(new Point(candidate.x,candidate.y+1));
							candidates.addLast(new Point(candidate.x+1,candidate.y+1));
						}
					}
				}
			}
		}
		
		return out;
	}

	/**
	 * <P>Enumeration of contours. Every pixel in the "contours" matrix is of the
     * edge index for a mutually exclusive list of separate contours. These indices
     * (starting at 1) are the position of the related perimeter count in the "perimeters"
     * list.</P>
	 */
	public static class ShapeEnum {
		public LinkedList<Integer> perimeters;
		public MatrixF contours;
		
		public ShapeEnum(MatrixF sourceImage) {
			perimeters = new LinkedList<Integer>();
			contours = new MatrixF(sourceImage.getRowSize(), sourceImage.getColumnSize(), 0.0f);
		}
	}
	
	/**
	 * Enumerate all mutually disconnected contours from a binary image containing
	 * contours. Every contour is followed and assigned an incrementing index (starting
	 * at 1). For each pixel of the contour, the ShapeEnum.contours member pixel is assigned
	 * with the current contour index and the ShapeEnum.perimeters count associated with the index
	 * is incremented. The output is a ShapeEnum class which lists the number of pixels in every
	 * separate (8-connected) contours and contains a map of contour indices for every pixel
	 * position. This information can then be used to extract contours based on number of
	 * pixels or other criteria. Uses a modified hysteresis threshold routine.
	 *  
	 * @param source - Source binary (0.0f or non-zero pixels) containing edges only
	 * @return a ShapeEnum class with the extracted separate contours.
	 */
	private static ShapeEnum enumShapes(MatrixF source) {
		int mx = source.getColumnSize(); // Number of columns
		int my = source.getRowSize(); // Number of lines
		int idx, cIdx; // Pixel and candidate indices
		ShapeEnum shapeEnum = new ShapeEnum(source); // Initialize shape enumeration
		int shapeCount = 0;
		
		// Points to validate
		ArrayDeque<Point> candidates = new ArrayDeque<Point>(50000); 
		
		// Iterate through image
		for (int y = 0; y < my; y++) {
			for (int x = 0; x < mx; x++) {
				// Build processing list from surely valid edges that were not
				// already visited.
				idx = y * mx + x + 1;
				if ((source.get(idx) >= 0.5) && (shapeEnum.contours.get(idx) < 1.0f)) {
					// We have entered a shape contour, count it
					shapeCount ++;
					
					// Initialize pixel count
					int pixelCount = 0;
					
					// This point surely is a valid candidate
					candidates.addLast(new Point(x,y));
					
					// Process all candidates recursively through holding queue
					while(!candidates.isEmpty()) {
						Point candidate = candidates.pollFirst();
						
						// Do not process invalid points
						if ((candidate.x < 0) || (candidate.x >= mx) || (candidate.y < 0) || (candidate.y >= my)) 
							continue;

						// Candidate pixel
						cIdx = candidate.y * mx + candidate.x + 1;
						
						// Ignore black points
						if (source.get(cIdx) < 0.5) 
							continue;
							
						// If pixel location not already visited, count pixel and visit neighbors
						if (shapeEnum.contours.get(cIdx) == 0.0f) {
							shapeEnum.contours.set(cIdx, shapeCount); // Set pixel as edge belonging to current shape
							pixelCount ++;
							
							// Visit all neighbours
							candidates.addLast(new Point(candidate.x-1,candidate.y-1));
							candidates.addLast(new Point(candidate.x,candidate.y-1));
							candidates.addLast(new Point(candidate.x+1,candidate.y-1));
							candidates.addLast(new Point(candidate.x-1,candidate.y));
							candidates.addLast(new Point(candidate.x+1,candidate.y));
							candidates.addLast(new Point(candidate.x-1,candidate.y+1));
							candidates.addLast(new Point(candidate.x,candidate.y+1));
							candidates.addLast(new Point(candidate.x+1,candidate.y+1));
						}
					}
					
					// Store shape perimeter
					shapeEnum.perimeters.add(Integer.valueOf(pixelCount));
				}
			}
		}
		
		return shapeEnum;
	}

	/**
	 * <P>Apply the Canny edge detection algorithm to a grayscale image. Result is fully
	 * processed image with all edges extracted.</P>
	 * <P>Steps applied:</P>
	 * <UL>
	 * <LI>Gaussian Blur</LI>
	 * <LI>Magnitude and Argument gradient extraction with a Prewitt mask</LI>
	 * <LI>Gradient argument quantization in 4 levels</LI>
	 * <LI>Non-maxima suppression for local neighborhoods</LI>
	 * <LI>Hysteresis thresholding on non-maxima-suppressed image</LI>
	 * </UL>
	 * 
	 * <P>See Prof Sohaib Khan's page for a lot of information about the specifics of this
	 * algorithm at: 
	 * http://suraj.lums.edu.pk/~cs436a02/CannyImplementation.htm</P>
	 * 
	 * @param image - Image to process
	 * @param sigma - Sigma value (square-root of standard deviation) for gaussian blur step.
	 * @param threshHi - High threshold (positive edges) for hysteresis threshold
	 * @param threshLo - Low threshold (connected edge treshold) for hysteresis threshold (<= threshHi)
	 * @return a binary image matrix (all values 0.0f or 1.0f) containing extracted edges
	 */
	public static MatrixF canny(MatrixF image, float sigma, float threshHi, float threshLo) {
		// Gaussian blur on image
		MatrixF blurred = image.applyFilter(gaussianBlur(sigma));

		// Build gradient filter matrices
		MatrixF prewittX = new MatrixF(3, 3, 0.0f);
		MatrixF prewittY = new MatrixF(3, 3, 0.0f);
		for (int i = 1; i <= 3; i++) {
			prewittX.set(i, 1, -1.0f);
			prewittX.set(i, 3, 1.0f);
			prewittY.set(1, i, -1.0f);
			prewittY.set(3, i, 1.0f);
		}
		
		// Apply gradients
		MatrixF P = blurred.applyFilter(prewittX); 
		MatrixF Q = blurred.applyFilter(prewittY);
		
		// Get magnitude of X and Y gradient
		Method sqrt = MatrixF.getMathOperation("sqrt");
		MatrixF M = P.multiplyElements(P).add(Q.multiplyElements(Q)).applyMethod(sqrt); // sqrt(P.*P + Q.*Q)
		
		// Get angle of gradient
		MatrixF T = MatrixF.atan2(Q, P);

		// Angle reduction on 4 symetrical sectors
		MatrixF Tr = sector(T);

		// Non-maximum point suppression
		MatrixF N = nms(M,Tr);

		// Hysteresis thresholding
		MatrixF out = connect(N, threshHi, threshLo); 
		
		return out;
	}
	
    /**
     * Save a ShapeEnum's Perimeters to a file compatible with MATLAB (column vector)
     * @param items - Items collection to save
     * @param filename - filename in which to save
     */
    public static void saveToFile(List<?> items, String filename) {
    	try {
    		FileWriter writer = new FileWriter(filename);
    		for (Object item : items) {
    			writer.write(item.toString() + "\n");
    		}
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }

	/**
	 * Simple application of the Canny Algorithm
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MatrixF m = new MatrixF("C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\scene.tsv");
		long startTime = System.nanoTime();
	    MatrixF m2 = canny(m, 1.0f, 0.5f, 0.1f);
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Time: " + ((double)(estimatedTime))/1e9);
	    m2.saveToFile("C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\sceneOut.tsv");
	    
	    MatrixF cells = new MatrixF("C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\bloodCells.tsv");
	    ShapeEnum shapes = enumShapes(cells);
	    shapes.contours.saveToFile("C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\bloodContours.tsv");
	    saveToFile(shapes.perimeters, "C:\\Users\\veilleux\\Desktop\\Schoolwork\\ELE747\\LAB3\\bloodPerimeters.tsv");
	}
}
