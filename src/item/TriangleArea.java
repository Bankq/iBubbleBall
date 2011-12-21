package item;

import logic.Position;

public interface TriangleArea {
	int vertexCnt = 3;
	
	void setVertex(Position[] vertex);
	Position[] getVertex();
	public void updateVertex(Position offset);
	public void rotateVertex(double angle);
}
