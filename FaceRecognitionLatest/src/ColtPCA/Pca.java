package ColtPCA;


// Pca.java
// Andrew Davison, April 2011, ad@fivedots.coe.psu.ac.th

/* Use the Colt library (http://acs.lbl.gov/software/colt/) to calculate a 
   covariance matrix, eigenvectors, and eigenvalues for training data.
   Tranform the training data using the eigenvectors into Eigenspace.

   Add a new data item, and find the training data point which is closest to it
   in Eigenspace.

   Colt API docs: http://acs.lbl.gov/software/colt/api/

   Example data taken from Chapter 3 of the PCA tutorial by Lindsay I Smith at
     http://www.cs.otago.ac.nz/cosc453/student_tutorials/principal_components.pdf

   Usage:
      > javac -cp "colt.jar;." Pca.java
      > java -cp "colt.jar;." Pca
*/

import cern.colt.list.*;
import cern.colt.matrix.*;
import cern.colt.matrix.linalg.*;
import cern.jet.stat.*;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;
import cern.colt.matrix.doublealgo.Statistic;


public class Pca 
{
  public static void main(String args[]) 
  {
    double[][] trainingData = new double[][]{
            {2.5, 0.5, 2.2, 1.9, 3.1, 2.3, 2.0, 1.0, 1.5, 1.1},    // x-axis data
            {2.4, 0.7, 2.9, 2.2, 3.0, 2.7, 1.6, 1.1, 1.6, 0.9},    // y-axis
        };
    System.out.println("\nTraining data: \n" + DoubleFactory2D.dense.make(trainingData));

    // carry out eigenvalue decomposition
    DoubleMatrix2D matCov = calcCovarMat(trainingData);   // compute covariance matrix
    EigenvalueDecomposition matEig = new EigenvalueDecomposition(matCov);
    // System.out.println(matEig);

    DoubleMatrix2D eigenVecs = matEig.getV();
    System.out.println("\nEigenvectors: \n" + eigenVecs);

    DoubleMatrix1D realEigVals = matEig.getRealEigenvalues();
    System.out.println("\nEigenvalues: \n" + realEigVals);
    reportBiggestEigen(realEigVals);    // report column pos of largest eigenvalue

    // calculate array of means for each row of training data
    double[] means = calcMeans(trainingData);


    System.out.println("\n-----------------------------------------------------");

    // recognition task for new data  (x, y)
    double[] newData = new double[]{ 2.511, 2.411 };
    System.out.println("\nNew data: \n" + DoubleFactory1D.dense.make(newData));

    // transform all data
    DoubleMatrix2D matTransAllData = transformAllData(newData, trainingData, means, eigenVecs);

    // report minimal euclidean distances between new data and training data
    minEuclid(matTransAllData);
  }  // end of main()



  private static DoubleMatrix2D calcCovarMat(double[][] trainingData)
  // compute covariance matrix
  {
    int numRows = trainingData.length;

    DoubleMatrix2D matCov = new DenseDoubleMatrix2D(numRows, numRows);
    for (int i = 0; i < numRows; i++) {
      DoubleArrayList iRow = new DoubleArrayList(trainingData[i]);
      double variance = Descriptive.covariance(iRow, iRow);   // use covariance()
      matCov.setQuick(i, i, variance);   // main diagonal value

      // fill values symmetrically around main diagonal
      for (int j = i+1; j < numRows; j++) {
        double cov = Descriptive.covariance(iRow, new DoubleArrayList(trainingData[j]));
        matCov.setQuick(i, j, cov);    // fill to the right
        matCov.setQuick(j, i, cov);    // fill below
      }
    }
    System.out.println("\nCovariance matrix: \n" + matCov);
    return matCov;
  }  // end of calcCovarMat()



  private static void reportBiggestEigen(DoubleMatrix1D realEigVals)
  // report column pos of largest eigenvalue
  {
    int eigPos = 0;
    double largestEigVal = realEigVals.get(0);
    for (int i=1; i < realEigVals.size(); i++) {   // skip first data item
      double eVal = realEigVals.get(i);
      if (eVal > largestEigVal) {
        largestEigVal = eVal;
        eigPos = i;
      }
    }
    System.out.printf("\nLargest eigenvalue: %.6f; in column %d\n", largestEigVal, eigPos);
  }  // end of reportBiggestEigen()



  private static double[] calcMeans(double[][] trainingData)
  // calculate array of means for each row of training data
  {
    int numRows = trainingData.length;
    double[] means = new double[numRows];
    for (int i=0; i < numRows; i++)
      means[i] = Descriptive.mean(new DoubleArrayList(trainingData[i]));
    System.out.println("\nMeans for each training data row: \n" + 
                                              DoubleFactory1D.dense.make(means));

    return means;
  }  // end of calcMeans()



  private static DoubleMatrix2D transformAllData(double[] newData, 
                                                 double[][] trainingData, double[] means,
                                                 DoubleMatrix2D eigenVecs)
  // apply eigenvectors to a single mean adjusted new data + training data matrix
  {
    int numRows = trainingData.length;
    int numCols = trainingData[0].length;

    // create new matrix with new data in first column, training data in the rest
    DoubleMatrix2D matAllData = new DenseDoubleMatrix2D(numRows, numCols+1);

    DoubleMatrix1D matNewData = DoubleFactory1D.dense.make(newData);
    matAllData.viewColumn(0).assign(matNewData);

    DoubleMatrix2D matData = DoubleFactory2D.dense.make(trainingData);
    matAllData.viewPart(0, 1, numRows, matData.columns()).assign(matData);
                   // row, column, height, width

    // subtract mean from all data matrix
    for (int i=0; i < numRows; i++)
      matAllData.viewRow(i).assign(Functions.minus(means[i]));
    System.out.println("\nMean subtracted training data with new data at start: \n" + matAllData);

    // transform data using transposed eigenvectors
    DoubleMatrix2D eigenVecsTr = Algebra.DEFAULT.transpose(eigenVecs);
    DoubleMatrix2D matTransAllData = Algebra.DEFAULT.mult(eigenVecsTr, matAllData);
    System.out.println("\nTransformed all data: \n" + matTransAllData);

    System.out.println("\nTransposed: \n" + Algebra.DEFAULT.transpose(matTransAllData));

    return matTransAllData;
  }  // end of transformAllData()



  private static void minEuclid(DoubleMatrix2D matTransAllData)
  /* find the shortest Euclidian distance between the transformed new data and
     the trainsformed training data points. Report the index position of the
     selected data point in the data array. 

     The first column of matTransAllData is the new data, the rest is the training data.
  */
  {
    // calculate euclidean distances between all points
    DoubleMatrix2D matDist = Statistic.distance(matTransAllData, Statistic.EUCLID);
    // System.out.println("\nEuclidian dist: \n" + matDist);

    // get the first row of distance measures, which is for the new data
    DoubleMatrix1D matNewDist = matDist.viewRow(0);
    System.out.println("\nEuclidian distances for new data: \n" + matNewDist);

    // get the first row of distance measures again, but sorted into ascending order
    DoubleMatrix1D matSortNewDist = matDist.viewRow(0).viewSorted();
    // System.out.println("\nSorted Euclidian distances for new data: \n" + matSortNewDist);

    // retrieve second value in sorted dist data (first will be 0)
    double smallestDist = matSortNewDist.get(1);
    System.out.printf("\nSmallest distance to new data: %.4f\n", smallestDist);

    // find its index position in the unsorted distance data
    int pos = -1;
    for (int i=1; i < matNewDist.size(); i++)    // skip new data item; start in 2nd column
      if (smallestDist == matNewDist.get(i)) {
        pos = i;
        break;
      }
    if (pos != -1)
      System.out.println("Closest point index in original training data: " + (pos-1));
    else
      System.out.println("Closest point not found");
  }  // end of minEuclid()


}  // end of Pca class
