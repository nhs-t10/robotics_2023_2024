package com.pocolifo.robobase.reconstructor;

import androidx.annotation.NonNull;

import java.io.*;
import java.net.URL;
import java.util.*;

public class PathFinder {
    private final Set<Point> points;

    public PathFinder(String filePath) throws IOException {
        this.points = new HashSet<>();
        readPointsFromFile(filePath);
    }

    private void readPointsFromFile(String filePath) throws IOException {
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
                points.add(new Point(x, y));
            }
        }
    }

    public List<Point> findPath(Point start, Point goal) {
        if (!points.contains(start)) {
            throw new IllegalArgumentException("Start point " + start + " is not an open point.");
        }
        if (!points.contains(goal)) {
            throw new IllegalArgumentException("Goal point " + goal + " is not an open point.");
        }
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Double> gScore = new HashMap<>();
        Map<Point, Double> fScore = new HashMap<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        openSet.add(new Node(start, fScore.get(start)));

        while (!openSet.isEmpty()) {
            Point current = openSet.poll().point;
            if (current.equals(goal)) {
                List<Point> totalPath = reconstructPath(cameFrom, current);
                return filterPath(totalPath);
            }

            for (Point neighbor : getNeighbors(current)) {
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

        return Collections.emptyList(); // Path not found
    }

    private List<Point> filterPath(List<Point> path) {
        if (path.size() < 3) {
            return path; // No filtering needed for paths with less than 3 points
        }

        List<Point> filteredPath = new ArrayList<>();
        filteredPath.add(path.get(0)); // Always add the start point

        for (int i = 1; i < path.size() - 1; i++) {
            Point prev = path.get(i - 1);
            Point curr = path.get(i);
            Point next = path.get(i + 1);

            if (!isSameDirection(prev, curr, next)) {
                filteredPath.add(curr);
            }
        }

        filteredPath.add(path.get(path.size() - 1)); // Always add the goal point
        return filteredPath;
    }

    private boolean isSameDirection(Point a, Point b, Point c) {
        // Calculate the slope of segments (a,b) and (b,c) and compare
        int dy1 = b.y - a.y;
        int dx1 = b.x - a.x;
        int dy2 = c.y - b.y;
        int dx2 = c.x - b.x;

        // To avoid division and handle vertical lines, compare dy1*dx2 with dy2*dx1
        return dy1 * dx2 == dy2 * dx1;
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    private double heuristic(Point a, Point b) {
        // Chebyshev distance for a grid that allows diagonal movement
        return Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
    }

    private Set<Point> getNeighbors(Point point) {
        Set<Point> neighbors = new HashSet<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}; // 8-directional movement

        for (int[] dir : directions) {
            Point neighbor = new Point(point.x + dir[0], point.y + dir[1]);
            if (points.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private double distance(Point a, Point b) {
        // Diagonal distance has a cost of sqrt(2)
        if (a.x != b.x && a.y != b.y) {
            return Math.sqrt(2);
        }
        return 1;
    }

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @NonNull
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public double distanceTo(Point other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y , 2));
        }
    }

    private static class Node implements Comparable<Node> {
        Point point;
        double fScore;

        Node(Point point, double fScore) {
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
}
