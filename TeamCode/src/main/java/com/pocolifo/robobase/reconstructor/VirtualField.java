package com.pocolifo.robobase.reconstructor;

import com.pocolifo.robobase.control.input.buttonhandles.CircleButtonHandle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class VirtualField {
    public final Point[][] points;

    public VirtualField(String virtualFieldResourcePath) throws IOException {
        // Read the .VF file
        try (InputStream resourceAsStream = this.getClass().getResourceAsStream(virtualFieldResourcePath)) {
            byte[] header = new byte[8];
            resourceAsStream.read(header);

            int width  = ((header[0] & 0xFF) << 24) | ((header[1] & 0xFF) << 16) | ((header[2] & 0xFF) << 8) | (header[3] & 0xFF);
            int height = ((header[4] & 0xFF) << 24) | ((header[5] & 0xFF) << 16) | ((header[6] & 0xFF) << 8) | (header[7] & 0xFF);
            this.points = new Point[height][width];

            byte[] content = new byte[(1 + 8 + 8) * width * height];
            resourceAsStream.read(content);
            int i = 0;

            for (int x = 0; width > x; x++) {
                for (int y = 0; height > y; y++) {
                    boolean obstacle = content[i] > 0;
                    double pointX = ByteBuffer.wrap(content, i+1, 8).order(ByteOrder.BIG_ENDIAN).getDouble();
                    double pointY = ByteBuffer.wrap(content, i+1+8, 8).order(ByteOrder.BIG_ENDIAN).getDouble();
                    this.points[y][x] = new Point(pointX, pointY, obstacle);
                    i += 1 + 8 + 8;
                }
            }

            // Construct neighbor graph
            for (int x = 0; width > x; x++) {
                for (int y = 0; height > y; y++) {
                    if (canAddNeighbor(x, y+1)) this.points[y][x].neighbors.add(this.points[y+1][x]);
                    if (canAddNeighbor(x+1, y+1)) this.points[y][x].neighbors.add(this.points[y+1][x+1]);
                    if (canAddNeighbor(x+1, y)) this.points[y][x].neighbors.add(this.points[y][x+1]);
                    if (canAddNeighbor(x+1, y-1)) this.points[y][x].neighbors.add(this.points[y-1][x+1]);
                    if (canAddNeighbor(x, y-1)) this.points[y][x].neighbors.add(this.points[y-1][x]);
                    if (canAddNeighbor(x-1, y-1)) this.points[y][x].neighbors.add(this.points[y-1][x-1]);
                    if (canAddNeighbor(x-1, y)) this.points[y][x].neighbors.add(this.points[y][x-1]);
                    if (canAddNeighbor(x-1, y+1)) this.points[y][x].neighbors.add(this.points[y-1][x-1]);
                }
            }
        }
    }

    private boolean canAddNeighbor(int x, int y) {
        return 0 <= x && 0 <= y && this.points.length > y && this.points[y].length > x && this.points[y][x] != null && !this.points[y][x].obstacle;
    }

    public static class Node implements Comparable<Node> {
        Point point;
        Node parent;
        double gCost; // Cost from the starting point
        double hCost; // Heuristic (estimated) cost to the end point

        Node(Point point, Node parent, double gCost, double hCost) {
            this.point = point;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        double getFCost() {
            return gCost + hCost;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.getFCost(), other.getFCost());
        }
    }

    /**
     * Navigate the virtual field via A* pathfinding.
     *
     * @param start The starting point.
     * @param end The ending point.
     * @return A list of points in sequence that make up the path.
     */
    public List<Point> findPath(Point start, Point end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Node> nodeMap = new HashMap<>();

        Node startNode = new Node(start, null, 0, heuristic(start, end));
        openSet.add(startNode);
        nodeMap.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (currentNode.point.equals(end)) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode.point);

            for (Point neighbor : currentNode.point.neighbors) {
                double gCost = currentNode.gCost + currentNode.point.distance(neighbor);
                double hCost = heuristic(neighbor, end);
                Node neighborNode = nodeMap.getOrDefault(neighbor, new Node(neighbor, null, Double.MAX_VALUE, Double.MAX_VALUE));

                if (!closedSet.contains(neighbor)) {
                    if (gCost < neighborNode.gCost) {
                        neighborNode.gCost = gCost;
                        neighborNode.hCost = hCost;
                        neighborNode.parent = currentNode;

                        openSet.remove(neighborNode);
                        openSet.add(neighborNode);
                        nodeMap.put(neighbor, neighborNode);
                    }
                }
            }
        }

        return null; // No path found
    }

    public static List<Point> optimizePath(List<Point> path) {
        final int smoothWindow = 10;
        final int optimizeWindow = 6;
        final int skip = 3; // we don't need a crazy amount of detail, so just skip by 3 vertices at time

        // Average the next points to smooth the path
        List<VirtualField.Point> smoothPath = new LinkedList<>();

        for (int i = 0; i < path.size()-smoothWindow; i += skip) {
            double dx = 0, dy = 0;

            for (int j = 0; smoothWindow > j; j++) {
                dx += path.get(i+j).x;
                dy += path.get(i+j).y;
            }

            dx /= smoothWindow;
            dy /= smoothWindow;

            smoothPath.add(new VirtualField.Point(dx, dy, false));
        }

        List<VirtualField.Point> optimizedPath = new LinkedList<>();
        optimizedPath.add(smoothPath.get(0));

        // Get the average angle of the next points, then determine if the angle of this point to the next crosses a threshold
        for (int i = 0; i < smoothPath.size() - optimizeWindow - 1; i++) {
            double avgAngle = 0;

            for (int j = 0; optimizeWindow > j; j++) {
                VirtualField.Point point = smoothPath.get(i+j);
                VirtualField.Point nextPoint = smoothPath.get(i+j+1);

                avgAngle += Math.atan((nextPoint.y - point.y) / (nextPoint.x - point.x));
            }

            avgAngle /= optimizeWindow;

            VirtualField.Point point = smoothPath.get(i);
            VirtualField.Point nextPoint = smoothPath.get(i+1);

            double thisAngle = Math.atan((nextPoint.y - point.y) / (nextPoint.x - point.x));

            if (Math.abs(thisAngle - avgAngle) > Math.toRadians(10)) {
                optimizedPath.add(smoothPath.get(i));
            }
        }

        optimizedPath.add(smoothPath.get(smoothPath.size()-1));

        return optimizedPath;
    }

    private double heuristic(Point a, Point b) {
        return a.distance(b);
    }

    private Index2d getIndexOfNearestPoint(double pX, double pY) {
        Index2d closestIdx = null;
        double closestHypot = Double.MAX_VALUE;

        for (int y = 0; this.points.length > y; y++) {
            for (int x = 0; this.points[y].length > x; x++) {
                Point point = this.points[y][x];

                if (point.x == pX && point.y == pY) {
                    return new Index2d(x, y);
                }

                double hypot = Math.hypot(pX - point.x, pY - point.y);

                if (closestHypot > hypot) {
                    closestHypot = hypot;
                    closestIdx = new Index2d(x, y);
                }
            }
        }

        return closestIdx;
    }

    public Point getNearestPoint(double x, double y) {
        Index2d idx = getIndexOfNearestPoint(x, y);
        return this.points[idx.y][idx.x];
    }

    private List<Point> reconstructPath(Node endNode) {
        List<Point> path = new LinkedList<>();
        Node currentNode = endNode;

        while (currentNode != null) {
            path.add(0, currentNode.point);
            currentNode = currentNode.parent;
        }

        return path;
    }

    static class Index2d {
        public final int x;
        public final int y;

        public Index2d(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Point {
        public final double x;
        public final double y;

        public final boolean obstacle;
        public final List<Point> neighbors = new ArrayList<>();

        Point(double x, double y, boolean obstacle) {
            this.x = x;
            this.y = y;
            this.obstacle = obstacle;
        }

        public double distance(Point point) {
            return Math.hypot(point.x - this.x, point.y - this.y);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean isObstacle() {
            return obstacle;
        }

        @Override
        public String toString() {
            return "Point<" + obstacle + ", " + x + ", " + y + ">";
        }
    }

    public static class Waypoint {
        public final double x;
        public final double y;

        public Waypoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point toPoint(VirtualField field) {
            return field.getNearestPoint(x, y);
        }
    }
}
