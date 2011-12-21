package gui;

import java.io.IOException;

import logic.Position;

import static org.lwjgl.opengl.GL11.*;

/**
 * Implementation of sprite that uses an OpenGL quad and a texture
 * to render a given image to the screen.
 */
public class Sprite {

	/** The texture that stores the image for this sprite */
	private Texture	texture;

	/** The width in pixels of this sprite */
	private int			width;

	/** The height in pixels of this sprite */
	private int			height;

	/**
	 * Create a new sprite from a specified image.
	 *
	 * @param loader the texture loader to use
	 * @param ref A reference to the image on which this sprite should be based
	 */
	public Sprite(TextureLoader loader, String ref) {
    try {
			texture = loader.getTexture(ref);
      width = texture.getImageWidth();
      height = texture.getImageHeight();
    } catch (IOException ioe) {
    	ioe.printStackTrace();
      System.exit(-1);
    }
	}

	/**
	 * Get the width of this sprite in pixels
	 *
	 * @return The width of this sprite in pixels
	 */
	public int getWidth() {
		return texture.getImageWidth();
	}

	/**
	 * Get the height of this sprite in pixels
	 *
	 * @return The height of this sprite in pixels
	 */
	public int getHeight() {
		return texture.getImageHeight();
	}

	/**
	 * Draw the sprite at the specified location
	 *
	 * @param x The x location at which to draw this sprite
	 * @param y The y location at which to draw this sprite
	 */
	public void draw(int x, int y) {
		// store the current model matrix
		glPushMatrix();

		// bind to the appropriate texture for this sprite
		texture.bind();

		// translate to the right location and prepare to draw
		glTranslatef(x, y, 0);

		// draw a quad textured to match the sprite
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			glTexCoord2f(0, texture.getHeight());
			glVertex2f(0, height);

			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width, height);

			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width, 0);
		}
		glEnd();
	    //glFlush();

		// restore the model view matrix to prevent contamination
		glPopMatrix();
	}
	
	public void drawTriangle(Position[] vertex){
		glPushMatrix();
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		texture.bind();
		//glTranslatef(vertex[0].getX(), vertex[0].getY(), 0);
		glBegin(GL_TRIANGLES);
		{
			glTexCoord2f(0, 0);
			glVertex2f(vertex[0].getX(), vertex[0].getY());
			glTexCoord2f(0, texture.getHeight());
			glVertex2f(vertex[1].getX(), vertex[1].getY());
			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(vertex[2].getX(), vertex[2].getY());
		}
		glEnd();
	   // glFlush();

		
		glPopMatrix();
		
	}

	public void drawRectangle(Position[] vertex) {		
		glPushMatrix();

		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		texture.bind();
		//glTranslatef(vertex[0].getX(), vertex[0].getY(), 0);

	// draw a quad textured to match the sprite
	glBegin(GL_QUADS);
	{
		glTexCoord2f(0, 0);
		glVertex2f(vertex[0].getX(), vertex[0].getY());
		
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(vertex[1].getX(),vertex[1].getY() );

		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(vertex[2].getX(), vertex[2].getY());

		glTexCoord2f(0,texture.getHeight());
		glVertex2f(vertex[3].getX(), vertex[3].getY());
	}
	glEnd();
    //glFlush();

	// restore the model view matrix to prevent contamination
	glPopMatrix();
		
	}

	public void drawCircle(Position position, int radius) {
		// TODO Auto-generated method stub 
		glPushMatrix();
		//glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	    //glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		texture.bind();
		//glTranslatef(position.getX(), position.getY(), 0);

	 

		float delta_angle = (float)(2.0*(Math.PI)/1000);
		 
		float texcoordX = 0;
		float texcoordY = 0;
		float[] vertex = new float[4];
		float x = position.getX();
		float y = position.getY();
		glBegin(GL_TRIANGLE_FAN);
		{
		//draw the vertex at the center of the circle
			texcoordX = (float)0.38;
			texcoordY = (float)0.38;
			glTexCoord2f(texcoordX,texcoordY);
			vertex[0] = vertex[1] = vertex[2] = (float)0.0;	
			vertex[3] = (float)1.0;        
			glVertex4f(x+vertex[0],y+vertex[1],vertex[2],vertex[3]);

		//draw the vertex on the contour of the circle
		for(int i = 0; i < 1000 ; i++)
		{
			texcoordX = (float)((Math.cos(delta_angle*i) + 1.0)*0.38);
			texcoordY = (float)((Math.sin(delta_angle*i) + 1.0)*0.38);
			glTexCoord2f(texcoordX,texcoordY);

			vertex[0] = (float)(Math.cos(delta_angle*i) * radius);
			vertex[1] = (float)(Math.sin(delta_angle*i) * radius);
			vertex[2] = (float)0.0;
			vertex[3] = (float)1.0;
			glVertex4f(x+vertex[0],y+vertex[1],vertex[2],vertex[3]);
		}

		texcoordX = (float)((1.0 + 1.0)*0.5);
		texcoordY = (float)((0.0 + 1.0)*0.5);
		glTexCoord2f(texcoordX,texcoordY);

		vertex[0] = (float)1.0 * radius;
		vertex[1] = (float)0.0 * radius;
		vertex[2] = (float)0.0;
		vertex[3] = (float)1.0;
		glVertex4f(x+vertex[0],y+vertex[1],vertex[2],vertex[3]);
		glEnd();

		  
	    glPopMatrix();
		}
		
	}

	public void drawWoodenSeesaw(Position[] vertex) {
		// TODO Auto-generated method stub
		
		Position[] triangleVertex = new Position[3];
		Position[] rectangleVertex = new Position[4];
		
		triangleVertex[0] = vertex[0];
		triangleVertex[1] = vertex[1];
		triangleVertex[2] = vertex[6];
		
		rectangleVertex[0] = vertex[2];
		rectangleVertex[1] = vertex[3];
		rectangleVertex[2] = vertex[4];
		rectangleVertex[3] = vertex[5];
		
		drawTriangle(triangleVertex);
		drawRectangle(rectangleVertex);
		
		
		
		/*glPushMatrix();

		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		texture.bind();
		//glTranslatef(vertex[0].getX(), vertex[0].getY(), 0);

	// draw a quad textured to match the sprite
	glBegin(GL_POLYGON);
	{
		glTexCoord2f((float)0.5, 0);
		glVertex2f(vertex[0].getX(), vertex[0].getY());

		glTexCoord2f((float)0.6, (float)0.33);
		glVertex2f(vertex[1].getX(),vertex[1].getY() );

		glTexCoord2f(1, (float)0.33);
		glVertex2f(vertex[2].getX(), vertex[2].getY());

		glTexCoord2f(1,1);
		glVertex2f(vertex[3].getX(), vertex[3].getY());
		 
		glTexCoord2f(0, 1);
		glVertex2f(vertex[4].getX(), vertex[4].getY());
		
		glTexCoord2f(0,(float)0.33);
		glVertex2f(vertex[5].getX(), vertex[5].getY());
		
		glTexCoord2f((float)0.4,(float)0.33);
		glVertex2f(vertex[6].getX(), vertex[6].getY());
	}
	glEnd();
    //glFlush();

	// restore the model view matrix to prevent contamination
	glPopMatrix();*/
		
	}
}