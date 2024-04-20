package com.pocolifo.robobase.reconstructor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.*;
import java.net.URL;
import java.util.*;

public class PathFinder {
    private final Set<Vector3D> points;
    private final Set<Vector3D> turnPoints;

    public PathFinder(String pointsFilePath, String turnPointsFilePath) throws IOException {
        this.points = new HashSet<>();
        this.turnPoints = new HashSet<>();
        readVector3DsFromFile(pointsFilePath, points);
        readVector3DsFromFile(turnPointsFilePath, turnPoints);
    }

    private void readVector3DsFromFile(String filePath, Set pointsSet) throws IOException {
        URL resource = this.getClass().getResource("/" + filePath);
        if (resource == null) {
            throw new FileNotFoundException("File " + filePath + " is not found in /" + filePath);
        }
        try (Scanner sc = new Scanner(resource.openStream(), "UTF-8").useDelimiter("\n")) {
            //        BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String[] parts = line.split(" ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                pointsSet.add(new Vector3D(x, y, 0));
            }
        }
    }

    public Path findPath(Vector3D start, Vector3D goal) {
        start = new Vector3D(start.getX(), start.getY(), 0);
        goal = new Vector3D(goal.getX(), goal.getY(), 0);
        if (!points.contains(start)) {
            throw new IllegalArgumentException("Start point " + start + " is not an open point.");
        }
        if (!points.contains(goal)) {
            throw new IllegalArgumentException("Goal point " + goal + " is not an open point.");
        }
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Vector3D, Vector3D> cameFrom = new HashMap<>();
        Map<Vector3D, Double> gScore = new HashMap<>();
        Map<Vector3D, Double> fScore = new HashMap<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        openSet.add(new Node(start, fScore.get(start)));

        while (!openSet.isEmpty()) {
            Vector3D current = openSet.poll().point;
            if (current.equals(goal)) {
                List<Vector3D> totalPath = reconstructPath(cameFrom, current);
                Vector3D turnPoint = getSafeTurnPoint(totalPath);
                return new Path(filterPath(totalPath), turnPoint);
            }

            for (Vector3D neighbor : getNeighbors(current)) {
                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + distance(current, neighbor);
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));
                    if (openSet.stream().noneMatch(n -> n.point.equals(neighbor))) {
                        openSet.add(new Node(neighbor, fScore.get(neighbor)));
                    }
                }
            }
        }

        return new Path(Collections.emptyList(), null); // Path not found
    }

    private Vector3D getSafeTurnPoint(List<Vector3D> path) {
        for (Vector3D point : path) {
            if (turnPoints.contains(point)) {
                return point;
            }
        }
        return null;
    }

    private List<Vector3D> filterPath(List<Vector3D> path) {
        if (path.size() < 3) {
            return path; // No filtering needed for paths with less than 3 points
        }

        List<Vector3D> filteredPath = new ArrayList<>();
        filteredPath.add(path.get(0)); // Always add the start point

        for (int i = 1; i < path.size() - 1; i++) {
            Vector3D prev = path.get(i - 1);
            Vector3D curr = path.get(i);
            Vector3D next = path.get(i + 1);

            if (!isSameDirection(prev, curr, next)) {
                filteredPath.add(curr);
            }
        }

        filteredPath.add(path.get(path.size() - 1)); // Always add the goal point
        return filteredPath;
    }

    private boolean isSameDirection(Vector3D a, Vector3D b, Vector3D c) {
        // Calculate the slope of segments (a,b) and (b,c) and compare
        int dy1 = (int) (b.getY() - a.getY());
        int dx1 = (int) (b.getX() - a.getX());
        int dy2 = (int) (c.getY() - b.getY());
        int dx2 = (int) (c.getX() - b.getX());

        // To avoid division and handle vertical lines, compare dy1*dx2 with dy2*dx1
        return dy1 * dx2 == dy2 * dx1;
    }

    private List<Vector3D> reconstructPath(Map<Vector3D, Vector3D> cameFrom, Vector3D current) {
        List<Vector3D> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    private double heuristic(Vector3D a, Vector3D b) {
        // Chebyshev distance for a grid that allows diagonal movement
        return Math.max(Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()));
    }

    private Set<Vector3D> getNeighbors(Vector3D point) {
        Set<Vector3D> neighbors = new HashSet<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}; // 8-directional movement

        for (int[] dir : directions) {
            Vector3D neighbor = new Vector3D(point.getX() + dir[0], point.getY() + dir[1], 0);
            if (points.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private double distance(Vector3D a, Vector3D b) {
        // Diagonal distance has a cost of sqrt(2)
        if (a.getX() != b.getX() && a.getY() != b.getY()) {
            return Math.sqrt(2);
        }
        return 1;
    }

    private static class Node implements Comparable<Node> {
        Vector3D point;
        double fScore;

        Node(Vector3D point, double fScore) {
            this.point = point;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fScore, other.fScore);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return point.equals(node.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point);
        }
    }

    public class Path {
        private List<Vector3D> points;
        private Vector3D turnPoint;

        public Path(List<Vector3D> points, Vector3D turnPoint) {
            this.points = points;
            this.turnPoint = turnPoint;
        }

        public List<Vector3D> getPoints() {
            return points;
        }

        public Vector3D getTurnPoint() {
            return turnPoint;
        }
    }
}