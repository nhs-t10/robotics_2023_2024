import bpy
import json


# Function to get vertices of an object by name
def get_vertices_by_name(object_name):
    obj = bpy.data.objects.get(object_name)

    if obj is not None and obj.type == 'MESH':
        mesh = obj.data
        vertices = [obj.matrix_world @ vertex.co for vertex in mesh.vertices]
        return list(map(lambda vertex: {"x": vertex.x, "y": vertex.y, "z": vertex.z}, vertices))
    else:
        return None


grid_vertices = get_vertices_by_name("Grid")  # REPLACE "Grid" with the OBJECT NAME of your GRID OBJECT
field_vertices = get_vertices_by_name("obj1")  # REPLACE "obj1" with the OBJECT NAME of the FIELD OBJECT that HAS ITS FLOOR VERTICES REMOVED

with open("/tmp/grid.json", 'wt') as fp:
    json.dump(grid_vertices, fp)

with open("/tmp/field.json", 'wt') as fp:
    json.dump(field_vertices, fp)
