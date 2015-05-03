package agilec.ikeaswipe;

/**
 * @author @emmaforsling @martingrad
 * Taken from http://github.com/leedavey/TextureModelDemo
 */

import java.nio.ByteOrder;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Class DrawModel
 */
public class DrawModel {
  private final int NUM_FACE_VERTICES = 3;            // three vertices for one face
  private final int NUM_VERTEX_COORDS = 3;            // x,y,z for one vertex
  private final int NUM_TEX_COORDS = 2;               // u,v (2D texture)

  // Define buffers for the vertex, texture and index.
  private final FloatBuffer mVertexBuffer;
  private final ShortBuffer mIndexBuffer;
  private final FloatBuffer mTexBuffer;

  /**
   * DrawModel creates arrays for vertices, textures and faces.
   * It loads an .obj file and stores the information in the corresponding arrays.
   *
   * @param context
   * @param resId
   * @author @emmaforsling @martingrad
   */
  public DrawModel(Context context, int resId) {

    // read in all the lines and put in their respective arraylists of strings
    // reason I do this is to get a count of the faces to be used to initialize the
    // float arrays

    ArrayList<String> vertexes = new ArrayList<>();
    ArrayList<String> textures = new ArrayList<>();
    ArrayList<String> faces = new ArrayList<>();

    // Create input stream to .obj file.
    InputStream iStream = context.getResources().openRawResource(resId);
    InputStreamReader isr = new InputStreamReader(iStream);
    BufferedReader bReader = new BufferedReader(isr);
    String line;
    try {
      // Read the file line by line, and store the information depending on the first
      // characters of each line, e.g. "v" - vertices
      while ((line = bReader.readLine()) != null) {
        // do not read in the leading v, vt or f
        if (line.startsWith("v ")) vertexes.add(line.substring(2));
        if (line.startsWith("vt ")) textures.add(line.substring(3));
        if (line.startsWith("f ")) faces.add(line.substring(2));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Allocate arrays holding the vertices, texture coords and indexes that will be
    // used to create the final buffers used in the rendering.
    // vCoords - vertex coordinates,  will be [x1, y1, z1, x2, y2, z2, ...]
    // tCoords - texture coordinates, will be [u1, v1, u2, v2, ...]
    // iCoords - indices,             will be [0, 1, 2, 3, ...]
    float[] vCoords = new float[faces.size() * NUM_FACE_VERTICES * NUM_VERTEX_COORDS];
    float[] tCoords = new float[faces.size() * NUM_FACE_VERTICES * NUM_TEX_COORDS];
    short[] iCoords = new short[faces.size() * NUM_FACE_VERTICES];

    int vertexIndex = 0;
    int faceIndex = 0;
    int textureIndex = 0;
    // For each face (each String in ArrayList faces)
    for (String i : faces) {
      // For each face component (each "word" within the face, separated by a whitespace)
      for (String j : i.split(" ")) {
        iCoords[faceIndex] = (short) faceIndex++;
        // Split "words" by "/" (vertex index/texture coordinate in the .obj file)
        String[] faceComponent = j.split("/");

        // Use the element to the left of "/" to retrieve a 3D vertex and store
        // it as a String (-1 to start at 0 in the array)
        String vertex = vertexes.get(Integer.parseInt(faceComponent[0]) - 1);

        // Use the element to the right of "/" to retrieve a 2D texture coordinate and store
        // it as a String (-1 to start at 0 in the array)
        String texture = textures.get(Integer.parseInt(faceComponent[1]) - 1);

        // Split the Strings and store them as arrays
        String vertexComp[] = vertex.split(" ");
        String textureComp[] = texture.split(" ");

        // Loop through the arrays and parse the values to floats and store them
        // in vCoords and tCoords.
        for (String v : vertexComp) {
          vCoords[vertexIndex++] = Float.parseFloat(v);
          //System.out.println("vertex: " + v);
        }

        for (String t : textureComp) {
          tCoords[textureIndex++] = Float.parseFloat(t);
          //System.out.println("texture: " + t);
        }
      }
    }

    // create the final buffers
    mVertexBuffer = makeFloatBuffer(vCoords);
    mIndexBuffer = makeShortBuffer(iCoords);
    mTexBuffer = makeFloatBuffer(tCoords);

  }

  /**
   * makeFloatBuffer creates a float buffer from a float array
   *
   * @param arr
   * @return
   * @author @emmaforsling @martingrad
   */
  private FloatBuffer makeFloatBuffer(float[] arr) {
    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
    bb.order(ByteOrder.nativeOrder());
    FloatBuffer fb = bb.asFloatBuffer();
    fb.put(arr);
    fb.position(0);
    return fb;
  }

  /**
   * makeShortBuffer creates a short buffer from a short array
   *
   * @param arr
   * @return
   * @author @emmaforsling @martingrad
   */
  private ShortBuffer makeShortBuffer(short[] arr) {
    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
    bb.order(ByteOrder.nativeOrder());
    ShortBuffer ib = bb.asShortBuffer();
    ib.put(arr);
    ib.position(0);
    return ib;
  }

  /**
   * draw function, that uses vertex buffer, texture buffer and index buffer to render the frame.
   *
   * @param gl
   * @author @emmaforsling @martingrad
   */
  public void draw(GL10 gl) {
    gl.glFrontFace(GL10.GL_CCW);
    gl.glVertexPointer(NUM_VERTEX_COORDS, GL10.GL_FLOAT, 0, mVertexBuffer);
    gl.glTexCoordPointer(NUM_TEX_COORDS, GL10.GL_FLOAT, 0, mTexBuffer);


    //gl.glColor4f(0.f, 0.f, 0.f, 1.f);
    gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.remaining(), GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
  }
}
