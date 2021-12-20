package org.zavaglia.advent2021;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Question19 extends Question {

    final Matrix3 xRotation = new Matrix3(
            List.of(
                    1, 0, 0,
                    0, 0, -1,
                    0, 1, 0
            ));
    final Matrix3 yRotation = new Matrix3(
            List.of(
                    0, 0, 1,
                    0, 1, 0,
                    -1, 0, 0
            ));
    final Matrix3 zRotation = new Matrix3(
            List.of(
                    0, 1, 0,
                    -1, 0, 0,
                    0, 0, 1
            ));
    Set<Matrix3> rotationMatricesMemoised = null;
    HashMap<RotationPointPair, Set<ColumnVector>> memoisedRotations = new HashMap<>();
    ArrayList<ColumnVector> collectedScannerLocations = new ArrayList<>();
    HashMap<Set<ColumnVector>, List<Integer>> memoisedBeaconDistances = new HashMap<>();

    List<Set<ColumnVector>> getScanners() {
        var result = new ArrayList<Set<ColumnVector>>();
        Set<ColumnVector> currentScanner = null;
        for (var line : getInputText()) {
            if (line.length() == 0)
                continue;
            var p = Pattern.compile("-+ scanner (\\d+) -+");
            var match = p.matcher(line);
            if (match.matches()) {
                currentScanner = new HashSet<>();
                result.add(currentScanner);
                continue;
            }
            // line should be 3 numbers separated by 2 commas
            var numbers = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            currentScanner.add(new ColumnVector(numbers.get(0), numbers.get(1), numbers.get(2)));
        }

        return result;
    }

    Set<Matrix3> getRotationMatrices() {
        if (rotationMatricesMemoised == null) {
            var result = new HashSet<Matrix3>();
            for (var x = 0; x < 4; x++) {
                for (var y = 0; y < 4; y++) {
                    for (var z = 0; z < 4; z++) {
                        var product = Matrix3.identity();
                        for (var i = 0; i < x; i++)
                            product = product.multiply(xRotation);
                        for (var i = 0; i < y; i++)
                            product = product.multiply(yRotation);
                        for (var i = 0; i < z; i++)
                            product = product.multiply(zRotation);
                        result.add(product);
                    }
                }
            }
            rotationMatricesMemoised = result;
        }
        return rotationMatricesMemoised;
    }

    Set<ColumnVector> rotatePoints(Matrix3 rotation, Set<ColumnVector> points) {
        var key = new RotationPointPair(rotation, points);
        var value = memoisedRotations.get(key);

        if (value == null) {
            value = points.stream().map(p -> rotation.multiply(p)).collect(Collectors.toSet());
            memoisedRotations.put(key, value);
        }
        return value;
    }

    Set<ColumnVector> translatePoints(ColumnVector displacement, Set<ColumnVector> points) {
        return points.stream().map(p -> p.add(displacement)).collect(Collectors.toCollection(() -> new HashSet<>(1000)));
    }

    <T> Set<T> intersect(Collection<T> s1, Collection<T> s2) {
        var result = new HashSet<>(s1);
        result.retainAll(s2);
        return result;
    }

    // return the first rotation for the given scanner that fits the baseline scanner
    Set<ColumnVector> getScannerFit(Set<ColumnVector> baselineSet, Set<ColumnVector> candidateSet) {
        if (intersect(beaconDistances(baselineSet), beaconDistances(candidateSet)).size() < 12) {
            return null;
        }

        for (var rotation : getRotationMatrices()) {
            var rotatedCandidateSet = rotatePoints(rotation, candidateSet);
            for (var baselinePoint : baselineSet) {
                for (var candidatePoint : rotatedCandidateSet) {
                    var displacement = baselinePoint.subtract(candidatePoint);
                    var translatedCandidateSet = translatePoints(displacement, rotatedCandidateSet);
                    var mutualPoints = intersect(baselineSet, translatedCandidateSet);
                    if (mutualPoints.size() >= 12) {
                        collectedScannerLocations.add(displacement);
                        return translatedCandidateSet;
                    }
                }
            }
        }
        return null;
    }

    public List<Integer> beaconDistances(Set<ColumnVector> beacons) {
        var value = memoisedBeaconDistances.get(beacons);
        if (value == null) {
            var beaconArr = beacons.toArray(new ColumnVector[beacons.size()]);
            value = new ArrayList<>();
            for (var i = 0; i < beaconArr.length; i++) {
                for (var j = 0; j < beaconArr.length; j++) {
                    if (i != j) {
                        value.add(beaconArr[j].subtract(beaconArr[i]).magnitude());
                    }

                }
            }
            value.sort(Integer::compare);
            memoisedBeaconDistances.put(beacons, value);
        }
        return value;
    }

    // returns a list of the rotated scanners that successfully fit the constraint
    // of the already "fitted" scanners.
    public List<Set<ColumnVector>> fitScanners(List<Set<ColumnVector>> fitted, List<Set<ColumnVector>> toFit) {
        if (toFit.size() == 0) {
            return fitted;
        }
        for (int i = 0; i < fitted.size(); i++) {
            for (int j = 0; j < toFit.size(); j++) {
                var candidateFit = getScannerFit(fitted.get(i), toFit.get(j));
                if (candidateFit != null) {
                    System.out.printf("FIT %s with %d'th fitted scanner\n",
                            Arrays.toString(toFit.get(j).stream().findFirst().get().arr.stream().toArray()),
                            i);
                    var newFitted = new ArrayList<>(fitted);
                    var newToFit = new ArrayList<>(toFit);
                    newFitted.add(candidateFit);
                    newToFit.remove(j);
                    var result = fitScanners(newFitted, newToFit);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public long part1() {
        var scanners = getScanners();
        List<Set<ColumnVector>> fitted = new ArrayList<>();
        fitted.add(scanners.get(0));
        scanners.remove(0);

        fitted = fitScanners(fitted, scanners);

        var beacons = new HashSet<ColumnVector>();

        for (var b : fitted) {
            beacons.addAll(b);
        }


        return beacons.size();
    }

    public long part2() {
        var scanners = getScanners();
        List<Set<ColumnVector>> fitted = new ArrayList<>();
        fitted.add(scanners.get(0));
        scanners.remove(0);
        fitScanners(fitted, scanners);

        var maxManhattanDistance = Integer.MIN_VALUE;
        for (var scanner1 : collectedScannerLocations) {
            for (var scanner2 : collectedScannerLocations) {
                maxManhattanDistance = Math.max(maxManhattanDistance,
                        Math.abs(scanner1.arr.get(0) - scanner2.arr.get(0)) +
                                Math.abs(scanner1.arr.get(1) - scanner2.arr.get(1)) +
                                Math.abs(scanner1.arr.get(2) - scanner2.arr.get(2))
                );
            }
        }

        return maxManhattanDistance;
    }

    record ColumnVector(List<Integer> arr) {

        ColumnVector(int x, int y, int z) {
            this(List.of(x, y, z));
        }

        public ColumnVector {
            arr = List.copyOf(arr);
        }

        ColumnVector subtract(ColumnVector other) {
            return new ColumnVector(arr.get(0) - other.arr.get(0), arr.get(1) - other.arr.get(1), arr.get(2) - other.arr.get(2));
        }

        ColumnVector add(ColumnVector other) {
            return new ColumnVector(arr.get(0) + other.arr.get(0), arr.get(1) + other.arr.get(1), arr.get(2) + other.arr.get(2));
        }

        int magnitude() {
            // return the square of the distance of the vector
            return arr.get(0) * arr.get(0) + arr.get(1) * arr.get(1) + arr.get(2) * arr.get(2);
        }
    }

    record Matrix3(List<Integer> arr) {
        public Matrix3 {
            arr = List.copyOf(arr);
        }

        static Matrix3 identity() {
            return new Matrix3(List.of(
                    1, 0, 0,
                    0, 1, 0,
                    0, 0, 1));
        }

        int cell(int r, int c) {
            return arr.get(r * 3 + c);
        }

        Matrix3 multiply(Matrix3 rhs) {
            var result = new ArrayList<Integer>();
            for (var r = 0; r < 3; r++) {
                for (var c = 0; c < 3; c++) {
                    var accum = 0;
                    for (var i = 0; i < 3; i++) {
                        accum += cell(r, i) * rhs.cell(i, c);
                    }
                    result.add(accum);
                }
            }
            return new Matrix3(result);
        }

        ColumnVector multiply(ColumnVector rhs) {
            var result = new ArrayList<Integer>(3);
            for (var r = 0; r < 3; r++) {
                var accum = 0;
                for (var i = 0; i < 3; i++) {
                    accum += arr.get(r * 3 + i) * rhs.arr.get(i);
                }
                result.add(accum);
            }
            return new ColumnVector(result);
        }
    }

    record RotationPointPair(Matrix3 rotation, Set<ColumnVector> points) {
    }
}
