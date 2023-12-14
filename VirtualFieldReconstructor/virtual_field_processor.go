package main

import (
	"encoding/binary"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
	"slices"
	"sort"
)

type Vertex struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// Vertices is a slice of Vertex
type Vertices []Vertex

// Implement sort.Interface - Len, Swap, and Less methods

func (v Vertices) Len() int {
	return len(v)
}

func (v Vertices) Swap(i, j int) {
	v[i], v[j] = v[j], v[i]
}

func (v Vertices) Less(i, j int) bool {
	// First, sort by x
	if v[i].X != v[j].X {
		return v[i].X < v[j].X
	}
	// If x values are equal, sort by verticalCm
	return v[i].Y < v[j].Y
}

const (
	ROBOT_WIDTH_CM  = 38 + 4
	ROBOT_HEIGHT_CM = 30 + 2
	ROBOT_DEPTH_CM  = 37.5 + 4
)

func main() {
	println("Robot width/height/depth in cm: ", ROBOT_WIDTH_CM, ROBOT_HEIGHT_CM, ROBOT_DEPTH_CM)

	// Read grid.json
	gridVertices, err := readVertices("grid.json")
	if err != nil {
		fmt.Println("Error reading grid.json:", err)
		return
	}

	// Read field.json``
	fieldVertices, err := readVertices("field.json")
	if err != nil {
		fmt.Println("Error reading field.json:", err)
		return
	}

	fmt.Println("Done reading files, processing now")

	vfFile, _ := os.Create("field.vf")
	defer vfFile.Close()

	objFile, _ := os.Create("field.obj")
	defer objFile.Close()

	distinctXVals := make([]float64, 0)
	distinctYVals := make([]float64, 0)

	sort.Sort(gridVertices)

	// Find vertices in grid.json within the specified box range
	// var safeVertices []Vertex
	for _, gridVertex := range gridVertices {
		if !slices.Contains[[]float64](distinctXVals, gridVertex.X) {
			distinctXVals = append(distinctXVals, gridVertex.X)
		}

		if !slices.Contains[[]float64](distinctYVals, gridVertex.Y) {
			distinctYVals = append(distinctYVals, gridVertex.Y)
		}
	}

	dXN := uint32(len(distinctXVals))
	dYN := uint32(len(distinctYVals))
	println("Distinct X/Y: ", dXN, dYN)

	binary.Write(vfFile, binary.BigEndian, dXN)
	binary.Write(vfFile, binary.BigEndian, dYN)

	for _, gridVertex := range gridVertices {
		var robotObstructed uint8 = 0

		for _, fieldVertex := range fieldVertices {
			if withinBox(gridVertex, fieldVertex, ROBOT_WIDTH_CM/2, ROBOT_HEIGHT_CM, ROBOT_DEPTH_CM/2) {
				robotObstructed = 1
				break
			}
		}

		binary.Write(vfFile, binary.BigEndian, robotObstructed)
		binary.Write(vfFile, binary.BigEndian, gridVertex.X)
		binary.Write(vfFile, binary.BigEndian, gridVertex.Y)

		if robotObstructed == uint8(0) {
			fmt.Fprintf(objFile, "v %f %f %f\n", gridVertex.X, gridVertex.Y, gridVertex.Z)
		}
	}

	println("Wrote virtual field files")
}

// Function to check if a vertex is within the specified box range
func withinBox(v1, v2 Vertex, boxWidth, boxHeight, boxDepth float64) bool {
	boxWidthMeters := boxWidth / 100.0   // Convert cm to meters
	boxHeightMeters := boxHeight / 100.0 // Convert cm to meters
	boxDepthMeters := boxDepth / 100.0   // Convert cm to meters

	return (v1.X-v2.X <= boxWidthMeters && v2.X-v1.X <= boxWidthMeters) &&
		(v1.Y-v2.Y <= boxHeightMeters && v2.Y-v1.Y <= boxHeightMeters) &&
		(v1.Z-v2.Z <= boxDepthMeters && v2.Z-v1.Z <= boxDepthMeters)
}

// Function to read vertices from a JSON file
func readVertices(filename string) (Vertices, error) {
	file, err := os.Open(filename)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var vertices []Vertex
	byteValue, _ := ioutil.ReadAll(file)
	if err := json.Unmarshal(byteValue, &vertices); err != nil {
		return nil, err
	}

	return Vertices(vertices), nil
}
