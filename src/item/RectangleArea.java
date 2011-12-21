package item;

import logic.Position;

public interface RectangleArea {
	int vertexCnt = 4;
	
	void setVertex(Position[] vertex);
	Position[] getVertex();
	public void updateVertex(Position offset);
	public void rotateVertex(double angle);
}
