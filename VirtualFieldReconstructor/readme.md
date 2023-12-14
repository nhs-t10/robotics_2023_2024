# How To

## Prerequisites

- The field for the current year loaded into Blender.

## Steps

- Load the field in blender
- Create a grid in blender that is 100x100 vertices and 12ft x 12ft (3.6576x3.6576 meters)
- Make sure that both objects are centered around (0, 0). The FTC field center should be at (0, 0) and so should the grid
- Size the field object to the grid so that the grid lines up with the field in the field object
- Go into edit mode and manually remove the floor vertices of the FIELD object, NOT the grid
- In the "Scripting" tab of blender, add the export_vertices.py file AND EDIT the OBJECT NAMES
- Run the script
- Edit the near.go script constants to match the robot dimensions in CM 
- In the directory that has the blender script output files, run near.go (go run near.go)
- Load the resulting safe_vertices.obj into the blender file and make sure that the vertices make sense (you may need to set all rotation to 0 degrees since it imports flipped)
- Move field.vf into the resources directory and use VirtualField to load it (ex: `new VirtualField("/centerstage.vf")`)


# Documentation

## .vf format

### Header

uint32 - Number of points in X direction (Width)
uint32 - Number of points in Y direction (Height)

### Point

uint8 - Binary, 0 - not an obstacle/clear,  1 - obstacle/not clear 
float64 - X meters from the center of the FTC field
float64 - Y meters from the center of the FTC field

### Final format

There is one `Header`, then Width * Height `Point`s following `Header`